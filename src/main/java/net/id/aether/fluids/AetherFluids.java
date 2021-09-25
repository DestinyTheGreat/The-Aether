package net.id.aether.fluids;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.id.aether.client.rendering.block.FluidRenderSetup;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.registry.Registry;

import static net.id.aether.Aether.locate;

public class AetherFluids {
    public static final FlowableFluid DENSE_AERCLOUD = new DenseAercloudFluid();

    public static void init() {
        Registry.register(Registry.FLUID, locate("dense_aercloud"), DENSE_AERCLOUD);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockRenderLayerMap.INSTANCE.putFluid(DENSE_AERCLOUD, RenderLayer.getTranslucent());
        FluidRenderSetup.setupFluidRendering(AetherFluids.DENSE_AERCLOUD, null, locate("dense_aercloud"), 0xFFFFFF);
    }
}
