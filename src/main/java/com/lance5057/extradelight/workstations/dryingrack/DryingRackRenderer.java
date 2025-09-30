package com.lance5057.extradelight.workstations.dryingrack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.joml.Quaternionf;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	public DryingRackRenderer(BlockEntityRendererProvider.Context cxt) {

	}

	@Override
	public void render(DryingRackBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		IItemHandlerModifiable inv = pBlockEntity.getItemHandler();
		for (int i = 0; i < DryingRackBlockEntity.NUM_SLOTS; i++) {
			ItemStack item = inv.getStackInSlot(i);

			if (!item.isEmpty()) {
				BakedModel bakedmodel = itemRenderer.getModel(item, pBlockEntity.getLevel(), null, 0);
				pPoseStack.pushPose();

//				pPoseStack.translate(0.5f, 0.45f + (i > 3 ? 0.5f : 0), 0.5f);
//
//				pPoseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(135), 0));
//				pPoseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(90 * (i % 4)), 0));
//				pPoseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(90), 0, 0));
//				pPoseStack.translate(0.25f, 0, 0);
//
//				float uniscale = 0.65f;
//				pPoseStack.scale(uniscale, uniscale, uniscale);

				// 确定物品在哪个层（上层或下层）
				boolean isUpperLevel = i >= 4;

				// 设置基本位置
				pPoseStack.translate(0.5f, isUpperLevel ? 0.95f : 0.45f, 0.5f);

				// 计算方向角度 - 每个槽位对应一个方向
				float directionAngle = 90 * (i % 4);

				// 应用Y轴旋转（方向）
				pPoseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(directionAngle)));

				// 应用X轴旋转（使物品平放）
				pPoseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(90)));

				// 调整物品位置，使其远离中心
				pPoseStack.translate(0.25f, 0, 0);

				// 统一缩放
				float uniscale = 0.65f;
				pPoseStack.scale(uniscale, uniscale, uniscale);

				// 添加轻微随机旋转以避免Z-fighting
				pPoseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians((i * 13) % 5)));


				itemRenderer.render(item, ItemDisplayContext.GROUND, false, pPoseStack, pBufferSource, pPackedLight,
						pPackedOverlay, bakedmodel);
				pPoseStack.popPose();
			}
		}
	}

}
