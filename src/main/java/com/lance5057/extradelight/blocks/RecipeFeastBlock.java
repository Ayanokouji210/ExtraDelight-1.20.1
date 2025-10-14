package com.lance5057.extradelight.blocks;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.integration.patchouli.processors.RecipeHolder;
import com.lance5057.extradelight.recipe.FeastRecipe;
import com.lance5057.extradelight.recipe.SimpleRecipeWrapper;
import com.lance5057.extradelight.util.ItemNBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public class RecipeFeastBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);

	public final boolean hasLeftovers;

	protected final VoxelShape[] SHAPES;

	public RecipeFeastBlock(Properties properties, boolean hasLeftovers, VoxelShape... shapes) {
		super(properties);
		SHAPES = shapes;
		this.hasLeftovers = hasLeftovers;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
				.setValue(getServingsProperty(), getMaxServings()));
	}

	public IntegerProperty getServingsProperty() {
		return SERVINGS;
	}

	public int getMaxServings() {
		return 4;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (SHAPES.length > 1)
			return Shapes.joinUnoptimized(SHAPES[0], rotateShape(state), BooleanOp.OR);
		else
			return SHAPES[0];
	}

	private VoxelShape rotateShape(BlockState state) {
		if (SHAPES.length > 4) {
			boolean l = state.getValue(SERVINGS) == 0;
			switch (state.getValue(FACING)) {
			case EAST:
			case WEST:
				if (l)
					return SHAPES[2];
				else
					return SHAPES[4];
			case SOUTH:
			case NORTH:
			default:
				if (l)
					return SHAPES[1];
				else
					return SHAPES[3];
			}
		}

		else if (SHAPES.length > 2) {
			switch (state.getValue(FACING)) {
			case EAST:
			case WEST:
				return SHAPES[2];
			case SOUTH:
			case NORTH:
			default:
				return SHAPES[1];
			}
		}
		return SHAPES[0];
	}

	public Optional<FeastRecipe> matchRecipe(Level level, ItemStack... itemstack) {
		if (level != null) {
			return level.getServer().getRecipeManager().getRecipeFor(ExtraDelightRecipes.FEAST.get(),
					new SimpleRecipeWrapper(itemstack), level);
		}
		return Optional.empty();

	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
            if(pPlayer.getMainHandItem().isEmpty()) {
                return InteractionResult.PASS;
            }else {
                return useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
        }
    }

	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
									   InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			if (this.takeServing(stack, level, pos, state, player, hand).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
		}

		return this.takeServing(stack, level, pos, state, player, hand);
	}

	protected InteractionResult takeServing(ItemStack stack, Level level, BlockPos pos, BlockState state,
			Player player, InteractionHand hand) {

		int servings = state.getValue(getServingsProperty());

		if (servings == 0) {
			level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
			level.destroyBlock(pos, true);
			return InteractionResult.SUCCESS;
		}


		ItemStack heldStack = player.getItemInHand(hand);
		RecipeManager recipeManager = level.getRecipeManager();
		Optional<FeastRecipe> r =  recipeManager.getRecipeFor(ExtraDelightRecipes.FEAST.get(),
				new SimpleRecipeWrapper(new ItemStack(this.asItem()), stack), level);

		if (r.isPresent()) {
			if (servings > 0) {
				ItemStack result = r.get().getResultItem(player.level().registryAccess()).copy();
				level.setBlock(pos, state.setValue(getServingsProperty(), servings - 1), 3);
				if (!player.getAbilities().instabuild) {
					if (heldStack.isDamageableItem())
						heldStack.hurtAndBreak(1, player, null);
					else
						heldStack.shrink(1);
				}
				if (!player.getInventory().add(result)) {
					player.drop(result, false);
				}
				if (level.getBlockState(pos).getValue(getServingsProperty()) == 0 && !this.hasLeftovers) {
					level.removeBlock(pos, false);
				}
				level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			}
		} else
			player.displayClientMessage(Component.translatable("extradelight.block.recipefeast.use_container"), true);

		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level,
			BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState()
				: super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isSolid();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return blockState.getValue(getServingsProperty());
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> tooltipComponents, TooltipFlag pFlag) {
		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.feast");
		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.BLUE));
	}

//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
//			TooltipFlag tooltipFlag) {
//		MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.feast");
//		tooltipComponents.add(textEmpty.withStyle(ChatFormatting.BLUE));
//	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
			Player player) {
		ItemStack stack = new ItemStack(this);
		stack = ItemNBTUtil.ItemNBTSetInt(stack,"servings",state.getValue(SERVINGS));
		//stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(SERVINGS, state.getValue(SERVINGS)));
		return stack;
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && !player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)
				&& state.getValue(SERVINGS) > 0) {
			ItemStack itemstack = new ItemStack(this);
			itemstack=ItemNBTUtil.ItemNBTSetInt(itemstack,"servings",state.getValue(SERVINGS));
			//itemstack.set(DataComponents.BLOCK_STATE,
			//		BlockItemStateProperties.EMPTY.with(SERVINGS, state.getValue(SERVINGS)));
			ItemEntity itementity = new ItemEntity(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
					itemstack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}

		super.playerWillDestroy(level, pos, state, player);
	}
}
