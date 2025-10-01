package com.lance5057.extradelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShapedWithJarRecipe extends ShapedRecipe {

	private final List<FluidStack> fluids;

	public ShapedWithJarRecipe(ResourceLocation pId, String pGroup, CraftingBookCategory pCategory,
							   int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems,
							   List<FluidStack> fluids, ItemStack pResult, boolean pShowNotification) {
		super(pId, pGroup, pCategory, pWidth, pHeight, pRecipeItems, pResult, pShowNotification);
		this.fluids = fluids;
	}

	public ShapedWithJarRecipe(ResourceLocation pId, String pGroup, CraftingBookCategory pCategory,
							   int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems,
							   List<FluidStack> fluids, ItemStack pResult) {
		this(pId, pGroup, pCategory, pWidth, pHeight, pRecipeItems, fluids, pResult, true);
	}

	@Override
	public boolean matches(CraftingContainer input, Level level) {
		if (!this.matches(input, level))
			return false;

		List<ItemStack> jars = gatherJars(input);
		if (jars.size() != fluids.size())
			return false;

		if (!testJars(jars))
			return false;

		return true;
	}

	private List<ItemStack> gatherJars(CraftingContainer  input) {
		List<ItemStack> jars = new ArrayList<>();
		for(int i = 0; i < input.getContainerSize(); ++i) {
			ItemStack item = input.getItem(i);
			if (!item.isEmpty() && item.getItem() == ExtraDelightItems.JAR.get()) {
				jars.add(item);
			}
		}

		return jars;
	}

	private boolean testJars(List<ItemStack> jars) {
//		List<FluidStack> jarFluids = jars.stream()
//				.map(j -> j.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).getFluidInTank(0)).toList();
//		for (int i = 0; i < fluids.size(); i++) {
//			if (!compareFluid(jarFluids, i))
//				return false;
//		}
		List<FluidStack> jarFluids = new ArrayList<>();
		for (ItemStack jar : jars) {
			IFluidHandlerItem fluidHandler = jar.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
			if (fluidHandler != null) {
				jarFluids.add(fluidHandler.getFluidInTank(0));
			}
		}

		for (FluidStack requiredFluid : fluids) {
			boolean found = false;
			for (FluidStack jarFluid : jarFluids) {
				if (jarFluid.isFluidEqual(requiredFluid) && jarFluid.getAmount() >= requiredFluid.getAmount()) {
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return true;
	}

	private boolean compareFluid(List<FluidStack> jarFluids, int i) {
		return jarFluids.stream().anyMatch(j -> j.containsFluid(fluids.get(i)));
	}


	@Override
	public ItemStack assemble(CraftingContainer input,RegistryAccess pRegistryAccess) {
//		List<ItemStack> jars = gatherJars(input);
//
//		for (FluidStack f : fluids) {
//			ItemStack i = jars.stream()
//					.filter(j -> j.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).getFluidInTank(0).containsFluid(f))
//					.findFirst().get();
//			i.getCapability(Capabilities.FluidHandler.ITEM).drain(f, IFluidHandler.FluidAction.EXECUTE);
//		}
//
//		return this.getResultItem(registries).copy();

		List<ItemStack> jars = gatherJars(input);

		for (FluidStack requiredFluid : fluids) {
			for (ItemStack jar : jars) {
				IFluidHandlerItem fluidHandler = jar.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
				if (fluidHandler != null) {
					FluidStack jarFluid = fluidHandler.getFluidInTank(0);
					if (jarFluid.isFluidEqual(requiredFluid) && jarFluid.getAmount() >= requiredFluid.getAmount()) {
						fluidHandler.drain(requiredFluid, IFluidHandler.FluidAction.EXECUTE);
						break;
					}
				}
			}
		}

		return super.assemble(input, pRegistryAccess);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.SHAPED_JAR_SERIALIZER.get();
	}

//	@Override
//	public RecipeType<?> getType() {
//		return ExtraDelightRecipes.SHAPED_JAR.get();
//	}

//	public static class Serializer implements RecipeSerializer<ShapedWithJarRecipe> {
//		public static final MapCodec<ShapedWithJarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
//				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.getGroup()),
//						CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC)
//								.forGetter(p_311732_ -> p_311732_.category()),
//						ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
//						FluidStack.OPTIONAL_CODEC.listOf().fieldOf("fluids").forGetter(recipe -> recipe.fluids),
//						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.getResultItem(null)),
//						Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true))
//								.forGetter(p_311731_ -> p_311731_.showNotification()))
//				.apply(instance, ShapedWithJarRecipe::new));
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, ShapedWithJarRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public MapCodec<ShapedWithJarRecipe> codec() {
//			return CODEC;
//		}
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, ShapedWithJarRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}
//
//		private static ShapedWithJarRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
//			ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.streamCodec().decode(buffer);
//			int f = buffer.readVarInt();
//			List<FluidStack> fluids = new ArrayList<FluidStack>();
//			for (int i = 0; i < f; i++) {
//				FluidStack op = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
//				if (op != null)
//					fluids.add(op);
//			}
//
//			return new ShapedWithJarRecipe(shapedRecipe.getGroup(), shapedRecipe.category(), shapedRecipe.pattern,
//					fluids, shapedRecipe.getResultItem(null), shapedRecipe.showNotification());
//		}
//
//		private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapedWithJarRecipe recipe) {
//			RecipeSerializer.SHAPED_RECIPE.streamCodec().encode(buffer, recipe);
//			buffer.writeVarInt(recipe.fluids.size());
//			for (int i = 0; i < recipe.fluids.size(); i++) {
//				FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluids.get(i));
//			}
//		}
//	}
//	@Override
//	public RecipeSerializer<?> getSerializer() {
//		return ExtraDelightRecipes.SHAPED_JAR_SERIALIZER.get();
//	}

	public static class Serializer implements RecipeSerializer<ShapedWithJarRecipe> {
		@Override
		public ShapedWithJarRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");
			CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);

			// 读取图案和材料
			Map<String, Ingredient> key = keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
			String[] pattern = shrink(patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
			int width = pattern[0].length();
			int height = pattern.length;

			NonNullList<Ingredient> ingredients = dissolvePattern(pattern, key, width, height);

			// 读取流体
			JsonArray fluidsArray = GsonHelper.getAsJsonArray(json, "fluids");
			List<FluidStack> fluids = new ArrayList<>();
			for (int i = 0; i < fluidsArray.size(); i++) {
				JsonObject fluidObj = fluidsArray.get(i).getAsJsonObject();
				FluidStack fluidStack = this.getFluidStack(fluidObj);
				fluids.add(fluidStack);
			}

			// 读取结果
			ItemStack result = itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			boolean showNotification = GsonHelper.getAsBoolean(json, "show_notification", true);

			return new ShapedWithJarRecipe(recipeId, group, category, width, height, ingredients, fluids, result, showNotification);
		}

		private FluidStack getFluidStack(JsonObject json) {
			String fluidName = GsonHelper.getAsString(json, "fluid");
			int amount = GsonHelper.getAsInt(json, "amount", 1000); // 默认 1000mb (1 bucket)

			// 从 Forge 注册表获取流体
			ResourceLocation fluidId = new ResourceLocation(fluidName);
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);

			if (fluid == null) {
				throw new JsonSyntaxException("Unknown fluid: " + fluidName);
			}

			// 可选：读取 NBT 数据
			CompoundTag nbt = null;
			if (json.has("nbt")) {
				try {
					nbt = TagParser.parseTag(GsonHelper.convertToString(json.get("nbt"), "nbt"));
				} catch (CommandSyntaxException e) {
					throw new JsonSyntaxException("Invalid NBT for fluid: " + fluidName);
				}
			}

			FluidStack stack = new FluidStack(fluid, amount);
			if (nbt != null) {
				stack.setTag(nbt);
			}

			return stack;
		}
		@Override
		public ShapedWithJarRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int width = buffer.readVarInt();
			int height = buffer.readVarInt();
			String group = buffer.readUtf();
			CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);

			NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
			for (int i = 0; i < ingredients.size(); ++i) {
				ingredients.add(Ingredient.fromNetwork(buffer));
			}

			// 读取流体
			int fluidCount = buffer.readVarInt();
			List<FluidStack> fluids = new ArrayList<>();
			for (int i = 0; i <= fluidCount; i++) {
				fluids.add(FluidStack.readFromPacket(buffer));
			}

			ItemStack result = buffer.readItem();
			boolean showNotification = buffer.readBoolean();

			return new ShapedWithJarRecipe(recipeId, group, category, width, height, ingredients, fluids, result, showNotification);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapedWithJarRecipe recipe) {
			buffer.writeVarInt(recipe.getWidth());
			buffer.writeVarInt(recipe.getHeight());
			buffer.writeUtf(recipe.getGroup());
			buffer.writeEnum(recipe.category());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			// 写入流体
			buffer.writeVarInt(recipe.fluids.size());
			for (FluidStack fluid : recipe.fluids) {
				fluid.writeToPacket(buffer);
			}

			buffer.writeItem(recipe.getResultItem(null));
			buffer.writeBoolean(recipe.showNotification());
		}

		// 以下是从ShapedRecipe复制的辅助方法
		private static String[] patternFromJson(JsonArray pPatternArray) {
			String[] astring = new String[pPatternArray.size()];
			if (astring.length > MAX_HEIGHT) {
				throw new JsonParseException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
			} else if (astring.length == 0) {
				throw new JsonParseException("Invalid pattern: empty pattern not allowed");
			} else {
				for(int i = 0; i < astring.length; ++i) {
					String s = GsonHelper.convertToString(pPatternArray.get(i), "pattern[" + i + "]");
					if (s.length() > MAX_WIDTH) {
						throw new JsonParseException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
					}

					if (i > 0 && astring[0].length() != s.length()) {
						throw new JsonParseException("Invalid pattern: each row must be the same width");
					}

					astring[i] = s;
				}

				return astring;
			}
		}

		private static String[] shrink(String... pToShrink) {
			int i = Integer.MAX_VALUE;
			int j = 0;
			int k = 0;
			int l = 0;

			for(int i1 = 0; i1 < pToShrink.length; ++i1) {
				String s = pToShrink[i1];
				i = Math.min(i, firstNonSpace(s));
				int j1 = lastNonSpace(s);
				j = Math.max(j, j1);
				if (j1 < 0) {
					if (k == i1) {
						++k;
					}

					++l;
				} else {
					l = 0;
				}
			}

			if (pToShrink.length == l) {
				return new String[0];
			} else {
				String[] astring = new String[pToShrink.length - l - k];

				for(int k1 = 0; k1 < astring.length; ++k1) {
					astring[k1] = pToShrink[k1 + k].substring(i, j + 1);
				}

				return astring;
			}
		}

		private static int firstNonSpace(String pEntry) {
			int i;
			for(i = 0; i < pEntry.length() && pEntry.charAt(i) == ' '; ++i) {
			}

			return i;
		}

		private static int lastNonSpace(String pEntry) {
			int i;
			for(i = pEntry.length() - 1; i >= 0 && pEntry.charAt(i) == ' '; --i) {
			}

			return i;
		}

		private static Map<String, Ingredient> keyFromJson(JsonObject pKeyEntry) {
			java.util.Map<String, Ingredient> map = new java.util.HashMap<>();

			for(java.util.Map.Entry<String, com.google.gson.JsonElement> entry : pKeyEntry.entrySet()) {
				if (entry.getKey().length() != 1) {
					throw new JsonParseException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
				}

				if (" ".equals(entry.getKey())) {
					throw new JsonParseException("Invalid key entry: ' ' is a reserved symbol.");
				}

				map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
			}

			map.put(" ", Ingredient.EMPTY);
			return map;
		}

		private static NonNullList<Ingredient> dissolvePattern(String[] pPattern, Map<String, Ingredient> pKeys, int pPatternWidth, int pPatternHeight) {
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(pPatternWidth * pPatternHeight, Ingredient.EMPTY);
			java.util.Set<String> set = new java.util.HashSet<>(pKeys.keySet());
			set.remove(" ");

			for(int i = 0; i < pPattern.length; ++i) {
				for(int j = 0; j < pPattern[i].length(); ++j) {
					String s = pPattern[i].substring(j, j + 1);
					Ingredient ingredient = pKeys.get(s);
					if (ingredient == null) {
						throw new JsonParseException("Pattern references symbol '" + s + "' but it's not defined in the key");
					}

					set.remove(s);
					nonnulllist.set(j + pPatternWidth * i, ingredient);
				}
			}

			if (!set.isEmpty()) {
				throw new JsonParseException("Key defines symbols that aren't used in pattern: " + set);
			} else {
				return nonnulllist;
			}
		}

		public static ItemStack itemStackFromJson(JsonObject pStackObject) {
			return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(pStackObject, true, true);
		}

		// 最大宽度和高度的常量
		static int MAX_WIDTH = 3;
		static int MAX_HEIGHT = 3;
	}

	public static class JsonParseException extends RuntimeException {
		public JsonParseException(String pMessage) {
			super(pMessage);
		}
	}
}
