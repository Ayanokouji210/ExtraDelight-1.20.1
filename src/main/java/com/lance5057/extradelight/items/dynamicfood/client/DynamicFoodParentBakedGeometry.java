package com.lance5057.extradelight.items.dynamicfood.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class DynamicFoodParentBakedGeometry implements BakedModel {
	@Override
	@Nonnull
	public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection,
			@Nonnull RandomSource pRandom) {
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	@Nonnull
	public TextureAtlasSprite getParticleIcon() {
		return ModelBakery.FIRE_0.sprite();
	}

	@Override
	@Nonnull
	public ItemOverrides getOverrides() {
		return new DynamicFoodItemOverrides();
	}

	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}
}