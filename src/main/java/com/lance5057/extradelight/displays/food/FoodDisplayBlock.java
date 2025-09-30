package com.lance5057.extradelight.displays.food;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.items.IItemHandler;

public class FoodDisplayBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty ENCASED = BooleanProperty.create("encased");

	public FoodDisplayBlock() {
		// strength used to be (0.5f, 6.0f)
		// properties.of used to be Material.METAL for some reason, this makes it gray
		super(Properties.copy(Blocks.DARK_OAK_PLANKS).strength(2.5F, 6.0F).sound(SoundType.WOOD).noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, false).setValue(ENCASED, false));
	}

	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pPlayer.getMainHandItem().isEmpty()){
			if (!pLevel.isClientSide) {
				MenuProvider menuProvider = getMenuProvider(pState, pLevel, pPos);
				NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, pPos);
			}
			return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
			//return InteractionResult.PASS;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
		}

    }

	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
									   InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide) {
			if (player.getItemInHand(hand).getItem() == Items.GLASS) {
				if (state.getValue(FoodDisplayBlock.ENCASED) == false) {
					// state.setValue(FoodDisplayBlock.ENCASED, true);
					level.setBlock(pos, state.setValue(FoodDisplayBlock.ENCASED, true), 2);
					player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
					return InteractionResult.SUCCESS;
				}
			}

			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof FoodDisplayEntity ent) {
				//player.openMenu(containerProvider);
				MenuProvider menuProvider = getMenuProvider(state, level, pos);
				NetworkHooks.openScreen((ServerPlayer) player, menuProvider, pos);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public @Nullable MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
		if (pLevel.getBlockEntity(pPos) instanceof FoodDisplayEntity ent) {
			MenuProvider containerProvider = new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.translatable(ent.getDisplayName());
				}

				@Override
				public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
														Player playerEntity) {
					return new FoodDisplayMenu(windowId, playerInventory, ent);
				}
			};
			return containerProvider;
		}
		return null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ExtraDelightBlockEntities.FOOD_DISPLAY.get().create(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, ENCASED, WATERLOGGED);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		FluidState fluid = level.getFluidState(context.getClickedPos());

		BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);

		return state;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
			BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return state;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof FoodDisplayEntity te) {
				IItemHandler items = te.getItemHandler();
				for (int i = 0; i < te.getItemHandler().getSlots(); i++) {
					level.addFreshEntity(
							new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i)));
				}
				level.updateNeighbourForOutputSignal(pos, this);
			}

			if (state.getValue(FoodDisplayBlock.ENCASED) == true)
				Block.popResource(level, pos, new ItemStack(Items.GLASS));

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}


//	@Override
//	protected MapCodec<? extends BaseEntityBlock> codec() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
