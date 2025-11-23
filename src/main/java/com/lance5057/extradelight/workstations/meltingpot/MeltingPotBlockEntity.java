package com.lance5057.extradelight.workstations.meltingpot;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightConfig;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.BottleFluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
//import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
//import net.neoforged.neoforge.items.IItemHandlerModifiable;
//import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import javax.annotation.Nonnull;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MeltingPotBlockEntity extends BlockEntity implements HeatableBlockEntity {
	public static final String ITEM_TAG = "inv";

	private final ItemStackHandler items = createItemHandler();
	private final Lazy<IItemHandlerModifiable> itemHandler = Lazy.of(() -> items);

	public static final int INPUT_SLOT = 0;
	public static final int BUCKET_SLOT = 1;
	public static final int BUCKET_SLOT_OUT = 2;

	private final FluidTank fluids = createFluidHandler();

	public int cookingTime = 0;
	public int cookingProgress = 0;

//	private final RecipeManager.CachedCheck<SingleRecipeInput, MeltingPotRecipe> quickCheck;

	public FluidTank getFluidTank() {
		return fluids;
	}

	public MeltingPotBlockEntity(BlockPos pos, BlockState blockState) {
		super(ExtraDelightBlockEntities.MELTING_POT.get(), pos, blockState);
//		this.quickCheck = RecipeManager.createCheck(ExtraDelightRecipes.MELTING_POT.get());
	}

	public IItemHandlerModifiable getItemHandler() {
		return itemHandler.get();
	}

	private ItemStackHandler createItemHandler() {
		return new ItemStackHandler(3) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (slot == 2)
					return false;
				return true;
			}

			@Override
			@Nonnull
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
//				if (slot == 1)
//					if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM) != null)
//						return super.insertItem(slot, stack, simulate);
//					else
//						return stack;

				return super.insertItem(slot, stack, simulate);
			}

			@Override
			protected void onContentsChanged(int slot) {

				updateInventory();
			}
		};
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		MeltingPotBlockEntity pBlockEntity = (MeltingPotBlockEntity) be;
		if (pBlockEntity.isHeated(level,pos)) {
//			boolean flag = false;
			IItemHandlerModifiable h = pBlockEntity.getItemHandler();
			FluidTank f = pBlockEntity.getFluidTank();

			ItemStack itemstack = h.getStackInSlot(0);
//			RecipeHolder<MeltingPotRecipe> recipeholder;
			MeltingPotRecipe recipe = null;
			if (!itemstack.isEmpty()) {
//				recipeholder = pBlockEntity.quickCheck.getRecipeFor(new SingleRecipeInput(itemstack), level)
//						.orElse(null);
				Optional<MeltingPotRecipe> recipeOptional = pBlockEntity.matchRecipe(itemstack);
				if (recipeOptional.isPresent()) {
					recipe = recipeOptional.get();
					pBlockEntity.cookingTime = recipe.cooktime;
				}else {
					pBlockEntity.cookingTime = 0;
					pBlockEntity.cookingProgress = 0;
				}
			} else {

				pBlockEntity.cookingTime = 0;
				pBlockEntity.cookingProgress = 0;
			}

			if (recipe != null) {
				FluidStack fluid = recipe.result.copy();

				if (f.isEmpty() || f.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount())
					if (!itemstack.isEmpty()) {
//						flag = true;
						pBlockEntity.cookingProgress++;
						if(ExtraDelightConfig.ENABLE_DEBUG_MODE.get())
							pBlockEntity.cookingProgress+=100;
						
						if (pBlockEntity.cookingProgress >= pBlockEntity.cookingTime) {
							f.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
							itemstack.shrink(1);
							pBlockEntity.cookingProgress = 0;
							if (itemstack.isEmpty()) {
								pBlockEntity.cookingTime = 0;
							}
							level.sendBlockUpdated(pos, state, state, 3);
						} else {

							for (int k = 0; k < 1; k++) {
								level.addParticle(ParticleTypes.DOLPHIN, pos.getX() + level.random.nextDouble() / 16,
										pos.getY() - level.random.nextDouble() / 16,
										pos.getZ() + level.random.nextDouble() / 16, 0, 1f, 0);
							}
						}
					} else {
						pBlockEntity.cookingProgress = 0;
					}

//				if (flag) {

//				}
			} else {
				pBlockEntity.cookingProgress = 0;
			}

			if (!h.getStackInSlot(BUCKET_SLOT).isEmpty()) {
				ItemStack inputItem = h.getStackInSlot(BUCKET_SLOT);
				ItemStack outputItem = h.getStackInSlot(BUCKET_SLOT_OUT);
				if (inputItem.getItem() == Items.BUCKET) {
					if (!f.getFluid().isEmpty()) {
						FluidStack stack = f.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
						if (stack.getAmount() == FluidType.BUCKET_VOLUME
								&& (outputItem.isEmpty() || (outputItem.getItem() == stack.getFluid().getBucket()
										&& outputItem.getCount() < outputItem.getMaxStackSize()))) {
							f.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
							inputItem.shrink(1);
							if (outputItem.isEmpty()) {
								h.setStackInSlot(2, stack.getFluid().getBucket().getDefaultInstance());
							} else {
								outputItem.grow(1);
							}
						}
					}
				} else if (inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
					IFluidHandlerItem fluidHandlerItem = inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
					FluidUtil.tryFluidTransfer(fluidHandlerItem, f, f.getFluidAmount(), true);
				} else if (inputItem.is(Items.GLASS_BOTTLE)) {
					ItemStack i = BottleFluidRegistry.getBottleFromFluid(pBlockEntity.getFluidTank().getFluid());
					if (!i.isEmpty()) {
						if (pBlockEntity.getFluidTank().drain(250, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
							pBlockEntity.getFluidTank().drain(250, IFluidHandler.FluidAction.EXECUTE);
							inputItem.shrink(1);
							
							if(pBlockEntity.items.getStackInSlot(BUCKET_SLOT_OUT).isEmpty())
								pBlockEntity.items.setStackInSlot(BUCKET_SLOT_OUT, i);
							else
							{
								ItemStack s = pBlockEntity.items.getStackInSlot(BUCKET_SLOT_OUT);
								
								if(ItemStack.isSameItem(i, s))
								{
									s.setCount(s.getCount()+i.getCount());								}
							}
						}
					}
				}

			}
		}
		pBlockEntity.updateInventory();
	}

	public boolean isHeated() {
		if (level == null)
			return false;
		return this.isHeated(level, worldPosition);
	}

	public void updateInventory() {
		requestModelDataUpdate();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(),
				Block.UPDATE_CLIENTS);
		this.setChanged();
	}

//	public Optional<RecipeHolder<MeltingPotRecipe>> matchRecipe(ItemStack itemstack) {
//		if (this.level != null) {
//			return level.getRecipeManager().getRecipeFor(ExtraDelightRecipes.MELTING_POT.get(),
//					new SingleRecipeInput(itemstack), level);
//		}
//		return Optional.empty();
//
//	}

	public Optional<MeltingPotRecipe> matchRecipe(ItemStack itemstack) {
		if (this.level != null) {
			for (MeltingPotRecipe recipe : level.getRecipeManager().getAllRecipesFor(ExtraDelightRecipes.MELTING_POT.get())) {
				if (recipe.matches(itemstack)) {
					return Optional.of(recipe);
				}
			}
		}
		return Optional.empty();
	}

	private FluidTank createFluidHandler() {
		FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME * 6) {
			@Override
			protected void onContentsChanged() {
				MeltingPotBlockEntity.this.requestModelDataUpdate();
				MeltingPotBlockEntity.this.getLevel().sendBlockUpdated(MeltingPotBlockEntity.this.getBlockPos(),
						MeltingPotBlockEntity.this.getBlockState(), MeltingPotBlockEntity.this.getBlockState(), 3);
				MeltingPotBlockEntity.this.setChanged();
			}
		};

		return tank;
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
		readNBT(tag);
	}

	void readNBT(CompoundTag nbt) {
		fluids.readFromNBT( nbt);
		if (nbt.contains(ITEM_TAG)) {
			items.deserializeNBT( nbt.getCompound(ITEM_TAG));
		}

		this.cookingTime = nbt.getInt("cooktime");
		this.cookingProgress = nbt.getInt("cookprogress");
	}

	CompoundTag writeNBT(CompoundTag tag) {

		fluids.writeToNBT( tag);
		tag.put(ITEM_TAG, items.serializeNBT());

		tag.putInt("cooktime", this.cookingTime);
		tag.putInt("cookprogress", cookingProgress);

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
