package com.lance5057.extradelight.workstations.evaporator.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
//import net.minecraft.world.item.crafting.RecipeInput;

//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class EvaporatorRecipeWrapper implements Container {

	final FluidTank tank;

	public EvaporatorRecipeWrapper(FluidTank tank) {
		this.tank = tank;
	}

	public FluidTank getTank() {
		return tank;
	}

	@Override
	public ItemStack getItem(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int i, int i1) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {

	}

	@Override
	public void setChanged() {

	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
        return false;
    }

	@Override
	public void clearContent() {

	}

	//container





}
