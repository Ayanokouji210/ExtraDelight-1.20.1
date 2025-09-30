package com.lance5057.extradelight.workstations;

import com.lance5057.extradelight.fluids.FluidKey;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.checkerframework.checker.units.qual.K;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.IFluidTank;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class FancyTank implements IFluidHandler, IFluidTank {
	protected Predicate<FluidStack> validator;
	protected LinkedHashMap<FluidKey, Integer> fluid;
	protected int capacity;
	protected final int variety_cap;
	public int getVarietyCap() {
		return variety_cap;
	}

	private boolean full;
	private boolean slots_full; // for enforcing variety_cap

	public FancyTank(int capacity) {
		this(capacity, e -> true, 6);
	}
	
	public FancyTank(int capacity, Predicate<FluidStack> validator) {
		this(capacity, validator, 6);
	}
	
	public FancyTank(int capacity, Predicate<FluidStack> validator, int variety_cap) {
		this.capacity = capacity;
		this.validator = validator;
		this.fluid = new LinkedHashMap<>();
		this.full = false;
		this.variety_cap = variety_cap;
		this.slots_full = false;
	}

	public int getTotalAmount() {
		return fluid.values().stream().mapToInt(i -> i).sum();
	}

	public FancyTank setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public FancyTank setValidator(Predicate<FluidStack> validator) {
		if (validator != null) {
			this.validator = validator;
		}
		return this;
	}

	public boolean isFluidValid(FluidStack stack) {
		return validator.test(stack);
	}

	public int getCapacity(int tank) {
		return capacity / variety_cap;
	}

	public FluidStack getFluid(int tank) {
		Iterator<Entry<FluidKey, Integer>> itr = fluid.entrySet().iterator();
		while (tank-- > 0 && itr.hasNext()) {
			itr.next();
		}
		if (!itr.hasNext())
			return FluidStack.EMPTY;
		var tmp = itr.next();
		FluidStack target = tmp.getKey().createStack(tmp.getValue());
		return target;
	}

	public int getFluidAmount(int tank) {
		FluidStack target = getFluid(tank);
		return target.getAmount();
	}

	public synchronized FancyTank readFromNBT(CompoundTag nbt) {
		this.fluid.clear();
		this.slots_full = false;
//		if (nbt.contains("FluidCount")){
//			for(int i=0;i<nbt.getInt("FluidCount");i++){
//
//			}
//		}
		int i = 0;

		while (nbt.contains("Fluid_" + i)) {
			FluidStack dat = FluidStack.loadFluidStackFromNBT(nbt.getCompound("Fluid_" + i));
			if (!dat.isEmpty())
				fill(dat, FluidAction.EXECUTE);
			i++;
		}
		return this;
	}

	public synchronized CompoundTag writeToNBT(CompoundTag nbt) {
		int index = 0;
		for (Entry<FluidKey, Integer> entry : fluid.entrySet()) {
			if (entry.getValue() > 0) {
				FluidStack stack = entry.getKey().createStack(entry.getValue());
				if (!stack.isEmpty()) {
					nbt.put("Fluid_"+index,stack.writeToNBT(new CompoundTag()));
					index++;
				}
			}
		}
		nbt.putInt("FluidCount", index);
		return nbt;
	}


	public synchronized CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        int index = 0;
        for (Entry<FluidKey, Integer> entry : fluid.entrySet()) {
            if (entry.getValue() > 0) {
                FluidStack stack = entry.getKey().createStack(entry.getValue());
                if (!stack.isEmpty()) {
                    nbt.put("Fluid_"+index,stack.writeToNBT(new CompoundTag()));
                    index++;
                }
            }
        }
        return nbt;
    }

	@Override
	public int getTanks() {
		return fluid.size();
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid(tank);
	}

	@Override
	public int getTankCapacity(int tank) {
		return capacity;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return isFluidValid(tank, stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int fill = doFill(resource, action);
		return fill;
	}
	
	public int getVacancy() {
		return capacity - getTotalAmount();
	}
	
	private synchronized int doFill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(resource)) return 0;
		if (slots_full) {
			FluidKey key = new FluidKey(resource);
			if(!fluid.containsKey(key))return 0;
		}
		if (full) return 0;

		int vacancy = capacity - getTotalAmount();
		int fillTotal = resource.getAmount();
		boolean applyChanges = action.simulate() ? false : true;

		if (vacancy < fillTotal) {
			if (applyChanges && vacancy == 0)
				full = true;
			return 0;
		}
		var key = new FluidKey(resource);
		
		if (fillTotal <= 0) return 0;
		if (applyChanges) {
			fluid.merge(key, resource.getAmount(), Integer::sum);
			onContentsChanged();
		}
		
		return fillTotal;
	}
	/*
	 * @Override public int fill(FluidStack resource, FluidAction action) { for (int
	 * i = 0; i < this.getTanks(); i++) { int fill = doFill(resource, action, i); if
	 * (fill != 0) return fill; } return 0; }
	 * 
	 * 
	 * private int doFill(FluidStack resource, FluidAction action, int tank) { if
	 * (resource.isEmpty() || !isFluidValid(resource)) { return 0; } if
	 * (action.simulate()) { if (fluid[tank].isEmpty()) { return Math.min(capacity,
	 * resource.getAmount()); } if
	 * (!FluidStack.isSameFluidSameComponents(fluid[tank], resource)) { return 0; }
	 * return Math.min(capacity - fluid[tank].getAmount(), resource.getAmount()); }
	 * if (fluid[tank].isEmpty()) { fluid[tank] =
	 * resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
	 * onContentsChanged(); return fluid[tank].getAmount(); } if
	 * (!FluidStack.isSameFluidSameComponents(fluid[tank], resource)) { return 0; }
	 * int filled = capacity - fluid[tank].getAmount();
	 * 
	 * if (resource.getAmount() < filled) { fluid[tank].grow(resource.getAmount());
	 * filled = resource.getAmount(); } else { fluid[tank].setAmount(capacity); } if
	 * (filled > 0) onContentsChanged(); return filled; }
	 */

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return doDrain(resource, action);
	}

	public FluidStack drain(FluidIngredient resource, FluidAction action) {
		FluidStack tmp =resource.getMatchingFluidStacks().get(0);
		//FluidStack tmp = resource.getFluids()[0];
		return doDrain(tmp, action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		Iterator<Entry<FluidKey, Integer>> itr = fluid.entrySet().iterator();
		while (itr.hasNext()) {
			var i = itr.next();
			FluidStack s = doDrain(i.getKey().createStack(maxDrain), action);
			if (!s.isEmpty())
				return s;
		}
		return FluidStack.EMPTY;
	}

	private FluidStack doDrain(FluidStack drain, FluidAction action) {
		return doDrain(drain, action, false);
	}

	private synchronized FluidStack doDrain(FluidStack drain, FluidAction action, boolean iff) {
		// iff - this will succeed if and only if maxDrain <= amount stored; default is
		// false
		int maxDrain = drain.getAmount();
		if (maxDrain <= 0)
			return FluidStack.EMPTY;

		FluidKey key = new FluidKey(drain);
		int keyval = fluid.getOrDefault(key, -1);
		if (keyval == -1)
			return FluidStack.EMPTY;
		if (iff && maxDrain > keyval)
			return FluidStack.EMPTY;

		boolean applyChanges = action.simulate() ? false : true;

		int maxDrainSanitized = Math.min(maxDrain, keyval);
		FluidStack stack = drain.copy();
		stack.setAmount(maxDrainSanitized);
		//FluidStack stack = drain.copyWithAmount(maxDrainSanitized);
		if (applyChanges) {
			fluid.merge(key, 0 - maxDrainSanitized, Integer::sum);
			if (fluid.getOrDefault(key, -1) <= 0)
				fluid.remove(key);
			if (full)
				full = false;
			onContentsChanged();
		}
		return stack;
	}

	/*
	 * @Override public FluidStack drain(FluidStack resource, FluidAction action) {
	 * for (int i = 0; i < this.getTanks(); i++) { if (!resource.isEmpty() &&
	 * FluidStack.isSameFluidSameComponents(resource, fluid[i])) { return
	 * doDrain(resource.getAmount(), action, i); } } return FluidStack.EMPTY; }
	 * 
	 * public FluidStack drain(SizedFluidIngredient resource, FluidAction action) {
	 * for (int i = 0; i < this.getTanks(); i++) { if (resource.test(fluid[i])) {
	 * return doDrain(resource.amount(), action, i); }
	 * 
	 * } return FluidStack.EMPTY; }
	 * 
	 * @Override public FluidStack drain(int maxDrain, FluidAction action) { for
	 * (int i = 0; i < this.getTanks(); i++) { FluidStack s = doDrain(maxDrain,
	 * action, i); if (!s.isEmpty()) return s; } return FluidStack.EMPTY; }
	 * 
	 * private FluidStack doDrain(int maxDrain, FluidAction action, int tank) { int
	 * drained = maxDrain; if (fluid[tank].getAmount() < drained) { drained =
	 * fluid[tank].getAmount(); } FluidStack stack =
	 * fluid[tank].copyWithAmount(drained); if (action.execute() && drained > 0) {
	 * fluid[tank].shrink(drained); onContentsChanged(); } return stack; }
	 */
	protected void onContentsChanged() {
		int variety = fluid.size();
		this.slots_full = (variety >= variety_cap);
	}

	@Override
	public FluidStack getFluid() {
		var iterator= this.fluid.entrySet().iterator();
		Entry<FluidKey,Integer> entry = null;
		while (iterator.hasNext()) {
			entry = iterator.next();
		}


		Entry<FluidKey, Integer> f = entry;
		if (f != null) {
			
			return f.getKey().createStack(f.getValue());
		}
		return FluidStack.EMPTY;
	}

	@Override
	public int getFluidAmount() {
		return 1000;
	}

	@Override
	public int getCapacity() {
		return 0;
	}

//	public void setFluid(FluidStack stack, int tank) {
//		this.fluid[tank] = stack;
//	}

	public boolean isEmpty(int tank) {
		return (getFluidAmount(tank) == 0);
	}

	public int getSpace(int tank) {
		return Math.max(0, capacity - getFluidAmount(tank));
	}

	public List<FluidStack> getAsList() {
		List<FluidStack> l = new ArrayList<FluidStack>();
		for (int i = 0; i < this.getTanks(); i++)
			l.add(getFluid(i));
		return l;
	}
}
