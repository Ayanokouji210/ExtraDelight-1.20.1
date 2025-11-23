package com.lance5057.extradelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.capabilities.FoodComponent;
import com.lance5057.extradelight.items.dynamicfood.DynamicToast;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static vectorwing.farmersdelight.common.registry.ModItems.foodItem;

public class DynamicToastRecipe extends ShapelessRecipe {
    private final String graphic;

    public DynamicToastRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result,
                              NonNullList<Ingredient> ingredients, String graphic) {
        super(id, group, category, result, ingredients);
        this.graphic = graphic;
    }


    public String getGraphic() {
        return graphic;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        return super.matches(pInv, pLevel);
    }

    @Override
    public ItemStack assemble(CraftingContainer input, RegistryAccess registries) {
        return super.assemble(input, registries);
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stack = event.getCrafting();
        Container inventory = event.getInventory();

        if (stack.getItem() instanceof DynamicToast) {

            int nutrition = 0;
            float saturation = 0;
            List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

            NonNullList<ItemStack> l = NonNullList.create();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack s = inventory.getItem(i);
                if (s != null && !s.isEmpty()) {
                    l.add(s);
                    if (s.getFoodProperties(null) != null) {
                        FoodProperties f = s.getFoodProperties(null);
                        nutrition += f.getNutrition();
                        saturation += f.getSaturationModifier();
                        effects.addAll(f.getEffects());
                    }
                }
            }

            FoodProperties.Builder food = new FoodProperties.Builder();
            if (stack.getCapability(ExtraDelightComponents.IFOOD).isPresent()) {
                ExtraDelightComponents.IFood iFood = stack.getCapability(ExtraDelightComponents.IFOOD).resolve().get();
                FoodComponent food1 = iFood.getFood();
                food1.set(nutrition, saturation, effects.stream().map(
                        pair -> new Pair<Supplier<MobEffectInstance>, Float>(() -> new MobEffectInstance(pair.getFirst().getEffect()), pair.getSecond())).toList());
            } else {
                ExtraDelight.logger.error("Can not Modify ifood capability!");

            }
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
       return ExtraDelightRecipes.DYNAMIC_TOAST_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ExtraDelightRecipes.DYNAMIC_TOAST.get();
    }

	public static class Serializer implements RecipeSerializer<DynamicToastRecipe> {
		@Override
		public DynamicToastRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");
            Pair<CraftingBookCategory, JsonElement> pair = CraftingBookCategory.CODEC.decode(JsonOps.INSTANCE, json.get("category")).result().orElse(null);
            CraftingBookCategory category = pair != null ? pair.getFirst() : CraftingBookCategory.MISC;

            NonNullList<Ingredient> ingredients = NonNullList.create();

            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients", new JsonArray());
            jsonArray.asList().stream().map(Ingredient::fromJson).forEach(ingredients::add);

			JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
			ItemStack result = CraftingHelper.getItemStack(resultJson, true);

            String graphic = GsonHelper.getAsString(json, "graphic");

            return new DynamicToastRecipe(resourceLocation, group, category, result, ingredients, graphic);
		}

		@Override
		public @Nullable DynamicToastRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
            CraftingBookCategory category = CraftingBookCategory.valueOf(buffer.readUtf());

            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
				ingredients.set(i, Ingredient.fromNetwork(buffer));
			}

			ItemStack result = buffer.readItem();

            String graphic = buffer.readUtf();

            return new DynamicToastRecipe(resourceLocation, group, category, result, ingredients,graphic);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, DynamicToastRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeUtf(recipe.category().name());

            buffer.writeVarInt(recipe.getIngredients().size());
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem(null));

            buffer.writeUtf(recipe.getGraphic());
		}
	}
}