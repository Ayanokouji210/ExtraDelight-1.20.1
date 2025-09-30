package com.lance5057.extradelight.workstations.doughshaping.recipes;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
//import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

public class DoughShapingRecipe extends SingleItemRecipe {

	public DoughShapingRecipe(ResourceLocation id, String pGroup, Ingredient pIngredient, ItemStack pResult) {
		super(ExtraDelightRecipes.DOUGH_SHAPING.get(), ExtraDelightRecipes.DOUGH_SHAPING_SERIALIZER.get(), id,pGroup,
				pIngredient, pResult);
	}

	@Override
	public boolean matches(Container input, Level pLevel) {
		return this.ingredient.test(input.getItem(0));
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
//	public boolean matches(SingleRecipeInput input, Level level) {
//		return this.ingredient.test(input.item());
//	}



	public ItemStack getToastSymbol() {
		return new ItemStack(ExtraDelightBlocks.DOUGH_SHAPING.get());
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public static class Serializer implements RecipeSerializer<DoughShapingRecipe> {
		public Serializer() {}

		@Override
		public DoughShapingRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "group", "");
			Ingredient ingredient;
			if (GsonHelper.isArrayNode(jsonObject, "ingredient")) {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredient"));
			} else {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			}
			//ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), false);

			Item item = GsonHelper.getAsItem(jsonObject, "result");
			int count = GsonHelper.getAsInt(jsonObject, "count", 1);
			ItemStack result = new ItemStack(item,count);

			return new DoughShapingRecipe(pRecipeId,s, ingredient, result);
		}

		@Nullable
		@Override
		public DoughShapingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			ItemStack itemstack = pBuffer.readItem();
			return new DoughShapingRecipe(pRecipeId,s, ingredient, itemstack);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, DoughShapingRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pRecipe.ingredient.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);
		}
//		private static final MapCodec<DoughShapingRecipe> CODEC = RecordCodecBuilder
//				.mapCodec(inst -> inst
//						.group(Codec.STRING.optionalFieldOf("group", "").forGetter(DoughShapingRecipe::getGroup),
//
//								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient")
//										.forGetter(p_301068_ -> p_301068_.ingredient),
//
//								ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result))
//						.apply(inst, DoughShapingRecipe::new));
//
//		public static DoughShapingRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
//			ItemStack itemstack = ItemStack.STREAM_CODEC.decode(pBuffer);
//			return new DoughShapingRecipe(s, ingredient, itemstack);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, DoughShapingRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.group);
//			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.ingredient);
//			ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
//		}
//
//		@Override
//		public MapCodec<DoughShapingRecipe> codec() {
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, DoughShapingRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, DoughShapingRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}
	}
}