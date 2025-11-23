package com.lance5057.extradelight.data.recipebuilders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.recipe.DynamicJamRecipe;

import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DynamicJamRecipeBuilder implements RecipeBuilder {
    private CookingPotRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final ItemStack resultStack;
    private final int cookingTime;
    private final float experience;
    private final ItemStack container;
    private final Map<String, Criterion> criteria = new LinkedHashMap<>();

    private final String graphic;

    public DynamicJamRecipeBuilder(int count, int cookingTime, float experience, @Nullable ItemLike container, String graphic) {
        this(cookingTime, experience, container, graphic);
    }

    public DynamicJamRecipeBuilder(int cookingTime, float experience, @Nullable ItemLike container, String graphic) {
        this.resultStack = new ItemStack(ExtraDelightItems.DYNAMIC_JAM.get(),1,
                ExtraDelightComponents.IDynamicFood.write(List.of(graphic)));
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.container = container != null ? new ItemStack(container) : ItemStack.EMPTY;
        this.tab = null;
        this.graphic = graphic;
    }

    public static DynamicJamRecipeBuilder cookingPotRecipe(int count, int cookingTime, float experience, String graphic) {
        return new DynamicJamRecipeBuilder(count, cookingTime, experience, null, graphic);
    }

    public static DynamicJamRecipeBuilder cookingPotRecipe(int count, int cookingTime, float experience,
                                                           ItemLike container, String graphic) {
        return new DynamicJamRecipeBuilder(count, cookingTime, experience, container, graphic);
    }

    public DynamicJamRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(tagIn));
    }

    public DynamicJamRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public DynamicJamRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public DynamicJamRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public DynamicJamRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }



    @Override
    public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
        return this;
    }

    public DynamicJamRecipeBuilder setRecipeBookTab(CookingPotRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public Item getResult() {
        return this.resultStack.getItem();
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.criteria.put(pCriterionName, new Criterion(pCriterionTrigger));
        return this;
    }



    public DynamicJamRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        this.criteria.put(criterionName, new Criterion(
                InventoryChangeTrigger.TriggerInstance.hasItems(items)));
        return this;
    }

    public DynamicJamRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.criteria.put("has_any_ingredient",
               new Criterion(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build())));
        return this;
    }

    public void build(Consumer<FinishedRecipe> output) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(resultStack.getItem());
        save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, location.getPath()));
    }

    public void build(Consumer<FinishedRecipe> outputIn, String save) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(resultStack.getItem());
        if ((ResourceLocation.parse(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
        } else {
            save(outputIn, ResourceLocation.parse(save));
        }
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        ResourceLocation recipeId = id.withPrefix("cooking/");
        Advancement.Builder advancementBuilder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
//        DynamicJamRecipe recipe = new DynamicJamRecipe(id,"", this.tab, this.ingredients, this.resultStack, this.container,
//                this.experience, this.cookingTime, this.graphic);
        output.accept(new Result(id, "", this.tab, this.ingredients, this.resultStack, this.container,
                this.experience, this.cookingTime, this.graphic, advancementBuilder,
                advancementBuilder.build(id.withPrefix("recipes/cooking/")).getId()));
        //output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/cooking/")));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private String group="";
        private final CookingPotRecipeBookTab tab;
        private final NonNullList<Ingredient> ingredient;
        private final ItemStack result;
        private final ItemStack container;
        private final float experience;
        private final float cookingTime;
        private final String graphic;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group,
                      CookingPotRecipeBookTab tab, NonNullList<Ingredient> ingredient,
                      ItemStack result, ItemStack container, float experience, float cookingTime,
                      String graphic, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.tab = tab;
            this.ingredient = ingredient;
            this.result = result;
            this.container = container;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.graphic = graphic;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            if(!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }
            pJson.addProperty("category",this.tab.getSerializedName());
            JsonArray ingredients = new JsonArray();
            for(Ingredient ingredient : this.ingredient) {
                ingredients.add(ingredient.toJson());
            }
            pJson.add("ingredients",ingredients);
            JsonElement resultjson = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, result).result().orElseThrow();
            pJson.add("result",resultjson);
            pJson.add("container",StackUtil.ItemStacktoJson(container,true));
            pJson.addProperty("experience",this.experience);
            pJson.addProperty("cooking_time",this.cookingTime);
            pJson.addProperty("graphic",this.graphic);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ExtraDelightRecipes.DYNAMIC_JAM_SERIALIZER.get();
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
