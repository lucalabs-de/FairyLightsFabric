package de.lucalabs.fairylights.main.feature.light;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class TwinkleLogic {
    private final float chance;

    private final int duration;

    private int time = -1;

    private int prevTime = -1;

    public TwinkleLogic(final float chance, final int duration) {
        this.chance = chance;
        this.duration = duration;
    }

    public float get(final float delta) {
        return this.time == -1 ? 0.0F : Mth.lerp(delta, (float) this.prevTime, (float) this.time) / this.duration;
    }

    public void tick(final RandomSource rng, final boolean powered) {
        this.prevTime = this.time;
        if (this.time != -1 || rng.nextFloat() < this.chance) this.time++;
        if (this.time >= this.duration || !powered) this.time = -1;
    }
}
