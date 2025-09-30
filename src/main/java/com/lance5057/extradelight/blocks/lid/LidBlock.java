package com.lance5057.extradelight.blocks.lid;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.blocks.interfaces.IStyleable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

import java.util.List;

public class LidBlock extends Block implements IStyleable {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
	public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, Styles.values().length - 1);

	public static enum Styles {
		COPPER_BLOCK, WHITE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, GRAY_TERRACOTTA, BLACK_TERRACOTTA, BROWN_TERRACOTTA,
		RED_TERRACOTTA, ORANGE_TERRACOTTA, YELLOW_TERRACOTTA, LIME_TERRACOTTA, GREEN_TERRACOTTA, CYAN_TERRACOTTA,
		LIGHT_BLUE_TERRACOTTA, BLUE_TERRACOTTA, MAGENTA_TERRACOTTA, PINK_TERRACOTTA, PURPLE_TERRACOTTA,
//		WHITE_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, BLACK_GLAZED_TERRACOTTA,
//		BROWN_GLAZED_TERRACOTTA, RED_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA,
//		LIME_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA,
//		BLUE_GLAZED_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA

	}

	public LidBlock() {
		super(Properties.copy(Blocks.COPPER_BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(STYLE, 0));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53681_) {
		p_53681_.add(STYLE);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return false;
	}

//	@Override
//	public boolean isPathfindable(BlockState pState, PathComputationType pType) {
//		return false;
//	}

	@Override
	public int numStyles() {
		return Styles.values().length;
	}

	@Override
	public int getCurrentStyle(BlockState state) {
		return state.getValue(STYLE);
	}

	@Override
	public void setNextStyle(Level level, BlockPos pos, BlockState state) {
		int next = state.getValue(STYLE) + 1;
		if (state.getValue(STYLE) >= numStyles() - 1) {
			next = 0;
		}

		for (int i = 0; i < 10; i++)
			level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SNOW_BLOCK.defaultBlockState()),
					pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
		level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

		BlockState nextState = state.setValue(STYLE, next);
		level.setBlock(pos, nextState, 3);
	}

	@Override
	public void setPrevStyle(Level level, BlockPos pos, BlockState state) {
		int next = state.getValue(STYLE) - 1;
		if (next < 0) {
			next = this.numStyles() - 1;
		}

		for (int i = 0; i < 10; i++)
			level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SNOW_BLOCK.defaultBlockState()),
					pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
		level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

		BlockState nextState = state.setValue(STYLE, next);
		level.setBlock(pos, nextState, 3);
	}

	@Override
	public BlockState getState(int i) {
		return this.defaultBlockState().setValue(STYLE, i);
	}

	@Override
	public void setStyle(Level level, BlockPos pos, BlockState state, int style) {
		BlockState nextState = state.setValue(STYLE, style);
		level.setBlock(pos, nextState, 3);
	}

	@Override
	public boolean isPatreonStyle(int style) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> tooltipComponents, TooltipFlag pFlag) {
		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
	}

//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
//			TooltipFlag tooltipFlag) {
//		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
//		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
//	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		ItemStack stack = new ItemStack(this);
		//stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
		CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            nbt.putInt("style", state.getValue(STYLE));
        }else{
			nbt = new CompoundTag();
		}
        stack.setTag(nbt);
		return stack;
	}


//	@Override
//	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos,
//			Player player) {
//		ItemStack stack = new ItemStack(this);
//		stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
//		return stack;
//	}


	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		return this.useItemOn(pPlayer.getMainHandItem(),pState, pLevel, pPos, pPlayer, pHand, pHit);
		//return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
									   InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide) {

			ItemStack r = new ItemStack(this.asItem());
			CompoundTag nbt = new CompoundTag();
			if(!stack.isEmpty()) {
				nbt = stack.getTag();
			}
			if (nbt != null) {
				nbt.putInt("style", state.getValue(STYLE));
			}else {
				nbt = new CompoundTag();
				nbt.putInt("style", state.getValue(STYLE));
			}
			r.setTag(nbt);
			//r.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
			if (!player.addItem(r.copy()))
				level.addFreshEntity(
						new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, r, 0, 0, 0));
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && !player.isCreative()
				&& level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			ItemStack itemstack = new ItemStack(this);
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("style", state.getValue(STYLE));
			itemstack.setTag(nbt);
			//itemstack.set(DataComponents.BLOCK_STATE,
			//		BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
			ItemEntity itementity = new ItemEntity(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
					itemstack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}

		super.playerWillDestroy(level, pos, state, player);
	}

}
