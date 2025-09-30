package com.lance5057.extradelight.items.jar;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.blocks.jar.JarBlock;
import com.lance5057.extradelight.blocks.jar.JarBlockEntity;
import com.lance5057.extradelight.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
//import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
//import net.neoforged.neoforge.client.model.data.ModelData;
//import net.neoforged.neoforge.client.model.renderable.BakedModelRenderable;
//import net.neoforged.neoforge.client.model.renderable.IRenderable;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.renderable.BakedModelRenderable;
import net.minecraftforge.client.model.renderable.IRenderable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.*;

import java.lang.Math;

public class JarItemModel extends BlockEntityWithoutLevelRenderer{
	private static JarItemModel instance;
	private final BlockEntityRenderDispatcher dispatch;
	private final JarBlockEntity modelSource;
	ModelResourceLocation rc = new ModelResourceLocation(
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "block/jar"),"inventory");
	IRenderable<ModelData> bm = BakedModelRenderable.of(rc).withModelDataContext();


	public JarItemModel(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
		super(blockEntityRenderDispatcher, entityModelSet);
		this.dispatch=blockEntityRenderDispatcher;
		this.modelSource=new JarBlockEntity(BlockPos.ZERO,ExtraDelightBlocks.JAR.get().defaultBlockState());

	}

	public static JarItemModel getInstance() {
		if (instance == null) {
			instance = new JarItemModel(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
					Minecraft.getInstance().getEntityModels());
		}
		return instance;
	}


	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack ps, MultiBufferSource mbs,
			int packedLight, int overlay) {
		BlockEntityRenderer<JarBlockEntity> blockEntityRenderer=(BlockEntityRenderer<JarBlockEntity>) dispatch.getRenderer(this.modelSource);


		if (stack.is(ExtraDelightItems.JAR.get())) {
			ps.pushPose();

			if (displayContext == ItemDisplayContext.GUI) {

				ps.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(30), (float) Math.toRadians(225), 0));
				ps.translate(-0.05f, 1.0f, -1.9f);
				ps.scale(1.25f, 1.25f, 1.25f);
			} else if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
					|| displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
				ps.translate(0, 0.35, -0.17f);
			} else {
				ps.translate(0, 0.25f, 0f);

			}

			if(blockEntityRenderer != null) {
				try{
					blockEntityRenderer.render(this.modelSource,1.0f,ps,mbs,packedLight,overlay);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(ExtraDelightBlocks.JAR.get().defaultBlockState());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
					ps.last(),
					mbs.getBuffer(Sheets.translucentCullBlockSheet()),
					modelSource.getBlockState(),
					model,
					1.0f,1.0f,1.0f,
					packedLight,overlay,
						ModelData.EMPTY,Sheets.translucentCullBlockSheet()

					);

			//bm.render(ps, mbs, RenderType::entityCutout, packedLight, overlay, 0, ModelData.EMPTY);

			VertexConsumer vertexConsumer = mbs.getBuffer(Sheets.translucentCullBlockSheet());
			Matrix4f mat = ps.last().pose();
			Matrix3f matrix3f = ps.last().normal();

			var f = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
			if (f.isPresent() && f.resolve().isPresent()) {
//				ps.pushPose();

				FluidStack fluidStack = f.resolve().get().getFluidInTank(0);
				Fluid fluid = fluidStack.getFluid();
				IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid);

//
                if (!fluidStack.isEmpty()) {
                    RenderUtil.buildCubeAll(new Vector3f(5.5f / 16f, 0.5f / 16f, 5.5f / 16f),
                            new Vector3f(5f / 16f, ((float) fluidStack.getAmount() / 1000f) * (6f / 16f), 5f / 16f), vertexConsumer,
                            mat, matrix3f, fluidTypeExtensions.getTintColor(fluidStack),
                            RenderUtil.getUV(fluidTypeExtensions.getStillTexture()), packedLight, overlay, ps);
//				ps.popPose();
                }
            }


			ps.popPose();
		}
	}
}
