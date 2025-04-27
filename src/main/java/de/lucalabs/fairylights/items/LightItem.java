package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.blocks.LightBlock;
import de.lucalabs.fairylights.components.FairyLightComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Settings properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public LightBlock getBlock() {
        return this.light;
    }

    @Override
    public void appendTooltip(final ItemStack stack, TooltipContext context, final List<Text> tooltip, final TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (Boolean.TRUE.equals(stack.get(FairyLightComponents.Lights.TWINKLE))) {
            tooltip.add(Text.translatable("item.fairyLights.twinkle").formatted(Formatting.GRAY, Formatting.ITALIC));
        }

        final var colors = stack.get(FairyLightComponents.Dyeable.COLORS);
        if (colors != null) {
            for (int i : colors) {
                tooltip.add(DyeableItem.getColorName(i).copy().formatted(Formatting.GRAY));
            }
        }
    }
}
