package com.lance5057.extradelight.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ItemNBTUtil {
    public static ItemStack ItemNBTSetInt(ItemStack stack ,String name, int amount) {
        CompoundTag tag=stack.getTag();
        if (tag==null) {
            tag=new CompoundTag();
        }
        tag.putInt(name, amount);
        stack.setTag(tag);
        return stack;
    }
}
