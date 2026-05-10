package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.blocks.LightBlock;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.COLORS;
import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.TWINKLE;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public LightBlock getBlock() {
        return this.light;
    }

    @Override
    public void appendHoverText(final ItemStack stack, TooltipContext context, final List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);

        if (Objects.requireNonNullElse(stack.get(TWINKLE), false)) {
            tooltip.add(Component.translatable("item.fairyLights.twinkle").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        if (stack.has(COLORS)) {
            for (int color : Objects.requireNonNull(stack.get(COLORS))) {
                tooltip.add(DyeableItem.getColorName(color).copy().withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
