package com.lance5057.extradelight.data.recipebuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.recipe.DynamicToastRecipe;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DynamicToastRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStack result;
	private final ItemStack resultStack; // Neo: add stack result support
	private final List<String> rows = Lists.newArrayList();
	private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;
	private boolean showNotification = true;

	private final DynamicToastRecipe.Serializer serializer;

	public DynamicToastRecipeBuilder(RecipeCategory p_249996_, ItemStack result) {
		this.category = p_249996_;
		this.result = result;
		this.resultStack = result;
		this.serializer = new DynamicToastRecipe.Serializer();
	}

	public static DynamicToastRecipeBuilder shaped(RecipeCategory p_251325_, ItemStack result) {
		return new DynamicToastRecipeBuilder(p_251325_, result);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public DynamicToastRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(tag));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public DynamicToastRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public DynamicToastRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol)) {
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
		} else if (symbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(symbol, ingredient);
			return this;
		}
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	public DynamicToastRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pattern);
			return this;
		}
	}



	public @NotNull DynamicToastRecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance criterion) {
		this.criteria.put(name, new Criterion(criterion));
		return this;
	}

	public @NotNull DynamicToastRecipeBuilder group(@Nullable String groupName) {
		this.group = groupName;
		return this;
	}

	public DynamicToastRecipeBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result.getItem();
	}

	@Override
	public void save(Consumer<FinishedRecipe> recipeOutput, @NotNull ResourceLocation id) {
		this.ensureValid(id);
//		ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
		Advancement.Builder advancement$builder = Advancement.Builder.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);

		recipeOutput.accept(new Result(id,this.group==null?"":this.group,this.category,
				this.rows,this.key,this.result,advancement$builder,
				id.withPrefix("recipes/"+this.category.getFolderName()+"/"),this.serializer));

//		DynamicToastRecipe shapedrecipe = new DynamicToastRecipe(Objects.requireNonNullElse(this.group, ""),
//				RecipeBuilder.determineBookCategory(this.category), shapedrecipepattern, this.resultStack);
//		recipeOutput.accept(id, shapedrecipe,
//				advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation location) {
//		if (this.criteria.isEmpty()) {
//			throw new IllegalStateException("No way of obtaining recipe " + loaction);
//		} else {
//			return ShapedRecipePattern.of(this.key, this.rows);
//		}

		if (this.rows.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + location + "!");
		} else {
			if (this.criteria.isEmpty()) {
				throw new IllegalStateException("No way of obtaining recipe " + location);
			}
		}
	}

	public class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final RecipeCategory category;
		private final List<String> pattern;
		private final Map<Character, Ingredient> key;
		private final ItemStack result;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;
		private final DynamicToastRecipe.Serializer serializer;

		public Result(ResourceLocation id, String group, RecipeCategory category,
					  List<String> pattern, Map<Character, Ingredient> key,
					  ItemStack result, Advancement.Builder advancement,
					  ResourceLocation advancementId, DynamicToastRecipe.Serializer serializer) {
			this.id = id;
			this.group = group;
			this.category = category;
			this.pattern = pattern;
			this.key = key;
			this.result = result;
			this.advancement = advancement;
			this.advancementId = advancementId;
			this.serializer =serializer;
		}

		@Override
		public void serializeRecipeData(@NotNull JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			JsonArray patternArray = new JsonArray();
			for (String s : this.pattern) {
				patternArray.add(s);
			}
			json.add("pattern", patternArray);

			JsonObject keyObject = new JsonObject();
			for (Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
				keyObject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
			}
			json.add("key", keyObject);

			JsonObject resultObject = new JsonObject();
			//resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
			//resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result.getItem()).toString());

			Item item = this.result.getItem();
			if (item == null) {
				throw new IllegalStateException(
						"ItemStack result is null! Recipe ID: " + this.id);
			}
			ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
			if (key == null) {
				throw new IllegalStateException(
						"Item " + item + " (class=" + item.getClass() + ") is not registered! Recipe ID: " + this.id);
			}
			resultObject.addProperty("item", key.toString());

			if (this.result.getCount() > 1) {
				resultObject.addProperty("count", this.result.getCount());
			}
			json.add("result", resultObject);
		}


		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return this.serializer;
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