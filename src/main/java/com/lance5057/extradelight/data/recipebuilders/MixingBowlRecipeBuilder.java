package com.lance5057.extradelight.data.recipebuilders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.lance5057.extradelight.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.*;
//import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
//import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MixingBowlRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final List<FluidIngredient> fluids = new ArrayList<>();
    @Nullable
    private String group;
    final int stirs;
    final ItemStack usedItem;
    private final Map<String, Criterion> criteria = new LinkedHashMap<>();
    private final Advancement.Builder advancement$builder;

    public MixingBowlRecipeBuilder(ItemStack pResult, int stirs, ItemStack usedItem) {
        this.stirs = stirs;
        this.usedItem = usedItem;
        this.result = pResult;
        this.advancement$builder = Advancement.Builder.advancement();
    }

    public static MixingBowlRecipeBuilder stir(ItemStack pResult, int grinds, ItemStack usedItem) {
        return new MixingBowlRecipeBuilder(pResult, grinds, usedItem);
    }

    @Override
    public RecipeBuilder unlockedBy(String s, CriterionTriggerInstance criterionTriggerInstance) {
        this.criteria.put(s, new Criterion(criterionTriggerInstance));
        return this;
    }

    //	public MixingBowlRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
//		this.criteria.put(criterionName, criterionTrigger);
//		return this;
//	}

    public MixingBowlRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public MixingBowlRecipeBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    public MixingBowlRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }

    public MixingBowlRecipeBuilder requires(FluidIngredient stack) {
        this.fluids.add(stack);
        return this;
    }

    public MixingBowlRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public MixingBowlRecipeBuilder requires(ItemLike pItem) {
        return this.requires(pItem, 1);
    }

    public MixingBowlRecipeBuilder requires(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    public Item getResult() {
        return this.result.getItem();
    }


    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        if (this.ingredients.size() > 9)
            throw new IllegalStateException("Mixing Bowl Recipe " + id + " has more than 9 ingredients!");

        ResourceLocation recipeId = id.withPrefix("mixing/");
        this.advancement$builder.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        this.criteria.forEach(this.advancement$builder::addCriterion);
//		MixingBowlRecipe recipe = new MixingBowlRecipe("", this.ingredients, this.fluids, this.result, this.stirs,
//				this.usedItem);
        ResourceLocation advancementId = advancement$builder.build(id.withPrefix("recipes/mixing/")).getId();
        output.accept(new Result(id, this.group == null ? "" : this.group, this.ingredients, this.fluids, this.result, this.stirs, this.usedItem,
                advancement$builder, advancementId));
        //recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/mixing/")));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final NonNullList<Ingredient> ingredients;
        private final List<FluidIngredient> fluids;
        private final ItemStack result;
        private final int stirs;
        private final ItemStack usedItem;
        private final Advancement.Builder advancement$builder;
        private final ResourceLocation advancementId;


        public Result(ResourceLocation id, String group, NonNullList<Ingredient> ingredients,
                      List<FluidIngredient> fluids, ItemStack result, int stirs, ItemStack usedItem,
                      Advancement.Builder advancement$builder, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.ingredients = ingredients;
            this.fluids = fluids;
            this.result = result;
            this.stirs = stirs;
            this.usedItem = usedItem;
            this.advancement$builder = advancement$builder;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray fluidsArray = new JsonArray();
            for (FluidIngredient fluid : this.fluids) {
                fluidsArray.add(fluid.serialize());
            }
            jsonObject.add("fluids", fluidsArray);

            JsonArray ingredientsArray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsArray.add(ingredient.toJson());
            }
            jsonObject.add("ingredients", ingredientsArray);

            jsonObject.add("result", StackUtil.ItemStacktoJson(this.result));
            jsonObject.addProperty("stirs", this.stirs);
            if (this.usedItem != null) {
                //jsonObject.addProperty("usedItem", this.usedItem.toString());
                jsonObject.add("usedItem", StackUtil.ItemStacktoJson(this.usedItem));
            }


        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ExtraDelightRecipes.MIXING_BOWL_SERIALIZER.get();
        }

        @Override
        public @org.jetbrains.annotations.Nullable JsonObject serializeAdvancement() {
            return this.advancement$builder.serializeToJson();
        }

        @Override
        public @org.jetbrains.annotations.Nullable ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
