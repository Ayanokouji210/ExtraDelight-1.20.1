package com.lance5057.extradelight.worldgen.features;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.worldgen.features.crops.*;
import com.lance5057.extradelight.worldgen.features.trees.CinnamonTreeFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExtraDelightFeatures {

    // 1.20.1 Forge 使用 DeferredRegister.create(...)
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, ExtraDelight.MOD_ID);

    // ---------- 作物 ----------
    public static final RegistryObject<WildCornFeature> PATCH_WILD_CORN =
            FEATURES.register("wild_corn", WildCornFeature::new);

    public static final RegistryObject<WildCropFeature> PATCH_WILD_GINGER =
            FEATURES.register("wild_ginger", () -> new WildCropFeature(ExtraDelightBlocks.WILD_GINGER.get()));

    public static final RegistryObject<WildBushStageFourFeature> PATCH_WILD_COFFEE =
            FEATURES.register("wild_coffee", () -> new WildBushStageFourFeature(ExtraDelightBlocks.COFFEE_BUSH.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_CHILI =
            FEATURES.register("wild_chili", () -> new WildCropFeature(ExtraDelightBlocks.WILD_CHILI.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_MALLOW =
            FEATURES.register("wild_mallow", () -> new WildCropFeature(ExtraDelightBlocks.WILD_MALLOW_ROOT.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_PEANUT =
            FEATURES.register("wild_peanut", () -> new WildCropFeature(ExtraDelightBlocks.WILD_PEANUT.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_MINT =
            FEATURES.register("wild_mint", () -> new WildCropFeature(ExtraDelightBlocks.MINT_CROP.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_GARLIC =
            FEATURES.register("wild_garlic", () -> new WildCropFeature(ExtraDelightBlocks.WILD_GARLIC.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_CUCUMBER =
            FEATURES.register("wild_cucumber", () -> new WildCropFeature(Fermentation.WILD_CUCUMBER.get()));

    public static final RegistryObject<WildCropFeature> PATCH_WILD_SOYBEAN =
            FEATURES.register("wild_soybean", () -> new WildCropFeature(Fermentation.WILD_SOYBEAN.get()));

    // ---------- 树 ----------
    public static final RegistryObject<CinnamonTreeFeature> PATCH_CINNAMON_TREE =
            FEATURES.register("cinnamon_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_HAZELNUT_TREE =
            FEATURES.register("hazelnut_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_APPLE_TREE =
            FEATURES.register("apple_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_LEMON_TREE = FEATURES
            .register("lemon_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_LIME_TREE = FEATURES
            .register("lime_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_ORANGE_TREE = FEATURES
            .register("orange_tree", CinnamonTreeFeature::new);

    public static final RegistryObject<CinnamonTreeFeature> PATCH_GRAPEFRUIT_TREE = FEATURES
            .register("grapefruit_tree", CinnamonTreeFeature::new);


    // ---------- 结构 ----------
    public static final RegistryObject<CornMazeFeature> CORN_MAZE_FEATURE =
            FEATURES.register("corn_maze_feature", CornMazeFeature::new);

    public static final RegistryObject<CornMazeRail> CORN_RAIL_FEATURE =
            FEATURES.register("corn_rail_feature", CornMazeRail::new);
}