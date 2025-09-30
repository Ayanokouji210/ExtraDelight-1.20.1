package com.lance5057.extradelight.workstations.dryingrack;

import com.google.gson.JsonObject;
import com.lance5057.extradelight.util.StackUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import javax.annotation.Nullable;

public class DryingRackSerializer implements RecipeSerializer<DryingRackRecipe> {
	@Override
	public DryingRackRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
		String s = GsonHelper.getAsString(jsonObject,"group", "");
		Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject,"ingredient"));

		ItemStack result = StackUtil.ItemStackfromJson(GsonHelper.getAsJsonObject(jsonObject,"result"));

		float experience = GsonHelper.getAsFloat(jsonObject,"experience");
		int cookingTime = GsonHelper.getAsInt(jsonObject,"cookingtime");

		return new DryingRackRecipe(resourceLocation,s,ingredient,result,experience,cookingTime);
	}

	@Override
	public @Nullable DryingRackRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
		String s = friendlyByteBuf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
		ItemStack result = friendlyByteBuf.readItem();
		float experience = friendlyByteBuf.readFloat();
		int cookingTime = friendlyByteBuf.readVarInt();
		return new DryingRackRecipe(resourceLocation,s,ingredient,result,experience,cookingTime);
	}

	@Override
	public void toNetwork(FriendlyByteBuf friendlyByteBuf, DryingRackRecipe dryingRackRecipe) {
		friendlyByteBuf.writeUtf(dryingRackRecipe.getGroup());
		dryingRackRecipe.ingredient.toNetwork(friendlyByteBuf);
		friendlyByteBuf.writeItem(dryingRackRecipe.result);
		friendlyByteBuf.writeFloat(dryingRackRecipe.experience);
		friendlyByteBuf.writeVarInt(dryingRackRecipe.cookingTime);
	}
//	private static final MapCodec<DryingRackRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst
//			.group(Codec.STRING.optionalFieldOf("group", "").forGetter(DryingRackRecipe::getGroup),
//
//					Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
//
//					ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
//
//					Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(r -> r.experience),
//					Codec.INT.optionalFieldOf("cookingtime", 100).forGetter(r -> r.cookingTime))
//			.apply(inst, DryingRackRecipe::new));
//
//	public static DryingRackRecipe fromNetwork(FriendlyByteBuf pBuffer) {
//		String s = pBuffer.readUtf();
//		Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
//		ItemStack itemstack = ItemStack.STREAM_CODEC.decode(pBuffer);
//		float f = pBuffer.readFloat();
//		int i = pBuffer.readVarInt();
//		return new DryingRackRecipe(s, ingredient, itemstack, f, i);
//	}
//
//	public static void toNetwork(FriendlyByteBuf pBuffer, DryingRackRecipe pRecipe) {
//		pBuffer.writeUtf(pRecipe.getGroup());
//		Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.ingredient);
//		ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
//		pBuffer.writeFloat(pRecipe.experience);
//		pBuffer.writeVarInt(pRecipe.cookingTime);
//	}
//
//	public static final StreamCodec<FriendlyByteBuf, DryingRackRecipe> STREAM_CODEC = StreamCodec
//			.of(DryingRackSerializer::toNetwork, DryingRackSerializer::fromNetwork);
//
//	@Override
//	public MapCodec<DryingRackRecipe> codec() {
//		return CODEC;
//	}
//
//	@Override
//	public StreamCodec<FriendlyByteBuf, DryingRackRecipe> streamCodec() {
//		return STREAM_CODEC;
//	}

}
