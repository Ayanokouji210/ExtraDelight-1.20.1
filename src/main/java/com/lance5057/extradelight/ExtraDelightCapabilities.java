//package com.lance5057.extradelight;
//
//import com.blamejared.crafttweaker.api.capability.Capabilities;
//import net.minecraft.core.Direction;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityManager;
//import net.minecraftforge.common.capabilities.CapabilityToken;
//import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import net.minecraftforge.items.IItemHandler;
//import org.jline.utils.InfoCmp;
////import net.neoforged.neoforge.capabilities.Capabilities;
////import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
////import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
////import net.neoforged.neoforge.items.ComponentItemHandler;
//
//import net.minecraftforge.common.capabilities.*;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.ItemStackHandler;
//
//
//public class ExtraDelightCapabilities {
//	public static final Capability<IItemHandler> ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
//	public static final Capability<IFluidHandler> FLUID_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
//
//	public static void registerCapabilities(RegisterCapabilitiesEvent event) {;
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.CANDY_BOWL.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.DRYING_RACK.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.FOOD_DISPLAY.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.KNIFE_BLOCK.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.MIXING_BOWL.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.MIXING_BOWL.get(),
//				(o, d) -> o.getFluidTank());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.MORTAR.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.MORTAR.get(),
//				(o, d) -> o.getFluidTank());
//
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.OVEN.get(),
//				(be, context) -> {
//					if (context == Direction.DOWN) {
//						return be.inputHandler;
//					} else if (context == Direction.UP)
//						return be.outputHandler;
//					else
//						return be.getInventory();
//				});
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.SPICE_RACK.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.WREATH.get(),
//				(o, d) -> o.getItemHandler());
//
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.COUNTER_CABINET_BLOCK.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.SINK_BLOCK.get(),
//				(o, d) -> o.getItemHandler());
//
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.TAP.get(),
//				(o, d) -> o.getFluidHandler());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.SINK_BLOCK.get(),
//				(o, d) -> o.getFluidHandler());
//
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.MELTING_POT.get(),
//				(o, d) -> o.getFluidTank());
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.MELTING_POT.get(),
//				(o, d) -> o.getItemHandler());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.CHILLER.get(),
//				(be, context) -> {
//					if (context == Direction.UP) {
//						return be.getDripTray();
//					}
//					return be.getFluidTank();
//				});
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.CHILLER.get(),
//				(be, context) -> {
//					return be.getInventory();
//				});
//
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.KEG.get(),
//				(o, d) -> o.getTank());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.JAR.get(),
//				(o, d) -> o.getTank());
//		event.registerItem(Capabilities.FluidHandler.ITEM,
//				(o, d) -> new FluidHandlerItemStack(ExtraDelightComponents.FLUID, o, 1000),
//				ExtraDelightItems.JAR.get());
//
//		event.registerItem(Capabilities.ItemHandler.ITEM,
//				(stack, context) -> new ComponentItemHandler(stack, ExtraDelightComponents.ITEMSTACK_HANDLER.get(), 8),
//				ExtraDelightItems.WHITE_CHOCOLATE_BOX.get(), ExtraDelightItems.ORANGE_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.MAGENTA_CHOCOLATE_BOX.get(), ExtraDelightItems.LIGHT_BLUE_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.YELLOW_CHOCOLATE_BOX.get(), ExtraDelightItems.LIME_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.PINK_CHOCOLATE_BOX.get(), ExtraDelightItems.GRAY_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.LIGHT_GRAY_CHOCOLATE_BOX.get(), ExtraDelightItems.CYAN_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.BLUE_CHOCOLATE_BOX.get(), ExtraDelightItems.BROWN_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.GREEN_CHOCOLATE_BOX.get(), ExtraDelightItems.RED_CHOCOLATE_BOX.get(),
//				ExtraDelightItems.BLACK_CHOCOLATE_BOX.get(), ExtraDelightItems.PURPLE_CHOCOLATE_BOX.get());
//
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.CHOCOLATE_BOX.get(),
//				(be, context) -> be.getItems());
//
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.FUNNEL.get(), (o, d) -> {
//			return o.getFluidTank();
//		});
//
//		event.register(ForgeCapabilities.ITEM_HANDLER, ExtraDelightBlockEntities.EVAPORATOR.get(),
//				(be, context) -> be.getItemHandler());
//		event.register(Capabilities.FluidHandler.BLOCK, ExtraDelightBlockEntities.EVAPORATOR.get(),
//				(o, d) -> {
//					if (o.isInventoryEmpty())
//						return o.getFluidTank();
//					return null;
//				});
//
//		event.registerItem(Capabilities.ItemHandler.ITEM,
//				(o, d) -> new ComponentItemHandler(o, ExtraDelightComponents.ITEMSTACK_HANDLER.get(), 7),
//				ExtraDelightItems.DYNAMIC_TEST2.get());
//	}
//}
