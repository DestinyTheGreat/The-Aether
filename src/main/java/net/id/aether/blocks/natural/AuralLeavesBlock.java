package net.id.aether.blocks.natural;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.util.RenderUtils;
import net.id.aether.util.DynamicBlockColorProvider;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.gen.SimpleRandom;

public class AuralLeavesBlock extends AetherLeavesBlock implements DynamicBlockColorProvider {

    private final Vec3i[] gradientColors;

    public AuralLeavesBlock(Settings settings, boolean collidable, Vec3i[] gradientColors) {
        super(settings, collidable);
        if(gradientColors.length != 4) {
            throw new InstantiationError("color gradient must contain exactly four colors");
        }
        this.gradientColors = gradientColors;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockColorProvider getProvider() {
        return (state, world, pos, tintIndex) -> getAuralColor(pos, gradientColors);
    }

    // Sigmoid
    protected static double contrastCurve(double contrast, double percent){
        percent = percent - 0.5;
        return MathHelper.clamp(percent * Math.sqrt((4 + contrast)/(4+4*contrast*percent*percent)) + 0.5,0,1);
    }

    public static int getAuralColor(BlockPos pos, Vec3i[] colorRGBs){
        Vec3i color1 = colorRGBs[0];
        Vec3i color2 = colorRGBs[1];
        Vec3i color3 = colorRGBs[2];
        Vec3i color4 = colorRGBs[3];
        float clumpSize = 7;

        // First, we mix color 1 and color 2 using noise
        PerlinNoiseSampler perlinNoise = new PerlinNoiseSampler(new SimpleRandom(1738));
        // Sample perlin noise (and change bounds from [-1, 1] to [0, 1])
        double perlin = 0.5 * (1 + perlinNoise.sample(pos.getX()/clumpSize,pos.getY()/clumpSize,pos.getZ()/clumpSize,4000,0));
        // Reshape contrast curve
        double percent = contrastCurve(36, perlin);
        percent = percent*(2-percent);
        // Interpolate
        double r1,g1,b1;
        r1 = (MathHelper.lerp(percent, color1.getX(), color2.getX()));
        g1 = (MathHelper.lerp(percent, color1.getY(), color2.getY()));
        b1 = (MathHelper.lerp(percent, color1.getZ(), color2.getZ()));

        // Now we mix colors 3 and 4 together using noise
        // Rinse, repeat as seen above.
        perlinNoise = new PerlinNoiseSampler(new SimpleRandom(1337));
        // Sample & reshape
        double perlin2 = MathHelper.clamp(0.5 * (1 + perlinNoise.sample(pos.getX()/clumpSize,pos.getY()/clumpSize,pos.getZ()/clumpSize,4000,0)),0,1);
        double percent2 = perlin2*(2-perlin2);
        // Interpolate
        double r2,g2,b2;
        r2 = (MathHelper.lerp(percent2, color3.getX(), color4.getX()));
        g2 = (MathHelper.lerp(percent2, color3.getY(), color4.getY()));
        b2 = (MathHelper.lerp(percent2, color3.getZ(), color4.getZ()));

        // This last section interpolates between r1, g1, b1, and r2, g2, b2, finally mixing all the colors together.
        perlinNoise = new PerlinNoiseSampler(new SimpleRandom(9980));
        double perlin3 = 0.5 * (1 + perlinNoise.sample(pos.getX()/clumpSize,pos.getY()/clumpSize,pos.getZ()/clumpSize,4000,0));
        double finalPercent = contrastCurve(25, perlin3);
        // Interpolate
        int finalR, finalG, finalB;
        finalR = (int) (MathHelper.lerp(finalPercent, r1, r2));
        finalG = (int) (MathHelper.lerp(finalPercent, g1, g2));
        finalB = (int) (MathHelper.lerp(finalPercent, b1, b2));

        return RenderUtils.toHex(finalR, finalG, finalB);
    }
}
