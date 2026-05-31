package de.lucalabs.fairylights;

import de.lucalabs.fairylights.client.data.FairyLightCraftingProvider;
import de.lucalabs.fairylights.main.attachments.FairyLightsAttachments;
import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import de.lucalabs.fairylights.main.creativetabs.FairyLightItemGroups;
import de.lucalabs.fairylights.main.entity.FairyLightEntities;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.main.items.crafting.FairyLightCraftingRecipes;
import de.lucalabs.fairylights.main.net.NeoForgeNetworking;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import de.lucalabs.fairylights.main.sounds.FairyLightSounds;
import de.lucalabs.fairylights.main.string.StringTypes;
import de.lucalabs.fairylights.main.util.Tags;
import de.lucalabs.fairylights.mixin.MappedRegistryAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class FairyLights {

    private boolean contentRegistered = false;

    public FairyLights(final IEventBus modBus) {
        modBus.addListener(RegisterEvent.class, this::onRegister);
        modBus.addListener(GatherDataEvent.class, this::onGatherData);
        FairyLightsAttachments.init(modBus);
        FairyLightItemGroups.init(modBus);
        NeoForgeNetworking.init(modBus);
    }

    /**
     * The common module registers eagerly in static initializers (a Fabric idiom). All vanilla
     * registries are simultaneously unfrozen during the RegisterEvent phase, so triggering those
     * initializers from a single RegisterEvent listener registers all content correctly. The custom
     * registries are the exception: the vendored RegistryBuilder writes them to the root registry,
     * which NeoForge keeps frozen, so we briefly unfreeze the root around that one call.
     */
    private void onRegister(final RegisterEvent event) {
        if (this.contentRegistered) {
            return;
        }
        this.contentRegistered = true;

        // Custom registries (connection types, string types, light variants) -> written to the root.
        final WritableRegistry<?> root = de.lucalabs.fairylights.mixin.RegistriesAccessor.getWRITABLE_REGISTRY();
        ((MappedRegistryAccessor) root).fairylights$setFrozen(false);
        try {
            FairyLightRegistries.initialize();
        } finally {
            ((MappedRegistryAccessor) root).fairylights$setFrozen(true);
        }

        // Vanilla content. Blocks before block entities/items so their static cross-references
        // (BlockEntityType blocks, BlockItems, light variants) resolve before they are needed.
        Tags.initialize();
        FairyLightSounds.initialize();
        FairyLightEntities.initialize();
        FairyLightBlocks.initialize();
        FairyLightBlockEntities.initialize();
        FairyLightItems.initialize();
        FairyLightItemComponents.initialize();
        FairyLightCraftingRecipes.initialize();
        ConnectionTypes.initialize();
        StringTypes.initialize();
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(
                event.includeServer(),
                new FairyLightCraftingProvider(output, lookupProvider)
        );
    }
}
