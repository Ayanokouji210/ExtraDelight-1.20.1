package com.lance5057.extradelight.workstations.evaporator;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightBlockEntities;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
//import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class EvaporatorBlock extends Block implements EntityBlock, IStyleable {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
	public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, Styles.values().length - 1);

	public static enum Styles {
		ANVIL, GOLD_BLOCK, NETHERITE_BLOCK, COPPER_BLOCK, IRON_BLOCK
	};

	public EvaporatorBlock() {
		super(Properties.copy(Blocks.IRON_BLOCK));
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

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pPlayer.getMainHandItem().isEmpty()){
			return InteractionResult.PASS;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
		}
	}

	public InteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos,
									   Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
			if (tileEntity instanceof EvaporatorBlockEntity mbe) {
				if (mbe.isInventoryEmpty()) {
					IFluidHandlerItem f = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
					if (f != null) {
						FluidUtil.interactWithFluidHandler(pPlayer, pHand, mbe.getFluidTank());
					}
				} else {
					if (stack.is(ItemTags.SHOVELS)) {
						mbe.dropItems();
					}
				}
			}

		}
		return InteractionResult.CONSUME;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new EvaporatorBlockEntity(pPos, pState);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof EvaporatorBlockEntity te) {
				IItemHandler items = te.getItemHandler();
				for (int i = 0; i < te.getItemHandler().getSlots(); i++) {
					level.addFreshEntity(
							new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i)));
				}
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

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
	public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable BlockGetter pLevel, List<Component> tooltipComponents, TooltipFlag pFlag) {
		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
	}

	//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
//			TooltipFlag tooltipFlag) {
//		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
//		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
//	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
			BlockEntityType<T> pBlockEntityType) {
		if (!pLevel.isClientSide())
			return pBlockEntityType == ExtraDelightBlockEntities.EVAPORATOR.get() ? EvaporatorBlockEntity::tick : null;
		return null;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		ItemStack stack = new ItemStack(this);
		CompoundTag tag = new CompoundTag();
		tag.putInt("style", state.getValue(STYLE));
		stack.setTag(tag);
		return stack;
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && !player.isCreative()
				&& level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			ItemStack itemstack = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putInt("style", state.getValue(STYLE));
			itemstack.setTag(tag);
			ItemEntity itementity = new ItemEntity(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
					itemstack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}

		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
		List<ItemStack> drops = super.getDrops(pState, pParams);
		for (ItemStack stack : drops) {
			if (stack.getItem() == this.asItem()) {
				CompoundTag tag = new CompoundTag();
				tag.putInt("style", pState.getValue(STYLE));
				stack.setTag(tag);
			}
		}
		return drops;
	}


	//	@Override
//	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos,
//			Player player) {
//		ItemStack stack = new ItemStack(this);
//		stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
//		return stack;
//	}

//	@Override
//	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
//		if (!level.isClientSide && !player.isCreative()
//				&& level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
//			ItemStack itemstack = new ItemStack(this);
//			itemstack.set(DataComponents.BLOCK_STATE,
//					BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
//			ItemEntity itementity = new ItemEntity(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
//					itemstack);
//			itementity.setDefaultPickUpDelay();
//			level.addFreshEntity(itementity);
//		}
//
//		return super.playerWillDestroy(level, pos, state, player);
//	}

}