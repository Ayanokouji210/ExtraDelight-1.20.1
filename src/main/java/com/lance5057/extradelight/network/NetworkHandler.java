package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.gui.StyleableMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
//import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
//import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ExtraDelight.MOD_ID,"main"))
			.networkProtocolVersion(()->PROTOCOL_VERSION)
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.simpleChannel();
//			new ResourceLocation(ExtraDelight.MOD_ID, "main"),
//			() -> PROTOCOL_VERSION,
//			PROTOCOL_VERSION::equals,
//			PROTOCOL_VERSION::equals


	public static void setupPackets() {
		int id = 0;

		// 注册 StyleableMenuSyncPacket
		CHANNEL.messageBuilder(StyleableMenuSyncPacket.class,id)
				.encoder(StyleableMenuSyncPacket::encode)
				.decoder(StyleableMenuSyncPacket::decode)
				.consumerMainThread(StyleableMenuSyncPacket::handle)
				.add();

//		CHANNEL.(id++, StyleableMenuSyncPacket.class,
//				StyleableMenuSyncPacket::encode,
//				StyleableMenuSyncPacket::decode,
//				StyleableMenuSyncPacket::handle);
//	public static void setupPackets(RegisterPayloadHandlersEvent event) {
//		PayloadRegistrar registrar = event.registrar(ExtraDelight.MOD_ID).versioned("1.0.0").optional();
//
//		registrar.playToClient(StyleableMenuSyncPacket.id, StyleableMenuSyncPacket.STREAM_CODEC, StyleableMenuSyncPacket::handle);
//	}
	}


	public static void register(){
		int id = 0;
		CHANNEL.messageBuilder(StyleableMenuSyncPacket.class, id++)
				.encoder(StyleableMenuSyncPacket::encode)
				.decoder(StyleableMenuSyncPacket::decode)
				.consumerMainThread(StyleableMenuSyncPacket::handle)
				.add();
	}
}
