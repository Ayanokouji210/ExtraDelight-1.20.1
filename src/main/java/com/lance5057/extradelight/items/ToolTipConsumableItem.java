package com.lance5057.extradelight.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class ToolTipConsumableItem extends Item {

	private final boolean hasFoodEffectTooltip;
	private final boolean hasCustomTooltip;
    private final Item finishUsingItem;

	public ToolTipConsumableItem(Properties properties) {
		super(properties);
		this.hasFoodEffectTooltip = false;
		this.hasCustomTooltip = false;
        this.finishUsingItem = Items.AIR;
	}

	public ToolTipConsumableItem(Properties properties, boolean hasFoodEffectTooltip) {
		super(properties);
		this.hasFoodEffectTooltip = hasFoodEffectTooltip;
		this.hasCustomTooltip = false;
        this.finishUsingItem = Items.AIR;
	}

	public ToolTipConsumableItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
		super(properties);
		this.hasFoodEffectTooltip = hasFoodEffectTooltip;
		this.hasCustomTooltip = hasCustomTooltip;
        this.finishUsingItem = Items.AIR;
	}

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pLivingEntity);
        if(pLivingEntity instanceof Player player) {
            if (this.finishUsingItem.equals(Items.AIR)) {
                if (!getCraftingRemainingItem(pStack).isEmpty()) {
                    if (!player.getAbilities().instabuild && !player.getInventory().add(getCraftingRemainingItem(pStack))) {
                        player.drop(getCraftingRemainingItem(pStack), false);
                    }
                } else {
                    if (!player.getAbilities().instabuild && !player.getInventory().add(new ItemStack(Items.BOWL))) {
                        player.drop(new ItemStack(Items.BOWL), false);
                    }
                }
            } else {
                if (!player.getAbilities().instabuild && !player.getInventory().add(new ItemStack(this.finishUsingItem))) {
                    player.drop(new ItemStack(this.finishUsingItem), false);
                }
            }
        }
        return itemstack;

    }

    @Override
	public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			if (this.hasCustomTooltip) {
				MutableComponent textEmpty = TextUtils
						.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath());
				tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
			}
			if (this.hasFoodEffectTooltip) {
				TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
			}
		}
	}

//	@Override
//	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
//			TooltipFlag isAdvanced) {
//		if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
//			if (this.hasCustomTooltip) {
//				MutableComponent textEmpty = TextUtils
//						.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath());
//				tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
//			}
//			if (this.hasFoodEffectTooltip) {
//				TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
//			}
//		}
//	}
}
