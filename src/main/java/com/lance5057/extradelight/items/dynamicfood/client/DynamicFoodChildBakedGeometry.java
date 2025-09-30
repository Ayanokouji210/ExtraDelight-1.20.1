package com.lance5057.extradelight.items.dynamicfood.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicFoodChildBakedGeometry implements BakedModel {
	private final List<BakedModel> childrenModels;
	

	public DynamicFoodChildBakedGeometry(List<BakedModel> childrenModels) {
		this.childrenModels = childrenModels;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand) {
		List<BakedQuad> bakedQuads = new ArrayList<>();
		for (BakedModel bakedModel : childrenModels) {
			bakedQuads.addAll(bakedModel.getQuads(state, side, rand));
		}
		return bakedQuads;
	}

	@Override
	@Nonnull
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand,
									@Nonnull ModelData data, @Nullable RenderType renderType) {
		List<BakedQuad> bakedQuads = new ArrayList<>();
		for (BakedModel bakedModel : childrenModels) {
			bakedQuads.addAll(bakedModel.getQuads(state, side, rand, data, renderType));
		}
		return bakedQuads;
	}

	@Override
	@Nonnull
	public ChunkRenderTypeSet getRenderTypes(@Nonnull BlockState state, @Nonnull RandomSource rand,
											 @Nonnull ModelData data) {
		Set<ChunkRenderTypeSet> chunkRenderTypeSets = new HashSet<>();
		for (BakedModel bakedModel : childrenModels) {
			chunkRenderTypeSets.add(bakedModel.getRenderTypes(state, rand, data));
		}
		return ChunkRenderTypeSet.union(chunkRenderTypeSets);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.childrenModels.stream().anyMatch(BakedModel::useAmbientOcclusion);
	}

	@Override
	public boolean isGui3d() {
		return this.childrenModels.stream().anyMatch(BakedModel::isGui3d);
	}

	@Override
	public boolean usesBlockLight() {
		return this.childrenModels.stream().anyMatch(BakedModel::usesBlockLight);
	}

	@Override
	public boolean isCustomRenderer() {
		return this.childrenModels.stream().anyMatch(BakedModel::isCustomRenderer);
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public TextureAtlasSprite getParticleIcon() {
		if (this.childrenModels.isEmpty()) {
			return ModelBakery.FIRE_0.sprite();
		} else {
			return this.childrenModels.get(0).getParticleIcon();
		}
	}

	@Override
	@Nonnull
	public TextureAtlasSprite getParticleIcon(@Nonnull ModelData modelData) {
		if (this.childrenModels.isEmpty()) {
			return ModelBakery.FIRE_0.sprite();
		} else {
			return this.childrenModels.get(0).getParticleIcon(modelData);
		}
	}

	@Override
	@Nonnull
	public ItemOverrides getOverrides() {
		return new ItemOverrides() {
		};
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack,
			boolean applyLeftHandTransform) {
		childrenModels.get(0).getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
//		this.getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
		return this;
	}
}