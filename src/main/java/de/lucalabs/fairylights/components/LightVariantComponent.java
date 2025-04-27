package de.lucalabs.fairylights.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.feature.light.IncandescentBehavior;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.feature.light.TorchLightBehavior;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.TransientComponent;

import java.util.Optional;

// this is absolutely atrocious code, I know. The whole light feature architecture should probably be rewritten
// instead of forcing it into these new Components
public class LightVariantComponent {
    private final ComponentType<Data> componentType;
    private final ItemStack stack;

    public LightVariantComponent(ComponentType<Data> componentType, ItemStack stack) {
        this.stack = stack;
        this.componentType = componentType;
    }

    public LightVariantComponent setLightVariantFor(ItemStack stack, LightVariant<?> variant) {
        if (variant instanceof SimpleLightVariant<?> slVariant) {
            this.stack.set(this.componentType, toData(stack, slVariant));
            return this;
        } else {
            throw new RuntimeException("The light variant component currently only works for SimpleLightVariant");
        }
    }

    public static Optional<LightVariant<?>> get(ItemStack stack) {
        return Optional.ofNullable(stack.get(FairyLightComponents.LIGHT_VARIANT)).map(LightVariantComponent::fromData);
    }

    private static Data toData(ItemStack stack, SimpleLightVariant<?> variant)  {
        LightBehavior b = variant.createBehavior(stack);
        LightBehaviourTag tag;

        if (b instanceof IncandescentBehavior) {
            tag = LightBehaviourTag.INCANDESCENT;
        } else if (b instanceof TorchLightBehavior) {
            tag = LightBehaviourTag.INCANDESCENT;
        } else {
            tag = LightBehaviourTag.STANDARD;
        }

        return new Data(
                variant.parallelsCord(),
                variant.getSpacing(),
                variant.getBounds(),
                variant.getFloorOffset(),
                tag,
                variant.isOrientable()
        );
    }

    private static SimpleLightVariant<?> fromData(Data data) {
        return switch (data.behaviour) {
            case STANDARD -> new SimpleLightVariant<>(
                    data.parallelsCord,
                    data.spacing,
                    data.bounds,
                    data.floorOffset,
                    SimpleLightVariant::standardBehavior,
                    data.orientable);
            case TORCH_LIGHT -> new SimpleLightVariant<>(
                    data.parallelsCord,
                    data.spacing,
                    data.bounds,
                    data.floorOffset,
                    stack -> new TorchLightBehavior(0.13D), // TODO actually serialize the correct dynamic value
                    data.orientable);
            case INCANDESCENT -> new SimpleLightVariant<>(
                    data.parallelsCord,
                    data.spacing,
                    data.bounds,
                    data.floorOffset,
                    stack -> new IncandescentBehavior(),
                    data.orientable);
        };
    }

    public record Data(boolean parallelsCord, float spacing, Box bounds, double floorOffset, LightBehaviourTag behaviour, boolean orientable) {
//        public static final Data EMPTY = new Data(null);
        public static final Codec<Box> BOX_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
               Codec.DOUBLE.fieldOf("minX").forGetter(o -> o.minX),
                    Codec.DOUBLE.fieldOf("minY").forGetter(o -> o.minY),
                    Codec.DOUBLE.fieldOf("minZ").forGetter(o -> o.minZ),
                    Codec.DOUBLE.fieldOf("maxX").forGetter(o -> o.maxX),
                    Codec.DOUBLE.fieldOf("maxY").forGetter(o -> o.maxY),
                    Codec.DOUBLE.fieldOf("maxZ").forGetter(o -> o.maxZ)
            ).apply(instance, Box::new)
        );
        public static final Codec<LightBehaviourTag> ENUM_CODEC = Codec.STRING.xmap(
                LightBehaviourTag::valueOf,
                Enum::name
        );
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.fieldOf("parallelsCord").forGetter(Data::parallelsCord),
                        Codec.FLOAT.fieldOf("spacing").forGetter(Data::spacing),
                        BOX_CODEC.fieldOf("bounds").forGetter(Data::bounds),
                        Codec.DOUBLE.fieldOf("floorOffset").forGetter(Data::floorOffset),
                        ENUM_CODEC.fieldOf("behaviour").forGetter(Data::behaviour),
                        Codec.BOOL.fieldOf("orientable").forGetter(Data::orientable)
                ).apply(instance, Data::new)
        );
        public static final PacketCodec<ByteBuf, Data> PACKET_CODEC = PacketCodecs.codec(CODEC);
    }

    public enum LightBehaviourTag {
        STANDARD, TORCH_LIGHT, INCANDESCENT
    }
}
