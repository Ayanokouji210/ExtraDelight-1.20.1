package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetScreen;
import com.lance5057.extradelight.blocks.sink.SinkCabinetScreen;
import com.mojang.datafixers.types.Type;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;
//import net.neoforged.neoforge.network.handling.IPayloadContext;

//public record HideableSlotUpdatePacket(int containerId) implements CustomPacketPayload {
//	public static final Type<HideableSlotUpdatePacket> id = new CustomPacketPayload.Type<HideableSlotUpdatePacket>(
//			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "hideable_slot_packet"));
//
//	public static void handle(HideableSlotUpdatePacket message, IPayloadContext ctx) {
//		if (ctx.flow().isClientbound()) {
//			ctx.enqueueWork(new Runnable() {
//
//				@Override
//				public void run() {
//					if (Minecraft.getInstance().screen != null) {
//						if (Minecraft.getInstance().screen instanceof SinkCabinetScreen screen) {
//							screen.switchTabs();
//						}
//						if (Minecraft.getInstance().screen instanceof CounterCabinetScreen screen) {
//							screen.switchTabs();
//						}
//					}
//				}
//
//			});
//		}
//	}
//
//	public static StreamCodec<ByteBuf, HideableSlotUpdatePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
//			HideableSlotUpdatePacket::containerId, HideableSlotUpdatePacket::new);
//
//	@Override
//	public Type<? extends CustomPacketPayload> type() {
//		return id;
//	}
//}

public class HideableSlotUpdatePacket {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ExtraDelight.MOD_ID, "hideable_slot_packet"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private final int containerId;

	public HideableSlotUpdatePacket(int containerId) {
		this.containerId = containerId;
	}

	public static void register() {
		INSTANCE.registerMessage(0, HideableSlotUpdatePacket.class,
				HideableSlotUpdatePacket::encode,
				HideableSlotUpdatePacket::decode,
				HideableSlotUpdatePacket::handle);
	}

	public static HideableSlotUpdatePacket decode(FriendlyByteBuf buf) {
		return new HideableSlotUpdatePacket(buf.readInt());
	}

	public static void encode(HideableSlotUpdatePacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.containerId);
	}

	public static void handle(HideableSlotUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
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
}
