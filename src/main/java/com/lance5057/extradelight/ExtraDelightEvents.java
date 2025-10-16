package com.lance5057.extradelight;

import com.lance5057.extradelight.items.components.ChillComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.awt.event.ItemEvent;

import static com.lance5057.extradelight.ExtraDelightConfig.*;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExtraDelight.MOD_ID)
public class ExtraDelightEvents {
//    @SubscribeEvent
//    public static void onConfigReloading(ModConfigEvent.Reloading event) {
//        if (event.getConfig().getSpec() == ExtraDelightConfig.spec) {
//            ExtraDelight.logger.info("Reloading ExtraDelight Config");
//            ExtraDelightConfig.ENABLE_DEBUG_MODE = event.getConfig().getSpec().get("enableDebug");
//            ExtraDelightConfig.ENABLE_RECIPE_BOOK_OVEN = event.getConfig().getSpec().get("enableRecipeBookOven");
//            ExtraDelightConfig.MINT_SPREAD = event.getConfig().getSpec().get("shouldMintSpread");
//            ExtraDelightConfig.MINT_SPREAD_RATE = event.getConfig().getSpec().get("mintSpreadRate");
//            ExtraDelightConfig.ALL_YEAR = event.getConfig().getSpec().get("allYearSpooky");
//        }
//    }


//	@SubscribeEvent
//	public static void modifyComponents(ModifyDefaultComponentsEvent event) {
//		event.modify(Items.BLUE_ICE,
//				builder -> builder.set(ExtraDelightComponents.CHILL.value(), new ChillComponent(2500)));
//		event.modify(Items.PACKED_ICE,
//				builder -> builder.set(ExtraDelightComponents.CHILL.value(), new ChillComponent(1000)));
//		event.modify(Items.ICE, builder -> builder.set(ExtraDelightComponents.CHILL.value(), new ChillComponent(250)));
//		event.modify(Items.SNOWBALL,
//				builder -> builder.set(ExtraDelightComponents.CHILL.value(), new ChillComponent(100)));
//
//	}

}
