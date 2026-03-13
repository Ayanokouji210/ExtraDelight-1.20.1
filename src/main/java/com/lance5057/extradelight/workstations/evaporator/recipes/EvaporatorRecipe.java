package com.lance5057.extradelight.workstations.evaporator.recipes;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.fluids.FluidIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

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


	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return outItem;
	}

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

        @Override
        public EvaporatorRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            ResourceLocation displayBlock = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "display_block"));
            FluidIngredient fliud = FluidIngredient.deserialize(GsonHelper.getAsJsonObject(jsonObject, "fluid"));
            ResourceLocation lootTable = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "loottable"));
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
			friendlyByteBuf.writeResourceLocation(evaporatorRecipe.output);  //lootTable
			friendlyByteBuf.writeResourceLocation(evaporatorRecipe.display); //displayBlock
			friendlyByteBuf.writeItem(evaporatorRecipe.outItem);			 //result

		}
	}

}