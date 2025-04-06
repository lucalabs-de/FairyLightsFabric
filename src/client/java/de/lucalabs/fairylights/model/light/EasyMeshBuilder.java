package de.lucalabs.fairylights.model.light;

import net.minecraft.client.model.*;

import java.util.ArrayList;
import java.util.List;

public class EasyMeshBuilder {

    private final String name;
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    private final ModelPartBuilder cubes;
    private final List<EasyMeshBuilder> children;

    public EasyMeshBuilder(final String name) {
        this.name = name;
        this.cubes = new ModelPartBuilder();
        this.children = new ArrayList<>();
    }

    public EasyMeshBuilder(final String name, final int u, final int v) {
        this(name);
        this.setTextureOffset(u, v);
    }

    public EasyMeshBuilder setTextureOffset(int u, int v) {
        this.cubes.uv(u, v);
        return this;
    }

    public EasyMeshBuilder addBox(float x, float y, float z, float width, float height, float depth) {
        this.cubes.cuboid(x, y, z, width, height, depth);
        return this;
    }

    public EasyMeshBuilder addBox(float x, float y, float z, float width, float height, float depth, float expand) {
        this.cubes.cuboid(x, y, z, width, height, depth, new Dilation(expand));
        return this;
    }

    public void addChild(EasyMeshBuilder child) {
        this.children.add(child);
    }

    public void setRotationPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotationAngles(float x, float y, float z) {
        this.xRot = x;
        this.yRot = y;
        this.zRot = z;
    }

    public void build(final ModelPartData parent) {
        ModelPartData part = parent.addChild(this.name,
                this.cubes,
                ModelTransform.of(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot));
        for (EasyMeshBuilder child : this.children) {
            child.build(part);
        }
    }

    public TexturedModelData build(final int xTexSize, final int yTexSize) {
        ModelData mesh = new ModelData();
        this.build(mesh.getRoot());
        return TexturedModelData.of(mesh, xTexSize, yTexSize);
    }
}
