package com.lance5057.extradelight.items.dynamicfood.api;//package com.lance5057.extradelight.items.dynamicfood.api;
//
//import javax.annotation.Nullable;
//
//import net.minecraft.Util;
//import net.minecraft.network.chat.Component;
//
//public class PieceType {
//	@Nullable
//	private String descriptionId;
//
//	public Component getDescription() {
//		return Component.translatable(this.getDescriptionId());
//	}
//
//	protected String getOrCreateDescriptionId() {
//		if (this.descriptionId == null) {
//			this.descriptionId = Util.makeDescriptionId("piece_type", PieceTypes.PIECE_REGISTRY.getKey(this));
//		}
//
//		return this.descriptionId;
//	}
//
//	public String getDescriptionId() {
//		return this.getOrCreateDescriptionId();
//	}
//}
