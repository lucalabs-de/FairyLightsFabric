package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.util.styled.StyledString;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class PennantBuntingConnectionItem extends ConnectionItem {
    public PennantBuntingConnectionItem(final Item.Settings properties) {
        super(properties, ConnectionTypes.PENNANT_BUNTING);
    }

    @Override
    public void appendTooltip(final ItemStack stack, final World world, final List<Text> tooltip, final TooltipContext flag) {
        final NbtCompound compound = stack.getNbt();
        if (compound == null) {
            return;
        }
        if (compound.contains("text", NbtElement.COMPOUND_TYPE)) {
            final NbtCompound text = compound.getCompound("text");
            final StyledString s = StyledString.deserialize(text);
            if (!s.isEmpty()) {
                tooltip.add(Text.translatable("format.fairylights.text", s.toTextText()).formatted(Formatting.GRAY));
            }
        }
        if (compound.contains("pattern", NbtElement.LIST_TYPE)) {
            final NbtList tagList = compound.getList("pattern", NbtElement.COMPOUND_TYPE);
            final int tagCount = tagList.size();
            if (tagCount > 0) {
                tooltip.add(Text.empty());
            }
            for (int i = 0; i < tagCount; i++) {
                final ItemStack item = ItemStack.fromNbt(tagList.getCompound(i));
                tooltip.add(item.getName());
            }
        }
    }
}
