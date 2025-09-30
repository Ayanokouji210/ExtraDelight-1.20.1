package com.lance5057.extradelight;

import com.lance5057.extradelight.aesthetics.AestheticBlocks;
import com.lance5057.extradelight.aesthetics.block.cornhuskdoll.CornHuskDollRenderer;
import com.lance5057.extradelight.blocks.chocolatebox.ChocolateBoxRenderer;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetRenderer;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetScreen;
import com.lance5057.extradelight.blocks.funnel.FunnelRenderer;
import com.lance5057.extradelight.blocks.jar.JarRenderer;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayRenderer;
import com.lance5057.extradelight.blocks.keg.KegRenderer;
import com.lance5057.extradelight.blocks.sink.SinkCabinetScreen;
import com.lance5057.extradelight.blocks.sink.SinkRenderer;
import com.lance5057.extradelight.capabilities.Chill;
import com.lance5057.extradelight.client.BlockStateItemGeometryLoader;
import com.lance5057.extradelight.displays.candybowl.CandyBowlRenderer;
import com.lance5057.extradelight.displays.food.FoodDisplayRenderer;
import com.lance5057.extradelight.displays.food.FoodDisplayScreen;
import com.lance5057.extradelight.displays.knife.KnifeBlockRenderer;
import com.lance5057.extradelight.displays.knife.KnifeBlockScreen;
import com.lance5057.extradelight.displays.spice.SpiceRackRenderer;
import com.lance5057.extradelight.displays.spice.SpiceRackScreen;
import com.lance5057.extradelight.displays.wreath.WreathRenderer;
import com.lance5057.extradelight.displays.wreath.WreathScreen;
import com.lance5057.extradelight.gui.StyleableScreen;
import com.lance5057.extradelight.items.dynamicfood.DynamicToast;
import com.lance5057.extradelight.items.dynamicfood.client.DynamicFoodGeometryLoader;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.workstations.chiller.ChillerScreen;
import com.lance5057.extradelight.workstations.doughshaping.DoughShapingScreen;
import com.lance5057.extradelight.workstations.dryingrack.DryingRackRenderer;
import com.lance5057.extradelight.workstations.evaporator.EvaporatorRenderer;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotScreen;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlRenderer;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlScreen;
import com.lance5057.extradelight.workstations.mortar.MortarRenderer;
import com.lance5057.extradelight.workstations.oven.OvenScreen;
import com.lance5057.extradelight.workstations.vat.VatScreen;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ModelEvent;
//import net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional;
//import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
//import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
//import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
//import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
//import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

//import net.minecraftforge.client.event.Register

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lance5057.extradelight.ExtraDelightClientEvents.chillMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExtraDelight.MOD_ID, value = Dist.CLIENT)
public class ExtraDelightClientEvents {
	static Map<Item,Integer> chillMap =new HashMap<>();

	static {
		//chill
		chillMap.put(Items.ICE,200);
		chillMap.put(Items.PACKED_ICE,400);
		chillMap.put(Items.BLUE_ICE,800);
		chillMap.put(Items.SNOWBALL,100);
		chillMap.put(Items.SNOW_BLOCK,400);

	}


	@SubscribeEvent
	public static void registerClient(FMLClientSetupEvent event) {
		event.enqueueWork(()->{
			MenuScreens.register(ExtraDelightContainers.OVEN_MENU.get(), OvenScreen::new);
			MenuScreens.register(ExtraDelightContainers.FOOD_DISPLAY_MENU.get(), FoodDisplayScreen::new);
			MenuScreens.register(ExtraDelightContainers.KNIFE_BLOCK_MENU.get(), KnifeBlockScreen::new);
			MenuScreens.register(ExtraDelightContainers.SPICE_RACK_MENU.get(), SpiceRackScreen::new);
			MenuScreens.register(ExtraDelightContainers.DOUGH_SHAPING_MENU.get(), DoughShapingScreen::new);
			MenuScreens.register(ExtraDelightContainers.WREATH_MENU.get(), WreathScreen::new);
			MenuScreens.register(ExtraDelightContainers.SINK_MENU.get(), SinkCabinetScreen::new);
			MenuScreens.register(ExtraDelightContainers.COUNTER_CABINET_MENU.get(), CounterCabinetScreen::new);
			MenuScreens.register(ExtraDelightContainers.STYLE_MENU.get(), StyleableScreen::new);
			MenuScreens.register(ExtraDelightContainers.MIXING_BOWL_MENU.get(), MixingBowlScreen::new);
			MenuScreens.register(ExtraDelightContainers.MELTING_POT_MENU.get(), MeltingPotScreen::new);
			MenuScreens.register(ExtraDelightContainers.CHILLER_MENU.get(), ChillerScreen::new);
			MenuScreens.register(ExtraDelightContainers.VAT_MENU.get(), VatScreen::new);
				}
		);

	}

	public static void setTERenderers() {
		BlockEntityRenderers.register(ExtraDelightBlockEntities.FOOD_DISPLAY.get(), FoodDisplayRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.KNIFE_BLOCK.get(), KnifeBlockRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.SPICE_RACK.get(), SpiceRackRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.MORTAR.get(), MortarRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.MIXING_BOWL.get(), MixingBowlRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.DRYING_RACK.get(), DryingRackRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.CORN_HUSK_DOLL.get(), CornHuskDollRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.WREATH.get(), WreathRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.CANDY_BOWL.get(), CandyBowlRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.COUNTER_CABINET_BLOCK.get(),
				CounterCabinetRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.SINK_BLOCK.get(), SinkRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.KEG.get(), KegRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.FUNNEL.get(), FunnelRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.CHOCOLATE_BOX.get(), ChocolateBoxRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.JAR.get(), JarRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.EVAPORATOR.get(), EvaporatorRenderer::new);
		BlockEntityRenderers.register(ExtraDelightBlockEntities.JAR_DISPLAY.get(), JarDisplayRenderer::new);
	}

	@SubscribeEvent
	public static void RegisterExtraModels(ModelEvent.RegisterAdditional event) {
		Map<ResourceLocation, Resource> rrs = Minecraft.getInstance().getResourceManager().listResources("models/extra",
				(location) -> {
					return location.getPath().endsWith(".json");
				});

		rrs.forEach((rl, r) -> {
			String s = rl.toString();

			s = s.substring(s.indexOf('/') + 1, s.indexOf('.'));

			ExtraDelight.logger.debug(s);

			//ModelResourceLocation rl2 = new ModelResourceLocation(new ResourceLocation(rl.getNamespace(), s),"");

			ResourceLocation rl2 = new ResourceLocation(rl.getNamespace(),s);

			event.register(rl2);
		});
	}


	@SubscribeEvent
	public static void registerBlockColourHandlers(final RegisterColorHandlersEvent.Block event) {

		final BlockColor bc = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null) {
				return BiomeColors.getAverageFoliageColor(blockAccess, pos);
			}

			return FoliageColor.getDefaultColor();
		};

		event.register((p_92636_, p_92637_, p_92638_, p_92639_) -> {
			return FoliageColor.getEvergreenColor();
		}, AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.spruce.ordinal()).get());

		event.register((p_92631_, p_92632_, p_92633_, p_92634_) -> {
			return FoliageColor.getBirchColor();
		}, AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.birch.ordinal()).get());
		event.register((p_92626_, p_92627_, p_92628_, p_92629_) -> {
			return p_92627_ != null && p_92628_ != null ? BiomeColors.getAverageFoliageColor(p_92627_, p_92628_)
					: FoliageColor.getDefaultColor();
		}, AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.oak.ordinal()).get(),
				AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.jungle.ordinal()).get(),
				AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.acacia.ordinal()).get(),
				AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.dark_oak.ordinal()).get(),
				AestheticBlocks.WREATHS.get(AestheticBlocks.WOOD.mangrove.ordinal()).get());
	}

	@SubscribeEvent
	public static void registerItemColourHandlers(final RegisterColorHandlersEvent.Item event) {
		final BlockColors blockColors = event.getBlockColors();

		// Use the Block's colour handler for an ItemBlock
		final ItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			final BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
			return blockColors.getColor(state, null, null, tintIndex);
		};

//		for (RegistryObject<Item> b : AestheticBlocks.WREATH_ITEMS)
		event.register(itemBlockColourHandler, AestheticBlocks.getRegistryListAsItems(AestheticBlocks.WREATH_ITEMS));
	}

//	@SubscribeEvent
//	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
//
//		event.registerItem(new IClientItemExtensions() {
//
//			@Override
//			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
//				return JarItemModel.getInstance();
//			}
//		}, ExtraDelightItems.JAR.asItem());
//
//	}

	@SubscribeEvent
	public static void registerLoader(ModelEvent.RegisterGeometryLoaders registerGeometryLoaders) {
		registerGeometryLoaders.register(DynamicFoodGeometryLoader.ID.getPath(), new DynamicFoodGeometryLoader());
		registerGeometryLoaders.register(BlockStateItemGeometryLoader.ID.getPath(), new BlockStateItemGeometryLoader());
	}

	public static void doFluidRenderLayer() {
		applyFluidRenderType(ExtraDelightBlocks.COOKING_OIL_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.VINEGAR_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.GRAVY_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.GLOW_BERRY_JUICE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.SWEET_BERRY_JUICE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.TOMATO_JUICE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.CACTUS_JUICE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.APPLE_CIDER_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.HOT_COCOA_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.MELON_JUICE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.EGG_MIX_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.BBQ_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.KETCHUP_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.MAYO_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.BROTH_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.CARAMEL_SAUCE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.MILKSHAKE_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.WHIPPED_CREAM_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.JAM_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.GOLDEN_JAM_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.GLOW_JAM_FLUID_BLOCK.get());
		applyFluidRenderType(ExtraDelightBlocks.TEA_FLUID_BLOCK.get());
		applyFluidRenderType(Fermentation.PICKLE_JUICE_FLUID_BLOCK.get());
	}

	public static void applyFluidRenderType(LiquidBlock liquid) {
		ItemBlockRenderTypes.setRenderLayer(liquid.getFluid().getFlowing(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(liquid.getFluid().getSource(), RenderType.translucent());
	}

	@SubscribeEvent
	public static void onRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.BOTTLE_FLUID_REGISTRY.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.CHILLER.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.DOUGH_SHAPING.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.DRYING_RACK.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.DYNAMIC_TOAST.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.EVAPORATOR.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.FEAST.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.MELTING_POT.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.MIXING_BOWL.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.MORTAR.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.OVEN.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.SHAPED_JAR.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.TOOL_ON_BLOCK.get(), r-> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(ExtraDelightRecipes.VAT.get(), r-> RecipeBookCategories.UNKNOWN);
	}




}

