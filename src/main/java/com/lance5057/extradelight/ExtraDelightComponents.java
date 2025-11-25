package com.lance5057.extradelight;

import com.lance5057.extradelight.capabilities.FoodComponent;
import com.lance5057.extradelight.capabilities.ItemHandler;
import com.lance5057.extradelight.items.components.ChillComponent;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
//import com.lance5057.extradelight.util.DataComponentIngredient;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ExtraDelightComponents {
    // 使用 Capability 系统替代 DataComponent 系统

    // 流体存储 Capability
    public static final Capability<IFluidContainer> FLUID = CapabilityManager.get(new CapabilityToken<>() {});

    // 冷却组件 Capability
    public static final Capability<IChillComponent> CHILL = CapabilityManager.get(new CapabilityToken<>() {});

    // 物品堆处理 Capability
    public static final Capability<EDItemStackHandler> ITEMSTACK_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});

    // 动态食物 Capability
    public static final Capability<IDynamicFood> DYNAMIC_FOOD = CapabilityManager.get(new CapabilityToken<>() {});

    //食物属性
    public static final Capability<IFood> IFOOD = CapabilityManager.get(new CapabilityToken<>() {});

    // 接口定义
    public interface IFluidContainer extends INBTSerializable<CompoundTag> {
        FluidStack getFluid();
        void setFluid(FluidStack fluid);
        int getCapacity();
    }

    public interface IChillComponent extends INBTSerializable<CompoundTag> {
        int getChill();
       // void setChill(ChillComponent chill);
    }

    public interface EDItemStackHandler extends IItemHandler, INBTSerializable<CompoundTag> {
        void fromItems(List<ItemStack> stackList);
        Iterable<ItemStack> nonEmptyItems();

        static NonNullList<ItemStack> read(ItemStack stack) {
            NonNullList<ItemStack> stackList = NonNullList.create();
            CompoundTag tag = stack.getOrCreateTag();
            if(!tag.isEmpty()){
                if (tag.contains("item_handler")) {
                    CompoundTag handler = tag.getCompound("item_handler");
                    ItemStackHandler stackHandler = new ItemStackHandler();
                    stackHandler.deserializeNBT(handler);
                    for(int i = 0; i < stackHandler.getSlots(); i++){
                        stackList.add(stackHandler.getStackInSlot(i));
                    }
                }
            }
            return stackList;
        }

        static CompoundTag write(NonNullList<ItemStack> stackList) {
            CompoundTag tag = new CompoundTag();
            ItemStackHandler stackHandler = new ItemStackHandler(stackList);
            CompoundTag serialized = stackHandler.serializeNBT();
            tag.put("item_handler", serialized);
            return tag;
        }
    }

    public interface IDynamicFood extends INBTSerializable<CompoundTag> {

        String TAG = "dynamic_food";

        List<String> graphics();
        void setGraphics(List<String> graphics);

        static List<String> read(ItemStack stack) {
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains(TAG)) {
                CompoundTag compound = tag.getCompound(TAG);
                if(compound.contains("graphics")) {
                    ListTag listTag = compound.getList("graphics",Tag.TAG_STRING);
                    return listTag.stream().map(Tag::getAsString).toList();
                }
            }
            return List.of();
        }

        static CompoundTag write(List<String> graphics) {
            if(graphics == null|| graphics.isEmpty()) {
                return new CompoundTag();
            }
            CompoundTag nbt = new CompoundTag();
            ListTag listTag = new ListTag();
            graphics.forEach(s->listTag.add(StringTag.valueOf(s)));
            CompoundTag graphicsTag = new CompoundTag();
            graphicsTag.put("graphics", listTag);
            nbt.put(TAG,graphicsTag);
            return nbt;
        }
    }

    public interface IFood extends INBTSerializable<CompoundTag> {
        String TAG = "ifood";
        FoodComponent getFood();
        void setFood(FoodComponent component);

        static FoodProperties read(ItemStack stack) {
            CompoundTag tag = stack.getOrCreateTag();
            FoodProperties.Builder builder = new FoodProperties.Builder();
            if(tag.contains(TAG)) {
                CompoundTag compound = tag.getCompound(TAG);

                if(compound.contains("nutrition")) {
                    builder.nutrition(compound.getInt("nutrition"));
                }else{
                    builder.nutrition(1);
                }

                if(compound.contains("saturation")) {
                    builder.saturationMod(compound.getInt("saturation"));
                }else{
                    builder.saturationMod(1);
                }

                if(compound.contains("effects")) {
                    ListTag listTag = compound.getList("effects", Tag.TAG_COMPOUND);
                    for(int i = 0; i < listTag.size(); i++){
                        CompoundTag effect = listTag.getCompound(i);
                        if(effect.contains("name")) {
                            String name = effect.getString("name");
                            MobEffect value = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(name));
                            if(value != null) {
                                builder.effect(()->new MobEffectInstance(value, effect.getInt("time")),1.0f);
                            }
                        }
                    }
                }

            }
            return builder.build();
        }

        static CompoundTag write(FoodProperties food) {
            int nutrition = food.getNutrition();
            float saturation = food.getSaturationModifier();
            List<Pair<MobEffectInstance, Float>> effects = food.getEffects();
            CompoundTag nbt = new CompoundTag();
            CompoundTag compound = new CompoundTag();
            if(nutrition != 0){
                compound.putInt("nutrition", nutrition);
            }
            if(saturation != 0){
                compound.putFloat("saturation", saturation);
            }
            if(!effects.isEmpty()){
                ListTag listTag = new ListTag();
                effects.forEach(pair->{
                    CompoundTag effectTag = new CompoundTag();
                    effectTag.putString("name",pair.getFirst().getEffect().getDescriptionId());
                    effectTag.putFloat("time", pair.getSecond());
                });
                compound.put("effects", listTag);
            }
            nbt.put(TAG, compound);
            return nbt;
        }
    }

    public static FluidStack getFluid(ItemStack stack) {
        return stack.getCapability(FLUID).map(IFluidContainer::getFluid).orElse(FluidStack.EMPTY);
    }

    public static void setFluid(ItemStack stack, FluidStack fluid) {
        stack.getCapability(FLUID).ifPresent(container -> container.setFluid(fluid));
    }

    public static int getChill(ItemStack stack) {
        return stack.getCapability(CHILL).map(IChillComponent::getChill).orElse(100);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(EDItemStackHandler.class);
        event.register(IChillComponent.class);
        event.register(IDynamicFood.class);
    }

}