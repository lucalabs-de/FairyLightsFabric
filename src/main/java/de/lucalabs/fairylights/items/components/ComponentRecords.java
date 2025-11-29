package de.lucalabs.fairylights.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.BlockFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.FenceFastenerAccessor;
import de.lucalabs.fairylights.fastener.accessor.PlayerFastenerAccessor;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ComponentRecords {
    public record ConnectionLogic(List<ItemStack> pattern, StringType string) {
        public static final Codec<ConnectionLogic> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.listOf().fieldOf("pattern").forGetter(ConnectionLogic::pattern),
                Identifier.CODEC.fieldOf("string").forGetter(logic -> FairyLightRegistries.STRING_TYPES.getId(logic.string))
        ).apply(instance, (pattern, string) -> {
            StringType stringType = Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.get(string));
            return new ConnectionLogic(pattern, stringType);
        }));

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
                Codec.STRING.fieldOf("type").forGetter(data -> data.type.name())
        ).apply(i, (data, t) -> {
            FastenerType type = FastenerType.fromName(t);
            FastenerAccessor accessor = switch (type) {
                case BLOCK -> new BlockFastenerAccessor();
                case FENCE -> new FenceFastenerAccessor();
                case PLAYER -> new PlayerFastenerAccessor();
            };

            accessor.deserialize(data);

            return new FastenerAccessorData(type, accessor);
        }));
    }

    public record ConnectionStatus(
            boolean isOn,
            boolean drop,
            float slack,
            List<BlockPos> litBlocks,
            FastenerAccessorData destination,
            ConnectionLogic logic) {
        public static final Codec<ConnectionStatus> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.fieldOf("isOn").forGetter(ConnectionStatus::isOn),
                Codec.BOOL.fieldOf("drop").forGetter(ConnectionStatus::drop),
                Codec.FLOAT.fieldOf("slack").forGetter(ConnectionStatus::slack),
                BlockPos.CODEC.listOf().fieldOf("litBlocks").forGetter(ConnectionStatus::litBlocks),
                FastenerAccessorData.CODEC.fieldOf("destination").forGetter(ConnectionStatus::destination),
                ConnectionLogic.CODEC.fieldOf("logic").forGetter(ConnectionStatus::logic)
        ).apply(i, ConnectionStatus::new));

        public static class Builder {
            private boolean isOn = false;
            private boolean drop = false;
            private float slack = 0.0F;
            @NotNull
            private List<BlockPos> litBlocks = Collections.emptyList();
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

            public ConnectionStatus build() {
                return new ConnectionStatus(isOn, drop, slack, litBlocks, accessorData, logic);
            }
        }
    }
}

