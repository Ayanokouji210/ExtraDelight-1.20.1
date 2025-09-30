package com.lance5057.extradelight.capabilities;

import net.minecraft.world.level.material.Fluid;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.IFluidTank;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import javax.annotation.Nonnull;


public class WellFluidCapability implements IFluidHandler, IFluidTank {

	private final Fluid fluid;

	public WellFluidCapability(Fluid fluid) {
		this.fluid = fluid;
	}

	@Nonnull
	@Override
	public FluidStack getFluid() {
		return new FluidStack(fluid, getFluidAmount());
	}

	@Override
	public int getFluidAmount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getCapacity() {
		return getFluidAmount();
	}

	@Override
	public boolean isFluidValid(FluidStack stack) {
		return stack.getFluid().isSame(fluid);
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return getFluidAmount();
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return resource.getAmount();
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !resource.isFluidEqual(resource)) {
			return FluidStack.EMPTY;
		}
		return drain(resource.getAmount(), action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return new FluidStack(fluid, maxDrain);
	}

}
