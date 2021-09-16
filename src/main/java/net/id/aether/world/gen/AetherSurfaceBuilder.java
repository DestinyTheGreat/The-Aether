package net.id.aether.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.class_6557;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.Random;

public class AetherSurfaceBuilder extends SurfaceBuilder<AetherSurfaceBuilderConfig> {
    public AetherSurfaceBuilder() {
        super(AetherSurfaceBuilderConfig.CODEC);
    }

    @Override
    public void generate(Random random, class_6557 chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int i, long l, AetherSurfaceBuilderConfig config) {
        BlockState topState = config.getTopMaterial();
        BlockState underState = config.getUnderMaterial();
        BlockPos.Mutable mut = new BlockPos.Mutable();
        int maxDepth = -1;
        int depth = (int) (noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int cX = x & 15;
        int cZ = z & 15;

        for (int m = height; m >= 0; --m) {
            mut.set(cX, m, cZ);
            BlockState blockState3 = chunk.getState(m);
            if (blockState3.isAir()) {
                maxDepth = -1;
            } else if (blockState3.isOf(defaultBlock.getBlock())) {
                if (maxDepth == -1) {
                    if (depth <= 0) {
                        topState = Blocks.AIR.getDefaultState();
                        underState = defaultBlock;
                    } else if (m >= seaLevel - 4 && m <= seaLevel + 1) {
                        topState = config.getTopMaterial();
                        underState = config.getUnderMaterial();
                    }

                    if (m < seaLevel && (topState == null || topState.isAir())) {
                        if (biome.getTemperature(mut.set(x, m, z)) < 0.15F) {
                            topState = Blocks.ICE.getDefaultState();
                        } else {
                            topState = defaultFluid;
                        }

                        mut.set(cX, m, cZ);
                    }

                    maxDepth = depth;
                    if (m >= seaLevel - 1) {
                        chunk.method_38092(m, topState);
                    } else if (m < seaLevel - 7 - depth) {
                        topState = Blocks.AIR.getDefaultState();
                        underState = defaultBlock;
                        chunk.method_38092(m, config.getUnderwaterMaterial());
                    } else {
                        chunk.method_38092(m, underState);
                    }
                } else if (maxDepth > 0) {
                    --maxDepth;
                    chunk.method_38092(m, underState);

                }
            }
        }
    }
}
