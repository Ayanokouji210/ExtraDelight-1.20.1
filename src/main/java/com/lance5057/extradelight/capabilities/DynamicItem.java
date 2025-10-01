package com.lance5057.extradelight.capabilities;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.items.dynamicfood.DynamicToast;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DynamicItem implements ExtraDelightComponents.IDynamicFood , ICapabilityProvider {

    private DynamicItemComponent component;

    public DynamicItem(DynamicItemComponent component) {
        this.component = component;
    }


    @Override
    public DynamicItemComponent getDynamicFood() {
        return this.component;
    }

    @Override
    public void setDynamicFood(DynamicItemComponent component) {
        this.component = component;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        NonNullList<ItemStack> items = component.getItems();


        //tag has "componentCount","component"
        //count
        tag.putInt("componentCount", items.size());

        //stack format "1":stackData
        CompoundTag itemsTag = new CompoundTag();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            itemsTag.put(String.valueOf(i),stack.save(new CompoundTag()));
        }
        tag.put("component",itemsTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        //compoundTag : componentCount, component
        if (compoundTag.contains("component")) {
            CompoundTag component = compoundTag.getCompound("component");
            NonNullList<ItemStack> items = NonNullList.create();;
            if (compoundTag.contains("componentCount")) {
                int count = compoundTag.getInt("componentCount");
                for (int i = 0; i < count; i++) {
                    if(component.contains(String.valueOf(i))) {
                        items.add(ItemStack.of(component.getCompound(component.get(String.valueOf(i)).toString())));
                    }else {
                        items.add(ItemStack.EMPTY);
                    }
                }
            }
            this.component = new DynamicItemComponent(items);
        }
    }



    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stack = event.getCrafting();
        Container input = event.getInventory();
        if (stack.getItem() instanceof DynamicToast) {

            int nutrition = 0;
            float saturation = 0;
            List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

            NonNullList<ItemStack> l = NonNullList.create();
            for (int i = 0; i < input.getContainerSize(); i++) {
                ItemStack s = input.getItem(i);
                if (s != null && !s.isEmpty()) {
                    l.add(s);
//                    if (stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).isPresent()) {
                    FoodProperties f = s.getFoodProperties(null);
                    if (f != null) {
                        nutrition += f.getNutrition();
                        saturation += f.getSaturationModifier();
                        effects.addAll(f.getEffects());
                    }
//                    } else
//                        ExtraDelight.logger
//                                .error(s.getDescriptionId() + " doesn't have a food component! How did we get here?!");
                }
            }




            FoodProperties.Builder food = new FoodProperties.Builder();
            food.nutrition(nutrition).saturationMod(saturation / input.getContainerSize()).alwaysEat();
            for (Pair<MobEffectInstance, Float> pair : effects) {
                food.effect(pair.getFirst(), pair.getSecond());
            }
            DynamicToast toast = (DynamicToast) stack.getItem();
            toast.ModifyBuilder(food);

            //set NBT
            CompoundTag tag = new CompoundTag();

            //ingredients
            CompoundTag ingredients = new CompoundTag();
            for (int i = 0; i < l.size(); i++) {
                ItemStack s = l.get(i);
                CompoundTag itemtag = new CompoundTag();
                s.save(itemtag);
                ingredients.put(String.valueOf(i),itemtag);
            }
            tag.put("ingredients",ingredients);

            //properties
            CompoundTag foodTag = new CompoundTag();

            foodTag.putInt("nutrition", nutrition);
            foodTag.putFloat("saturation", saturation);

            ListTag effectsTag = new ListTag();
            for (Pair<MobEffectInstance, Float> effect : effects) {
                CompoundTag effectTag = new CompoundTag();
                effect.getFirst().save(effectTag);
                effectTag.putFloat("probability", effect.getSecond());
                effectsTag.add(effectTag);
            }
            foodTag.put("effects", effectsTag);

            tag.put("properties",foodTag);

            CompoundTag nbt = stack.getOrCreateTag();
            nbt.put("properties",foodTag);


            Optional<ExtraDelightComponents.IDynamicFood> resolve = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve();
            if (resolve.isPresent()&&resolve.get()!=null) {
                DynamicItemComponent dynamicFood = resolve.get().getDynamicFood();
                for (ItemStack itemStack : l) {
                    dynamicFood.addItem(itemStack);
                }
                dynamicFood.serializeNBT();
            }



        } else {
            ExtraDelight.logger.error("DynamicToastRecipe result not DynamicToast!");
        }


//        ExtraDelight.logger.info("Crafting:{} " , stack);
//        if (stack.getItem() == ExtraDelightItems.DYNAMIC_TOAST.get()) {
//            DynamicToast toast = (DynamicToast) stack.getItem();
//            int nutrition = 0;
//            float saturation = 0;
//            List<Pair<MobEffectInstance,Float>> effects = new ArrayList<>();
//
//            NonNullList<ItemStack> ingredients = NonNullList.create();
//            for (int i = 0; i < inventory.getContainerSize(); i++) {
//                ItemStack s = inventory.getItem(i);
//
//                if (s != null && !s.isEmpty()) {
//                    ingredients.add(s);
//                    if (s.getItem().getFoodProperties() != null) {
//                        FoodProperties f = s.getItem().getFoodProperties();
//                        nutrition += f.getNutrition();
//                        saturation += f.getSaturationModifier();
//
//                        effects.addAll(f.getEffects());
//                    } else
//                        ExtraDelight.logger
//                                .error(" {} doesn't have a food component! How did we get here?!", s.getDescriptionId());
//                }
//            }
//
//            CompoundTag nbt = stack.getOrCreateTag();
//            CompoundTag ingredientsTag = new CompoundTag();
//
//            for (int i = 0; i < ingredients.size(); i++) {
//                CompoundTag itemTag = new CompoundTag();
//                ingredients.get(i).save(itemTag);
//                ingredientsTag.put(String.valueOf(i), itemTag);
//            }
//
//            nbt.put("ingredients", ingredientsTag);
//
//            FoodProperties.Builder foodBuilder = new FoodProperties.Builder()
//                    .nutrition(nutrition)
//                    .saturationMod(saturation / ingredients.size());
//
//            for (Pair<MobEffectInstance, Float> effect : effects) {
//                foodBuilder.effect(effect.getFirst(), effect.getSecond());
//            }
//
//            FoodProperties food = foodBuilder.build();
//
//            // 在 1.20.1 中，我们需要将食物属性存储在 NBT 中
//            CompoundTag foodTag = new CompoundTag();
//            foodTag.putInt("nutrition", food.getNutrition());
//            foodTag.putFloat("saturation", food.getSaturationModifier());
//
//            // 存储效果
//            ListTag effectsTag = new ListTag();
//            for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
//                CompoundTag effectTag = new CompoundTag();
//                effectTag.put("effect", effect.getFirst().save(new CompoundTag()));
//                effectTag.putFloat("probability", effect.getSecond());
//                effectsTag.add(effectTag);
//            }
//            foodTag.put("effects", effectsTag);
//
//            nbt.put("food_properties", foodTag);
//
//            stack.setTag(nbt);
//
//            toast.ModifyBuilder(foodBuilder);
//            if(stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).isPresent()
//                    &&stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().isPresent()) {
//                ExtraDelightComponents.IDynamicFood dynamicFood = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().get();
//                for(int i=0;i< inventory.getContainerSize();i++){
//                    ItemStack item = inventory.getItem(i);
//                    if (!item.isEmpty()) {
//                        dynamicFood.getDynamicFood().addItem(item);
//                    }
//                }
//                // ExtraDelight.logger.info("Dynamic Food:{} " , stack);
//
//            }
//        }

    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if(capability == ExtraDelightComponents.DYNAMIC_FOOD) {
            return LazyOptional.of(() -> this.component).cast();
        }
        return LazyOptional.empty();
    }
}
