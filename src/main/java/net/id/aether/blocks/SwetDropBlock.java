package net.id.aether.blocks;

import net.id.aether.entities.hostile.swet.SwetEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SwetDropBlock extends EntityBlockEgg {
    private static final DirectionProperty FACING = Properties.FACING;

    public SwetDropBlock(Settings settings, BiFunction<World, BlockPos, ? extends LivingEntity> function) {
        super(settings, function);
    }

    public SwetDropBlock(Settings settings, Supplier<EntityType<? extends SwetEntity>> entityType) {
        super(settings, (world, pos) -> {
            SwetEntity swet = entityType.get().create(world);
            if (swet != null) {
                swet.setSize(1, true);
                swet.setPosition(Vec3d.of(pos));
            }
            return swet;
        });
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
