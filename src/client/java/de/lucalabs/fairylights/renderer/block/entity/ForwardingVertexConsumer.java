package de.lucalabs.fairylights.renderer.block.entity;

import net.minecraft.client.render.VertexConsumer;

public abstract class ForwardingVertexConsumer implements VertexConsumer {
    protected abstract VertexConsumer delegate();

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return this.delegate().vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        return this.delegate().color(r, g, b, a);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return this.delegate().texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return this.delegate().overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return this.delegate().light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return this.delegate().normal(x, y, z);
    }

    @Override
    public void next() {
        this.delegate().next();
    }

    @Override
    public void fixedColor(int r, int g, int b, int a) {
        this.delegate().fixedColor(r, g, b, a);
    }

    @Override
    public void unfixColor() {
        this.delegate().unfixColor();
    }
}
