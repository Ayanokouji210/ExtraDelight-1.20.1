package com.lance5057.extradelight.workstations.meltingpot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup.Provider;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;

import java.util.Objects;
//import net.neoforged.neoforge.fluids.FluidStack;

public class MeltingPotRecipe implements Recipe<Container> {
	ResourceLocation id;
	final String group;
	public Ingredient input;
	public int cooktime;
	public FluidStack result;

	public MeltingPotRecipe(ResourceLocation id,Ingredient in, int time, FluidStack out, String group) {
		this.id = id;
		this.group = group;
		this.input = in;
		this.cooktime = time;
		this.result = out;
	}

	@Override
	public boolean matches(Container input, Level level) {
		for (int i = 0; i < input.getContainerSize(); i++) {
			if (this.input.test(input.getItem(i))) {
				return true;
			}
		}
		return false;
	}


	public boolean matches(ItemStack input) {

        return this.input.test(input);
    }

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return new ItemStack(Items.STICK);
	}

//	@Override
//	public ItemStack assemble(Container input, Provider registries) {
//		return new ItemStack(Items.STICK); // Because other mods expect something regardless
//	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return new ItemStack(Items.STICK);
	}

//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return new ItemStack(Items.STICK); // Because other mods expect something regardless
//	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.MELTING_POT_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.MELTING_POT.get();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(this.input);
		return ingredients;
	}

	public static class Serializer implements RecipeSerializer<MeltingPotRecipe>{

		@Override
		public MeltingPotRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
			String group = GsonHelper.getAsString(jsonObject, "group", "");
			Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			int cooktime = GsonHelper.getAsInt(jsonObject, "cooktime", 100);

//			JsonObject resultJson = GsonHelper.getAsJsonObject(jsonObject, "result");
//			FluidStack resultFluid = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(
//                    new ResourceLocation(GsonHelper.getAsString(resultJson, "id"))))
//					,GsonHelper.getAsInt(jsonObject,"amount"));
			FluidStack resultFluid = StackUtil.FluidStackfromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
			return new MeltingPotRecipe(resourceLocation,input, cooktime, resultFluid, group);

		}

		@Override
		public @Nullable MeltingPotRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
			String group = friendlyByteBuf.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
			int cooktime = friendlyByteBuf.readVarInt();
			FluidStack fluid =FluidStack.readFromPacket(friendlyByteBuf);



			return new MeltingPotRecipe(resourceLocation, ingredient, cooktime, fluid, group);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, MeltingPotRecipe meltingPotRecipe) {
			friendlyByteBuf.writeUtf(meltingPotRecipe.group);
			meltingPotRecipe.input.toNetwork(friendlyByteBuf);
			friendlyByteBuf.writeVarInt(meltingPotRecipe.cooktime);
			meltingPotRecipe.result.writeToPacket(friendlyByteBuf);

		}
	}

//	public static class Serializer implements RecipeSerializer<MeltingPotRecipe> {
//		private static final MapCodec<MeltingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst
//				.group(Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.input),
//						Codec.INT.optionalFieldOf("cooktime", 100).forGetter(r -> r.cooktime),
//						FluidStack.CODEC.fieldOf("result").forGetter(r -> r.result),
//						Codec.STRING.optionalFieldOf("group", "").forGetter(MeltingPotRecipe::getGroup))
//
//				.apply(inst, MeltingPotRecipe::new));
//
//		public static MeltingPotRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
//
//			int cooktime = pBuffer.readVarInt();
//			FluidStack usedItem = FluidStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
//			return new MeltingPotRecipe(ingredient, cooktime, usedItem, s);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, MeltingPotRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.group);
//			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
//			pBuffer.writeVarInt(pRecipe.cooktime);
//			FluidStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.result);
//
//		}
//
//		@Override
//		public MapCodec<MeltingPotRecipe> codec() {
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, MeltingPotRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, MeltingPotRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}
//	}
}
