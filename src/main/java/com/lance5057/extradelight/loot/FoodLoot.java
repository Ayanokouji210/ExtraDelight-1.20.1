package com.lance5057.extradelight.loot;

import com.mojang.serialization.Codec;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
//import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
//import net.neoforged.neoforge.common.loot.LootModifier;

public class FoodLoot extends LootModifier {
//	public static final Supplier<MapCodec<FoodLoot>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(
//			inst -> codecStart(inst).and(ResourceKey ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTableID))
//					.apply(inst, FoodLoot::new)));

	public static final Codec<FoodLoot> CODEC = RecordCodecBuilder.create(instance ->
			codecStart(instance).and(
					ResourceLocation.CODEC.fieldOf("loot_table").forGetter(FoodLoot::getLootTableId)
			).apply(instance, FoodLoot::new));

	private final ResourceLocation lootTableId;

	public FoodLoot(final LootItemCondition[] conditions, final ResourceLocation lootTableId) {
		super(conditions);
		this.lootTableId = lootTableId;
	}

	public ResourceLocation getLootTableId() {
		return this.lootTableId;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		LootTable extraTable = context.getResolver().getLootTable(this.lootTableId);
		if (extraTable != LootTable.EMPTY) {
			extraTable.getRandomItemsRaw(context, generatedLoot::add);
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
