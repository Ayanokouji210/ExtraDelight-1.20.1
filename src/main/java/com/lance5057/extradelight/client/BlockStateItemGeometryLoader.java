package com.lance5057.extradelight.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.common.data.ExistingFileHelper;
//import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
//import net.neoforged.neoforge.client.model.generators.ModelBuilder;
//import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateItemGeometryLoader implements IGeometryLoader<BlockStateItemUnbakedGeometry> {
	public static ResourceLocation ID = ExtraDelight.modLoc("blockstateitem");

	@Override
	public BlockStateItemUnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
			throws JsonParseException {
		return new BlockStateItemUnbakedGeometry();
	}

	public static <T extends ModelBuilder<T>> CustomLoaderBuilder<T> builder(T parent,
																			 ExistingFileHelper existingFileHelper) {
		return new CustomLoaderBuilder<T>(ID, parent, existingFileHelper) {
		};
	}
}