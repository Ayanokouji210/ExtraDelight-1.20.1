package com.lance5057.extradelight;

import com.lance5057.extradelight.capabilities.FoodComponent;
import com.lance5057.extradelight.capabilities.ItemHandler;
import com.lance5057.extradelight.items.components.ChillComponent;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
//import com.lance5057.extradelight.util.DataComponentIngredient;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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
    }

    public interface IDynamicFood extends INBTSerializable<CompoundTag> {
        List<String> graphics();
        void setGraphics(List<String> graphics);

        static List<String> read(ItemStack stack) {
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("dynamic_food")) {
                CompoundTag compound = tag.getCompound("dynamic_food");
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
            nbt.put("dynamic_food",graphicsTag);
            return nbt;
        }
    }

    public interface IFood extends INBTSerializable<CompoundTag> {
        FoodComponent getFood();
        void setFood(FoodComponent component);
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