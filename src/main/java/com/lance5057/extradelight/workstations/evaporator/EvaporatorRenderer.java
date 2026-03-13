package com.lance5057.extradelight.workstations.evaporator;

import com.lance5057.extradelight.util.RenderUtil;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

		}

		if (!pBlockEntity.getFluidTank().getFluid().isEmpty()) {
			FluidStack fluidStack = pBlockEntity.getFluidTank().getFluid();
			Fluid fluid = fluidStack.getFluid();
			IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid);

			ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);
			Vector4f uv = RenderUtil.getUV(stillTexture);

			int tint = fluidTypeExtensions.getTintColor(fluidStack);

			VertexConsumer vertexConsumer = pBufferSource.getBuffer(Sheets.translucentItemSheet());
			Matrix4f mat = pPoseStack.last().pose();
			Matrix3f matrix3f = pPoseStack.last().normal();

			pPoseStack.pushPose();
//
			float fillRatio = (float) fluidStack.getAmount() / (float) pBlockEntity.getFluidTank().getCapacity();
			float fluidHeight = 0.1f + 0.1f * fillRatio;


			Vector3f pos1 =new Vector3f(0.05f,fluidHeight,0.05f);
			Vector3f pos2 =new Vector3f(0.05f,fluidHeight,0.95f);
			Vector3f pos3 =new Vector3f(0.95f,fluidHeight,0.95f);
			Vector3f pos4 =new Vector3f(0.95f,fluidHeight,0.05f);

			RenderUtil.buildPlane(pos1,pos2,pos3,pos4,vertexConsumer,mat,matrix3f,tint,uv,Direction.UP.getNormal(), pPackedLight,pPackedOverlay,pPoseStack);

			pPoseStack.popPose();
		}
	}
}