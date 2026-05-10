package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public abstract class ForwardingVertexConsumer implements VertexConsumer {
    protected abstract VertexConsumer delegate();

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        return this.delegate().addVertex(x, y, z);
    }

    @Override
    public @NotNull VertexConsumer setColor(int r, int g, int b, int a) {
        return this.delegate().setColor(r, g, b, a);
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        return this.delegate().setUv(u, v);
    }

    @Override
    public @NotNull VertexConsumer setUv1(int u, int v) {
        return this.delegate().setUv1(u, v);
    }

    @Override
    public @NotNull VertexConsumer setUv2(int u, int v) {
        return this.delegate().setUv2(u, v);
    }

    @Override
    public @NotNull VertexConsumer setNormal(float x, float y, float z) {
        return this.delegate().setNormal(x, y, z);
    }
}
