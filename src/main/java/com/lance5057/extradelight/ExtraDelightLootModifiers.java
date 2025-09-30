package com.lance5057.extradelight;

import com.lance5057.extradelight.loot.FoodLoot;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
//import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
//import net.neoforged.neoforge.registries.DeferredRegister;
//import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Locale;
import java.util.function.Supplier;

public class ExtraDelightLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExtraDelight.MOD_ID);

	public static final RegistryObject<Codec<FoodLoot>> FOOD_LOOT = LOOT_MODIFIERS.register("food_loot", () -> FoodLoot.CODEC);

	public static void register(IEventBus eventBus){
		LOOT_MODIFIERS.register(eventBus);
	}

}
