package com.lance5057.extradelight.data;

import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EDBannerTags extends TagsProvider<BannerPattern> {

	public EDBannerTags(PackOutput output, CompletableFuture<Provider> provider,
                        @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.BANNER_PATTERN, provider, ExtraDelight.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
//		this.tag(ExtraDelightBanners.CITRUS_RIND_BANNER_PATTERN).add(ExtraDelightBanners.CITRUS_RIND);
	}
}
