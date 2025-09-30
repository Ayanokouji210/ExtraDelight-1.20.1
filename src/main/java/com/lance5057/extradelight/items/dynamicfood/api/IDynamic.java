package com.lance5057.extradelight.items.dynamicfood.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface IDynamic {
	Collection<ResourceLocation> getPieces(ItemStack itemStack);
}
