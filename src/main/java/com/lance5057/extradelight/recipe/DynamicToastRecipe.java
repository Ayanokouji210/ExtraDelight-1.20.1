package com.lance5057.extradelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.items.dynamicfood.DynamicToast;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static vectorwing.farmersdelight.common.registry.ModItems.foodItem;

public class DynamicToastRecipe extends ShapedRecipe {
	private final ItemStack result;


	public DynamicToastRecipe(ResourceLocation id, String group,int width,int height, NonNullList<Ingredient> pattern,
							  ItemStack result) {
		super(id,group, CraftingBookCategory.MISC, width,height,pattern, result);
		this.result=result;
	}

	@Override
	public boolean matches(CraftingContainer pInv, Level pLevel) {
		return super.matches(pInv, pLevel);
	}

	@Override
	public ItemStack assemble(CraftingContainer input, RegistryAccess registries) {
		ItemStack stack = super.getResultItem(registries).copy();
		if (stack.getItem() instanceof DynamicToast) {

			int nutrition = 0;
			float saturation = 0;
			List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

			NonNullList<ItemStack> l = NonNullList.create();
			for (ItemStack s : input.getItems()) {
				if (s != null && !s.isEmpty()) {
					l.add(s);
					if (s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).isPresent()) {
						FoodProperties f = s.getFoodProperties(null);
                        if (f != null) {
                            nutrition += f.getNutrition();
							saturation += f.getSaturationModifier();
							effects.addAll(f.getEffects());
                        }
					} else
						ExtraDelight.logger
								.error(s.getDescriptionId() + " doesn't have a food component! How did we get here?!");
				}
			}

			Optional<ExtraDelightComponents.IDynamicFood> resolve = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve();
			if (resolve.isPresent()&&resolve.get()!=null) {
				DynamicItemComponent dynamicFood = resolve.get().getDynamicFood();
                for (ItemStack itemStack : l) {
                    dynamicFood.addItem(itemStack);
                }
			}

			FoodProperties.Builder food = new FoodProperties.Builder();
			food.nutrition(nutrition).saturationMod(saturation / input.getItems().size()).alwaysEat();
			for (Pair<MobEffectInstance, Float> pair : effects) {
				food.effect(pair.getFirst(), pair.getSecond());
			}
			stack.getItem().foodProperties = food.build();
		} else {
			ExtraDelight.logger.error("DynamicToastRecipe result not DynamicToast!");
		}
		return stack;
	}



	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.DYNAMIC_TOAST_SERIALIZER.get();
	}

//	@Override
//	public RecipeType<?> getType() {
//		return ExtraDelightRecipes.DYNAMIC_SANDWICH.get();
//	}

	public static class Serializer implements RecipeSerializer<DynamicToastRecipe> {
		@Override
		public DynamicToastRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");

			// 读取图案
			JsonArray patternJson = GsonHelper.getAsJsonArray(json, "pattern");
			String[] pattern = new String[patternJson.size()];
			for (int i = 0; i < patternJson.size(); i++) {
				pattern[i] = GsonHelper.convertToString(patternJson.get(i), "pattern[" + i + "]");
			}
			// 读取键
			JsonObject keyJson = GsonHelper.getAsJsonObject(json, "key");
			int width = pattern[0].length();
			int height = pattern.length;
			NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

			for (int i = 0; i < pattern.length; i++) {
				String patternRow = pattern[i];
				for (int j = 0; j < patternRow.length(); j++) {
					char c = patternRow.charAt(j);
					Ingredient ingredient = Ingredient.EMPTY;
					if (c != ' ') {
						String key = String.valueOf(c);
						JsonObject ingredientJson = GsonHelper.getAsJsonObject(keyJson, key);
						ingredient = Ingredient.fromJson(ingredientJson);
					}
					ingredients.set(j + i * width, ingredient);
				}
			}

			// 读取结果
			JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
			ItemStack result = CraftingHelper.getItemStack(resultJson, true);

			return new DynamicToastRecipe(resourceLocation, group, width, height, ingredients, result);
		}

		@Override
		public @Nullable DynamicToastRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			int width = buffer.readVarInt();
			int height = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

			for (int i = 0; i < ingredients.size(); i++) {
				ingredients.set(i, Ingredient.fromNetwork(buffer));
			}

			ItemStack result = buffer.readItem();

			return new DynamicToastRecipe(resourceLocation, group, width, height, ingredients, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, DynamicToastRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeVarInt(recipe.getWidth());
			buffer.writeVarInt(recipe.getHeight());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.result);
		}
//		public static final MapCodec<DynamicToastRecipe> CODEC = RecordCodecBuilder.mapCodec(p_340778_ -> p_340778_
//				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.getGroup()),
//						CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC)
//								.forGetter(p_311732_ -> p_311732_.category()),
//						ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
//						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.getResultItem(null)))
//
//				.apply(p_340778_, DynamicToastRecipe::new));
//		public static final StreamCodec<RegistryFriendlyByteBuf, DynamicToastRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public MapCodec<DynamicToastRecipe> codec() {
//			return CODEC;
//		}
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, DynamicToastRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}
//
//		private static DynamicToastRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
//			String s = buffer.readUtf();
//			CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
//			ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
//			ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
//			return new DynamicToastRecipe(s, craftingbookcategory, shapedrecipepattern, itemstack);
//		}
//
//		private static void toNetwork(RegistryFriendlyByteBuf buffer, DynamicToastRecipe recipe) {
//			buffer.writeUtf(recipe.getGroup());
//			buffer.writeEnum(recipe.category());
//			ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
//			ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem(null));
//		}
	}
}