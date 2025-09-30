package com.lance5057.extradelight.blocks.countercabinet;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
//import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class CounterCabinetBlock extends BaseEntityBlock {
//	public static final MapCodec<CounterCabinetBlock> CODEC = simpleCodec(CounterCabinetBlock::new);

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public CounterCabinetBlock() {
		this(Properties.copy(Blocks.DARK_OAK_WOOD).noOcclusion());
	}

	public CounterCabinetBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
	}

//	@Override
//	protected MapCodec<? extends BaseEntityBlock> codec() {
//		return CODEC;
//	}


	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		return this.useWithoutItem(pState, pLevel, pPos, pPlayer,pHit);
	}

	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hit) {
		if (!level.isClientSide) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof CounterCabinetBlockEntity be) {

				MenuProvider containerProvider = new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.translatable(be.getDisplayName());
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
							Player playerEntity) {
						return new CounterCabinetMenu(windowId, playerInventory, be);
					}
				};
//				player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
				NetworkHooks.openScreen((ServerPlayer) player, containerProvider, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof CounterCabinetBlockEntity te) {
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
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof CounterCabinetBlockEntity) {
			((CounterCabinetBlockEntity) tileEntity).recheckOpen();
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ExtraDelightBlockEntities.COUNTER_CABINET_BLOCK.get().create(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
		return true;
	}
}
