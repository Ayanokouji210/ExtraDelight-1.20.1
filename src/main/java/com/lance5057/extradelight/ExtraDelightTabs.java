package com.lance5057.extradelight;

import com.lance5057.extradelight.aesthetics.AestheticBlocks;
import com.lance5057.extradelight.capabilities.ItemHandler;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.modules.SummerCitrus;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;

public class ExtraDelightTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
			ExtraDelight.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.extradelight.tab"))
					.icon(() -> new ItemStack(ExtraDelightItems.WOODEN_SPOON.get()))
					.displayItems((parameters, output) -> {
						for (RegistryObject<Item> i : ExtraDelightItems.ITEMS.getEntries())
							if (i != ExtraDelightItems.EASTER_EGG && i != ExtraDelightItems.DYNAMIC_JAM)
								output.accept(i.get());
                        makeJam(output, "orange", SummerCitrus.SLICED_ORANGE.get());
                        makeJam(output, "lemon", SummerCitrus.SLICED_LEMON.get());
                        makeJam(output, "lime", SummerCitrus.SLICED_LIME.get());
                        makeJam(output, "grapefruit", SummerCitrus.SLICED_GRAPEFRUIT.get());
                        makeJam(output, "sweet_berries", Items.SWEET_BERRIES);
                        makeJam(output, "glow_berries", Items.GLOW_BERRIES);
                        makeJam(output, "apple", Items.APPLE);
                        makeJam(output, "golden_apple", Items.GOLDEN_APPLE);
                        makeJam(output, "chorus_fruit", Items.CHORUS_FRUIT);
                        makeJam(output, "melon", Items.MELON_SLICE);
                        makeJam(output, "carrot", Items.CARROT);

                        ItemStack s = new ItemStack(ExtraDelightItems.DYNAMIC_JAM.get());
                        boolean cap1 =s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().isPresent();
                        s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).ifPresent(c->c.setGraphics((List.of("mint"))));
                        s.getCapability(ExtraDelightComponents.ITEMSTACK_HANDLER).ifPresent(i->i.fromItems(List.of(
                                ExtraDelightItems.MINT.get().getDefaultInstance(),
                                ExtraDelightItems.MINT.get().getDefaultInstance(),
                                ExtraDelightItems.MINT.get().getDefaultInstance(), Items.SUGAR.getDefaultInstance(),
                                ExtraDelightItems.AGAR_AGAR.get().getDefaultInstance(),
                                Items.SUGAR.getDefaultInstance()
                        )));
                        s.setTag(new DynamicItemComponent(List.of("mint")).serializeNBT());
                        output.accept(s);
					}).build());

    @SubscribeEvent
    public static void AddItemsToTab(BuildCreativeModeTabContentsEvent event){

    }

    private static void makeJam(CreativeModeTab.Output output, String graphic, Item item) {
        ItemStack s = new ItemStack(ExtraDelightItems.DYNAMIC_JAM.get());

        if(s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().isPresent()){
            ExtraDelightComponents.IDynamicFood dynamicFood = s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().get();
            dynamicFood.setGraphics(List.of(graphic));
            ExtraDelight.logger.debug("graphics : {}", dynamicFood.graphics() );
            s.getOrCreateTag().put("dynamic_food",dynamicFood.serializeNBT());
        }
//        s.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).ifPresent(c->{
//            c.setGraphics(List.of(graphic));});

        s.getCapability(ExtraDelightComponents.ITEMSTACK_HANDLER).ifPresent(i->{i.fromItems(List.of(
                item.getDefaultInstance(), item.getDefaultInstance(),
                item.getDefaultInstance(), Items.SUGAR.getDefaultInstance(), Items.SUGAR.getDefaultInstance(),
                Items.SUGAR.getDefaultInstance()
        ));
            s.getOrCreateTag().put("item_handler",i.serializeNBT());
        }
        );


        output.accept(s);
    }


    public static final RegistryObject<CreativeModeTab> AESTHETICS = TABS.register("aesthetics",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.extradelight.aesthetics"))
					.icon(() -> new ItemStack(AestheticBlocks.WALLPAPER_BLOCKS.get(0).get()))
					.displayItems((parameters, output) -> {
						for (RegistryObject<Item> i : AestheticBlocks.ITEMS.getEntries())
							output.accept(i.get());
					}).build());
}
