package com.lance5057.extradelight.data.recipebuilders;

import com.blamejared.crafttweaker.impl.plugin.core.PluginManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.chiller.ChillerRecipe;
import net.minecraft.CrashReport;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.fluids.FluidStack;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChillerRecipeBuilder implements RecipeBuilder {

//	private ChillerRecipeBookTab tab;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStack resultStack;
	private final int cookingTime;
	private final float experience;
	private final ItemStack container;
	private final FluidStack fluid;
	private final Boolean consumeContainer;
	private String group ="";
	private final Advancement.Builder advancement=Advancement.Builder.advancement();
	private final ChillerRecipe.Serializer serializer;
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	private ChillerRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, ItemStack container,
			FluidStack fluid, boolean consumeContainer) {
		this.result = resultIn.getItem();
		this.resultStack = resultIn;
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container;
//		this.tab = null;
		this.fluid = fluid;
		this.consumeContainer = consumeContainer;
		this.serializer = new ChillerRecipe.Serializer();
	}

	public static ChillerRecipeBuilder chill(ItemStack mainResult, int cookingTime, float experience,
			ItemStack container, FluidStack fluid) {
		return new ChillerRecipeBuilder(mainResult, cookingTime, experience, container, fluid, false);
	}

	public static ChillerRecipeBuilder chill(ItemStack mainResult, int cookingTime, float experience,
			ItemStack container, FluidStack fluid, boolean consumeContainer) {
		return new ChillerRecipeBuilder(mainResult, cookingTime, experience, container, fluid, consumeContainer);
	}

	public ChillerRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		return addIngredient(Ingredient.of(tagIn));
	}

	public ChillerRecipeBuilder addIngredient(TagKey<Item> itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}

	public ChillerRecipeBuilder addIngredient(ItemLike itemIn) {
		return addIngredient(itemIn, 1);
	}

	public ChillerRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}

	public ChillerRecipeBuilder addIngredient(Ingredient ingredientIn) {
		return addIngredient(ingredientIn, 1);
	}

	public ChillerRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredientToList(ingredientIn);
		}
		return this;
	}

	private void addIngredientToList(Ingredient i) {
		if (ingredients.size() > 3)
			Minecraft.crash(CrashReport.forThrowable(new Throwable(), "Chiller cannot accept more than 4 items! Waah!"));
		else
			ingredients.add(i);
	}

	public ChillerRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
		this.criteria.put(criterionName,  new Criterion(criterionTrigger));
		return this;
	}

	public ChillerRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	public ChillerRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
		this.criteria.put("has_any_ingredient",
				new Criterion(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build())));
		return this;
	}

//	public ChillerRecipeBuilder setRecipeBookTab(ChillerRecipeBookTab tab) {
//		this.tab = tab;
//		return this;
//	}


	public void build(Consumer<FinishedRecipe> consumer) {
//		ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
//		build(consumer, ExtraDelight.MOD_ID + ":" + location.getPath());
		String fluidName = BuiltInRegistries.FLUID.getKey(fluid.getFluid()).getPath();
		String itemName  = BuiltInRegistries.ITEM.getKey(result).getPath();
		//String path = itemName + "_from_" + fluidName;   // 例如 jelly_white_block_item_from_dark_chocolate
		String path = itemName + "_from_" + fluidName + (ingredients.isEmpty() ? "" : "_filled");
		build(consumer, ExtraDelight.MOD_ID + ":" + path);
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		save(consumerIn, ResourceLocation.parse(save));
	}

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
		ResourceLocation recipeId = id.withPrefix("chilling/");
		this.advancement
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(this.advancement::addCriterion);

//		ChillerRecipe recipe = new ChillerRecipe(id,"", this.ingredients, this.fluid, this.resultStack, this.container,
//				this.experience, this.cookingTime, this.consumeContainer);
		ResourceLocation advancementId = this.advancement.build(id.withPrefix("recipes/chilling")).getId();
		output.accept(new Result(id,this.group,this.fluid,this.resultStack,this.container,this.advancement,advancementId,
				this.consumeContainer,this.cookingTime,this.experience,this.ingredients,this.serializer));
				//recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/chilling/")));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private NonNullList<Ingredient> inputItems;
		private final FluidStack inputFluid;
		private final ItemStack resultStack;
		private final ItemStack containerStack;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;
		private final Boolean consumeContainer;
		private final int cookingTime;
		private final float experience;

		private ChillerRecipe.Serializer serializer;


		public Result(ResourceLocation id, String group, FluidStack inputFluid,
					  ItemStack resultStack, ItemStack containerStack, Advancement.Builder advancement,
					  ResourceLocation advancementId, Boolean consumeContainer, int cookingTime, float experience,
					  NonNullList<Ingredient> inputItems, ChillerRecipe.Serializer serializer) {
			this.id = id;
			this.group = group;
			this.inputFluid = inputFluid;
			this.resultStack = resultStack;
			this.containerStack = containerStack;
			this.advancement = advancement;
			this.advancementId = advancementId;
			this.consumeContainer = consumeContainer;
			this.cookingTime = cookingTime;
			this.experience = experience;
			this.inputItems = inputItems;
			this.serializer = serializer;
		}

		@Override
		public void serializeRecipeData(JsonObject jsonObject) {
			if(!this.group.isEmpty()){
				jsonObject.addProperty("group", this.group);
			}
			jsonObject.addProperty("consumeContainer",this.consumeContainer);
			jsonObject.add("container", StackUtil.ItemStacktoJson(containerStack));
			jsonObject.addProperty("cookingTime",this.cookingTime);
			jsonObject.addProperty("experience",this.experience);
			jsonObject.add("fluid", StackUtil.FluidStacktoJson(this.inputFluid));

			JsonArray ingredientsArray = new JsonArray();
			for(Ingredient ingredient : this.inputItems){
				ingredientsArray.add(ingredient.toJson());
			}
			jsonObject.add("ingredients",ingredientsArray);

			jsonObject.add("result", StackUtil.ItemStacktoJson(this.resultStack));
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.CHILLER_SERIALIZER.get();
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

}
