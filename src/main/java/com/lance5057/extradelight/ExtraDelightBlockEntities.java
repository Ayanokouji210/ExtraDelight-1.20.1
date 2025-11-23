package com.lance5057.extradelight;

import com.lance5057.extradelight.aesthetics.AestheticBlocks;
import com.lance5057.extradelight.aesthetics.block.cornhuskdoll.CornHuskDollBlockEntity;
import com.lance5057.extradelight.blocks.chocolatebox.ChocolateBoxBlockEntity;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetBlockEntity;
import com.lance5057.extradelight.blocks.entities.TapBlockEntity;
import com.lance5057.extradelight.blocks.funnel.FunnelBlockEntity;
import com.lance5057.extradelight.blocks.jar.JarBlockEntity;
import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlockEntity;
import com.lance5057.extradelight.blocks.keg.KegBlockEntity;
import com.lance5057.extradelight.blocks.picnicbasket.PicnicBasketBlockEntity;
import com.lance5057.extradelight.blocks.sink.SinkCabinetBlockEntity;
import com.lance5057.extradelight.displays.candybowl.CandyBowlEntity;
import com.lance5057.extradelight.displays.food.FoodDisplayEntity;
import com.lance5057.extradelight.displays.fruitbowl.FruitBowlBlockEntity;
import com.lance5057.extradelight.displays.knife.KnifeBlockEntity;
import com.lance5057.extradelight.displays.spice.SpiceRackEntity;
import com.lance5057.extradelight.displays.wreath.WreathEntity;
import com.lance5057.extradelight.modules.Fermentation;
import com.lance5057.extradelight.modules.SummerCitrus;
import com.lance5057.extradelight.workstations.chiller.ChillerBlockEntity;
import com.lance5057.extradelight.workstations.dryingrack.DryingRackBlockEntity;
import com.lance5057.extradelight.workstations.evaporator.EvaporatorBlockEntity;
import com.lance5057.extradelight.workstations.juicer.JuicerBlockEntity;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotBlockEntity;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlBlockEntity;
import com.lance5057.extradelight.workstations.mortar.MortarBlockEntity;
import com.lance5057.extradelight.workstations.oven.OvenBlockEntity;
import com.lance5057.extradelight.workstations.vat.VatBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.HashSet;
import java.util.Set;

public class ExtraDelightBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExtraDelight.MOD_ID);

	public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN = TILES.register(
			"oven", () -> BlockEntityType.Builder.of(OvenBlockEntity::new, ExtraDelightBlocks.OVEN.get()).build(null));

	public static final RegistryObject<BlockEntityType<FoodDisplayEntity>> FOOD_DISPLAY = TILES
			.register("food_display", () -> BlockEntityType.Builder
					.of(FoodDisplayEntity::new, AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.FOOD_DISPLAY))
					.build(null));

	public static final RegistryObject<BlockEntityType<KnifeBlockEntity>> KNIFE_BLOCK = TILES
			.register("knife_block", () -> BlockEntityType.Builder
					.of(KnifeBlockEntity::new, AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.KNIFE_BLOCKS))
					.build(null));
	public static final RegistryObject<BlockEntityType<SpiceRackEntity>> SPICE_RACK = TILES
			.register("spice_rack", () -> BlockEntityType.Builder
					.of(SpiceRackEntity::new,
							ArrayUtils.addAll(AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.SPICE_RACKS),
									AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.SPICE_RACKS_FULL)))
					.build(null));

	public static final RegistryObject<BlockEntityType<WreathEntity>> WREATH = TILES.register(
			"wreath",
			() -> BlockEntityType.Builder
					.of(WreathEntity::new, AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.WREATHS))
					.build(null));

	public static final RegistryObject<BlockEntityType<SinkCabinetBlockEntity>> SINK_BLOCK = TILES
			.register("sink_block", () -> BlockEntityType.Builder
					.of(SinkCabinetBlockEntity::new, AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.SINKS))
					.build(null));

	public static final RegistryObject<BlockEntityType<CounterCabinetBlockEntity>> COUNTER_CABINET_BLOCK = TILES
			.register("counter_cabinet_block",
					() -> BlockEntityType.Builder
							.of(CounterCabinetBlockEntity::new,
									AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.COUNTER_CABINETS))
							.build(null));

    public static final RegistryObject<BlockEntityType<PicnicBasketBlockEntity>> PICNIC_BASKET = TILES
            .register("picnic_basket",
                    () -> BlockEntityType.Builder
                            .of(PicnicBasketBlockEntity::new, SummerCitrus.WHITE_PICNIC_BASKET.get(),
                                    SummerCitrus.ORANGE_PICNIC_BASKET.get(), SummerCitrus.MAGENTA_PICNIC_BASKET.get(),
                                    SummerCitrus.LIGHT_BLUE_PICNIC_BASKET.get(),
                                    SummerCitrus.YELLOW_PICNIC_BASKET.get(), SummerCitrus.LIME_PICNIC_BASKET.get(),
                                    SummerCitrus.PINK_PICNIC_BASKET.get(), SummerCitrus.GRAY_PICNIC_BASKET.get(),
                                    SummerCitrus.LIGHT_GRAY_PICNIC_BASKET.get(), SummerCitrus.CYAN_PICNIC_BASKET.get(),
                                    SummerCitrus.BLUE_PICNIC_BASKET.get(), SummerCitrus.BROWN_PICNIC_BASKET.get(),
                                    SummerCitrus.GREEN_PICNIC_BASKET.get(), SummerCitrus.RED_PICNIC_BASKET.get(),
                                    SummerCitrus.BLACK_PICNIC_BASKET.get(), SummerCitrus.PURPLE_PICNIC_BASKET.get())
                            .build(null));


    public static final RegistryObject<BlockEntityType<MortarBlockEntity>> MORTAR = TILES
			.register("mortar", () -> BlockEntityType.Builder
					.of(MortarBlockEntity::new, ExtraDelightBlocks.MORTAR_STONE.get()).build(null));

	public static final RegistryObject<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL = TILES
			.register("mixing_bowl", () -> BlockEntityType.Builder
					.of(MixingBowlBlockEntity::new, ExtraDelightBlocks.MIXING_BOWL.get()).build(null));

	public static final RegistryObject<BlockEntityType<DryingRackBlockEntity>> DRYING_RACK = TILES
			.register("drying_rack", () -> BlockEntityType.Builder
					.of(DryingRackBlockEntity::new, ExtraDelightBlocks.DRYING_RACK.get()).build(null));

	public static final RegistryObject<BlockEntityType<CornHuskDollBlockEntity>> CORN_HUSK_DOLL = TILES
			.register("corn_husk_doll", () -> BlockEntityType.Builder
					.of(CornHuskDollBlockEntity::new, AestheticBlocks.CORN_HUSK_DOLL.get()).build(null));

	public static final RegistryObject<BlockEntityType<CandyBowlEntity>> CANDY_BOWL = TILES
			.register("candy_bowl", () -> BlockEntityType.Builder
					.of(CandyBowlEntity::new, ExtraDelightBlocks.CANDY_BOWL.get()).build(null));

	public static final RegistryObject<BlockEntityType<TapBlockEntity>> TAP = TILES.register("tap",
			() -> BlockEntityType.Builder.of(TapBlockEntity::new, ExtraDelightBlocks.TAP.get()).build(null));

	public static final RegistryObject<BlockEntityType<KegBlockEntity>> KEG = TILES.register("keg",
			() -> BlockEntityType.Builder.of(KegBlockEntity::new, ExtraDelightBlocks.KEG.get()).build(null));

	public static final RegistryObject<BlockEntityType<MeltingPotBlockEntity>> MELTING_POT = TILES
			.register("melting_pot", () -> BlockEntityType.Builder
					.of(MeltingPotBlockEntity::new, ExtraDelightBlocks.MELTING_POT.get()).build(null));

	public static final RegistryObject<BlockEntityType<ChillerBlockEntity>> CHILLER = TILES
			.register("chiller", () -> BlockEntityType.Builder
					.of(ChillerBlockEntity::new, ExtraDelightBlocks.CHILLER.get()).build(null));

	public static final RegistryObject<BlockEntityType<FunnelBlockEntity>> FUNNEL = TILES.register(
			"funnel",
			() -> BlockEntityType.Builder.of(FunnelBlockEntity::new, ExtraDelightBlocks.FUNNEL.get()).build(null));

	public static final RegistryObject<BlockEntityType<ChocolateBoxBlockEntity>> CHOCOLATE_BOX = TILES
			.register("chocolate_box", () -> BlockEntityType.Builder.of(ChocolateBoxBlockEntity::new,
					ExtraDelightBlocks.WHITE_CHOCOLATE_BOX.get(), ExtraDelightBlocks.ORANGE_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.MAGENTA_CHOCOLATE_BOX.get(), ExtraDelightBlocks.LIGHT_BLUE_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.YELLOW_CHOCOLATE_BOX.get(), ExtraDelightBlocks.LIME_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.PINK_CHOCOLATE_BOX.get(), ExtraDelightBlocks.GRAY_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.LIGHT_GRAY_CHOCOLATE_BOX.get(), ExtraDelightBlocks.CYAN_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.BLUE_CHOCOLATE_BOX.get(), ExtraDelightBlocks.BROWN_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.GREEN_CHOCOLATE_BOX.get(), ExtraDelightBlocks.RED_CHOCOLATE_BOX.get(),
					ExtraDelightBlocks.BLACK_CHOCOLATE_BOX.get(), ExtraDelightBlocks.PURPLE_CHOCOLATE_BOX.get())
					.build(null));

	public static final RegistryObject<BlockEntityType<JarBlockEntity>> JAR = TILES.register("jar",
			() -> BlockEntityType.Builder.of(JarBlockEntity::new, ExtraDelightBlocks.JAR.get()).build(null));

//	public static void addCabinets(BlockEntityTypeAddBlocksEvent event) {
//		event.modify(ModBlockEntityTypes.CABINET.get(), ExtraDelightBlocks.CINNAMON_CABINET.get(),
//				ExtraDelightBlocks.FRUIT_CABINET.get());
//		event.modify(ModBlockEntityTypes.CABINET.get(),
//				AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.CABINETS));
//	};

	@SubscribeEvent
	public static void addCabinets(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES)) {
			BlockEntityType<?> cabinetType = ModBlockEntityTypes.CABINET.get();

			Block[] additionalBlocks = ArrayUtils.addAll(
					new Block[] {
							ExtraDelightBlocks.CINNAMON_CABINET.get(),
							ExtraDelightBlocks.FRUIT_CABINET.get()
					},
					AestheticBlocks.getRegistryListAsBlocks(AestheticBlocks.CABINETS)
			);

			modifyBlockEntityTypeBlocks(cabinetType, additionalBlocks);
		}
	}

	private static void modifyBlockEntityTypeBlocks(BlockEntityType<?> type, Block[] additionalBlocks) {
		// 直接访问 validBlocks（AT 已公开）
		Set<Block> validBlocks = type.validBlocks;

		if (!(validBlocks instanceof HashSet)) {
			// 如果不是可写集合，创建新的 HashSet 并替换
			validBlocks = new HashSet<>(validBlocks);
			type.validBlocks = validBlocks;
		}

		for (Block block : additionalBlocks) {
			if (block != null) {
				validBlocks.add(block);
			}
		}
	}

	public static final RegistryObject<BlockEntityType<VatBlockEntity>> VAT = TILES.register("vat",
			() -> BlockEntityType.Builder.of(VatBlockEntity::new, ExtraDelightBlocks.VAT.get()).build(null));

	public static final RegistryObject<BlockEntityType<EvaporatorBlockEntity>> EVAPORATOR = TILES
			.register("evaporator", () -> BlockEntityType.Builder
					.of(EvaporatorBlockEntity::new, ExtraDelightBlocks.EVAPORATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<JarDisplayBlockEntity>> JAR_DISPLAY = TILES
			.register("jar_display", () -> BlockEntityType.Builder
					.of(JarDisplayBlockEntity::new, Fermentation.JAR_DISPLAY_BLOCK.get()).build(null));


    public static final RegistryObject<BlockEntityType<JuicerBlockEntity>> JUICER = TILES.register(
            "juicer",
            () -> BlockEntityType.Builder.of(JuicerBlockEntity::new, ExtraDelightBlocks.JUICER.get()).build(null));

    public static final RegistryObject<BlockEntityType<FruitBowlBlockEntity>> FRUIT_BOWL = TILES.register(
            "fruit_bowl",
            () -> BlockEntityType.Builder.of(FruitBowlBlockEntity::new, ExtraDelightBlocks.FRUIT_BOWL.get()).build(null));

}
