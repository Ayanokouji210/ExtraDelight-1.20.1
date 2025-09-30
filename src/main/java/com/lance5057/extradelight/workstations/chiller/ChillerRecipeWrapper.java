package com.lance5057.extradelight.workstations.chiller;

//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.items.IItemHandler;
//import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ChillerRecipeWrapper extends RecipeWrapper {
	FluidStack tank;

	public ChillerRecipeWrapper(IItemHandler inv, FluidStack tank) {
		super((IItemHandlerModifiable) inv);
		this.tank = tank;
	}

}
