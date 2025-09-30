package com.lance5057.extradelight;

import com.lance5057.extradelight.items.components.ChillComponent;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ExtraDelightComponents {
    // 使用 Capability 系统替代 DataComponent 系统

    // 流体存储 Capability
    public static final Capability<IFluidContainer> FLUID = CapabilityManager.get(new CapabilityToken<>() {});

    // 冷却组件 Capability
    public static final Capability<IChillComponent> CHILL = CapabilityManager.get(new CapabilityToken<>() {});

    // 物品堆处理 Capability
    public static final Capability<IItemStackHandler> ITEMSTACK_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});

    // 动态食物 Capability
    public static final Capability<IDynamicFood> DYNAMIC_FOOD = CapabilityManager.get(new CapabilityToken<>() {});

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

    public interface IItemStackHandler extends INBTSerializable<CompoundTag> {
        ItemStackHandler getHandler();
        void setHandler(ItemStackHandler handler);
    }

    public interface IDynamicFood extends INBTSerializable<CompoundTag> {
        DynamicItemComponent getDynamicFood();
        void setDynamicFood(DynamicItemComponent component);
    }

    // 辅助方法 - 获取或创建组件
    public static FluidStack getFluid(ItemStack stack) {
        return stack.getCapability(FLUID).map(IFluidContainer::getFluid).orElse(FluidStack.EMPTY);
    }

    public static void setFluid(ItemStack stack, FluidStack fluid) {
        stack.getCapability(FLUID).ifPresent(container -> container.setFluid(fluid));
    }

    public static int getChill(ItemStack stack) {
        return stack.getCapability(CHILL).map(IChillComponent::getChill).orElse(100);
    }

//    public static void setChill(ItemStack stack, ChillComponent chill) {
//        stack.getCapability(CHILL).ifPresent(component -> component.setChill(chill));
//    }

    public static ItemStackHandler getItemStackHandler(ItemStack stack) {
        return stack.getCapability(ITEMSTACK_HANDLER).map(IItemStackHandler::getHandler).orElse(new ItemStackHandler());
    }

    public static void setItemStackHandler(ItemStack stack, ItemStackHandler handler) {
        stack.getCapability(ITEMSTACK_HANDLER).ifPresent(component -> component.setHandler(handler));
    }

    public static DynamicItemComponent getDynamicFood(ItemStack stack) {
        return stack.getCapability(DYNAMIC_FOOD).map(IDynamicFood::getDynamicFood).orElse(new DynamicItemComponent());
    }

    public static void setDynamicFood(ItemStack stack, DynamicItemComponent component) {
        stack.getCapability(DYNAMIC_FOOD).ifPresent(food -> food.setDynamicFood(component));
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IChillComponent.class);
        event.register(IDynamicFood.class);
    }

}