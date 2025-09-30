package com.lance5057.extradelight.gui;

import com.blamejared.crafttweaker.api.capability.Capabilities;
import com.lance5057.extradelight.util.BottleFluidRegistry;
import com.lance5057.extradelight.workstations.FancyTank;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.items.IItemHandler;
//import net.neoforged.neoforge.items.SlotItemHandler;

public class FancyTankInSlot extends SlotItemHandler {
	FancyTank tank;

	public FancyTankInSlot(IItemHandler itemHandler, FancyTank tank, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.tank = tank;
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return calcFluidInSlotSize(stack);
	}

	private int calcFluidInSlotSize(ItemStack pSlot) {
//		if (pSlot.getCapability(Capabilities.ITEM) != null && pSlot.getItem() != Items.BUCKET) {
//			if (pSlot.getCapability(Capabilities.ITEM).resolve().get().getStackInSlot(0).isEmpty())
//				return 0;
//			return 1;
//		} else {
////			if (pSlot.getItem() == Items.BUCKET)
////				return (tank.getCapacity() - tank.getTotalAmount()) / 1000;
//			if (!BottleFluidRegistry.getFluidFromBottle(pSlot).isEmpty())
//				return tank.getVacancy() / 250;
//			return 0;
//		}

//		var capOptional = pSlot.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
//		if (capOptional.isPresent() && pSlot.getItem() != Items.BUCKET) {
//			if (capOptional.get().getStackInSlot(0).isEmpty())
//				return 0;
//			return 1;
//		} else {
//			if (!BottleFluidRegistry.getFluidFromBottle(pSlot).isEmpty())
//				return tank.getVacancy() / 250;
//			return 0;
//		}

		if (pSlot.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() && pSlot.getItem() != Items.BUCKET) {
			var handler =pSlot.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
			if (handler!=null&&handler.getFluidInTank(0).isEmpty())
				return 0;
			return 1;
		} else {
//			if (pSlot.getItem() == Items.BUCKET)
//				return (tank.getCapacity() - tank.getTotalAmount()) / 1000;
			if (!BottleFluidRegistry.getFluidFromBottle(pSlot).isEmpty())
				return tank.getVacancy() / 250;
			return 0;
		}

	}

}
