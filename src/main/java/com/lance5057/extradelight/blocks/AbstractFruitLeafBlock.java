package com.lance5057.extradelight.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractFruitLeafBlock extends Block {

	public static final int DECAY_DISTANCE = 7;
	public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
	public static final int MAX_AGE = 3;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
	public static final BooleanProperty STERILE = BooleanProperty.create("sterile");
	
	public AbstractFruitLeafBlock(Properties properties) {
		super(properties);
	}

}
