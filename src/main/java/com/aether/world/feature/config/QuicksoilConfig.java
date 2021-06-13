package com.aether.world.feature.config;

import com.aether.blocks.AetherBlocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class QuicksoilConfig extends DynamicConfiguration {
    private Optional<BlockState> optionalState;

    public QuicksoilConfig(Optional<BlockState> state, Optional<String> type) {
        super(state.orElse(AetherBlocks.QUICKSOIL.defaultBlockState()), type);
        this.optionalState = state;
    }

    public Optional<BlockState> getOptionalState() {
        return optionalState;
    }
}
