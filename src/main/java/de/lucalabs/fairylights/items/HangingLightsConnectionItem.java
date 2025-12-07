package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Settings properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        final ComponentRecords.ConnectionLogic logic = ComponentRecords.ConnectionLogic.fromItemStack(stack);
        if (logic == null) {
            return;
        }

        final Identifier name = FairyLightRegistries.STRING_TYPES.getId(logic.string().orElseThrow());
        tooltip.add(Text.translatable("item." + name.getNamespace() + "." + name.getPath()).formatted(Formatting.GRAY));

        if (!logic.pattern().isEmpty()) {
            tooltip.add(Text.empty());
        }
        for (ItemStack lightStack : logic.pattern()) {
            tooltip.add(lightStack.getName());
            lightStack.getItem().appendTooltip(lightStack, context, tooltip, type);
        }
    }
}
