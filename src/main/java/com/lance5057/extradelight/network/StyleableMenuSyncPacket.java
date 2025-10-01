package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.gui.StyleableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StyleableMenuSyncPacket {
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

	public int getContainerId() { return containerId; }

	public BlockPos getPos() {
		return pos;
	}
}