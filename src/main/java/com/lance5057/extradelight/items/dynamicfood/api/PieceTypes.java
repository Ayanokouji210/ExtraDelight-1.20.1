package com.lance5057.extradelight.items.dynamicfood.api;//package com.lance5057.extradelight.items.dynamicfood.api;
//
//import java.util.function.Supplier;
//
//import com.lance5057.extradelight.ExtraDelight;
//
//import net.minecraft.core.Registry;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.neoforged.neoforge.registries.DeferredRegister;
//import net.neoforged.neoforge.registries.RegistryBuilder;
//
//public class PieceTypes {
//	public static ResourceKey<Registry<PieceType>> PIECE_TYPE_KEY = ResourceKey
//			.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "pieces"));
//
//	public static final Registry<PieceType> PIECE_REGISTRY = new RegistryBuilder<>(PIECE_TYPE_KEY).sync(true)
//			.defaultKey(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "empty")).create();
//
//	public static final DeferredRegister<PieceType> PIECES = DeferredRegister.create(PIECE_REGISTRY,
//			ExtraDelight.MOD_ID);
//
//	public static Supplier<PieceType> TEST = PIECES.register("test", () -> new PieceType());
//	
//	public static void setup() {
//
//	}
//}
