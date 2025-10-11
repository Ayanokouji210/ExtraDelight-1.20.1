package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.mortar.recipes.MortarRecipe;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MortarRecipeBuilder implements RecipeBuilder {
	private final ItemStack result;
	private final Ingredient ingredient;
	private final int grinds;
	private final FluidStack fluid;
	@Nullable
	private String group;
	private final Advancement.Builder advancementBuilder;
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	private MortarRecipeBuilder(ItemStack pResult, Ingredient pIngredient, FluidStack fluidOut, int pGrind) {
		this.result = pResult;
		this.ingredient = pIngredient;
		this.grinds = pGrind;
		this.fluid = fluidOut;
		this.advancementBuilder=Advancement.Builder.advancement();
	}

	public static MortarRecipeBuilder grind(Ingredient pIngredient, ItemStack pResult, FluidStack fluidOut,
			int grinds) {
		return new MortarRecipeBuilder(pResult, pIngredient, fluidOut, grinds);
	}

	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		this.criteria.put(pCriterionName,new Criterion(pCriterionTrigger));
		return this;
	}

	public MortarRecipeBuilder group(@Nullable String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	public Item getResult() {
		if (result != null)
			return this.result.getItem();
		return Items.AIR;
	}

	@Override
	public void save(Consumer<FinishedRecipe> output,ResourceLocation id) {
		ResourceLocation recipeId = id.withPrefix("mortar/");
		Advancement.Builder advancementBuilder = this.advancementBuilder
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);

		//MortarRecipe recipe = new MortarRecipe("", this.ingredient, this.result, this.fluid, this.grinds);
		output.accept(new Result(id,this.group==null?"":this.group,
				this.ingredient,this.result,
				this.fluid,this.grinds,this.advancementBuilder,
				this.advancementBuilder.build(id.withPrefix("recipes/mortar/")).getId()));
				//recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/mortar/")));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final ItemStack result;
		private final FluidStack fluid;
		private final int grinds;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;


		public Result(ResourceLocation id, String group, Ingredient ingredient,
					  ItemStack result, FluidStack fluid, int grinds,
					  Advancement.Builder advancement, ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.ingredient = ingredient;
			this.result = result;
			this.fluid = fluid;
			this.grinds = grinds;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject pJson) {

			if(this.group != null && !this.group.isEmpty()) {
				pJson.addProperty("group", group);
			}
			pJson.add("fluidOut", StackUtil.FluidStacktoJson(this.fluid));
			pJson.addProperty("grinds", grinds);
			pJson.add("ingredient",this.ingredient.toJson());

			pJson.add("result", StackUtil.ItemStacktoJson(this.result));
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.MORTAR_SERIALIZER.get();
		}

		@Override
		public @org.jetbrains.annotations.Nullable JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Override
		public @org.jetbrains.annotations.Nullable ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}

}