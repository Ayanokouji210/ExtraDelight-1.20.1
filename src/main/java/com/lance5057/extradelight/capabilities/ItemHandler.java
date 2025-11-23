package com.lance5057.extradelight.capabilities;

import com.google.common.collect.Iterables;
import com.lance5057.extradelight.ExtraDelightComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;

public class ItemHandler extends ItemStackHandler implements ExtraDelightComponents.EDItemStackHandler {

    public ItemHandler() {
        super(6);
    }

    @Override
    public void fromItems(List<ItemStack> stackList) {
        Iterator<ItemStack> iterator = stackList.iterator();
        this.stacks.forEach(stack -> {stack=iterator.hasNext() ? iterator.next(): ItemStack.EMPTY;});
    }

    @Override
    public Iterable<ItemStack> nonEmptyItems() {
      return Iterables.filter(this.stacks, (stack)->!stack.isEmpty());
    }
}
