package com.lance5057.extradelight.data.recipebuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.recipe.ShapedWithJarRecipe;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
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
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class ShapedWithJarRecipeBuilder implements RecipeBuilder {

	private final RecipeCategory category;
	private final Item result;
	private final int count;
	private final ItemStack resultStack; // Neo: add stack result support
	private final List<String> rows = Lists.newArrayList();
	private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	@Nullable
	private String group="";
	private boolean showNotification = true;
	private final List<FluidStack> fluids;

	public ShapedWithJarRecipeBuilder(RecipeCategory category, ItemLike result, int count, List<FluidStack> fluids) {
		this(category, new ItemStack(result, count), fluids);
	}

	public ShapedWithJarRecipeBuilder(RecipeCategory category, ItemStack result, List<FluidStack> fluids) {
		this.category = category;
		this.result = result.getItem();
		this.count = result.getCount();
		this.resultStack = result;
		this.fluids = fluids;
	}



//	/**
//	 * Creates a new builder for a shaped recipe.
//	 */
//	public static ShapedWithJarRecipeBuilder shaped(RecipeCategory category, ItemLike result, int i) {
//		return shaped(category, result, 1);
//	}

	/**
	 * Creates a new builder for a shaped recipe.
	 */
	public static ShapedWithJarRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count,
			FluidStack... fluids) {
		return new ShapedWithJarRecipeBuilder(category, result, count, Arrays.asList(fluids));
	}

	public static ShapedWithJarRecipeBuilder shaped(RecipeCategory p_251325_, ItemStack result,
			FluidStack... fluids) {
		return new ShapedWithJarRecipeBuilder(p_251325_, result, Arrays.asList(fluids));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public ShapedWithJarRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(tag));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public ShapedWithJarRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public ShapedWithJarRecipeBuilder define(Character symbol, Ingredient ingredient) {
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
	public ShapedWithJarRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pattern);
			return this;
		}
	}

	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		this.criteria.put(pCriterionName, new Criterion(pCriterionTrigger));
		return this;
	}

//	public ShapedWithJarRecipeBuilder unlockedBy(String name, Criterion criterion) {
//		this.criteria.put(name, criterion);
//		return this;
//	}

	public ShapedWithJarRecipeBuilder group(@Nullable String groupName) {
		this.group = groupName;
		return this;
	}

	public ShapedWithJarRecipeBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result;
	}
	


	@Override
	public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
		this.ensureValid(id);
		this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(this.advancement::addCriterion);
		recipeOutput.accept(new Result(id,this.group,this.category,this.key,this.rows,this.fluids,
				this.resultStack,this.showNotification,this.advancement,
				this.advancement.build(id.withPrefix("recipes/"+this.category.getFolderName()+"/")).getId()));
//		ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
//		Advancement.Builder advancement$builder = recipeOutput.advancement()
//				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
//				.rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
//		this.criteria.forEach(advancement$builder::addCriterion);
//		ShapedWithJarRecipe shapedrecipe = new ShapedWithJarRecipe(Objects.requireNonNullElse(this.group, ""),
//				RecipeBuilder.(this.category), shapedrecipepattern, this.fluids, this.resultStack,
//				this.showNotification);
//		recipeOutput.accept(id, shapedrecipe,
//				advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}


	private void ensureValid(ResourceLocation location) {
		if (this.rows.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + location + "!");
		} else {
			for(String s : this.rows) {
				if (s.isEmpty()) {
					throw new IllegalStateException("Pattern is empty for shaped recipe " + location + "!");
				}
			}
		}
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final RecipeCategory category;
		private final Map<Character, Ingredient> key;
		private final List<String> pattern;
		private final List<FluidStack> fluids;
		private final ItemStack result;
		private final boolean showNotification;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation id, String group, RecipeCategory category,
					  Map<Character, Ingredient> key, List<String> pattern, List<FluidStack> fluids,
					  ItemStack result, boolean showNotification, Advancement.Builder advancement,
					  ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.category = category;
			this.key = key;
			this.pattern = pattern;
			this.fluids = fluids;
			this.result = result;
			this.showNotification = showNotification;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
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

			if (!this.fluids.isEmpty()) {
				JsonArray fluidsArray = new JsonArray();
				for (FluidStack fluid : this.fluids) {
					// 这里需要根据你的 FluidStack 序列化方式实现
					// fluidsArray.add(FluidStackUtil.toJson(fluid));
				}
				json.add("fluids", fluidsArray);
			}

			JsonObject resultObject = new JsonObject();
			resultObject.addProperty("item", this.result.getItem().toString());
			if (this.result.getCount() > 1) {
				resultObject.addProperty("count", this.result.getCount());
			}
			json.add("result", resultObject);

			json.addProperty("show_notification", this.showNotification);
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.SHAPED_JAR_SERIALIZER.get();
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

        public RecipeCategory getCategory() {
            return category;
        }
    }

}
