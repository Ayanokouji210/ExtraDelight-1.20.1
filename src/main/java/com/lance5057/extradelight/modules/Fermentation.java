package com.lance5057.extradelight.modules;

import com.lance5057.extradelight.*;
import com.lance5057.extradelight.blocks.RecipeFeastBlock;
import com.lance5057.extradelight.blocks.RipeSalamiBlock;
import com.lance5057.extradelight.blocks.UnripeSalamiBlock;
import com.lance5057.extradelight.blocks.crops.CucumberCrop;
import com.lance5057.extradelight.blocks.crops.SoybeanCrop;
import com.lance5057.extradelight.blocks.fluids.VinegarFluidBlock;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlock;
import com.lance5057.extradelight.blocks.jardisplay.JarSingularBlock;
import com.lance5057.extradelight.blocks.jardisplay.JarSingularItem;
import com.lance5057.extradelight.client.BlockStateItemGeometryLoader;
import com.lance5057.extradelight.data.BlockModels;
import com.lance5057.extradelight.data.ItemModels;
import com.lance5057.extradelight.data.MiscLootTables;
import com.lance5057.extradelight.data.Recipes;
import com.lance5057.extradelight.data.recipebuilders.*;
import com.lance5057.extradelight.food.EDFoods;
import com.lance5057.extradelight.items.ToolTipConsumableItem;
import com.lance5057.extradelight.util.EDItemGenerator;
import com.lance5057.extradelight.workstations.vat.recipes.VatRecipe.StageIngredient;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
//import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
//import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
//import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
//import net.neoforged.neoforge.client.model.generators.ModelFile;
//import net.neoforged.neoforge.common.Tags;
//import net.neoforged.neoforge.common.crafting.CompoundIngredient;
//import net.neoforged.neoforge.common.data.LanguageProvider;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
//import net.neoforged.neoforge.registries.RegistryObject;
//import net.neoforged.neoforge.registries.RegistryObject;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.WildCropBlock;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
//import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.data.recipe.CookingRecipes;

import java.util.function.Consumer;

public class Fermentation {
	public static final RegistryObject<CucumberCrop> CUCUMBER_CROP = ExtraDelightBlocks.BLOCKS.register("cucumber_crop",
			() -> new CucumberCrop(Block.Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<SoybeanCrop> SOYBEAN_CROP = ExtraDelightBlocks.BLOCKS.register("soybean_crop",
			() -> new SoybeanCrop(Block.Properties.copy(Blocks.WHEAT)));

	public static final RegistryObject<Block> WILD_CUCUMBER = ExtraDelightBlocks.BLOCKS.register("wild_cucumber",
			() -> new WildCropBlock(MobEffects.MOVEMENT_SPEED, 6, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_SOYBEAN = ExtraDelightBlocks.BLOCKS.register("wild_soybean",
			() -> new WildCropBlock(MobEffects.SATURATION, 6, Block.Properties.copy(Blocks.TALL_GRASS)));

	public static final RegistryObject<Item> WILD_CUCUMBER_ITEM = ExtraDelightItems.ITEMS.register("wild_cucumber_item",
			() -> new BlockItem(WILD_CUCUMBER.get(), new Item.Properties()));
	public static final RegistryObject<Item> WILD_SOYBEAN_ITEM = ExtraDelightItems.ITEMS.register("wild_soybean_item",
			() -> new BlockItem(WILD_SOYBEAN.get(), new Item.Properties()));

	public static final RegistryObject<Item> CUCUMBER = EDItemGenerator
			.register("cucumber", () -> new Item(new Item.Properties().food(EDFoods.CUCUMBER))).advancementIngredients()
			.finish();
	public static final RegistryObject<Item> SOYBEAN_POD = EDItemGenerator
			.register("soybean_pod", () -> new Item(new Item.Properties())).advancementIngredients().finish();

	public static final RegistryObject<Item> CUCUMBER_SEED = ExtraDelightItems.ITEMS.register("cucumber_seed",
			() -> new ItemNameBlockItem(CUCUMBER_CROP.get(), new Item.Properties()));
	public static final RegistryObject<Item> SOYBEANS = ExtraDelightItems.ITEMS.register("soybeans",
			() -> new ItemNameBlockItem(SOYBEAN_CROP.get(), new Item.Properties()));

	public static final RegistryObject<Item> SALT = EDItemGenerator.register("salt",
			() -> new Item(new Item.Properties())).advancementIngredients().finish();
	public static final RegistryObject<Block> SALT_BLOCK = ExtraDelightBlocks.BLOCKS.register("salt_block",
			() -> new Block(Block.Properties.copy(Blocks.REDSTONE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final RegistryObject<Item> SALT_BLOCK_ITEM = ExtraDelightItems.ITEMS.register("salt_block_item",
			() -> new BlockItem(SALT_BLOCK.get(), new Item.Properties()));

	public static final RegistryObject<Block> CUCUMBER_CRATE = ExtraDelightBlocks.BLOCKS.register("cucumber_crate",
			() -> new Block(Block.Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.PLANT)));
	public static final RegistryObject<Item> CUCUMBER_CRATE_ITEM = ExtraDelightItems.ITEMS.register("cucumber_crate_item",
			() -> new BlockItem(CUCUMBER_CRATE.get(), new Item.Properties()));
	public static final RegistryObject<Block> SOYBEAN_SACK = ExtraDelightBlocks.BLOCKS.register("soybean_sack",
			() -> new Block(
					Block.Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final RegistryObject<Item> SOYBEAN_SACK_ITEM = ExtraDelightItems.ITEMS.register("soybean_sack_item",
			() -> new BlockItem(SOYBEAN_SACK.get(), new Item.Properties()));

	public static final RegistryObject<JarSingularBlock> GHERKINS_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("gherkins_block", () -> new JarSingularBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)
					.strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> GHERKINS_BLOCK_ITEM = EDItemGenerator
			.register("gherkins_block_item",
					() -> new JarSingularItem(GHERKINS_BLOCK.get(),new Item.Properties()))
//							new Item.Properties().component(DataComponents.BLOCK_STATE,
//									BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> GHERKIN_ITEM = EDItemGenerator
			.register("gherkin_item", () -> new Item(new Item.Properties().food(EDFoods.GHERKINS)))
			.advancementIngredients().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_BEETS_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_beets_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_BEETS_BLOCK_ITEM = EDItemGenerator
			.register("pickled_beets_block_item",
					() -> new JarSingularItem(PICKLED_BEETS_BLOCK.get(),
							new Item.Properties()))
//									.component(DataComponents.BLOCK_STATE,
//									BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> PICKLED_BEET_ITEM = EDItemGenerator
			.register("pickled_beet_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_BEET)))
			.advancementIngredients().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_ONIONS_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_onions_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_ONIONS_BLOCK_ITEM = EDItemGenerator
			.register("pickled_onions_block_item",
					() -> new JarSingularItem(PICKLED_ONIONS_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> PICKLED_ONION_ITEM = EDItemGenerator
			.register("pickled_onion_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_ONION)))
			.advancementIngredients().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_CARROTS_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_carrots_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_CARROTS_BLOCK_ITEM = EDItemGenerator
			.register("pickled_carrots_block_item",
					() -> new JarSingularItem(PICKLED_CARROTS_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> PICKLED_CARROT_ITEM = EDItemGenerator
			.register("pickled_carrot_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_CARROT)))
			.advancementSnack().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_EGGS_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_eggs_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_EGGS_BLOCK_ITEM = EDItemGenerator
			.register("pickled_eggs_block_item",
					() -> new JarSingularItem(PICKLED_EGGS_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> PICKLED_EGG_ITEM = EDItemGenerator
			.register("pickled_egg_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_EGG)))
			.advancementSnack().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_FISH_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_fish_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_FISH_BLOCK_ITEM = EDItemGenerator
			.register("pickled_fish_block_item",
					() -> new JarSingularItem(PICKLED_FISH_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();
	public static final RegistryObject<Item> PICKLED_FISH_ITEM = EDItemGenerator
			.register("pickled_fish_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_FISH)))
			.advancementSnack().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_SAUSAGE_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_sausage_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_SAUSAGE_BLOCK_ITEM = EDItemGenerator
			.register("pickled_sausage_block_item",
					() -> new JarSingularItem(PICKLED_SAUSAGE_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementButchercraft().finish();
	public static final RegistryObject<Item> PICKLED_SAUSAGE_ITEM = EDItemGenerator
			.register("pickled_sausage_item", () -> new Item(new Item.Properties().food(EDFoods.PICKLED_SAUSAGE)))
			.advancementButchercraft().servingToolTip().finish();

	public static final RegistryObject<JarSingularBlock> PICKLED_GINGER_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("pickled_ginger_block", () -> new JarSingularBlock(BlockBehaviour.Properties
					.copy(Blocks.GLASS).strength(0.8F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Item> PICKLED_GINGER_BLOCK_ITEM = EDItemGenerator
			.register("pickled_ginger_block_item",
					() -> new JarSingularItem(PICKLED_GINGER_BLOCK.get(),
							new Item.Properties()))
									//.component(DataComponents.BLOCK_STATE,
									//BlockItemStateProperties.EMPTY.with(RecipeFeastBlock.SERVINGS, 4))))
			.advancementFeast().finish();

	public static final RegistryObject<Item> PICKLE_JUICE = EDItemGenerator
			.register("pickle_juice", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> PICKLE_JUICE_FLUID_BUCKET = ExtraDelightItems.ITEMS.register(
			"pickle_juice_fluid_bucket", () -> ExtraDelightItems.stack1bucketItem(ExtraDelightFluids.PICKLE_JUICE));
	public static final RegistryObject<VinegarFluidBlock> PICKLE_JUICE_FLUID_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"pickle_juice_fluid_block", () -> new VinegarFluidBlock(ExtraDelightFluids.PICKLE_JUICE.FLUID.get(),
					BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));

	public static final RegistryObject<Item> SOY_SAUCE_ITEM = EDItemGenerator
			.register("soy_sauce_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(EDFoods.SOY_SAUCE)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> SAUERKRAUT_ITEM = EDItemGenerator
			.register("sauerkraut_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(EDFoods.SAUERKRAUT)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> KIMCHI_ITEM = EDItemGenerator
			.register("kimchi_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(EDFoods.KIMCHI)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> HOT_SAUCE_ITEM = EDItemGenerator
			.register("hot_sauce_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(EDFoods.HOT_SAUCE)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> MISO_PASTE_ITEM = EDItemGenerator
			.register("miso_paste_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(EDFoods.MISO_PASTE)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> NATTO_ITEM = EDItemGenerator
			.register("natto_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(EDFoods.NATTO)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> FISH_SAUCE_ITEM = EDItemGenerator
			.register("fish_sauce_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(EDFoods.FISH_SAUCE)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> SALAMI_MIX = EDItemGenerator
			.register("salami_mix", () -> new Item(new Item.Properties().food(EDFoods.SALAMI_MIX)))
			.advancementIngredients().finish();

	public static final RegistryObject<Block> UNRIPE_SALAMI_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("unripe_salami_block", () -> new UnripeSalamiBlock(
					Block.Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.TERRACOTTA_PINK)));
	public static final RegistryObject<Item> UNRIPE_SALAMI_ITEM = EDItemGenerator
			.register("unripe_salami_item",
					() -> new BlockItem(UNRIPE_SALAMI_BLOCK.get(), new Item.Properties().food(EDFoods.UNRIPE_SALAMI)))
			.advancementIngredients().finish();

	public static final RegistryObject<Block> SALAMI_BLOCK = ExtraDelightBlocks.BLOCKS.register("salami_block",
			() -> new RipeSalamiBlock(
					Block.Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.TERRACOTTA_RED)));
	public static final RegistryObject<Item> SALAMI_ITEM = EDItemGenerator
			.register("salami_item",
					() -> new BlockItem(SALAMI_BLOCK.get(), new Item.Properties().food(EDFoods.SALAMI)))
			.advancementSnack().finish();

	public static final RegistryObject<Item> SOAKED_SOYBEANS_ITEM = EDItemGenerator
			.register("soaked_soybeans_item",
					() -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(EDFoods.COOKED_SOYBEANS)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> MASHED_SOYBEANS_ITEM = EDItemGenerator
			.register("mashed_soybeans_item", () -> new Item(new Item.Properties().food(EDFoods.COOKED_SOYBEANS)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> COOKED_SOYBEANS_ITEM = EDItemGenerator
			.register("cooked_soybeans_item", () -> new Item(new Item.Properties().food(EDFoods.COOKED_SOYBEANS)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> SOY_MILK = EDItemGenerator
			.register("soy_milk", () -> new MilkBottleItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> NAEM_MOO_ITEM = EDItemGenerator
			.register("naem_moo_item", () -> new Item(new Item.Properties().food(EDFoods.NAEM_MOO)))
			.advancementSnack().finish();

	public static final RegistryObject<Item> SLICED_BEETROOT_ITEM = EDItemGenerator
			.register("sliced_beetroot_item", () -> new Item(new Item.Properties().food(Foods.BEETROOT)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> SHREDDED_CABBAGE_ITEM = EDItemGenerator
			.register("shredded_cabbage_item", () -> new Item(new Item.Properties().food(FoodValues.CABBAGE)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> SLICED_CUCUMBER_ITEM = EDItemGenerator
			.register("sliced_cucumber_item", () -> new Item(new Item.Properties().food(EDFoods.CUCUMBER)))
			.advancementIngredients().finish();
	public static final RegistryObject<Item> SLICED_GHERKIN_ITEM = EDItemGenerator
			.register("sliced_gherkin_item", () -> new Item(new Item.Properties().food(EDFoods.GHERKINS)))
			.advancementIngredients().finish();

	public static final RegistryObject<Item> GAZPACHO = EDItemGenerator
			.register("gazpacho", () -> new Item(new Item.Properties().food(EDFoods.GAZPACHO))).advancementMeal()
			.finish();
	public static final RegistryObject<Item> EDAMAME = EDItemGenerator
			.register("edamame", () -> new Item(new Item.Properties().food(EDFoods.EDAMAME))).advancementSnack()
			.finish();
	public static final RegistryObject<Item> BEEF_BULGOGI = EDItemGenerator
			.register("beef_bulgogi", () -> new Item(new Item.Properties().food(EDFoods.BEEF_BULGOGI)))
			.advancementButchercraft().finish();
	public static final RegistryObject<Item> HONEY_CHILI_CHICKEN = EDItemGenerator
			.register("honey_chili_chicken", () -> new Item(new Item.Properties().food(EDFoods.HONEY_CHILI_CHICKEN)))
			.advancementButchercraft().finish();
	public static final RegistryObject<Item> CARAMEL_CHICKEN = EDItemGenerator
			.register("caramel_chicken", () -> new Item(new Item.Properties().food(EDFoods.CARAMEL_CHICKEN)))
			.advancementButchercraft().finish();
	public static final RegistryObject<RecipeFeastBlock> SOY_GLAZED_SALMON_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"soy_glazed_salmon_block",
			() -> new RecipeFeastBlock(
					BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_ORANGE), true,
					Block.box(0, 0, 0, 0, 0, 0), Block.box(-4.0D, 0.0D, 2.5D, 20.0D, 1.0D, 11.5D),
					Block.box(2.5D, 0.0D, -4.0D, 11.5D, 1.0D, 20.0D)));
	public static final RegistryObject<Item> SOY_GLAZED_SALMON_BLOCK_ITEM = EDItemGenerator
			.register("soy_glazed_salmon_block_item",
					() -> new BlockItem(SOY_GLAZED_SALMON_BLOCK.get(), new Item.Properties()))
			.advancementFeast().finish();
	public static final RegistryObject<Item> SOY_GLAZED_SALMON_ITEM = EDItemGenerator
			.register("soy_glazed_salmon_item", () -> new Item(new Item.Properties().food(EDFoods.SOY_GLAZED_SALMON)))
			.advancementMeal().servingToolTip().finish();
	public static final RegistryObject<Item> STEAK_PICKLED_ONION_PIE_SLICE = EDItemGenerator
			.register("steak_pickled_onion_pie_slice",
					() -> new ToolTipConsumableItem(ExtraDelightItems.stack16FoodItem(EDFoods.BACON_EGG_PIE), true))
			.advancementButchercraft().servingToolTip().finish();
	public static final RegistryObject<Block> STEAK_PICKLED_ONION_PIE = ExtraDelightBlocks.BLOCKS.register(
			"steak_pickled_onion_pie",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), STEAK_PICKLED_ONION_PIE_SLICE));
	public static final RegistryObject<Item> STEAK_PICKLED_ONION_PIE_ITEM = EDItemGenerator
			.register("steak_pickled_onion_pie_item",
					() -> new BlockItem(STEAK_PICKLED_ONION_PIE.get(), new Item.Properties()))
			.advancementButchercraft().feastToolTip().finish();
	public static final RegistryObject<Item> KIWIBURGER = EDItemGenerator
			.register("kiwiburger", () -> new Item(new Item.Properties().food(EDFoods.KIWIBURGER))).advancementMeal()
			.finish();
	public static final RegistryObject<Item> SHIRAZI_SALAD = EDItemGenerator
			.register("shirazi_salad", () -> new Item(new Item.Properties().food(EDFoods.SHIRAZI_SALAD)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> CUCUMBER_SALAD = EDItemGenerator
			.register("cucumber_salad", () -> new Item(new Item.Properties().food(EDFoods.CUCUMBER_SALAD)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> MISO_SOUP = EDItemGenerator
			.register("miso_soup", () -> new Item(new Item.Properties().food(EDFoods.MISO_SOUP))).advancementMeal()
			.finish();
	public static final RegistryObject<Item> NATTO_AND_RICE = EDItemGenerator
			.register("natto_and_rice", () -> new Item(new Item.Properties().food(EDFoods.NATTO_AND_RICE)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> SAUERKRAUT_SOUP = EDItemGenerator
			.register("sauerkraut_soup", () -> new Item(new Item.Properties().food(EDFoods.SAUERKRAUT_SOUP)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> SAUERKRAUT_AND_SAUSAGE = EDItemGenerator
			.register("sauerkraut_and_sausage",
					() -> new Item(new Item.Properties().food(EDFoods.SAUERKRAUT_AND_SAUSAGE)))
			.advancementButchercraft().finish();
	public static final RegistryObject<Item> YEAST_SPREAD = EDItemGenerator
			.register("yeast_spread",
					() -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(EDFoods.YEAST_SPREAD)))
			.advancementIngredients().finish();

	public static final RegistryObject<RecipeFeastBlock> CHEESYMITE_SCROLL_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"cheesymite_scroll_block",
			() -> new RecipeFeastBlock(
					BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_ORANGE), true,
					ExtraDelightBlocks.plate));
	public static final RegistryObject<Item> CHEESYMITE_SCROLL_BLOCK_ITEM = EDItemGenerator
			.register("cheesymite_scroll_block_item",
					() -> new BlockItem(CHEESYMITE_SCROLL_BLOCK.get(), new Item.Properties()))
			.advancementFeast().finish();
	public static final RegistryObject<Item> CHEESYMITE_SCROLL_SERVING = EDItemGenerator
			.register("cheesymite_scroll",
					() -> new Item(new Item.Properties().food(EDFoods.CHEESYMITE_SCROLL_SERVING)))
			.advancementSnack().servingToolTip().finish();

	public static final RegistryObject<Item> MORKOVCHA = EDItemGenerator
			.register("morkovcha", () -> new Item(new Item.Properties().food(EDFoods.MORKOVCHA))).advancementMeal()
			.finish();
	public static final RegistryObject<Item> ZUPA_OGORKOWA = EDItemGenerator
			.register("zupa_ogorkowa", () -> new Item(new Item.Properties().food(EDFoods.ZUPA_OGORKOWA)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> KIMCHI_FRIED_RICE = EDItemGenerator
			.register("kimchi_fried_rice", () -> new Item(new Item.Properties().food(EDFoods.KIMCHI_FRIED_RICE)))
			.advancementMeal().finish();
	public static final RegistryObject<Item> KONGJANG = EDItemGenerator
			.register("kongjang", () -> new Item(new Item.Properties().food(EDFoods.KONGJANG))).advancementMeal()
			.finish();
	public static final RegistryObject<Item> CHEESEBURGER_PICKLE = EDItemGenerator
			.register("cheeseburger_pickle", () -> new Item(new Item.Properties().food(EDFoods.CHEESEBURGER_PICKLE)))
			.advancementSnack().finish();
	public static final RegistryObject<Item> HOT_WINGS = EDItemGenerator
			.register("hot_wings", () -> new Item(new Item.Properties().food(EDFoods.HOT_WINGS))).advancementMeal()
			.finish();

	public static final RegistryObject<Block> JAR_DISPLAY_BLOCK = ExtraDelightBlocks.BLOCKS.register("jar_display_block",
			() -> new JarDisplayBlock(Block.Properties.copy(Blocks.GLASS).strength(0.8F)));

	public static final RegistryObject<Item> COOKED_WHEAT_SEEDS = EDItemGenerator
			.register("cooked_wheat_seeds", () -> new Item(new Item.Properties().food(EDFoods.COOKED_WHEAT_SEEDS)))
			.advancementIngredients().finish();

	public static void blockModels(BlockStateProvider bsp) {
		BlockModels.crateBlock(bsp, CUCUMBER_CRATE.get(), "cucumber", "oak");
		BlockModels.sackBlock(bsp, SOYBEAN_SACK.get(), "soybean", "brown");
		BlockModels.recipeFeastBlock(bsp, GHERKINS_BLOCK.get(), "gherkin_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_BEETS_BLOCK.get(), "pickled_beets_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_ONIONS_BLOCK.get(), "pickled_onions_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_CARROTS_BLOCK.get(), "pickled_carrot_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_EGGS_BLOCK.get(), "pickled_egg_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_FISH_BLOCK.get(), "pickled_fish_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_SAUSAGE_BLOCK.get(), "pickled_sausage_jar");
		BlockModels.recipeFeastBlock(bsp, PICKLED_GINGER_BLOCK.get(), "pickled_ginger_jar");
		BlockModels.recipeFeastBlock(bsp, SOY_GLAZED_SALMON_BLOCK.get(), "soy_glazed_salmon");
		BlockModels.recipeFeastBlock(bsp, CHEESYMITE_SCROLL_BLOCK.get(), "cheesy_vegemite_scrolls");

//		BlockModels.pieBlock(bsp, STEAK_PICKLED_ONION_PIE.get(), "steak_and_pickled_onion_pie");

		bsp.getVariantBuilder(STEAK_PICKLED_ONION_PIE.get()).forAllStates(state -> {
			int bites = state.getValue(PieBlock.BITES);
			String suffix = bites > 0 ? "_slice" + bites : "";
			return ConfiguredModel.builder().modelFile(bsp.models()
					.withExistingParent(
							ForgeRegistries.BLOCKS.getKey(STEAK_PICKLED_ONION_PIE.get()).getPath() + suffix,
							bsp.modLoc("block/pie" + suffix))
					.texture("particle", bsp.modLoc("block/meat_pie_top"))
					.texture("top", bsp.modLoc("block/meat_pie_top"))
					.texture("inner", bsp.modLoc("block/steak_and_pickled_onion_pie_inner")))
					.rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + 180) % 360).build();
		});

		BlockModels.cropCrossBlock(bsp, CUCUMBER_CROP.get(), "cucumber", CucumberCrop.AGE);
		bsp.simpleBlock(WILD_CUCUMBER.get(), new ConfiguredModel(bsp.models()
				.cross("wild_cucumber", bsp.modLoc("block/crops/cucumber/cucumber_stage7")).renderType("cutout")));
		BlockModels.cropCrossBlock(bsp, SOYBEAN_CROP.get(), "soybeans", SoybeanCrop.AGE);
		bsp.simpleBlock(WILD_SOYBEAN.get(), new ConfiguredModel(bsp.models()
				.cross("wild_soybean", bsp.modLoc("block/crops/soybeans/soybeans_stage7")).renderType("cutout")));
		bsp.simpleBlock(JAR_DISPLAY_BLOCK.get(), bsp.models().withExistingParent("jar_display", bsp.mcLoc("air"))
				.texture("particle", bsp.mcLoc("block/glass")));

		bsp.getVariantBuilder(UNRIPE_SALAMI_BLOCK.get()).forAllStates(state -> {
			int count = state.getValue(UnripeSalamiBlock.COUNT);

			String suffix = "_" + (count + 1);

			return ConfiguredModel.builder()
					.modelFile(bsp.models()
							.withExistingParent("block/unripe_salami_block" + suffix.toLowerCase(),
									bsp.modLoc("block/unripe_salami" + suffix.toLowerCase()))
							.renderType("cutout"))
					.rotationY(((int) state.getValue(UnripeSalamiBlock.FACING).toYRot()) % 360).build();
		});

		bsp.getVariantBuilder(SALAMI_BLOCK.get()).forAllStates(state -> {
			int count = state.getValue(RipeSalamiBlock.COUNT);

			String suffix = "_" + (count + 1);

			return ConfiguredModel.builder()
					.modelFile(bsp.models()
							.withExistingParent("block/ripe_salami_block" + suffix.toLowerCase(),
									bsp.modLoc("block/ripe_salami" + suffix.toLowerCase()))
							.renderType("cutout"))
					.rotationY(((int) state.getValue(RipeSalamiBlock.FACING).toYRot()) % 360).build();
		});

		bsp.simpleBlock(SALT_BLOCK.get());
	}

	public static void itemModels(ItemModelProvider tmp) {
		ItemModels.forBlockItemFlat(tmp, WILD_CUCUMBER_ITEM, "crops/cucumber/cucumber_stage7");
		ItemModels.forItem(tmp, CUCUMBER, "crops/cucumber/cucumber");
		ItemModels.forBlockItemFlat(tmp, WILD_SOYBEAN_ITEM, "crops/soybeans/soybeans_stage7");
		ItemModels.forItem(tmp, SOYBEAN_POD, "crops/soybeans/soybeans_pod");
		ItemModels.forItem(tmp, CUCUMBER_SEED, "crops/cucumber/cucumber_seeds");
		ItemModels.forItem(tmp, SOYBEANS, "crops/soybeans/soybeans");
		ItemModels.forBlockItem(tmp, CUCUMBER_CRATE_ITEM, "cucumber_crate");
		ItemModels.forBlockItem(tmp, SOYBEAN_SACK_ITEM, "soybean_sack");
		tmp.getBuilder(GHERKINS_BLOCK_ITEM.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, GHERKIN_ITEM, "gherkin");
		tmp.getBuilder(PICKLED_BEETS_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_BEET_ITEM, "pickled_beetroot");
		tmp.getBuilder(PICKLED_ONIONS_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_ONION_ITEM, "pickled_onion");
		tmp.getBuilder(PICKLED_CARROTS_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_CARROT_ITEM, "pickled_carrot");
		tmp.getBuilder(PICKLED_EGGS_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_EGG_ITEM, "pickled_egg");
		tmp.getBuilder(PICKLED_FISH_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_FISH_ITEM, "pickled_fish");
		tmp.getBuilder(PICKLED_GINGER_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, PICKLED_SAUSAGE_ITEM, "pickled_sausage");
		tmp.getBuilder(PICKLED_SAUSAGE_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);

		tmp.getBuilder(CHEESYMITE_SCROLL_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);

		ItemModels.forItem(tmp, SOY_SAUCE_ITEM, "soy_sauce");
		ItemModels.forItem(tmp, HOT_SAUCE_ITEM, "hot_sauce");
		ItemModels.forItem(tmp, MISO_PASTE_ITEM, "miso_paste");
		ItemModels.forItem(tmp, NATTO_ITEM, "natto");
		ItemModels.forItem(tmp, FISH_SAUCE_ITEM, "fish_sauce");
		ItemModels.forItem(tmp, SALAMI_MIX, "salami_mix");
		tmp.getBuilder(UNRIPE_SALAMI_ITEM.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		tmp.getBuilder(SALAMI_ITEM.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, SOAKED_SOYBEANS_ITEM, "soaked_soybeans");
		ItemModels.forItem(tmp, SHREDDED_CABBAGE_ITEM, "shredded_cabbage");
		ItemModels.forItem(tmp, SLICED_CUCUMBER_ITEM, "crops/cucumber/cucumber_slices");
		ItemModels.forItem(tmp, SLICED_GHERKIN_ITEM, "gherkin_slices");
		ItemModels.forItem(tmp, PICKLE_JUICE, "pickle_juice_bottle");
		ItemModels.forItem(tmp, PICKLE_JUICE_FLUID_BUCKET, "pickle_juice_bucket");

		ItemModels.forItem(tmp, SALT, "salt");
		ItemModels.forItem(tmp, SAUERKRAUT_ITEM, "sauerkraut");
		ItemModels.forItem(tmp, KIMCHI_ITEM, "kimchi");
		ItemModels.forItem(tmp, SOY_MILK, "soy_milk_bottle");
		ItemModels.forItem(tmp, SLICED_BEETROOT_ITEM, "sliced_beetroot");
		ItemModels.forItem(tmp, EDAMAME, "edamame");
		ItemModels.forItem(tmp, MISO_SOUP, "miso_soup");
		ItemModels.forItem(tmp, YEAST_SPREAD, "yeast_extract");
		ItemModels.forItem(tmp, NAEM_MOO_ITEM, "naem_moo");
		ItemModels.forItem(tmp, COOKED_SOYBEANS_ITEM, "cooked_soybeans");
		ItemModels.forItem(tmp, MASHED_SOYBEANS_ITEM, "mashed_soybeans");
		ItemModels.forItem(tmp, KIWIBURGER, "kiwiburger");
		ItemModels.forItem(tmp, STEAK_PICKLED_ONION_PIE_ITEM, "steak_and_pickled_onion_pie");
		ItemModels.forItem(tmp, STEAK_PICKLED_ONION_PIE_SLICE, "steak_and_pickled_onion_pie_slice");
		tmp.getBuilder(SOY_GLAZED_SALMON_BLOCK_ITEM.getId().getPath())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.customLoader(BlockStateItemGeometryLoader::builder);
		ItemModels.forItem(tmp, SOY_GLAZED_SALMON_ITEM, "soy_glazed_salmon");
		ItemModels.forItem(tmp, GAZPACHO, "gazpacho");
		ItemModels.forItem(tmp, BEEF_BULGOGI, "beef_bulgogi");
		ItemModels.forItem(tmp, HONEY_CHILI_CHICKEN, "honey_chili_chicken");
		ItemModels.forItem(tmp, CARAMEL_CHICKEN, "caramel_chicken");
		ItemModels.forItem(tmp, CUCUMBER_SALAD, "cucumber_salad");
		ItemModels.forItem(tmp, SHIRAZI_SALAD, "shirazi_salad");
		ItemModels.forItem(tmp, NATTO_AND_RICE, "natto_rice");
		ItemModels.forItem(tmp, SAUERKRAUT_SOUP, "sauerkraut_soup");
		ItemModels.forItem(tmp, SAUERKRAUT_AND_SAUSAGE, "sauerkraut_sausage");
		ItemModels.forItem(tmp, CHEESYMITE_SCROLL_SERVING, "cheesy_vegemite_scroll");
		ItemModels.forItem(tmp, ZUPA_OGORKOWA, "zupa_ogorkowa");
		ItemModels.forItem(tmp, MORKOVCHA, "morkovcha");
		ItemModels.forItem(tmp, KIMCHI_FRIED_RICE, "kimchi_fried_rice");
		ItemModels.forItem(tmp, KONGJANG, "kongjang");
		ItemModels.forItem(tmp, CHEESEBURGER_PICKLE, "cheeseburger_pickle_popper");

		ItemModels.forBlockItem(tmp, SALT_BLOCK_ITEM, "salt");

		ItemModels.forItem(tmp, HOT_WINGS, "hot_wings");
		ItemModels.forItem(tmp, COOKED_WHEAT_SEEDS, "cooked_wheat_seeds");
	}

	public final static int dayTick = 24000;
	public final static int hourTick = 1000;

	public static void Recipes(Consumer<FinishedRecipe> consumer) {
		// Vanilla Crafting
		Recipes.bucket("pickle_juice", consumer, PICKLE_JUICE_FLUID_BUCKET.get(), Items.GLASS_BOTTLE,
				PICKLE_JUICE.get());

		Recipes.bundleItem9(Ingredient.of(ExtraDelightTags.CUCUMBER), CUCUMBER_CRATE_ITEM.get(), CUCUMBER.get(),
				consumer, "cucumber");
		Recipes.bundleItem9(Ingredient.of(ExtraDelightTags.SOYBEAN), SOYBEAN_SACK_ITEM.get(), SOYBEANS.get(), consumer,
				"soybean");
		Recipes.bundleItem9(Ingredient.of(SALT.get()), SALT_BLOCK_ITEM.get(), SALT.get(), consumer, "salt");

		Recipes.vanillaCooking(Ingredient.of(Items.WHEAT_SEEDS), COOKED_WHEAT_SEEDS.get(), consumer,
				"cooked_wheat_seeds");

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, STEAK_PICKLED_ONION_PIE_ITEM.get()).pattern("ff ")
				.pattern("ff ").define('f', STEAK_PICKLED_ONION_PIE_SLICE.get())
				.unlockedBy("has_pie",
						InventoryChangeTrigger.TriggerInstance.hasItems(STEAK_PICKLED_ONION_PIE_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("steak_pickled_onion_pie_slice"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, CUCUMBER_SEED.get(), 1).requires(CUCUMBER.get())
				.unlockedBy("has_cucumber", InventoryChangeTrigger.TriggerInstance.hasItems(CUCUMBER.get()))
				.save(consumer, ExtraDelight.modLoc("cucumber_seeds"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, KIWIBURGER.get(), 1).requires(ForgeTags.BREAD)
				.requires(ModItems.BEEF_PATTY.get()).requires(ForgeTags.CROPS_CABBAGE)
				.requires(ExtraDelightTags.PROCESSED_TOMATO).requires(ExtraDelightTags.PROCESSED_ONION)
				.requires(ExtraDelightTags.PICKLED_BEETROOT).requires(ModItems.FRIED_EGG.get())
				.unlockedBy("has_beetroot", InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_BEET_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("kiwiburger"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, KIWIBURGER.get(), 1).requires(ModItems.HAMBURGER.get())
				.requires(ExtraDelightTags.PICKLED_BEETROOT).requires(ModItems.FRIED_EGG.get())
				.unlockedBy("has_beetroot", InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_BEET_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("kiwiburger_burger"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, NATTO_AND_RICE.get()).requires(ExtraDelightTags.NATTO)
				.requires(ModItems.COOKED_RICE.get()).requires(ExtraDelightTags.SOY_SAUCE)
				.requires(ModItems.FRIED_EGG.get())
				.unlockedBy("natto_and_rice", InventoryChangeTrigger.TriggerInstance.hasItems(NATTO_ITEM.get()))
				.save(consumer);

		// Cutting board
		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(WILD_CUCUMBER.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						CUCUMBER_SEED.get(), 1)
				.addResultWithChance(CUCUMBER.get(), 0.2f, 1).addResultWithChance(Items.GREEN_DYE, 0.1f, 1)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "wild_cucumber_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(WILD_SOYBEAN.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						SOYBEAN_POD.get(), 1)
				.addResultWithChance(Items.YELLOW_DYE, 0.5f, 2)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "wild_soybean_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(Items.BEETROOT), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						SLICED_BEETROOT_ITEM.get(), 2)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "sliced_beetroot_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(ModItems.CABBAGE_LEAF.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						SHREDDED_CABBAGE_ITEM.get(), 2)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "shredded_cabbage_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(CUCUMBER.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						SLICED_CUCUMBER_ITEM.get(), 3)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "sliced_cucumber_knife"));

		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(SOYBEAN_POD.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
				SOYBEANS.get(), 3).build(consumer, ExtraDelight.modLoc("cutting/" + "shucked_soybeans_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(GHERKIN_ITEM.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						SLICED_GHERKIN_ITEM.get(), 3)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "sliced_gherkin_knife"));

		CuttingBoardRecipeBuilder
				.cuttingRecipe(Ingredient.of(STEAK_PICKLED_ONION_PIE_ITEM.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES),
						STEAK_PICKLED_ONION_PIE_SLICE.get(), 4)
				.build(consumer, ExtraDelight.modLoc("cutting/" + "steak_pickled_onion_pie_knife"));

		// Feasts
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(GHERKIN_ITEM.get()),
						GHERKINS_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(GHERKINS_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("gherkin_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_BEET_ITEM.get()),
						PICKLED_BEETS_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_BEETS_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_beet_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_ONION_ITEM.get()),
						PICKLED_ONIONS_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_ONIONS_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_onion_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_CARROT_ITEM.get()),
						PICKLED_CARROTS_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_CARROTS_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_carrot_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_EGG_ITEM.get()),
						PICKLED_EGGS_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_EGGS_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_egg_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_FISH_ITEM.get()),
						PICKLED_FISH_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_FISH_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_fish_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(PICKLED_SAUSAGE_ITEM.get()),
						PICKLED_SAUSAGE_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_SAUSAGE_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_sausage_pull_feast"));
		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(ExtraDelightItems.PICKLED_GINGER.get()),
						PICKLED_GINGER_BLOCK_ITEM.get())
				.unlockedBy("has_pickle_jar",
						InventoryChangeTrigger.TriggerInstance.hasItems(PICKLED_GINGER_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("pickled_ginger_pull_feast"));

		FeastRecipeBuilder
				.feast(Ingredient.of(ModItems.COOKED_RICE.get()), new ItemStack(SOY_GLAZED_SALMON_ITEM.get()),
						SOY_GLAZED_SALMON_BLOCK_ITEM.get())
				.unlockedBy("has_soy_glazed_salmon",
						InventoryChangeTrigger.TriggerInstance.hasItems(SOY_GLAZED_SALMON_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("soy_glazed_salmon_pull_feast"));

		FeastRecipeBuilder
				.feast(Ingredient.of(ExtraDelightTags.SPOONS), new ItemStack(CHEESYMITE_SCROLL_SERVING.get()),
						CHEESYMITE_SCROLL_BLOCK_ITEM.get())
				.unlockedBy("has_cheesymite_scrolls",
						InventoryChangeTrigger.TriggerInstance.hasItems(CHEESYMITE_SCROLL_BLOCK_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("cheesymite_scroll_pull_feast"));

		// Mixing
		Recipes.mixing(new ItemStack(GAZPACHO.get(), 1), Recipes.LONG_GRIND, new ItemStack(Items.BOWL),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.PROCESSED_TOMATO),
						Ingredient.of(ExtraDelightTags.PROCESSED_CUCUMBER),
						Ingredient.of(ExtraDelightTags.BREAD_CRUMBS) },
				new FluidIngredient[] { FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.OIL.FLUID.get(), 250)),
						FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.VINEGAR.FLUID.get(), 250)) },
				consumer, "gazpacho_mixing");

		Recipes.mixing(new ItemStack(SHIRAZI_SALAD.get(), 1), Recipes.FAST_GRIND, new ItemStack(Items.BOWL),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.PROCESSED_TOMATO),
						Ingredient.of(ExtraDelightTags.PROCESSED_CUCUMBER),
						Ingredient.of(ExtraDelightTags.PROCESSED_ONION), Ingredient.of(ExtraDelightTags.MINT), },
				new FluidIngredient[] {
						FluidIngredient.fromFluid(ExtraDelightFluids.OIL.FLUID.get(), 250) },
				consumer, "shirazi_salad_mixing");

		Recipes.mixing(new ItemStack(CUCUMBER_SALAD.get(), 2), Recipes.FAST_GRIND, new ItemStack(Items.BOWL),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.PROCESSED_CUCUMBER),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC),
						Ingredient.of(ExtraDelightTags.PROCESSED_GINGER), Ingredient.of(ExtraDelightTags.SOY_SAUCE), },
				new FluidIngredient[] { FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.OIL.FLUID.get(), 250)),
						FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.VINEGAR.FLUID.get(), 250)) },
				consumer, "cucumber_salad_mixing");

		Recipes.mixing(new ItemStack(MORKOVCHA.get(), 3), Recipes.LONG_GRIND, new ItemStack(Items.BOWL),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.PROCESSED_CARROT),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC), Ingredient.of(ExtraDelightTags.CHILI_POWDER),
						Ingredient.of(ExtraDelightTags.SALT), },
				new FluidIngredient[] { FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.OIL.FLUID.get(), 250)),
						FluidIngredient.fromFluidStack(new FluidStack(ExtraDelightFluids.VINEGAR.FLUID.get(), 250)) },
				consumer, "morkovcha_mixing");

		Recipes.mixing(new ItemStack(HOT_WINGS.get(), 1), Recipes.FAST_GRIND, ItemStack.EMPTY,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.CHICKEN_WING_COOKED),
						Ingredient.of(ExtraDelightTags.BUTTER), Ingredient.of(ExtraDelightTags.HOT_SAUCE) },
				new FluidIngredient[] {}, consumer, "hot_wings_mixing");

		Recipes.mixing(new ItemStack(SALAMI_MIX.get(), 8), Recipes.STANDARD_GRIND, ItemStack.EMPTY,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.GROUND_BEEF), Ingredient.of(ExtraDelightTags.FAT),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC) },
				new FluidIngredient[] {}, consumer, "salami_mix");

		// Mortar
		MortarRecipeBuilder
				.grind(Ingredient.of(ExtraDelightTags.SOAKED_SOYBEANS), new ItemStack(MASHED_SOYBEANS_ITEM.get(),1),
						FluidStack.EMPTY, Recipes.STANDARD_GRIND)
				.unlockedBy("has_soaked_soybeans",
						InventoryChangeTrigger.TriggerInstance.hasItems(SOAKED_SOYBEANS_ITEM.get()))
				.save(consumer, ExtraDelight.modLoc("mashed_soybeans"));

		// Oven
		OvenRecipeBuilder
				.OvenRecipe(new ItemStack(STEAK_PICKLED_ONION_PIE_ITEM.get(), 1), Recipes.NORMAL_COOKING,
						Recipes.MEDIUM_EXP, new ItemStack(ExtraDelightItems.PIE_DISH.get()), false)
				.addIngredient(ExtraDelightTags.CUBED_BEEF_RAW).addIngredient(ExtraDelightTags.PICKLED_ONION)
				.addIngredient(ExtraDelightTags.GRAVY).addIngredient(ExtraDelightTags.CHEESE)
				.addIngredient(ModItems.PIE_CRUST.get()).unlockedByAnyIngredient(PICKLED_ONION_ITEM.get()).build(consumer);

		OvenRecipeBuilder
				.OvenRecipe(new ItemStack(CHEESYMITE_SCROLL_BLOCK_ITEM.get(), 1), Recipes.NORMAL_COOKING,
						Recipes.MEDIUM_EXP, new ItemStack(ExtraDelightItems.SQUARE_PAN.get()), false)
				.addIngredient(ExtraDelightTags.CHEESE).addIngredient(ExtraDelightTags.CHEESE)
				.addIngredient(ExtraDelightTags.CHEESE).addIngredient(ExtraDelightTags.DOUGH)
				.addIngredient(ExtraDelightTags.YEAST_SPREAD).addIngredient(ExtraDelightTags.DOUGH)
				.addIngredient(ExtraDelightTags.DOUGH).addIngredient(ExtraDelightTags.BUTTER)
				.addIngredient(ExtraDelightTags.DOUGH).unlockedByAnyIngredient(YEAST_SPREAD.get()).build(consumer);

		OvenRecipeBuilder
				.OvenRecipe(new ItemStack(CHEESEBURGER_PICKLE.get(), 3), Recipes.NORMAL_COOKING, Recipes.MEDIUM_EXP,
						new ItemStack(ExtraDelightItems.SHEET.get()), false)
				.addIngredient(ExtraDelightTags.GROUND_BEEF_RAW).addIngredient(ExtraDelightTags.CHEESE)
				.addIngredient(ExtraDelightTags.GROUND_BEEF_RAW).addIngredient(ForgeTags.RAW_BACON)
				.addIngredient(ForgeTags.RAW_BACON).addIngredient(ForgeTags.RAW_BACON)
				.addIngredient(ExtraDelightTags.PICKLED_CUCUMBER).addIngredient(ExtraDelightTags.PICKLED_CUCUMBER)
				.addIngredient(ExtraDelightTags.PICKLED_CUCUMBER).unlockedByAnyIngredient(GHERKIN_ITEM.get()).build(consumer);

		// Pot
		Recipes.pot(COOKED_SOYBEANS_ITEM.get(), 1, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.SOAKED_SOYBEANS) }, "cooked_soybeans", consumer);
		Recipes.pot(SOY_MILK.get(), 1, CookingRecipes.SLOW_COOKING, 1.0F, Items.GLASS_BOTTLE,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.MASHED_SOYBEANS) }, "soy_milk", consumer);

		Recipes.pot(EDAMAME.get(), 1, CookingRecipes.FAST_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(SOYBEAN_POD.get()), Ingredient.of(ExtraDelightTags.SALT) }, "edamame", consumer);

		Recipes.pot(BEEF_BULGOGI.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, ModItems.COOKED_RICE.get(),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.CUBED_BEEF_RAW),
						Ingredient.of(ExtraDelightTags.PROCESSED_ONION), Ingredient.of(ExtraDelightTags.CHILI_POWDER),
						Ingredient.of(ExtraDelightTags.HOT_SAUCE), Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC),
						Ingredient.of(ExtraDelightTags.SOY_SAUCE) },
				"beef_bulgogi", consumer);

		Recipes.pot(HONEY_CHILI_CHICKEN.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, ModItems.COOKED_RICE.get(),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.CUBED_CHICKEN_RAW), Ingredient.of(Items.HONEY_BOTTLE),
						Ingredient.of(ExtraDelightTags.PROCESSED_CHILI),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC),
						Ingredient.of(ExtraDelightTags.PROCESSED_GINGER), Ingredient.of(ExtraDelightTags.SOY_SAUCE) },
				"honey_chili_chicken", consumer);

		Recipes.pot(CARAMEL_CHICKEN.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, ModItems.COOKED_RICE.get(),
				new Ingredient[] { Ingredient.of(ExtraDelightTags.CHICKEN_THIGH_RAW), Ingredient.of(Items.SUGAR),
						Ingredient.of(ExtraDelightTags.PROCESSED_ONION),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC), Ingredient.of(ExtraDelightTags.FISH_SAUCE),
						Ingredient.of(ExtraDelightTags.SOY_SAUCE) },
				"caramel_chicken", consumer);

		Recipes.pot(SOY_GLAZED_SALMON_BLOCK_ITEM.get(), 1, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(Items.SALMON), Ingredient.of(Items.HONEY_BOTTLE),
						Ingredient.of(ExtraDelightTags.PROCESSED_GINGER), Ingredient.of(ExtraDelightTags.COOKING_OIL),
						Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC), Ingredient.of(ExtraDelightTags.SOY_SAUCE) },
				"soy_glazed_salmon", consumer);

		Recipes.pot(MISO_SOUP.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.BROTH), Ingredient.of(ExtraDelightTags.MISO_PASTE),
						Ingredient.of(Items.DRIED_KELP), Ingredient.of(ExtraDelightTags.MISO_SOUP_INGREDIENTS) },
				"miso_soup", consumer);

		Recipes.pot(SAUERKRAUT_SOUP.get(), 3, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.BROTH), Ingredient.of(ExtraDelightTags.SAUERKRAUT),
						Ingredient.of(ForgeTags.RAW_BACON), Ingredient.of(ExtraDelightTags.PROCESSED_CARROT),
						Ingredient.of(ExtraDelightTags.PROCESSED_ONION),
						Ingredient.of(ExtraDelightTags.PROCESSED_POTATO) },
				"sauerkraut_soup", consumer);

		Recipes.pot(SAUERKRAUT_AND_SAUSAGE.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.SAUSAGE_RAW),
						Ingredient.of(ExtraDelightTags.SAUERKRAUT), Ingredient.of(ForgeTags.RAW_BACON),
						Ingredient.of(Items.SUGAR), Ingredient.of(ExtraDelightTags.PROCESSED_ONION),
						Ingredient.of(ExtraDelightTags.PROCESSED_APPLE) },
				"sauerkraut_and_sausage", consumer);

		Recipes.pot(YEAST_SPREAD.get(), 1, CookingRecipes.SLOW_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightItems.YEAST.get()), Ingredient.of(ExtraDelightItems.YEAST.get()),
						Ingredient.of(ExtraDelightItems.YEAST.get()), Ingredient.of(ExtraDelightTags.SALT) },
				"yeast_spread", consumer);

		Recipes.pot(ZUPA_OGORKOWA.get(), 3, CookingRecipes.FAST_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.PROCESSED_ONION),
						Ingredient.of(ExtraDelightTags.PROCESSED_CARROT),
						Ingredient.of(ExtraDelightTags.PROCESSED_PICKLED_CUCUMBER),
						Ingredient.of(ExtraDelightTags.PROCESSED_POTATO), Ingredient.of(ExtraDelightTags.BROTH),
						Ingredient.of(ExtraDelightTags.PICKLE_JUICE) },
				"zupa_ogorkowa", consumer);

		Recipes.pot(KIMCHI_FRIED_RICE.get(), 2, CookingRecipes.FAST_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ModItems.COOKED_RICE.get()), Ingredient.of(ExtraDelightTags.KIMCHI),
						Ingredient.of(ExtraDelightTags.HOT_SAUCE), Ingredient.of(Items.DRIED_KELP),
						Ingredient.of(ExtraDelightTags.COOKING_OIL), Ingredient.of(ModItems.FRIED_EGG.get()) },
				"kimchi_fried_rice", consumer);

		Recipes.pot(KONGJANG.get(), 2, CookingRecipes.NORMAL_COOKING, 1.0F, Items.BOWL,
				new Ingredient[] { Ingredient.of(ExtraDelightTags.SOAKED_SOYBEANS),
						Ingredient.of(ExtraDelightTags.SOAKED_SOYBEANS), Ingredient.of(ExtraDelightTags.SOY_SAUCE),
						Ingredient.of(Items.SUGAR), Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC),
						Ingredient.of(ExtraDelightTags.COOKING_OIL) },
				"kongjang", consumer);

		// Evaporating
		EvaporatorRecipeBuilder
				.evaporate(FluidIngredient.fromFluid(Fluids.WATER, 1000), new ItemStack(SALT.get(),1),
						MiscLootTables.EVAPORATOR_WATER.location(), 10000, SALT_BLOCK.get())
				.unlockedBy("has_water", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WATER_BUCKET))
				.save(consumer, ExtraDelight.modLoc("evaporate_water"));

		// Vat
		VatRecipeBuilder.pickle(new ItemStack(GHERKINS_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.CUCUMBER)).requires(Ingredient.of(ExtraDelightTags.CUCUMBER))
				.requires(Ingredient.of(ExtraDelightTags.CUCUMBER)).requires(Ingredient.of(ExtraDelightTags.CUCUMBER))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), (int) (dayTick), false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, (int) (dayTick * 7), true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_BEETS_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_BEETROOT))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_BEETROOT))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_BEETROOT))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_BEETROOT)).requires(Ingredient.of(Items.SUGAR))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250)) // fluid
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 7, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_ONIONS_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ForgeTags.CROPS_ONION)).requires(Ingredient.of(ForgeTags.CROPS_ONION))
				.requires(Ingredient.of(ForgeTags.CROPS_ONION)).requires(Ingredient.of(ForgeTags.CROPS_ONION))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_CARROTS_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(Tags.Items.CROPS_CARROT)).requires(Ingredient.of(Tags.Items.CROPS_CARROT))
				.requires(Ingredient.of(Tags.Items.CROPS_CARROT)).requires(Ingredient.of(Tags.Items.CROPS_CARROT))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 3, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_EGGS_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.BOILED_EGG))
				.requires(Ingredient.of(ExtraDelightTags.BOILED_EGG))
				.requires(Ingredient.of(ExtraDelightTags.BOILED_EGG))
				.requires(Ingredient.of(ExtraDelightTags.BOILED_EGG)).requires(Ingredient.of(Items.SUGAR))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_FISH_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ForgeTags.RAW_FISHES))
				.requires(Ingredient.of(ForgeTags.RAW_FISHES))
				.requires(Ingredient.of(ForgeTags.RAW_FISHES))
				.requires(Ingredient.of(ForgeTags.RAW_FISHES)).requires(Ingredient.of(Items.SUGAR))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_SAUSAGE_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.SAUSAGE_COOKED))
				.requires(Ingredient.of(ExtraDelightTags.SAUSAGE_COOKED))
				.requires(Ingredient.of(ExtraDelightTags.SAUSAGE_COOKED))
				.requires(Ingredient.of(ExtraDelightTags.SAUSAGE_COOKED))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(PICKLED_GINGER_BLOCK_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.SLICED_GINGER))
				.requires(Ingredient.of(ExtraDelightTags.SLICED_GINGER))
				.requires(Ingredient.of(ExtraDelightTags.SLICED_GINGER))
				.requires(Ingredient.of(ExtraDelightTags.SLICED_GINGER))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), hourTick, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(SOAKED_SOYBEANS_ITEM.get()), new ItemStack(Items.BOWL))
				.requires(Ingredient.of(ExtraDelightTags.SOYBEAN))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 1000)) // fluid
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(SOY_SAUCE_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_SOYBEANS))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_WHEAT_SEEDS))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 3, true))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 14, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(SAUERKRAUT_ITEM.get(), 2), new ItemStack(Items.BOWL, 2))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CABBAGE))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CABBAGE))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), hourTick, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(HOT_SAUCE_ITEM.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CHILI))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_ONION))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(KIMCHI_ITEM.get(), 2), new ItemStack(Items.BOWL, 2))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CABBAGE))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CABBAGE))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_GINGER))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC))
				.requires(Ingredient.of(ExtraDelightTags.CHILI_POWDER))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), hourTick * 7, false))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, true)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(MISO_PASTE_ITEM.get(), 6), new ItemStack(Items.GLASS_BOTTLE, 6))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_SOYBEANS))
				.requires(Ingredient.of(ModItems.COOKED_RICE.get()))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_SOYBEANS))
				.requires(Ingredient.of(ModItems.COOKED_RICE.get()))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_SOYBEANS))
				.requires(Ingredient.of(ModItems.COOKED_RICE.get()))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 2, false))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 14, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(NATTO_ITEM.get()), new ItemStack(Items.BOWL))
				.requires(Ingredient.of(ExtraDelightTags.COOKED_SOYBEANS))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 3, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(FISH_SAUCE_ITEM.get()), new ItemStack(Items.GLASS_BOTTLE))
				.requires(Ingredient.of(ForgeTags.RAW_FISHES))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 14, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(NAEM_MOO_ITEM.get(), 2), new ItemStack(Items.LILY_PAD, 2))
				.requires(Ingredient.of(ExtraDelightTags.GROUND_PORK_RAW))
//				.requires(Ingredient.of(ExtraDelightTags.LEATHER_SCRAP))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC))
				.requires(Ingredient.of(ModItems.COOKED_RICE.get()))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CHILI))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 5, true))
				.save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(NAEM_MOO_ITEM.get(), 2), new ItemStack(Items.BIG_DRIPLEAF, 2))
				.requires(Ingredient.of(ExtraDelightTags.GROUND_PORK_RAW))
//				.requires(Ingredient.of(ExtraDelightTags.LEATHER_SCRAP))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_GARLIC))
				.requires(Ingredient.of(ModItems.COOKED_RICE.get()))
				.requires(Ingredient.of(ExtraDelightTags.PROCESSED_CHILI))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 250))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightTags.SALT), dayTick * 5, true))
				.save(consumer, "naem_moo_but_with_dripleaf");

		VatRecipeBuilder.pickle(new ItemStack(ExtraDelightItems.VINEGAR.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4))
				.requires(CompoundIngredient.of(Ingredient.of(ExtraDelightTags.FRUIT),
						Ingredient.of(Items.SUGAR_CANE), Ingredient.of(Items.SUGAR),
						Ingredient.of(Tags.Items.CROPS_WHEAT), Ingredient.of(ForgeTags.CROPS_RICE)))
				.requires(Ingredient.of(Items.SUGAR)).requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 1000))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 3, true))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, false)).save(consumer);

		VatRecipeBuilder.pickle(new ItemStack(ExtraDelightItems.VINEGAR.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4))
				.requires(Ingredient.of(Items.SUGAR))
				.requiresFluid(FluidIngredient.fromFluid(ExtraDelightFluids.APPLE_CIDER.FLUID.get(), 1000))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 3, true))
				.requiresStage(new StageIngredient(Ingredient.EMPTY, dayTick * 7, false))
				.save(consumer, "apple_cider_vinegar");

		VatRecipeBuilder.pickle(new ItemStack(ExtraDelightItems.YEAST.get(), 16), new ItemStack(Items.GLASS_BOTTLE, 16))
				.requires(Ingredient.of(ExtraDelightTags.FRUIT))
				.requiresFluid(FluidIngredient.fromFluid(Fluids.WATER, 1000))
				.requiresStage(new StageIngredient(Ingredient.of(ExtraDelightItems.YEAST.get()), dayTick * 7, false))
				.save(consumer);
	}

	public static void EngLoc(LanguageProvider lp) {
		lp.add("effect.extradelight.pickled", "Pickled!");
		lp.add("effect.extradelight.pickled.description", "Chance to negate poison tick.");
		lp.add(CUCUMBER_CROP.get(), "Cucumbers");
		lp.add(SOYBEAN_CROP.get(), "Soybeans");
		lp.add(WILD_CUCUMBER.get(), "Wild Cucumber");
		lp.add(WILD_SOYBEAN.get(), "Wild Soybean");
		lp.add(CUCUMBER.get(), "Cucumber");
		lp.add(SOYBEAN_POD.get(), "Soybean Pod");
		lp.add(CUCUMBER_SEED.get(), "Cucumber Seeds");
		lp.add(SOYBEANS.get(), "Soybeans");
		lp.add(SALT.get(), "Salt");
		lp.add(SALT_BLOCK.get(), "Salt Block");
		lp.add(CUCUMBER_CRATE.get(), "Cucumber Crate");
		lp.add(SOYBEAN_SACK.get(), "Soybean Sack");
		lp.add(GHERKINS_BLOCK.get(), "Jar of Pickled Cucumbers");
		lp.add(GHERKIN_ITEM.get(), "Pickled Cucumber");
		lp.add(PICKLED_BEETS_BLOCK.get(), "Jar of Pickled Sliced Beetroot");
		lp.add(PICKLED_BEET_ITEM.get(), "Pickled Sliced Beetroot");
		lp.add(PICKLED_ONIONS_BLOCK.get(), "Jar of Pickled Onions");
		lp.add(PICKLED_ONION_ITEM.get(), "Pickled Onion");
		lp.add(PICKLED_CARROTS_BLOCK.get(), "Jar of Pickled Carrot");
		lp.add(PICKLED_CARROT_ITEM.get(), "Pickled Carrot");
		lp.add(PICKLED_EGGS_BLOCK.get(), "Jar of Pickled Eggs");
		lp.add(PICKLED_EGG_ITEM.get(), "Pickled Egg");
		lp.add(PICKLED_FISH_BLOCK.get(), "Jar of Pickled Fish");
		lp.add(PICKLED_FISH_ITEM.get(), "Pickled Fish");
		lp.add(PICKLED_SAUSAGE_BLOCK.get(), "Jar of Pickled Sausage");
		lp.add(PICKLED_SAUSAGE_ITEM.get(), "Pickled Sausage");
		lp.add(PICKLED_GINGER_BLOCK.get(), "Jar of Pickled Ginger");
		lp.add(PICKLE_JUICE.get(), "Pickle Juice");
		lp.add(PICKLE_JUICE_FLUID_BUCKET.get(), "Pickle Juice Bucket");
		lp.add("fluid_type.extradelight.pickle_juice_fluid", "Pickle Juice");
		lp.add("block.extradelight.pickle_juice_fluid_block", "Pickle Juice");
		lp.add(SOY_SAUCE_ITEM.get(), "Soy Sauce");
		lp.add(SAUERKRAUT_ITEM.get(), "Sauerkraut");
		lp.add(KIMCHI_ITEM.get(), "Kimchi");
		lp.add(HOT_SAUCE_ITEM.get(), "Hot Sauce");
		lp.add(MISO_PASTE_ITEM.get(), "Miso Paste");
		lp.add(NATTO_ITEM.get(), "Natto");
		lp.add(FISH_SAUCE_ITEM.get(), "Fish Sauce");
		lp.add(SALAMI_MIX.get(), "Salami Mix");
		lp.add(UNRIPE_SALAMI_ITEM.get(), "Unripe Salami");
		lp.add(SALAMI_ITEM.get(), "Salami");
		lp.add(SOAKED_SOYBEANS_ITEM.get(), "Soaked Soybeans");
		lp.add(MASHED_SOYBEANS_ITEM.get(), "Mashed Soybeans");
		lp.add(COOKED_SOYBEANS_ITEM.get(), "Cooked Soybeans");
		lp.add(SOY_MILK.get(), "Soy Milk");
		lp.add(NAEM_MOO_ITEM.get(), "Naem Moo");
		lp.add(SLICED_BEETROOT_ITEM.get(), "Sliced Beetroot");
		lp.add(SHREDDED_CABBAGE_ITEM.get(), "Shredded Cabbage");
		lp.add(SLICED_CUCUMBER_ITEM.get(), "Sliced Cucumber");
		lp.add(SLICED_GHERKIN_ITEM.get(), "Sliced Pickled Cucumber");
		lp.add(GAZPACHO.get(), "Gazpacho");
		lp.add(EDAMAME.get(), "Edamame");
		lp.add(BEEF_BULGOGI.get(), "Beef Bulgogi");
		lp.add(HONEY_CHILI_CHICKEN.get(), "Honey Chili Chicken");
		lp.add(CARAMEL_CHICKEN.get(), "Caramel Chicken");
		lp.add(SOY_GLAZED_SALMON_BLOCK.get(), "Soy-Glazed Salmon");
		lp.add(SOY_GLAZED_SALMON_ITEM.get(), "Plate of Soy-Glazed Salmon");
		lp.add(STEAK_PICKLED_ONION_PIE.get(), "Steak and Pickled Onion Pie");
		lp.add(STEAK_PICKLED_ONION_PIE_SLICE.get(), "Slice of Steak and Pickled Onion Pie");
		lp.add(KIWIBURGER.get(), "Kiwiburger");
		lp.add(SHIRAZI_SALAD.get(), "Shirazi Salad");
		lp.add(CUCUMBER_SALAD.get(), "Cucumber Salad");
		lp.add(MISO_SOUP.get(), "Miso Soup");
		lp.add(NATTO_AND_RICE.get(), "Natto and Rice");
		lp.add(SAUERKRAUT_SOUP.get(), "Sauerkraut Soup");
		lp.add(SAUERKRAUT_AND_SAUSAGE.get(), "Sauerkraut and Sausage");
		lp.add(YEAST_SPREAD.get(), "Yeast Spread");
		lp.add(CHEESYMITE_SCROLL_BLOCK.get(), "Cheesymite Scrolls");
		lp.add(CHEESYMITE_SCROLL_SERVING.get(), "Cheesymite Scroll");
		lp.add(MORKOVCHA.get(), "Morkovcha");
		lp.add(ZUPA_OGORKOWA.get(), "Zupa Ogórkowa");
		lp.add(KIMCHI_FRIED_RICE.get(), "Kimchi Fried Rice");
		lp.add(KONGJANG.get(), "Kongjang");
		lp.add(CHEESEBURGER_PICKLE.get(), "Cheeseburger Pickle Popper");
		lp.add(HOT_WINGS.get(), "Hot Wings");
		lp.add("block.extradelight.jar_display_block", "Jar Display");
		lp.add(COOKED_WHEAT_SEEDS.get(), "Roasted Wheat Berries");
	}
}
