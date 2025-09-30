package com.lance5057.extradelight.aesthetics.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MoldingBlock extends Block {
	public static final EnumProperty<Direction> HALF = BlockStateProperties.VERTICAL_DIRECTION;

	public MoldingBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.defaultBlockState().setValue(HALF, Direction.UP));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(HALF);
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		//BlockPos blockpos = pContext.getClickedPos();
		//BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
		Direction direction = pContext.getClickedFace();
		return this.defaultBlockState().setValue(HALF, direction == Direction.UP ? Direction.DOWN : Direction.UP);

	}
}
