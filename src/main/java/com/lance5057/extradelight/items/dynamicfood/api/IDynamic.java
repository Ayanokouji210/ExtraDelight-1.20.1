package com.lance5057.extradelight.items.dynamicfood.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public interface IDynamic {
	List<ResourceLocation> getPieces(ItemStack itemStack);
}
