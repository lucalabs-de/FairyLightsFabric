package de.lucalabs.fairylights.feature.light;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.util.CodecUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FixedColorBehavior implements ColorLightBehavior {
    private final float red;
    private final float green;
    private final float blue;

    private final Identifier id = Identifier.of(FairyLights.ID, "beh_fixed_color");
    private final Codec<FixedColorBehavior> CODEC = Registry.register(
            FairyLightRegistries.BEHAVIOR_CODECS,
            id,
            CodecUtils.deserializeOnlyCodec(
                    Codec.FLOAT.fieldOf("red"),
                    Codec.FLOAT.fieldOf("green"),
                    Codec.FLOAT.fieldOf("blue"),
                    FixedColorBehavior::new
            ));

    public FixedColorBehavior(final float red, final float green, final float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static ColorLightBehavior create(final ItemStack stack) {
        final int rgb = DyeableItem.getColor(stack);
        final float red = (rgb >> 16 & 0xFF) / 255.0F;
        final float green = (rgb >> 8 & 0xFF) / 255.0F;
        final float blue = (rgb & 0xFF) / 255.0F;
        return new FixedColorBehavior(red, green, blue);
    }

    @Override
    public float getRed(final float delta) {
        return this.red;
    }

    @Override
    public float getGreen(final float delta) {
        return this.green;
    }

    @Override
    public float getBlue(final float delta) {
        return this.blue;
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
    }

    @Override
    public void tick(final World world, final Vec3d origin, final Light<?> light) {
    }

    @Override
    public Codec<? extends LightBehavior> getCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.fieldOf("red").forGetter(x -> this.red),
                Codec.FLOAT.fieldOf("green").forGetter(x -> this.green),
                Codec.FLOAT.fieldOf("blue").forGetter(x -> this.blue)
        ).apply(i, FixedColorBehavior::new));
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }
}
