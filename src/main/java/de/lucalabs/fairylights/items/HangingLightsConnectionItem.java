package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.ComponentRecords;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        final ComponentRecords.ConnectionLogic logic = ComponentRecords.ConnectionLogic.fromItemStack(stack);
        if (logic == null) {
            return;
        }

        final ResourceLocation name = FairyLightRegistries.STRING_TYPES.getKey(logic.string().orElse(StringTypes.BLACK_STRING));
        tooltip.add(Component.translatable("item." + name.getNamespace() + "." + name.getPath()).withStyle(ChatFormatting.GRAY));

        if (!logic.pattern().isEmpty()) {
            tooltip.add(Component.empty());
        }
        for (ItemStack lightStack : logic.pattern()) {
            tooltip.add(lightStack.getHoverName());
            lightStack.getItem().appendHoverText(lightStack, context, tooltip, type);
        }
    }
}
