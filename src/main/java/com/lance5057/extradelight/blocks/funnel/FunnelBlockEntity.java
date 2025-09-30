package com.lance5057.extradelight.blocks.funnel;

import com.lance5057.extradelight.ExtraDelightBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.FluidUtil;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.Optional;

public class FunnelBlockEntity extends BlockEntity {
	private int cooldownTime = -1;
	private long tickedGameTime;

	private final FluidTank fluid = createFluidHandler();

	public FluidTank getFluidTank() {
		return fluid;
	}

	private FluidTank createFluidHandler() {
		return new FluidTank(FluidType.BUCKET_VOLUME) {
			@Override
			protected void onContentsChanged() {
				FunnelBlockEntity.this.requestModelDataUpdate();
				FunnelBlockEntity.this.getLevel().sendBlockUpdated(FunnelBlockEntity.this.getBlockPos(),
						FunnelBlockEntity.this.getBlockState(), FunnelBlockEntity.this.getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
				FunnelBlockEntity.this.setChanged();
			}
		};
	}

	public FunnelBlockEntity(BlockPos pos, BlockState blockState) {
		super(ExtraDelightBlockEntities.FUNNEL.get(), pos, blockState);
		// TODO Auto-generated constructor stub
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		if (be instanceof FunnelBlockEntity funnel) {
			if (state.getValue(FunnelBlock.ENABLED)) {
				if (!funnel.isOnCooldown()) {
					funnel.setCooldown(8);
					Direction dir = state.getValue(FunnelBlock.FACING);

					pushFluidOut(level, pos, dir, funnel);
					pullFluidIn(level, pos, funnel);
				} else
					funnel.cooldownTime--;
			}
		}
	}

	public void setCooldown(int cooldownTime) {
		this.cooldownTime = cooldownTime;
	}

	private boolean isOnCooldown() {
		return this.cooldownTime > 0;
	}

	private static void pullFluidIn(Level level, BlockPos pos, FunnelBlockEntity funnel) {
		BlockState state = level.getBlockState(pos.above());
		Block block = state.getBlock();
		if (funnel.fluid.isEmpty())
			if (block instanceof LiquidBlock liquid) {
				funnel.fluid.fill(new FluidStack(liquid.getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE);
				level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			} else {
				Optional<IFluidHandler> holder = FluidUtil.getFluidHandler(level, pos.above(), Direction.UP).resolve();
				if (holder.isPresent()) {
					IFluidHandler handler = holder.get();
					FluidStack fluid = handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
					funnel.fluid.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
				}
			}
	}

	private static void pushFluidOut(Level level, BlockPos pos, Direction dir, FunnelBlockEntity funnel) {
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		if (!funnel.fluid.isEmpty()) {
			Optional<IFluidHandler> holder = FluidUtil.getFluidHandler(level, pos.relative(dir), dir).resolve();
			if (holder.isPresent()) {
				IFluidHandler handler = holder.get();

				int filled = handler.fill(funnel.fluid.getFluid(), IFluidHandler.FluidAction.EXECUTE);
				funnel.fluid.drain(filled, IFluidHandler.FluidAction.EXECUTE);
			} else {
				Block block1 = level.getBlockState(pos.relative(dir)).getBlock();
				if (block1==Blocks.AIR||block1==Blocks.CAVE_AIR||block1==Blocks.VOID_AIR) {
					if (funnel.fluid.getFluidAmount() >= 1000)
						FluidUtil.tryPlaceFluid(null, level, null, pos.relative(dir), funnel.fluid,
								funnel.fluid.getFluid());
				}
			}
		}

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
//		writeNBT(tag, null);

		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		// InteractionHandle your Data
		readNBT(tag);
	}

	void readNBT(CompoundTag nbt) {
		fluid.readFromNBT(nbt);
		this.cooldownTime = nbt.getInt("TransferCooldown");
	}

	CompoundTag writeNBT(CompoundTag tag) {

		fluid.writeToNBT(tag);
		tag.putInt("TransferCooldown", this.cooldownTime);
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
