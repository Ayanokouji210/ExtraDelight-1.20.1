package com.lance5057.extradelight;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class ExtraDelightBanners {
	public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister
			.create(Registries.BANNER_PATTERN, ExtraDelight.MOD_ID);

	public static final TagKey<BannerPattern> CITRUS_PATTERN_TAG = TagKey.create(Registries.BANNER_PATTERN,
			ExtraDelight.modLoc("citrus"));

    public static final RegistryObject<BannerPattern> CITRUS_PITH = BANNER_PATTERNS.register("banner_pattern_citrus_pith",
			() -> new BannerPattern("extradelight_banner_pattern_citrus_pith"));

	public static final RegistryObject<BannerPattern> CITRUS_FRUIT = BANNER_PATTERNS.register("banner_pattern_citrus_fruit",
			() -> new BannerPattern("extradelight_banner_pattern_citrus_fruit"));


//	public static final RegistryObject<BannerPattern> CITRUS_PITH = BANNER_PATTERNS.register("banner_pattern_citrus_pith",
//			() -> new BannerPattern(ExtraDelight.modLoc("banner_pattern_citrus_pith"),
//					"block.minecraft.banner.extradelight.banner_pattern_citrus_pith"));
//
//	public static final RegistryObject<BannerPattern> CITRUS_FRUIT = BANNER_PATTERNS.register("banner_pattern_citrus_fruit",
//			() -> new BannerPattern(ExtraDelight.modLoc("banner_pattern_citrus_fruit"),
//					"block.minecraft.banner.extradelight.banner_pattern_citrus_fruit"));

}
