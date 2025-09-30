package com.lance5057.extradelight.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class BlockStateItemOverrides extends ItemOverrides {
//	private final Cache<Integer, MetalTileBakedGeometry> cache;

	public BlockStateItemOverrides() {
//		this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.of(5, ChronoUnit.MINUTES)).build();
	}

//	@Override
//	@Nullable
//	@ParametersAreNonnullByDefault
//	public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel,
//			@Nullable LivingEntity pEntity, int pSeed) {
//		if (pStack.getItem() instanceof BlockItem bi) {
//			if (pStack.has(DataComponents.BLOCK_STATE)) {
//				BlockItemStateProperties bisp = pStack.get(DataComponents.BLOCK_STATE);
//
//				BlockState bs = bisp.apply(bi.getBlock().defaultBlockState());
//
//				BakedModel bm = Minecraft.getInstance().getBlockRenderer().getBlockModel(bs);
//
//				return bm;
//			} else
//				return Minecraft.getInstance().getBlockRenderer().getBlockModel(bi.getBlock().defaultBlockState());
//		}
//		return pModel;
//	}

	@Override
	@Nullable
	@ParametersAreNonnullByDefault
	public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel,
							  @Nullable LivingEntity pEntity, int pSeed) {
		if (pStack.getItem() instanceof BlockItem bi) {
			// 在 1.20.1 中，我们使用 NBT 数据而不是数据组件
			CompoundTag tag = pStack.getTag();
			if (tag != null && tag.contains("BlockStateTag")) {
				CompoundTag blockStateTag = tag.getCompound("BlockStateTag");
				BlockState defaultState = bi.getBlock().defaultBlockState();
				BlockState customState = getStateFromNBT(defaultState, blockStateTag);

				return Minecraft.getInstance().getBlockRenderer().getBlockModel(customState);
			} else {
				return Minecraft.getInstance().getBlockRenderer().getBlockModel(bi.getBlock().defaultBlockState());
			}
		}
		return pModel;
	}

	// 从 NBT 数据中获取方块状态
	private BlockState getStateFromNBT(BlockState defaultState, CompoundTag nbt) {
		BlockState state = defaultState;

		for (String key : nbt.getAllKeys()) {
			Property<?> property = defaultState.getBlock().getStateDefinition().getProperty(key);
			if (property != null) {
				state = setValue(state, property, nbt.getString(key));
			}
		}

		return state;
	}

	// 设置方块状态属性值
	private <T extends Comparable<T>> BlockState setValue(BlockState state, Property<T> property, String value) {
		Optional<T> optional = property.getValue(value);
		if (optional.isPresent()) {
			return state.setValue(property, optional.get());
		}
		return state;
	}

}