package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.recipe.BottleFluidRegistryRecipe;
import com.lance5057.extradelight.util.StackUtil;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.function.Consumer;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class BottleFluidRegistryRecipeBuilder implements RecipeBuilder {

	public Ingredient bottle;
	public FluidIngredient fluid;
	private Advancement.Builder advancement=Advancement.Builder.recipeAdvancement();
	private String group = "";
	private BottleFluidRegistryRecipe.Serializer serializer;

	public BottleFluidRegistryRecipeBuilder(Ingredient bottle, FluidIngredient fluid) {
		this.bottle = bottle;
		this.fluid = fluid;
		this.serializer= new BottleFluidRegistryRecipe.Serializer();
	}



//	@Override
//	public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
//		return this;
//	}

	@Override
	public @Nonnull RecipeBuilder unlockedBy(@Nonnull String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
		return this;
	}

	@Override
	public RecipeBuilder group(String groupName) {
		this.group = groupName;
		return this;
	}

	@Override
	public Item getResult() {
		return Items.AIR;
	}

//	@Override
//	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
//		ResourceLocation recipeId = id.withPrefix("bottlefluid/");
////		Advancement.Builder advancementBuilder = recipeOutput.advancement()
////				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
////				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
//
//		BottleFluidRegistryRecipe recipe = new BottleFluidRegistryRecipe("", this.bottle, this.fluid);
//		recipeOutput.accept(recipeId, recipe, null);
//	}

	@Override
	public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
		this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
				.rewards(AdvancementRewards.Builder.recipe(pRecipeId).addRecipe(pRecipeId))
				.requirements(RequirementsStrategy.OR);
		//.parent(ROOT_RECIPE_ADVANCEMENT)

		BottleFluidRegistryRecipe recipe =new BottleFluidRegistryRecipe(pRecipeId,this.group,this.bottle,this.fluid.getMatchingFluidStacks().get(0));
		Advancement build = advancement.build(pRecipeId.withPrefix("bottlefluid/"));
		pFinishedRecipeConsumer.accept(new BottleFluidRegistryRecipeBuilder.Result(pRecipeId,group,recipe.bottle,
				this.fluid.getMatchingFluidStacks().get(0),this.advancement,build.getId(),this.serializer));
//				pRecipeId, recipe, this.advancement.build(pRecipeId.withPrefix("recipes/bottlefluid/")));

	}

	public static class Result implements FinishedRecipe {
		 private final ResourceLocation id;
		 private final String group;
		 private final Ingredient bottle;
		 private final FluidStack fluid;
		 private final Advancement.Builder advancement;
		 private final ResourceLocation advancementId;
		 private final BottleFluidRegistryRecipe.Serializer serializer;

		public Result(ResourceLocation id, String group, Ingredient bottle,
					  FluidStack fluid, Advancement.Builder advancement,
					  ResourceLocation advancementId, BottleFluidRegistryRecipe.Serializer serializer) {
			this.id = id;
			this.group = group;
			this.bottle = bottle;
			this.fluid = fluid;
			this.advancement = advancement;
			this.advancementId = advancementId;
			this.serializer = serializer;
		}

		@Override
		public void serializeRecipeData(JsonObject jsonObject) {
			if(!this.group.isEmpty()) {
				jsonObject.addProperty("group", this.group);
			}
			jsonObject.add("bottle", this.bottle.toJson());
			jsonObject.add("fluid", StackUtil.FluidStacktoJson(this.fluid));
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.BOTTLE_FLUID_SERIALIZER.get();
		}

		@Override
		public @Nullable JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Override
		public @Nullable ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}

//		ResourceLocation recipeId = pRecipeId.withPrefix("bottlefluid/");
//		BottleFluidRegistryRecipe recipe = new BottleFluidRegistryRecipe(this.group, this.bottle, this.fluid);
//		FinishedRecipe FR = new FinishedRecipe() {
//			@Override
//			public void serializeRecipeData(@Nonnull JsonObject pJson) {
//
//			}
//
//			@Override
//			public @Nonnull ResourceLocation getId() {
//				return recipeId;
//			}
//
//			@Override
//			public @Nonnull RecipeSerializer<?> getType() {
//				return recipe;
//			}
//
//			@Override
//			public @Nullable JsonObject serializeAdvancement() {
//				return null;
//			}
//
//			@Override
//			public @Nullable ResourceLocation getAdvancementId() {
//				return null;
//			}
//		};
//		pFinishedRecipeConsumer.accept(FR);
		//ResourceLocation location, Recipe<?> recipe, @Nullable AdvancementHolder advancement
		//recipeId, recipe, null
}
