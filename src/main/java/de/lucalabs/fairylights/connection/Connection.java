package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.collision.Collidable;
import de.lucalabs.fairylights.collision.CollidableList;
import de.lucalabs.fairylights.collision.FeatureCollisionTree;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.feature.Feature;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.items.ConnectionItem;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.util.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class Connection {
    public static final int MAX_LENGTH = 32;
    public static final double PULL_RANGE = 5;
    public static final float MAX_SLACK = 3;

    public static final FeatureType CORD_FEATURE = FeatureType.register("cord");

    private static final CubicBezier SLACK_CURVE = new CubicBezier(0.495F, 0.505F, 0.495F, 0.505F);

    protected final Fastener<?> fastener;
    private final ConnectionType<?> type;
    private final UUID uuid;
    protected World world;
    @Nullable
    protected Curve prevCatenary;
    protected float slack = 1;
    private FastenerAccessor destination;
    @Nullable
    private FastenerAccessor prevDestination;
    @Nullable
    private Curve catenary;
    private Collidable collision = Collidable.empty();

    private boolean updateCatenary;

    private int prevStretchStage;

    private boolean removed;

    private boolean drop;

    public Connection(final ConnectionType<?> type, final World world, final Fastener<?> fastener, final UUID uuid) {
        this.type = type;
        this.world = world;
        this.fastener = fastener;
        this.uuid = uuid;
        this.computeCatenary();
    }

    public ConnectionType<?> getType() {
        return this.type;
    }

    @Nullable
    public final Curve getCatenary() {
        return this.catenary;
    }

    @Nullable
    public final Curve getPrevCatenary() {
        return this.prevCatenary == null ? this.catenary : this.prevCatenary;
    }

    public final World getWorld() {
        return this.world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    public final Collidable getCollision() {
        return this.collision;
    }

    public final Fastener<?> getFastener() {
        return this.fastener;
    }

    public final UUID getUUID() {
        return this.uuid;
    }

    public final FastenerAccessor getDestination() {
        return this.destination;
    }

    public final void setDestination(final Fastener<?> destination) {
        this.prevDestination = this.destination;
        this.destination = destination.createAccessor();
        this.computeCatenary();
    }

    public boolean isDestination(final FastenerAccessor location) {
        return this.destination.equals(location);
    }

    public void setDrop() {
        this.drop = true;
    }

    public void noDrop() {
        this.drop = false;
    }

    public boolean shouldDrop() {
        return this.drop;
    }

    public ItemStack getItemStack() {
        final ItemStack stack = new ItemStack(this.getType().getItem());
        stack.applyComponentsFrom(this.serializeLogic().build().toComponents());
        return stack;
    }

    public float getRadius() {
        return 0.0625F;
    }

    public final boolean isDynamic() {
        return this.fastener.isMoving() || this.destination.get(this.world, false).filter(Fastener::isMoving).isPresent();
    }

    public final boolean isModifiable(final PlayerEntity player) {
        return this.world.canPlayerModifyAt(player, this.fastener.getPos());
    }

    public final void remove() {
        if (!this.removed) {
            this.removed = true;
            this.onRemove();
        }
    }

    public final boolean isRemoved() {
        return this.removed;
    }

    public void computeCatenary() {
        this.updateCatenary = true;
    }

    public void disconnect(final PlayerEntity player, final Vec3d hit) {
        this.destination.get(this.world).ifPresent(f -> this.disconnect(f, hit));
    }

    private void disconnect(final Fastener<?> destinationFastener, final Vec3d hit) {
        this.fastener.removeConnection(this);
        destinationFastener.removeConnection(this.uuid);
        if (this.shouldDrop()) {
            final ItemStack stack = this.getItemStack();
            final ItemEntity item = new ItemEntity(this.world, hit.x, hit.y, hit.z, stack);
            final float scale = 0.05F;
            item.setVelocity(
                    this.world.random.nextGaussian() * scale,
                    this.world.random.nextGaussian() * scale + 0.2F,
                    this.world.random.nextGaussian() * scale
            );
            this.world.spawnEntity(item);
        }
        this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.CORD_DISCONNECT, SoundCategory.BLOCKS, 1, 1);
    }

    public boolean reconnect(final Fastener<?> destination) {
        return this.fastener.reconnect(this.world, this, destination);
    }

    public boolean interact(
            final PlayerEntity player,
            final Vec3d hit,
            final FeatureType featureType,
            final int feature,
            final ItemStack heldStack,
            final Hand hand) {

        final Item item = heldStack.getItem();
        if (item instanceof ConnectionItem && !this.matches(heldStack)) {
            return this.replace(player, hit, heldStack);
        } else if (heldStack.isOf(Items.STRING)) {
            return this.slacken(hit, heldStack, 0.2F);
        } else if (heldStack.isOf(Items.STICK)) {
            return this.slacken(hit, heldStack, -0.2F);
        }
        return false;
    }

    public boolean matches(final ItemStack stack) {
        return this.serializeLogic().build().matchesItemStack(stack);
    }

    private boolean replace(final PlayerEntity player, final Vec3d hit, final ItemStack heldStack) {
        return this.destination.get(this.world).map(dest -> {
            this.fastener.removeConnection(this);
            dest.removeConnection(this.uuid);
            if (this.shouldDrop()) {
                ItemHelper.giveItemToPlayer(player, this.getItemStack());
            }
            final ConnectionType<? extends Connection> type = ((ConnectionItem) heldStack.getItem()).getConnectionType();
            final Connection conn = this.fastener.connect(this.world, dest, type, ComponentRecords.ConnectionLogic.fromItemStack(heldStack), true);
            conn.slack = this.slack;
            conn.onConnect(player.getWorld(), player, heldStack);
            heldStack.decrement(1);
            this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.CORD_CONNECT, SoundCategory.BLOCKS, 1, 1);
            return true;
        }).orElse(false);
    }

    private boolean slacken(final Vec3d hit, final ItemStack heldStack, final float amount) {
        if (this.slack <= 0 && amount < 0 || this.slack >= MAX_SLACK && amount > 0) {
            return true;
        }
        this.slack = MathHelper.clamp(this.slack + amount, 0, MAX_SLACK);
        if (this.slack < 1e-2F) {
            this.slack = 0;
        }
        this.computeCatenary();
        this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.CORD_STRETCH, SoundCategory.BLOCKS, 1, 0.8F + (MAX_SLACK - this.slack) * 0.4F);
        return true;
    }

    public void onConnect(final World world, final PlayerEntity user, final ItemStack heldStack) {
    }

    protected void onRemove() {
    }

    protected void onUpdate() {
    }

    protected void onCalculateCatenary(final boolean relocated) {
    }

    public final boolean update(final Vec3d from) {
        this.prevCatenary = this.catenary;
        final boolean changed = this.destination.get(this.world, false).map(dest -> {
            final Vec3d point = dest.getConnectionPoint();
            final boolean c = this.updateCatenary(from, dest, point);
            this.onUpdate();
            final double dist = point.distanceTo(from);
            final double pull = dist - MAX_LENGTH + PULL_RANGE;
            if (pull > 0) {
                final int stage = (int) (pull + 0.1F);
                if (stage > this.prevStretchStage) {
                    this.world.playSound(null, point.x, point.y, point.z, FairyLightSounds.CORD_STRETCH, SoundCategory.BLOCKS, 0.25F, 0.5F + stage / 8F);
                }
                this.prevStretchStage = stage;
            }
            if (dist > MAX_LENGTH + PULL_RANGE) {
                this.world.playSound(null, point.x, point.y, point.z, FairyLightSounds.CORD_SNAP, SoundCategory.BLOCKS, 0.75F, 0.8F + this.world.random.nextFloat() * 0.3F);
                this.remove();
            } else if (dest.isMoving()) {
                dest.resistSnap(from);
            }
            return c;
        }).orElse(false);

        if (this.destination.isGone(this.world)) {
            this.remove();
        }

        return changed;
    }

    private boolean updateCatenary(final Vec3d from, final Fastener<?> dest, final Vec3d point) {
        if (this.updateCatenary || this.isDynamic()) {
            final Vec3d vec = point.subtract(from);
            if (vec.length() > 1e-6) {
                final Direction facing = this.fastener.getFacing();
                if (this.fastener instanceof FenceFastener && dest instanceof FenceFastener && vec.horizontalLength() < 1e-2) {
                    this.catenary = this.verticalHelix(vec);
                } else {
                    this.catenary = Catenary.from(
                            vec,
                            facing.getAxis() == Direction.Axis.Y
                                    ? 0.0F
                                    : (float) Math.toRadians(90.0F + facing.asRotation()),
                            SLACK_CURVE,
                            this.slack);
                }
                this.onCalculateCatenary(!this.destination.equals(this.prevDestination));
                final CollidableList.Builder bob = new CollidableList.Builder();
                this.addCollision(bob, from);
                this.collision = bob.build();
            }
            this.updateCatenary = false;
            this.prevDestination = this.destination;
            return true;
        }
        return false;
    }

    private Curve verticalHelix(final Vec3d vec) {
        final float length = (float) vec.length();
        final float height = (float) vec.y;
        final float stepSize = 0.25F;
        final float loopsPerBlock = 1.0F;
        final float radius = 0.33F;
        final int steps = (int) (MathHelper.TAU * radius * loopsPerBlock * length / stepSize);
        final float rad = -MathHelper.TAU * (loopsPerBlock * length);
        final float[] x = new float[steps];
        final float[] y = new float[steps];
        final float[] z = new float[steps];
        float helixLength = 0.0F;
        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            x[i] = radius * MathHelper.cos(t * rad);
            y[i] = t * height;
            z[i] = radius * MathHelper.sin(t * rad);
            if (i > 0) {
                helixLength += MathHelper.sqrt(
                        MathHelper.square(x[i] - x[i - 1]) +
                                MathHelper.square(y[i] - y[i - 1]) +
                                MathHelper.square(z[i] - z[i - 1]));
            }
        }
        return new Curve3D(steps, x, y, z, helixLength);
    }

    public void addCollision(final CollidableList.Builder collision, final Vec3d origin) {
        if (this.catenary == null) {
            return;
        }
        final int count = this.catenary.getCount();
        if (count <= 2) {
            return;
        }
        final float r = this.getRadius();
        final Catenary.SegmentIterator it = this.catenary.iterator();
        final Box[] bounds = new Box[count - 1];
        int index = 0;
        while (it.next()) {
            final float x0 = it.getX(0.0F);
            final float y0 = it.getY(0.0F);
            final float z0 = it.getZ(0.0F);
            final float x1 = it.getX(1.0F);
            final float y1 = it.getY(1.0F);
            final float z1 = it.getZ(1.0F);
            bounds[index++] = new Box(
                    origin.x + x0, origin.y + y0, origin.z + z0,
                    origin.x + x1, origin.y + y1, origin.z + z1
            ).expand(r);
        }
        collision.add(FeatureCollisionTree.build(CORD_FEATURE, i -> Segment.INSTANCE, i -> bounds[i], 1, bounds.length - 2));
    }

    public void deserialize(final Fastener<?> destination, @NotNull final ComponentRecords.ConnectionLogic logic, final boolean drop) {
        this.destination = destination.createAccessor();
        this.drop = drop;
        this.deserializeLogic(logic);
    }

    public ComponentRecords.ConnectionStatus.Builder serialize() {
        ComponentRecords.ConnectionStatus.Builder status = new ComponentRecords.ConnectionStatus.Builder();
        return status
                .destination(this.destination)
                .logic(this.serializeLogic().build())
                .slack(this.slack)
                .drop(this.drop);
    }

    public void deserialize(final ComponentRecords.ConnectionStatus status) {
        this.destination = status.destination().accessor();
        this.deserializeLogic(status.logic());
        this.slack = status.slack();
        this.drop = status.drop();
        this.updateCatenary = true;
    }

    public ComponentRecords.ConnectionLogic.Builder serializeLogic() {
        return new ComponentRecords.ConnectionLogic.Builder();
    }

    public void deserializeLogic(final ComponentRecords.ConnectionLogic logic) {
    }

    static class Segment implements Feature {
        static final Segment INSTANCE = new Segment();

        @Override
        public int getId() {
            return 0;
        }
    }
}
