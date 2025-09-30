package com.lance5057.extradelight.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import javax.annotation.Nullable;
//import net.neoforged.neoforge.common.ItemAbilities;
//import net.neoforged.neoforge.common.ItemAbility;


public class StrippableLog extends RotatedPillarBlock {

	final RotatedPillarBlock stripped;
	final ResourceKey<LootTable> drop;

	public StrippableLog(RotatedPillarBlock stripped, ResourceKey<LootTable> drop, Properties pProperties) {

		super(pProperties);
		this.stripped = stripped;
		this.drop = drop;
	}

	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
		if(toolAction.equals(ToolActions.AXE_STRIP)){
			if (stripped != null) {
//                    dropLoot(context.getLevel(), context.getPlayer(), context.getClickedPos());

                return stripped.defaultBlockState().setValue(RotatedPillarBlock.AXIS,
						state.getValue(RotatedPillarBlock.AXIS));
			}
		}
		return state;
	}

//	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction,
//										   boolean simulate) {
//
//		if (ItemAbilities.AXE_STRIP == toolAction) {
//			if (stripped != null) {
//				dropLoot(context.getLevel(), context.getPlayer(), context.getClickedPos());
//
//				return stripped.defaultBlockState().setValue(RotatedPillarBlock.AXIS,
//						state.getValue(RotatedPillarBlock.AXIS));
//			}
//		}
//		return state;
//	}

	private void dropLoot(Level level, Player player, BlockPos pos) {
		if (level != null && !level.isClientSide()) {
			// 直接获取主手物品的时运附魔等级
			int fortuneLevel = player.getMainHandItem().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

			// 构建 LootParams
			final LootParams pContext = new LootParams.Builder((ServerLevel) level)
					.withParameter(LootContextParams.TOOL, player.getMainHandItem())
					.withParameter(LootContextParams.THIS_ENTITY, player)
					.withLuck(player.getLuck() + fortuneLevel) // 直接使用附魔等级
					.create(LootContextParamSets.BLOCK); // 使用 BLOCK 而不是 EMPTY

			// 获取并掉落战利品
			level.getServer().getLootData().getLootTable(drop.location()).getRandomItems(pContext).forEach(itemStack -> {
				level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f,
						itemStack, 0, 0, 0));
			});
		}
	}
}
