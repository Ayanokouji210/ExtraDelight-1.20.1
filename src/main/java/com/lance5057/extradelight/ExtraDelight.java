package com.lance5057.extradelight;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.lance5057.extradelight.capabilities.DynamicItem;
import com.lance5057.extradelight.items.dynamicfood.client.DynamicFoodItemOverrides;
import com.lance5057.extradelight.modules.SummerCitrus;
import com.lance5057.extradelight.recipe.DynamicToastRecipe;
//import com.lance5057.extradelight.util.DataComponentIngredient;
import com.lance5057.extradelight.util.RenderUtil;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.lance5057.extradelight.aesthetics.AestheticBlocks;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.network.NetworkHandler;
import com.lance5057.extradelight.worldgen.features.ExtraDelightFeatures;
import com.lance5057.extradelight.worldgen.placers.FoliagePlacerRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import vectorwing.farmersdelight.common.registry.ModItems;


@Mod(ExtraDelight.MOD_ID)
public class ExtraDelight {
	public final static String MOD_ID = "extradelight";
	public static final String VERSION = "2.6.1";

	public static ResourceLocation modLoc(String s) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
	}

	public static Logger logger = LogManager.getLogger();

	public static Map<Item,Integer> chillMap =new HashMap<>();

	public ExtraDelight() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeMod.enableMilkFluid();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,ExtraDelightConfig.spec,"extradelight-common.toml");

		modEventBus.addListener(this::setupClient);
		modEventBus.addListener(this::setupCommon);
		modEventBus.addListener(ExtraDelightBlockEntities::addCabinets);
		NetworkHandler.register();

		Fermentation f = new Fermentation();
        SummerCitrus s = new SummerCitrus();

		AestheticBlocks.setup();
		AestheticBlocks.BLOCKS.register(modEventBus);
		AestheticBlocks.ITEMS.register(modEventBus);

//		ExtraDelightComponents.COMPONENTS.register(modEventBus);
		ExtraDelightBlocks.register(modEventBus);
		ExtraDelightFluids.register(modEventBus);

//		PieceTypes.PIECES.register(modEventBus);

		ExtraDelightItems.ITEMS.register(modEventBus);
		ExtraDelightTabs.TABS.register(modEventBus);

        ExtraDelightPaintings.PAINTING_VARIANTS.register(modEventBus);
        ExtraDelightBanners.BANNER_PATTERNS.register(modEventBus);

        ExtraDelightBlockEntities.TILES.register(modEventBus);
		ExtraDelightRecipes.RECIPE_TYPES.register(modEventBus);
		ExtraDelightRecipes.RECIPE_SERIALIZERS.register(modEventBus);
		ExtraDelightContainers.register(modEventBus);
		ExtraDelightLootModifiers.LOOT_MODIFIERS.register(modEventBus);

		FoliagePlacerRegistry.PLACER.register(modEventBus);
		ExtraDelightWorldGen.FEATURES.register(modEventBus);

		ExtraDelightFeatures.FEATURES.register(modEventBus);

		ExtraDelightMobEffects.register(modEventBus);

		modEventBus.addListener(ExtraDelightComponents::registerCapabilities);


        ExtraDelightParticles.PARTICLE_TYPES.register(modEventBus);
	}

	public void setupClient(FMLClientSetupEvent event) {

		event.enqueueWork(() -> {
			ExtraDelightClientEvents.setTERenderers();
			ExtraDelightClientEvents.doFluidRenderLayer();
		});
	}

	public void setupCommon(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ExtraDelightBlocks.setup();
			ExtraDelightItems.setup();
		//	PottedPlants.setup();
			registerItemSetAdditions();
			setupChillMap();
		});
	}

	public static void setupChillMap() {
		//chill
		chillMap.put(Items.ICE, 100);
		chillMap.put(Items.PACKED_ICE, 1000);
		chillMap.put(Items.BLUE_ICE, 10000);
		chillMap.put(Items.SNOWBALL, 50);
		chillMap.put(Items.SNOW_BLOCK, 250);
		chillMap.put(SummerCitrus.ICE_CUBES.get(), 25);
	}

	public static void registerItemSetAdditions() {
		Set<Item> newWantedItems = Sets.newHashSet(ExtraDelightItems.CHILI.get(), ExtraDelightItems.GINGER.get(),
				ExtraDelightItems.GARLIC.get(), Fermentation.CUCUMBER.get());
		newWantedItems.addAll(Villager.WANTED_ITEMS);
		Villager.WANTED_ITEMS = ImmutableSet.copyOf(newWantedItems);

	}

}