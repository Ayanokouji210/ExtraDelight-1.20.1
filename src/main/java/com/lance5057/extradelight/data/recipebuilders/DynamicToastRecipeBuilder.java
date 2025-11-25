package com.lance5057.extradelight.data.recipebuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.recipe.DynamicToastRecipe;

import com.lance5057.extradelight.util.StackUtil;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DynamicToastRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStack result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;
	private final String graphic;


	public DynamicToastRecipeBuilder(RecipeCategory category, ItemStack result, String graphic) {
		this.category = category;
		this.result = result;
        this.graphic = graphic;
	}

	public static DynamicToastRecipeBuilder shapeless(RecipeCategory category, ItemStack result, String graphic) {
		return new DynamicToastRecipeBuilder(category, result,graphic);
	}

    /**
     * Adds an ingredient that can be any item in the given tag.
     */
    public DynamicToastRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(tag);
    }

    /**
     * Adds an ingredient of the given item.
     */
    public DynamicToastRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public DynamicToastRecipeBuilder requires(ItemLike item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    /**
     * Adds an ingredient.
     */
    public DynamicToastRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    /**
     * Adds an ingredient multiple times.
     */
    public DynamicToastRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

	public DynamicToastRecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
		this.criteria.put(name, new Criterion(trigger));
		return this;
	}

	public DynamicToastRecipeBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

	@Override
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
		Advancement.Builder adv = Advancement.Builder.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(RequirementsStrategy.OR);
		this.criteria.forEach(adv::addCriterion);

		consumer.accept(new Result(id, this.group == null ? "" : this.group,
                this.category,this.result,this.ingredients,this.graphic,adv,adv.build(
				id.withPrefix("recipes/" + this.category.getFolderName() + "/")).getId()));
	}

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final RecipeCategory category;
        private final NonNullList<Ingredient> ingredients;
		private final ItemStack result;
        private final String graphic;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;


		public Result(ResourceLocation id, String group, RecipeCategory category,
					  ItemStack result,NonNullList<Ingredient> ingredients,
                      String graphic, Advancement.Builder advancement,
					  ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.category = category;
			this.result = result;
            this.ingredients = ingredients;
            this.graphic = graphic;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) json.addProperty("group", this.group);
            json.addProperty("category", CraftingRecipeBuilder.determineBookCategory(this.category).getSerializedName());
            json.addProperty("graphic",this.graphic);
            JsonArray ingredientsJson = new JsonArray();
            this.ingredients.forEach(ingredient -> {
                ingredientsJson.add(ingredient.toJson());
            });
            json.add("ingredients",ingredientsJson);
            json.add("result",StackUtil.ItemStacktoJson(result));
		}

		@Override public ResourceLocation getId() { return this.id; }
		@Override public RecipeSerializer<?> getType() { return ExtraDelightRecipes.DYNAMIC_TOAST_SERIALIZER.get(); }
		@Override @Nullable public JsonObject serializeAdvancement() { return this.advancement.serializeToJson(); }
		@Override @Nullable public ResourceLocation getAdvancementId() { return this.advancementId; }
	}
}