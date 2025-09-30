package com.lance5057.extradelight.workstations.mortar;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightTags;
import com.lance5057.extradelight.blocks.interfaces.IStyleable;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.util.BottleFluidRegistry;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
//import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public class MortarBlock extends Block implements EntityBlock, IStyleable {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D);
	public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, Styles.values().length - 1);

	public static enum Styles {
		STONE, GRANITE, DIORITE, ANDESITE, DEEPSLATE, TUFF, SANDSTONE_TOP, RED_SANDSTONE_TOP, PRISMARINE, NETHERRACK,
		BASALT_SIDE, BLACKSTONE, END_STONE, BEDROCK, AMETHYST_BLOCK, GILDED_BLACKSTONE
	};

	public MortarBlock() {
		this(SoundType.STONE);
		this.registerDefaultState(this.stateDefinition.any().setValue(STYLE, 0));
	}

	public MortarBlock(SoundType soundType) {
		// strength used to be (1.5f, 2.0f)
		super(Properties.copy(Blocks.STONE).strength(0.5F, 2.0F).sound(soundType).noOcclusion());
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
		if (pPlayer.getMainHandItem().isEmpty()){
			if(!pLevel.isClientSide()){
				BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
				if(blockEntity instanceof MortarBlockEntity mbe){
					ItemStack insertedItem = mbe.getInsertedItem();
					if((!insertedItem.isEmpty())){
						mbe.extractItem(pPlayer);
						return InteractionResult.SUCCESS;
					}
				}
			}
			return InteractionResult.CONSUME;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
		}
	}

	public InteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos,
									   Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//		if (pLevel.isClientSide) {
//			return ItemInteractionResult.SUCCESS;
//		} else {
 		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (tileEntity instanceof MortarBlockEntity mbe) {
			ItemStack i = BottleFluidRegistry.getBottleFromFluid(mbe.getFluidTank().getFluid());
			ItemStack offhandStack = pPlayer.getOffhandItem();

			if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isPresent()) {
 				IFluidHandlerItem f = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
				if (f != null) {
					FluidUtil.interactWithFluidHandler(pPlayer, pHand, mbe.getFluidTank());
					return InteractionResult.SUCCESS;
				}
			} else if (!i.isEmpty() && (ItemStack.isSameItem(i.getCraftingRemainingItem(), stack) ||
					ItemStack.isSameItem(i.getCraftingRemainingItem(), offhandStack))) {

//				if (!i.isEmpty()) {
				if (mbe.getFluidTank().drain(250, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
					mbe.getFluidTank().drain(250, IFluidHandler.FluidAction.EXECUTE);

					BlockEntityUtils.Inventory.givePlayerItemStack(i, pPlayer, pLevel, pPos);
					pPlayer.getItemInHand(pHand).shrink(1);
					return InteractionResult.SUCCESS;
//					}
				}
			} else if (pPlayer.isCrouching()) {
				mbe.extractItem(pPlayer);
				return InteractionResult.SUCCESS;
			} else if (mbe.getInsertedItem().isEmpty()) {

				if (offhandStack.isEmpty())
					if (!stack.isEmpty()&&!stack.is(ExtraDelightTags.PESTLES))
						mbe.insertItem(stack);
					else
						return InteractionResult.PASS;
//				if (pHand.equals(InteractionHand.MAIN_HAND))
//					return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

				mbe.insertItem(offhandStack);
				return InteractionResult.SUCCESS;
			}

			else if (stack.is(ExtraDelightTags.PESTLES) || offhandStack.is(ExtraDelightTags.PESTLES)) {
				mbe.grind(pPlayer);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
//	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MortarBlockEntity(pPos, pState);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof MortarBlockEntity te) {
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
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
		pTooltip.add(textEmpty.withStyle(ChatFormatting.AQUA));
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
	public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pBuilder) {
		List<ItemStack> drops = super.getDrops(pState, pBuilder);
		for (ItemStack stack : drops) {
			if (stack.getItem() == this.asItem()) {
				CompoundTag tag = new CompoundTag();
				tag.putInt("style", pState.getValue(STYLE));
				stack.setTag(tag);
			}
		}
		return drops;
	}
}


	//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
//			TooltipFlag tooltipFlag) {
//		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
//		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
//	}

//	@Override
//	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos,
//			Player player) {
//		ItemStack stack = new ItemStack(this);
//		stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(STYLE, state.getValue(STYLE)));
//		return stack;
//	}
//
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

