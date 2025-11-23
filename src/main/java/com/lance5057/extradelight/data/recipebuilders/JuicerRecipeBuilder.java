package com.lance5057.extradelight.data.recipebuilders;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.juicer.JuicerRecipe;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JuicerRecipeBuilder implements RecipeBuilder {
	private final ItemStack result;
	private final Ingredient ingredient;
	private final FluidStack fluid;
	private final int chance;
	@Nullable
	private String group;
	private final Map<String, Criterion> criteria = new LinkedHashMap<>();

	private JuicerRecipeBuilder(ItemStack pResult, Ingredient pIngredient, FluidStack fluidOut, int chance) {
		this.result = pResult;
		this.ingredient = pIngredient;
		this.fluid = fluidOut;
		this.chance = chance;
	}

	public static JuicerRecipeBuilder squeeze(Ingredient pIngredient, ItemStack pResult, FluidStack fluidOut, int chance) {
		return new JuicerRecipeBuilder(pResult, pIngredient, fluidOut, chance);
	}

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.criteria.put(pCriterionName, new Criterion(pCriterionTrigger));
        return this;
    }

    public JuicerRecipeBuilder group(@Nullable String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	public Item getResult() {
		if (result != null)
			return this.result.getItem();
		return Items.AIR;
	}

	@Override
	public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
		ResourceLocation recipeId = id.withPrefix("juicer/");
		Advancement.Builder advancementBuilder = Advancement.Builder.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);

	    output.accept(new Result(id,this.ingredient,this.fluid,this.chance,this.result,
                advancementBuilder,advancementBuilder.build(id.withPrefix("recipes/juicer/")).getId()));
	}

    public class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private String group="";
        private final Ingredient ingredient;
        private final FluidStack fluidOut;
        private final int chance;
        private final ItemStack result;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient ingredient, FluidStack fluidOut,
                      int chance, ItemStack result, Advancement.Builder advancementBuilder,
                      ResourceLocation advancementId) {
            this.id = id;
            this.ingredient = ingredient;
            this.fluidOut = fluidOut;
            this.chance = chance;
            this.result = result;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        public Result(ResourceLocation id, Ingredient ingredient, FluidStack fluidOut, int chance,
                      ItemStack result, Advancement.Builder advancementBuilder,
                      ResourceLocation advancementId, String group) {
            this.id = id;
            this.ingredient = ingredient;
            this.fluidOut = fluidOut;
            this.chance = chance;
            this.result = result;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.group = group;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            if(!this.group.isEmpty()){
                pJson.addProperty("group", this.group);
            }
            pJson.addProperty("chance",this.chance);
            pJson.add("fluidOut", StackUtil.FluidStacktoJson(fluidOut));
            pJson.add("ingredient", ingredient.toJson());
            pJson.add("result", StackUtil.ItemStacktoJson(result));
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ExtraDelightRecipes.JUICER_SERIALIZER.get();
        }

        @Override
        public @org.jetbrains.annotations.Nullable JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        @Override
        public @org.jetbrains.annotations.Nullable ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}