package com.lance5057.extradelight.blocks.picnicbasket;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class PicnicBasketBlockEntity extends BlockEntity {
	public static final String TAG = "inv";
	private final PicnicBasketItemStackHandler items = createHandler();
	private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);
	public final static int NUM_SLOTS = 12;

	public PicnicBasketBlockEntity(BlockPos pos, BlockState blockState) {
		super(ExtraDelightBlockEntities.PICNIC_BASKET.get(), pos, blockState);
	}

	private PicnicBasketItemStackHandler createHandler() {
		return new PicnicBasketItemStackHandler(NUM_SLOTS);
	}


//	@Override
//	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
//		super.collectImplicitComponents(builder);
//		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items.getStacks()));
//	}
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
	public @NotNull CompoundTag getUpdateTag() {
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

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
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
	
	public String getDisplayName() {
		return "screen.picnic_basket.name";
	}
}