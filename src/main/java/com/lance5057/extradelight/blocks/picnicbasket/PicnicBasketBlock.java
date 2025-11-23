package com.lance5057.extradelight.blocks.picnicbasket;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class PicnicBasketBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final ResourceLocation CONTENTS = ResourceLocation.withDefaultNamespace("contents");
	protected VoxelShape SHAPE = Block.box(1.0D, 0.0D, 3.0D, 15.0D, 5.0D, 13.0D);
	protected VoxelShape SHAPE2 = Block.box(3.0D, 0.0D, 1.0D, 13.0D, 5.0D, 15.0D);

	@Nullable
	private final DyeColor color;

	public PicnicBasketBlock(Properties p_54120_) {
		super(p_54120_);
		SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 8.0D, 16.0D);
		SHAPE2 = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 8.0D, 12.0D);
		this.color = DyeColor.BLACK;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public PicnicBasketBlock(@Nullable DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PicnicBasketBlockEntity(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction d = pState.getValue(FACING);

		if (d == Direction.EAST || d == Direction.WEST)
			return SHAPE;
		return SHAPE2;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.getMainHandItem().isEmpty()) {
            return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
        }else{
            if (pLevel.getBlockEntity(pPos) instanceof PicnicBasketBlockEntity be) {
                if (pPlayer instanceof ServerPlayer sp) {
                    NetworkHooks.openScreen(sp,this.getMenuProvider(pState,pLevel,pPos),pPos);
                    return  InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                       InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof PicnicBasketBlockEntity be) {
                if (player instanceof ServerPlayer sp) {
                    MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
                    NetworkHooks.openScreen(sp,menuProvider,pos);
                }
            }
		}

		return InteractionResult.SUCCESS;
	}

    @Override
    public @org.jetbrains.annotations.Nullable MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity tileEntity = pLevel.getBlockEntity(pPos);

        if (tileEntity instanceof PicnicBasketBlockEntity be) {
            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable(be.getDisplayName());
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
                                                        Player playerEntity) {
                    return new PicnicBasketMenu(windowId, playerInventory, be);
                }
            };
            return containerProvider;
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PicnicBasketBlockEntity picnicBasket && !level.isClientSide) {

                ItemStack itemStack = new ItemStack(this);
                picnicBasket.saveToItem(itemStack);

                popResource(level, pos, itemStack);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PicnicBasketBlockEntity picnicBasket) {
            picnicBasket.saveToItem(itemStack);
        }

        return itemStack;
    }

    @Nullable
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

}