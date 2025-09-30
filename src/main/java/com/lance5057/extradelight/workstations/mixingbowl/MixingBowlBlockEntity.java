package com.lance5057.extradelight.workstations.mixingbowl;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.util.BottleFluidRegistry;
import com.lance5057.extradelight.workstations.FancyTank;
import com.lance5057.extradelight.workstations.IFancyTankHandler;
import com.lance5057.extradelight.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.lance5057.extradelight.workstations.mixingbowl.recipes.MixingBowlRecipeWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
//import net.neoforged.neoforge.items.IItemHandlerModifiable;
//import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import javax.annotation.Nonnull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class MixingBowlBlockEntity extends BlockEntity implements IFancyTankHandler<MixingBowlBlockEntity> {
	public static final String INV_TAG = "inv";

//	protected MixingBowlMenu menu;
	private final ItemStackHandler items = createHandler();
	private final Lazy<IItemHandlerModifiable> itemHandler = Lazy.of(() -> items);
	public static final int CRAFT_SLOTS = 8;
	public static final int CONTAINER_SLOT = 9;
	public static final int LIQUID_IN_SLOT = 10;
	public static final int LIQUID_OUT_SLOT = 11;
	public static final int GHOST_SLOT = 12;

	@Override
	public int getFluidInSlot() {
		return LIQUID_IN_SLOT;
	}

	@Override
	public int getFluidOutSlot() {
		return LIQUID_OUT_SLOT;
	}

	public static final String FLUID_TAG = "tank";

	private final FancyTank fluids = createFluidHandler();

	private int stirs = 0;
	public boolean complete = false;
	public ItemStack containerItem = ItemStack.EMPTY;

	MixingBowlRecipe curRecipe;

	public MixingBowlBlockEntity(BlockPos pPos, BlockState pState) {
		super(ExtraDelightBlockEntities.MIXING_BOWL.get(), pPos, pState);
	}

//	public void setMenu(MixingBowlMenu menu) {
//		this.menu = menu;
//	}

	private FancyTank createFluidHandler() {
		FancyTank tank = new FancyTank(FluidType.BUCKET_VOLUME * 6) { // 6000
			@Override
			protected void onContentsChanged() {
				super.onContentsChanged();
				var level = MixingBowlBlockEntity.this.getLevel();
				if (level == null)
					return;
				MixingBowlBlockEntity.this.requestModelDataUpdate();
				level.sendBlockUpdated(MixingBowlBlockEntity.this.getBlockPos(),
						MixingBowlBlockEntity.this.getBlockState(), MixingBowlBlockEntity.this.getBlockState(),
						Block.UPDATE_ALL);
				MixingBowlBlockEntity.this.setChanged();
			}
		};

		return tank;
	}

	@Override
	public FancyTank getFluidTank() {
		return fluids;
	}

	@Override
	public IItemHandlerModifiable getItemHandler() {
		return itemHandler.get();
	}

//	private boolean canFitInSlot(int slot, ItemStack pSlot, boolean fitAll) {
//		if (slot == LIQUID_OUT_SLOT) {
//			if (fitAll)
//				return pSlot.getCount() <= calcFluidOutSlotSize(slot, pSlot);
//			else
//				return calcFluidOutSlotSize(slot, pSlot) != 0;
//		} else if (slot == LIQUID_IN_SLOT) {
//			return calcFluidInSlotSize(slot) != 0;
//		}
//		return true;
//	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(GHOST_SLOT + 1) {

			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				switch (slot) {
				case LIQUID_IN_SLOT:
					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() || stack.is(Items.BUCKET)
							|| !BottleFluidRegistry.getFluidFromBottle(stack).isEmpty();
				case LIQUID_OUT_SLOT:

					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() || stack.is(Items.BUCKET)
							|| ItemStack.isSameItem(stack,
									BottleFluidRegistry.getBottleFromFluid(MixingBowlBlockEntity.this.getFluidTank()
											.drain(250, IFluidHandler.FluidAction.SIMULATE)).getCraftingRemainingItem())
							|| stack.is(Items.GLASS_BOTTLE);
				case GHOST_SLOT:
					return false;
				default:
					return true;
				}
			}

			@Override
			protected void onContentsChanged(int slot) {
				if (slot != GHOST_SLOT) {
					zeroProgress();
					updateInventory();
				}

				if (slot == LIQUID_IN_SLOT)
					MixingBowlBlockEntity.this.fillInternal(MixingBowlBlockEntity.this);
				if (slot == LIQUID_OUT_SLOT)
					MixingBowlBlockEntity.this.drainInternal(MixingBowlBlockEntity.this);
			}

		};
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return meal.hasCraftingRemainingItem();
	}

	public int getLastFilledSlot(IItemHandlerModifiable inventory) {
		if (inventory.getStackInSlot(9) != ItemStack.EMPTY)
			return 9;
		for (int i = 0; i <= 9; i++) {
			if (inventory.getStackInSlot(i) == ItemStack.EMPTY)
				return i - 1;
		}
		return -1;
	}

	public void extractItem(Player playerEntity, IItemHandlerModifiable inventory) {
		int i = getLastFilledSlot(inventory);
		if (i != -1) {
			ItemStack itemStack = inventory.extractItem(i, 1, false);
			playerEntity.addItem(itemStack);
			updateInventory();
			return;
		}

		updateInventory();
	}

	public int getNextEmptySlot() {
		for (int i = 0; i < items.getSlots(); ++i) {
			ItemStack slotStack = items.getStackInSlot(i);
			if (slotStack.isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public void zeroProgress() {
		stirs = 0;
	}

	public void updateInventory() {
		this.setupRecipe();
		requestModelDataUpdate();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		this.setChanged();
	}

	private void setupRecipe() {
		Optional<MixingBowlRecipe> recipe = this.matchRecipe();
		if (recipe.isPresent()) {
			this.curRecipe = recipe.get();
			this.items.setStackInSlot(GHOST_SLOT, curRecipe.getResultItem(this.level.registryAccess()).copy());
		} else {
			this.curRecipe = null;
			this.items.setStackInSlot(GHOST_SLOT, ItemStack.EMPTY.copy());
		}
	}

	public int getStirs() {
		return stirs;
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
//		CompoundTag tag = new CompoundTag();
//
//		writeNBT(tag);

		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		// InteractionHandle your Data
		readNBT(tag);
	}

	void readNBT(CompoundTag nbt) {
		if (nbt.contains(INV_TAG)) {
			items.deserializeNBT(nbt.getCompound(INV_TAG));
		}
		this.getFluidTank().readFromNBT(nbt);
		this.stirs = nbt.getInt("stirs");
		if (nbt.contains("usedItem"))
			this.containerItem = ItemStack.of(nbt.getCompound("usedItem"));
			//ItemStack.of(nbt.getCompound("usedItem")).ifPresent(i -> containerItem = i);
		this.complete = nbt.getBoolean("complete");
	}

	CompoundTag writeNBT(CompoundTag tag) {

		tag.put(INV_TAG, items.serializeNBT());
		this.getFluidTank().writeToNBT(tag);
		tag.putInt("stirs", this.stirs);

		if (!containerItem.isEmpty())
			tag.put("usedItem", containerItem.save(new CompoundTag()));
		tag.putBoolean("complete", this.complete);

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

	private ItemStack[] getItems() {
		int s = getLastFilledSlot(items);
		if (s != -1) {
			ItemStack[] stacks = new ItemStack[s + 1];
			for (int i = 0; i < s + 1; i++) {
				stacks[i] = items.getStackInSlot(i);
			}
			return stacks;
		}
		return new ItemStack[0];
	}

	private void clearItems(int k) {
		for (int i = 0; i < 9; i++) {
			if (!items.getStackInSlot(i).isEmpty())
				items.extractItem(i, 1, false);
		}
		if (!items.getStackInSlot(CONTAINER_SLOT).isEmpty())
			items.extractItem(CONTAINER_SLOT, k, false);
	}

	protected Optional<MixingBowlRecipe> matchRecipe() {
		if (level != null) {

			Optional<MixingBowlRecipe> recipe = level.getRecipeManager().getRecipeFor(
					ExtraDelightRecipes.MIXING_BOWL.get(), new MixingBowlRecipeWrapper(this.items, this.fluids) {
						public int size() {
							return 9;
						}

						@Override
						public boolean isEmpty() {
							boolean res = true;
							if (this.getTank().getTotalAmount() != 0)
								return false;
							for (int i = 0; i < this.inv.getSlots(); i++)
								if (!this.inv.getStackInSlot(i).isEmpty()) {
									res = false;
									break;
								}
							return res;
						}
					}, level);

			// setRecipe(recipe);
			return recipe;
		}
		return Optional.empty();

	}

	public InteractionResult mix(Player player) {

//		Optional<RecipeHolder<MixingBowlRecipe>> recipeOptional = matchRecipe();
		if (curRecipe != null) {
//			MixingBowlRecipe recipe = recipeOptional.get().value();

			if (this.stirs < curRecipe.getStirs()) {
				stirs++;

//				ItemStack[] items = getItems();
//				for (int i = 0; i < 1 + level.random.nextInt(4); i++)
//					level.addParticle(
//							new ItemParticleOption(ParticleTypes.ITEM,
//									items[items.length > 1 ? level.random.nextInt(items.length - 1) : 0]),
//							worldPosition.getX() + 0.25f + level.random.nextDouble() / 2,
//							worldPosition.getY() - 0.5f - level.random.nextDouble(),
//							worldPosition.getZ() + 0.25f + level.random.nextDouble() / 2, 0, 0, 0);

				level.playSound(player, worldPosition, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1, 1);
			} else {
				this.containerItem = curRecipe.getUsedItem().copy();

				ItemStack i = curRecipe.getResultItem(player.level().registryAccess()).copy();
				int k = i.getCount();
				var fl = curRecipe.getFluids();

				i.onCraftedBy(player.level(), player, 1);
//				NeoForgeEventFactory.firePlayerCraftingEvent(player, i, new RecipeWrapper(items));
				BlockEntityUtils.Inventory.givePlayerItemStack(i, player, level, worldPosition);
				dropContainers(items, player);
				clearItems(k);
				removeFluids(fl);
				this.stirs = 0;
//				items.setStackInSlot(CONTAINER_SLOT, i);
				complete = true;
			}
			this.updateInventory();
		}

		return InteractionResult.SUCCESS;
	}

	private void removeFluids(List<FluidIngredient> list) {
		for (int i = 0; i < list.size(); i++) {
			fluids.drain(list.get(i), IFluidHandler.FluidAction.EXECUTE);
		}
	}

	public boolean testContainerItem(ItemStack stack) {
		if (this.containerItem.isEmpty())
			return true;
		return this.containerItem.getItem() == stack.getItem();
	}

	private void dropContainers(@Nonnull IItemHandlerModifiable inv, Player player) {
		for (int i = 0; i < 9; i++) {
			BlockEntityUtils.Inventory.givePlayerItemStack(inv.getStackInSlot(i).getCraftingRemainingItem().copy(),
					player, level, worldPosition);

		}
	}

}