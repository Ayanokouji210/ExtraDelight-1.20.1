package com.lance5057.extradelight.workstations.evaporator.recipes;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
//import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.HolderLookup.Provider;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class EvaporatorRecipe implements Recipe<EvaporatorRecipeWrapper> {
	final String group;
	protected final int cookTime;
	private ResourceLocation id;

	public int getCookTime() {
		return cookTime;
	}

	public ResourceLocation getOutput() {
		return output;
	}

	final FluidIngredient fluid;

	public FluidIngredient getFluid() {
		return fluid;
	}

	final ResourceLocation output;
	final ResourceLocation display;

	final ItemStack outItem;

	public ResourceLocation getDisplay() {
		return display;
	}

	public EvaporatorRecipe(ResourceLocation id,String pGroup, FluidIngredient fluid, int time, ResourceLocation lootTable,
			ResourceLocation displayBlock, ItemStack outItem) {
		this.id = id;
		this.group = pGroup;
		this.cookTime = time;
		this.fluid = fluid;
		this.output = lootTable;
		this.display = displayBlock;
		this.outItem = outItem;
	}

	@Override
	public boolean matches(EvaporatorRecipeWrapper input, Level level) {
		return this.fluid.test(input.tank.getFluid());
	}

	@Override
	public ItemStack assemble(EvaporatorRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
		return this.getResultItem().copy();
	}

//	@Override
//	public ItemStack assemble(EvaporatorRecipeWrapper input, Provider registries) {
//		return this.getResultItem(registries).copy();
//	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return outItem;
	}


//	@Override
//	public ItemStack getResultItem(Provider registries) {
//		return outItem;
//	}

	public ItemStack getResultItem() {
		return outItem;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraDelightRecipes.EVAPORATOR_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ExtraDelightRecipes.EVAPORATOR.get();
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}

    @Override
    public ResourceLocation getId() {
        return this.id;
    }



    public static class Serializer implements RecipeSerializer<EvaporatorRecipe> {
//		private static final MapCodec<EvaporatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst
//				.group(Codec.STRING.optionalFieldOf("group", "").forGetter(EvaporatorRecipe::getGroup),
//						SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter(EvaporatorRecipe::getFluid),
//						Codec.INT.fieldOf("time").forGetter(EvaporatorRecipe::getCookTime),
//						ResourceLocation.CODEC.fieldOf("loottable").forGetter(EvaporatorRecipe::getOutput),
//						ResourceLocation.CODEC.fieldOf("display_block").forGetter(EvaporatorRecipe::getDisplay),
//						ItemStack.CODEC.fieldOf("outItem").forGetter(EvaporatorRecipe::getResultItem))
//				.apply(inst, EvaporatorRecipe::new));
//
//		public static EvaporatorRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
//			String s = pBuffer.readUtf();
//			SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(pBuffer);
//			int g = pBuffer.readVarInt();
//			ResourceLocation r = ResourceLocation.STREAM_CODEC.decode(pBuffer);
//			ResourceLocation d = ResourceLocation.STREAM_CODEC.decode(pBuffer);
//
//			ItemStack stack = ItemStack.STREAM_CODEC.decode(pBuffer);
//
//			return new EvaporatorRecipe(s, fluid, g, r, d, stack);
//		}
//
//		public static void toNetwork(RegistryFriendlyByteBuf pBuffer, EvaporatorRecipe pRecipe) {
//			pBuffer.writeUtf(pRecipe.group);
//			SizedFluidIngredient.STREAM_CODEC.encode(pBuffer, pRecipe.fluid);
//			pBuffer.writeVarInt(pRecipe.cookTime);
//			ResourceLocation.STREAM_CODEC.encode(pBuffer, pRecipe.output);
//			ResourceLocation.STREAM_CODEC.encode(pBuffer, pRecipe.display);
//
//			ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.outItem);
//		}
//
//		@Override
//		public MapCodec<EvaporatorRecipe> codec() {
//			return CODEC;
//		}
//
//		public static final StreamCodec<RegistryFriendlyByteBuf, EvaporatorRecipe> STREAM_CODEC = StreamCodec
//				.of(Serializer::toNetwork, Serializer::fromNetwork);
//
//		@Override
//		public StreamCodec<FriendlyByteBuf, EvaporatorRecipe> streamCodec() {
//			return STREAM_CODEC;
//		}

		@Override
		public EvaporatorRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
			String s= GsonHelper.getAsString(jsonObject, "group", "");
			ResourceLocation displayBlock =new ResourceLocation(GsonHelper.getAsString(jsonObject, "display_block"));
			FluidIngredient fliud = FluidIngredient.deserialize(GsonHelper.getAsJsonObject(jsonObject, "fluid"));
			ResourceLocation lootTable=new ResourceLocation(GsonHelper.getAsString(jsonObject, "loottable"));
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "outItem"));
			int time = GsonHelper.getAsInt(jsonObject, "time", 200);

			return new EvaporatorRecipe(resourceLocation,s,fliud,time,lootTable,displayBlock,result);
		}

		@Override
		public @Nullable EvaporatorRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
			String s=friendlyByteBuf.readUtf();
			FluidIngredient fluid = FluidIngredient.read(friendlyByteBuf);
			int time = friendlyByteBuf.readInt();
			ResourceLocation lootTable = friendlyByteBuf.readResourceLocation();
			ResourceLocation displayBlock = friendlyByteBuf.readResourceLocation();
			ItemStack result =friendlyByteBuf.readItem();

			return new EvaporatorRecipe(resourceLocation,s,fluid,time,lootTable,displayBlock,result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, EvaporatorRecipe evaporatorRecipe) {
			friendlyByteBuf.writeUtf(evaporatorRecipe.group);
			evaporatorRecipe.fluid.write(friendlyByteBuf);
			friendlyByteBuf.writeInt(evaporatorRecipe.cookTime);
			friendlyByteBuf.writeResourceLocation(evaporatorRecipe.output);
			friendlyByteBuf.writeResourceLocation(evaporatorRecipe.display);
			friendlyByteBuf.writeItem(evaporatorRecipe.outItem);

		}
	}

}