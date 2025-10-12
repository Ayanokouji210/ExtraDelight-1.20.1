package com.lance5057.extradelight.blocks.chocolatebox;

import com.google.common.collect.Sets;
import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
//import net.minecraft.core.component.DataComponentMap;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
//import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Set;

public class ChocolateBoxBlockEntity extends BlockEntity {
	public static final String TAG = "inv";
	private final ChocolateBoxItemStackHandler items = createHandler();
	private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);
	public final static int NUM_SLOTS = 8;

    public static final Set<Item> chocolateBoxItems = Sets.newHashSet(ExtraDelightItems.WHITE_CHOCOLATE_BOX.get(), ExtraDelightItems.ORANGE_CHOCOLATE_BOX.get(),
            ExtraDelightItems.MAGENTA_CHOCOLATE_BOX.get(), ExtraDelightItems.LIGHT_BLUE_CHOCOLATE_BOX.get(),
            ExtraDelightItems.YELLOW_CHOCOLATE_BOX.get(), ExtraDelightItems.LIME_CHOCOLATE_BOX.get(),
            ExtraDelightItems.PINK_CHOCOLATE_BOX.get(), ExtraDelightItems.GRAY_CHOCOLATE_BOX.get(),
            ExtraDelightItems.LIGHT_GRAY_CHOCOLATE_BOX.get(), ExtraDelightItems.CYAN_CHOCOLATE_BOX.get(),
            ExtraDelightItems.BLUE_CHOCOLATE_BOX.get(), ExtraDelightItems.BROWN_CHOCOLATE_BOX.get(),
            ExtraDelightItems.GREEN_CHOCOLATE_BOX.get(), ExtraDelightItems.RED_CHOCOLATE_BOX.get(),
            ExtraDelightItems.BLACK_CHOCOLATE_BOX.get(), ExtraDelightItems.PURPLE_CHOCOLATE_BOX.get()
    );

	public ChocolateBoxBlockEntity(BlockPos pos, BlockState blockState) {
		super(ExtraDelightBlockEntities.CHOCOLATE_BOX.get(), pos, blockState);
	}

	private ChocolateBoxItemStackHandler createHandler() {
		return new ChocolateBoxItemStackHandler(NUM_SLOTS) {

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				ChocolateBoxBlockEntity.this.requestModelDataUpdate();
				ChocolateBoxBlockEntity.this.getLevel().sendBlockUpdated(ChocolateBoxBlockEntity.this.getBlockPos(),
						ChocolateBoxBlockEntity.this.getBlockState(), ChocolateBoxBlockEntity.this.getBlockState(),
						Block.UPDATE_CLIENTS);
				ChocolateBoxBlockEntity.this.setChanged();
			}
		};
	}



//	@Override
//	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
//		super.collectImplicitComponents(builder);
//		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items.getStacks()));
//	}
//
//
//	@Override
//	protected void applyImplicitComponents(DataComponentInput input) {
//		super.applyImplicitComponents(input);
//		input.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items.getStacks());
//	}

	public IItemHandler getItems() {
		return this.itemHandler.get();
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();

		writeNBT(nbt);

		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		readNBT(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		// InteractionHandle your Data
		if (tag != null)
			readNBT(tag);
	}

	void readNBT(CompoundTag nbt) {
		if (nbt.contains(TAG)) {
			items.deserializeNBT(nbt.getCompound(TAG));
		}
	}

	CompoundTag writeNBT(CompoundTag tag) {
		tag.put(TAG, items.serializeNBT());
		return tag;
	}

	@Override
	public void load(@Nonnull CompoundTag nbt) {
		super.load(nbt);
		readNBT(nbt);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt) {
		super.saveAdditional(nbt);
		writeNBT(nbt);
	}
}
