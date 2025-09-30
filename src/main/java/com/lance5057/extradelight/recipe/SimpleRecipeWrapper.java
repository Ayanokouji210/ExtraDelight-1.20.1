package com.lance5057.extradelight.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Arrays;
import java.util.List;

public class SimpleRecipeWrapper implements Container {
	List<ItemStack> items;

	public SimpleRecipeWrapper(ItemStack...itemStacks)
	{
		items = Arrays.asList(itemStacks);
	}


	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int index) {
		return items.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		ItemStack stack = items.get(index);
		return stack.split(count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack stack = items.get(index);
		items.set(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		items.set(index, stack);
	}

	@Override
	public void setChanged() {
		// 不需要实现
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		for (int i = 0; i < items.size(); i++) {
			items.set(i, ItemStack.EMPTY);
		}
	}

//	@Override
//	public ItemStack getItem(int index) {
//		return items.get(index);
//	}
//
//
//
//	@Override
//	public int size() {
//		return items.size();
//	}

}
