package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
    public void appendTooltip(final ItemStack stack, final TooltipContext context, final List<Text> tooltip, final TooltipType type) {
        final Identifier name = Identifier.tryParse(stack.getOrDefault(FairyLightComponents.Connection.STRING, ""));
        if (name != null) {
            tooltip.add(Text.translatable("item." + name.getNamespace() + "." + name.getPath()).formatted(Formatting.GRAY));
        }

        final var items = stack.get(FairyLightComponents.Connection.PATTERN);
        if (items != null) {
            if (!items.isEmpty()) {
                tooltip.add(Text.empty());
            }
            for (ItemStack lightStack : items) {
                tooltip.add(lightStack.getName());
                lightStack.getItem().appendTooltip(lightStack, context, tooltip, type);
            }
        }
    }
}
