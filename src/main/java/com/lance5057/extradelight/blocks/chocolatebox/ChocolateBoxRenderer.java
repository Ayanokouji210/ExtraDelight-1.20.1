package com.lance5057.extradelight.blocks.chocolatebox;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.joml.Quaternionf;

public class ChocolateBoxRenderer implements BlockEntityRenderer<ChocolateBoxBlockEntity> {

	public ChocolateBoxRenderer(BlockEntityRendererProvider.Context cxt) {

	}

	@Override
	public void render(ChocolateBoxBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!pBlockEntity.hasLevel()) {
			return;
		}

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		IItemHandler itemInteractionHandler = pBlockEntity.getItems();

		Direction dir = pBlockEntity.getBlockState().getValue(ChocolateBoxBlock.FACING);

		pPoseStack.pushPose();
		pPoseStack.translate(0.5f, 0, 0.5f); // 移动到方块中心

		// 应用方块方向旋转
		pPoseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-dir.toYRot())));

//		float xoff = 0;
//		float yoff = 0;
//		float zoff = 0;

//		for (int i = 0; i < ChocolateBoxBlockEntity.NUM_SLOTS; i++) {
//			xoff = (i % 4) * 0.15f;
//			if (i % 4 == 0) {
//				zoff += 0.15f;
////				yoff -= 0.1f;
//			}

		// 计算4x4网格中的位置
		for (int i = 0; i < ChocolateBoxBlockEntity.NUM_SLOTS; i++) {

			ItemStack item = itemInteractionHandler.getStackInSlot(i);

			if (!item.isEmpty()) {
				// 计算行列位置 (4x4网格)
				int row = i / 4;    // 0-3
				int col = i % 4;    // 0-3

				// 计算偏移量 - 从中心点开始分布
				float xOffset = (col - 1.5f) * 0.18f; // -1.5, -0.5, 0.5, 1.5
				float zOffset = (row - 0.5f) * 0.18f; // -1.5, -0.5, 0.5, 1.5

				pPoseStack.translate(0, 0, 0.001f * i);

				BakedModel bakedmodel = itemRenderer.getModel(item, pBlockEntity.getLevel(), null, 0);
				pPoseStack.pushPose();
				// 应用位置偏移
				pPoseStack.translate(xOffset, 0.1f, zOffset);

				// 添加微小随机旋转避免Z-fighting
				float randomXRot = (i * 7) % 3; // 微小随机X旋转
				float randomYRot = (i * 11) % 5; // 微小随机Y旋转
				float randomZRot = (i * 13) % 5; // 微小随机Z旋转
				// 应用旋转 - 使物品平放并稍微随机旋转
				pPoseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(-75 + randomXRot)));
				pPoseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(randomYRot)));
				pPoseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(randomZRot)));

				// 统一缩放
				float uniscale = 0.4f;
//				pPoseStack.translate(0.5f, 0, 0.5f);
//				pPoseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(-dir.toYRot()), 0));
//				pPoseStack.translate(xoff - 0.225f, yoff + 0.1, zoff - 0.2);

//				if (i % 2 == 0)
//					pPoseStack.translate(0, 0.0, 0.05);

//				pPoseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(-80), 0, 0));
//				float uniscale = 0.5f;
				pPoseStack.scale(uniscale, uniscale, uniscale);
				itemRenderer.render(item, ItemDisplayContext.GROUND, false, pPoseStack, pBufferSource, pPackedLight,
						pPackedOverlay, bakedmodel);
				pPoseStack.popPose();
			}
		}
		pPoseStack.popPose();
	}

}
