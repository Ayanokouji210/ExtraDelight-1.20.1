package com.lance5057.extradelight.recipe;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.network.NetworkHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup.Provider;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.CraftingHelper;
import javax.annotation.Nullable;

public class FeastRecipe implements Recipe<SimpleRecipeWrapper> {
	protected final String group;
	protected final BlockItem feast;
	protected final Ingredient container;
	protected final ItemStack result;
	final CookingBookCategory category = CookingBookCategory.FOOD;
	protected final ResourceLocation id;

	public Ingredient getContainer() {
		return container;
	}

	public FeastRecipe(ResourceLocation id,String pGroup, BlockItem feast, Ingredient pIngredient, ItemStack pResult) {
		this.id =id;
		this.group = pGroup;
		this.feast = feast;
		this.container = pIngredient;
		this.result = pResult;
	}

	public FeastRecipe(ResourceLocation id,String pGroup, ItemStack feast, Ingredient pIngredient, ItemStack pResult) {
		this.id =id;
		this.group = pGroup;
		this.feast = (BlockItem) feast.getItem();
		this.container = pIngredient;
		this.result = pResult;
	}

	public BlockItem getFeast() {
		return feast;
	}

	public ItemStack getFeastStack() {
		return new ItemStack(feast);
	}

	@Override
	public boolean matches(SimpleRecipeWrapper pContainer, Level pLevel) {
		return this.container.test(pContainer.getItem(1)) && this.feast == pContainer.getItem(0).getItem();
	}

	public static class Serializer implements RecipeSerializer<FeastRecipe> {


		@Override
		public FeastRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			String s = GsonHelper.getAsString(pJson, "group", "");
			Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "container"));
			ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pJson, "result"), false);
			//ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
			ItemStack feast = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "out"));

			return new FeastRecipe(pRecipeId,s, feast, ingredient, result);
		}

		@Override
		public @Nullable FeastRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			ItemStack itemstack = pBuffer.readItem();
            if (pBuffer.readItem().getItem() instanceof BlockItem bi) {
				return new FeastRecipe(pRecipeId,s, bi, ingredient, itemstack);
            }else{
				ExtraDelight.logger.error("error reading feast recipe,cannot find block item");
				return new FeastRecipe(pRecipeId,s, (BlockItem) Blocks.AIR.asItem(), ingredient, itemstack);
			}

		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, FeastRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pRecipe.container.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);
			pBuffer.writeItem(new ItemStack(pRecipe.getFeast()));
		}
	}

	@Override
	public ItemStack assemble(SimpleRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
		return this.result;
	}

	//	@Override
//	public ItemStack assemble(SimpleRecipeWrapper input, Provider registries) {
//		return this.result;
//	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return this.result;
	}

//
//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return this.result;
//	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.FEAST_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.FEAST.get();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
}
