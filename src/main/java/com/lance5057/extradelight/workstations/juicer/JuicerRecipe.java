package com.lance5057.extradelight.workstations.juicer;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class JuicerRecipe extends SingleItemRecipe {
	protected final FluidStack fluidOut;
	protected final int percentChance;

	public JuicerRecipe(ResourceLocation id, String pGroup, Ingredient pIngredient, ItemStack pResult, int chance, FluidStack fluidResult) {
		super(ExtraDelightRecipes.JUICER.get(), ExtraDelightRecipes.JUICER_SERIALIZER.get(), id, pGroup, pIngredient,
				pResult);
		this.fluidOut = fluidResult;
		this.percentChance = chance;
	}

	public FluidStack getFluid() {
		return fluidOut;
	}

	public Ingredient getInput() {
		return this.ingredient;
	}

	public int getChance() {
		return this.percentChance;
	}

    public boolean matches(ItemStack input) {
        return this.ingredient.test(input);
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    public ItemStack getToastSymbol() {
		return new ItemStack(ExtraDelightBlocks.JUICER.get());
	}

	public static class Serializer implements RecipeSerializer<JuicerRecipe> {

        @Override
        public JuicerRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            int chance = GsonHelper.getAsInt(jsonObject, "chance", 50);
            FluidStack fluid = StackUtil.FluidStackfromJson(GsonHelper.getAsJsonObject(jsonObject, "fluidOut"));
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            ItemStack result = StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new JuicerRecipe(pRecipeId, s, ingredient, result, chance, fluid);
        }

        @Override
        public @Nullable JuicerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int chance = pBuffer.readInt();
            FluidStack fluidOut = pBuffer.readFluidStack();
            return new JuicerRecipe(pRecipeId, s, ingredient, result, chance, fluidOut);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, JuicerRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.percentChance);
            pBuffer.writeFluidStack(pRecipe.fluidOut);
        }
    }
}