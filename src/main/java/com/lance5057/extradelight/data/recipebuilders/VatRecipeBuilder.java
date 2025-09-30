package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.vat.recipes.VatRecipe;
import com.lance5057.extradelight.workstations.vat.recipes.VatRecipe.StageIngredient;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VatRecipeBuilder implements RecipeBuilder {
	final ItemStack containerItem;

	@Nullable
	private String group="";
	final ItemStack result;
	final NonNullList<Ingredient> ingredients = NonNullList.create();
	final NonNullList<StageIngredient> stageIngredients = NonNullList.create();
	FluidStack fluid;

	protected int stages = 0;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();

	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	public VatRecipeBuilder(ItemStack pResult, ItemStack containerItem) {
		this.containerItem = containerItem;
		this.result = pResult;
	}

	public static VatRecipeBuilder pickle(ItemStack pResult, ItemStack usedItem) {
		return new VatRecipeBuilder(pResult, usedItem);
	}

	public VatRecipeBuilder requiresFluid(FluidStack stack) {
		this.fluid = stack;
		return this;
	}

	public VatRecipeBuilder requiresFluid(FluidIngredient ingredient) {
		this.fluid=ingredient.getMatchingFluidStacks().get(0);
		return this;
	}

	public VatRecipeBuilder requires(Ingredient pIngredient) {
		return this.requires(pIngredient, 1);
	}

	public VatRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.ingredients.add(pIngredient);
		}

		return this;
	}

	public VatRecipeBuilder requires(TagKey<Item> pTag) {
		return this.requires(Ingredient.of(pTag));
	}

	public VatRecipeBuilder requires(ItemLike pItem) {
		return this.requires(pItem, 1);
	}

	public VatRecipeBuilder requires(ItemLike pItem, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.requires(Ingredient.of(pItem));
		}

		return this;
	}

	public VatRecipeBuilder requiresStage(StageIngredient pIngredient) {
		this.stageIngredients.add(pIngredient);
		stages++;

		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		this.criteria.put(pCriterionName,new Criterion(pCriterionTrigger));
		return this;
	}

//	@Override
//	public RecipeBuilder unlockedBy(String criterionName, Criterion criterionTrigger) {
//		this.criteria.put(criterionName, criterionTrigger);
//		return this;
//	}

	@Override
	public VatRecipeBuilder group(String groupName) {
		this.group = groupName;
		return this;
	}

	@Override
	public Item getResult() {
		return result.getItem();
	}

	@Override
	public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
		if (this.ingredients.size() > 6)
			throw new IllegalStateException("Vat Recipe " + id + " has more than 6 ingredients!");

		ResourceLocation recipeId = id.withPrefix("vat/");
		Advancement.Builder advancementBuilder = this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);
//		VatRecipe recipe = new VatRecipe("", this.ingredients, stageIngredients, this.fluid, this.result, this.stages,
//				this.containerItem);
		recipeOutput.accept(new Result(id,group,ingredients,
				stageIngredients,fluid,result,stages,containerItem,
				advancement,advancement.build(id.withPrefix("recipes/vat/")).getId()));
				//recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/vat/")));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final NonNullList<Ingredient> ingredients;
		private final NonNullList<StageIngredient> stageIngredients;
		private final FluidStack fluid;
		private final ItemStack result;
		private final int stages;
		private final ItemStack containerItem;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation id, String group, NonNullList<Ingredient> ingredients,
					  NonNullList<StageIngredient> stageIngredients, FluidStack fluid,
					  ItemStack result, int stages, ItemStack containerItem,
					  Advancement.Builder advancement, ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.ingredients = ingredients;
			this.stageIngredients = stageIngredients;
			this.fluid = fluid;
			this.result = result;
			this.stages = stages;
			this.containerItem = containerItem;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			JsonArray ingredientsArray = new JsonArray();
			for (Ingredient ingredient : this.ingredients) {
				ingredientsArray.add(ingredient.toJson());
			}
			json.add("ingredients", ingredientsArray);

			JsonArray stageIngredientsArray = new JsonArray();
			for (StageIngredient stageIngredient : this.stageIngredients) {
				stageIngredientsArray.add(stageIngredient.toJson());
			}
			json.add("stage_ingredients", stageIngredientsArray);

			// 这里需要根据你的 FluidStack 序列化方式实现
			// if (!this.fluid.isEmpty()) {
			//     json.add("fluids", FluidStackUtil.toJson(this.fluid));
			// }

//			JsonObject resultJson = new JsonObject();
//			resultJson.addProperty("item", this.result.getItem().toString());
//			if (this.result.getCount() > 1) {
//				resultJson.addProperty("count", this.result.getCount());
//			}
			json.add("result", StackUtil.ItemStacktoJson(this.result));

			json.addProperty("stages", this.stages);

//			if (!this.containerItem.isEmpty()) {
//				JsonObject containerJson = new JsonObject();
//				containerJson.addProperty("item", this.containerItem.getItem().toString());
//				if (this.containerItem.getCount() > 1) {
//					containerJson.addProperty("count", this.containerItem.getCount());
//				}
//			}
			json.add("usedItem", StackUtil.ItemStacktoJson(this.containerItem));
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.VAT_SERIALIZER.get();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}

}
