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
import net.minecraft.nbt.StringTag;
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

    private List<String> graphics;

    public DynamicItem(List<String> graphics) {
        this.graphics = graphics;
    }


    @Override
    public List<String> graphics() {
        return this.graphics;
    }

    @Override
    public void setGraphics(List<String> graphics) {
        this.graphics = graphics;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        CompoundTag tag = new CompoundTag();

        ListTag listTag = new ListTag();
        for (String graphic : this.graphics) {
            listTag.add(StringTag.valueOf(graphic));
        }
        tag.put("graphics", listTag);

        nbt.put("dynamic",tag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        this.graphics=new ArrayList<>();
        if(compoundTag.contains("dynamic")) {
            CompoundTag nbt = compoundTag.getCompound("dynamic");
                 if(nbt.contains("graphics")) {
                     ListTag listTag = nbt.getList("graphics",Tag.TAG_STRING);
                     for (int i = 0; i < listTag.size(); i++) {
                         this.graphics.add(listTag.getString(i));
                     }
                 }
            ExtraDelight.logger.error("Broken DynamicItem Tag! {}",compoundTag.toString());
        }
    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if(capability == ExtraDelightComponents.DYNAMIC_FOOD) {
            return LazyOptional.of(() -> this).cast();
        }
        return LazyOptional.empty();
    }
}
