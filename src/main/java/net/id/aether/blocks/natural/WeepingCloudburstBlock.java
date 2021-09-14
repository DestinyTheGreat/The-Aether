package net.id.aether.blocks.natural;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WeepingCloudburstBlock extends PlantBlock implements Waterloggable {

    public static final EnumProperty<Section> SECTION = EnumProperty.of("section", Section.class);
    public static final BooleanProperty NOGROW = BooleanProperty.of("nogrow");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public WeepingCloudburstBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(SECTION, Section.BOTTOM).with(NOGROW, false).with(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(WATERLOGGED, true);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var waterLogged = state.get(WATERLOGGED);
        if(random.nextInt(6) == 0 && (waterLogged || random.nextBoolean())) {
            var roofState = world.getBlockState(pos.up());
            var air = roofState.isAir();
            if(air || roofState.getFluidState().getFluid() == Fluids.WATER) {

                int height = 0;

                var bottomPos = pos.down();
                var bottomState = world.getBlockState(bottomPos);

                while(bottomState.isOf(this) && bottomState.get(SECTION) == Section.BODY && world.getFluidState(bottomPos).isEmpty()) {
                    height++;
                    bottomPos = bottomPos.down();
                    bottomState = world.getBlockState(bottomPos);
                }

                world.setBlockState(pos, state.with(NOGROW, true));

                if(air && ((height > 0 && random.nextInt(3 - (height / 2)) <= random.nextInt(2)) || height > 4)) {
                    world.setBlockState(pos.up(), getDefaultState().with(NOGROW, true).with(SECTION, Section.TOP));
                }
                else {
                    world.setBlockState(pos.up(), getDefaultState().with(SECTION, Section.BODY).with(WATERLOGGED, world.isWater(pos.up())));
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(NOGROW);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var floorPos = pos.down();
        return switch (state.get(SECTION)) {
            case BOTTOM -> world.isWater(pos) && canPlantOnTop(world.getBlockState(floorPos), world, floorPos);
            case BODY -> world.getBlockState(floorPos).isOf(this);
            case TOP -> !world.isWater(pos) && world.getBlockState(floorPos).isOf(this);
        };
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public float getMaxModelOffset() {
        return 0.2F;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED, SECTION, NOGROW);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public enum Section implements StringIdentifiable {
        BOTTOM,
        BODY,
        TOP;

        @Override
        public String toString() {
            return this.asString();
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }
}
