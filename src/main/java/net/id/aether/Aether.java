package net.id.aether;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.id.aether.api.MoaAPI;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.client.model.AetherArmorModels;
import net.id.aether.client.model.AetherModelPredicates;
import net.id.aether.client.rendering.AetherColorProviders;
import net.id.aether.client.rendering.entity.AetherEntityRenderers;
import net.id.aether.client.rendering.entity.layer.AetherModelLayers;
import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.commands.AetherCommands;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.AetherItems;
import net.id.aether.loot.AetherLootNumberProviderTypes;
import net.id.aether.registry.TrinketSlotRegistry;
import net.id.aether.world.AetherGameRules;
import net.id.aether.world.dimension.AetherDimension;
import net.id.aether.world.feature.AetherConfiguredFeatures;
import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.id.aether.world.gen.AetherCarvers;
import net.id.aether.world.weather.AetherWeatherController;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aether implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "the_aether";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static Identifier locate(String location) {
        return new Identifier(MOD_ID, location);
    }

    @Override
    public void onInitialize() {
        TrinketSlotRegistry.init();
        AetherCarvers.init();
        AetherTreeHell.init();
        AetherFeatures.registerFeatures();
        AetherConfiguredFeatures.registerFeatures();
        AetherDimension.setupDimension();
        AetherBlocks.init();
        AetherFluids.init();
        AetherEntityTypes.init();
        AetherItems.init();
        AetherBlockEntityTypes.init();
        AetherCommands.init();
        AetherGameRules.init();
        AetherLootNumberProviderTypes.init();
        MoaAPI.init();
        AetherWeatherController.init();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations("aether", MOD_ID);
        AetherModelPredicates.init();
        AetherArmorModels.registerArmorModels();
        AetherModelLayers.initClient();
        AetherEntityRenderers.initClient();
        AetherBlockEntityTypes.initClient();
        AetherFluids.initClient();
        AetherParticles.initClient();
        AetherColorProviders.initClient();
        AetherWeatherController.initClient();
    }
}
