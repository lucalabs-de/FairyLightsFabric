package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.items.LightVariant;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.item.ItemStack;

public class LightVariantComponent extends GenericItemComponent<LightVariant<?>> {
    public LightVariantComponent(ItemStack stack, ComponentKey<?> key) {
        super(stack, key);
    }

    public LightVariantComponent setLightVariant(LightVariant<?> variant) {
        return (LightVariantComponent) super.set(variant);
    }
}
