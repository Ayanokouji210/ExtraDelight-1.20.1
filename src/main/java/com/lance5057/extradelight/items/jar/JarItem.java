package com.lance5057.extradelight.items.jar;

import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.blocks.IDisplayInteractable;
import com.lance5057.extradelight.blocks.jar.JarBlock;
import com.lance5057.extradelight.blocks.jar.JarBlockEntity;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.fluids.SimpleFluidContent;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Objects;

public class JarItem extends BlockItem implements IDisplayInteractable {

	public JarItem(JarBlock jarBlock, Properties p_41383_) {
		super(jarBlock, p_41383_);
	}


	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ICapabilityProvider() {
			private final LazyOptional<IFluidHandlerItem> capability =LazyOptional.of(()->new FluidHandlerItemStack(stack,1000));
			@Override
			public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
				if(capability == ForgeCapabilities.FLUID_HANDLER_ITEM){
					return this.capability.cast();
				}
				return LazyOptional.empty();
			}
		};
	}

	@Override
	public void appendHoverText(ItemStack stack, Level pLevel, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

		var f = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM,null);

		if (f != null && f.isPresent()) {
			var handlerItem=f.resolve().get();

            if (handlerItem != null&&!handlerItem.getFluidInTank(0).isEmpty()) {
                tooltipComponents.add(Component.literal("Contains: " + handlerItem.getTankCapacity(0) + "mb - ")
                        .append(Component.translatable(handlerItem.getFluidInTank(0).getFluid().getFluidType().getDescriptionId()))
                        .withStyle(ChatFormatting.WHITE));
            }
        }
	}

	@Override
	protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
		if(pContext.getLevel().getFluidState(pContext.getClickedPos()).isSource()){
			return false;
		}
		return super.canPlace(pContext, pState);
	}

	@Override
	protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
		boolean placed= super.placeBlock(pContext, pState);
		if(pContext.getLevel().getFluidState(pContext.getClickedPos().above()).isSource()){
			return false;
		}
		return placed;
	}

	@Override
	public Block getBlock() {
		return super.getBlock();

	}




	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		Item item= pContext.getItemInHand().getItem();
		if(item == null) {
			return super.useOn(pContext);
		}
		super.useOn(pContext);
		if(pContext.getLevel().getFluidState(pContext.getClickedPos().above()).isSource()){
			FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos().above());
			FluidStack fluidStack = new FluidStack(fluidState.getType(), 1000);
			ItemStack stack = pContext.getItemInHand();
			ItemStack newStack = stack.getItem().getDefaultInstance();
			stack.shrink(1);
			newStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
				if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)>0){
					handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
					pContext.getLevel().setBlock(pContext.getClickedPos().above(),Blocks.AIR.defaultBlockState(),2);
				}
			});
			Objects.requireNonNull(pContext.getPlayer()).addItem(newStack);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult itemInteract(ItemStack heldItem, ItemStack interactItem, BlockState state, Level level,
			BlockPos pos, Player player, InteractionHand hand) {
		
		return InteractionResult.SUCCESS;
	}

	@Override
	public void extractItem(Level level, BlockPos pos, Player player, JarDisplayBlockEntity jdbe,
							ItemStackHandler handler, ItemStack s, int index) {
		if (!player.addItem(s)) {
			level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), s.copy()));
			handler.setStackInSlot(index, ItemStack.EMPTY);
		}
	}

//	@Override
//	public void extractItem(Level level, BlockPos pos, Player player, JarDisplayBlockEntity jdbe,
//			ItemStackHandler handler, ItemStack s, int index) {
//
//		if (!player.addItem(s)) {
//			level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), s.copy()));
//			handler.setStackInSlot(index, ItemStack.EMPTY);
//		}
//
//	}

	@Override
	public void convertToSingular(BlockPos pos, Level level, JarDisplayBlockEntity jdbe, ItemStack stack) {
		BlockState bs = this.getBlock().defaultBlockState();
		FluidStack fluid = FluidStack.EMPTY;
		if (stack.getCapability(ExtraDelightComponents.FLUID).isPresent()) {
			fluid = stack.getCapability(ExtraDelightComponents.FLUID).orElse(null).getFluid();
		}
		level.setBlock(pos, bs, Block.UPDATE_ALL);
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof JarBlockEntity jbe)
			jbe.getTank().fill(fluid.copy(), IFluidHandler.FluidAction.EXECUTE);
	}

	public class JarFluidHandler implements IFluidHandler {
		private FluidTank tank;

		public JarFluidHandler(FluidTank tank) {
			this.tank = tank;
		}

		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public @Nonnull FluidStack getFluidInTank(int i) {
			if(i==0){
				return tank.getFluid();
			}
			return FluidStack.EMPTY;
		}

		@Override
		public int getTankCapacity(int i) {
			return tank.getCapacity();
		}

		@Override
		public boolean isFluidValid(int i, @Nonnull FluidStack fluidStack) {
			if(this.tank.isFluidValid(fluidStack)){
				return true;
			}
			return false;
		}

		@Override
		public int fill(FluidStack fluidStack, FluidAction fluidAction) {
			if(tank.isFluidValid(fluidStack)) {
				int fill = tank.fill(fluidStack, FluidAction.SIMULATE);
				if(fill==fluidStack.getAmount()){
					tank.fill(fluidStack,FluidAction.EXECUTE);
				}else {

				}
			}
			return 0;
		}

		@Override
		public @Nonnull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
			return null;
		}

		@Override
		public @Nonnull FluidStack drain(int i, FluidAction fluidAction) {
			return null;
		}
	}

}
