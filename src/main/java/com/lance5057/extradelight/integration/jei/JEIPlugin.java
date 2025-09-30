package com.lance5057.extradelight.integration.jei;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightContainers;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.integration.jei.categories.*;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.workstations.chiller.ChillerMenu;
import com.lance5057.extradelight.workstations.chiller.ChillerScreen;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlMenu;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlScreen;
import com.lance5057.extradelight.workstations.oven.OvenMenu;
import com.lance5057.extradelight.workstations.oven.OvenScreen;
import com.lance5057.extradelight.workstations.vat.VatMenu;
import com.lance5057.extradelight.workstations.vat.VatScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.RecipeHolder;
import javax.annotation.Nonnull;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new MortarRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new MixingBowlRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OvenRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new DryingRackRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new DoughShapingRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ToolOnBlockRecipeCatagory(registry.getJeiHelpers().getGuiHelper()),
				new FeastRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new MeltingPotRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ChillerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new VatRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new EvaporatorRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new BottleFluidRegistryCategory(registry.getJeiHelpers().getGuiHelper()),
				new ShapedWithJarRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		registry.addRecipes(MortarRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.MORTAR.get()).stream().toList());
		registry.addRecipes(MixingBowlRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.MIXING_BOWL.get()).stream().toList());
		registry.addRecipes(OvenRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.OVEN.get()).stream().toList());
		registry.addRecipes(DryingRackRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.DRYING_RACK.get()).stream().toList());
		registry.addRecipes(DoughShapingRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.DOUGH_SHAPING.get()).stream().toList());
		registry.addRecipes(FeastRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.FEAST.get()).stream().toList());
		registry.addRecipes(ToolOnBlockRecipeCatagory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.TOOL_ON_BLOCK.get()).stream().toList());
		registry.addRecipes(MeltingPotRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.MELTING_POT.get()).stream().toList());
		registry.addRecipes(ShapedWithJarRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.SHAPED_JAR.get()).stream().toList());
		registry.addRecipes(ChillerRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.CHILLER.get()).stream().toList());
		registry.addRecipes(VatRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.VAT.get()).stream().toList());
		registry.addRecipes(EvaporatorRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
				.getAllRecipesFor(ExtraDelightRecipes.EVAPORATOR.get()).stream().toList());
		registry.addRecipes(BottleFluidRegistryCategory.TYPE,
				Minecraft.getInstance().level.getRecipeManager()
						.getAllRecipesFor(ExtraDelightRecipes.BOTTLE_FLUID_REGISTRY.get()).stream()
						.toList());

		registry.addIngredientInfo(new ItemStack(ExtraDelightItems.MINT.get()), VanillaTypes.ITEM_STACK,
				Component.translatable(ExtraDelight.MOD_ID + ".jei.info.mint"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.CINNAMON_SAPLING.get()),
						new ItemStack(ExtraDelightItems.CINNAMON_LOG.get()),
						new ItemStack(ExtraDelightItems.CINNAMON_BARK.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.cinnamon"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.WILD_GINGER.get()),
						new ItemStack(ExtraDelightItems.GINGER.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.ginger"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.CORN_SEEDS.get()),
						new ItemStack(ExtraDelightItems.UNSHUCKED_CORN.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.corn"));
		registry.addIngredientInfo(List.of(new ItemStack(ExtraDelightItems.WILD_CHILI_BLOCK.get()),
				new ItemStack(ExtraDelightItems.CHILI.get()), new ItemStack(ExtraDelightItems.CHILI_SEEDS.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.chili"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.WILD_MALLOW_ROOT_BLOCK.get()),
						new ItemStack(ExtraDelightItems.MALLOW_ROOT.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.mallow"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.WILD_PEANUT_BLOCK.get()),
						new ItemStack(ExtraDelightItems.PEANUTS_IN_SHELL.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.peanut"));
		registry.addIngredientInfo(List.of(new ItemStack(ExtraDelightItems.COFFEE_CHERRIES.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.coffee"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.HAZELNUT_SAPLING.get()),
						new ItemStack(ExtraDelightItems.HAZELNUTS_IN_SHELL.get()),
						new ItemStack(ExtraDelightItems.HAZELNUT_LEAVES.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.hazelnut"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.APPLE_SAPLING.get()), new ItemStack(Items.APPLE),
						new ItemStack(ExtraDelightItems.APPLE_LEAVES.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.apple"));
		registry.addIngredientInfo(
				List.of(new ItemStack(ExtraDelightItems.WILD_GARLIC_BLOCK.get()),
						new ItemStack(ExtraDelightItems.GARLIC.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.garlic"));
		registry.addIngredientInfo(
				List.of(new ItemStack(Fermentation.WILD_CUCUMBER.get()), new ItemStack(Fermentation.CUCUMBER.get()),
						new ItemStack(Fermentation.CUCUMBER_SEED.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.cucumber"));
		registry.addIngredientInfo(
				List.of(new ItemStack(Fermentation.WILD_SOYBEAN.get()), new ItemStack(Fermentation.SOYBEAN_POD.get())),
				VanillaTypes.ITEM_STACK, Component.translatable(ExtraDelight.MOD_ID + ".jei.info.soybean"));
		registry.addIngredientInfo(new ItemStack(Fermentation.PICKLE_JUICE.get()), VanillaTypes.ITEM_STACK,
				Component.translatable(ExtraDelight.MOD_ID + ".jei.info.pickle_juice"));
		registry.addIngredientInfo(List.of(new ItemStack(ExtraDelightItems.YEAST.get()), new ItemStack(ExtraDelightItems.YEAST_POT.get())),
									VanillaTypes.ITEM_STACK,
									Component.translatable(ExtraDelight.MOD_ID + ".jei.info.yeast"));

		List<ItemStack> hide = List.of(ExtraDelightItems.EASTER_EGG.get().getDefaultInstance());
		registry.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hide);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_AMETHYST.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_ANDESITE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_BASALT.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_BLACKSTONE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_DEEPSLATE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_DIORITE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_ENDSTONE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_GILDED_BLACKSTONE.get()),
				MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_GRANITE.get()), MortarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.PESTLE_STONE.get()), MortarRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.MORTAR_STONE.get()), MortarRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.WOODEN_SPOON.get()), MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.STONE_SPOON.get()), MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.IRON_SPOON.get()), MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.GOLD_SPOON.get()), MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.DIAMOND_SPOON.get()), MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.NETHERITE_SPOON.get()),
				MixingBowlRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.MIXING_BOWL.get()), MixingBowlRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.OVEN.get()), OvenRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.DRYING_RACK.get()), DryingRackRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.DOUGH_SHAPING.get()),
				DoughShapingRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.MELTING_POT.get()),
				MeltingPotRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.CHILLER.get()), ChillerRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.VAT.get()), VatRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.LID.get()), VatRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ExtraDelightItems.EVAPORATOR.get()),
				EvaporatorRecipeCategory.TYPE);

	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(OvenMenu.class, ExtraDelightContainers.OVEN_MENU.get(),
				OvenRecipeCategory.TYPE, 0, 6, 9, 36);
		registration.addRecipeTransferHandler(VatMenu.class, ExtraDelightContainers.VAT_MENU.get(),
				VatRecipeCategory.TYPE, 0, 6, 9, 36);
		registration.addRecipeTransferHandler(ChillerMenu.class, ExtraDelightContainers.CHILLER_MENU.get(),
				ChillerRecipeCategory.TYPE, 0, 6, 9, 36);
		registration.addRecipeTransferHandler(MixingBowlMenu.class, ExtraDelightContainers.MIXING_BOWL_MENU.get(),
				MixingBowlRecipeCategory.TYPE, 0, 6, 9, 36);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(OvenScreen.class, 90, 36, 22, 22, OvenRecipeCategory.TYPE);
		registration.addRecipeClickArea(VatScreen.class, 62, 50 - 19, 87, 18, VatRecipeCategory.TYPE);
		registration.addRecipeClickArea(ChillerScreen.class, 101, 42, 22, 15, ChillerRecipeCategory.TYPE);
		registration.addRecipeClickArea(MixingBowlScreen.class, 122, 23, 20, 18, MixingBowlRecipeCategory.TYPE);
	}

}
