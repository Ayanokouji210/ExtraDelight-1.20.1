package com.lance5057.extradelight.workstations.vat.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.workstations.vat.VatBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.common.util.RecipeMatcher;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class VatRecipe implements Recipe<VatRecipeWrapper> {
//	protected final int cookTime;
//
//	public int getCookTime() {
//		return cookTime;
//	}

	final ItemStack containerItem;

	final String group;
	final ItemStack result;
	final NonNullList<Ingredient> ingredients;
	final NonNullList<StageIngredient> stageIngredients;
	final FluidStack fluid;
	protected final int stages;
	private final ResourceLocation id;

	//protected final int stages;

	public int getStages() {
		return stages;
	}

	public VatRecipe(ItemStack containerItem, String group, ItemStack result, NonNullList<Ingredient> ingredients,
					 NonNullList<StageIngredient> stageIngredients, FluidStack fluid, int stages,
					 ResourceLocation id) {
		this.containerItem = containerItem;
		this.group = group;
		this.result = result;
		this.ingredients = ingredients;
		this.stageIngredients = stageIngredients;
		this.fluid = fluid;
		this.stages = stages;
		this.id = id;
	}

	//	public VatRecipe(String pGroup, NonNullList<Ingredient> pIngredients,
//			NonNullList<StageIngredient> pStageIngredients, SizedFluidIngredient pFluids, ItemStack pResult, int stages,
//			ItemStack usedItem) {
////		this.cookTime = time;
//		this.containerItem = usedItem;
//		this.group = pGroup;
//		this.result = pResult;
//
//		this.ingredients = pIngredients;
//		this.stageIngredients = pStageIngredients;
//		this.fluid = pFluids;
//		this.stages = stages;
//	}

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

	public NonNullList<StageIngredient> getStageIngredients() {
		return this.stageIngredients;
	}

	public FluidStack getFluid() {
		return this.fluid;
	}

	@Override
	public boolean matches(VatRecipeWrapper input, Level level) {
//		StackedContents stackedcontents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < 6; ++j) {
			ItemStack itemstack = input.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
//				if (isSimple)
//					stackedcontents.accountStack(itemstack, 1);
//				else
				inputs.add(itemstack);
			}
		}

		return i == this.ingredients.size() && RecipeMatcher.findMatches(inputs, this.ingredients) != null
				&& fluid.isFluidEqual(input.getTank().getFluid())
				&& ItemStack.isSameItem(containerItem, input.getItem(VatBlockEntity.CONTAINER_SLOT))
				&& input.getItem(VatBlockEntity.CONTAINER_SLOT).getCount() >= containerItem.getCount();
	}

	@Override
	public ItemStack assemble(VatRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */


//
//	public ItemStack assemble(SimpleContainer pInv, RegistryAccess p_267165_) {
//		return this.result.copy();
//	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= this.ingredients.size();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return this.result.copy();
	}

	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.VAT_SERIALIZER.get();
	}

//	public int getTime() {
//		return cookTime;
//	}

	public ItemStack getUsedItem() {
		return this.containerItem;
	}



//	@Override
//	public ItemStack assemble(VatRecipeWrapper input, Provider registries) {
//		return this.result.copy();
//	}
//
//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return this.result;
//	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.VAT.get();
	}

	public static class StageIngredient {
		public static final StageIngredient EMPTY = new StageIngredient(Ingredient.EMPTY, 0, false);
		public Ingredient ingredient;
		public int time;
		public boolean lid;

		public StageIngredient(Ingredient i, int t, boolean l) {
			ingredient = i;
			time = t;
			lid = l;
		}

		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.add("ingredient", ingredient.toJson());
			json.addProperty("time", time);
			json.addProperty("lid", lid);
			return json;
		}

		public static StageIngredient fromJson(JsonObject json) {
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			int time = GsonHelper.getAsInt(json, "time");
			boolean lid = GsonHelper.getAsBoolean(json, "lid");
			return new StageIngredient(ingredient, time, lid);
		}

		public static StageIngredient fromNetwork(FriendlyByteBuf buffer) {
			Ingredient i = Ingredient.fromNetwork(buffer);
			int t = buffer.readVarInt();
			boolean l = buffer.readBoolean();
			return new StageIngredient(i, t, l);
		}

		public static void toNetwork(FriendlyByteBuf buffer, StageIngredient ingredient) {
			ingredient.ingredient.toNetwork(buffer);
			buffer.writeVarInt(ingredient.time);
			buffer.writeBoolean(ingredient.lid);
		}
	}

	public static class Serializer implements RecipeSerializer<VatRecipe> {
		@Override
		public VatRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");

			JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (int i = 0; i < ingredientsArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(ingredientsArray.get(i));
				ingredients.add(ingredient);
			}

			JsonArray stageIngredientsArray = GsonHelper.getAsJsonArray(json, "stage_ingredients");
			NonNullList<StageIngredient> stageIngredients = NonNullList.create();
			for (int i = 0; i < stageIngredientsArray.size(); ++i) {
				StageIngredient stageIngredient = StageIngredient.fromJson(stageIngredientsArray.get(i).getAsJsonObject());
				stageIngredients.add(stageIngredient);
			}

			FluidStack fluid = FluidStack.EMPTY;
			if (json.has("fluids")) {
				JsonObject fluidJson = GsonHelper.getAsJsonObject(json, "fluids");
				// 这里需要根据你的 FluidStack 序列化方式实现
				// fluid = FluidStackUtil.fromJson(fluidJson);
			}

			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int stages = GsonHelper.getAsInt(json, "stages");

			ItemStack containerItem = ItemStack.EMPTY;
			if (json.has("usedItem")) {
				containerItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "usedItem"));
			}

			return new VatRecipe(containerItem, group, result,ingredients, stageIngredients, fluid, stages, recipeId);
		}

		@Override
		public VatRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf();

			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			int x = buffer.readVarInt();
			NonNullList<StageIngredient> stageIngredients = NonNullList.withSize(x, StageIngredient.EMPTY);
			for (int j = 0; j < stageIngredients.size(); ++j) {
				stageIngredients.set(j, StageIngredient.fromNetwork(buffer));
			}

			FluidStack fluid = buffer.readFluidStack();
			ItemStack itemstack = buffer.readItem();
			int stages = buffer.readVarInt();
			ItemStack usedItem = buffer.readItem();

			return new VatRecipe(usedItem,s,itemstack,ingredients,stageIngredients,fluid,stages,recipeId);
			//return new VatRecipe(recipeId, s, ingredients, stageIngredients, fluid, itemstack, stages, usedItem);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, VatRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeVarInt(recipe.stageIngredients.size());

			for (StageIngredient ingredient : recipe.stageIngredients) {
				StageIngredient.toNetwork(buffer, ingredient);
			}

			buffer.writeFluidStack(recipe.getFluid());
			buffer.writeItem(recipe.result);
			buffer.writeVarInt(recipe.getStages());
			buffer.writeItem(recipe.getUsedItem());
		}
	}
	//		public static Codec<StageIngredient> CODEC = RecordCodecBuilder
	//				.create(inst -> inst.group(Ingredient.CODEC.fieldOf("ingredient").forGetter(s -> s.ingredient),
	//						Codec.INT.fieldOf("time").forGetter(s -> s.time),
	//						Codec.BOOL.fieldOf("lid").forGetter(s -> s.lid)).apply(inst, StageIngredient::new));
	//
	//		public static final StreamCodec<RegistryFriendlyByteBuf, StageIngredient> STREAM_CODEC = StreamCodec
	//				.of(StageIngredient::write, StageIngredient::read);
	//
	//		private static StageIngredient read(RegistryFriendlyByteBuf buffer) {
	//			Ingredient i = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
	//			int t = buffer.readVarInt();
	//			boolean l = buffer.readBoolean();
	//			return new StageIngredient(i, t, l);
	//		}
	//
	//		private static void write(RegistryFriendlyByteBuf buffer, StageIngredient r) {
	//
	//			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, r.ingredient);
	//			buffer.writeVarInt(r.time);
	//			buffer.writeBoolean(r.lid);
	//		}
	//	}
	//
	//	public static class Serializer implements RecipeSerializer<VatRecipe> {
	//		private static final MapCodec<VatRecipe> CODEC = RecordCodecBuilder
	//				.mapCodec(inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(VatRecipe::getGroup),
	//
	//						Ingredient.LIST_CODEC.fieldOf("ingredients").xmap(ing -> {
	//							NonNullList<Ingredient> nonNullList = NonNullList.create();
	//							nonNullList.addAll(ing);
	//							return nonNullList;
	//						}, ing -> ing).forGetter(VatRecipe::getIngredients),
	//
	//						Codec.list(StageIngredient.CODEC).fieldOf("stage_ingredients").xmap(ing -> {
	//							NonNullList<StageIngredient> nonNullList = NonNullList.create();
	//							nonNullList.addAll(ing);
	//							return nonNullList;
	//						}, ing -> ing).forGetter(VatRecipe::getStageIngredients),
	//
	//						SizedFluidIngredient.FLAT_CODEC.fieldOf("fluids").forGetter(VatRecipe::getFluid),
	//
	//						ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
	//
	////						Codec.INT.fieldOf("time").forGetter(r -> r.cookTime),
	//						Codec.INT.fieldOf("stages").forGetter(r -> r.stages),
	//
	//						ItemStack.CODEC.optionalFieldOf("usedItem", ItemStack.EMPTY).forGetter(r -> r.containerItem))
	//						.apply(inst, VatRecipe::new));
	//
	//		public static VatRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
	//			String s = pBuffer.readUtf();
	//
	//			int i = pBuffer.readVarInt();
	//			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
	//
	//			for (int j = 0; j < nonnulllist.size(); ++j) {
	//				nonnulllist.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer));
	//			}
	//
	//			int x = pBuffer.readVarInt();
	//			NonNullList<StageIngredient> nonnulllist2 = NonNullList.withSize(x, StageIngredient.EMPTY);
	//
	//			for (int j = 0; j < nonnulllist2.size(); ++j) {
	//				nonnulllist2.set(j, StageIngredient.STREAM_CODEC.decode(pBuffer));
	//			}
	//
	//			SizedFluidIngredient f = SizedFluidIngredient.STREAM_CODEC.decode(pBuffer);
	//
	//			ItemStack itemstack = ItemStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
	////			int stirs = pBuffer.readVarInt();
	//			int stages = pBuffer.readVarInt();
	//			ItemStack usedItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(pBuffer);
	//			return new VatRecipe(s, nonnulllist, nonnulllist2, f, itemstack, stages, usedItem);
	//		}
	//
	//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, VatRecipe pRecipe) {
	//			pBuffer.writeUtf(pRecipe.getGroup());
	//			pBuffer.writeVarInt(pRecipe.getIngredients().size());
	//
	//			for (Ingredient ingredient : pRecipe.getIngredients()) {
	//				Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, ingredient);
	//			}
	//
	//			pBuffer.writeVarInt(pRecipe.stageIngredients.size());
	//
	//			for (StageIngredient ingredient : pRecipe.stageIngredients) {
	//				StageIngredient.STREAM_CODEC.encode(pBuffer, ingredient);
	//			}
	//
	//			SizedFluidIngredient.STREAM_CODEC.encode(pBuffer, pRecipe.getFluid());
	//
	//			ItemStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.result);
	////			pBuffer.writeVarInt(pRecipe.getTime());
	//			pBuffer.writeVarInt(pRecipe.getStages());
	//			ItemStack.OPTIONAL_STREAM_CODEC.encode(pBuffer, pRecipe.getUsedItem());
	//
	//		}
	//
	//		@Override
	//		public MapCodec<VatRecipe> codec() {
	//			// TODO Auto-generated method stub
	//			return CODEC;
	//		}
	//
	//		public static final StreamCodec<RegistryFriendlyByteBuf, VatRecipe> STREAM_CODEC = StreamCodec
	//				.of(Serializer::toNetwork, Serializer::fromNetwork);
	//
	//		@Override
	//		public StreamCodec<RegistryFriendlyByteBuf, VatRecipe> streamCodec() {
	//			// TODO Auto-generated method stub
	//			return STREAM_CODEC;
	//		}
}