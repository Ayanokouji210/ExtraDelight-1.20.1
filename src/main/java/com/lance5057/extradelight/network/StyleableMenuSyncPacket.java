package com.lance5057.extradelight.network;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.gui.StyleableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StyleableMenuSyncPacket implements ICustomPacket {
    private final ResourceLocation id=ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,"style_packet");
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

    @Override
    public @Nullable FriendlyByteBuf getInternalData() {
        return null;
    }

    @Override
    public ResourceLocation getName() {
        return id;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}