package com.lance5057.extradelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.items.dynamicfood.DynamicToast;
import com.mojang.serialization.DynamicOps;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;


import java.util.List;

public class DynamicToastRecipe extends ShapelessRecipe {
    private final String graphic;

    public DynamicToastRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result,
                              NonNullList<Ingredient> ingredients, String graphic) {
        super(id, group, category, result, ingredients);
        this.graphic = graphic;
    }


    public String getGraphic() {
        return graphic;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public ItemStack assemble(CraftingContainer input, RegistryAccess registries) {
        ItemStack assemble = super.assemble(input, registries).copy();
        NonNullList<ItemStack> stacks = NonNullList.withSize(this.getIngredients().size(), ItemStack.EMPTY);
        for (int i = 0; i < this.getIngredients().size(); ++i) {
            stacks.set(i, this.getIngredients().get(i).getItems()[0].copy());
        }
        CompoundTag write = ExtraDelightComponents.EDItemStackHandler.write(stacks);
        if(this.graphic.equals("dynamic")){
            CompoundTag nbt = new CompoundTag();
            CompoundTag tag = new CompoundTag();
            List<ItemStack> jams = input.getItems().stream().filter(i -> i.is(ExtraDelightItems.DYNAMIC_JAM.get())).toList();
            if(!jams.isEmpty()){
                ListTag listTag = new ListTag();
                listTag.add(StringTag.valueOf("dynamic_jam"));
                tag.put("graphics", listTag);
                nbt.put(ExtraDelightComponents.IDynamicFood.TAG, tag);
                nbt.putString("dynamic",ExtraDelightComponents.IDynamicFood.read(jams.get(0)).get(0));
            }else {
                ExtraDelight.logger.warn("Unknown dynamic jam {} ");
                nbt.put(ExtraDelightComponents.IDynamicFood.TAG, tag);
            }

            write.merge(nbt);
        }else {
            write.merge(ExtraDelightComponents.IDynamicFood.write(List.of(this.graphic)));
        }
        assemble.setTag(write);
        return assemble;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {return width*height >= 2;}


    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack resultItem = super.getResultItem(pRegistryAccess).copy();
        if(resultItem.getItem() instanceof DynamicToast) {
            CompoundTag tag = resultItem.getOrCreateTag();
            if(this.graphic.equals("dynamic")){
                tag.putString(ExtraDelightComponents.IDynamicFood.TAG,"dynamic_jam/sweet_berries");
            }else {
                tag.merge(ExtraDelightComponents.IDynamicFood.write(List.of(this.graphic)));
            }
            resultItem.setTag(tag);
        }
        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
       return ExtraDelightRecipes.DYNAMIC_TOAST_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

	public static class Serializer implements RecipeSerializer<DynamicToastRecipe> {
		@Override
		public DynamicToastRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");
            CraftingBookCategory category = CraftingBookCategory.MISC;
            try {
                category = CraftingBookCategory.valueOf(json.get("category").getAsString().toLowerCase());
            }catch (Exception e){
                ExtraDelight.logger.warn("Error parsing crafting book category: {},using default MISC " , json.get("category").getAsString());
            }

            NonNullList<Ingredient> ingredients = NonNullList.create();

            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients", new JsonArray());
            jsonArray.asList().stream().map(Ingredient::fromJson).forEach(ingredients::add);

			JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
			ItemStack result = CraftingHelper.getItemStack(resultJson, true);

            String graphic = GsonHelper.getAsString(json, "graphic");

            return new DynamicToastRecipe(resourceLocation, group, category, result, ingredients, graphic);
		}

		@Override
		public @Nullable DynamicToastRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
            CraftingBookCategory category = CraftingBookCategory.valueOf(buffer.readUtf());

            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
				ingredients.set(i, Ingredient.fromNetwork(buffer));
			}

			ItemStack result = buffer.readItem();

            String graphic = buffer.readUtf();

            return new DynamicToastRecipe(resourceLocation, group, category, result, ingredients,graphic);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, DynamicToastRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeUtf(recipe.category().name());

            buffer.writeVarInt(recipe.getIngredients().size());
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem(null));

            buffer.writeUtf(recipe.getGraphic());
		}
	}
}