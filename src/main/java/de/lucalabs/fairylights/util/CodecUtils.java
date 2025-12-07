package de.lucalabs.fairylights.util;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.datafixers.util.Function3;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class CodecUtils {
    private CodecUtils() {
    }

    public static <F1, R> Codec<R> deserializeOnlyCodec(MapCodec<F1> f1, Function<F1, R> constructor) {
        Codec<R> codec = RecordCodecBuilder.create(i -> i.group(
                f1.forGetter(x -> null)
        ).apply(i, constructor));

        return Codec.of(new ErrorEncoder<>("Cannot encode using a decoder-only codec"), codec::decode);
    }

    public static <F1, F2, R> Codec<R> deserializeOnlyCodec(MapCodec<F1> f1, MapCodec<F2> f2, BiFunction<F1, F2, R> constructor) {
        Codec<R> codec = RecordCodecBuilder.create(i -> i.group(
                f1.forGetter(x -> null),
                f2.forGetter(x -> null)
        ).apply(i, constructor));

        return Codec.of(new ErrorEncoder<>("Cannot encode using a decoder-only codec"), codec::decode);
    }

    public static <F1, F2, F3, R> Codec<R> deserializeOnlyCodec(MapCodec<F1> f1, MapCodec<F2> f2, MapCodec<F3> f3, Function3<F1, F2, F3, R> constructor) {
        Codec<R> codec = RecordCodecBuilder.create(i -> i.group(
                f1.forGetter(x -> null),
                f2.forGetter(x -> null),
                f3.forGetter(x -> null)
        ).apply(i, constructor));

        return Codec.of(new ErrorEncoder<>("Cannot encode using a decoder-only codec"), codec::decode);
    }

    private record ErrorEncoder<R>(String message) implements Encoder<R> {
        @Override
        public <T> DataResult<T> encode(R r, DynamicOps<T> dynamicOps, T t) {
            return DataResult.error(() -> message);
        }
    }
}
