package com.lance5057.extradelight.workstations.mixingbowl.recipes;

import com.lance5057.extradelight.workstations.FancyTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
//import net.neoforged.neoforge.items.IItemHandler;
//import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class MixingBowlRecipeWrapper extends RecipeWrapper {

	final FancyTank tank;

	public MixingBowlRecipeWrapper(IItemHandler inv, FancyTank tank) {
		super((IItemHandlerModifiable) inv);
		this.tank = tank;
	}

	public FancyTank getTank() {
		return tank;
	}
}
