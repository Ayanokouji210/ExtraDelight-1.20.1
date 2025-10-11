package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.recipe.ToolOnBlockRecipe;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolOnBlockBuilder implements RecipeBuilder {
	private final BlockItem in;
	private final Ingredient tool;
	private final BlockItem out;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	@Nullable
	private String group="";
//	private final ToolOnBlockRecipe.Serializer serializer;
//	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	private ToolOnBlockBuilder(BlockItem pResult, BlockItem block, Ingredient pIngredient ) {
		this.in = pResult;
		this.tool = pIngredient;
		this.out = block;
//		this.serializer = pSerializer;
	}

	public static ToolOnBlockBuilder make(Item pResult, Ingredient pIngredient, Item block) {
		if (pResult instanceof BlockItem b1) {
			if (block instanceof BlockItem b2) {
				return new ToolOnBlockBuilder(b1, b2, pIngredient);
			} else {
				ExtraDelight.logger.error("ToolOnBlockRecipe Invalid! " + block.toString() + " is not a BlockItem!");
				return null;
			}
		} else {
			ExtraDelight.logger.error("ToolOnBlockRecipe Invalid! " + pResult.toString() + " is not a BlockItem!");
			return null;
		}
	}

	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
		return this;
	}


	public ToolOnBlockBuilder group(@Nullable String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	public Item getResult() {
		return this.in;
	}


	@Override
	public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
//		ResourceLocation saveID = ResourceLocation.fromNamespaceAndPath(pRecipeId.getNamespace(),
//				"tool_on_block/" + pRecipeId.getPath());
//		this.ensureValid(pRecipeId);
//		this.advancement.parent(ResourceLocation.fromNamespaceAndPath("recipes/root"))
//				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(saveID))
//				.rewards(AdvancementRewards.Builder.recipe(saveID)).requirements(RequirementsStrategy.OR);
//		pFinishedRecipeConsumer.accept(new ToolOnBlockBuilder.Result(saveID, this.group == null ? "" : this.group,
//				this.tool, this.in, this.out, this.advancement,
//				ResourceLocation.fromNamespaceAndPath(pRecipeId.getNamespace(),
//						"recipes/" + this.in.getItemCategory().getRecipeFolderName() + "/" + pRecipeId.getPath()),
//				this.serializer));

		ResourceLocation recipeId = id.withPrefix("toolonblock/");
		Advancement.Builder advancementBuilder = this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
//		this.criteria.forEach(advancementBuilder::addCriterion);
		output.accept(new Result(recipeId,this.group,this.tool,this.in,this.out,advancement,
				advancement.build(recipeId.withPrefix("recipes/toolonblock/")).getId()));
//		public OvenRecipe(String group, @Nullable OvenRecipeBookTab tab, NonNullList<Ingredient> inputItems,
//				ItemStack output, ItemStack container, float experience, int cookTime) {
//		ToolOnBlockRecipe recipe = new ToolOnBlockRecipe(this.in, this.tool, this.out);
//		output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/toolonblock/")));


	}

	/**
	 * Makes sure that this obtainable.
	 */
	private void ensureValid(ResourceLocation pId) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + pId);
		}
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient tool;
		private final BlockItem in;
		private final BlockItem out;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation pId, String pGroup, Ingredient pIngredient, BlockItem in, BlockItem out,
				Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
			this.id = pId;
			this.group = pGroup;
			this.tool = pIngredient;
			this.in = in;
			this.out = out;
			this.advancement = pAdvancement;
			this.advancementId = pAdvancementId;
		}

		public void serializeRecipeData(JsonObject pJson) {
			if (!this.group.isEmpty()) {
				pJson.addProperty("group", this.group);
			}

			pJson.add("ingredient", this.tool.toJson());

			pJson.addProperty("blockIn", ForgeRegistries.ITEMS.getKey(in).toString());
			pJson.addProperty("blockOut", ForgeRegistries.ITEMS.getKey(out).toString());
		}

		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.TOOL_ON_BLOCK_SERIALIZER.get();
		}


		/**
		 * Gets the ID for the recipe.
		 */
		public ResourceLocation getId() {
			return this.id;
		}

		/**
		 * Gets the JSON for the advancement that unlocks this recipe. Null if there is
		 * no advancement.
		 */
		@Nullable
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		/**
		 * Gets the ID for the advancement associated with this recipe. Should not be
		 * null if {@link #} is non-null.
		 */
		@Nullable
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}
}
