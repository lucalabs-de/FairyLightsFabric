package de.lucalabs.fairylights.string;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record StringType(int color) {
    public static final Codec<StringType> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("color").forGetter(FairyLightRegistries.STRING_TYPES::getKey)
    ).apply(i, id -> Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.get(id))));
}
