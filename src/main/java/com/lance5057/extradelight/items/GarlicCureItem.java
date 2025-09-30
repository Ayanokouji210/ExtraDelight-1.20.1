package com.lance5057.extradelight.items;

import com.lance5057.extradelight.ExtraDelightItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//import net.neoforged.fml.ModList;
import net.minecraftforge.fml.ModList;
import javax.annotation.Nonnull;

public class GarlicCureItem extends Item {

	public GarlicCureItem(Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn,
			@Nonnull LivingEntity entityLiving) {
		if (!worldIn.isClientSide) {
			if (ModList.get().isLoaded("vampirism")) {

				//TODO: GARLIC_CURE
				//entityLiving.remove(ExtraDelightItems.GARLIC_CURE);
				entityLiving.removeEffect(ExtraDelightItems.GARLIC_CURE);
			}
		}
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}
}
