package com.lance5057.extradelight.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.MilkBottleItem;

public class BottleFoodItem extends Item {
    public BottleFoodItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack $$3 = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        return pEntityLiving instanceof Player && ((Player)pEntityLiving).getAbilities().instabuild ? $$3 : new ItemStack(Items.GLASS_BOTTLE);
    }
}
