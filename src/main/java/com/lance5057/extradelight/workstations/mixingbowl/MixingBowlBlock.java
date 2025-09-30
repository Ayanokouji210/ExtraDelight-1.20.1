package com.lance5057.extradelight.workstations.mixingbowl;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightTags;
import com.lance5057.extradelight.blocks.interfaces.IStyleable;
import com.lance5057.extradelight.data.LootModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public class MixingBowlBlock extends Block implements EntityBlock, IStyleable {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
	public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, Styles.values().length - 1);

	public static enum Styles {
		OAK_PLANKS, SPRUCE_PLANKS, BIRCH_PLANKS, DARK_OAK_PLANKS, ACACIA_PLANKS, MANGROVE_PLANKS, BAMBOO_PLANKS,
		CHERRY_PLANKS, JUNGLE_PLANKS, WARPED_PLANKS, CRIMSON_PLANKS, GLASS, STONE, IRON_BLOCK, GOLD_BLOCK, TERRACOTTA,
		WHITE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, GRAY_TERRACOTTA, BLACK_TERRACOTTA, BROWN_TERRACOTTA, RED_TERRACOTTA,
		ORANGE_TERRACOTTA, YELLOW_TERRACOTTA, LIME_TERRACOTTA, GREEN_TERRACOTTA, CYAN_TERRACOTTA, LIGHT_BLUE_TERRACOTTA,
		BLUE_TERRACOTTA, MAGENTA_TERRACOTTA, PINK_TERRACOTTA, PURPLE_TERRACOTTA, WHITE_GLAZED_TERRACOTTA,
		LIGHT_GRAY_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, BLACK_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA,
		RED_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA,
		GREEN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, BLUE_GLAZED_TERRACOTTA,
		MAGENTA_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA
	};

	public MixingBowlBlock() {
		super(Properties.copy(Blocks.ACACIA_WOOD).strength(0.5F, 1.0F).sound(SoundType.WOOD).noOcclusion());
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
			if(!pLevel.isClientSide()){
				if(pLevel.getBlockEntity(pPos) instanceof MixingBowlBlockEntity be){
					MenuProvider containerProvider = new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.translatable("screen.mixing_bowl.name");
						}

						@Override
						public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
																Player playerEntity) {
							return new MixingBowlMenu(windowId, playerInventory, be);
						}
					};
					//pPlayer.openMenu(containerProvider);
					NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, pPos);
				}
			}
			return InteractionResult.SUCCESS;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
		}
	}


	public InteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos,
									   Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide) {
			return InteractionResult.SUCCESS;
		} else if (stack.is(ExtraDelightTags.SPOONS)) {
			BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
			if (tileEntity instanceof MixingBowlBlockEntity mbe) {
				mbe.mix(pPlayer);
			}
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
			if (tileEntity instanceof MixingBowlBlockEntity mbe) {
				MenuProvider containerProvider = new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.translatable("screen.mixing_bowl.name");
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
							Player playerEntity) {
						return new MixingBowlMenu(windowId, playerInventory, mbe);
					}
				};
				//pPlayer.openMenu(containerProvider);
				NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, pPos);

			}
			return InteractionResult.CONSUME;

		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		// TODO Auto-generated method stub
		return new MixingBowlBlockEntity(pPos, pState);
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

//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
//			TooltipFlag tooltipFlag) {
//		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.styleable");
//		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.AQUA));
//	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof MixingBowlBlockEntity te) {
				IItemHandler items = te.getItemHandler();
				for (int i = 0; i < te.getItemHandler().getSlots(); i++) {
					if (i != MixingBowlBlockEntity.GHOST_SLOT)
						level.addFreshEntity(
								new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i)));
				}
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
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
