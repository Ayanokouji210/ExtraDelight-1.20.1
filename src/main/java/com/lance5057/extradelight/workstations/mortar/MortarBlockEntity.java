package com.lance5057.extradelight.workstations.mortar;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.BlockEntityUtils;
import com.lance5057.extradelight.workstations.mortar.recipes.MortarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
//import net.neoforged.neoforge.items.IItemHandlerModifiable;
//import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MortarBlockEntity extends SyncedBlockEntity {
//	private final LazyOptional<IItemHandlerModifiable> handler = LazyOptional.of(this::createHandler);

	private int grinds = 0;

	public static final String INV_TAG = "inv";

	private final ItemStackHandler items = createHandler();
	private final Lazy<IItemHandlerModifiable> itemHandler = Lazy.of(() -> items);
	public static final int NUM_SLOTS = 1;

	public static final String FLUID_TAG = "fluid";
	private final FluidTank tank = createFluidHandler();

	private FluidTank createFluidHandler() {
		FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME) {
			@Override
			protected void onContentsChanged() {
				MortarBlockEntity.this.requestModelDataUpdate();
				MortarBlockEntity.this.getLevel().sendBlockUpdated(MortarBlockEntity.this.getBlockPos(),
						MortarBlockEntity.this.getBlockState(), MortarBlockEntity.this.getBlockState(), 3);
				MortarBlockEntity.this.setChanged();
			}
		};

		return tank;
	}

	public float getFullness() {
		return (float) tank.getFluidAmount() / (float) tank.getCapacity();
	}

	public MortarBlockEntity(BlockPos pPos, BlockState pState) {
		super(ExtraDelightBlockEntities.MORTAR.get(), pPos, pState);
	}

	public IItemHandlerModifiable getItemHandler() {
		return itemHandler.get();
	}

	public FluidTank getFluidTank() {
		return tank;
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(1) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 4;
			}

			@Override
			protected void onContentsChanged(int slot) {
				zeroProgress();
				updateInventory();
			}
		};
	}

	public void insertItem(ItemStack stack) {
		BlockEntityUtils.Inventory.insertItem(items, stack, NUM_SLOTS);
		this.updateInventory();
	}

	public void extractItem(Player p) {
		BlockEntityUtils.Inventory.extractItem(p, items, NUM_SLOTS);
		this.updateInventory();
	}

	public void zeroProgress() {
		grinds = 0;
	}

	public void updateInventory() {
		requestModelDataUpdate();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		this.setChanged();
	}

	public ItemStack getInsertedItem() {
		return items.getStackInSlot(0);
	}

	public int getGrind() {
		return grinds;
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
		tank.readFromNBT(nbt);

		this.grinds = nbt.getInt("Grinds");
	}

	CompoundTag writeNBT(CompoundTag tag) {

		tag.put(INV_TAG, items.serializeNBT());
		tank.writeToNBT(tag);
		tag.putInt("Grinds", this.grinds);

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

	public Optional<MortarRecipe> matchRecipe(ItemStack itemstack) {
		if (this.level != null) {
//			return level.getRecipeManager().getRecipeFor(ExtraDelightRecipes.MORTAR.get(),
//					new SingleRecipeInput(itemstack), level);
			for (Recipe<?> recipe : level.getRecipeManager().getRecipes()) {
				if (recipe instanceof MortarRecipe mortarRecipe &&
						mortarRecipe.matches(itemstack)) {
					return Optional.of(mortarRecipe);
				}
			}
		}
		return Optional.empty();

	}

	public InteractionResult grind(Player Player) {
		Optional<MortarRecipe> recipeOptional = matchRecipe(getInsertedItem());
		recipeOptional.ifPresent(recipe -> {
			//MortarRecipe recipe = r.value();
			FluidStack recipeFluid = recipe.getFluid();
			if ((tank.isEmpty() || recipeFluid.isFluidEqual(tank.getFluid()))
					&& tank.fill(recipe.getFluid(), IFluidHandler.FluidAction.SIMULATE) == recipe.getFluid().getAmount()) {

				if ((this.grinds + 1) < recipe.getGrinds()) {
					grinds++;

					for (int i = 0; i < 1 + level.random.nextInt(4); i++)
						level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, getInsertedItem()),
								worldPosition.getX() + 0.25f + level.random.nextDouble() / 2,
								worldPosition.getY() + 0.5f - level.random.nextDouble(),
								worldPosition.getZ() + 0.25f + level.random.nextDouble() / 2, 0, 0, 0);

					level.playSound(Player, worldPosition, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1, 1);
				} else {
					ItemStack in = getInsertedItem();

					for (int i = 0; i < in.getCount(); i++) {

						ItemStack it = recipe.getResultItem(this.level.registryAccess()).copy();

						BlockEntityUtils.Inventory.dropItemInWorld(it, level, worldPosition);
//						level.addFreshEntity(new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 0.5f,
//								getBlockPos().getZ(), it));
						tank.fill(recipe.getFluid(), IFluidHandler.FluidAction.EXECUTE);
					}
					items.setStackInSlot(0, ItemStack.EMPTY);

				}
				updateInventory();
			}
		});

		return InteractionResult.SUCCESS;
	}

//	@Override
//	public void setRecipeUsed(RecipeHolder<?> p_300902_) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public RecipeHolder<?> getRecipeUsed() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
