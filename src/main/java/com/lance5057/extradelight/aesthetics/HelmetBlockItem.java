package com.lance5057.extradelight.aesthetics;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;

public class HelmetBlockItem extends BlockItem {

	public HelmetBlockItem(Block block, Properties pProperties) {
		super(block, pProperties);
		// TODO Auto-generated constructor stub
	}


	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
		return armorType == EquipmentSlot.HEAD;
	}

	@Override
	@Nullable
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}
}
