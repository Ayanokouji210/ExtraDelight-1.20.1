package com.lance5057.extradelight.workstations.chiller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.common.util.RecipeMatcher;
//import net.neoforged.neoforge.fluids.FluidStack;

public class ChillerRecipe implements Recipe<ChillerRecipeWrapper> {
	public static final int INPUT_SLOTS = 4;

	private final String group;
//	private final ChillerRecipeBookTab tab;
	private final NonNullList<Ingredient> inputItems;
	private final FluidStack fluid;
	public final ItemStack output;
	private final ItemStack container;
	private final float experience;
	private final int cookTime;
	public final boolean consumeContainer;
	private final ResourceLocation id;

	public ChillerRecipe(ResourceLocation id, String group, NonNullList<Ingredient> inputItems, FluidStack inputFluid, ItemStack output,
			ItemStack container, float experience, int cookTime) {
		this.id=id;
		this.group = group;
		this.fluid = inputFluid;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		} else {
			this.container = ItemStack.EMPTY;
		}

		this.experience = experience;
		this.cookTime = cookTime;
		this.consumeContainer = false;
	}

	public ChillerRecipe(ResourceLocation id,String group, NonNullList<Ingredient> inputItems, FluidStack inputFluid, ItemStack output,
			ItemStack container, float experience, int cookTime, boolean consumeContainer) {
		this.id=id;
		this.group = group;
		this.fluid = inputFluid;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		} else {
			this.container = ItemStack.EMPTY;
		}

		this.experience = experience;
		this.cookTime = cookTime;
		this.consumeContainer = consumeContainer;
	}

	public ItemStack getContainerOverride() {
		return this.container;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

//	@Nullable
//	public ChillerRecipeBookTab getRecipeBookTab() {
//		return this.tab;
//	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputItems;
	}

	public ItemStack getOutput() {
		return this.output.copy();
	}

//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return this.output.copy();
//	}

	public ItemStack getOutputContainer() {
		return this.container;
	}

//	@Override
//	public ItemStack assemble(ChillerRecipeWrapper input, Provider registries) {
//		return this.output.copy();
//	}


	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public boolean shouldConsumeContainer() {
		return this.consumeContainer;
	}

	@Override
	public boolean matches(ChillerRecipeWrapper inv, Level level) {
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < INPUT_SLOTS; ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				inputs.add(itemstack);
			}
		}

		int[] matches = RecipeMatcher.findMatches(inputs, this.inputItems);
		return i == this.inputItems.size() && matches != null
				&& inv.getItem(INPUT_SLOTS + 1).getItem() == this.container.getItem()
				&& inv.tank.containsFluid(this.fluid);
	}

	@Override
	public ItemStack assemble(ChillerRecipeWrapper chillerRecipeWrapper, RegistryAccess registryAccess) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputItems.size();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.output.copy();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.CHILLER_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.CHILLER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ExtraDelightItems.CHILLER.get());
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	public FluidStack getFluid() {
		return fluid;
	}

	public static class Serializer implements RecipeSerializer<ChillerRecipe> {
		public Serializer() {
		}

		@Override
		public ChillerRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {

			String s = GsonHelper.getAsString(jsonObject,"group","");
			Boolean consume = GsonHelper.getAsBoolean(jsonObject,"consumeContainer",false);

			ItemStack container =StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject,"container"));

			int cookTime = GsonHelper.getAsInt(jsonObject,"cooktime",200);
			float experience = GsonHelper.getAsFloat(jsonObject,"experience",0.0f);

			FluidStack fluid = StackUtil.FluidStackfromJson(GsonHelper.getAsJsonObject(jsonObject,"fluid"));

			NonNullList<Ingredient> inputItems = NonNullList.create();
			JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject,"ingredients");
			for(int i = 0; i < jsonArray.size(); i++) {
				inputItems.add(Ingredient.fromJson(jsonArray.get(i)));
			}

			ItemStack result =StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject,"result"));

			return new ChillerRecipe(resourceLocation,s,inputItems,fluid,result,container,experience,cookTime,consume);
		}

		@Override
		public @Nullable ChillerRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
			String s= friendlyByteBuf.readUtf();
			int index = friendlyByteBuf.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for(int i =0; i<index; i++){
				ingredients.add(Ingredient.fromNetwork(friendlyByteBuf));
			}
			FluidStack fluid=friendlyByteBuf.readFluidStack();
			ItemStack output = friendlyByteBuf.readItem();
			ItemStack container = friendlyByteBuf.readItem();
			float experience = friendlyByteBuf.readFloat();
			int cookTime = friendlyByteBuf.readVarInt();
			boolean comsume = friendlyByteBuf.readBoolean();


			return new ChillerRecipe(resourceLocation,s,ingredients,fluid,output,container,experience,cookTime,comsume);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, ChillerRecipe chillerRecipe) {
			friendlyByteBuf.writeUtf(chillerRecipe.group);
			friendlyByteBuf.writeVarInt(chillerRecipe.inputItems.size());
			for(int i=0;i<chillerRecipe.inputItems.size();i++) {
				chillerRecipe.inputItems.get(i).toNetwork(friendlyByteBuf);
			}
			friendlyByteBuf.writeFluidStack(chillerRecipe.fluid);
			friendlyByteBuf.writeItem(chillerRecipe.output);
			friendlyByteBuf.writeItem(chillerRecipe.container);
			friendlyByteBuf.writeFloat(chillerRecipe.experience);
			friendlyByteBuf.writeVarInt(chillerRecipe.cookTime);
			friendlyByteBuf.writeBoolean(chillerRecipe.consumeContainer);

		}


//		private static final MapCodec<ChillerRecipe> CODEC = RecordCodecBuilder.mapCodec(
//				inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(ChillerRecipe::getGroup),
////				ChillerRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab")
////						.xmap(optional -> optional.orElse(null), Optional::of).forGetter(ChillerRecipe::getRecipeBookTab),
//						Ingredient.LIST_CODEC.fieldOf("ingredients").xmap(ingredients -> {
//							NonNullList<Ingredient> nonNullList = NonNullList.create();
//							nonNullList.addAll(ingredients);
//							return nonNullList;
//						}, ingredients -> ingredients).forGetter(ChillerRecipe::getIngredients),
//						FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(r -> r.fluid),
//						ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
//
//						ItemStack.CODEC.lenientOptionalFieldOf("container", ItemStack.EMPTY)
//								.forGetter(ChillerRecipe::getContainerOverride),
//						Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(ChillerRecipe::getExperience),
//						Codec.INT.lenientOptionalFieldOf("cookingtime", 200).forGetter(ChillerRecipe::getCookTime),
//						Codec.BOOL.fieldOf("consumeContainer").forGetter(ChillerRecipe::shouldConsumeContainer)
//
//				).apply(inst, ChillerRecipe::new));
//
//		public static ChillerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
//			String groupIn = buffer.readUtf();
////			ChillerRecipeBookTab tabIn = ChillerRecipeBookTab.findByName(buffer.readUtf());
//			int i = buffer.readVarInt();
//			NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);
//
//			for (int j = 0; j < inputItemsIn.size(); ++j) {
//				inputItemsIn.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
//			}
//			FluidStack fluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
//
//			ItemStack outputIn = ItemStack.STREAM_CODEC.decode(buffer);
//			ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
//			float experienceIn = buffer.readFloat();
//			int cookTimeIn = buffer.readVarInt();
//			boolean consumeContainer = buffer.readBoolean();
//			return new ChillerRecipe(groupIn, /* tabIn, */ inputItemsIn, fluid, outputIn, container, experienceIn,
//					cookTimeIn, consumeContainer);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf buffer, ChillerRecipe recipe) {
//			buffer.writeUtf(recipe.group);
////			buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
//			buffer.writeVarInt(recipe.inputItems.size());
//
//			for (Ingredient ingredient : recipe.inputItems) {
//				Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
//			}
//
//			FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluid);
//
//			ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
//			ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.container);
//			buffer.writeFloat(recipe.experience);
//			buffer.writeVarInt(recipe.cookTime);
//			buffer.writeBoolean(recipe.consumeContainer);
//		}
//
//		@Override
//		public MapCodec<ChillerRecipe> codec() {
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, ChillerRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, ChillerRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}

	}

}
