package com.lance5057.extradelight.fluids;

//import net.neoforged.neoforge.fluids.FluidStack;

import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidKey {
	private final FluidStack fluid;
	
	public FluidKey(FluidStack fluid) {
		this.fluid = new FluidStack(fluid.getFluid(), 1);
		this.fluid.setTag(fluid.getTag());
		//this.fluid.applyComponents(fluid.getComponents().copy());
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof FluidKey)) return false;
		return this.fluid.equals(((FluidKey) o).fluid);
//		if(!(o instanceof FluidKey other)) return false;
//		return FluidStack.isSameFluidSameComponents(this.fluid,other.fluid);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(fluid.getFluid(),fluid.getTag());
	}
	
	public FluidStack createStack(int amount) {
		FluidStack res = new FluidStack(fluid.getFluid(),amount);
		if(fluid.getTag() != null) {
			res.setTag(fluid.getTag().copy());
		}
		return res;
	}



}