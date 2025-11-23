package com.lance5057.extradelight.capabilities;

import com.lance5057.extradelight.ExtraDelight;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class FoodComponent  {
    private int nutrition;
    private float saturation;
    private List<Pair<Supplier<MobEffectInstance>, Float>> effects;

    public FoodComponent(int nutrition, float saturation, List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.effects = effects;
    }

    public FoodComponent set(int nutrition, float saturation, List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.effects = effects;
        return this;
    }

    public FoodComponent set(FoodProperties food) {
        this.nutrition = food.getNutrition();
        this.saturation = food.getSaturationModifier();
        this.effects = food.getEffects().stream().map(pair->new Pair<Supplier<MobEffectInstance>,Float>(()->pair.getFirst(),pair.getSecond())).toList();
        return this;
    }

    public int getNutrition() {
        return nutrition;
    }

    public FoodComponent setNutrition(int nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public float getSaturation() {
        return saturation;
    }

    public FoodComponent setSaturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    public List<Pair<Supplier<MobEffectInstance>, Float>> getEffects() {
        return effects;
    }

    public FoodComponent setEffects(List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
        this.effects = effects;
        return this;
    }
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("nutrition", nutrition);
        tag.putFloat("saturation", saturation);
        ListTag list = new ListTag();
        if(!this.effects.isEmpty()) {
            for (Pair<Supplier<MobEffectInstance>, Float> pair : effects) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("effect", pair.getFirst().get().getDescriptionId());
                compoundTag.putFloat("possible", pair.getSecond());
                list.add(compoundTag);
            }
            tag.put("effects", list);
        }
        CompoundTag tag1 = new CompoundTag();
        tag1.put("food", tag);
        return tag1;
    }

    public void deserializeNBT(CompoundTag tag) {
        nutrition = tag.getInt("nutrition");
        saturation = tag.getFloat("saturation");
        if(tag.contains("effects")) {
            ListTag listTag = tag.getList("effects", 10);
            effects = new ArrayList<Pair<Supplier<MobEffectInstance>, Float>>();
            for(int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag = listTag.getCompound(i);
                if(compoundTag.contains("effect")&&compoundTag.contains("possible")) {
                    Optional<Holder<MobEffect>> holder = ForgeRegistries.MOB_EFFECTS.getHolder(
                            ResourceLocation.parse(compoundTag.getString("effect")));
                    float possible = compoundTag.getFloat("possible");
                    if(holder.isPresent()){
                        MobEffectInstance instance = new MobEffectInstance(holder.get().get());
                        effects.add(new Pair<>(() -> instance, possible));
                    }else {
                        ExtraDelight.logger.error("error parsing mob effect with id {}",compoundTag.getString("effect"));
                    }
                }else {
                    ExtraDelight.logger.error("error parsing effects,invalid nbt data",compoundTag.getString("effect"));
                }
            }
        }
    }

    public static FoodComponent fromNBT(CompoundTag tag) {
        CompoundTag food = tag.getCompound("food");
        int nutrition = food.getInt("nutrition");
        float saturation = food.getFloat("saturation");
        List<Pair<Supplier<MobEffectInstance>,Float>> list = new ArrayList<>();
        ListTag effects = food.getList("effects", Tag.TAG_COMPOUND);
        for(int i = 0; i < effects.size(); ++i) {
            CompoundTag compoundTag = effects.getCompound(i);
            if(compoundTag.contains("effect")&&compoundTag.contains("possible")) {
                Optional<Holder<MobEffect>> holder = ForgeRegistries.MOB_EFFECTS.getHolder(
                        ResourceLocation.parse(compoundTag.getString("effect")));
                float possible = compoundTag.getFloat("possible");
                holder.ifPresent(mobEffectHolder ->
                        list.add(new Pair<>(
                                () -> new MobEffectInstance(mobEffectHolder.get()), possible)));
            }
        }

        return new FoodComponent(nutrition, saturation, list);

    }

    public static FoodProperties.Builder getBuilder(FoodComponent foodComponent) {
        FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
        if(foodComponent != null) {
            foodBuilder.nutrition(foodComponent.getNutrition());
            foodBuilder.saturationMod(foodComponent.getSaturation());
            for(Pair<Supplier<MobEffectInstance>, Float> pair : foodComponent.getEffects()){
                foodBuilder.effect(pair.getFirst(),pair.getSecond());
            }
            return  foodBuilder;
        }
        return foodBuilder;
    }

    public FoodProperties.Builder getBuilder(){
        return getBuilder(this);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity){
        if(!pLevel.isClientSide()&&pLivingEntity instanceof Player player) {
            player.gameEvent(GameEvent.EAT);
            if(pStack.getTag()!=null && pStack.getTag().contains("food")) {
                FoodComponent foodComponent = fromNBT(pStack.getTag());
                for(Pair<Supplier<MobEffectInstance>,Float> pair:foodComponent.effects){
                    if(pair.getSecond()>=1.0f){
                        player.addEffect(pair.getFirst().get());
                    }else {
                        Random random = new Random();
                        if(random.nextFloat()<pair.getSecond()){
                            player.addEffect(pair.getFirst().get());
                        }
                    }
                }
                player.getFoodData().eat(foodComponent.nutrition,foodComponent.saturation);
            }
        }
        if (!(pLivingEntity instanceof Player) || !((Player) pLivingEntity).getAbilities().instabuild) {
            pStack.shrink(1);
        }
        return pStack;
    }

}
