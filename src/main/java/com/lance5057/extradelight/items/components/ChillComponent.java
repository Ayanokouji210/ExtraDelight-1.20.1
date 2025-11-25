package com.lance5057.extradelight.items.components;


import com.lance5057.extradelight.ExtraDelight;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.lance5057.extradelight.ExtraDelightClientEvents.chillMap;

public record ChillComponent(int time) {


	public static final Codec<ChillComponent> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("chill").forGetter(ChillComponent::time))
			.apply(instance, ChillComponent::new));

	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeInt(time);
	}

	public static ChillComponent readFromBuffer(FriendlyByteBuf buffer) {
		return new ChillComponent(buffer.readInt());
	}

//	public void addToTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
//		tooltip.add(Component.translatable(ExtraDelight.MOD_ID + ".tooltip.chill", this.time)
//				.withStyle(ChatFormatting.AQUA));
//	}

    @SubscribeEvent
    public static void ItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(chillMap.containsKey(stack.getItemHolder())){
            Integer i = chillMap.get(stack.getItemHolder());
            event.getToolTip().add(Component.translatable(
                    ExtraDelight.MOD_ID + ".tooltip.chill", i).withStyle(ChatFormatting.AQUA));
        }
    }

//	@Override
//	public void modify(ItemTooltipEvent context) {
//		context.getToolTip().add(Component.translatable(
//				ExtraDelight.MOD_ID + ".tooltip.chill", this.time).withStyle(ChatFormatting.AQUA));
//	}
}