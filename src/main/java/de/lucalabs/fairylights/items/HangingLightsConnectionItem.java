package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Settings properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    public static StringType getString(final NbtCompound tag) {
        return Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.get(Identifier.tryParse(tag.getString("string"))));
    }

    public static void setString(final NbtCompound tag, final StringType string) {
        final Identifier name = FairyLightRegistries.STRING_TYPES.getId(string);
        if (name == null) {
            throw new NullPointerException("Missing registry name: " + string);
        }
        tag.putString("string", name.toString());
    }

    @Override
    public void appendTooltip(final ItemStack stack, @Nullable final World world, final List<Text> tooltip, final TooltipContext context) {
        final NbtCompound compound = stack.getNbt();
        if (compound != null) {
            final Identifier name = Identifier.tryParse(compound.getString("string"));
            if (name != null) {
                tooltip.add(Text.translatable("item." + name.getNamespace() + "." + name.getPath()).formatted(Formatting.GRAY));
            }
        }
        if (compound != null && compound.contains("pattern", NbtElement.LIST_TYPE)) {
            final NbtList tagList = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
            final int tagCount = tagList.size();
            if (tagCount > 0) {
                tooltip.add(Text.empty());
            }
            for (int i = 0; i < tagCount; i++) {
                final ItemStack lightStack = ItemStack.fromNbt(tagList.getCompound(i));
                tooltip.add(lightStack.getName());
                lightStack.getItem().appendTooltip(lightStack, world, tooltip, context);
            }
        }
    }
}
