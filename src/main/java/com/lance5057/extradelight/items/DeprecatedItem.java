package com.lance5057.extradelight.items;

import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class DeprecatedItem extends Item {
	public DeprecatedItem() {
		super(new Properties());
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		return InteractionResultHolder.success(changeToStack(player.getItemInHand(usedHand)));
	}


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    MutableComponent textEmpty = Component.translatable(ExtraDelight.MOD_ID + ".tooltip.deprecated");
		pTooltipComponents.add(textEmpty.withStyle(ChatFormatting.RED));
	}

	public abstract ItemStack changeToStack(ItemStack itemStack);

}
