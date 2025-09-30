package com.lance5057.extradelight.data;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightBlocks;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.modules.Fermentation;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiConsumer;

public class MiscLootTables implements LootTableSubProvider {

	private static <T> ResourceKey<Registry<T>> createRegistryKey(String pName) {
		return ResourceKey.createRegistryKey(new ResourceLocation(pName));
	}

	public static final ResourceKey<Registry<LootTable>> LOOT_TABLE = createRegistryKey("loot_table");

	public static final ResourceKey<LootTable> SHUCKED_CORN = ResourceKey.create(LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "misc/shucked_corn"));
	public static final ResourceKey<LootTable> CORN_TOP = ResourceKey.create(LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "misc/corn_top"));
	public static final ResourceKey<LootTable> CINNAMON_LOG = ResourceKey.create(LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "misc/cinnamon_log"));

	public static final ResourceKey<LootTable> EVAPORATOR_LAVA_TEST = ResourceKey.create(LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "evaporator/lava_test"));

	public static final ResourceKey<LootTable> EVAPORATOR_WATER = ResourceKey.create(LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "evaporator/water"));
	protected MiscLootTables() {
		super();
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> t) {
		//ResourceLocation SHUCKED_CORN = new ResourceLocation(ExtraDelight.MOD_ID,"misc/shucked_corn");
		t.accept(SHUCKED_CORN.location(),new LootTable.Builder().withPool(createPoolWithItem(ExtraDelightItems.CORN_HUSK.get(), 2, 3))
						.withPool(createPoolWithItem(ExtraDelightItems.CORN_SILK.get(), 1, 2))
						.withPool(createPoolWithItem(ExtraDelightItems.CORN_ON_COB.get(), 1)));

		//ResourceLocation CINNAMON_LOG = new ResourceLocation(ExtraDelight.MOD_ID,"misc/cinnamon_log");
		t.accept(CINNAMON_LOG.location(),
				LootTable.lootTable().withPool(createPoolWithItem(ExtraDelightItems.CINNAMON_BARK.get(), 1, 4))
						.withPool(createPoolWithItem(ModItems.TREE_BARK.get(), 0, 2)));

		//ResourceLocation EVAPORATOR_LAVA_TEST = new ResourceLocation(ExtraDelight.MOD_ID,"evaporator/lava_test");
		//HolderLookup.RegistryLookup<Biome> registrylookup = this.registries.lookupOrThrow(Registries.BIOME);
		t.accept(EVAPORATOR_LAVA_TEST.location(), LootTable.lootTable().withPool(createPoolWithItem(Items.COBBLESTONE, 1, 5))
				.withPool(createPoolWithItem(Items.STONE, 1, 4)
//										.when(LocationCheck.checkLocation(LocationPredicate.Builder.location()
//												.setBiomes(HolderSet.direct(registrylookup.getOrThrow(Biomes.RIVER),
//														registrylookup.getOrThrow(Biomes.OCEAN)))))
				));


		//ResourceLocation EVAPORATOR_WATER = new ResourceLocation(ExtraDelight.MOD_ID,"evaporator/water");
		t.accept(EVAPORATOR_WATER.location(),
				LootTable.lootTable()
						.withPool(createPoolWithItem(Fermentation.SALT.get(), 1)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.withPool(LootPool.lootPool().name("extras").setRolls(UniformGenerator.between(0, 3))
								.add(LootItem.lootTableItem(Items.COD).setWeight(1))
								.add(LootItem.lootTableItem(Items.BONE_MEAL).setWeight(5))
								.add(LootItem.lootTableItem(Items.STICK).setWeight(10))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5))
								.add(LootItem.lootTableItem(Items.LEATHER_BOOTS)
										.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.05F)))
										.setWeight(1)))

						.withPool(LootPool.lootPool().name("ocean_river").setRolls(UniformGenerator.between(0, 3))
								.add(LootItem.lootTableItem(Items.COD).setWeight(1))
								.add(LootItem.lootTableItem(Items.SALMON).setWeight(1))
								.add(LootItem.lootTableItem(Fermentation.SALT.get()).setWeight(1))
								.add(LootItem.lootTableItem(Items.SAND).setWeight(5))
								.add(LootItem.lootTableItem(Items.GRAVEL).setWeight(5))
								.setRolls(UniformGenerator.between(1, 3))
								.when(LocationCheck.checkLocation(LocationPredicate.Builder.location()
														.setBiome(Biomes.RIVER).setBiome(Biomes.OCEAN))))
//										.setBiomes(HolderSet.direct(registrylookup.getOrThrow(Biomes.RIVER),
//												registrylookup.getOrThrow(Biomes.OCEAN))))))
						.withPool(LootPool.lootPool().name("swamp").setRolls(UniformGenerator.between(0, 3))
								.add(LootItem.lootTableItem(Items.CLAY_BALL))
								.add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(1))
								.add(LootItem.lootTableItem(Items.SAND).setWeight(1))
								.setRolls(UniformGenerator.between(1, 3))
								.when(LocationCheck.checkLocation(LocationPredicate.Builder.location()
												.setBiome(Biomes.SWAMP).setBiome(Biomes.MANGROVE_SWAMP)))));
//										.setBiomes(HolderSet.direct(registrylookup.getOrThrow(Biomes.SWAMP),
//												registrylookup.getOrThrow(Biomes.MANGROVE_SWAMP)))))));
	}

	@Nonnull
	public static LootPool.Builder createPoolWithItem(Item item, int count) {
		return LootPool.lootPool().add(LootItem.lootTableItem(item))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
	}

	@Nonnull
	public static LootPool.Builder createPoolWithItem(Item item, int min, int max) {
		return LootPool.lootPool().add(LootItem.lootTableItem(item))
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
	}

	@Nonnull
	public static LootPool.Builder createPoolWithItem(Item item) {
		return LootPool.lootPool().add(LootItem.lootTableItem(item));
	}

	protected Builder createCropDrops(Block pCropBlock, Item pGrownCropItem, Item pSeedsItem,
			LootItemCondition.Builder pDropGrownCropCondition) {
		//HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(pGrownCropItem).when(pDropGrownCropCondition)
								.otherwise(LootItem.lootTableItem(pSeedsItem))))
				.withPool(LootPool.lootPool().when(pDropGrownCropCondition)
						.add(LootItem.lootTableItem(pSeedsItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(
								Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))));
	}

}
