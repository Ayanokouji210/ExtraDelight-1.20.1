package com.lance5057.extradelight.items;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
//import net.neoforged.neoforge.common.EffectCures;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.ArrayList;
import java.util.Iterator;

public class XocolatlItem extends DrinkableItem {
	public XocolatlItem(Properties properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
		consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FoodValues.MEDIUM_DURATION));
		
		Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
		ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();

		while (itr.hasNext()) {
			MobEffectInstance effect = itr.next();
			if (effect.getEffect().getCategory().equals(MobEffectCategory.HARMFUL) && effect.getCurativeItems().contains(new ItemStack(Items.MILK_BUCKET))) {
				compatibleEffects.add((Holder<MobEffect>) effect.getEffect());
			}
		}

		if (compatibleEffects.size() > 0) {
			MobEffectInstance selectedEffect = consumer.getEffect(compatibleEffects.get(level.random.nextInt(compatibleEffects.size())).get());
			if (selectedEffect != null) {
				consumer.removeEffect(selectedEffect.getEffect());
			}
		}

	}
}