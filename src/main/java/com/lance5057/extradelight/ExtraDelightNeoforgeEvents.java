package com.lance5057.extradelight;

import com.lance5057.extradelight.modules.Fermentation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.common.BasicItemListing;
//import net.neoforged.neoforge.common.NeoForgeMod;
//import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
//import net.neoforged.neoforge.event.village.VillagerTradesEvent;
//import net.neoforged.neoforge.event.village.WandererTradesEvent;

@Mod.EventBusSubscriber(modid = ExtraDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExtraDelightNeoforgeEvents {
	@SubscribeEvent
	public static void picklePoison(LivingAttackEvent event) {
		if (event.getSource().is(DamageTypes.MAGIC)) {
			LivingEntity le = event.getEntity();
			if (le.hasEffect(ExtraDelightMobEffects.PICKLED.get())) {
				float amp = le.getEffect(ExtraDelightMobEffects.PICKLED.get()).getAmplifier();

				if (le.level().getRandom().nextFloat() <= (amp + 1) / 4f) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void villagerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.FARMER) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ExtraDelightItems.UNSHUCKED_CORN.get(), 12),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ExtraDelightItems.CACTUS.get(), 8),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ExtraDelightItems.GINGER.get(), 12),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ExtraDelightItems.CHILI.get(), 16),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ExtraDelightItems.MALLOW_ROOT.get(), 6),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(3)
					.add(new BasicItemListing(new ItemStack(ExtraDelightItems.COFFEE_CHERRIES.get(), 16),
							new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(1)
					.add(new BasicItemListing(new ItemStack(ExtraDelightItems.PEANUTS_IN_SHELL.get(), 22),
							new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(2)
					.add(new BasicItemListing(new ItemStack(ExtraDelightItems.HAZELNUTS_IN_SHELL.get(), 22),
							new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ExtraDelightItems.MINT.get(), 32),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ExtraDelightItems.GARLIC.get(), 12),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Fermentation.CUCUMBER.get(), 8),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Fermentation.SOYBEAN_POD.get(), 12),
					new ItemStack(Items.EMERALD), 2, 16, 0.05F));

		}
	}

	@SubscribeEvent
	public static void onWandererTrades(WandererTradesEvent event) {
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
				new ItemStack(ExtraDelightItems.APPLE_SAPLING.get(), 1), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 9),
				new ItemStack(ExtraDelightItems.CINNAMON_SAPLING.get(), 1), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 5),
				new ItemStack(ExtraDelightItems.HAZELNUT_SAPLING.get(), 1), 2, 16, 0.05F));

		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 1),
				new ItemStack(Fermentation.SOYBEANS.get(), 3), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 1),
				new ItemStack(Fermentation.CUCUMBER_SEED.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
				new ItemStack(ExtraDelightItems.CHILI_SEEDS.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 1),
				new ItemStack(ExtraDelightItems.CORN_SEEDS.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
				new ItemStack(ExtraDelightItems.GARLIC.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 16),
				new ItemStack(ExtraDelightItems.MINT.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 7),
				new ItemStack(ExtraDelightItems.COFFEE_CHERRIES.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
				new ItemStack(ExtraDelightItems.MALLOW_ROOT.get(), 2), 2, 16, 0.05F));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
				new ItemStack(ExtraDelightItems.GINGER.get(), 2), 2, 16, 0.05F));
	}
}
