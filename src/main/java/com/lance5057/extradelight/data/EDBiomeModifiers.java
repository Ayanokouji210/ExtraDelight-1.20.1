package com.lance5057.extradelight.data;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.worldgen.config.WildConfig;
import com.lance5057.extradelight.worldgen.features.ExtraDelightFeatures;
import com.lance5057.extradelight.worldgen.features.trees.ExtraDelightTreeFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
//import net.neoforged.neoforge.common.Tags;
//import net.neoforged.neoforge.common.world.ForgeBiomeModifiers;
//import net.neoforged.neoforge.registries.NeoForgeRegistries;
//import net.neoforged.neoforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.common.world.ForgeBiomeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EDBiomeModifiers extends BaseDatapackRegistryProvider {

	protected EDBiomeModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, ExtraDelight.MOD_ID);
	}

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, context -> {
				HolderSet.Named<Biome> plains = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_PLAINS);
				HolderSet.Named<Biome> jungle = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_JUNGLE);
				//HolderSet.Named<Biome> forest = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_FOREST);
				HolderSet.Named<Biome> hot = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_HOT);
				HolderSet.Named<Biome> swamp = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_SWAMP);
				HolderSet.Named<Biome> cold = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_COLD);
				HolderSet.Named<Biome> hill = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_MOUNTAIN);
                HolderSet.Named<Biome> wet = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_WET);
                HolderSet.Named<Biome> slope = context.lookup(Registries.BIOME)
						.getOrThrow(Tags.Biomes.IS_SLOPE);
				//HolderSet.Named<Biome> temperate = context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_TEMPERATE);
				List<HolderSet<Biome>> list =new ArrayList<>();
				list.add(slope);
				//list.add(temperate);
				AndHolderSet<Biome> garlicBiome = new AndHolderSet<Biome>(list);

				// Corn
				HolderSet.Direct<PlacedFeature> wildCornHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_CORN.get(),
								new WildConfig(120, 250, 12, 25, 256))),
						List.of(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_corn")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(plains, wildCornHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Coffee
				HolderSet.Direct<PlacedFeature> wildCoffeeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_COFFEE.get(),
								new WildConfig(3, 7, 5, 5, 128))),
						List.of(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_coffee")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(jungle, wildCoffeeHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Ginger
				HolderSet.Direct<PlacedFeature> wildGingerHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_GINGER.get(),
								new WildConfig(2, 5, 2, 5, 128))),
						List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_ginger")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(jungle, wildGingerHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Chili
				HolderSet.Direct<PlacedFeature> wildChiliHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_CHILI.get(),
								new WildConfig(2, 5, 2, 5, 128))),
						List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_chili")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(hot, wildChiliHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Peanut
				HolderSet.Direct<PlacedFeature> wildPeanutHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_PEANUT.get(),
								new WildConfig(3, 7, 5, 5, 128))),
						List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_peanut")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(jungle, wildPeanutHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Mallow
				HolderSet.Direct<PlacedFeature> wildMallowHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_MALLOW.get(),
								new WildConfig(70, 150, 10, 5, 256))),
						List.of(RarityFilter.onAverageOnceEvery(70), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_mallow")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(swamp, wildMallowHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Mint
				HolderSet.Direct<PlacedFeature> wildMintHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_MINT.get(),
								new WildConfig(20, 100, 10, 5, 256))),
						List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_mint")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(cold, wildMintHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Garlic
				HolderSet.Direct<PlacedFeature> wildGarlicHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_GARLIC.get(),
								new WildConfig(3, 7, 5, 5, 128))),
						List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_garlic")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(garlicBiome, wildGarlicHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Cucumber
				HolderSet.Direct<PlacedFeature> wildCucumberHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_CUCUMBER.get(),
								new WildConfig(2, 3, 2, 5, 64))),
						List.of(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_cucumber")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(hill, wildCucumberHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Soybean
				HolderSet.Direct<PlacedFeature> wildSoybeanHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_WILD_SOYBEAN.get(),
								new WildConfig(2, 4, 5, 5, 64))),
						List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));

//				context.register(
//						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "patch_wild_soybean")),
//						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(temperate, wildSoybeanHolderSet,
//								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Cinnamon

				HolderSet.Direct<PlacedFeature> cinnamonTreeHolderSet = HolderSet
						.direct(Holder.direct(new PlacedFeature(
								Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_CINNAMON_TREE.get(),
										ExtraDelightTreeFeatures.createCinnamonTree().build())),
								List.of(RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(),
										HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
										BiomeFilter.biome()))));

				context.register(
						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "cinnamon_tree")),
						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(jungle, cinnamonTreeHolderSet,
								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Hazelnut

				HolderSet.Direct<PlacedFeature> hazelnutTreeHolderSet = HolderSet
						.direct(Holder.direct(new PlacedFeature(
								Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_HAZELNUT_TREE.get(),
										ExtraDelightTreeFeatures.createHazelnutTree().build())),
								List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
										HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
										BiomeFilter.biome()))));

//				context.register(
//						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "hazelnut_tree")),
//						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(forest, hazelnutTreeHolderSet,
//								GenerationStep.Decoration.VEGETAL_DECORATION));

				// Apple

				HolderSet.Direct<PlacedFeature> appleTreeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_APPLE_TREE.get(),
								ExtraDelightTreeFeatures.createAppleTree().build())),
						List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
								HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
								BiomeFilter.biome()))));


                // Lemon

                HolderSet.Direct<PlacedFeature> lemonTreeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
                        Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_LEMON_TREE.get(),
                                ExtraDelightTreeFeatures.createLemonTree().build())),
                        List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()))));

                context.register(
                        biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "lemon_tree")),
                        new ForgeBiomeModifiers.AddFeaturesBiomeModifier(hill, lemonTreeHolderSet,
                                GenerationStep.Decoration.VEGETAL_DECORATION));

                // Lime

                HolderSet.Direct<PlacedFeature> limeTreeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
                        Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_LIME_TREE.get(),
                                ExtraDelightTreeFeatures.createLimeTree().build())),
                        List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()))));

                context.register(
                        biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "lime_tree")),
                        new ForgeBiomeModifiers.AddFeaturesBiomeModifier(hot, limeTreeHolderSet,
                                GenerationStep.Decoration.VEGETAL_DECORATION));

                // Orange

                HolderSet.Direct<PlacedFeature> orangeTreeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
                        Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_ORANGE_TREE.get(),
                                ExtraDelightTreeFeatures.createOrangeTree().build())),
                        List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()))));

                context.register(
                        biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "orange_tree")),
                        new ForgeBiomeModifiers.AddFeaturesBiomeModifier(wet, orangeTreeHolderSet,
                                GenerationStep.Decoration.VEGETAL_DECORATION));

                // Grapefruit

                HolderSet.Direct<PlacedFeature> grapefruitTreeHolderSet = HolderSet.direct(Holder.direct(new PlacedFeature(
                        Holder.direct(new ConfiguredFeature<>(ExtraDelightFeatures.PATCH_GRAPEFRUIT_TREE.get(),
                                ExtraDelightTreeFeatures.createGrapefruitTree().build())),
                        List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()))));

                context.register(
                        biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "grapefruit_tree")),
                        new ForgeBiomeModifiers.AddFeaturesBiomeModifier(jungle, grapefruitTreeHolderSet,
                                GenerationStep.Decoration.VEGETAL_DECORATION));


//				context.register(
//						biomeModifier(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "apple_tree")),
//						new ForgeBiomeModifiers.AddFeaturesBiomeModifier(forest, appleTreeHolderSet,
//								GenerationStep.Decoration.VEGETAL_DECORATION));
			});
}
