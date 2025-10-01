package com.lance5057.extradelight.items.dynamicfood;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.capabilities.DynamicItem;
import com.lance5057.extradelight.data.recipebuilders.ChillerRecipeBuilder;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.items.dynamicfood.api.IDynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DynamicToast extends Item implements IDynamic {
	public static BakedModel model;

	static final ResourceLocation base_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
			"extra/dynamics/toast/toast");
	static final ResourceLocation missing_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
			"extra/dynamics/toast/empty_toast");
	private FoodProperties.Builder builder;


	public DynamicToast(Properties properties) {

		super(properties);
		this.builder=new FoodProperties.Builder();
		builder.alwaysEat();


		// TODO Auto-generated constructor stub
	}

	public DynamicToast(Properties pProperties, FoodProperties.Builder builder) {
		super(pProperties);
		this.builder = builder;
		builder.alwaysEat();
	}

	@Override
	public Collection<ResourceLocation> getPieces(ItemStack itemStack) {
		ResourceLocation base =ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,"toast");
		List<ResourceLocation> i = new ArrayList<ResourceLocation>();

		i.add(base_model);

		//ItemContainerContents comp = itemStack.getComponents().get(ExtraDelightComponents.ITEMSTACK_HANDLER.get());
		LazyOptional<ExtraDelightComponents.IDynamicFood> capability = itemStack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD);
		if (capability.isPresent()) {


			ExtraDelightComponents.IDynamicFood comp = capability.orElseThrow(NullPointerException::new);
			if (comp.getDynamicFood().getItems().size() > 1) {
				ItemStack s = comp.getDynamicFood().getStackInSlot(1);
				String str = s.getItem().getDescriptionId();
				str = str.substring(str.lastIndexOf('.') + 1);
				ResourceLocation rc = ExtraDelight.modLoc( str);
				i.add(rc);
			} else
				i.add(base_model);
			//i.add(missing_model);

        } else
			i.add(missing_model);

//		i.add(bm);
		return i;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
		LazyOptional<ExtraDelightComponents.IDynamicFood> capability = pStack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD);
		if (capability.isPresent()) {
            try {
                DynamicItemComponent comp = capability.orElseThrow(NullPointerException::new).getDynamicFood();
                List<ItemStack> nonEmptyItems = new ArrayList<>();
                for (int i = 0; i < comp.getSlots(); i++) {
                    ItemStack s = comp.getStackInSlot(i);
                    if (!s.isEmpty()) {
                        nonEmptyItems.add(s);
                    }
                }
                if (!nonEmptyItems.isEmpty()) {
                    tooltip.add(Component.translatable("tooltip.dynamic.ingredients"));
                    for (ItemStack s : nonEmptyItems) {
                        tooltip.add(Component.literal(" - ").append(Component.translatable(s.getDescriptionId())));
                        if (pIsAdvanced.isAdvanced()) {
                            s.getItem().appendHoverText(s, pLevel, tooltip, pIsAdvanced);
                        }
                    }
                    if (!pIsAdvanced.isAdvanced())
                        tooltip.add(Component.translatable("tooltip.see_more").withStyle(ChatFormatting.DARK_GRAY));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}

//	@Override
//	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
//			TooltipFlag isAdvanced) {
//		ItemContainerContents comp = stack.getComponents().get(ExtraDelightComponents.ITEMSTACK_HANDLER.get());
//
//		if (comp != null) {
//			{
//				if (comp.nonEmptyItems() != null) {
//					tooltip.add(Component.translatable("tooltip.dynamic.ingredients"));
//					for (ItemStack s : comp.nonEmptyItems()) {
//						tooltip.add(Component.literal(" - ").append(Component.translatable(s.getDescriptionId())));
//						if (isAdvanced.hasShiftDown()) {
//							s.getItem().appendHoverText(stack, context, tooltip, isAdvanced);
//						}
//					}
//					if (!isAdvanced.hasShiftDown())
//						tooltip.add(Component.translatable("tooltip.see_more").withColor(0xFF555555));
//				}
//			}
//		}
//	}

	@Override
	public Component getName(ItemStack itemStack) {
//		ItemContainerContents comp = itemStack.getComponents().get(ExtraDelightComponents.ITEMSTACK_HANDLER.get());
//		if (comp != null) {
//			if (comp.getSlots() > 1)
//				return Component.translationArg(Component.translatable(this.getDescriptionId(itemStack),
//						Component.translatable(comp.getStackInSlot(1).getDescriptionId())));
//		}
//
//		return Component.translatable("dynamic.toast");

		LazyOptional<ExtraDelightComponents.IDynamicFood> capability = itemStack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD);
		if (capability!=null&&capability.isPresent()&&capability.resolve().isPresent()) {
			try {
				DynamicItemComponent dynamicFood = capability.resolve().get().getDynamicFood();
				if (!dynamicFood.getItems().isEmpty()) {
					return Component.translatable(this.getDescriptionId(itemStack),
							Component.translatable(dynamicFood.getStackInSlot(0).getDescriptionId()));
				}else {
					return Component.translatable("dynamic.toast");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Component.translatable("dynamic.toast");

	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ICapabilityProvider() {
			private final LazyOptional<ExtraDelightComponents.IDynamicFood> capability =
					LazyOptional.of(()->createDynamicFood(stack,nbt));

			private ExtraDelightComponents.IDynamicFood createDynamicFood(ItemStack stack, CompoundTag nbt) {
				DynamicItemComponent component = new DynamicItemComponent(NonNullList.create());
				DynamicItem dynamicItem = new DynamicItem(component);

				if(nbt!=null) {
					dynamicItem.deserializeNBT(nbt);
				}else if(stack.hasTag()) {
					dynamicItem.deserializeNBT(stack.getOrCreateTag());
				}

				return dynamicItem;

			}

			@Override
			public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
				if(capability == ExtraDelightComponents.DYNAMIC_FOOD) {
					return this.capability.cast();
				}
				return LazyOptional.empty();
			}

		};

	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		super.readShareTag(stack, nbt);
		if(nbt!=null) {
			stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).ifPresent(food -> {
				if(food instanceof DynamicItem dynamicItem){
					dynamicItem.deserializeNBT(nbt);
				}
			});
		}
	}

	public FoodProperties.Builder getBuilder() {
		return builder;
	}

	@Override
	public @Nullable CompoundTag getShareTag(ItemStack stack) {
		CompoundTag shareTag = super.getShareTag(stack);
		if(shareTag == null) {
			shareTag = new CompoundTag();
		}
		CompoundTag finalNbt = shareTag;
		stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).ifPresent(food -> {
			if(food instanceof DynamicItem dynamicItem){
				CompoundTag tag = dynamicItem.serializeNBT();
				finalNbt.merge(tag);
			}
		});
		return finalNbt;
	}

	public void ModifyBuilder(FoodProperties.Builder pBuilder) {
		this.builder=pBuilder;
	}

	@Override
	public boolean isEdible() {
		return true;
	}

	@Override
	public FoodProperties getFoodProperties() {
		return this.builder.build();
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		if(!pLevel.isClientSide()&&pLivingEntity instanceof Player player) {
			FoodProperties build = this.builder.build();
			player.getFoodData().eat(build.getNutrition(), build.getSaturationModifier());
			player.gameEvent(GameEvent.EAT);
            if (pStack.getTag() != null && pStack.getTag().contains("properties")) {
                CompoundTag food= pStack.getTag().getCompound("properties");
				ListTag effects = food.getList("effects", Tag.TAG_COMPOUND);
				for (int i = 0; i < effects.size(); i++) {
                    CompoundTag compound = effects.getCompound(i);
                    MobEffectInstance load = MobEffectInstance.load(compound);
                    if (load != null) {
                        player.addEffect(load);
                    }
                }
            }

        }
		if (!(pLivingEntity instanceof Player) || !((Player) pLivingEntity).getAbilities().instabuild) {
			pStack.shrink(1);
		}
		return pStack;
	}

	@Override
	public @Nonnull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand){
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		if (!itemstack.isEmpty()&&itemstack.is(ExtraDelightItems.DYNAMIC_TOAST.get())) {
			FoodProperties food1 = this.builder.build();
			if(pPlayer.canEat(food1.canAlwaysEat())){
				pPlayer.startUsingItem(pUsedHand);
				return InteractionResultHolder.consume(itemstack);
			}else{
				return InteractionResultHolder.fail(itemstack);
			}


		}
		return InteractionResultHolder.consume(itemstack);
	}
}
