package de.lucalabs.fairylights.feature;

import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Pennant extends HangingFeature {
    private final int color;

    private final Item item;

    public Pennant(final int index, final Vec3d point, final float yaw, final float pitch, final int color, final Item item) {
        super(index, point, yaw, pitch, 0.0F, 0.0F);
        this.color = color;
        this.item = item;
    }

    public int getColor() {
        return this.color;
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public Box getBounds() {
        return new Box(-0.22D, -0.5D, -0.02D, 0.22D, 0.0D, 0.02D);
    }

    @Override
    public boolean parallelsCord() {
        return true;
    }
}
