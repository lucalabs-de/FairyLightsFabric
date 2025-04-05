package de.lucalabs.fairylights.events;

import com.google.common.collect.Sets;
import de.lucalabs.fairylights.collision.Collidable;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class ClientEventHandler {
    private ClientEventHandler() {}

    @Nullable
    public static Connection getHitConnection() {
        final net.minecraft.util.hit.HitResult result = MinecraftClient.getInstance().crosshairTarget;
        if (result instanceof EntityHitResult) {
            final Entity entity = ((EntityHitResult) result).getEntity();
            if (entity instanceof HitConnection) {
                return ((HitConnection) entity).result.connection;
            }
        }
        return null;
    }

    @Nullable
    private static HitResult getHitConnection(final World world, final Entity viewer) {
        final Box bounds = new Box(viewer.getBlockPos()).expand(Connection.MAX_LENGTH + 1.0D);
        final Set<Fastener<?>> fasteners = collectFasteners(world, bounds);
        return getHitConnection(viewer, bounds, fasteners);
    }

    @Nullable
    private static HitResult getHitConnection(final Entity viewer, final Box bounds, final Set<Fastener<?>> fasteners) {
        if (fasteners.isEmpty()) {
            return null;
        }
        final Vec3d origin = viewer.getCameraPosVec(1);
        final Vec3d look = viewer.getRotationVector();
        final double reach = MinecraftClient.getInstance().interactionManager.getReachDistance();
        final Vec3d end = origin.add(look.x * reach, look.y * reach, look.z * reach);
        Connection found = null;
        Intersection rayTrace = null;
        double distance = Double.MAX_VALUE;
        for (final Fastener<?> fastener : fasteners) {
            for (final Connection connection : fastener.getOwnConnections()) {
                if (connection.getDestination().getType() == FastenerType.PLAYER) {
                    continue;
                }
                final Collidable collision = connection.getCollision();
                final Intersection result = collision.intersect(origin, end);
                if (result != null) {
                    final double dist = result.result().distanceTo(origin);
                    if (dist < distance) {
                        distance = dist;
                        found = connection;
                        rayTrace = result;
                    }
                }
            }
        }
        if (found == null) {
            return null;
        }
        return new HitResult(found, rayTrace);
    }

    public static void updateHitConnection() {
        final MinecraftClient mc = MinecraftClient.getInstance();
        final Entity viewer = mc.getCameraEntity();
        if (mc.crosshairTarget != null && mc.world != null && viewer != null) {
            final HitResult result = getHitConnection(mc.world, viewer);
            if (result != null) {
                final Vec3d eyes = viewer.getCameraPosVec(1.0F);
                if (result.intersection.result().distanceTo(eyes) < mc.crosshairTarget.getPos().distanceTo(eyes)) {
                    mc.crosshairTarget = new EntityHitResult(new HitConnection(mc.world, result));
                    mc.targetedEntity = null;
                }
            }
        }
    }

    private static Set<Fastener<?>> collectFasteners(final World world, final Box bounds) {
        final Set<Fastener<?>> fasteners = Sets.newLinkedHashSet();
        final CollectFastenersEvent event = new CollectFastenersEvent(world, bounds, fasteners);
        world.getNonSpectatingEntities(FenceFastenerEntity.class, bounds)
                .forEach(event::accept);
        final int minX = MathHelper.floor(bounds.minX / 16.0D);
        final int maxX = MathHelper.ceil(bounds.maxX / 16.0D);
        final int minZ = MathHelper.floor(bounds.minZ / 16.0D);
        final int maxZ = MathHelper.ceil(bounds.maxZ / 16.0D);
        final ChunkManager provider = world.getChunkManager();
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                final WorldChunk chunk = provider.getWorldChunk(x, z, false);
                if (chunk != null) {
                    event.accept(chunk);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(event);
        return fasteners;
    }

    static class HitConnection extends Entity {
        final ClientEventHandler.HitResult result;

        HitConnection(final World world, final ClientEventHandler.HitResult result) {
            super(EntityType.ITEM, world);
            this.setId(-1);
            this.result = result;
            this.setPosition(result.intersection.result());
        }

        @Override
        public boolean damage(final DamageSource source, final float amount) {
            if (source.getAttacker() == MinecraftClient.getInstance().player) {
                this.processAction(PlayerAction.ATTACK);
                return true;
            }
            return false;
        }

        @Override
        public ActionResult interact(final PlayerEntity player, final Hand hand) {
            if (player == MinecraftClient.getInstance().player) {
                this.processAction(PlayerAction.INTERACT);
                return ActionResult.SUCCESS;
            }
            return super.interact(player, hand);
        }

        private void processAction(final PlayerAction action) {
            this.result.connection.processClientAction(MinecraftClient.getInstance().player, action, this.result.intersection);
        }

//        @Override
//        public ItemStack getPickedResult(net.minecraft.util.hit.HitResult target) {
//            return this.result.connection.getItemStack();
//        }

        // TODO check if that does the same as the method above
        @Override
        public ItemStack getPickBlockStack() {
            return this.result.connection.getItemStack();
        }

        @Override
        protected void initDataTracker() {
        }

        @Override
        protected void writeCustomDataToNbt(final NbtCompound compound) {
        }

        @Override
        protected void readCustomDataFromNbt(final NbtCompound compound) {
        }

        @Override
        public Packet<ClientPlayPacketListener> createSpawnPacket() {
            return new Packet<>() {
                @Override
                public void write(final PacketByteBuf buf) {

                }

                @Override
                public void apply(final ClientPlayPacketListener p_131342_)
                {

                }
            };
        }
    }

    private record HitResult(Connection connection, Intersection intersection) {
    }
}
