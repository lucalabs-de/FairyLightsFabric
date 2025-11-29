package de.lucalabs.fairylights.string;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.util.Identifier;

import java.util.Objects;

public record StringType(int color) {
    public static final Codec<StringType> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("color").forGetter(FairyLightRegistries.STRING_TYPES::getId)
    ).apply(i, id -> Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.get(id))));
}
