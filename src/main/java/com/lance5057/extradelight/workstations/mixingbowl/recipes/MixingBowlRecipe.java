package com.lance5057.extradelight.workstations.mixingbowl.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.common.util.RecipeMatcher;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;

public class MixingBowlRecipe implements Recipe<MixingBowlRecipeWrapper> {
	final ResourceLocation id;
	protected final int stirs;
    final ItemStack container;
    final Ingredient utensil;

	final String group;
	final ItemStack result;
	final NonNullList<Ingredient> ingredients;
	final List<FluidIngredient> fluids;
//	private final boolean isSimple;

	public MixingBowlRecipe(ResourceLocation id,String pGroup, NonNullList<Ingredient> pIngredients, List<FluidIngredient> pFluids,
			ItemStack pResult, int stirs, ItemStack container,Ingredient utensil) {
		this.id = id;
		this.stirs = stirs;
        this.container = container;
        this.utensil = utensil;
		this.group = pGroup;
		this.result = pResult;

		this.ingredients = pIngredients;
		this.fluids = pFluids;
	}

	public String getGroup() {
		return this.group;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	public List<FluidIngredient> getFluids() {
		return this.fluids;
	}

	@Override
	public boolean matches(MixingBowlRecipeWrapper input, Level level) {
//		StackedContents stackedcontents = new StackedContents();
		List<ItemStack> inputs = new ArrayList<>();
		int i = 0;

		for (int j = 0; j < 9; ++j) {
			ItemStack itemstack = input.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
//				if (isSimple)
//					stackedcontents.accountStack(itemstack, 1);
//				else
				inputs.add(itemstack);
			}
		}
		
		boolean itemMatchFlag = (this.ingredients == null || this.ingredients.isEmpty() || ItemStack.isSameItem(this.ingredients.get(0).getItems()[0],ItemStack.EMPTY)) ? // some failsafes
				i == 0 
				: i == this.ingredients.size() && RecipeMatcher.findMatches(inputs, this.ingredients) != null;
		return itemMatchFlag
				&& matchFluids(input.getTank().getAsList()) && ItemStack.isSameItem(container, input.getItem(9))
				&& input.getItem(9).getCount() >= container.getCount();
	}

	boolean matchFluids(List<FluidStack> f) {
//		if (this.fluids.size() < f.size())
//			return false;

		int count = 0;
		for (int i = 0; i < fluids.size(); i++) {
			for (int j = 0; j < f.size(); j++) {
				FluidIngredient f1 = fluids.get(i);
				FluidStack f2 = f.get(j);

				if (f1.test(f2)) {
					count++;
				}
			}
		}

		if (count == fluids.size())
			return true;
		return false;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack assemble(SimpleContainer pInv, RegistryAccess p_267165_) {
		return this.result.copy();
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= this.ingredients.size();
	}

	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.MIXING_BOWL_SERIALIZER.get();
	}

	public int getStirs() {
		return stirs;
	}

    public ItemStack getContainer() {
        return this.container;
    }

    public Ingredient getUtensil() {
        return this.utensil;
    }

	@Override
	public ItemStack assemble(MixingBowlRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

//	@Override
//	public ItemStack assemble(MixingBowlRecipeWrapper input, Provider registries) {
//		return this.result.copy();
//	}


	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return this.result;
//	}

	@Override
	public RecipeType<?> getType() {
		// TODO Auto-generated method stub
		return ExtraDelightRecipes.MIXING_BOWL.get();
	}

	public static class Serializer implements RecipeSerializer<MixingBowlRecipe> {
		@Override
		public MixingBowlRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "group", "");

			NonNullList<Ingredient> pIngredients = NonNullList.create();
			JsonArray ingredientJson = GsonHelper.getAsJsonArray(jsonObject,"ingredients");
			for (int i = 0; i < ingredientJson.size(); i++) {
				pIngredients.add(Ingredient.fromJson(ingredientJson.get(i)));
			}

			JsonArray fluidJson = GsonHelper.getAsJsonArray(jsonObject,"fluids");
			List<FluidIngredient> pFluids = new ArrayList<>();
			for (int j = 0; j < fluidJson.size(); j++) {
				pFluids.add(FluidIngredient.deserialize(fluidJson.get(j)));
			}

			ItemStack result = CraftingHelper.getItemStack(
					GsonHelper.getAsJsonObject(jsonObject,"result"), false);

			int stirs=GsonHelper.getAsInt(jsonObject, "stirs", 4);

			ItemStack container = StackUtil.ItemStackfromJson(
					GsonHelper.getAsJsonObject(jsonObject,"container"));

            Ingredient utensil = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "utensil"));

            return new MixingBowlRecipe(resourceLocation,s,pIngredients,pFluids,result,stirs,container,utensil);
		}

		@Override
		public @Nullable MixingBowlRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
			String s = friendlyByteBuf.readUtf();
			int IngredientSize = friendlyByteBuf.readVarInt();
			NonNullList<Ingredient> pIngredients = NonNullList.create();
			for(int i=0;i<IngredientSize;i++) {
				pIngredients.add(Ingredient.fromNetwork(friendlyByteBuf));
			}
			int FluidSize = friendlyByteBuf.readVarInt();
			List<FluidIngredient> pFluids = NonNullList.create();
			for(int i=0;i<FluidSize;i++) {
				pFluids.add(FluidIngredient.read(friendlyByteBuf));
			}
			ItemStack result = friendlyByteBuf.readItem();
			int stirs=friendlyByteBuf.readVarInt();
			ItemStack container = friendlyByteBuf.readItem();

            Ingredient utensil = Ingredient.fromNetwork(friendlyByteBuf);

            return new MixingBowlRecipe(resourceLocation,s,pIngredients,pFluids,result,stirs,container,utensil);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, MixingBowlRecipe mixingBowlRecipe) {
			friendlyByteBuf.writeUtf(mixingBowlRecipe.getGroup());
			friendlyByteBuf.writeVarInt(mixingBowlRecipe.ingredients.size());
			for (int i = 0; i < mixingBowlRecipe.ingredients.size(); i++) {
				mixingBowlRecipe.ingredients.get(i).toNetwork(friendlyByteBuf);
			}
			friendlyByteBuf.writeVarInt(mixingBowlRecipe.fluids.size());
			for (int i = 0; i < mixingBowlRecipe.fluids.size(); i++) {
				mixingBowlRecipe.fluids.get(i).write(friendlyByteBuf);
			}
			friendlyByteBuf.writeItemStack(mixingBowlRecipe.result,true);
			friendlyByteBuf.writeVarInt(mixingBowlRecipe.stirs);
			friendlyByteBuf.writeItem(mixingBowlRecipe.container);
            mixingBowlRecipe.utensil.toNetwork(friendlyByteBuf);


		}
//		private static final MapCodec<MixingBowlRecipe> CODEC = RecordCodecBuilder.mapCodec(
//				inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(MixingBowlRecipe::getGroup),
//
//						Ingredient.LIST_CODEC.fieldOf("ingredients").xmap(ing -> {
//							NonNullList<Ingredient> nonNullList = NonNullList.create();
//							if(!ing.isEmpty())nonNullList.addAll(ing);
//							return nonNullList;
//						}, ing -> ing).forGetter(MixingBowlRecipe::getIngredients),
//
//						SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluids")
//								.forGetter(MixingBowlRecipe::getFluids),
//
//						ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
//
//						Codec.INT.optionalFieldOf("stirs", 100).forGetter(r -> r.stirs),
//
//						ItemStack.CODEC.optionalFieldOf("usedItem", ItemStack.EMPTY).forGetter(r -> r.usedItem))
//						.apply(inst, MixingBowlRecipe::new));
//
//		public static MixingBowlRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			int i = pBuffer.readVarInt();
//			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
//
//			for (int j = 0; j < nonnulllist.size(); ++j) {
//				nonnulllist.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer));
//			}
//
//			int f = pBuffer.readVarInt();
//			List<SizedFluidIngredient> fl = new ArrayList<SizedFluidIngredient>();
//
//			for (int h = 0; h < f; ++h) {
//				fl.add(h, SizedFluidIngredient.STREAM_CODEC.decode(pBuffer));
//			}
//
//			ItemStack itemstack = ItemStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
//			int stirs = pBuffer.readVarInt();
//			ItemStack usedItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
//			return new MixingBowlRecipe(s, nonnulllist, fl, itemstack, stirs, usedItem);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, MixingBowlRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.getGroup());
//			pBuffer.writeVarInt(pRecipe.getIngredients().size());
//
//			for (Ingredient ingredient : pRecipe.getIngredients()) {
//				Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, ingredient);
//			}
//
//			pBuffer.writeVarInt(pRecipe.getFluids().size());
//
//			for (SizedFluidIngredient f : pRecipe.getFluids()) {
//				SizedFluidIngredient.STREAM_CODEC.encode(pBuffer, f);
//			}
//
//			ItemStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.result);
//			pBuffer.writeVarInt(pRecipe.getStirs());
//			ItemStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.getUsedItem());
//
//		}
//
//		@Override
//		public MapCodec<MixingBowlRecipe> codec() {
//			// TODO Auto-generated method stub
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, MixingBowlRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, MixingBowlRecipe> streamCodec() {
//			// TODO Auto-generated method stub
//			return STREAM_CODEC;
//		}
	}

}