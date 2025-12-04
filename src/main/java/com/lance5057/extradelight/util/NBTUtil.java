package com.lance5057.extradelight.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NBTUtil {


    /**
     *  Tag structure
     *  dynamic_food:(Compound)
     *      - graphics:(List)
     *          - "string" (string)
     */
    public class DynamicFood implements INBTSerializable<CompoundTag> {
        static final String TAG = "dynamic_food";
        private List<String> graphics;

        public DynamicFood(List<String> graphics) {
            this.graphics = graphics;
        }

        public List<String> getGraphics() {
            return graphics;
        }

        public void setGraphics(List<String> graphics) {
            this.graphics = graphics;
        }


        @Override
        public CompoundTag serializeNBT() {
            CompoundTag result = new CompoundTag();
            CompoundTag tag = new CompoundTag();
            ListTag listTag = new ListTag();
            this.graphics.forEach(i->listTag.add(StringTag.valueOf(i)));
            tag.put("graphics",listTag);
            result.put(TAG,tag);
            return result;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if(nbt.contains(TAG)){
                CompoundTag tag = nbt.getCompound(TAG);
                if(tag.contains("graphics")){
                    ListTag listTag = tag.getList("graphics",Tag.TAG_STRING);
                    listTag.forEach(i->this.graphics.add(i.getAsString()));
                }
            }
        }
    }

    /**
     *  Tag structure
     *  item_handler:(Compound)
     *      -
     *
     */
    public class ItemHandler implements INBTSerializable<CompoundTag> {
        final String TAG ="item_handler";
        private NonNullList<ItemStack> items;

        public ItemHandler(NonNullList<ItemStack> items) {
            this.items = items;
        }

        public NonNullList<ItemStack> getItems() {
            return items;
        }

        public void setItems(NonNullList<ItemStack> items) {
            this.items = items;
        }

        @Override
        public CompoundTag serializeNBT() {
            ItemStackHandler stackHandler = new ItemStackHandler(this.items);
            CompoundTag tag = stackHandler.serializeNBT();
            CompoundTag nbt = new CompoundTag();
            nbt.put(TAG,tag);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            ItemStackHandler stackHandler = new ItemStackHandler();
            if (nbt.contains(TAG)) {
                CompoundTag compound = nbt.getCompound(TAG);
                stackHandler.deserializeNBT(compound);
                NonNullList<ItemStack> list = NonNullList.withSize(stackHandler.getSlots(),ItemStack.EMPTY);
                for(int i=0;i<stackHandler.getSlots();i++){
                    list.set(i,stackHandler.getStackInSlot(i));
                }
                this.items = list;
            }
        }
    }

    /**
     *
     *
     *
     *
     */
    public static class IFood implements INBTSerializable<CompoundTag> {
        final String TAG ="ifood";
        private int nutrition;
        private float saturation;
        private List<Pair<MobEffectInstance, Float>> effects;

        public IFood() {
            this.nutrition =0;
            this.saturation =0.0f;
            this.effects = new ArrayList<>();
        }

        public IFood(int nutrition, float saturation, List<Pair<MobEffectInstance, Float>> effects) {
            this.nutrition = nutrition;
            this.saturation = saturation;
            this.effects = effects;
        }

        public int getNutrition() {
            return nutrition;
        }

        public void setNutrition(int nutrition) {
            this.nutrition = nutrition;
        }

        public float getSaturation() {
            return saturation;
        }

        public void setSaturation(float saturation) {
            this.saturation = saturation;
        }

        public List<Pair<MobEffectInstance, Float>> getEffects() {
            return effects;
        }

        public void setEffects(List<Pair<MobEffectInstance, Float>> effects) {
            this.effects = effects;
        }

        public FoodProperties getFoodProperties() {
            FoodProperties.Builder builder = new FoodProperties.Builder();
            builder.nutrition(nutrition).saturationMod(saturation);
            this.effects.forEach(pair->{builder.effect(pair.getFirst(),pair.getSecond());});
            return builder.build();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag result = new CompoundTag();
            CompoundTag tag = new CompoundTag();
            tag.putInt("nutrition",this.nutrition);
            tag.putFloat("saturation",this.saturation);
            ListTag listTag = new ListTag();
            this.effects.forEach(i->{
                CompoundTag effectTag = new CompoundTag();
                effectTag.putString("effect",i.getFirst().getEffect().getDescriptionId());
                effectTag.putInt("time",i.getFirst().getDuration());
                effectTag.putInt("amplifier",i.getFirst().getAmplifier());
                effectTag.putFloat("prob",i.getSecond());
                listTag.add(effectTag);
                    }
            );
            tag.put("effects",listTag);
            result.put(TAG,tag);
            return result;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if(nbt.contains(TAG)){
                CompoundTag tag = nbt.getCompound(TAG);
                if(tag.contains("nutrition")){
                    this.nutrition = tag.getInt("nutrition");
                    this.saturation = tag.getFloat("saturation");
                    this.effects = NonNullList.create();
                    ListTag listTag = tag.getList("effects",Tag.TAG_COMPOUND);
                    for(int i=0;i<listTag.size();i++){
                        CompoundTag effectTag = listTag.getCompound(i);
                        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(effectTag.getString("effect")));
                        int time = effectTag.getInt("time");
                        int amplifier = effectTag.getInt("amplifier");
                        float probability = effectTag.getFloat("prob");
                        if (mobEffect != null) {
                            this.effects.add(new Pair<>(new MobEffectInstance(mobEffect,time,amplifier),probability));
                        }

                    }
                }
            }
        }
    }

}
