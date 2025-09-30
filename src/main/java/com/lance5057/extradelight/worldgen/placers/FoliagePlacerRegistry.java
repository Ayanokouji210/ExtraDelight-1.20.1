package com.lance5057.extradelight.worldgen.placers;

import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;

public class FoliagePlacerRegistry {
	public static final DeferredRegister<FoliagePlacerType<?>> PLACER = DeferredRegister
			.create(Registries.FOLIAGE_PLACER_TYPE, ExtraDelight.MOD_ID);

	public static final RegistryObject<FoliagePlacerType<FruitLeafPlacer>> FRUIT_LEAF = PLACER
			.register("fruit_leaf", () -> new FoliagePlacerType<>(FruitLeafPlacer.CODEC.codec()));
}
