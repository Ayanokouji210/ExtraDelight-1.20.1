package com.lance5057.extradelight.blocks;

import com.lance5057.extradelight.blocks.entities.TapBlockEntity;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.util.BottleFluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
//import net.neoforged.neoforge.fluids.FluidActionResult;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class TapBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty GROUND = BlockStateProperties.DOWN;

	private static final VoxelShape EAST_DOWN_SHAPE = Shapes.or(Block.box(0.0, 0.0, 6.0, 4.0, 10.0, 10.0),
			Block.box(4.0, 6.0, 6.0, 8.0, 10.0, 10.0));
	private static final VoxelShape NORTH_DOWN_SHAPE = Shapes.or(Block.box(6.0, 0.0, 12.0, 10.0, 10.0, 16.0),
			Block.box(6.0, 6.0, 8.0, 10.0, 10.0, 12.0));
	private static final VoxelShape SOUTH_DOWN_SHAPE = Shapes.or(Block.box(6.0, 0.0, 0.0, 10.0, 10.0, 4.0),
			Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 8.0));
	private static final VoxelShape WEST_DOWN_SHAPE = Shapes.or(Block.box(12.0, 0.0, 6.0, 16.0, 10.0, 10.0),
			Block.box(8.0, 6.0, 6.0, 12.0, 10.0, 10.0));
	private static final VoxelShape EAST_SHAPE = Block.box(0.0, 6.0, 6.0, 4.0, 10.0, 10.0);
	private static final VoxelShape NORTH_SHAPE = Block.box(6.0, 6.0, 12.0, 10.0, 10.0, 16.0);
	private static final VoxelShape SOUTH_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 4.0);
	private static final VoxelShape WEST_SHAPE = Block.box(12.0, 6.0, 6.0, 16.0, 10.0, 10.0);

	public TapBlock(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(GROUND, true));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TapBlockEntity(pPos, pState);
	}

	@Override
	public VoxelShape getShape(BlockState p_54105_, BlockGetter p_54106_, BlockPos p_54107_,
			CollisionContext p_54108_) {
		if (p_54105_.getValue(GROUND))
			switch ((Direction) p_54105_.getValue(FACING)) {
			case NORTH:
				return NORTH_DOWN_SHAPE;
			case SOUTH:
				return SOUTH_DOWN_SHAPE;
			case WEST:
				return WEST_DOWN_SHAPE;
			case EAST:

			default:
				return EAST_DOWN_SHAPE;
			}
		else
			switch ((Direction) p_54105_.getValue(FACING)) {
			case NORTH:
				return NORTH_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			case EAST:

			default:
				return EAST_SHAPE;
			}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_54041_) {
		if (p_54041_.getClickedFace().getOpposite() == Direction.DOWN) {
			Direction direction = p_54041_.getHorizontalDirection().getOpposite();
			return this.defaultBlockState().setValue(FACING, direction).setValue(GROUND, true);
		}

		Direction direction = p_54041_.getClickedFace();
		return this.defaultBlockState().setValue(FACING, direction).setValue(GROUND, false);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_54103_) {
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
		p_54097_.add(FACING, GROUND);
	}

	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return false;
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pPlayer.getItemInHand(pHand).isEmpty()) {
			return InteractionResult.PASS;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(), pState, pLevel, pPos, pPlayer, pHand, pHit);
		}
	}


	public InteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos,
			Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (tileEntity instanceof TapBlockEntity be) {
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

							}
						} else {
							FluidActionResult far = FluidUtil.tryEmptyContainer(stack, be.getFluidHandler(),
									Integer.MAX_VALUE, pPlayer, true);
							if (far.isSuccess()) {
								stack.shrink(1);
								pPlayer.setItemInHand(pHand, stack);
								pPlayer.getInventory().placeItemBackInInventory(far.getResult());

							}
						}
					}
				}

				Direction dir = pState.getValue(FACING).getOpposite();

				float x = pPos.getX() + (0.5f + (0.3f * dir.getNormal().getX()));
				float z = pPos.getZ() + (0.5f + (0.3f * dir.getNormal().getZ()));

				pLevel.addParticle(ParticleTypes.DRIPPING_WATER, x, pPos.getY() + 0.25f, z, 0, 0, 0);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;

	}

}
