package com.lance5057.extradelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AllLootTables extends LootTableProvider {
	public AllLootTables(PackOutput pOutput) {
		super(pOutput, Collections.emptySet(),
				List.of(new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK),
						new SubProviderEntry(MiscLootTables::new, LootContextParamSets.ARCHAEOLOGY),
						new SubProviderEntry(StructureLootTables::new, LootContextParamSets.EMPTY)

				));
	}
}
