package com.lance5057.extradelight.blocks.jardisplay;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.blocks.IDisplayInteractable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.items.IItemHandler;
//import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class JarDisplayBlockEntity extends BlockEntity {
	public static final String TAG = "inv";
	private final ItemStackHandler items = createHandler();
	private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);
	public final static int NUM_SLOTS = 4;

	public JarDisplayBlockEntity(BlockPos pos, BlockState blockState) {
		super(ExtraDelightBlockEntities.JAR_DISPLAY.get(), pos, blockState);
		// TODO Auto-generated constructor stub
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(NUM_SLOTS) {

			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (stack.getItem() instanceof BlockItem bi) {
					if (bi.getBlock() instanceof IDisplayInteractable)
						return true;
					else if (bi instanceof IDisplayInteractable)
						return true;
				}
				return false;
			}

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				JarDisplayBlockEntity.this.requestModelDataUpdate();
				JarDisplayBlockEntity.this.getLevel().sendBlockUpdated(JarDisplayBlockEntity.this.getBlockPos(),
						JarDisplayBlockEntity.this.getBlockState(), JarDisplayBlockEntity.this.getBlockState(),
						Block.UPDATE_CLIENTS);
				JarDisplayBlockEntity.this.setChanged();
			}
		};
	}

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
