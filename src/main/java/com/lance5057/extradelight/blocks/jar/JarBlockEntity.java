package com.lance5057.extradelight.blocks.jar;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.util.BottleFluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
//import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.SimpleFluidContent;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class JarBlockEntity extends BlockEntity {
	public static final String FLUID_TAG = "fluid";
	private final FluidTank tank = createFluidHandler();

	public FluidTank getTank() {
		return tank;
	}

	public JarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(ExtraDelightBlockEntities.JAR.get(), p_155229_, p_155230_);
	}

	private FluidTank createFluidHandler() {
		FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME) {
			@Override
			protected void onContentsChanged() {
				JarBlockEntity.this.requestModelDataUpdate();
				JarBlockEntity.this.getLevel().sendBlockUpdated(JarBlockEntity.this.getBlockPos(),
						JarBlockEntity.this.getBlockState(), JarBlockEntity.this.getBlockState(), 3);
				JarBlockEntity.this.setChanged();
			}
		};

		return tank;
	}

//	@Override
//	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
//		super.collectImplicitComponents(builder);
//		builder.set(ExtraDelightComponents.FLUID.get(), SimpleFluidContent.copyOf(tank.getFluid()));
//	}
//
//	@Override
//	protected void applyImplicitComponents(DataComponentInput input) {
//		super.applyImplicitComponents(input);
//		tank.setFluid(input.getOrDefault(ExtraDelightComponents.FLUID.get(), SimpleFluidContent.EMPTY).copy());
//	}

	public float getFullness() {
		return (float) tank.getFluidAmount() / (float) tank.getCapacity();
	}

	public boolean use(Player player, InteractionHand hand) {
		ItemStack i = BottleFluidRegistry.getBottleFromFluid(this.getTank().getFluid()).getCraftingRemainingItem()
				.copy();
		if(this.getTank().getFluid().getFluid()==Fluids.WATER)
			i = Items.GLASS_BOTTLE.getDefaultInstance();
		ItemStack i2 = player.getItemInHand(hand);
		if (ItemStack.isSameItem(i2, i)) {

			if (!i.isEmpty()) {
				if (this.getTank().drain(250, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
					BlockEntityUtils.Inventory.givePlayerItemStack(
							BottleFluidRegistry.getBottleFromFluid(this.getTank().getFluid()).copy(), player, level,
							worldPosition);
					this.getTank().drain(250, IFluidHandler.FluidAction.EXECUTE);

					player.getItemInHand(hand).shrink(1);
					return true;
				}
			}
		} else if (!player.getItemInHand(hand).isEmpty()) {
			FluidStack stack = BottleFluidRegistry.getFluidFromBottle(player.getItemInHand(hand));
			if (!stack.isEmpty()) {
				if (this.getTank().fill(stack, IFluidHandler.FluidAction.SIMULATE) == 250) {
					this.getTank().fill(stack, IFluidHandler.FluidAction.EXECUTE);

					BlockEntityUtils.Inventory.givePlayerItemStack(
							player.getItemInHand(hand).getCraftingRemainingItem().copy(), player, level, worldPosition);
					if (player.getItemInHand(hand).is(Items.POTION))
						BlockEntityUtils.Inventory.givePlayerItemStack(Items.GLASS_BOTTLE.getDefaultInstance(), player,
								level, worldPosition);
					player.getItemInHand(hand).shrink(1);
					return true;
				} else
					return false;
			} else
				return FluidUtil.interactWithFluidHandler(player, hand, tank);
		}
		return FluidUtil.interactWithFluidHandler(player, hand, tank);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();

		writeNBT(nbt);

		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		readNBT(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		readNBT(tag);
	}

	void readNBT(CompoundTag nbt) {
		tank.readFromNBT(nbt);
	}

	CompoundTag writeNBT(CompoundTag tag) {

		tank.writeToNBT(tag);

		return tag;
	}

	@Override
	public void load(@Nonnull CompoundTag nbt) {
		super.load(nbt);
		readNBT(nbt);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt) {
		super.saveAdditional(nbt);
		writeNBT(nbt);
	}


}
