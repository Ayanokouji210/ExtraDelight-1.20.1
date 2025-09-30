package com.lance5057.extradelight.items;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class DrinkItem extends DrinkableItem {


    public DrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pLivingEntity);
        return pLivingEntity instanceof Player && ((Player)pLivingEntity).getAbilities().instabuild ? itemstack : new ItemStack(Items.GLASS_BOTTLE);
    }
}
