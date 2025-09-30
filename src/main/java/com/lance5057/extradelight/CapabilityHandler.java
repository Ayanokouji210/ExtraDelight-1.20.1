package com.lance5057.extradelight;

import com.lance5057.extradelight.capabilities.Chill;
import com.lance5057.extradelight.capabilities.DynamicItem;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;

import static com.lance5057.extradelight.ExtraDelightClientEvents.chillMap;

@Mod.EventBusSubscriber
public class CapabilityHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        //chill
        if (chillMap.containsKey(item)) {
            ExtraDelightComponents.IChillComponent chillComponent = new Chill(chillMap.get(item));
            ICapabilityProvider chillProvider = new ICapabilityProvider() {
                @Override
                public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    if (capability == ExtraDelightComponents.CHILL) {
                        return LazyOptional.of(() -> chillComponent).cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(ExtraDelight.MOD_ID, "chill"), chillProvider);
        }

        //dynamicFood
        if(item.equals(ExtraDelightItems.DYNAMIC_TOAST.get())){
            ExtraDelightComponents.IDynamicFood dynamicFood = new DynamicItem(new DynamicItemComponent());
            ICapabilityProvider dynamicFoodProvider = new ICapabilityProvider() {

                @Override
                public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    if(capability == ExtraDelightComponents.DYNAMIC_FOOD){
                        return LazyOptional.of(() -> dynamicFood).cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(ExtraDelight.MOD_ID, "dynamic_food"), dynamicFoodProvider);
        }



    }


}
