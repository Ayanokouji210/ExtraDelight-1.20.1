package com.lance5057.extradelight;

import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetMenu;
import com.lance5057.extradelight.blocks.sink.SinkCabinetMenu;
import com.lance5057.extradelight.displays.food.FoodDisplayMenu;
import com.lance5057.extradelight.displays.knife.KnifeBlockMenu;
import com.lance5057.extradelight.displays.spice.SpiceRackMenu;
import com.lance5057.extradelight.displays.wreath.WreathMenu;
import com.lance5057.extradelight.gui.StyleableMenu;
import com.lance5057.extradelight.workstations.chiller.ChillerMenu;
import com.lance5057.extradelight.workstations.doughshaping.DoughShapingMenu;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotMenu;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlMenu;
import com.lance5057.extradelight.workstations.oven.OvenMenu;
import com.lance5057.extradelight.workstations.vat.VatMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.common.extensions.IForgeMenuType;
//import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;


public class ExtraDelightContainers {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU,
			ExtraDelight.MOD_ID);

	//IForgeMenuType<OvenMenu> OVEN_M=MENU_TYPES.register("oven",()->DeferredRegister.create(ForgeRegistries.MENU_TYPES,ExtraDelight.MOD_ID))

	public static final RegistryObject<MenuType<OvenMenu>> OVEN_MENU = MENU_TYPES.register("oven",
			() -> IForgeMenuType.create(OvenMenu::new));
	public static final RegistryObject<MenuType<MixingBowlMenu>> MIXING_BOWL_MENU = MENU_TYPES.register("mixing_bowl",
			() -> IForgeMenuType.create(MixingBowlMenu::new));

	public static final RegistryObject<MenuType<FoodDisplayMenu>> FOOD_DISPLAY_MENU = MENU_TYPES.register("food_display",
			() -> IForgeMenuType.create(FoodDisplayMenu::new));
	public static final RegistryObject<MenuType<KnifeBlockMenu>> KNIFE_BLOCK_MENU = MENU_TYPES.register("knife_block",
			() -> IForgeMenuType.create(KnifeBlockMenu::new));
	public static final RegistryObject<MenuType<SpiceRackMenu>> SPICE_RACK_MENU = MENU_TYPES.register("spice_rack",
			() -> IForgeMenuType.create(SpiceRackMenu::new));

	public static final RegistryObject<MenuType<DoughShapingMenu>> DOUGH_SHAPING_MENU = MENU_TYPES.register("dough_shaping",
			() -> IForgeMenuType.create(DoughShapingMenu::new));
	public static final RegistryObject<MenuType<WreathMenu>> WREATH_MENU = MENU_TYPES.register("wreath",
			() -> IForgeMenuType.create(WreathMenu::new));
	public static final RegistryObject<MenuType<SinkCabinetMenu>> SINK_MENU = MENU_TYPES.register("sink",
			() -> IForgeMenuType.create(SinkCabinetMenu::new));
	public static final RegistryObject<MenuType<CounterCabinetMenu>> COUNTER_CABINET_MENU = MENU_TYPES
			.register("counter_cabinet", () -> IForgeMenuType.create(CounterCabinetMenu::new));
	public static final RegistryObject<MenuType<StyleableMenu>> STYLE_MENU = MENU_TYPES.register("style",
			() -> IForgeMenuType.create(StyleableMenu::new));

	public static final RegistryObject<MenuType<MeltingPotMenu>> MELTING_POT_MENU = MENU_TYPES.register("melting_pot",
			() -> IForgeMenuType.create(MeltingPotMenu::new));
	public static final RegistryObject<MenuType<ChillerMenu>> CHILLER_MENU = MENU_TYPES.register("chiller",
			() -> IForgeMenuType.create(ChillerMenu::new));

	public static final RegistryObject<MenuType<VatMenu>> VAT_MENU = MENU_TYPES.register("vat",
			() -> IForgeMenuType.create(VatMenu::new));

	public static void register(IEventBus modBus) {
		MENU_TYPES.register(modBus);
	}

}
