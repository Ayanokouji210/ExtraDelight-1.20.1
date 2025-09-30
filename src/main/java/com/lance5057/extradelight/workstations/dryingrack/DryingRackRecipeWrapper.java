package com.lance5057.extradelight.workstations.dryingrack;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import javax.annotation.Nonnull;

public class DryingRackRecipeWrapper extends RecipeWrapper {
//    private IItemHandler input;
//
//    public DryingRackRecipeWrapper(IItemHandler input) {
//        super((IItemHandlerModifiable) input);
//        this.input = input;
//    }
//
//    public @Nonnull ItemStack getItem(int index) {
//        if(index!=0){
//            throw new IllegalArgumentException("no item for index"+index);
//        }else{
//            return this.input.getStackInSlot(index);
//        }
//    }
//
//    public int size() {
//        return 1;
//    }

    private IItemHandler input;

    public DryingRackRecipeWrapper(IItemHandler input) {
        super((IItemHandlerModifiable) input);
        this.input = input;
    }

    @Override
    public int getContainerSize() {
        return input.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < input.getSlots(); i++) {
            if (!input.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index < 0 || index >= input.getSlots()) {
            return ItemStack.EMPTY;
        }
        return input.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index < 0 || index >= input.getSlots()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = input.getStackInSlot(index).copy();
        if (stack.getCount() <= count) {
            input.extractItem(index, stack.getCount(), false);
            return stack;
        } else {
            ItemStack result = stack.split(count);
            input.extractItem(index, count, false);
            return result;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index < 0 || index >= input.getSlots()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = input.getStackInSlot(index).copy();
        input.extractItem(index, stack.getCount(), false);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < input.getSlots()) {
            input.extractItem(index, input.getStackInSlot(index).getCount(), false);
            input.insertItem(index, stack, false);
        }
    }

    @Override
    public void setChanged() {
        // 不需要实现
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < input.getSlots(); i++) {
            input.extractItem(i, input.getStackInSlot(i).getCount(), false);
        }
    }

}
