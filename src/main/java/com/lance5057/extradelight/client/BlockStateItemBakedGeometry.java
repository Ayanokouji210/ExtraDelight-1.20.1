package com.lance5057.extradelight.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BlockStateItemBakedGeometry implements BakedModel {
	public final ItemTransform thirdPersonLeftHand = new ItemTransform(new Vector3f(75, 45, 0),
			new Vector3f(0, 2.5f, 0), new Vector3f(0.375f, 0.375f, 0.375f));
	public final ItemTransform thirdPersonRightHand = new ItemTransform(new Vector3f(75, 45, 0),
			new Vector3f(0, 2.5f, 0), new Vector3f(0.375f, 0.375f, 0.375f));
	public final ItemTransform firstPersonLeftHand = new ItemTransform(new Vector3f(0, 225, 0), new Vector3f(0, 0, 0),
			new Vector3f(0.40f, 0.40f, 0.40f));
	public final ItemTransform firstPersonRightHand = new ItemTransform(new Vector3f(0, 45, 0), new Vector3f(0, 0, 0),
			new Vector3f(0.40f, 0.40f, 0.40f));
	public final ItemTransform head = new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0),
			new Vector3f(0.625f, 0.625f, 0.625f));
	public final ItemTransform gui = new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0),
			new Vector3f(0.625f, 0.625f, 0.625f));
	public final ItemTransform ground = new ItemTransform(new Vector3f(0, 0, 0), new Vector3f(0, 3, 0),
			new Vector3f(0.25f, 0.25f, 0.25f));
	public final ItemTransform fixed = new ItemTransform(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0),
			new Vector3f(0.5f, 0.5f, 0.5f));

	private final ItemTransforms transforms = new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand,
			firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, ImmutableMap.of());

	public BlockStateItemBakedGeometry() {
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand) {
		List<BakedQuad> bakedQuads = new ArrayList<>();

		return bakedQuads;
	}
//
//	@Override
//	@Nonnull
//	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand,
//			@Nonnull ModelData data, @Nullable RenderType renderType) {
//		List<BakedQuad> bakedQuads = new ArrayList<>();
//		for (BakedModel bakedModel : childrenModels) {
//			bakedQuads.addAll(bakedModel.getQuads(state, side, rand, data, renderType));
//		}
//		return bakedQuads;
//	}
//
//	@Override
//	@Nonnull
//	public ChunkRenderTypeSet getRenderTypes(@Nonnull BlockState state, @Nonnull RandomSource rand,
//			@Nonnull ModelData data) {
//		Set<ChunkRenderTypeSet> chunkRenderTypeSets = new HashSet<>();
//		for (BakedModel bakedModel : childrenModels) {
//			chunkRenderTypeSets.add(bakedModel.getRenderTypes(state, rand, data));
//		}
//		return ChunkRenderTypeSet.union(chunkRenderTypeSets);
//	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return true;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public TextureAtlasSprite getParticleIcon() {
		return ModelBakery.FIRE_0.sprite();
	}

	@Override
	public TextureAtlasSprite getParticleIcon(@Nonnull ModelData data) {
		return ModelBakery.FIRE_0.sprite();
	}

//	@Override
//	@Nonnull
//	public TextureAtlasSprite getParticleIcon(@Nonnull ModelData modelData) {
//		return ModelBakery.FIRE_0.sprite();
//	}

	@Override
	@Nonnull
	public ItemOverrides getOverrides() {
		return new BlockStateItemOverrides();
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack,
			boolean applyLeftHandTransform) {
		transforms.getTransform(transformType).apply(applyLeftHandTransform, poseStack);
		return this;
	}
}