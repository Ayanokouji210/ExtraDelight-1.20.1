package com.lance5057.extradelight.worldgen.features.trees;

import com.lance5057.extradelight.ExtraDelight;
//import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

//public class ExtraDelightTreeGrowers {
//	public static final TreeGrower CINNAMON = new TreeGrower(ExtraDelight.MOD_ID + "cinnamon", 0.1F, Optional.empty(),
//			Optional.empty(), Optional.of(ExtraDelightTreeFeatures.CINNAMON), Optional.empty(), Optional.empty(),
//			Optional.empty());
//	public static final TreeGrower HAZELNUT = new TreeGrower(ExtraDelight.MOD_ID + "hazelnut", 0.1F, Optional.empty(),
//			Optional.empty(), Optional.of(ExtraDelightTreeFeatures.HAZELNUT), Optional.empty(), Optional.empty(),
//			Optional.empty());
//	public static final TreeGrower APPLE = new TreeGrower(ExtraDelight.MOD_ID + "apple", 0.1F, Optional.empty(),
//			Optional.empty(), Optional.of(ExtraDelightTreeFeatures.APPLE), Optional.empty(), Optional.empty(),
//			Optional.empty());
//
//}
public class ExtraDelightTreeGrowers {
	public static final AbstractTreeGrower CINNAMON = new AbstractTreeGrower() {
		@Nullable
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
			return ExtraDelightTreeFeatures.CINNAMON;
		}
	};

	public static final AbstractTreeGrower HAZELNUT = new AbstractTreeGrower() {
		@Nullable
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
			return ExtraDelightTreeFeatures.HAZELNUT;
		}
	};

	public static final AbstractTreeGrower APPLE = new AbstractTreeGrower() {
		@Nullable
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
			return ExtraDelightTreeFeatures.APPLE;
		}
	};

}