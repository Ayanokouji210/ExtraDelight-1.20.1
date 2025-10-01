package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
	private static final String PROTOCOL_VERSION = "1.0.0";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ExtraDelight.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);


	public static final int HIDEABLE_SLOT_UPDATE_ID = 0;
	public static final int STYLEABLE_MENU_SYNC_ID = 1;

	public static void register() {
		CHANNEL.registerMessage(
				HIDEABLE_SLOT_UPDATE_ID,
				HideableSlotUpdatePacket.class,
				HideableSlotUpdatePacket::encode,
				HideableSlotUpdatePacket::decode,
				HideableSlotUpdatePacket::handle
		);

		CHANNEL.registerMessage(
				STYLEABLE_MENU_SYNC_ID,
				StyleableMenuSyncPacket.class,
				StyleableMenuSyncPacket::encode,
				StyleableMenuSyncPacket::decode,
				StyleableMenuSyncPacket::handle
		);

		ExtraDelight.logger.info("ExtraDelight network system registered with {} packets", 2);
	}
}