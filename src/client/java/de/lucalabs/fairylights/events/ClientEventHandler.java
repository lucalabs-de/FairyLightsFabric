package de.lucalabs.fairylights.events;

import com.google.common.collect.Sets;
import de.lucalabs.fairylights.collision.Collidable;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.components.GenericComponent;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.net.serverbound.InteractionConnectionMessage;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.Curve;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
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
import org.joml.Vector3f;
import org.ladysnake.cca.api.v3.component.ComponentAccess;

import java.util.ConcurrentModificationException;
import java.util.Set;

public final class ClientEventHandler {
    private ClientEventHandler() {
    }

    public static void onDrawEntityHighlight(
            final Entity entity,
            final Camera camera,
            final float tickDelta,
            final MatrixStack matrix,
            final VertexConsumerProvider buf) {

        Vec3d pos = camera.getPos();

        if (entity instanceof FenceFastenerEntity) {
            drawFenceFastenerHighlight(
                    (FenceFastenerEntity) entity,
                    matrix,
                    buf.getBuffer(RenderLayer.getLines()),
                    tickDelta,
                    pos.x,
                    pos.y,
                    pos.z);
        } else if (entity instanceof final HitConnection hit) {
            if (hit.result.intersection.featureType() == Connection.CORD_FEATURE) {
                matrix.push();
                final Vec3d p = hit.result.connection.getFastener().getConnectionPoint();
                matrix.translate(p.x - pos.x, p.y - pos.y, p.z - pos.z);
                renderHighlight(hit.result.connection, matrix, buf.getBuffer(RenderLayer.getLines()));
                matrix.pop();
            } else {
                final Box bb = hit.result.intersection.hitBox().offset(-pos.x, -pos.y, -pos.z).expand(0.002D);
                WorldRenderer.drawBox(
                        matrix,
                        buf.getBuffer(RenderLayer.getLines()),
                        bb,
                        0.0F,
                        0.0F,
                        0.0F,
                        RenderConstants.HIGHLIGHT_ALPHA);
            }
        }
    }

    private static void drawFenceFastenerHighlight(
            final FenceFastenerEntity fence,
            final MatrixStack matrix,
            final VertexConsumer buf,
            final float delta,
            final double dx,
            final double dy,
            final double dz) {


        final PlayerEntity player = MinecraftClient.getInstance().player;
        // Check if the server will allow interaction
        if (player != null && (player.canSee(fence) || player.squaredDistanceTo(fence) <= 9.0D)) {
            final Box selection = fence.getBoundingBox().offset(-dx, -dy, -dz).expand(0.002D);
            WorldRenderer.drawBox(matrix, buf, selection, 0.0F, 0.0F, 0.0F, RenderConstants.HIGHLIGHT_ALPHA);
        }
    }

    private static void renderHighlight(final Connection connection, final MatrixStack matrix, final VertexConsumer buf) {
        final Curve cat = connection.getCatenary();
        if (cat == null) {
            return;
        }
        final Vector3f p = new Vector3f();
        final Vector3f v1 = new Vector3f();
        final Vector3f v2 = new Vector3f();
        final LineBuilder builder = new LineBuilder(matrix, buf);
        final float r = connection.getRadius() + 0.01F;
        for (int edge = 0; edge < 4; edge++) {
            p.set(cat.getX(0), cat.getY(0), cat.getZ(0));
            v1.set(cat.getDx(0), cat.getDy(0), cat.getDz(0));
            v1.normalize();
            v2.set(-v1.x(), -v1.y(), -v1.z());
            for (int n = 0; edge == 0 && n < 8; n++) {
                addVertex(builder, (n + 1) / 2 % 4, p, v1, v2, r);
            }
            addVertex(builder, edge, p, v1, v2, r);
            for (int i = 1; i < cat.getCount() - 1; i++) {
                p.set(cat.getX(i), cat.getY(i), cat.getZ(i));
                v2.set(-cat.getDx(i), -cat.getDy(i), -cat.getDz(i));
                v2.normalize();
                addVertex(builder, edge, p, v1, v2, r);
                addVertex(builder, edge, p, v1, v2, r);
                v1.set(-v2.x(), -v2.y(), -v2.z());
            }
            p.set(cat.getX(), cat.getY(), cat.getZ());
            v2.set(-v1.x(), -v1.y(), -v1.z());
            addVertex(builder, edge, p, v1, v2, r);
            for (int n = 0; edge == 0 && n < 8; n++) {
                addVertex(builder, (n + 1) / 2 % 4, p, v1, v2, r);
            }
        }
    }

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
        world.getNonSpectatingEntities(FenceFastenerEntity.class, bounds)
                .forEach(e -> collectFastenersAttachedToEntity(e, bounds, fasteners));
        final int minX = MathHelper.floor(bounds.minX / 16.0D);
        final int maxX = MathHelper.ceil(bounds.maxX / 16.0D);
        final int minZ = MathHelper.floor(bounds.minZ / 16.0D);
        final int maxZ = MathHelper.ceil(bounds.maxZ / 16.0D);
        final ChunkManager provider = world.getChunkManager();
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                final WorldChunk chunk = provider.getWorldChunk(x, z, false);
                if (chunk != null) {
                    try {
                        for (final BlockEntity e : chunk.getBlockEntities().values()) {
                            collectFastenersAttachedToEntity(e, bounds, fasteners);
                        }
                    } catch (final ConcurrentModificationException e) {
                        // RenderChunk's may find an invalid block entity while building and trigger a remove not on main thread
                    }
                }
            }
        }
        return fasteners;
    }

    private static void collectFastenersAttachedToEntity(final ComponentAccess e, final Box bounds, Set<Fastener<?>> fasteners) {
        FairyLightComponents.FASTENER.maybeGet(e).flatMap(GenericComponent::get).ifPresent(f -> {
            if (bounds.contains(f.getConnectionPoint())) {
                fasteners.add(f);
            }
        });
    }

    private static void addVertex(final LineBuilder builder, final int edge, final Vector3f p, final Vector3f v1, final Vector3f v2, final float r) {
        builder.accept(get(edge, p, v1, v2, r));
    }

    private static Vector3f get(final int edge, final Vector3f p, final Vector3f v1, final Vector3f v2, final float r) {
        final Vector3f up = new Vector3f();
        final Vector3f side = new Vector3f();
        // if collinear
        if (v1.dot(v2) < -(1.0F - 1.0e-2F)) {
            final float h = MathHelper.sqrt(v1.x() * v1.x() + v1.z() * v1.z());
            // if vertical
            if (h < 1.0e-2F) {
                up.set(-1.0F, 0.0F, 0.0F);
            } else {
                up.set(-v1.x() / h * -v1.y(), -h, -v1.z() / h * -v1.y());
            }
        } else {
            up.set(v2.x(), v2.y(), v2.z());
            up.lerp(v1, 0.5F);
        }
        up.normalize();
        side.set(v1.x(), v1.y(), v1.z());
        side.cross(up);
        side.normalize();
        side.mul(edge == 0 || edge == 3 ? -r : r);
        up.mul(edge < 2 ? -r : r);
        up.add(side);
        up.add(p);
        return up;
    }

    static class LineBuilder {
        final MatrixStack matrix;
        final VertexConsumer buf;
        Vector3f last;

        LineBuilder(MatrixStack matrix, VertexConsumer buf) {
            this.matrix = matrix;
            this.buf = buf;
        }

        void accept(Vector3f pos) {
            if (this.last == null) {
                this.last = pos;
            } else {
                Vector3f n = new Vector3f(pos);
                n.sub(this.last);
                n.normalize();
                n = this.matrix.peek().getNormalMatrix().transform(n);
                this.buf.vertex(this.matrix.peek().getPositionMatrix(), this.last.x(), this.last.y(), this.last.z())
                        .color(0.0F, 0.0F, 0.0F, RenderConstants.HIGHLIGHT_ALPHA)
                        .normal(n.x(), n.y(), n.z())
                        .next();
                this.buf.vertex(this.matrix.peek().getPositionMatrix(), pos.x(), pos.y(), pos.z())
                        .color(0.0F, 0.0F, 0.0F, RenderConstants.HIGHLIGHT_ALPHA)
                        .normal(n.x(), n.y(), n.z())
                        .next();
                this.last = null;
            }
        }
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
            ClientPlayNetworking.send(
                    InteractionConnectionMessage.ID,
                    new InteractionConnectionMessage(this.result.connection, action, this.result.intersection));
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
        protected void initDataTracker(DataTracker.Builder builder) {
        }

        @Override
        protected void writeCustomDataToNbt(final NbtCompound compound) {
        }

        @Override
        protected void readCustomDataFromNbt(final NbtCompound compound) {
        }
    }

    private record HitResult(Connection connection, Intersection intersection) {
    }
}
