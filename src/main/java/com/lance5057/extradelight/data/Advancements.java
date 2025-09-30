package com.lance5057.extradelight.data;

import com.lance5057.extradelight.data.advancement.EDAdvancementGenerator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
//import net.neoforged.neoforge.common.data.AdvancementProvider;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Advancements extends AdvancementProvider {
	public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, List.of(new EDAdvancementGenerator().toSubProvider(existingFileHelper)));
	}
}