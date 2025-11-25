package com.lance5057.extradelight.workstations.mortar.recipes;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;

public class MortarRecipe implements Recipe<Container> {
	private final ResourceLocation id;
	private final String group;
	private final Ingredient ingredient;
	private final ItemStack result;
	protected final int grinds;
	protected final FluidStack fluidOut;

	public MortarRecipe(ResourceLocation id, String group, Ingredient ingredient,
						ItemStack result, int grinds, FluidStack fluidOut) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = result;
		this.grinds = grinds;
		this.fluidOut = fluidOut;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public ItemStack getResult() {
		return result;
	}

	public FluidStack getFluidOut() {
		return fluidOut;
	}

	@Override
	public String getGroup() {
		return group;
	}

	public int getGrinds() {
		return grinds;
	}

	public FluidStack getFluid() {
		return fluidOut;
	}

	@Override
	public boolean matches(Container input, Level level) {
		return this.ingredient.test(input.getItem(0));
	}

	@Override
	public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ExtraDelightBlocks.MORTAR_STONE.get());
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.MORTAR_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.MORTAR.get();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

    public boolean matches(ItemStack itemstack) {
		return this.ingredient.test(itemstack);
    }

    public static class Serializer implements RecipeSerializer<MortarRecipe> {


		@Override
		public MortarRecipe fromJson(ResourceLocation pRecipeId, JsonObject pjson) {
			String group = GsonHelper.getAsString(pjson, "group", "");
			FluidStack fluidOut = StackUtil.FluidStackfromJson(GsonHelper.getAsJsonObject(pjson, "fluidOut"));
			int grinds = GsonHelper.getAsInt(pjson, "grinds", 4);

			Ingredient ingredient = Ingredient.fromJson(pjson.get("ingredient"));

			ItemStack result = StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(pjson, "result"));


			return new MortarRecipe(pRecipeId,group,ingredient,result,grinds,fluidOut);
		}

		@Override
		public @Nullable MortarRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			FluidStack fluidOut = pBuffer.readFluidStack();
			int grinds = pBuffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			ItemStack result = pBuffer.readItem();

			return new MortarRecipe(pRecipeId,s,ingredient,result,grinds,fluidOut);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, MortarRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pBuffer.writeFluidStack(pRecipe.fluidOut);
			pBuffer.writeInt(pRecipe.grinds);
			pRecipe.ingredient.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);

		}
	}
//		private static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder
//				.mapCodec(inst -> inst
//						.group(Codec.STRING.optionalFieldOf("group", "").forGetter(MortarRecipe::getGroup),
//
//								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient")
//										.forGetter(p_301068_ -> p_301068_.ingredient),
//
//								ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(r -> r.result),
//								FluidStack.OPTIONAL_CODEC.fieldOf("fluidOut").forGetter(MortarRecipe::getFluid),
//								Codec.INT.optionalFieldOf("grinds", 200).forGetter(MortarRecipe::getGrinds))
//						.apply(inst, MortarRecipe::new));
//
//		public static MortarRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
//			ItemStack itemstack = ItemStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
//			FluidStack fluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
//			int g = pBuffer.readInt();
//			return new MortarRecipe(s, ingredient, itemstack, fluid, g);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, MortarRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.group);
//			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.ingredient);
//			ItemStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.result);
//			FluidStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.fluidOut);
//			pBuffer.writeInt(pRecipe.grinds);
//		}
//
//		@Override
//		public MapCodec<MortarRecipe> codec() {
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}
//	}
}