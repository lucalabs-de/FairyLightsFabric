package de.lucalabs.fairylights.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelLoader {
    BakedModel getModel(ResourceLocation path);
}
