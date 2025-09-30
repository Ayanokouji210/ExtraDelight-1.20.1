package com.lance5057.extradelight.items;

import com.lance5057.extradelight.ExtraDelightItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//import net.neoforged.fml.ModList;
import net.minecraftforge.fml.ModList;
import javax.annotation.Nonnull;

public class GarlicTooltipItem extends ToolTipConsumableItem {

	public GarlicTooltipItem(Properties properties) {
		super(properties);
	}

	public GarlicTooltipItem(Properties properties, boolean hasFoodEffectTooltip) {
		super(properties, hasFoodEffectTooltip);
	}

	public GarlicTooltipItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
		super(properties, hasFoodEffectTooltip, hasCustomTooltip);
	}

	@Nonnull
	@Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn,
			@Nonnull LivingEntity entityLiving) {
		if (!worldIn.isClientSide) {
			if (ModList.get().isLoaded("vampirism"))
				entityLiving.removeEffect(ExtraDelightItems.GARLIC_CURE);
		}
		return super.finishUsingItem(stack, worldIn, entityLiving);
	}
}
