package com.lance5057.extradelight.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.capabilities.FoodComponent;
import com.lance5057.extradelight.items.dynamicfood.DynamicJam;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.data.recipe.CraftingRecipes;

public class DynamicJamRecipe extends CookingPotRecipe {

    private final String graphic;

    public DynamicJamRecipe(ResourceLocation id, String group, CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems,
                            ItemStack output, ItemStack container, float experience, int cookTime, String graphic) {
        super(id, group, tab, inputItems, output, container, experience, cookTime);
        this.graphic = graphic;

    }

    public String getGraphic() {
        return graphic;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        ItemStack stack = super.getResultItem(access);
        if (stack.getItem() instanceof DynamicJam jam) {
            //stack.set(ExtraDelightComponents.DYNAMIC_FOOD.get(), new DynamicItemComponent(List.of(graphic)));
            stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).ifPresent(food -> {
                food.setGraphics(List.of(graphic));
            });

            List<ItemStack> stacks = new ArrayList<ItemStack>();
            for (Ingredient i : this.getIngredients()) {
                if (i.getItems().length > 0)
                    stacks.add(i.getItems()[0]);
            }

            //stack.set(ExtraDelightComponents.ITEMSTACK_HANDLER.get(), ItemContainerContents.fromItems(stacks));
            stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                for (int i = 0; i < stacks.size(); ++i) {
                    handler.insertItem(i, stacks.get(i), false);
                }
            });
        } else {
            ExtraDelight.logger.error("DynamicJamRecipe result not DynamicJam!");
        }
        stack.setTag(stack.getOrCreateTag().merge(ExtraDelightComponents.IDynamicFood.write(List.of(this.graphic))));
        return stack;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv, RegistryAccess registryAccess) {
        ItemStack stack = this.getResultItem(registryAccess).copy();
        if (stack.getItem() instanceof DynamicJam) {

            int nutrition = 0;
            float saturation = 0;

            List<ItemStack> l = new ArrayList<ItemStack>();
            for (int i = 0; i < inv.getContainerSize() - 2; i++) {
                ItemStack s = inv.getItem(i);
                if (s != null && !s.isEmpty()) {
                    l.add(s);
                    if (s.getCapability(ExtraDelightComponents.IFOOD).resolve().isPresent()) {
                        FoodComponent food = s.getCapability(ExtraDelightComponents.IFOOD).resolve().get().getFood();
                        FoodProperties f = food.getBuilder().build();
                        nutrition += f.getNutrition();
                        saturation += f.getSaturationModifier();

                    }
                }
            }
            stack.setTag(stack.getOrCreateTag().merge(ExtraDelightComponents.IDynamicFood.write(List.of(this.graphic))));

//			stack.set(ExtraDelightComponents.DYNAMIC_FOOD.get(), new DynamicItemComponent(List.of(graphic)));
//			stack.set(ExtraDelightComponents.ITEMSTACK_HANDLER.get(), ItemContainerContents.fromItems(l));

//            FoodProperties food = new FoodProperties(nutrition, saturation / inv.size(), false, 1.6F,
//                    java.util.Optional.empty(), effects);
//
//            stack.set(DataComponents.FOOD, food);
        } else {
            ExtraDelight.logger.error("DynamicToastRecipe result not DynamicToast!");
        }
        return stack;
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stack = event.getCrafting();
        Container inv = event.getInventory();
        List<ItemStack> l = new ArrayList<ItemStack>();
        if (stack.is(ExtraDelightItems.DYNAMIC_JAM.get())) {
            CompoundTag tag = event.getCrafting().getOrCreateTag();
            int nutrition = 0;
            float saturation = 0;
            List<Pair<Supplier<MobEffectInstance>, Float>> effects = new ArrayList<>();
            for (int i = 0; i < inv.getContainerSize() - 2; i++) {
                ItemStack s = inv.getItem(i);
                if (s != null && !s.isEmpty()) {
                    l.add(s);
                    FoodProperties foodProperties = s.getFoodProperties(null);
                    nutrition += foodProperties.getNutrition();
                    saturation += foodProperties.getSaturationModifier();
                    for (Pair<MobEffectInstance, Float> pair : foodProperties.getEffects()) {
                        effects.add(new Pair<>(pair::getFirst, pair.getSecond()));
                    }
                }

            }
            int finalNutrition = nutrition;
            float finalSaturation = saturation;
            stack.getCapability(ExtraDelightComponents.IFOOD).ifPresent(
                    food -> {
                        food.setFood(new FoodComponent(finalNutrition, finalSaturation, effects));
                    });
        }
        ExtraDelight.logger.debug("DynamicJamRecipe Tag: {}",stack.getOrCreateTag().toString());


//`            if(stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().isPresent()) {
//                IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
//                for(int i = 0; i < handler.getSlots(); i++) {
//                    handler.insertItem(i,l.get(i) , false);
//                }
//            }`

//            if(stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().isPresent()){
//                DynamicItemComponent dynamicFood = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().get().getDynamicFood();
//            }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExtraDelightRecipes.DYNAMIC_JAM_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ExtraDelightRecipes.DYNAMIC_JAM.get();
    }

    public static class Serializer implements RecipeSerializer<DynamicJamRecipe> {

        public Serializer() {
        }

        @Override
        public DynamicJamRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.findByName(
                    GsonHelper.getAsString(jsonObject, "category", ""));
            NonNullList<Ingredient> ingredient = NonNullList.create();
            JsonArray ingredientsJson = jsonObject.getAsJsonArray("ingredients");
            for (int i = 0; i < ingredientsJson.size(); ++i) {
                ingredient.add(Ingredient.fromJson(ingredientsJson.get(i)));
            }
            //ItemStack result = StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            ItemStack result = ItemStack.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("result")).result().orElseThrow().getFirst();
            ItemStack container = StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject, "container"));
            float experience = GsonHelper.getAsFloat(jsonObject, "experience");
            int cookTime = GsonHelper.getAsInt(jsonObject, "cooking_time");
            String graphic = GsonHelper.getAsString(jsonObject, "graphic");
            return new DynamicJamRecipe(pRecipeId, s, tab, ingredient, result, container, experience, cookTime, graphic);
        }

        @Override
        public @Nullable DynamicJamRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            String category = buffer.readUtf();
            CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.findByName(category);
            int i = buffer.readVarInt();

            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int l = 0; l < i; l++) {
                ingredients.add(Ingredient.fromNetwork(buffer));
            }

            ItemStack result = buffer.readItem();
            ItemStack container = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            String graphic = buffer.readUtf();

            return new DynamicJamRecipe(pRecipeId, s, tab, ingredients, result, container, experience, cookTime, graphic);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DynamicJamRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeUtf(recipe.getRecipeBookTab() != null ? recipe.getRecipeBookTab().toString() : "");
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem(null));
            buffer.writeItem(recipe.getOutputContainer());
            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookTime());
            buffer.writeUtf(recipe.getGraphic());
        }
    }
}
