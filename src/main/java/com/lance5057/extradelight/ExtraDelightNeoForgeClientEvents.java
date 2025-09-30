package com.lance5057.extradelight;

import com.lance5057.butchercraft.Butchercraft;
import com.mojang.blaze3d.shaders.FogShape;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
//import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
//import net.minecraft.world.item.component.TooltipProvider;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.ModList;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ViewportEvent;
//import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
//import net.neoforged.neoforge.registries.RegistryObject;
//import com.lance5057.butchercraft.Butchercraft;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ExtraDelight.MOD_ID, value = Dist.CLIENT)
public class ExtraDelightNeoForgeClientEvents {
	@SubscribeEvent
	public static void onFogDensityEvent(ViewportEvent.RenderFog event) {
		if (Minecraft.getInstance().player.level().dimension() == ExtraDelightWorldGen.CORNFIELD) {
			event.setNearPlaneDistance(-8);
			event.scaleFarPlaneDistance(0.25f);
			event.setFogShape(FogShape.CYLINDER);
			event.setCanceled(true);
		}

	}

	@SubscribeEvent
	public static void onFogColorEvent(ViewportEvent.ComputeFogColor event) {
		if (Minecraft.getInstance().player.level().dimension() == ExtraDelightWorldGen.CORNFIELD) {
			float f = 0.5f;
			event.setRed(f);
			event.setBlue(f);
			event.setGreen(f);
		}
	}

//	@SubscribeEvent
//	public static void registerComponentToolTips(ItemTooltipEvent event) {
//		ItemStack stack = event.getItemStack();
//		var ctx = event.getFlags();
//		List<Component> tooltip = event.getToolTip();
//		TooltipFlag flag = event.getFlags();
//
//		TooltipModifier tooltipProvider = stack.get(ExtraDelightComponents.CHILL.get());
//
//		if (tooltipProvider != null) {
//
//			tooltipProvider.addToTooltip(ctx, i -> {
//				tooltip.add(i);
//			}, flag);
//		}
//
//		TooltipProvider tooltipProvider1 = stack.get(ExtraDelightComponents.DYNAMIC_FOOD.get());
//
//		if (tooltipProvider1 != null) {
//			tooltipProvider1.addToTooltip(ctx, i -> {
//				tooltip.add(i);
//			}, flag);
//		}
//	}



//	@SubscribeEvent
//	public static void registerComponentToolTips(ItemTooltipEvent event) {
//		ItemStack stack = event.getItemStack();
//		// 在 1.20.1 Forge 中，event.getFlags() 返回的是 TooltipFlag，而不是上下文（ctx）
//		// Flag 通常用于判断是高级模式还是普通模式（如在创意模式悬停查看）
//		var tooltipFlag = event.getFlags();
//		List<Component> tooltip = event.getToolTip();

		// 关键修改 1: 组件获取方式
		// 在 1.20.1 Forge 中，通常使用 Capability 系统而不是直接的组件获取
		// 假设 ExtraDelightComponents.CHILL 是一个 Capability
//		LazyOptional<ExtraDelightComponents.IChillComponent> chillCap = stack.getCapability(ExtraDelightComponents.CHILL);
//		// 使用 ifPresent 来处理可能存在的 Capability
//		chillCap.ifPresent(provider -> {
//			// 假设你的 TooltipModifier 有一个方法可以添加提示
//			// 你可能需要调整 addToTooltip 方法的参数来适应 Forge 的 TooltipFlag
//			provider.addToTooltip(tooltip, tooltipFlag);
//		});

//		// 同样处理 DYNAMIC_FOOD Capability
//		LazyOptional<DynamicFoodCapability> foodCap = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD);
//		foodCap.ifPresent(provider -> {
//			provider.addToTooltip(tooltip, tooltipFlag);
//		});

		// 注意: 上面的 LazyOptional.ifPresent 是异步的，但工具提示事件是即时处理的，所以通常没问题。
//	}

	@SubscribeEvent
	public static void addToolTip(ItemTooltipEvent event) {
		if (feasts.stream().anyMatch(s -> event.getItemStack().is(s.get()))) {
			event.getToolTip()
					.add(Component.translatable(ExtraDelight.MOD_ID + ".tooltip.feast").withStyle(ChatFormatting.BLUE));
		}

		if (servings.stream().anyMatch(s -> event.getItemStack().is(s.get()))) {
			event.getToolTip().add(
					Component.translatable(ExtraDelight.MOD_ID + ".tooltip.serving").withStyle(ChatFormatting.BLUE));
		}

		if (!ModList.get().isLoaded("butchercraft")) {
			ExtraDelight.logger.warn("{} is not installed", Butchercraft.MOD_ID);

			if (butchercraft.stream().anyMatch(s -> event.getItemStack().is(s.get()))) {
				event.getToolTip().add(Component.translatable(ExtraDelight.MOD_ID + ".tooltip.butchercraft")
						.withStyle(ChatFormatting.RED));
			}
		}
	}

	public static Set<RegistryObject<Item>> feasts = new HashSet<RegistryObject<Item>>();

	public static Set<RegistryObject<Item>> servings = new HashSet<RegistryObject<Item>>();

	public static Set<RegistryObject<Item>> butchercraft = new HashSet<RegistryObject<Item>>();

}
