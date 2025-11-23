package com.lance5057.extradelight.items.dynamicfood;

import java.util.ArrayList;
import java.util.List;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightTags;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.items.dynamicfood.api.IDynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


public class DynamicJam extends Item implements IDynamic {
    public static final ResourceLocation base_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
            "extra/dynamics/jam/jam_jar");
    public static final ResourceLocation missing_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
            "extra/dynamics/jam/jam_missing");

    public DynamicJam(Properties properties) {
        super(properties);
    }

    @Override
    public List<ResourceLocation> getPieces(ItemStack itemStack) {
        List<ResourceLocation> i = new ArrayList<ResourceLocation>();

        i.add(base_model);

        //DynamicItemComponent comp = itemStack.getComponents().get(ExtraDelightComponents.DYNAMIC_FOOD.get());
        ExtraDelightComponents.IDynamicFood comp = itemStack.getCapability(
                ExtraDelightComponents.DYNAMIC_FOOD).resolve().orElse(null);

        if (comp != null) {
            comp.deserializeNBT(itemStack.getOrCreateTag().contains("dynamic_food")?itemStack.getOrCreateTag().getCompound("dynamic_food"):new CompoundTag());
        }
        if (comp != null && ! comp.graphics().isEmpty()) {
            {
                if (!comp.graphics().isEmpty()) {

                    ResourceLocation rc = ExtraDelight.modLoc("extra/dynamics/jam/" + comp.graphics().get(0));

                    i.add(rc);
                } else
                    i.add(missing_model);
            }
        } else
            i.add(missing_model);

//		i.add(bm);
        return i;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag isAdvanced) {
        //ItemContainerContents comp = stack.getComponents().get(ExtraDelightComponents.ITEMSTACK_HANDLER.get());
        IItemHandler comp = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

        if (comp != null) {
            {
                if (isAdvanced.isAdvanced()) {
                    if (nonEmptyItems(comp)) {
                        tooltip.add(Component.translatable("tooltip.dynamic.ingredients"));
                        for (int i = 0; i < comp.getSlots(); i++) {
                            ItemStack s = comp.getStackInSlot(i);
                            tooltip.add(Component.literal(" - ").append(Component.translatable(s.getDescriptionId())));
                        }
                    }
                } else {
                    tooltip.add(Component.translatable("tooltip.see_more").setStyle(Style.EMPTY.withColor(0xFFAAAAAA)));
                }
            }
        }
    }

    private boolean nonEmptyItems(IItemHandler itemHandler) {
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        var cap = itemStack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().get();
        if(cap != null) {
            cap.deserializeNBT(itemStack.getOrCreateTag().getCompound("dynamic_food"));
            if (!cap.graphics().isEmpty()&&!cap.graphics().get(0).isEmpty()) {
                return Component.translatable("extradelight.jam." + (cap.graphics().isEmpty() ? "" :cap.graphics().get(0)));
            }else {
                return Component.translatable("item.extradelight.jam");
            }
        }
        return Component.translatable(getDescriptionId());
    }

}
