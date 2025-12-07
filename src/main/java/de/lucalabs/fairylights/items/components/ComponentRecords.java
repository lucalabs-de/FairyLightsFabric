package de.lucalabs.fairylights.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.BlockFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FenceFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.PlayerFastenerAccessor;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import de.lucalabs.fairylights.util.Utils;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.PATTERN;
import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.STRING;

public class ComponentRecords {

    public record ConnectionLogic(List<ItemStack> pattern, StringType string) {
        public static final Codec<ConnectionLogic> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.listOf().fieldOf("pattern").forGetter(ConnectionLogic::pattern),
                StringType.CODEC.fieldOf("string").forGetter(ConnectionLogic::string)
        ).apply(instance, ConnectionLogic::new));

        public ComponentMapImpl toComponents() {
            ComponentMapImpl comps = new ComponentMapImpl(ComponentMap.EMPTY);
            comps.set(PATTERN, pattern());
            comps.set(STRING, string());
            return comps;
        }

        public boolean matchesItemStack(ItemStack stack) {
            StringType stackString = stack.get(STRING);
            List<ItemStack> pattern = Objects.requireNonNullElse(stack.get(PATTERN), Collections.emptyList());

            boolean patternEqual = this.pattern().size() == pattern.size() && IntStream.range(0, pattern.size())
                    .allMatch(i -> ItemStack.areItemsAndComponentsEqual(pattern.get(i), this.pattern().get(i)));

            return this.string().equals(stackString) && patternEqual;
        }

        public static ConnectionLogic fromItemStack(ItemStack i) {
            Builder b = new Builder();
            if (i.contains(STRING)) {
                b.stringType(i.get(STRING));
            }
            if (i.contains(PATTERN)) {
                b.pattern(i.get(PATTERN));
            }
            return b.build();
        }

        public static class Builder {
            @NotNull
            private List<ItemStack> pattern = Collections.emptyList();
            @NotNull
            private StringType string = StringTypes.BLACK_STRING;

            public Builder pattern(List<ItemStack> pattern) {
                this.pattern = pattern;
                return this;
            }

            public Builder stringType(StringType string) {
                this.string = string;
                return this;
            }

            public ConnectionLogic build() {
                return new ConnectionLogic(pattern, string);
            }
        }
    }

    public record FastenerAccessorData(FastenerType type, FastenerAccessor accessor) {
        public static final Codec<FastenerAccessorData> CODEC = RecordCodecBuilder.create(i -> i.group(
                NbtCompound.CODEC.fieldOf("accessor").forGetter(data -> data.accessor().serialize()),
                Codec.INT.fieldOf("type").forGetter(data -> data.type.ordinal())
        ).apply(i, (data, t) -> {
            FastenerType type = Utils.getEnumValue(FastenerType.class, t);
            FastenerAccessor accessor = switch (type) {
                case BLOCK -> new BlockFastenerAccessor();
                case FENCE -> new FenceFastenerAccessor();
                case PLAYER -> new PlayerFastenerAccessor();
            };

            accessor.deserialize(data);

            return new FastenerAccessorData(type, accessor);
        }));

        public static FastenerAccessorData from(FastenerAccessor accessor) {
            return new FastenerAccessorData(accessor.getType(), accessor);
        }
    }

    public record ConnectionStatus(
            boolean isOn,
            boolean drop,
            float slack,
            Set<BlockPos> litBlocks,
            FastenerAccessorData destination,
            ConnectionLogic logic) {
        public static final Codec<ConnectionStatus> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.fieldOf("isOn").forGetter(ConnectionStatus::isOn),
                Codec.BOOL.fieldOf("drop").forGetter(ConnectionStatus::drop),
                Codec.FLOAT.fieldOf("slack").forGetter(ConnectionStatus::slack),
                BlockPos.CODEC.listOf().fieldOf("litBlocks").forGetter(x -> x.litBlocks.stream().toList()), // TODO verify that the performance of this is okay. I guess it's just a factor 2 in the O(n) serialization
                FastenerAccessorData.CODEC.fieldOf("destination").forGetter(ConnectionStatus::destination),
                ConnectionLogic.CODEC.fieldOf("logic").forGetter(ConnectionStatus::logic)
        ).apply(i, (o, d, s, l, ds, lo) -> new ConnectionStatus(o, d, s, new HashSet<>(l), ds, lo)));

        public static class Builder {
            private boolean isOn = false;
            private boolean drop = false;
            private float slack = 0.0F;
            @NotNull
            private Set<BlockPos> litBlocks = Collections.emptySet();
            @NotNull
            private ConnectionLogic logic = new ConnectionLogic.Builder().build();
            @NotNull
            private FastenerAccessorData accessorData = new FastenerAccessorData(FastenerType.PLAYER, new PlayerFastenerAccessor());

            public Builder slack(float slack) {
                this.slack = slack;
                return this;
            }

            public Builder isOn(boolean isOn) {
                this.isOn = isOn;
                return this;
            }

            public Builder drop(boolean drop) {
                this.drop = drop;
                return this;
            }

            public Builder logic(ConnectionLogic logic) {
                this.logic = logic;
                return this;
            }

            public Builder destination(FastenerAccessor accessor) {
                this.accessorData = new FastenerAccessorData(accessor.getType(), accessor);
                return this;
            }

            public Builder litBlocks(Set<BlockPos> litBlocks) {
                this.litBlocks = litBlocks;
                return this;
            }

            public ConnectionStatus build() {
                return new ConnectionStatus(isOn, drop, slack, litBlocks, accessorData, logic);
            }
        }
    }
}

