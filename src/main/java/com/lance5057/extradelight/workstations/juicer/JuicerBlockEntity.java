package com.lance5057.extradelight.workstations.juicer;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import com.lance5057.extradelight.ExtraDelightRecipes;
import com.lance5057.extradelight.util.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class JuicerBlockEntity extends SyncedBlockEntity {
//	private final LazyOptional<IItemHandlerModifiable> handler = LazyOptional.of(this::createHandler);

	private int grinds = 0;

	public static final String INV_TAG = "inv";

	private final ItemStackHandler items = createHandler();
	private final Lazy<IItemHandlerModifiable> itemHandler = Lazy.of(() -> items);
	public static final int NUM_SLOTS = 1;

	public static final String FLUID_TAG = "fluid";
	private final FluidTank tank = createFluidHandler();

	private FluidTank createFluidHandler() {
		FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME * 4) {
			@Override
			protected void onContentsChanged() {
				JuicerBlockEntity.this.requestModelDataUpdate();
				JuicerBlockEntity.this.getLevel().sendBlockUpdated(JuicerBlockEntity.this.getBlockPos(),
						JuicerBlockEntity.this.getBlockState(), JuicerBlockEntity.this.getBlockState(), 3);
				JuicerBlockEntity.this.setChanged();
			}
		};

		return tank;
	}

	public float getFullness() {
		return (float) tank.getFluidAmount() / (float) tank.getCapacity();
	}

	public JuicerBlockEntity(BlockPos pPos, BlockState pState) {
		super(ExtraDelightBlockEntities.JUICER.get(), pPos, pState);
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
				return 8;
			}

			@Override
			protected void onContentsChanged(int slot) {
				zeroProgress();
				updateInventory();
			}
		};
	}

	public void insertItem(ItemStack stack) {
		Optional<JuicerRecipe> recipeOptional = matchRecipe(stack);
		recipeOptional.ifPresent(r -> {
			BlockEntityUtils.Inventory.insertItem(items, stack, NUM_SLOTS);
			this.updateInventory();
		});
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

	public Optional<JuicerRecipe> matchRecipe(ItemStack itemstack) {
		if (this.level != null) {
            for(JuicerRecipe recipe:level.getRecipeManager().getAllRecipesFor(ExtraDelightRecipes.JUICER.get())) {
                if(recipe.matches(itemstack)){
                    return Optional.of(recipe);
                }
            }
		}
		return Optional.empty();

	}

	public InteractionResult grind(Player Player) {
		Optional<JuicerRecipe> recipeOptional = matchRecipe(getInsertedItem());
		recipeOptional.ifPresent(r -> {
			JuicerRecipe recipe = r;
			if ((tank.isEmpty() || recipe.getFluid().isFluidEqual(tank.getFluid()))
					&& tank.fill(recipe.getFluid(), IFluidHandler.FluidAction.SIMULATE) == recipe.getFluid().getAmount()) {

				if ((this.grinds + 1) < 8) {
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
						if (this.level.random.nextInt(100) < recipe.percentChance) {
							ItemStack it = recipe.getResultItem(this.level.registryAccess()).copy();

							BlockEntityUtils.Inventory.dropItemInWorld(it, level, worldPosition);
						}
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
