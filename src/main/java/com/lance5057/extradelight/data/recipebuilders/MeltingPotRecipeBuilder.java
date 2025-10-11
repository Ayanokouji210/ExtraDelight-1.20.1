package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotRecipe;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.fluids.FluidStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MeltingPotRecipeBuilder implements RecipeBuilder {

	private String group = "";
	private final Ingredient input;
	private final int cooktime;
	private final FluidStack output;
	private Advancement.Builder advancement$builder;
	private MeltingPotRecipe.Serializer serializer;
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	public MeltingPotRecipeBuilder(Ingredient input, int cooktime, FluidStack output) {
		this.input = input;
		this.cooktime = cooktime;
		this.output = output;
		this.advancement$builder = Advancement.Builder.advancement();
		this.serializer = new MeltingPotRecipe.Serializer();
	}

	public static MeltingPotRecipeBuilder melt(Ingredient in, int time, FluidStack out) {
		return new MeltingPotRecipeBuilder(in, time, out);
	}

	@Override
	public RecipeBuilder unlockedBy(String s, CriterionTriggerInstance criterionTriggerInstance) {
		this.criteria.put(s,new Criterion(criterionTriggerInstance));
		return this;
	}


	@Override
	public RecipeBuilder group(String groupName) {
		this.group = groupName;
		return this;
	}

	@Override
	public Item getResult() {
		return Items.AIR;
	}

	@Override
	public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
		ResourceLocation recipeId = id.withPrefix("melting/");
		this.advancement$builder.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		//MeltingPotRecipe recipe = new MeltingPotRecipe(this.input, this.cooktime, this.output, group);
		Advancement advancementId = this.advancement$builder.build(id.withPrefix("recipes/melting/"));
		recipeOutput.accept(new Result(id,this.group,this.input,this.cooktime,
				this.output,this.serializer,advancementId.getId(),advancement$builder));
				//recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/melting/")));
	}

	public class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient input;
		private final int cookTime;
		private final FluidStack output;
		private final Advancement.Builder advancementBuilder;
		private final ResourceLocation advancementId;
		private final MeltingPotRecipe.Serializer serializer;

		public Result(ResourceLocation id, String group, Ingredient input,
					  int cookime, FluidStack output, MeltingPotRecipe.Serializer serializer,
					  ResourceLocation advancementId, Advancement.Builder advancementBuilder) {
			this.id = id;
			this.group = group;
			this.input = input;
			this.cookTime = cooktime;
			this.output = output;
			this.serializer = serializer;
			this.advancementId = advancementId;
			this.advancementBuilder = advancementBuilder;
		}

		@Override
		public void serializeRecipeData(JsonObject jsonObject) {
            if (!group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }
			jsonObject.addProperty("cooktime", this.cookTime);
			jsonObject.add("ingredient", this.input.toJson());
			jsonObject.add("result", StackUtil.FluidStacktoJson(this.output));
        }

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ExtraDelightRecipes.MELTING_POT_SERIALIZER.get();
		}

		@Override
		public @Nullable JsonObject serializeAdvancement() {
			return this.advancementBuilder.serializeToJson();
		}

		@Override
		public @Nullable ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}

}
