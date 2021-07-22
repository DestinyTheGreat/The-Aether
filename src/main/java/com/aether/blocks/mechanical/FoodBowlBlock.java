package com.aether.blocks.mechanical;

import com.aether.blocks.blockentity.FoodBowlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FoodBowlBlock extends BaseEntityBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    private final VoxelShape shapeX = Block.box(0, 0, 1, 16, 8, 15);
    private final VoxelShape shapeZ = Block.box(1, 0, 0, 15, 8, 16);

    public FoodBowlBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.Z).setValue(FULL, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!player.isShiftKeyDown() && world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
            return InteractionResult.sidedSuccess(foodBowl.handleUse(player, hand, player.getItemInHand(hand)) && world.isClientSide());
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(AXIS, ctx.getHorizontalDirection().getAxis());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(AXIS)) {
            case X -> shapeX;
            case Z -> shapeZ;
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(AXIS));
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoodBowlBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, FULL);
    }
}
