package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.client.model.ModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class NeoForgeModelLoader implements ModelLoader {
    @Override
    public BakedModel getModel(ResourceLocation path) {
        return Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(path));
    }
}
