package com.lance5057.extradelight.blocks;

import com.lance5057.extradelight.blocks.jardisplay.JarDisplayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public interface IDisplayInteractable {
	public InteractionResult itemInteract(ItemStack heldItem, ItemStack interactItem, BlockState state, Level level,
										  BlockPos pos, Player player, InteractionHand hand);

	public void extractItem(Level level, BlockPos pos, Player player, JarDisplayBlockEntity jdbe,
							ItemStackHandler handler, ItemStack s, int index);

	public void convertToSingular(BlockPos pos, Level level, JarDisplayBlockEntity jdbe, ItemStack stack);
}
