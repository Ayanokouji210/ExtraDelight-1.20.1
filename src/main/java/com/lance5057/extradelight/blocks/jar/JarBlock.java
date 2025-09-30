package com.lance5057.extradelight.blocks.jar;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.blocks.IDisplayInteractable;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlock;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlockEntity;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.util.BlockEntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
//import net.neoforged.neoforge.fluids.SimpleFluidContent;

public class JarBlock extends Block implements EntityBlock {
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 9.0D, 11.0D);

	public JarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new JarBlockEntity(pos, state);
	}

	@Override
	public @Nonnull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pPlayer.getMainHandItem().isEmpty()){
			return InteractionResult.PASS;
		}else {
			return this.useItemOn(pPlayer.getMainHandItem(),pState,pLevel,pPos,pPlayer,pHand,pHit);
		}

	}

	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
									   InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide) {

			if (stack.getItem() instanceof IDisplayInteractable bi) {
				ItemStack clone = this.getCloneItemStack(state, result, level, pos, player).copy();

				level.setBlock(pos,
						Fermentation.JAR_DISPLAY_BLOCK.get().defaultBlockState().setValue(JarDisplayBlock.FACING,
								result.getDirection() == Direction.UP || result.getDirection() == Direction.DOWN
										? player.getDirection()
										: result.getDirection().getOpposite()),
						UPDATE_ALL);
//					level.setBlockEntity(new JarDisplayBlockEntity(pos, state));

				BlockEntity be = level.getBlockEntity(pos);
				if (be != null && be instanceof JarDisplayBlockEntity jdbe) {
					BlockEntityUtils.Inventory.insertItem(jdbe.getItems(), clone, JarDisplayBlockEntity.NUM_SLOTS);
					BlockEntityUtils.Inventory.insertItem(jdbe.getItems(), stack, JarDisplayBlockEntity.NUM_SLOTS);
				}
				// Something went wrong, put it back!
				else {
					level.setBlock(pos, state, UPDATE_ALL);
					ExtraDelight.logger.error("Jar Display Entity Invalid!");
				}

				return InteractionResult.SUCCESS;
			} else {
				BlockEntity be = level.getBlockEntity(pos);
				if (be != null && be instanceof JarBlockEntity jdbe) {
					jdbe.use(player, hand);
				}
			}

		}
		return InteractionResult.PASS;
	}



	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
			Player player) {
		ItemStack stack = new ItemStack(this);
		FluidStack fluid = FluidStack.EMPTY;
		BlockEntity ent = level.getBlockEntity(pos);
		if (ent instanceof JarBlockEntity jbe) {
			//stack.set(ExtraDelightComponents.FLUID.get(), SimpleFluidContent.copyOf(jbe.getTank().getFluid()));
			fluid = ((JarBlockEntity) ent).getTank().getFluid();
			stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
		}
		return stack;
	}

	@Override
	public List<ItemStack> getDrops(BlockState pState, LootParams.Builder params) {
		// 确保有一个BlockEntity
		if (params.getParameter(LootContextParams.BLOCK_ENTITY) instanceof JarBlockEntity be) {
			ItemStack stack = new ItemStack(this);
			if(stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()){
				var f =stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
				if(f.isPresent()){
					var tank =f.get();
					tank.fill(be.getTank().getFluid(), IFluidHandler.FluidAction.EXECUTE);
				}
			}
			return Collections.singletonList(stack);
		}
		return super.getDrops(pState, params);
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

		if (pStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
			Optional<IFluidHandlerItem> resolve = pStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
			if (resolve.isPresent()) {
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (blockEntity instanceof JarBlockEntity jbe) {
                    jbe.getTank().setFluid(resolve.get().getFluidInTank(0));
                }
            }
        }

	}
}
