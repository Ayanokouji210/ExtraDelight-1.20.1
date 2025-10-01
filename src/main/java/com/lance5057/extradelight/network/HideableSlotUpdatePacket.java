package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetScreen;
import com.lance5057.extradelight.blocks.sink.SinkCabinetScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

public class HideableSlotUpdatePacket {
	private final int containerId;

	public HideableSlotUpdatePacket(int containerId) {
		this.containerId = containerId;
	}

	public static void encode(HideableSlotUpdatePacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.containerId);
	}

	public static HideableSlotUpdatePacket decode(FriendlyByteBuf buf) {
		return new HideableSlotUpdatePacket(buf.readInt());
	}

	public static void handle(HideableSlotUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (Minecraft.getInstance().screen != null) {
				if (Minecraft.getInstance().screen instanceof SinkCabinetScreen screen) {
					screen.switchTabs();
				}
				if (Minecraft.getInstance().screen instanceof CounterCabinetScreen screen) {
					screen.switchTabs();
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public int getContainerId() {
		return containerId;
	}

}