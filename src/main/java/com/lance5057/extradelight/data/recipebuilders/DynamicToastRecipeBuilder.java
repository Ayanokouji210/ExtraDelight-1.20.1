package com.lance5057.extradelight.data.recipebuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.recipe.DynamicToastRecipe;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DynamicToastRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStack result;
	private final List<String> rows = Lists.newArrayList();
	private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;
	private final RecipeSerializer<?> serializer;


	public DynamicToastRecipeBuilder(RecipeCategory category, ItemStack result,RecipeSerializer<?> serializer) {
		this.category = category;
		this.result = result;
		this.serializer = serializer;
	}

	public static DynamicToastRecipeBuilder shaped(RecipeCategory category, ItemStack result, RecipeSerializer<?> serializer) {
		return new DynamicToastRecipeBuilder(category, result,serializer);
	}

	public DynamicToastRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return define(symbol, Ingredient.of(tag));
	}

	public DynamicToastRecipeBuilder define(Character symbol, ItemLike item) {
		return define(symbol, Ingredient.of(item));
	}

	public DynamicToastRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol))
			throw new IllegalArgumentException("Symbol '" + symbol + "' already defined!");
		if (symbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' is reserved!");
		this.key.put(symbol, ingredient);
		return this;
	}

	public DynamicToastRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length())
			throw new IllegalArgumentException("Pattern must be same width on every line!");
		this.rows.add(pattern);
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
		ensureValid(id);
		Advancement.Builder adv = Advancement.Builder.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(RequirementsStrategy.OR);
		this.criteria.forEach(adv::addCriterion);

		consumer.accept(new Result(id, this.group == null ? "" : this.group,
				this.category.getFolderName(),
				this.rows, this.key, this.result,
				this.serializer, adv,
				id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation id) {
		if (this.rows.isEmpty())
			throw new IllegalStateException("No pattern for recipe " + id);
		if (this.criteria.isEmpty())
			throw new IllegalStateException("No way of obtaining recipe " + id);
	}

	/* ---------- FinishedRecipe ---------- */
	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final String category;
		private final List<String> pattern;
		private final Map<Character, Ingredient> key;
		private final ItemStack result;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;
		private final RecipeSerializer<?> serializer;

		public Result(ResourceLocation id, String group, String category,
					  List<String> pattern, Map<Character, Ingredient> key,
					  ItemStack result,RecipeSerializer<?> serializer, Advancement.Builder advancement,
					  ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.category = category;
			this.pattern = pattern;
			this.key = key;
			this.result = result;
			this.advancement = advancement;
			this.advancementId = advancementId;
			this.serializer=serializer;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) json.addProperty("group", this.group);

			JsonArray arr = new JsonArray();
			this.pattern.forEach(arr::add);
			json.add("pattern", arr);

			JsonObject keyObj = new JsonObject();
			this.key.forEach((k, v) -> keyObj.add(String.valueOf(k), v.toJson()));
			json.add("key", keyObj);

			JsonObject res = new JsonObject();
			Item item = this.result.getItem();
			if (item == null)
				throw new IllegalStateException("ItemStack result is null! Recipe ID: " + this.id);
			ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
			if (key == null)
				throw new IllegalStateException("Item " + item + " is not registered! Recipe ID: " + this.id);
			res.addProperty("item", key.toString());
			if (this.result.getCount() > 1) res.addProperty("count", this.result.getCount());
			json.add("result", res);
		}

		@Override public ResourceLocation getId() { return this.id; }
		@Override public RecipeSerializer<?> getType() { return this.serializer; }
		@Override @Nullable public JsonObject serializeAdvancement() { return this.advancement.serializeToJson(); }
		@Override @Nullable public ResourceLocation getAdvancementId() { return this.advancementId; }
	}
}