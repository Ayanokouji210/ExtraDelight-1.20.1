package com.lance5057.extradelight.blocks.sink;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.util.BottleFluidRegistry;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.fluids.FluidActionResult;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
//import net.neoforged.neoforge.items.IItemHandler;

public class SinkCabinetBlock extends Block implements EntityBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public SinkCabinetBlock() {
		super(Properties.copy(Blocks.OAK_PLANKS).dynamicShape());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ExtraDelightBlockEntities.SINK_BLOCK.get().create(pPos, pState);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_54041_) {
		Direction direction = p_54041_.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState rotate(BlockState p_54094_, Rotation p_54095_) {
		return p_54094_.setValue(FACING, p_54095_.rotate(p_54094_.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState p_54091_, Mirror p_54092_) {
		return p_54091_.rotate(p_54092_.getRotation(p_54091_.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54097_) {
		p_54097_.add(FACING);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pPlayer.getMainHandItem().isEmpty()) {
            if (!pLevel.isClientSide) {
                MenuProvider menuProvider = getMenuProvider(pState, pLevel, pPos);
                NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, pPos);
            }
            return InteractionResult.SUCCESS;
		}else{
			return useItemOn(pPlayer.getMainHandItem(), pState, pLevel, pPos, pPlayer, pHand, pHit);
		}
    }

	public InteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos,
									   Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (tileEntity instanceof SinkCabinetBlockEntity be) {
			if (!pLevel.isClientSide) {
				if (stack.is(Items.GLASS_BOTTLE)) {
					BlockEntityUtils.Inventory.givePlayerItemStack(
							BottleFluidRegistry.getBottleFromFluid(new FluidStack(Fluids.WATER.getSource(), 1000)),
							pPlayer, pLevel, pPos);
					pPlayer.getItemInHand(pHand).shrink(1);
				} else if (stack.is(Items.POTION)) {
					BlockEntityUtils.Inventory.givePlayerItemStack(new ItemStack(Items.GLASS_BOTTLE), pPlayer, pLevel,
							pPos);
					pPlayer.getItemInHand(pHand).shrink(1);
				} else {
					IFluidHandlerItem handlerItem = FluidUtil.getFluidHandler(stack).orElse(null);
					if (handlerItem != null) {

						FluidStack f = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
						if (f.isEmpty()) {
							FluidActionResult far = FluidUtil.tryFillContainer(stack, be.getFluidHandler(),
									Integer.MAX_VALUE, pPlayer, true);
							if (far.isSuccess()) {
								stack.shrink(1);
								pPlayer.setItemInHand(pHand, stack);
								pPlayer.getInventory().placeItemBackInInventory(far.getResult());
								return InteractionResult.SUCCESS;

							}
						} else {
							FluidActionResult far = FluidUtil.tryEmptyContainer(stack, be.getFluidHandler(),
									Integer.MAX_VALUE, pPlayer, true);
							if (far.isSuccess()) {
								stack.shrink(1);
								pPlayer.setItemInHand(pHand, stack);
								pPlayer.getInventory().placeItemBackInInventory(far.getResult());
								return InteractionResult.SUCCESS;

							}
						}
					} else {
						MenuProvider menuProvider = getMenuProvider(pState, pLevel, pPos);
						//pPlayer.openMenu(containerProvider);
						NetworkHooks.openScreen((ServerPlayer) pPlayer,menuProvider,pPos);
						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		return InteractionResult.CONSUME;

	}

	@Override
	public @Nullable MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof SinkCabinetBlockEntity be) {
            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable(be.getDisplayName());
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
                                                        Player playerEntity) {
                    return new SinkCabinetMenu(windowId, playerInventory, be);
                }
            };
            return containerProvider;
        }
		return null;
    }

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof SinkCabinetBlockEntity te) {
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
}
