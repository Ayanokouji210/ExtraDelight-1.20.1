package com.lance5057.extradelight.items;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
//import net.neoforged.neoforge.common.EffectCures;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GourmetHotCocoa extends DrinkableItem {
	public GourmetHotCocoa(Properties properties) {
		super(properties, false, true);
	}

//	@Override
//	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
//		Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
//		ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();
//
//		while (itr.hasNext()) {
//			MobEffectInstance effect = itr.next();
//			if (effect.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)
//					&& effect.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET))) {
//				compatibleEffects.add(effect.getEffect());
//			}
//		}
//
//		if (compatibleEffects.size() > 0) {
//			for (Holder<MobEffect> effect : compatibleEffects) {
//				MobEffectInstance selectedEffect = consumer.getEffect(effect);
//				if (selectedEffect != null && !net.neoforged.neoforge.event.EventHooks.onEffectRemoved(consumer,
//						selectedEffect, EffectCures.MILK)) {
//					consumer.removeEffect(selectedEffect.getEffect());
//				}
//			}
//		}
//	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
		// 在物品使用完成后执行效果
		if (!level.isClientSide) {
			this.affectConsumer(stack, level, consumer);
		}
		return super.finishUsingItem(stack, level, consumer);
	}
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
		// 获取实体所有活跃效果
		Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
		List<MobEffect> effectsToRemove = new ArrayList<>();

		// 遍历效果，找出可被牛奶治愈的有害效果
		while (itr.hasNext()) {
			MobEffectInstance effect = itr.next();
			if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
				// 检查效果是否可以被牛奶治愈
				if (isCuredByMilk(effect)) {
					effectsToRemove.add(effect.getEffect());
				}
			}
		}

		// 移除所有符合条件的效果
		for (MobEffect effect : effectsToRemove) {
			consumer.removeEffect(effect);
		}
	}

	// 判断一个效果是否可以被牛奶治愈
	private boolean isCuredByMilk(MobEffectInstance effectInstance) {
		// 获取效果的治愈物品列表
		List<ItemStack> curativeItems = effectInstance.getCurativeItems();

		// 检查牛奶桶是否在治愈物品列表中
		for (ItemStack stack : curativeItems) {
			if (stack.getItem() == Items.MILK_BUCKET) {
				return true;
			}
		}
		return false;
	}

}
