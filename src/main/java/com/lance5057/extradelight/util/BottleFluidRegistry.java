package com.lance5057.extradelight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightFluids;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.data.recipebuilders.BottleFluidRegistryRecipeBuilder;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.modules.Fermentation;


import com.lance5057.extradelight.modules.SummerCitrus;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;

public class BottleFluidRegistry {
	private static int bottleMB = 250;

	public static List<BottleFluid> registry = new ArrayList<BottleFluid>();

	public static void register(Ingredient bottle, FluidIngredient fluid) {
		registry.add(new BottleFluid(bottle, fluid));
	}

	public static ItemStack getBottleFromFluid(FluidStack f) {
		if (f.getAmount() >= bottleMB) {
			Optional<BottleFluid> b = registry.stream().filter(bf -> bf.fluid.test(f)).findFirst();
			if (b.isPresent()) {
				return b.get().bottle.getItems()[0].copy();
			}
		}
		return ItemStack.EMPTY;
	}

    public static ItemStack getBottleFromFluidWithoutSize(Fluid f) {
        Optional<BottleFluid> b = registry.stream().filter(bf -> bf.fluid.test(new FluidStack(f, 1000))).findFirst();
        if (b.isPresent()) {
            return b.get().bottle.getItems()[0].copy();
        }
        return ItemStack.EMPTY;
    }

    public static FluidStack getFluidFromBottle(ItemStack i) {
		Optional<BottleFluid> b = registry.stream().filter(bf -> bf.bottle.test(i)).findFirst();
		if (b.isPresent()) {
			return b.get().fluid.getMatchingFluidStacks().get(0);
		}
		return FluidStack.EMPTY;
	}

	public static void createRecipesForJEI(Consumer<FinishedRecipe> consumer) {
		registry.forEach(bf -> {
            new BottleFluidRegistryRecipeBuilder(bf.bottle, bf.fluid).save(consumer,
                    bf.fluid.getMatchingFluidStacks().get(0).getTranslationKey());
		});
	}

	public static class BottleFluid {
		public Ingredient bottle;
		public FluidIngredient fluid;

		public BottleFluid(Ingredient b, FluidIngredient f) {
			this.bottle = b;
			this.fluid = f;
		}

	}

	static {
				
		register(Ingredient.of(ModItems.APPLE_CIDER.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.APPLE_CIDER.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.BBQ_SAUCE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.BBQ.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.BLOOD_CHOCOLATE_SYRUP_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.BLOOD_CHOCOLATE_SYRUP.FLUID.get(), bottleMB));
		register(Ingredient.of(ModItems.BONE_BROTH.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.BROTH.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.CACTUS_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.CACTUS_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.CARAMEL_SAUCE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.CARAMEL_SAUCE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.COCOA_BUTTER_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.COCOA_BUTTER.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.COFFEE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.COFFEE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.COOKING_OIL.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.OIL.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.DARK_CHOCOLATE_SYRUP_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.DARK_CHOCOLATE_SYRUP.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.EGG_MIX.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.EGG_MIX.FLUID.get(), bottleMB));
        register(Ingredient.of(SummerCitrus.EGG_WHITE.get()),
                FluidIngredient.fromFluid(ExtraDelightFluids.EGG_WHITE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.GLOW_BERRY_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.GLOW_BERRY_JUICE.FLUID.get(), bottleMB));
        register(Ingredient.of(SummerCitrus.GRAPEFRUIT_JUICE.get()),
                FluidIngredient.fromFluid(ExtraDelightFluids.GRAPEFRUIT_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.GRAVY.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.GRAVY.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.HAZELNUT_SPREAD_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.COCOA_NUT_BUTTER_SPREAD.FLUID.get(), bottleMB));
		register(Ingredient.of(ModItems.HOT_COCOA.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.HOT_COCOA.FLUID.get(), bottleMB));
		register(PartialNBTIngredient.of(new DynamicItemComponent(List.of("sweet_berries")).serializeNBT(),ExtraDelightItems.DYNAMIC_JAM.get()),
//                DataComponentIngredient.of(false, ExtraDelightComponents.DYNAMIC_FOOD,
//                        new DynamicItemComponent(List.of("sweet_berries")), ExtraDelightItems.DYNAMIC_JAM.get()),
                FluidIngredient.fromFluid(ExtraDelightFluids.JAM.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.KETCHUP.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.KETCHUP.FLUID.get(), bottleMB));
        register(Ingredient.of(SummerCitrus.LEMON_JUICE.get()),
                FluidIngredient.fromFluid(ExtraDelightFluids.LEMON_JUICE.FLUID.get(), bottleMB));
        register(Ingredient.of(SummerCitrus.LIME_JUICE.get()),
                FluidIngredient.fromFluid(ExtraDelightFluids.LIME_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.MARSHMALLOW_FLUFF_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.MARSHMALLOW_FLUFF.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.MAYO.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.MAYO.FLUID.get(), bottleMB));
		register(Ingredient.of(ModItems.MELON_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.MELON_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ModItems.MILK_BOTTLE.get()),
				FluidIngredient.fromFluid(ForgeMod.MILK.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.MILK_CHOCOLATE_SYRUP_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.MILK_CHOCOLATE_SYRUP.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.MILKSHAKE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.MILKSHAKE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.PEANUT_BUTTER_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.NUT_BUTTER.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.SWEET_BERRY_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.SWEET_BERRY_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.TEA.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.TEA.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.TOMATO_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.TOMATO_JUICE.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.VINEGAR.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.VINEGAR.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.WHIPPED_CREAM.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), bottleMB));
		register(Ingredient.of(ExtraDelightItems.WHITE_CHOCOLATE_SYRUP_BOTTLE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.WHITE_CHOCOLATE_SYRUP.FLUID.get(), bottleMB));
		register(Ingredient.of(Fermentation.PICKLE_JUICE.get()),
				FluidIngredient.fromFluid(ExtraDelightFluids.PICKLE_JUICE.FLUID.get(), bottleMB));

		// If we just use Items.POTION we get an item called Uncraftable Potion instead of Water Bottle
		register(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION),Potions.WATER)),FluidIngredient.fromFluid(Fluids.WATER, bottleMB));
				//.createItemStack(Items.POTION, Potions.WATER)),

	}
}
