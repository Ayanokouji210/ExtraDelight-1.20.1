package com.lance5057.extradelight.recipe;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;

import com.lance5057.extradelight.util.StackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;

public class BottleFluidRegistryRecipe implements Recipe<SimpleRecipeWrapper> {
	final String group;
	public Ingredient bottle;
	private final ResourceLocation id;

	public Ingredient getBottle() {
		return bottle;
	}

	public FluidStack getFluid() {
		return fluid;
	}

	public FluidStack fluid;

	public BottleFluidRegistryRecipe(ResourceLocation id,String group, Ingredient b, FluidStack f) {
		this.group = group;
		this.fluid = f;
		this.bottle = b;
		this.id = id;
	}

	@Override
	public boolean matches(SimpleRecipeWrapper input, Level level) {
		return false;
	}

	@Override
	public ItemStack assemble(SimpleRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
	}

//	@Override
//	public ItemStack assemble(SimpleRecipeWrapper input, Provider registries) {
//		return ItemStack.EMPTY;
//	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.BOTTLE_FLUID_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.BOTTLE_FLUID_REGISTRY.get();
	}

	public static class Serializer implements RecipeSerializer<BottleFluidRegistryRecipe> {
//		private static final MapCodec<BottleFluidRegistryRecipe> CODEC = RecordCodecBuilder
//				.mapCodec(inst -> inst
//						.group(Codec.STRING.optionalFieldOf("group", "").forGetter(BottleFluidRegistryRecipe::getGroup),
//								Ingredient.CODEC.fieldOf("bottle").forGetter(BottleFluidRegistryRecipe::getBottle),
//								FluidIngredient.fromFluid().fieldOf("fluid")
//										.forGetter(BottleFluidRegistryRecipe::getFluid))
//						.apply(inst, BottleFluidRegistryRecipe::new));

//		public static BottleFluidRegistryRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(pBuffer);
//			Ingredient i = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
//
//			return new BottleFluidRegistryRecipe(s, i, fluid);
//		}

//		public static void toNetwork(FriendlyByteBuf buffer, BottleFluidRegistryRecipe pRecipe) {
////			pBuffer.writeUtf(pRecipe.group);
////			SizedFluidIngredient.STREAM_CODEC.encode(pBuffer, pRecipe.fluid);
////			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.bottle);
//			String group = buffer.readUtf();
//			Ingredient bottle = Ingredient.fromNetwork(buffer);
//			FluidStack fluid = FluidStack.readFromPacket(buffer);
//
//			return new BottleFluidRegistryRecipe(group, bottle, fluid);
//		}


		@Override
		public BottleFluidRegistryRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");

			Ingredient bottle = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "bottle"));
			FluidStack fluid = StackUtil.FluidStackfromJson(GsonHelper.getAsJsonObject(json, "fluid"));
			return new BottleFluidRegistryRecipe(pRecipeId,group, bottle, fluid);
		}

		@Override
		public @Nullable BottleFluidRegistryRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			Ingredient bottle = Ingredient.fromNetwork(buffer);
			FluidStack fluid = FluidStack.readFromPacket(buffer);

			return new BottleFluidRegistryRecipe(pRecipeId,group, bottle,fluid);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BottleFluidRegistryRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.bottle.toNetwork(buffer);
			recipe.fluid.writeToPacket(buffer);
		}
	}
}
