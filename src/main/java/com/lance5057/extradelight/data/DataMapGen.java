//package com.lance5057.extradelight.data;
//
//import java.util.concurrent.CompletableFuture;
//
//import com.lance5057.extradelight.ExtraDelightItems;
//import com.lance5057.extradelight.modules.Fermentation;
//
////import de.teamlapen.vampirism.data.provider.DataMapsProvider;
//import net.minecraft.core.HolderLookup.Provider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.world.item.Item;
////import net.neoforged.neoforge.common.data.DataMapProvider;
////import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
////import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
//
//public class DataMapGen extends DataMapsProvider {
//
//	protected DataMapGen(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
//		super(packOutput, lookupProvider);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected void gather() {
//		Builder<Compostable, Item> compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
//
//		compostables.add(ExtraDelightItems.CORN_COB.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CORN_COB_BUNDLE.asItem().builtInRegistryHolder(), new Compostable(0.85f), false);
//		compostables.add(ExtraDelightItems.CORN_HUSK.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CORN_HUSK_BUNDLE.asItem().builtInRegistryHolder(), new Compostable(0.85f), false);
//		compostables.add(ExtraDelightItems.CORN_ON_COB.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CORN_SEEDS.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CORN_SILK.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.DRIED_CORN_HUSK.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.DRIED_CORN_HUSK_BUNDLE.asItem().builtInRegistryHolder(),
//				new Compostable(0.85f), false);
//		compostables.add(ExtraDelightItems.UNSHUCKED_CORN.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.BAD_FOOD.asItem().builtInRegistryHolder(), new Compostable(1f), false);
//		compostables.add(ExtraDelightItems.CACTUS.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.SLICED_APPLE.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.SLICED_GINGER.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.SLICED_ONION.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.SLICED_POTATO.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.SLICED_TOMATO.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.BREAD_SLICE.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.GINGER.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.MINT.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.COFFEE_CHERRIES.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.CINNAMON_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CINNAMON_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CINNAMON_BARK.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.HAZELNUT_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.HAZELNUT_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.HAZELNUTS.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.HAZELNUTS_IN_SHELL.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.MARSHMALLOW.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.MALLOW_ROOT.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.PEANUTS_IN_SHELL.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.PEANUTS.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.CHILI.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//
//		compostables.add(ExtraDelightItems.WILD_CHILI_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.WILD_MALLOW_ROOT_BLOCK.asItem().builtInRegistryHolder(),
//				new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.WILD_GINGER.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.WILD_PEANUT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.2f),
//				false);
//		compostables.add(ExtraDelightItems.APPLE_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.WILD_GARLIC_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(ExtraDelightItems.GARLIC.asItem().builtInRegistryHolder(), new Compostable(0.2f), false);
//		compostables.add(Fermentation.WILD_CUCUMBER.asItem().builtInRegistryHolder(), new Compostable(0.2f),false);
//		compostables.add(Fermentation.CUCUMBER.asItem().builtInRegistryHolder(), new Compostable(0.2f),false);
//		compostables.add(Fermentation.SLICED_CUCUMBER_ITEM.asItem().builtInRegistryHolder(), new Compostable(0.2f),false);
//		compostables.add(Fermentation.WILD_SOYBEAN.asItem().builtInRegistryHolder(), new Compostable(0.2f),false);
//		compostables.add(Fermentation.SOYBEAN_POD.asItem().builtInRegistryHolder(), new Compostable(0.2f),false);
//	}
//
//}

package com.lance5057.extradelight.data;

import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.modules.Fermentation;

import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataMapGen {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册堆肥物品和概率
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_COB.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_COB_BUNDLE.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_HUSK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_HUSK_BUNDLE.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_ON_COB.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_SEEDS.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CORN_SILK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.DRIED_CORN_HUSK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.DRIED_CORN_HUSK_BUNDLE.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.UNSHUCKED_CORN.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.BAD_FOOD.get(), 1.0f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CACTUS.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.SLICED_APPLE.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.SLICED_GINGER.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.SLICED_ONION.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.SLICED_POTATO.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.SLICED_TOMATO.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.BREAD_SLICE.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.GINGER.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.MINT.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.COFFEE_CHERRIES.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CINNAMON_LEAVES.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CINNAMON_SAPLING.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CINNAMON_BARK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.HAZELNUT_LEAVES.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.HAZELNUT_SAPLING.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.HAZELNUTS.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.HAZELNUTS_IN_SHELL.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.MARSHMALLOW.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.MALLOW_ROOT.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.PEANUTS_IN_SHELL.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.PEANUTS.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.CHILI.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.WILD_CHILI_BLOCK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.WILD_MALLOW_ROOT_BLOCK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.WILD_GINGER.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.WILD_PEANUT_BLOCK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.APPLE_LEAVES.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.WILD_GARLIC_BLOCK.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(ExtraDelightItems.GARLIC.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(Fermentation.WILD_CUCUMBER.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(Fermentation.CUCUMBER.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(Fermentation.SLICED_CUCUMBER_ITEM.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(Fermentation.WILD_SOYBEAN.get(), 0.2f);
            ComposterBlock.COMPOSTABLES.put(Fermentation.SOYBEAN_POD.get(), 0.2f);
        });
    }
}
