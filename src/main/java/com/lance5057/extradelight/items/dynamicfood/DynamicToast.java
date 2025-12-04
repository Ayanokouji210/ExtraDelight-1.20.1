package com.lance5057.extradelight.items.dynamicfood;

import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.capabilities.DynamicItem;
import com.lance5057.extradelight.data.recipebuilders.ChillerRecipeBuilder;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import com.lance5057.extradelight.items.dynamicfood.api.IDynamic;
import com.lance5057.extradelight.util.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//import net.minecraft.world.item.component.ItemContainerContents;

import java.util.*;

public class DynamicToast extends Item implements IDynamic {
	public static BakedModel model;

	static final ResourceLocation base_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
			"extra/dynamics/toast/toast");
	static final ResourceLocation missing_model = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,
			"extra/dynamics/toast/empty_toast");
	private final FoodProperties.Builder builder;


    @Override
    public Object getRenderPropertiesInternal() {
        return super.getRenderPropertiesInternal();
    }

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
	public List<ResourceLocation> getPieces(ItemStack itemStack) {
		ResourceLocation base =ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID,"toast");
		List<ResourceLocation> i = new ArrayList<ResourceLocation>();

		i.add(base_model);

		//ItemContainerContents comp = itemStack.getComponents().get(ExtraDelightComponents.ITEMSTACK_HANDLER.get());
//		LazyOptional<ExtraDelightComponents.IDynamicFood> capability = itemStack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD);
//		if (capability.isPresent()) {
//
//
//			ExtraDelightComponents.IDynamicFood comp = capability.orElseThrow(NullPointerException::new);
//			if (comp.graphics().size() > 1) {
//				ItemStack s = new ItemStack(CraftingHelper.getItem(comp.graphics().get(0),true));
//				String str = s.getItem().getDescriptionId();
//				str = str.substring(str.lastIndexOf('.') + 1);
//				ResourceLocation rc = ExtraDelight.modLoc( str);
//				i.add(rc);
//			} else
//                i.add(missing_model);
//
//        } else
//			i.add(missing_model);

        List<String> read = ExtraDelightComponents.IDynamicFood.read(itemStack);
        if(!read.isEmpty()){
            if (!itemStack.getOrCreateTag().contains(ExtraDelightComponents.IDynamicFood.TAG)) {
                String str = "extra/dynamics/toast/";
                ResourceLocation rc = ExtraDelight.modLoc(str + read.get(0));
                i.add(rc);
            }
            if(itemStack.getOrCreateTag().contains("dynamic")){
                String str = "extra/dynamics/toast/dynamic_jam/";
                ResourceLocation rc = ExtraDelight.modLoc(str + itemStack.getOrCreateTag().getString("dynamic"));
                i.add(rc);
            }
        }else
            i.add(missing_model);
//		i.add(bm);
		return i;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        Optional<ExtraDelightComponents.EDItemStackHandler> resolve = pStack.getCapability(ExtraDelightComponents.ITEMSTACK_HANDLER).resolve();
        if(resolve.isPresent()) {
            ExtraDelightComponents.EDItemStackHandler handler = resolve.get();
            if(handler.nonEmptyItems() != null){
                tooltip.add(Component.translatable("tooltip.dynamic.ingredients"));
                for (ItemStack s : handler.nonEmptyItems()) {
                    tooltip.add(Component.literal(" - ").append(s.getItem().getName(s)));
                    if (pIsAdvanced.isAdvanced()) {
                        s.getItem().appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
                    }
                }
                if (!pIsAdvanced.isAdvanced())
                    tooltip.add(Component.translatable("tooltip.see_more").withStyle(Style.EMPTY.withColor( 0xFF555555)));
            }
        }

    }

	@Override
	public Component getName(ItemStack itemStack) {
        NonNullList<ItemStack> read = ExtraDelightComponents.EDItemStackHandler.read(itemStack);
        for(ItemStack s : read.stream().filter(i->!i.isEmpty()).toList()) {
            if(!s.is(ExtraDelightItems.TOAST.get())){
                return Component.translatable(this.getDescriptionId(itemStack),
                                    s.getItem().getName(s));
            }
        }
        return Component.translatable("dynamic.toast");

	}

    @Override
    public @org.jetbrains.annotations.Nullable FoodProperties getFoodProperties(ItemStack stack, @org.jetbrains.annotations.Nullable LivingEntity entity) {
        NBTUtil.IFood iFood = new NBTUtil.IFood();
        iFood.deserializeNBT(stack.getOrCreateTag());
        return iFood.getFoodProperties();
    }

    @Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ICapabilityProvider() {
			private final LazyOptional<ExtraDelightComponents.IDynamicFood> capability =
					LazyOptional.of(()->createDynamicFood(stack,nbt));

			private ExtraDelightComponents.IDynamicFood createDynamicFood(ItemStack stack, CompoundTag nbt) {
				DynamicItem dynamicItem = new DynamicItem(List.of());

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

	@Override
	public boolean isEdible() {
		return true;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
//		if(!pLevel.isClientSide()&&pLivingEntity instanceof Player player) {
//            FoodProperties build = this.builder.build();
//			player.getFoodData().eat(build.getNutrition(), build.getSaturationModifier());
//			player.gameEvent(GameEvent.EAT);
//            if (pStack.getTag() != null && pStack.getTag().contains("properties")) {
//                CompoundTag food= pStack.getTag().getCompound("properties");
//				ListTag effects = food.getList("effects", Tag.TAG_COMPOUND);
//				for (int i = 0; i < effects.size(); i++) {
//                    CompoundTag compound = effects.getCompound(i);
//                    MobEffectInstance load = MobEffectInstance.load(compound);
//                    if (load != null) {
//                        player.addEffect(load);
//                    }
//                }
//            }
//
//        }
//		if (!(pLivingEntity instanceof Player) || !((Player) pLivingEntity).getAbilities().instabuild) {
//			pStack.shrink(1);
//		}
//		return pStack;
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
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
