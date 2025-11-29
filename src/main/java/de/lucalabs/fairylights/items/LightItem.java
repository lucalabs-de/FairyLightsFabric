package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.blocks.LightBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.COLORS;
import static de.lucalabs.fairylights.items.components.FairyLightItemComponents.TWINKLE;

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
    public void appendTooltip(final ItemStack stack, TooltipContext context, final List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (Objects.requireNonNullElse(stack.get(TWINKLE), false)) {
            tooltip.add(Text.translatable("item.fairyLights.twinkle").formatted(Formatting.GRAY, Formatting.ITALIC));
        }

        if (stack.contains(COLORS)) {
            for (int color : Objects.requireNonNull(stack.get(COLORS))) {
                tooltip.add(DyeableItem.getColorName(color).copy().formatted(Formatting.GRAY));
            }
        }
    }
}
