package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import de.lucalabs.fairylights.main.string.StringTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag type) {
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
