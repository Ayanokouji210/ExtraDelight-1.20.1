package com.lance5057.extradelight.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lance5057.extradelight.workstations.chiller.ChillerRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;

import java.util.Objects;
import java.util.Set;

public class StackUtil {

    public static JsonObject ItemStacktoJson(ItemStack itemStack) {
        JsonObject json = new JsonObject();
        if (itemStack == null||itemStack.isEmpty()) {
            json.addProperty("item","minecraft:air");
            json.addProperty("count", 0);
            return json;
        }
        json.addProperty("item",ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());

        if (itemStack.getCount()>1) {
            json.addProperty("count", itemStack.getCount());
        }
        return json;

    }

    public static JsonObject ItemStacktoJson(ItemStack itemStack, boolean hasTag) {
        JsonObject  json = ItemStacktoJson(itemStack);

        JsonObject nbt = new JsonObject();
        if (hasTag) {
            CompoundTag tag = itemStack.getTag();
            if (tag!=null) {
                for(String key:tag.getAllKeys()){
                    try {
                        nbt.addProperty(key,tag.get(key).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e+"can't add tag to stack,tag serialize error");
                    }
                }
            }
            json.add("tag",nbt);

        }

        return json;
    }


    public static ItemStack ItemStackfromJson(JsonObject jsonObject) {
        if (jsonObject == null||jsonObject.get("item")==null) {
            return ItemStack.EMPTY;
        }

        Item item = GsonHelper.getAsItem(jsonObject,"item");

        int count = 1;
        if (jsonObject.has("count")) {
            count = GsonHelper.getAsInt(jsonObject,"count");
        }
        return new ItemStack(item,count);

    }

    public static JsonObject BlockItemtoJson(BlockItem blockItem) {
        JsonObject json = new JsonObject();
        if(blockItem==null) {
            json.addProperty("item","minecraft:air");
            //json.addProperty("count", 0);
        }
        json.addProperty("item",ForgeRegistries.ITEMS.getKey(blockItem).toString());
        return json;
        //json.addProperty("count", 1);
    }

    public static BlockItem BlockItemfromJson(JsonObject jsonObject) {
        if(jsonObject.has("item")) {
            Item item=ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject,"item")));
            if(item==null) {
                throw new JsonParseException("Item" + GsonHelper.getAsString(jsonObject, "item") + "not found");
            }
            if(item instanceof BlockItem blockItem) {
                return blockItem;
            }else {
                throw new JsonParseException("Item" + GsonHelper.getAsString(jsonObject, "item") + " is not a BlockItem");
            }

        }else {
            throw new JsonParseException("No item property in json object");
        }
    }

    public static JsonObject FluidStacktoJson(FluidStack fluidStack) {
        JsonObject jsonObject = new JsonObject();
        if(fluidStack==null||fluidStack.isEmpty()){
            jsonObject.addProperty("fluid","minecraft:air");
            jsonObject.addProperty("amount",0);
            return jsonObject;
        }

        Fluid fluid = fluidStack.getFluid();
        jsonObject.addProperty("fluid",ForgeRegistries.FLUIDS.getKey(fluid).toString());

        int amount =fluidStack.getAmount();
        jsonObject.addProperty("amount",amount);

        return jsonObject;

    }

    public static FluidStack FluidStackfromJson(JsonObject jsonObject) {
        if (jsonObject == null||jsonObject.get("fluid")==null||
                jsonObject.get("amount")==null||
                Objects.equals(GsonHelper.getAsString(jsonObject,"fluid"), "minecraft:air") ||
                GsonHelper.getAsInt(jsonObject,"amount")==0) {
            return FluidStack.EMPTY;
        }

        Fluid fluid =ForgeRegistries.FLUIDS.getValue( new ResourceLocation(GsonHelper.getAsString(jsonObject,"fluid")));
        if (fluid==null) {
            throw new JsonParseException("Could not find fluid for " + GsonHelper.getAsString(jsonObject,"fluid"));
        }
        int amount =GsonHelper.getAsInt(jsonObject,"amount");
        return new FluidStack(fluid,amount);
    }


}
