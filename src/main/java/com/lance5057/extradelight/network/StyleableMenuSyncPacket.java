package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.gui.StyleableScreen;
import com.mojang.datafixers.types.Type;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkEvent;
import javax.annotation.Nullable;

import java.util.function.Supplier;
//import net.neoforged.neoforge.network.handling.IPayloadContext;


public class StyleableMenuSyncPacket implements ICustomPacket {
	private final int containerId;
	private final BlockPos pos;

	public StyleableMenuSyncPacket(int containerId, BlockPos pos) {
		this.containerId = containerId;
		this.pos = pos;
	}

	public static void encode(StyleableMenuSyncPacket message, FriendlyByteBuf buf) {
		buf.writeInt(message.containerId);
		buf.writeBlockPos(message.pos);
	}

	public static StyleableMenuSyncPacket decode(FriendlyByteBuf buf) {
		return new StyleableMenuSyncPacket(buf.readInt(), buf.readBlockPos());
	}

	public static void handle(StyleableMenuSyncPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (Minecraft.getInstance().screen != null
					&& Minecraft.getInstance().screen instanceof StyleableScreen screen) {
				screen.setPos(message.pos);
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public int getContainerId() {
		return containerId;
	}

	public BlockPos getPos() {
		return pos;
	}

	@Override
	public @Nullable FriendlyByteBuf getInternalData() {
		return null;
	}

	@Override
	public ResourceLocation getName() {
		return null;
	}

	@Override
	public int getIndex() {
		return 0;
	}
}


//public record StyleableMenuSyncPacket(int containerId, BlockPos pos) implements ICustomPacket {
//	public static final Type<StyleableMenuSyncPacket> id = new CustomPacketPayload.Type<StyleableMenuSyncPacket>(
//			ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "style_packet"));
//
//	public StyleableMenuSyncPacket(FriendlyByteBuf buf) {
//		this(buf.readInt(), buf.readBlockPos());
//	}
//
////	@Override
////	public void write(FriendlyByteBuf buf) {
////		buf.writeInt(containerId);
////		buf.writeBlockPos(pos);
////	}
////
//
//
//
//	public static void handle(StyleableMenuSyncPacket message, IPayloadContext ctx) {
//		if (ctx.flow().isClientbound()) {
//			ctx.enqueueWork(new Runnable() {
//
//				@Override
//				public void run() {
//					if (Minecraft.getInstance().screen != null
//							&& Minecraft.getInstance().screen instanceof StyleableScreen screen) {
//						screen.setPos(message.pos());
//					}
//				}
//
//			});
//		}
//	}
//
//	public static StreamCodec<ByteBuf, StyleableMenuSyncPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
//			StyleableMenuSyncPacket::containerId, BlockPos.STREAM_CODEC, StyleableMenuSyncPacket::pos,
//			StyleableMenuSyncPacket::new);
//
//	@Override
//	public Type<? extends CustomPacketPayload> type() {
//		return id;
//	}
//
//	@Override
//	public @Nullable FriendlyByteBuf getInternalData() {
//		return null;
//	}
//
//	@Override
//	public ResourceLocation getName() {
//		return null;
//	}
//
//	@Override
//	public int getIndex() {
//		return 0;
//	}
//}
