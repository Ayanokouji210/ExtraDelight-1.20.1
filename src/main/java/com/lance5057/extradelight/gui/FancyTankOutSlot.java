package com.lance5057.extradelight.gui;

import com.lance5057.extradelight.util.BottleFluidRegistry;
import com.lance5057.extradelight.workstations.FancyTank;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FancyTankOutSlot extends SlotItemHandler {
	FancyTank tank;

	public FancyTankOutSlot(IItemHandler itemHandler, FancyTank tank, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.tank = tank;
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return calcFluidOutSlotSize(stack);
	}

	private int calcFluidOutSlotSize(ItemStack pSlot) {
		if (pSlot.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().isPresent() && !pSlot.is(Items.BUCKET)
				&& !pSlot.getCraftingRemainingItem().is(Items.BUCKET))
			return 1;
		else {
			if (pSlot.getItem() == Items.BUCKET)
				return tank.getFluidAmount(0) / 1000;
			else if (ItemStack.isSameItem(pSlot,
					BottleFluidRegistry.getBottleFromFluid(tank.drain(250, IFluidHandler.FluidAction.SIMULATE)).getCraftingRemainingItem()))
				return tank.getFluidAmount(0) / 250;
			else if (tank.drain(250, IFluidHandler.FluidAction.SIMULATE).getFluid()==Fluids.WATER && pSlot.is(Items.GLASS_BOTTLE))
				return tank.getFluidAmount(0) / 250;
			return 0;
		}
	}
}
