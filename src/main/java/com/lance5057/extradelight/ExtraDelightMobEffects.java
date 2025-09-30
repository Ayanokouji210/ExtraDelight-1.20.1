package com.lance5057.extradelight;

import com.lance5057.extradelight.effects.PickledEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;

public class ExtraDelightMobEffects {
	private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT,
			ExtraDelight.MOD_ID);

	public static final RegistryObject<PickledEffect> PICKLED = EFFECTS.register("pickled",
			PickledEffect::new);

	public static void register(IEventBus modBus) {
		EFFECTS.register(modBus);
	}
}
