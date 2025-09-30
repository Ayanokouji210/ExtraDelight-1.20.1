package com.lance5057.extradelight.data.recipebuilders;

//import com.lance5057.extradelight.workstations.evaporator.recipes.EvaporatorRecipe;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.evaporator.recipes.EvaporatorRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EvaporatorRecipeBuilder implements RecipeBuilder {
	@Nullable
	private String group;
	FluidIngredient fluid;
	ResourceLocation lootTable;
	Block display;
	ItemStack out;

	protected final int cookTime;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();

	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	public EvaporatorRecipeBuilder(FluidIngredient fluid, ItemStack out, ResourceLocation lootTable, int cookTime,
								   Block displayBlock) {
		this.fluid = fluid;
		this.lootTable = lootTable;
		this.cookTime = cookTime;
		this.display = displayBlock;
		this.out = out;
	}

	public static EvaporatorRecipeBuilder evaporate(FluidIngredient fluid, ItemStack out, ResourceLocation lootTable,
			int cookTime, Block displayBlock) {
		return new EvaporatorRecipeBuilder(fluid, out, lootTable, cookTime, displayBlock);
	}

//	@Override
//	public RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
//		this.criteria.put(criterionName, criterionTrigger);
//		return this;
//	}

	@Override
	public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
		this.criteria.put(criterionName, new Criterion(criterionTrigger));
		return this;
	}

	@Override
	public EvaporatorRecipeBuilder group(String groupName) {
		this.group = groupName;
		return this;
	}

	@Override
	public Item getResult() {
		return Items.STICK; // Cause sometimes other mods are dumb
	}

//	@Override
//	public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
//		ResourceLocation recipeId = id.withPrefix("evaporator/");
//		Advancement.Builder advancementBuilder = recipeOutput
//				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
//				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
//		this.criteria.forEach(advancementBuilder::addCriterion);
//		EvaporatorRecipe recipe = new EvaporatorRecipe("", this.fluid, this.cookTime, this.lootTable,
//				BuiltInRegistries.BLOCK.getKey(display), out);
//		recipeOutput.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/evaporator/")));
//	}

//	@Override
//	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
//
//		ResourceLocation recipeId = id.withPrefix("evaporator/");
//		Advancement.Builder advancementBuilder = recipeOutput.advancement()
//				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
//				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
//		this.criteria.forEach(advancementBuilder::addCriterion);
//		EvaporatorRecipe recipe = new EvaporatorRecipe("", this.fluid, this.cookTime, this.lootTable,
//				BuiltInRegistries.BLOCK.getKey(display), out);
//		recipeOutput.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/evaporator/")));
//	}


	@Override
	public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation id) {
		ResourceLocation recipeId = id.withPrefix("evaporator/");
		this.advancement.addCriterion("has_the_recipe",RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(this.advancement::addCriterion);
		EvaporatorRecipe recipe = new EvaporatorRecipe(id,"",this.fluid,this.cookTime,this.lootTable,BuiltInRegistries.BLOCK.getKey(display),out);
		pFinishedRecipeConsumer.accept(new Result(id,"",this.fluid,this.lootTable,this.display,this.out,this.cookTime,this.advancement, this.advancement.build(id.withPrefix("recipes/evaporator")).getId()));
				//recipeId,recipe,advancementBuilder.build(id.withPrefix("recpies/evaporator")));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final FluidIngredient fluid;
		private final Block displayBlock;
		private final ItemStack out;
		private final int cookTime;
		private final Advancement.Builder advancement;
		private final @org.jetbrains.annotations.Nullable ResourceLocation advancementId;
		private ResourceLocation lootTable;

		public Result(ResourceLocation id, String group, FluidIngredient fluid,ResourceLocation lootTable,
					  Block displayBlock, ItemStack out,int cookTime,Advancement.Builder advancement,
					  @org.jetbrains.annotations.Nullable ResourceLocation advancementId) {
			this.id = id;
			this.group = group;
			this.fluid = fluid;
			this.displayBlock = displayBlock;
			this.lootTable = lootTable;
			this.out = out;
			this.cookTime = cookTime;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		@Override
		public void serializeRecipeData(JsonObject pJson) {
			if (group != null && !group.isEmpty()) {
				pJson.addProperty("group", this.group);
			}
			pJson.addProperty("display_block", ForgeRegistries.BLOCKS.getKey(this.displayBlock).toString());
			pJson.add("fluid", this.fluid.serialize());
			pJson.addProperty("loottable", this.lootTable.toString());
			pJson.add("outItem", StackUtil.ItemStacktoJson(this.out));
			pJson.addProperty("time",this.cookTime);

		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.EVAPORATOR_SERIALIZER.get();
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
