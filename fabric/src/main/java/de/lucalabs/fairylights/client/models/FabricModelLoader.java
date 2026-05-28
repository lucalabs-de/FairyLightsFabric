package de.lucalabs.fairylights.client.models;

import de.lucalabs.fairylights.client.model.ModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class FabricModelLoader implements ModelLoader {
    @Override
    public BakedModel getModel(ResourceLocation path) {
        return Minecraft.getInstance().getModelManager().getModel(path);
    }
}
