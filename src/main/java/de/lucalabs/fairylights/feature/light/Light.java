package de.lucalabs.fairylights.feature.light;

import de.lucalabs.fairylights.feature.HangingFeature;
import de.lucalabs.fairylights.items.LightVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class Light<T extends LightBehavior> extends HangingFeature {
    private static final int SWAY_RATE = 10;
    private static final int SWAY_PEAK_COUNT = 5;
    private static final int SWAY_CYCLE = SWAY_RATE * SWAY_PEAK_COUNT;

    private final ItemStack item;
    private final LightVariant<T> variant;
    private final LightBehavior behavior;

    private int sway;
    private boolean swaying;
    private boolean swayDirection;
    private int tick;

    private boolean powered;

    public Light(final int index, final Vec3d point, final float yaw, final float pitch, final ItemStack item, final LightVariant<T> variant, final float descent) {
        super(index, point, yaw, pitch, 0.0F, descent);
        this.item = item;
        this.variant = variant;
        this.behavior = variant.createBehavior(item);
    }

    public LightBehavior getBehavior() {
        return this.behavior;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public LightVariant<T> getVariant() {
        return this.variant;
    }

    public void startSwaying(final boolean swayDirection) {
        this.swayDirection = swayDirection;
        this.swaying = true;
        this.sway = 0;
    }

    public void stopSwaying() {
        this.sway = 0;
        this.roll = 0.0F;
        this.swaying = false;
    }

    public void power(final boolean powered, final boolean now) {
        this.behavior.power(powered, now, this);
        this.powered = powered;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void tick(final World world, final Vec3d origin) {
        super.tick(world);
        this.behavior.tick(world, origin, this);
        if (this.swaying) {
            if (this.sway >= SWAY_CYCLE) {
                this.stopSwaying();
            } else {
                this.roll = (float) (Math.sin((this.swayDirection ? 1 : -1) * 2 * Math.PI / SWAY_RATE * this.sway) * Math.pow(180 / Math.PI * 2, -this.sway / (float) SWAY_CYCLE));
                this.sway++;
            }
        }
        this.tick++;
    }

    @Override
    public Box getBounds() {
        return this.getVariant().getBounds();
    }

    @Override
    public boolean parallelsCord() {
        return this.getVariant().parallelsCord();
    }
}
