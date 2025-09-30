package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.oven.recipes.OvenRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.Consumer<FinishedRecipe>;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OvenRecipeBuilder implements RecipeBuilder {
//	private OvenRecipeBookTab tab;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStack resultStack;
	private final int cookingTime;
	private final float experience;
	private final ItemStack container;
	private final boolean consumeContainer;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();
	private String group="";

	private OvenRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, ItemStack container,
			boolean consumeContainer) {
		this.result = resultIn.getItem();
		this.resultStack = resultIn;
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container;
//		this.tab = null;
		this.consumeContainer = consumeContainer;
	}

	public static OvenRecipeBuilder OvenRecipe(ItemStack mainResult, int cookingTime, float experience,
			ItemStack container, boolean consumeContainer) {
		return new OvenRecipeBuilder(mainResult, cookingTime, experience, container, consumeContainer);
	}

	public OvenRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		return addIngredient(Ingredient.of(tagIn));
	}

	public OvenRecipeBuilder addIngredient(TagKey<Item> itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}

	public OvenRecipeBuilder addIngredient(ItemLike itemIn) {
		return addIngredient(itemIn, 1);
	}

	public OvenRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}

	public OvenRecipeBuilder addIngredient(Ingredient ingredientIn) {
		return addIngredient(ingredientIn, 1);
	}

	public OvenRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			ingredients.add(ingredientIn);
		}
		return this;
	}

	public OvenRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
		this.advancement.addCriterion(criterionName, criterionTrigger);
		// this.criteria.put(criterionName, new Criterion(criterionTrigger));
		return this;
	}

	public OvenRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	public OvenRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
//		this.criteria.put("has_any_ingredient",
//				InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
		this.advancement.addCriterion("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(items));
		return this;
	}

//	public OvenRecipeBuilder setRecipeBookTab(OvenRecipeBookTab tab) {
//		this.tab = tab;
//		return this;
//	}

	public void build(Consumer<FinishedRecipe> consumer) {
		ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
		build(consumer, ExtraDelight.MOD_ID + ":" + location.getPath());
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		save(consumerIn, ResourceLocation.parse(save));
	}

//	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
//		ResourceLocation saveID = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "oven/" + id.getPath());
//		if (!advancement.getCriteria().isEmpty()) {
//			advancement.parent(ResourceLocation.fromNamespaceAndPath("recipes/root"))
//					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
//					.rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
//			ResourceLocation advancementId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(),
//					"recipes/" + result.getItemCategory().getRecipeFolderName() + "/" + id.getPath());
//
//			consumerIn.accept(new OvenRecipeBuilder.Result(saveID, result, count, ingredients, cookingTime, experience,
//					container, tab, advancement, advancementId));
//		} else {
//			consumerIn.accept(new OvenRecipeBuilder.Result(saveID, result, count, ingredients, cookingTime, experience,
//					container, tab));
//		}
//	}

	@Override
	public RecipeBuilder group(String p_176495_) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.result;
	}

	@Override
	public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
		ResourceLocation recipeId = id.withPrefix("cooking/oven/");
		Advancement.Builder advancementBuilder = this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);

//		public OvenRecipe(String group, @Nullable OvenRecipeBookTab tab, NonNullList<Ingredient> inputItems,
//				ItemStack output, ItemStack container, float experience, int cookTime) {
//		OvenRecipe recipe = new OvenRecipe("", /* this.tab, */ this.ingredients, this.resultStack, this.container,
//				this.experience, this.cookingTime, this.consumeContainer);
		//output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/cooking/")));
		output.accept(new Result(recipeId, this.group == null ? "" : this.group, this.ingredients,
				this.resultStack, this.container, this.experience, this.cookingTime,
				this.consumeContainer, this.advancement,
				new ResourceLocation(id.getNamespace(), "recipes/oven/" + id.getPath())));
	}


	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final NonNullList<Ingredient> ingredients;
		private final ItemStack result;
		private final ItemStack container;
		private final float experience;
		private final int cookingTime;
		private final boolean consumeContainer;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation id, String group, NonNullList<Ingredient> ingredients,
					  ItemStack result, ItemStack container, float experience, int cookingTime,
					  boolean consumeContainer, Advancement.Builder advancement, ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.ingredients = ingredients;
			this.result = result;
			this.container = container;
			this.experience = experience;
			this.cookingTime = cookingTime;
			this.consumeContainer = consumeContainer;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			JsonArray arrayIngredients = new JsonArray();
			for (Ingredient ingredient : ingredients) {
				arrayIngredients.add(ingredient.toJson());
			}
			json.add("ingredients", arrayIngredients);

//			JsonObject objectResult = new JsonObject();
//			objectResult.addProperty("item", this.result.getItem().toString());
//			if (this.result.getCount() > 1) {
//				objectResult.addProperty("count", this.result.getCount());
//			}
//			json.add("result", objectResult);

			JsonObject result = StackUtil.ItemStacktoJson(this.result);
			json.add("result", result);

			json.add("container", StackUtil.ItemStacktoJson(this.container));


			if (this.experience > 0) {
				json.addProperty("experience", this.experience);
			}

			json.addProperty("cookingtime", this.cookingTime);
			json.addProperty("consumeContainer", this.consumeContainer);
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.OVEN_SERIALIZER.get();
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

//	public static class Result implements Consumer<FinishedRecipe> {
//		private final ResourceLocation id;
//		private final OvenRecipeBookTab tab;
//		private final List<Ingredient> ingredients;
//		private final Item result;
//		private final int count;
//		private final int cookingTime;
//		private final float experience;
//		private final Item container;
//		private final Advancement.Builder advancement;
//		private final ResourceLocation advancementId;
//
//		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn,
//				int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable OvenRecipeBookTab tabIn,
//				@Nullable Advancement.Builder advancement, @Nullable ResourceLocation advancementId) {
//			this.id = idIn;
//			this.tab = tabIn;
//			this.ingredients = ingredientsIn;
//			this.result = resultIn;
//			this.count = countIn;
//			this.cookingTime = cookingTimeIn;
//			this.experience = experienceIn;
//			this.container = containerIn;
//			this.advancement = advancement;
//			this.advancementId = advancementId;
//		}
//
//		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn,
//				int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable OvenRecipeBookTab tabIn) {
//			this(idIn, resultIn, countIn, ingredientsIn, cookingTimeIn, experienceIn, containerIn, tabIn, null, null);
//		}
//
//		@Override
//		public void serializeRecipeData(JsonObject json) {
//			if (tab != null) {
//				json.addProperty("recipe_book_tab", tab.toString());
//			}
//
//			JsonArray arrayIngredients = new JsonArray();
//
//			for (Ingredient ingredient : ingredients) {
//				arrayIngredients.add(ingredient.toJson());
//			}
//			json.add("ingredients", arrayIngredients);
//
//			JsonObject objectResult = new JsonObject();
//			objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
//			if (count > 1) {
//				objectResult.addProperty("count", count);
//			}
//			json.add("result", objectResult);
//
//			if (container != null) {
//				JsonObject objectContainer = new JsonObject();
//				objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(container).toString());
//				json.add("container", objectContainer);
//			}
//			if (experience > 0) {
//				json.addProperty("experience", experience);
//			}
//			json.addProperty("cookingtime", cookingTime);
//		}
//
//		@Override
//		public ResourceLocation getId() {
//			return id;
//		}
//
//		@Override
//		public RecipeSerializer<?> getType() {
//			return ExtraDelightRecipes.OVEN_SERIALIZER.get();
//		}
//
//		@Nullable
//		@Override
//		public JsonObject serializeAdvancement() {
//			return advancement != null ? advancement.serializeToJson() : null;
//		}
//
//		@Nullable
//		@Override
//		public ResourceLocation getAdvancementId() {
//			return advancementId;
//		}
//	}

}
