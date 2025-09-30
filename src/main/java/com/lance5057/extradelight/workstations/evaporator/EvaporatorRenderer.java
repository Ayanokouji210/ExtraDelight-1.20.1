package com.lance5057.extradelight.workstations.evaporator;

import com.lance5057.extradelight.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
//import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
//import net.neoforged.neoforge.client.model.data.ModelData;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class EvaporatorRenderer implements BlockEntityRenderer<EvaporatorBlockEntity> {

	public EvaporatorRenderer(BlockEntityRendererProvider.Context cxt) {

	}

	@Override
	public void render(EvaporatorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		IItemHandlerModifiable inv = pBlockEntity.getItemHandler();
		ItemStack item = inv.getStackInSlot(0);

		if (!item.isEmpty() || pBlockEntity.getDisplayBlock() == EvaporatorBlockEntity.ice) {
			ResourceLocation display = pBlockEntity.getDisplayBlock();

			BlockRenderDispatcher br = Minecraft.getInstance().getBlockRenderer();

			pPoseStack.pushPose();
			pPoseStack.translate(0.05f, 0.11f, 0.05f);
			pPoseStack.scale(0.9f, 0.1f, 0.9f);

			br.renderSingleBlock(BuiltInRegistries.BLOCK.get(display).defaultBlockState(), pPoseStack, pBufferSource,
					pPackedLight, pPackedOverlay, ModelData.EMPTY, null);
			pPoseStack.popPose();

//			for (int i = 0; i < item.getCount(); i++) {
//				BakedModel bakedmodel = itemRenderer.getModel(item, pBlockEntity.getLevel(), null, 0);
//				pPoseStack.pushPose();
//
//				pPoseStack.translate(0.5f, 0.15f, 0.5f);
//				pPoseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians((90 * i)), 0));
//				pPoseStack.mulPose(
//						new Quaternionf().rotateXYZ((float) Math.toRadians(45), 0, (float) Math.toRadians(45)));
//				pPoseStack.translate(0.15f, 0, 0);
//
//				float scale = 1;
//				pPoseStack.scale(scale, scale, scale);
//
//				float uniscale = 0.65f;
//				pPoseStack.scale(uniscale, uniscale, uniscale);
//				itemRenderer.render(item, ItemDisplayContext.GROUND, false, pPoseStack, pBufferSource, pPackedLight,
//						pPackedOverlay, bakedmodel);
//				pPoseStack.popPose();
//			}
		}

		if (!pBlockEntity.getFluidTank().getFluid().isEmpty()) {
			FluidStack fluidStack = pBlockEntity.getFluidTank().getFluid();
			Fluid fluid = fluidStack.getFluid();
			IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid);

			// 获取流体纹理
			ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);
			Vector4f uv = RenderUtil.getUV(stillTexture);

			// 获取流体颜色
			int tint = fluidTypeExtensions.getTintColor(fluidStack);

			VertexConsumer vertexConsumer = pBufferSource.getBuffer(Sheets.translucentItemSheet());
//			VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.translucent());
			Matrix4f mat = pPoseStack.last().pose();
			Matrix3f matrix3f = pPoseStack.last().normal();
			//Matrix3f normal = new Matrix3f();

			pPoseStack.pushPose();
//
//			// 计算流体高度基于tank的填充程度
			float fillRatio = (float) fluidStack.getAmount() / (float) pBlockEntity.getFluidTank().getCapacity();
			float fluidHeight = 0.1f + 0.1f * fillRatio; // 从0.2f到0.8f的高度范围


			Vector3f pos1 =new Vector3f(0.05f,fluidHeight,0.05f);
			Vector3f pos2 =new Vector3f(0.05f,fluidHeight,0.95f);
			Vector3f pos3 =new Vector3f(0.95f,fluidHeight,0.95f);
			Vector3f pos4 =new Vector3f(0.95f,fluidHeight,0.05f);

			RenderUtil.buildPlane(pos1,pos2,pos3,pos4,vertexConsumer,mat,matrix3f,tint,uv,Direction.UP.getNormal(), pPackedLight,pPackedOverlay,pPoseStack);

			pPoseStack.popPose();
			//
//			// 使用正确的顶点顺序构建流体平面
//			RenderUtil.buildPlaneTest(pos1,pos2,pos3,pos4,vertexConsumer,mat,matrix3f,tint,uvtest,Direction.UP.getNormal(),pPackedLight,pPackedOverlay,pPoseStack);
////			RenderUtil.buildTriangle(pos4,pos3,pos2,vertexConsumer,mat,matrix3f,tint,uv,Direction.UP.getNormal(),pPackedLight,pPackedOverlay,pPoseStack);
////
//////			pPoseStack.popPose();
//////
//////			pPoseStack.pushPose();
////
////			RenderUtil.buildTriangle(pos3,pos2,pos1,vertexConsumer,mat,matrix3f,tint,uv,Direction.UP.getNormal(),pPackedLight,pPackedOverlay,pPoseStack);
//
//			pPoseStack.popPose();

//			pPoseStack.pushPose();
//
//			// 从纹理图集中获取纹理精灵
//			TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
//
//			// 获取流体颜色
//			int tintColor = IClientFluidTypeExtensions.of(fluid).getTintColor();
//
//			// 获取渲染类型 - 使用半透明渲染层
//			RenderType renderType = Sheets.translucentItemSheet();
//			VertexConsumer consumer = pBufferSource.getBuffer(renderType);
//
//			// 获取变换矩阵和法线矩阵
//			Matrix4f poseMatrix = pPoseStack.last().pose();
//			Matrix3f normalMatrix = pPoseStack.last().normal();
//
//			// 定义平面的四个顶点（稍微缩小以避免z-fighting）
//			float minX = 0.05f;
//			float maxX = 0.95f;
//			float minZ = 0.05f;
//			float maxZ = 0.95f;
//
//			// 获取纹理UV坐标
//			float u0 = sprite.getU0();
//			float u1 = sprite.getU1();
//			float v0 = sprite.getV0();
//			float v1 = sprite.getV1();
//
//			Vector4f vector4f = new Vector4f(u0, u1, v0, v1);
//
//			// 向上法线
//			Vec3i upNormal = Direction.UP.getNormal();
//
//			RenderUtil.buildPlaneTest(0.05f,0.95f,0.2f,0.05f,0.95f,
//					consumer,poseMatrix,normalMatrix,tintColor,vector4f,pPackedLight,pPackedOverlay,pPoseStack);
//
//
//			pPoseStack.popPose();

		}
	}


	public static void drawTexturedPlane(PoseStack poseStack, ResourceLocation texture, float x, float y, float z, float width, float height, float textureX, float textureY, float textureWidth, float textureHeight) {
		// 确保渲染状态设置正确
		RenderSystem.enableBlend(); // 启用混合，用于透明或半透明纹理
		RenderSystem.defaultBlendFunc(); // 使用默认混合函数
		// 根据你的纹理特性，你可能需要禁用深度测试
		// RenderSystem.disableDepthTest();

		// 绑定指定的纹理
		RenderSystem.setShaderTexture(0, texture);
		// 设置着色器。对于纹理绘制，通常使用 POSITION_TEX_SHADER
		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		// 从矩阵栈获取当前的变换矩阵，并将其应用到RenderSystem
		// 从PoseStack获取最终的变换矩阵
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // 设置颜色为白色，不透明

		// 开始构建顶点数据
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX); // 使用四边形模式和位置-纹理格式

		// 获取当前矩阵栈的最后一个Pose，用于应用变换
		PoseStack.Pose lastPose = poseStack.last();
		Matrix4f matrix4f = lastPose.pose(); // 获取变换矩阵

		// 定义平面的四个顶点（逆时针顺序，从右下开始）
		// 顶点0: 右下角
		bufferBuilder.vertex(matrix4f, x + width, y, z + height).uv(textureX + textureWidth, textureY + textureHeight).endVertex();
		// 顶点1: 右上角
		bufferBuilder.vertex(matrix4f, x + width, y + height, z).uv(textureX + textureWidth, textureY).endVertex();
		// 顶点2: 左上角
		bufferBuilder.vertex(matrix4f, x, y + height, z).uv(textureX, textureY).endVertex();
		// 顶点3: 左下角
		bufferBuilder.vertex(matrix4f, x, y, z + height).uv(textureX, textureY + textureHeight).endVertex();

		// 上传顶点数据并执行绘制
		// 使用BufferUploader.draw(bufferBuilder.end()); 也可以
		Tesselator.getInstance().end();

		// 恢复渲染状态（根据你的需要，可能需要在调用此方法后恢复）
		RenderSystem.disableBlend();
		// RenderSystem.enableDepthTest();
	}


}