package com.lance5057.extradelight;

import com.lance5057.extradelight.blocks.chocolatebox.ChocolateBoxBlock;
import com.lance5057.extradelight.blocks.countercabinet.CounterCabinetBlockEntity;
import com.lance5057.extradelight.blocks.entities.TapBlockEntity;
import com.lance5057.extradelight.blocks.jar.JarBlockEntity;
import com.lance5057.extradelight.blocks.keg.KegBlockEntity;
import com.lance5057.extradelight.blocks.sink.SinkCabinetBlockEntity;
import com.lance5057.extradelight.capabilities.Chill;
import com.lance5057.extradelight.capabilities.DynamicItem;
import com.lance5057.extradelight.displays.candybowl.CandyBowlEntity;
import com.lance5057.extradelight.displays.food.FoodDisplayEntity;
import com.lance5057.extradelight.displays.knife.KnifeBlockEntity;
import com.lance5057.extradelight.displays.spice.SpiceRackEntity;
import com.lance5057.extradelight.displays.wreath.WreathEntity;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.workstations.chiller.ChillerBlockEntity;
import com.lance5057.extradelight.workstations.dryingrack.DryingRackBlockEntity;
import com.lance5057.extradelight.workstations.meltingpot.MeltingPotBlockEntity;
import com.lance5057.extradelight.workstations.mixingbowl.MixingBowlBlockEntity;
import com.lance5057.extradelight.workstations.mortar.MortarBlockEntity;
import com.lance5057.extradelight.workstations.oven.OvenBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.function.Supplier;

import static com.lance5057.extradelight.ExtraDelightClientEvents.chillMap;

@Mod.EventBusSubscriber(modid = ExtraDelight.MOD_ID)
public class CapabilityHandler {
    private static final Capability<IItemHandler> ITEM = ForgeCapabilities.ITEM_HANDLER;
    private static final Capability<IFluidHandler> FLUID = ForgeCapabilities.FLUID_HANDLER;

    @SubscribeEvent
    public static void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        //chill
        if (chillMap.containsKey(item)) {
            ExtraDelightComponents.IChillComponent chillComponent = new Chill(chillMap.get(item));
            ICapabilityProvider chillProvider = new ICapabilityProvider() {
                @Override
                public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    if (capability == ExtraDelightComponents.CHILL) {
                        return LazyOptional.of(() -> chillComponent).cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "chill"), chillProvider);
        }

        //dynamicFood
        if(item.equals(ExtraDelightItems.DYNAMIC_TOAST.get())){
            ExtraDelightComponents.IDynamicFood dynamicFood = new DynamicItem(new DynamicItemComponent());
            ICapabilityProvider dynamicFoodProvider = new ICapabilityProvider() {

                @Override
                public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    if(capability == ExtraDelightComponents.DYNAMIC_FOOD){
                        return LazyOptional.of(() -> dynamicFood).cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "dynamic_food"), dynamicFoodProvider);
        }

    }

    @SubscribeEvent
    public static void attachBlockEntityCapability(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity be = event.getObject();
        if(be instanceof CandyBowlEntity cbe){addItemHandler(event,"candy_bowl",cbe::getItemHandler);}
        if(be instanceof DryingRackBlockEntity cbe){addItemHandler(event,"drying_rack",cbe::getItemHandler);}
        if(be instanceof FoodDisplayEntity cbe){addItemHandler(event,"food_display",cbe::getItemHandler);}
        if(be instanceof KnifeBlockEntity cbe){addItemHandler(event,"Knife_block",cbe::getItemHandler);}
        if(be instanceof MixingBowlBlockEntity cbe){addItemFluidHandler(event,"mixing_bowl",cbe::getItemHandler,cbe::getFluidTank);}
        if(be instanceof MortarBlockEntity cbe){addItemFluidHandler(event,"mortar",cbe::getItemHandler,cbe::getFluidTank);}

        if(be instanceof OvenBlockEntity cbe){
            event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "oven_block"),
                    new ICapabilityProvider() {
                        @Override
                        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
                            if (cap ==ForgeCapabilities.FLUID_HANDLER) {
                                if(side == Direction.DOWN) {
                                    return LazyOptional.of(()->cbe.inputHandler).cast();
                                }
                                if(side == Direction.UP) {
                                    return LazyOptional.of(()->cbe.outputHandler).cast();
                                }
                            }
                            return LazyOptional.of(cbe::getInventory).cast();
                        }
                    });
        }

        if(be instanceof SpiceRackEntity cbe){addItemHandler(event,"spice_rack",cbe::getItemHandler);}
        if(be instanceof WreathEntity cbe){addItemHandler(event,"wreath",cbe::getItemHandler);}
        if(be instanceof CounterCabinetBlockEntity cbe){addItemHandler(event,"counter_cabine",cbe::getItemHandler);}
        if(be instanceof SinkCabinetBlockEntity cbe){addItemFluidHandler(event,"sink",cbe::getItemHandler,cbe::getFluidHandler);}
        if(be instanceof TapBlockEntity cbe){addFluidHandler(event,"tap",cbe::getFluidHandler);}
        if(be instanceof MeltingPotBlockEntity cbe){addItemFluidHandler(event,"melting_pot",cbe::getItemHandler,cbe::getFluidTank);}

        if(be instanceof ChillerBlockEntity cbe){
            event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,"chiller"),
                    new ICapabilityProvider() {
                        @Override
                        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
                            if(cap == ITEM){
                                return LazyOptional.of(cbe::getInventory).cast();
                            }
                            if(cap == FLUID){
                                if(side == Direction.UP){
                                    return LazyOptional.of(cbe::getDripTray).cast();
                                }
                                return LazyOptional.of(cbe::getFluidTank).cast();
                            }
                            return LazyOptional.empty();
                        }
                    });
        }

        if(be instanceof KegBlockEntity cbe){addFluidHandler(event,"keg",cbe::getTank);}
        }

    private static void addItemHandler(
            AttachCapabilitiesEvent<BlockEntity> event,String path,
            Supplier<IItemHandler> supplier ) {
        event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,path),
                new ICapabilityProvider() {
                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
                        if(cap == ITEM){
                            return LazyOptional.of(supplier::get).cast();
                        }
                        return LazyOptional.empty();
                    }
                });
    }

    private static void addFluidHandler(
            AttachCapabilitiesEvent<BlockEntity> event,String path,
            Supplier<IFluidHandler> supplier ) {
        event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,path),
                new ICapabilityProvider() {
                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
                        if(cap == FLUID){
                            return LazyOptional.of(supplier::get).cast();
                        }
                        return LazyOptional.empty();
                    }
                });
    }

    private static void addItemFluidHandler(
            AttachCapabilitiesEvent<BlockEntity> event,String path,
            Supplier<IItemHandler> item,
            Supplier<IFluidHandler> fluid) {
        event.addCapability(ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,path),
                new ICapabilityProvider() {
                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
                        if(cap == ITEM){
                            return LazyOptional.of(item::get).cast();
                        }
                        if(cap == FLUID){
                            return LazyOptional.of(fluid::get).cast();
                        }
                        return LazyOptional.empty();
                    }
                });
    }

}
