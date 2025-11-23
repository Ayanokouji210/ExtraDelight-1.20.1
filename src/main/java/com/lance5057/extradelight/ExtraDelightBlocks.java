package com.lance5057.extradelight;

import com.lance5057.extradelight.aesthetics.AestheticBlocks;
import com.lance5057.extradelight.blocks.*;
import com.lance5057.extradelight.blocks.chocolatebox.ChocolateBoxBlock;
import com.lance5057.extradelight.blocks.crops.*;
import com.lance5057.extradelight.blocks.crops.corn.CornBottom;
import com.lance5057.extradelight.blocks.crops.corn.CornTop;
import com.lance5057.extradelight.blocks.fluids.GlowBerryFluidBlock;
import com.lance5057.extradelight.blocks.fluids.HotFluidBlock;
import com.lance5057.extradelight.blocks.fluids.VinegarFluidBlock;
import com.lance5057.extradelight.blocks.funnel.FunnelBlock;
import com.lance5057.extradelight.blocks.jar.JarBlock;
import com.lance5057.extradelight.blocks.keg.KegBlock;
import com.lance5057.extradelight.blocks.lid.LidBlock;
import com.lance5057.extradelight.data.MiscLootTables;
import com.lance5057.extradelight.displays.candybowl.CandyBowlBlock;
import com.lance5057.extradelight.displays.fruitbowl.FruitBowlBlock;
import com.lance5057.extradelight.modules.SummerCitrus;
import com.lance5057.extradelight.workstations.chiller.ChillerBlock;
import com.lance5057.extradelight.workstations.doughshaping.DoughShapingBlock;
import com.lance5057.extradelight.workstations.dryingrack.DryingRackBlock;
import com.lance5057.extradelight.workstations.evaporator.EvaporatorBlock;
import com.lance5057.extradelight.workstations.juicer.JuicerBlock;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotBlock;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlBlock;
import com.lance5057.extradelight.workstations.mortar.MortarBlock;
import com.lance5057.extradelight.workstations.oven.OvenBlock;
import com.lance5057.extradelight.workstations.vat.VatBlock;
import com.lance5057.extradelight.worldgen.features.trees.ExtraDelightTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
//import net.neoforged.neoforge.common.ToolActions;
//import net.neoforged.neoforge.common.ToolAction;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.WildCropBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class ExtraDelightBlocks {

	public final static VoxelShape bowl = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D);
	public final static VoxelShape plate = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
	public final static VoxelShape pan = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	public final static VoxelShape pot = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
	public final static VoxelShape stand = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D);
	public final static VoxelShape fondue = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,ExtraDelight.MOD_ID);

	public static void register(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);

	}

	public static void setup() {
		//TODO: Block property of flammable
		FireBlock fire = (FireBlock) Blocks.FIRE;


		fire.setFlammable(ExtraDelightBlocks.CINNAMON_DOOR.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_FENCE.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_LEAVES.get(), 30, 60);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_LOG.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_PLANKS.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_SLAB.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_STAIRS.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_STICK_BLOCK.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_TRAPDOOR.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.CINNAMON_WOOD.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.RAW_CINNAMON_BLOCK.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.STRIPPED_CINNAMON_LOG.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.STRIPPED_CINNAMON_WOOD.get(), 5, 5);

		for (int i = 0; i < AestheticBlocks.DRIED_CORN_FENCE.size(); i++)
			fire.setFlammable(AestheticBlocks.DRIED_CORN_FENCE.get(i).get(), 5, 5);

		for (int i = 0; i < AestheticBlocks.WREATHS.size(); i++)
			fire.setFlammable(AestheticBlocks.WREATHS.get(i).get(), 5, 5);

		fire.setFlammable(ExtraDelightBlocks.FRUIT_DOOR.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_FENCE.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_LOG.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_PLANKS.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_SLAB.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_STAIRS.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_TRAPDOOR.get(), 5, 20);
		fire.setFlammable(ExtraDelightBlocks.FRUIT_WOOD.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.STRIPPED_FRUIT_LOG.get(), 5, 5);
		fire.setFlammable(ExtraDelightBlocks.STRIPPED_FRUIT_WOOD.get(), 5, 5);

		fire.setFlammable(ExtraDelightBlocks.HAZELNUT_LEAVES.get(), 30, 60);
		fire.setFlammable(ExtraDelightBlocks.CORN_COB_BUNDLE.get(), 30, 60);
		fire.setFlammable(ExtraDelightBlocks.CORN_HUSK_BUNDLE.get(), 30, 60);
		fire.setFlammable(ExtraDelightBlocks.DRIED_CORN_HUSK_BUNDLE.get(), 300, 600);

		fire.setFlammable(ExtraDelightBlocks.MINT_CROP.get(), 1000, 1000);

		fire.setFlammable(ExtraDelightBlocks.APPLE_LEAVES.get(), 30, 60);

        fire.setFlammable(SummerCitrus.LEMON_LEAVES.get(), 30, 60);
        fire.setFlammable(SummerCitrus.LIME_LEAVES.get(), 30, 60);
        fire.setFlammable(SummerCitrus.ORANGE_LEAVES.get(), 30, 60);
        fire.setFlammable(SummerCitrus.GRAPEFRUIT_LEAVES.get(), 30, 60);


    }

	public static final RegistryObject<OvenBlock> OVEN = BLOCKS.register("oven", OvenBlock::new);
	public static final RegistryObject<DryingRackBlock> DRYING_RACK = BLOCKS.register("drying_rack",
			DryingRackBlock::new);
	
	public static final RegistryObject<Block> DOUGH_SHAPING = BLOCKS.register("dough_shaping", DoughShapingBlock::new);
	public static final RegistryObject<Block> MIXING_BOWL = BLOCKS.register("mixing_bowl", MixingBowlBlock::new);

	public static final RegistryObject<Block> YEAST_POT = BLOCKS.register("yeast_pot",
			() -> new YeastPotBlock(ExtraDelightItems.YEAST, 4));
	public static final RegistryObject<Block> VINEGAR_POT = BLOCKS.register("vinegar_pot",
			() -> new YeastPotBlock(ExtraDelightItems.VINEGAR, 8));

	public static final RegistryObject<Block> MORTAR_STONE = BLOCKS.register("mortar_stone", () -> new MortarBlock());

	public static final RegistryObject<Block> VAT = BLOCKS.register("vat", VatBlock::new);
	public static final RegistryObject<Block> EVAPORATOR = BLOCKS.register("evaporator", EvaporatorBlock::new);
	public static final RegistryObject<LidBlock> LID = BLOCKS.register("lid", LidBlock::new);

    public static final RegistryObject<JuicerBlock> JUICER = BLOCKS.register("juicer",
            () -> new JuicerBlock(Properties.copy(Blocks.BARREL).noOcclusion()));

    public static final RegistryObject<Block> FRUIT_BOWL = BLOCKS.register("fruit_bowl",
            () -> new FruitBowlBlock(Properties.copy(Blocks.ACACIA_PLANKS)));


    public static final RegistryObject<Block> FLOUR = BLOCKS.register("flour", () -> new CarpetBlock(
			Properties.copy(Blocks.MOSS_BLOCK).strength(0.1F, 0.0F).sound(SoundType.WOOL).noOcclusion()));
//	public static final RegistryObject<Block> COOKING_OIL = BLOCKS.register("cooking_oil",
//			() -> new CarpetBlock(Properties.copy(Blocks.YELLOW_CARPET).strength(0.1F, 0.0F)
//					.sound(SoundType.SLIME_BLOCK).noOcclusion().friction(0.98f)));
	public static final RegistryObject<Block> BREADCRUMBS = BLOCKS.register("breadcrumbs", () -> new CarpetBlock(
			Properties.copy(Blocks.MOSS_BLOCK).strength(0.1F, 0.0F).sound(SoundType.WOOL).noOcclusion()));
	public static final RegistryObject<Block> CORNMEAL = BLOCKS.register("cornmeal", () -> new CarpetBlock(
			Properties.copy(Blocks.MOSS_BLOCK).strength(0.1F, 0.0F).sound(SoundType.WOOL).noOcclusion()));

	public static final RegistryObject<Block> SWEET_BERRY_PIE = ExtraDelightBlocks.BLOCKS.register("sweet_berry_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.SWEET_BERRY_PIE_SLICE));
	public static final RegistryObject<Block> GLOW_BERRY_PIE = ExtraDelightBlocks.BLOCKS.register("glow_berry_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.GLOW_BERRY_PIE_SLICE));
	public static final RegistryObject<Block> CHEESECAKE = ExtraDelightBlocks.BLOCKS.register("cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.CHEESECAKE_SLICE));
	public static final RegistryObject<Block> HONEY_CHEESECAKE = ExtraDelightBlocks.BLOCKS.register("honey_cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.HONEY_CHEESECAKE_SLICE));
	public static final RegistryObject<Block> CHOCOLATE_CHEESECAKE = ExtraDelightBlocks.BLOCKS.register(
			"chocolate_cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.CHOCOLATE_CHEESECAKE_SLICE));
	public static final RegistryObject<Block> PUMPKIN_CHEESECAKE = ExtraDelightBlocks.BLOCKS.register(
			"pumpkin_cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.PUMPKIN_CHEESECAKE_SLICE));
	public static final RegistryObject<Block> GLOW_BERRY_CHEESECAKE = ExtraDelightBlocks.BLOCKS
			.register("glow_berry_cheesecake", () -> new PieBlock(Properties.copy(Blocks.CAKE),
					ExtraDelightItems.GLOW_BERRY_CHEESECAKE_SLICE));

	public static final RegistryObject<Block> QUICHE = ExtraDelightBlocks.BLOCKS.register("quiche",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.QUICHE_SLICE));

	// Feasts
	public static final RegistryObject<RecipeFeastBlock> SALISBURY_STEAK_FEAST = BLOCKS.register("salisbury_steak_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, bowl));
	public static final RegistryObject<RecipeFeastBlock> MASHED_POTATO_GRAVY = BLOCKS.register(
			"mashed_potato_gravy_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, bowl));

	public static final RegistryObject<RecipeFeastBlock> HASH_FEAST = BLOCKS.register("hash_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));
	public static final RegistryObject<RecipeFeastBlock> POT_ROAST_FEAST = BLOCKS.register("potroast_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(5.0D, 2.0D, 2.0D, 11.0D, 8.0D, 14.0D),
					Block.box(2.0D, 2.0D, 5.0D, 14.0D, 8.0D, 11.0D)));

	public static final RegistryObject<RecipeFeastBlock> MEATLOAF_FEAST = BLOCKS.register("meatloaf_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(5.0D, 2.0D, 3.0D, 11.0D, 8.0D, 13.0D),
					Block.box(3.0D, 2.0D, 5.0D, 13.0D, 8.0D, 11.0D)));

	public static final RegistryObject<RecipeFeastBlock> BBQ_RIBS_FEAST = BLOCKS.register("bbq_ribs_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(2.0D, 2.0D, 2.0D, 14.0D, 6.0D, 14.0D)));
	public static final RegistryObject<RecipeFeastBlock> PULLED_PORK_FEAST = BLOCKS.register("pulled_pork_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, bowl));

	public static final RegistryObject<RecipeFeastBlock> RACK_LAMB = BLOCKS.register("rack_lamb_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 8.0D, 12.0D)));
	public static final RegistryObject<RecipeFeastBlock> STIRFRY = BLOCKS.register("stirfry_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, bowl));
	public static final RegistryObject<RecipeFeastBlock> BEEF_WELLINGTON = BLOCKS.register("beef_wellington_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(4.5D, 2.0D, 2.0D, 11.5D, 9.0D, 14.0D),
					Block.box(2.0D, 2.0D, 4.5D, 14.0D, 9.0D, 11.5D)));
	public static final RegistryObject<RecipeFeastBlock> HAGGIS = BLOCKS.register("haggis_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 10.0D, 12.0D)));

	public static final RegistryObject<JellyBlock> JELLY_WHITE = BLOCKS.register("jelly_white_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_WHITE),
					ExtraDelightItems.JELLY_WHITE::get, true));
	public static final RegistryObject<JellyBlock> JELLY_ORANGE = BLOCKS.register("jelly_orange_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_ORANGE),
					ExtraDelightItems.JELLY_ORANGE::get, true));
	public static final RegistryObject<JellyBlock> JELLY_MAGENTA = BLOCKS.register("jelly_magenta_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_MAGENTA),
					ExtraDelightItems.JELLY_MAGENTA::get, true));
	public static final RegistryObject<JellyBlock> JELLY_LIGHT_BLUE = BLOCKS.register("jelly_light_blue_block",
			() -> new JellyBlock(
					Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE),
					ExtraDelightItems.JELLY_LIGHT_BLUE::get, true));
	public static final RegistryObject<JellyBlock> JELLY_YELLOW = BLOCKS.register("jelly_yellow_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_YELLOW),
					ExtraDelightItems.JELLY_YELLOW::get, true));
	public static final RegistryObject<JellyBlock> JELLY_LIME = BLOCKS.register("jelly_lime_block",
			() -> new JellyBlock(
					Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN),
					ExtraDelightItems.JELLY_LIME::get, true));
	public static final RegistryObject<JellyBlock> JELLY_PINK = BLOCKS.register("jelly_pink_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_PINK),
					ExtraDelightItems.JELLY_PINK::get, true));
	public static final RegistryObject<JellyBlock> JELLY_GREY = BLOCKS.register("jelly_grey_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_GRAY),
					ExtraDelightItems.JELLY_GREY::get, true));
	public static final RegistryObject<JellyBlock> JELLY_LIGHT_GREY = BLOCKS.register("jelly_light_grey_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_LIGHT_GRAY),
					ExtraDelightItems.JELLY_LIGHT_GREY::get, true));
	public static final RegistryObject<JellyBlock> JELLY_CYAN = BLOCKS.register("jelly_cyan_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_CYAN),
					ExtraDelightItems.JELLY_CYAN::get, true));
	public static final RegistryObject<JellyBlock> JELLY_PURPLE = BLOCKS.register("jelly_purple_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_PURPLE),
					ExtraDelightItems.JELLY_PURPLE::get, true));
	public static final RegistryObject<JellyBlock> JELLY_BLUE = BLOCKS.register("jelly_blue_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_BLUE),
					ExtraDelightItems.JELLY_BLUE::get, true));
	public static final RegistryObject<JellyBlock> JELLY_BROWN = BLOCKS.register("jelly_brown_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_BROWN),
					ExtraDelightItems.JELLY_BROWN::get, true));
	public static final RegistryObject<JellyBlock> JELLY_GREEN = BLOCKS.register("jelly_green_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_GREEN),
					ExtraDelightItems.JELLY_GREEN::get, true));
	public static final RegistryObject<JellyBlock> JELLY_RED = BLOCKS.register("jelly_red_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_RED),
					ExtraDelightItems.JELLY_RED::get, true));
	public static final RegistryObject<JellyBlock> JELLY_BLACK = BLOCKS.register("jelly_black_block",
			() -> new JellyBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.TERRACOTTA_BLACK),
					ExtraDelightItems.JELLY_BLACK::get, true));

	public static final RegistryObject<PieBlock> MEAT_PIE_BLOCK = BLOCKS.register("meat_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE).mapColor(MapColor.COLOR_BROWN),
					ExtraDelightItems.MEAT_PIE_SLICE));

	public static final RegistryObject<Block> CHEESE_BLOCK = BLOCKS.register("cheese_block",
			() -> new Block(Properties.copy(Blocks.SPONGE).sound(SoundType.SLIME_BLOCK)
					.mapColor(MapColor.COLOR_ORANGE)));
	public static final RegistryObject<StairBlock> CHEESE_STAIRS_BLOCK = BLOCKS.register("cheese_stairs_block",
			() -> new StairBlock(CHEESE_BLOCK.get().defaultBlockState(), Properties
					.copy(Blocks.SPONGE).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<SlabBlock> CHEESE_SLAB_BLOCK = BLOCKS.register("cheese_slab_block",
			() -> new SlabBlock(Properties.copy(Blocks.SPONGE).mapColor(MapColor.COLOR_ORANGE)
					.sound(SoundType.SLIME_BLOCK)));

	public static final RegistryObject<Block> BUTTER_BLOCK = BLOCKS.register("butter_block",
			() -> new Block(Properties.copy(Blocks.ICE).mapColor(MapColor.COLOR_YELLOW)
					.friction(0.98F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<StairBlock> BUTTER_STAIRS_BLOCK = BLOCKS.register("butter_stairs_block",
			() -> new StairBlock(CHEESE_BLOCK.get().defaultBlockState(), Properties
					.copy(Blocks.SPONGE).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<SlabBlock> BUTTER_SLAB_BLOCK = BLOCKS.register("butter_slab_block",
			() -> new SlabBlock(Properties.copy(Blocks.SPONGE).mapColor(MapColor.COLOR_YELLOW)
					.sound(SoundType.SLIME_BLOCK)));

	public static final RegistryObject<RecipeFeastBlock> MACARONI_CHEESE = BLOCKS.register("macaroni_cheese_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_YELLOW), true, pot));
	public static final RegistryObject<RecipeFeastBlock> LASAGNA = BLOCKS.register("lasagna_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_YELLOW), true, pan));
	public static final RegistryObject<RecipeFeastBlock> HOTDISH = BLOCKS.register("hotdish_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_YELLOW), true, pan));

	// Stew
	public static final RegistryObject<RecipeFeastBlock> CURRY = BLOCKS.register("curry_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_ORANGE), true, pot));
	public static final RegistryObject<RecipeFeastBlock> BEEF_STEW = BLOCKS.register("beef_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));
	public static final RegistryObject<RecipeFeastBlock> PORK_STEW = BLOCKS.register("pork_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));
	public static final RegistryObject<RecipeFeastBlock> LAMB_STEW = BLOCKS.register("lamb_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));
	public static final RegistryObject<RecipeFeastBlock> RABBIT_STEW = BLOCKS.register("rabbit_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));
	public static final RegistryObject<RecipeFeastBlock> CHICKEN_STEW = BLOCKS.register("chicken_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));
	public static final RegistryObject<RecipeFeastBlock> FISH_STEW = BLOCKS.register("fish_stew_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));

	public static final RegistryObject<RecipeFeastBlock> SALAD = BLOCKS.register("salad_block",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_GREEN),
					true, Block.box(2.5D, 0.0D, 2.5D, 13.5D, 4.0D, 13.5D)));

//Halloween Start
	public static final RegistryObject<CornBottom> CORN_BOTTOM = BLOCKS.register("corn_bottom",
			() -> new CornBottom(Properties.copy(Blocks.WHEAT).mapColor(MapColor.COLOR_GREEN)
					.offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<CornTop> CORN_TOP = BLOCKS.register("corn_top", () -> new CornTop(Properties
			.copy(Blocks.WHEAT).mapColor(MapColor.COLOR_GREEN).offsetType(BlockBehaviour.OffsetType.XZ)));

	public static final RegistryObject<RecipeFeastBlock> CORNBREAD = BLOCKS.register("cornbread",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_YELLOW),
					true, pan));

	public static final RegistryObject<Block> CARAMEL_CHEESECAKE = ExtraDelightBlocks.BLOCKS.register(
			"caramel_cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.CARAMEL_CHEESECAKE_SLICE));

	public static final RegistryObject<Block> PUMPKIN_PIE = ExtraDelightBlocks.BLOCKS.register("pumpkin_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.PUMPKIN_PIE_SLICE));

	public static final RegistryObject<RecipeFeastBlock> CORN_PUDDING = BLOCKS.register("corn_pudding",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_YELLOW),
					true, pan));

	public static final RegistryObject<PieBlock> PUMPKIN_ROLL = BLOCKS.register("pumpkin_roll",
			() -> new PieBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					ExtraDelightItems.PUMPKIN_ROLL) {
				@Override
				public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
						CollisionContext context) {
					switch (state.getValue(PieBlock.FACING)) {
					case EAST:
					case WEST:
						return Block.box(2.0D, 0.0D, 4.5D, 14.0D, 7.0D, 11.5D);
					default:
						return Block.box(4.5D, 0.0D, 2.0D, 11.5D, 7.0D, 14.0D);
					}
				}
			});

	public static final RegistryObject<RecipeFeastBlock> APPLE_CRISP = BLOCKS.register("apple_crisp",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));

	public static final RegistryObject<RecipeFeastBlock> STUFFING = BLOCKS.register("stuffing",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.COLOR_BROWN), true, pot));

	public static final RegistryObject<RecipeFeastBlock> POTATO_AU_GRATIN = BLOCKS.register("potato_au_gratin",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));

	public static final RegistryObject<Block> FLOUR_SACK = BLOCKS.register("flour_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> CORNMEAL_SACK = BLOCKS.register("cornmeal_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> SUGAR_SACK = BLOCKS.register("sugar_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> CORN_SACK = BLOCKS.register("corn_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> CORN_SILK_SACK = BLOCKS.register("corn_silk_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> CORN_CRATE = BLOCKS.register("corn_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final RegistryObject<CornHuskBlock> CORN_HUSK_BUNDLE = BLOCKS.register("corn_husk_bundle",
			() -> new CornHuskBlock(
					Properties.copy(Blocks.HAY_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final RegistryObject<HayBlock> DRIED_CORN_HUSK_BUNDLE = BLOCKS.register("dried_corn_husk_bundle",
			() -> new HayBlock(Properties.copy(Blocks.HAY_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final RegistryObject<HayBlock> CORN_COB_BUNDLE = BLOCKS.register("corn_cob_bundle",
			() -> new HayBlock(Properties.copy(Blocks.HAY_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final RegistryObject<Block> BREADCRUMB_SACK = BLOCKS.register("breadcrumb_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> EGG_CRATE = BLOCKS.register("egg_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final RegistryObject<Block> APPLE_CRATE = BLOCKS.register("apple_crate",
			() -> new Block(Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_RED)));

	// Winter start!
	public static final RegistryObject<SaplingBlock> CINNAMON_SAPLING = BLOCKS.register("cinnamon_sapling",
			() -> new SaplingBlock(ExtraDelightTreeGrowers.CINNAMON,
					Properties.copy(Blocks.DARK_OAK_SAPLING)));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CINNAMON_LOG = BLOCKS.register(
			"stripped_cinnamon_log", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CINNAMON_WOOD = BLOCKS.register(
			"stripped_cinnamon_wood", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<RotatedPillarBlock> CINNAMON_WOOD = BLOCKS.register("cinnamon_wood",
			() -> new StrippableLog(STRIPPED_CINNAMON_WOOD.get(), MiscLootTables.CINNAMON_LOG,
					Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<StrippableLog> CINNAMON_LOG = BLOCKS.register("cinnamon_log",
			() -> new StrippableLog(STRIPPED_CINNAMON_LOG.get(), MiscLootTables.CINNAMON_LOG,
					Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<Block> CINNAMON_PLANKS = BLOCKS.register("cinnamon_planks",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<FenceBlock> CINNAMON_FENCE = BLOCKS.register("cinnamon_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> CINNAMON_FENCE_GATE = BLOCKS.register("cinnamon_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.OAK));
	public static final RegistryObject<StairBlock> CINNAMON_STAIRS = BLOCKS.register("cinnamon_stairs",
			() -> new StairBlock(CINNAMON_PLANKS.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> CINNAMON_DOOR = BLOCKS.register("cinnamon_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> CINNAMON_TRAPDOOR = BLOCKS.register("cinnamon_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<LeavesBlock> CINNAMON_LEAVES = BLOCKS.register("cinnamon_leaves",
			() -> new LeavesBlock(Properties.copy(Blocks.ACACIA_LEAVES)));
	public static final RegistryObject<Block> CINNAMON_CABINET = BLOCKS.register("cinnamon_cabinet",
			() -> new CabinetBlock(Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<SlabBlock> CINNAMON_SLAB = BLOCKS.register("cinnamon_slab",
			() -> new SlabBlock(Properties.copy(Blocks.DARK_OAK_SLAB)));
	public static final RegistryObject<PressurePlateBlock> CINNAMON_PRESSURE_PLATE = BLOCKS.register(
			"cinnamon_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Properties.copy(Blocks.ACACIA_PLANKS), BlockSetType.OAK));
	public static final RegistryObject<ButtonBlock> CINNAMON_BUTTON = BLOCKS.register("cinnamon_button",
			() -> new ButtonBlock(Properties.copy(Blocks.ACACIA_PLANKS),BlockSetType.OAK, 30,true));

	public static final RegistryObject<RotatedPillarBlock> CINNAMON_STICK_BLOCK = BLOCKS.register("cinnamon_stick_block",
			() -> new RotatedPillarBlock(
					Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> GROUND_CINNAMON_SACK = BLOCKS.register("ground_cinnamon_sack",
			() -> new Block(
					Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> RAW_CINNAMON_BLOCK = BLOCKS.register("raw_cinnamon_block", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_BROWN)));

	public static final RegistryObject<Block> APPLE_CHEESECAKE = ExtraDelightBlocks.BLOCKS.register("apple_cheesecake",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.APPLE_CHEESECAKE_SLICE));

	public static final RegistryObject<Block> SUGAR_COOKIE_BLOCK = BLOCKS.register("sugar_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> CHOCOLATE_CHIP_COOKIE_BLOCK = BLOCKS.register(
			"chocolate_chip_cookie_block", () -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> APPLE_COOKIE_BLOCK = BLOCKS.register("apple_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> HONEY_COOKIE_BLOCK = BLOCKS.register("honey_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> SWEET_BERRY_COOKIE_BLOCK = BLOCKS.register("sweet_berry_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> GLOW_BERRY_COOKIE_BLOCK = BLOCKS.register("glow_berry_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> PUMPKIN_COOKIE_BLOCK = BLOCKS.register("pumpkin_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> GINGERBREAD_COOKIE_BLOCK = BLOCKS.register("gingerbread_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final RegistryObject<GingerCrop> GINGER_CROP = BLOCKS.register("ginger_crop",
			() -> new GingerCrop(Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> GINGER_CRATE = BLOCKS.register("ginger_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final RegistryObject<Block> WILD_GINGER = BLOCKS.register("wild_ginger",
			() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<MintCrop> MINT_CROP = BLOCKS.register("mint_crop", MintCrop::new);
	public static final RegistryObject<Block> MINT_SACK = BLOCKS.register("mint_sack",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> GOLDEN_APPLE_CRATE = BLOCKS.register("golden_apple_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final RegistryObject<Block> BROWN_MUSHROOM_CRATE = BLOCKS.register("brown_mushroom_crate",
			() -> new Block(
					Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final RegistryObject<Block> RED_MUSHROOM_CRATE = BLOCKS.register("red_mushroom_crate",
			() -> new Block(Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_RED)));
	public static final RegistryObject<Block> SWEET_BERRY_CRATE = BLOCKS.register("sweet_berry_crate",
			() -> new Block(Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_RED)));
	public static final RegistryObject<Block> GLOW_BERRY_CRATE = BLOCKS.register("glow_berry_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_YELLOW)));

	public static final RegistryObject<CandyBowlBlock> CANDY_BOWL = BLOCKS.register("candy_bowl",
			() -> new CandyBowlBlock(Properties.copy(Blocks.GLASS)));

	public static RegistryObject<FrostableBlock> WHITE_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"white_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> LIGHT_GRAY_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("light_gray_frosted_gingerbread_block",
					() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> GRAY_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"gray_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> BLACK_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"black_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> BROWN_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"brown_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> RED_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"red_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> ORANGE_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"orange_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> YELLOW_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"yellow_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> LIME_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"lime_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> GREEN_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"green_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> CYAN_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"cyan_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> LIGHT_BLUE_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS
			.register("light_blue_frosted_gingerbread_block",
					() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> BLUE_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"blue_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> PURPLE_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"purple_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> MAGENTA_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"magenta_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static RegistryObject<FrostableBlock> PINK_FROSTED_GINGERBREAD_BLOCK = ExtraDelightBlocks.BLOCKS.register(
			"pink_frosted_gingerbread_block",
			() -> new FrostableBlock(Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final RegistryObject<Block> CANDY_CANE_RED_BLOCK = BLOCKS.register("candy_cane_red_block",
			() -> new Block(Properties.copy(Blocks.STONE)));
	public static final RegistryObject<Block> CANDY_CANE_GREEN_BLOCK = BLOCKS.register("candy_cane_green_block",
			() -> new Block(Properties.copy(Blocks.STONE)));
	public static final RegistryObject<Block> CANDY_CANE_BLUE_BLOCK = BLOCKS.register("candy_cane_blue_block",
			() -> new Block(Properties.copy(Blocks.STONE)));

	public static final RegistryObject<RecipeFeastBlock> CINNAMON_ROLLS = BLOCKS.register("cinnamon_rolls",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));
	public static final RegistryObject<RecipeFeastBlock> MONKEY_BREAD = BLOCKS.register("monkey_bread",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
					Block.box(3.0D, 1.0D, 3.0D, 13.0D, 5.0D, 13.0D)));
	public static final RegistryObject<Block> COFFEE_CAKE = ExtraDelightBlocks.BLOCKS.register("coffee_cake",
			() -> new CakeBlock(Properties.copy(Blocks.CAKE)));
	public static final RegistryObject<RecipeFeastBlock> CHRISTMAS_PUDDING = BLOCKS.register("christmas_pudding",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, stand));
	public static final RegistryObject<RecipeFeastBlock> PUNCH = BLOCKS.register("punch",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.GLASS).mapColor(MapColor.COLOR_PINK), true,
					Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D)));
	public static final RegistryObject<PieBlock> MILK_TART = ExtraDelightBlocks.BLOCKS.register("milk_tart",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.MILK_TART_SLICE) {
				@Override
				public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
						CollisionContext context) {
					return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
				}
			});
	public static final RegistryObject<RecipeFeastBlock> MINT_LAMB = BLOCKS.register("mint_lamb",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D),
					Block.box(4.0D, 0.0D, 0.0D, 12.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 4.0D, 16.0D, 1.0D, 12.0D),
					Block.box(4.0D, 0.0D, 0.0D, 12.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 4.0D, 16.0D, 4.0D, 12.0D)));
	public static final RegistryObject<RecipeFeastBlock> CHARCUTERIE_BOARD = BLOCKS.register("charcuterie_board",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(1.0D, 1.0D, 1.0D, 15.0D, 5.0D, 15.0D)));

	public static final RegistryObject<TapBlock> TAP = BLOCKS.register("tap",
			() -> new TapBlock(Properties.copy(Blocks.STONE)));

	public static final RegistryObject<HorizontalPanBlock> SHEET_BLOCK = BLOCKS.register("sheet",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(1.0D, 0.0D, 0.0D, 15.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 1.0D, 16.0D, 1.0D, 15.0D)));
	public static final RegistryObject<HorizontalPanBlock> TRAY_BLOCK = BLOCKS.register("tray",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(1.0D, 0.0D, 0.0D, 15.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 1.0D, 16.0D, 2.0D, 15.0D)));
	public static final RegistryObject<HorizontalPanBlock> LOAF_PAN_BLOCK = BLOCKS.register("loaf_pan",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(4.0D, 0.0D, 2.0D, 12.0D, 4.0D, 14.0D), Block.box(2.0D, 0.0D, 4.0D, 14.0D, 4.0D, 12.0D)));
	public static final RegistryObject<HorizontalPanBlock> PIE_DISH_BLOCK = BLOCKS.register("pie_dish",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D)));
	public static final RegistryObject<HorizontalPanBlock> SQUARE_PAN_BLOCK = BLOCKS.register("square_pan",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D), Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D)));
	public static final RegistryObject<BakingStoneBlock> BAKING_STONE_BLOCK = BLOCKS.register("baking_stone",
			() -> new BakingStoneBlock(Properties.copy(Blocks.DIRT).sound(SoundType.STONE)));
	public static final RegistryObject<HorizontalPanBlock> MUFFIN_TIN_BLOCK = BLOCKS.register("muffin_tray",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(3.5D, 0.0D, 1.5D, 12.5D, 2.0D, 14.5D), Block.box(1.5D, 0.0D, 3.5D, 14.5D, 2.0D, 12.5D)));
	public static final RegistryObject<HorizontalPanBlock> SERVING_POT_BLOCK = BLOCKS.register("serving_pot",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.LANTERN),
					Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D)));
	public static final RegistryObject<HorizontalPanBlock> BAR_MOLD = BLOCKS.register("bar_mold",
			() -> new HorizontalPanBlock(Properties.copy(Blocks.DIRT).sound(SoundType.METAL),
					Block.box(1.0D, 0.0D, 0.0D, 15.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 1.0D, 16.0D, 2.0D, 15.0D)));

	public static final RegistryObject<BushStageFour> COFFEE_BUSH = ExtraDelightBlocks.BLOCKS.register("coffee_bush",
			() -> new BushStageFour(Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0),
					Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0), ExtraDelightItems.COFFEE_CHERRIES,
					Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission()
							.sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));

	public static final RegistryObject<PeanutCrop> PEANUT_CROP = ExtraDelightBlocks.BLOCKS.register("peanut_crop",
			() -> new PeanutCrop(Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission()
					.sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> WILD_PEANUT = BLOCKS.register("wild_peanut",
			() -> new WildCropBlock(MobEffects.ABSORPTION, 6, Properties.copy(Blocks.TALL_GRASS)));

	public static final RegistryObject<ChiliCrop> CHILI_CROP = BLOCKS.register("chili_crop",
			() -> new ChiliCrop(Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> WILD_CHILI = BLOCKS.register("wild_chili",
			() -> new WildCropBlock(MobEffects.DAMAGE_BOOST, 6, Properties.copy(Blocks.TALL_GRASS)));

	public static final RegistryObject<MallowRootCrop> MALLOW_ROOT_CROP = BLOCKS.register("mallow_root_crop",
			() -> new MallowRootCrop(Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> WILD_MALLOW_ROOT = BLOCKS.register("wild_mallow_root",
			() -> new WildCropBlock(MobEffects.SLOW_FALLING, 6, Properties.copy(Blocks.TALL_GRASS)));

	// Fluids
	public static final RegistryObject<LiquidBlock> COOKING_OIL_FLUID_BLOCK = BLOCKS.register("cooking_oil_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.OIL.FLUID.get(),
					Properties.copy(Blocks.WATER).noCollission().strength(100.0F).liquid()
							.noLootTable().replaceable().randomTicks().pushReaction(PushReaction.DESTROY)));

	public static final RegistryObject<VinegarFluidBlock> VINEGAR_FLUID_BLOCK = BLOCKS.register("vinegar_fluid_block",
			() -> new VinegarFluidBlock(ExtraDelightFluids.VINEGAR.FLUID.get(),
					Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));

	public static final RegistryObject<LiquidBlock> GRAVY_FLUID_BLOCK = BLOCKS.register("gravy_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.GRAVY.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> GLOW_BERRY_JUICE_FLUID_BLOCK = BLOCKS.register(
			"glow_berry_fluid_block",
			() -> new GlowBerryFluidBlock(ExtraDelightFluids.GLOW_BERRY_JUICE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> SWEET_BERRY_JUICE_FLUID_BLOCK = BLOCKS.register(
			"sweet_berry_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.SWEET_BERRY_JUICE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> TOMATO_JUICE_FLUID_BLOCK = BLOCKS.register(
			"tomato_juice_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.TOMATO_JUICE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> CACTUS_JUICE_FLUID_BLOCK = BLOCKS.register(
			"cactus_juice_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.CACTUS_JUICE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> APPLE_CIDER_FLUID_BLOCK = BLOCKS.register("apple_cider_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.APPLE_CIDER.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> HOT_COCOA_FLUID_BLOCK = BLOCKS.register("hot_cocoa_fluid_block",
			() -> new HotFluidBlock(ExtraDelightFluids.HOT_COCOA.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> MELON_JUICE_FLUID_BLOCK = BLOCKS.register("melon_juice_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.MELON_JUICE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> EGG_MIX_FLUID_BLOCK = BLOCKS.register("egg_mix_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.EGG_MIX.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> BBQ_FLUID_BLOCK = BLOCKS.register("bbq_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.BBQ.FLUID.get(), Properties.copy(Blocks.WATER)
					.noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> KETCHUP_FLUID_BLOCK = BLOCKS.register("ketchup_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.KETCHUP.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> MAYO_FLUID_BLOCK = BLOCKS.register("mayo_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.MAYO.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> BROTH_FLUID_BLOCK = BLOCKS.register("broth_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.BROTH.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> CARAMEL_SAUCE_FLUID_BLOCK = BLOCKS.register(
			"caramel_sauce_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.CARAMEL_SAUCE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> MILKSHAKE_FLUID_BLOCK = BLOCKS.register("milkshake_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.MILKSHAKE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> WHIPPED_CREAM_FLUID_BLOCK = BLOCKS.register(
			"whipped_cream_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> JAM_FLUID_BLOCK = BLOCKS.register("jam_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.JAM.FLUID.get(), Properties.copy(Blocks.WATER)
					.noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> GOLDEN_JAM_FLUID_BLOCK = BLOCKS.register("golden_jam_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.GOLDEN_JAM.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> GLOW_JAM_FLUID_BLOCK = BLOCKS.register("glow_jam_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.GLOW_JAM.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> TEA_FLUID_BLOCK = BLOCKS.register("tea_fluid_block",
			() -> new HotFluidBlock(ExtraDelightFluids.TEA.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<KegBlock> KEG = BLOCKS.register("keg_block",
			() -> new KegBlock(Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<JarBlock> JAR = BLOCKS.register("jar",
			() -> new JarBlock(Properties.copy(Blocks.GLASS)));

	public static final RegistryObject<RotatedPillarBlock> STRIPPED_FRUIT_LOG = BLOCKS.register("stripped_fruit_log",
			() -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<RotatedPillarBlock> FRUIT_WOOD = BLOCKS.register("fruit_wood",
			() -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)) {
				@Override
				public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction ToolAction,
						boolean simulate) {
					if (ToolAction == ToolActions.AXE_STRIP)
						return ExtraDelightBlocks.STRIPPED_FRUIT_WOOD.get().defaultBlockState()
								.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
					return null;
				}
			});
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_FRUIT_WOOD = BLOCKS.register("stripped_fruit_wood",
			() -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));
	public static final RegistryObject<RotatedPillarBlock> FRUIT_LOG = BLOCKS.register("fruit_log",
			() -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)) {
				@Override
				public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction ToolAction,
						boolean simulate) {
					if (ToolAction == ToolActions.AXE_STRIP)
						return ExtraDelightBlocks.STRIPPED_FRUIT_LOG.get().defaultBlockState()
								.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
					return null;
				}
			});
	public static final RegistryObject<Block> FRUIT_PLANKS = BLOCKS.register("fruit_planks",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<FenceBlock> FRUIT_FENCE = BLOCKS.register("fruit_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> FRUIT_FENCE_GATE = BLOCKS.register("fruit_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.OAK));
	public static final RegistryObject<StairBlock> FRUIT_STAIRS = BLOCKS.register("fruit_stairs",
			() -> new StairBlock(FRUIT_PLANKS.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> FRUIT_DOOR = BLOCKS.register("fruit_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> FRUIT_TRAPDOOR = BLOCKS.register("fruit_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<Block> FRUIT_CABINET = BLOCKS.register("fruit_cabinet",
			() -> new CabinetBlock(Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<SlabBlock> FRUIT_SLAB = BLOCKS.register("fruit_slab",
			() -> new SlabBlock(Properties.copy(Blocks.DARK_OAK_SLAB)));
	public static final RegistryObject<PressurePlateBlock> FRUIT_PRESSURE_PLATE = BLOCKS.register("fruit_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Properties.copy(Blocks.ACACIA_PLANKS), BlockSetType.OAK));
	public static final RegistryObject<ButtonBlock> FRUIT_BUTTON = BLOCKS.register("fruit_button",
			() -> new ButtonBlock(Properties.copy(Blocks.ACACIA_PLANKS),BlockSetType.OAK, 30,true));

//	public static final RegistryObject<CeilingHangingSignBlock> FRUIT_CEILING_HANGING_SIGN = BLOCKS.register(
//			"fruit_ceiling_hanging_sign",
//			() -> new CeilingHangingSignBlock(WoodType.OAK, Block.Properties.copy(Blocks.ACACIA_PLANKS)));
//	public static final RegistryObject<WallHangingSignBlock> FRUIT_WALL_HANGING_SIGN = BLOCKS.register(
//			"fruit_wall_hanging_sign",
//			() -> new WallHangingSignBlock(WoodType.OAK, Block.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<StandingSignBlock> FRUIT_STANDING_SIGN = BLOCKS.register("fruit_standing_sign",
			() -> new StandingSignBlock(Properties.copy(Blocks.ACACIA_PLANKS), WoodType.OAK));
	public static final RegistryObject<WallSignBlock> FRUIT_WALL_SIGN = BLOCKS.register("fruit_wall_sign",
			() -> new WallSignBlock(Properties.copy(Blocks.ACACIA_PLANKS), WoodType.OAK));

    public static final RegistryObject<Block> HAZELNUT_PETAL_LITTER = ExtraDelightBlocks.BLOCKS.register(
            "hazelnut_petal_litter", () -> new CarpetBlock(Block.Properties.copy(Blocks.ACACIA_LEAVES)));

	public static final RegistryObject<FruitLeafBlock> HAZELNUT_LEAVES = BLOCKS.register("hazelnut_leaves",
			() -> new FruitLeafBlock(Properties.copy(Blocks.ACACIA_LEAVES),
					ExtraDelightItems.HAZELNUTS_IN_SHELL, ExtraDelightBlocks.HAZELNUT_PETAL_LITTER,
                    ExtraDelightParticles.HAZELNUT_PETALS));
	public static final RegistryObject<SaplingBlock> HAZELNUT_SAPLING = BLOCKS.register("hazelnut_sapling",
			() -> new SaplingBlock(ExtraDelightTreeGrowers.HAZELNUT,
					Properties.copy(Blocks.DARK_OAK_SAPLING)));

	public static final RegistryObject<Block> MILK_CHOCOLATE_BLOCK = BLOCKS.register("milk_chocolate_block",
			() -> new ChocolateStyleBlock(Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<FenceBlock> MILK_CHOCOLATE_FENCE = BLOCKS.register("milk_chocolate_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> MILK_CHOCOLATE_FENCE_GATE = BLOCKS.register(
			"milk_chocolate_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.ACACIA));
	public static final RegistryObject<StairBlock> MILK_CHOCOLATE_STAIRS = BLOCKS.register("milk_chocolate_stairs",
			() -> new StairBlock(MILK_CHOCOLATE_BLOCK.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> MILK_CHOCOLATE_DOOR = BLOCKS.register("milk_chocolate_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.ACACIA));
	public static final RegistryObject<TrapDoorBlock> MILK_CHOCOLATE_TRAPDOOR = BLOCKS.register(
			"milk_chocolate_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.ACACIA));
	public static final RegistryObject<SlabBlock> MILK_CHOCOLATE_SLAB = BLOCKS.register("milk_chocolate_slab",
			() -> new SlabBlock(Properties.copy(Blocks.ACACIA_SLAB)));
	public static final RegistryObject<RotatedPillarBlock> MILK_CHOCOLATE_PILLAR = BLOCKS.register(
			"milk_chocolate_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));

	public static final RegistryObject<Block> DARK_CHOCOLATE_BLOCK = BLOCKS.register("dark_chocolate_block",
			() -> new ChocolateStyleBlock(Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<FenceBlock> DARK_CHOCOLATE_FENCE = BLOCKS.register("dark_chocolate_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> DARK_CHOCOLATE_FENCE_GATE = BLOCKS.register(
			"dark_chocolate_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.ACACIA));
	public static final RegistryObject<StairBlock> DARK_CHOCOLATE_STAIRS = BLOCKS.register("dark_chocolate_stairs",
			() -> new StairBlock(DARK_CHOCOLATE_BLOCK.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> DARK_CHOCOLATE_DOOR = BLOCKS.register("dark_chocolate_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.ACACIA));
	public static final RegistryObject<TrapDoorBlock> DARK_CHOCOLATE_TRAPDOOR = BLOCKS.register(
			"dark_chocolate_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.ACACIA));
	public static final RegistryObject<SlabBlock> DARK_CHOCOLATE_SLAB = BLOCKS.register("dark_chocolate_slab",
			() -> new SlabBlock(Properties.copy(Blocks.ACACIA_SLAB)));
	public static final RegistryObject<RotatedPillarBlock> DARK_CHOCOLATE_PILLAR = BLOCKS.register(
			"dark_chocolate_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));

	public static final RegistryObject<Block> WHITE_CHOCOLATE_BLOCK = BLOCKS.register("white_chocolate_block",
			() -> new ChocolateStyleBlock(Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<FenceBlock> WHITE_CHOCOLATE_FENCE = BLOCKS.register("white_chocolate_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> WHITE_CHOCOLATE_FENCE_GATE = BLOCKS.register(
			"white_chocolate_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.ACACIA));
	public static final RegistryObject<StairBlock> WHITE_CHOCOLATE_STAIRS = BLOCKS.register("white_chocolate_stairs",
			() -> new StairBlock(WHITE_CHOCOLATE_BLOCK.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> WHITE_CHOCOLATE_DOOR = BLOCKS.register("white_chocolate_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.ACACIA));
	public static final RegistryObject<TrapDoorBlock> WHITE_CHOCOLATE_TRAPDOOR = BLOCKS.register(
			"white_chocolate_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.ACACIA));
	public static final RegistryObject<SlabBlock> WHITE_CHOCOLATE_SLAB = BLOCKS.register("white_chocolate_slab",
			() -> new SlabBlock(Properties.copy(Blocks.ACACIA_SLAB)));
	public static final RegistryObject<RotatedPillarBlock> WHITE_CHOCOLATE_PILLAR = BLOCKS.register(
			"white_chocolate_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));

	public static final RegistryObject<Block> BLOOD_CHOCOLATE_BLOCK = BLOCKS.register("blood_chocolate_block",
			() -> new ChocolateStyleBlock(Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<FenceBlock> BLOOD_CHOCOLATE_FENCE = BLOCKS.register("blood_chocolate_fence",
			() -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
	public static final RegistryObject<FenceGateBlock> BLOOD_CHOCOLATE_FENCE_GATE = BLOCKS.register(
			"blood_chocolate_fence_gate",
			() -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE), WoodType.ACACIA));
	public static final RegistryObject<StairBlock> BLOOD_CHOCOLATE_STAIRS = BLOCKS.register("blood_chocolate_stairs",
			() -> new StairBlock(BLOOD_CHOCOLATE_BLOCK.get().defaultBlockState(),
					Properties.copy(Blocks.ACACIA_STAIRS)));
	public static final RegistryObject<DoorBlock> BLOOD_CHOCOLATE_DOOR = BLOCKS.register("blood_chocolate_door",
			() -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR), BlockSetType.ACACIA));
	public static final RegistryObject<TrapDoorBlock> BLOOD_CHOCOLATE_TRAPDOOR = BLOCKS.register(
			"blood_chocolate_trapdoor",
			() -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR), BlockSetType.ACACIA));
	public static final RegistryObject<SlabBlock> BLOOD_CHOCOLATE_SLAB = BLOCKS.register("blood_chocolate_slab",
			() -> new SlabBlock(Properties.copy(Blocks.ACACIA_SLAB)));
	public static final RegistryObject<RotatedPillarBlock> BLOOD_CHOCOLATE_PILLAR = BLOCKS.register(
			"blood_chocolate_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));

	public static final RegistryObject<Block> MELTING_POT = BLOCKS.register("melting_pot",
			() -> new MeltingPotBlock(Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> CHILLER = BLOCKS.register("chiller", () -> new ChillerBlock());
	public static final RegistryObject<Block> FUNNEL = BLOCKS.register("funnel",
			() -> new FunnelBlock(Properties.copy(Blocks.COPPER_BLOCK)));

	public static final RegistryObject<ChocolateBoxBlock> WHITE_CHOCOLATE_BOX = BLOCKS.register("white_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.WHITE, Properties.copy(Blocks.WHITE_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> ORANGE_CHOCOLATE_BOX = BLOCKS.register("orange_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.ORANGE, Properties.copy(Blocks.ORANGE_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> MAGENTA_CHOCOLATE_BOX = BLOCKS.register(
			"magenta_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.MAGENTA, Properties.copy(Blocks.MAGENTA_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> LIGHT_BLUE_CHOCOLATE_BOX = BLOCKS.register(
			"light_blue_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.LIGHT_BLUE, Properties.copy(Blocks.LIGHT_BLUE_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> YELLOW_CHOCOLATE_BOX = BLOCKS.register("yellow_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.YELLOW, Properties.copy(Blocks.YELLOW_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> LIME_CHOCOLATE_BOX = BLOCKS.register("lime_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.LIME, Properties.copy(Blocks.LIME_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> PINK_CHOCOLATE_BOX = BLOCKS.register("pink_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.PINK, Properties.copy(Blocks.PINK_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> GRAY_CHOCOLATE_BOX = BLOCKS.register("gray_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.GRAY, Properties.copy(Blocks.GRAY_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> LIGHT_GRAY_CHOCOLATE_BOX = BLOCKS.register(
			"light_gray_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.LIGHT_GRAY, Properties.copy(Blocks.LIGHT_GRAY_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> CYAN_CHOCOLATE_BOX = BLOCKS.register("cyan_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.CYAN, Properties.copy(Blocks.CYAN_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> BLUE_CHOCOLATE_BOX = BLOCKS.register("blue_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.BLUE, Properties.copy(Blocks.BLUE_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> BROWN_CHOCOLATE_BOX = BLOCKS.register("brown_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.BROWN, Properties.copy(Blocks.BROWN_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> GREEN_CHOCOLATE_BOX = BLOCKS.register("green_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.GREEN, Properties.copy(Blocks.GREEN_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> RED_CHOCOLATE_BOX = BLOCKS.register("red_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.RED, Properties.copy(Blocks.RED_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> BLACK_CHOCOLATE_BOX = BLOCKS.register("black_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.BLACK, Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<ChocolateBoxBlock> PURPLE_CHOCOLATE_BOX = BLOCKS.register("purple_chocolate_box",
			() -> new ChocolateBoxBlock(DyeColor.PURPLE, Properties.copy(Blocks.PURPLE_WOOL)));

	public static final RegistryObject<RecipeFeastBlock> BROWNIES = BLOCKS.register("brownies",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));
	public static final RegistryObject<RecipeFeastBlock> BLONDIES = BLOCKS.register("blondies",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.YELLOW_WOOL).mapColor(MapColor.COLOR_YELLOW),
					true, pan));
	public static final RegistryObject<Block> CHOCOLATE_CAKE = ExtraDelightBlocks.BLOCKS.register("chocolate_cake",
			() -> new CakeBlock(Properties.copy(Blocks.CAKE)));
	public static final RegistryObject<RecipeFeastBlock> FUDGE = BLOCKS.register("fudge",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D),
					Block.box(0.0D, 0.0D, 1.0D, 16.0D, 2.0D, 15.0D), Block.box(1.0D, 0.0D, 0.0D, 15.0D, 2.0D, 16.0D)));
	public static final RegistryObject<RecipeFeastBlock> STICKY_TOFFEE_PUDDING = BLOCKS.register("sticky_toffee_pudding",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, stand));
	public static final RegistryObject<RecipeFeastBlock> CRISP_RICE_TREATS = BLOCKS.register("crisp_rice_treats",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.YELLOW_WOOL).mapColor(MapColor.COLOR_YELLOW),
					true, pan));
	public static final RegistryObject<RecipeFeastBlock> SCOTCHAROOS = BLOCKS.register("scotcharoos",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));
	public static final RegistryObject<RecipeFeastBlock> BLACK_FOREST_TRIFLE = BLOCKS.register("black_forest_trifle",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, Block.box(2.0D, 2.0D, 2.0D, 14.0D, 7.0D, 14.0D)));

	public static final RegistryObject<RecipeFondueFeastBlock> MILK_CHOCOLATE_FONDUE = BLOCKS
			.register("milk_chocolate_fondue",
					() -> new RecipeFondueFeastBlock(
							Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN), true,
							fondue));
	public static final RegistryObject<RecipeFondueFeastBlock> DARK_CHOCOLATE_FONDUE = BLOCKS
			.register("dark_chocolate_fondue",
					() -> new RecipeFondueFeastBlock(
							Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN), true,
							fondue));
	public static final RegistryObject<RecipeFondueFeastBlock> WHITE_CHOCOLATE_FONDUE = BLOCKS
			.register("white_chocolate_fondue",
					() -> new RecipeFondueFeastBlock(
							Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN), true,
							fondue));
	public static final RegistryObject<RecipeFondueFeastBlock> BLOOD_CHOCOLATE_FONDUE = BLOCKS
			.register("blood_chocolate_fondue",
					() -> new RecipeFondueFeastBlock(
							Properties.copy(Blocks.BROWN_WOOL).mapColor(MapColor.COLOR_BROWN), true,
							fondue));

	public static final RegistryObject<LiquidBlock> COCOA_BUTTER_FLUID_BLOCK = BLOCKS.register(
			"cocoa_butter_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.COCOA_BUTTER.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> WHITE_CHOCOLATE_SYRUP_FLUID_BLOCK = BLOCKS.register(
			"white_chocolate_syrup_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.WHITE_CHOCOLATE_SYRUP.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> DARK_CHOCOLATE_SYRUP_FLUID_BLOCK = BLOCKS.register(
			"dark_chocolate_syrup_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.DARK_CHOCOLATE_SYRUP.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> MILK_CHOCOLATE_SYRUP_FLUID_BLOCK = BLOCKS.register(
			"milk_chocolate_syrup_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.MILK_CHOCOLATE_SYRUP.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> BLOOD_CHOCOLATE_SYRUP_FLUID_BLOCK = BLOCKS.register(
			"blood_chocolate_syrup_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.BLOOD_CHOCOLATE_SYRUP.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<LiquidBlock> COFFEE_FLUID_BLOCK = BLOCKS.register("coffee_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.COFFEE.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> NUT_BUTTER_FLUID_BLOCK = BLOCKS.register("peanut_butter_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.NUT_BUTTER.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> MARSHMALLOW_FLUFF_FLUID_BLOCK = BLOCKS.register(
			"marshmallow_fluff_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.MARSHMALLOW_FLUFF.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));
	public static final RegistryObject<LiquidBlock> COCOA_NUT_BUTTER_SPREAD_FLUID_BLOCK = BLOCKS.register(
			"cocoa_nut_butter_spread_fluid_block",
			() -> new LiquidBlock(ExtraDelightFluids.COCOA_NUT_BUTTER_SPREAD.FLUID.get(), Properties
					.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable().liquid()));

	public static final RegistryObject<Block> MARSHMALLOW_BLOCK = BLOCKS.register("marshmallow_block",
			() -> new Block(Properties.copy(Blocks.WHITE_WOOL)));
	public static final RegistryObject<Block> GOLDEN_CARROT_CRATE = BLOCKS.register("golden_carrot_crate",
			() -> new Block(
					Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_YELLOW)));

    public static final RegistryObject<Block> APPLE_PETAL_LITTER = ExtraDelightBlocks.BLOCKS
            .register("apple_petal_litter", () -> new CarpetBlock(Block.Properties.copy(Blocks.ACACIA_LEAVES)));

    public static final RegistryObject<VanillaFruitLeafBlock> APPLE_LEAVES = BLOCKS.register("apple_leaves",
			() -> new VanillaFruitLeafBlock(Properties.copy(Blocks.ACACIA_LEAVES), Items.APPLE,
                    APPLE_PETAL_LITTER));
	public static final RegistryObject<SaplingBlock> APPLE_SAPLING = BLOCKS.register("apple_sapling",
			() -> new SaplingBlock(ExtraDelightTreeGrowers.APPLE,
					Properties.copy(Blocks.DARK_OAK_SAPLING)));
	public static final RegistryObject<RecipeFeastBlock> PORK_AND_APPLES_FEAST = BLOCKS.register("pork_apple_roast",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, plate, Block.box(5.0D, 2.0D, 2.0D, 11.0D, 8.0D, 14.0D),
					Block.box(2.0D, 2.0D, 5.0D, 14.0D, 8.0D, 11.0D)));
	public static final RegistryObject<RecipeFeastBlock> STUFFED_APPLES_FEAST = BLOCKS.register("stuffed_apples",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_BROWN),
					true, pan));
	public static final RegistryObject<PieBlock> TARTE_TATIN = ExtraDelightBlocks.BLOCKS.register("tarte_tatin",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.TARTE_TATIN_SLICE) {
				@Override
				public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
						CollisionContext context) {
					return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
				}
			});

	public static final RegistryObject<Block> HANGING_ONIONS = BLOCKS.register("hanging_onions",
			() -> new HangingBlock(Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.WOOD)));
	public static final RegistryObject<Block> HANGING_MINT = BLOCKS.register("hanging_mint",
			() -> new HangingBlock(Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.PLANT)) {
				@Override
				public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
						CollisionContext context) {
					return Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);
				}
			});
	public static final RegistryObject<Block> HANGING_HAM = BLOCKS.register("hanging_ham", () -> new HangingBlock(
			Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.TERRACOTTA_ORANGE)));
	public static final RegistryObject<Block> HANGING_CHILI = BLOCKS.register("hanging_chili", () -> new HangingBlock(
			Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.CRIMSON_NYLIUM)));
	public static final RegistryObject<Block> HANGING_DRIED_CHILI = BLOCKS.register("hanging_dried_chili",
			() -> new HangingBlock(
					Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.TERRACOTTA_RED)));
	public static final RegistryObject<Block> HANGING_CORN = BLOCKS.register("hanging_corn", () -> new HangingBlock(
			Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.TERRACOTTA_YELLOW)));

	public static final RegistryObject<Block> CHILI_CRATE = BLOCKS.register("chili_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.CRIMSON_NYLIUM)));
	public static final RegistryObject<Block> CHILI_POWDER_SACK = BLOCKS.register("chili_powder_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_RED)));
	public static final RegistryObject<RecipeFeastBlock> CHILI_CON_CARNE_FEAST = BLOCKS.register("chili_con_carne",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.NETHER), true, pot));
	public static final RegistryObject<RecipeFeastBlock> WHITE_CHILI_FEAST = BLOCKS.register("white_chili",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.IRON_BLOCK).strength(0.8F)
					.sound(SoundType.LANTERN).mapColor(MapColor.RAW_IRON), true, pot));

	public static final RegistryObject<Block> PEANUT_IN_SHELL_SACK = BLOCKS.register("peanut_in_shell_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.DIRT)));
	public static final RegistryObject<Block> PEANUT_SACK = BLOCKS.register("peanut_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.COLOR_ORANGE)));
	public static final RegistryObject<Block> ROASTED_PEANUT_SACK = BLOCKS.register("roasted_peanut_sack",
			() -> new Block(
					Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_ORANGE)));
	public static final RegistryObject<Block> NUT_BUTTER_COOKIE_BLOCK = BLOCKS.register("nut_butter_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final RegistryObject<Block> HAZELNUT_IN_SHELL_SACK = BLOCKS.register("hazelnut_in_shell_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.DIRT)));
	public static final RegistryObject<Block> HAZELNUT_SACK = BLOCKS.register("hazelnut_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.COLOR_ORANGE)));
	public static final RegistryObject<Block> ROASTED_HAZELNUT_SACK = BLOCKS.register("roasted_hazelnut_sack",
			() -> new Block(
					Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_ORANGE)));
	public static final RegistryObject<Block> MISSISSIPPI_MUD_PIE = ExtraDelightBlocks.BLOCKS.register(
			"mississippi_mud_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.MISSISSIPPI_MUD_PIE_SLICE));

	public static final RegistryObject<Block> MALLOW_ROOT_CRATE = BLOCKS.register("mallow_root_crate",
			() -> new Block(Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.WOOD)));
	public static final RegistryObject<Block> MALLOW_POWDER_SACK = BLOCKS.register("mallow_powder_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.QUARTZ)));
	public static final RegistryObject<Block> GRASSHOPPER_PIE = ExtraDelightBlocks.BLOCKS.register("grasshopper_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.GRASSHOPPER_PIE_SLICE));
	public static final RegistryObject<RecipeFeastBlock> MARSHMALLOW_SLICE_FEAST = BLOCKS.register("marshmallow_slice",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_PINK),
					true, pan));

	public static final RegistryObject<Block> COFFEE_CHERRY_CRATE = BLOCKS.register("coffee_cherry_crate",
			() -> new Block(
					Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.COLOR_YELLOW)));
	public static final RegistryObject<Block> GREEN_COFFEE_BEAN_SACK = BLOCKS.register("green_coffee_beans_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.COLOR_GREEN)));
	public static final RegistryObject<Block> COFFEE_BEAN_SACK = BLOCKS.register("coffee_bean_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> GROUND_COFFEE_SACK = BLOCKS.register("ground_coffee_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_RED)));

	public static final RegistryObject<Block> COCOA_BEAN_SACK = BLOCKS.register("cocoa_beans_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final RegistryObject<Block> ROASTED_COCOA_BEAN_SACK = BLOCKS.register("roasted_cocoa_beans_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.COLOR_RED)));
	public static final RegistryObject<Block> COCOA_SOLIDS_SACK = BLOCKS.register("cocoa_solids_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.CRIMSON_HYPHAE)));
	public static final RegistryObject<Block> COCOA_POWDER_SACK = BLOCKS.register("cocoa_powder_sack",
			() -> new Block(Properties.copy(ModBlocks.RICE_BAG.get()).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final RegistryObject<Block> CHOCOLATE_COOKIE_BLOCK = BLOCKS.register("chocolate_cookie_block",
			() -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final RegistryObject<Block> BACON_EGG_PIE = ExtraDelightBlocks.BLOCKS.register("bacon_egg_pie",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.BACON_EGG_PIE_SLICE));
	public static final RegistryObject<Block> PANFORTE = ExtraDelightBlocks.BLOCKS.register("panforte",
			() -> new PieBlock(Properties.copy(Blocks.CAKE), ExtraDelightItems.PANFORTE_SLICE) {
				@Override
				public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
						CollisionContext context) {
					return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
				}
			});

	public static final RegistryObject<GarlicCrop> GARLIC_CROP = BLOCKS.register("garlic_crop",
			() -> new GarlicCrop(Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> WILD_GARLIC = BLOCKS.register("wild_garlic",
			() -> new WildCropBlock(MobEffects.LEVITATION, 6, Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<RecipeFeastBlock> BRUSCHETTA_FEAST = BLOCKS.register("bruschetta_feast",
			() -> new RecipeFeastBlock(Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.COLOR_RED),
					true, plate, Block.box(1.0D, 1.0D, 1.0D, 15.0D, 5.0D, 15.0D)));
	public static final RegistryObject<Block> HANGING_GARLIC = BLOCKS.register("hanging_garlic",
			() -> new HangingBlock(Properties.copy(Blocks.ACACIA_LEAVES).mapColor(MapColor.WOOD)));
	public static final RegistryObject<Block> GARLIC_CRATE = BLOCKS.register("garlic_crate", () -> new Block(
			Properties.copy(ModBlocks.BEETROOT_CRATE.get()).mapColor(MapColor.TERRACOTTA_WHITE)));

    public static final RegistryObject<Block> POTTED_CINNAMON_SAPLING = BLOCKS.register("potted_cinnamon_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CINNAMON_SAPLING,
                    Block.Properties.copy(Blocks.POTTED_ACACIA_SAPLING).mapColor(MapColor.PLANT)));
    public static final RegistryObject<Block> POTTED_HAZELNUT_SAPLING = BLOCKS.register("potted_hazelnut_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HAZELNUT_SAPLING,
                    Block.Properties.copy(Blocks.POTTED_ACACIA_SAPLING).mapColor(MapColor.PLANT)));
    public static final RegistryObject<Block> POTTED_APPLE_SAPLING = BLOCKS.register("potted_apple_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, APPLE_SAPLING,
                    Block.Properties.copy(Blocks.POTTED_ACACIA_SAPLING).mapColor(MapColor.PLANT)));

}
