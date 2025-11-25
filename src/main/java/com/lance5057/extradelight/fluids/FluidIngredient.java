package com.lance5057.extradelight.fluids;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FluidIngredient implements Predicate<FluidStack> {
    public static final FluidIngredient EMPTY = new FluidStackIngredient();
    protected int amount;
    public List<FluidStack> matchingFluidStacks;

    public static FluidIngredient of(TagKey<Fluid> tag, int amount) {
        FluidIngredient.FluidTagIngredient ingredient = new FluidIngredient.FluidTagIngredient();
        ingredient.tag = tag;
        ingredient.amount = amount;
        return ingredient;
    }

    public static FluidIngredient of(Fluid fluid, int amount) {
        FluidIngredient.FluidStackIngredient ingredient = new FluidIngredient.FluidStackIngredient();
        ingredient.fluid = fluid;
        ingredient.amount = amount;
        ingredient.fixFlowing();
        return ingredient;
    }

    public static FluidIngredient of(FluidStack fluidStack) {
        FluidIngredient.FluidStackIngredient ingredient = new FluidIngredient.FluidStackIngredient();
        ingredient.fluid = fluidStack.getFluid();
        ingredient.amount = fluidStack.getAmount();
        ingredient.fixFlowing();
        if (fluidStack.hasTag())
            ingredient.tagToMatch = fluidStack.getTag();
        return ingredient;
    }

    public static FluidIngredient fromTag(TagKey<Fluid> tag, int amount) {
        return of(tag, amount);
    }

    public static FluidIngredient fromFluid(Fluid fluid, int amount) {
        return of(fluid, amount);
    }

    public static FluidIngredient fromFluidStack(FluidStack fluidStack) {
        return of(fluidStack);
    }



    protected int getAmount() {
        return amount;
    }

    protected abstract boolean testInternal(FluidStack t);

    protected abstract void readInternal(FriendlyByteBuf buffer);

    protected abstract void writeInternal(FriendlyByteBuf buffer);

    protected abstract void readInternal(JsonObject json);

    protected abstract void writeInternal(JsonObject json);

    protected abstract List<FluidStack> determineMatchingFluidStacks();

    public int getRequiredAmount() {
        return amount;
    }

    public List<FluidStack> getMatchingFluidStacks() {
        if (matchingFluidStacks != null)
            return matchingFluidStacks;
        return matchingFluidStacks = determineMatchingFluidStacks();
    }

    @Override
    public boolean test(FluidStack t) {
        if (t == null)
            throw new IllegalArgumentException("FluidStack cannot be null");
        return testInternal(t);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this instanceof FluidIngredient.FluidTagIngredient);
        buffer.writeVarInt(amount);
        writeInternal(buffer);
    }

    public static FluidIngredient read(FriendlyByteBuf buffer) {
        boolean isTagIngredient = buffer.readBoolean();
        FluidIngredient ingredient = isTagIngredient ? new FluidIngredient.FluidTagIngredient() : new FluidIngredient.FluidStackIngredient();
        ingredient.amount = buffer.readVarInt();
        ingredient.readInternal(buffer);
        return ingredient;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        writeInternal(json);
        json.addProperty("amount", amount);
        return json;
    }

    public static boolean isFluidIngredient(@Nullable JsonElement je) {
        if (je == null || je.isJsonNull())
            return false;
        if (!je.isJsonObject())
            return false;
        JsonObject json = je.getAsJsonObject();
        if (json.has("fluidTag"))
            return true;
        else if (json.has("fluid"))
            return true;
        return false;
    }

    public static FluidIngredient deserialize(@Nullable JsonElement je) {
        if (!isFluidIngredient(je))
            throw new JsonSyntaxException("Invalid fluid ingredient: " + je);

        JsonObject json = je.getAsJsonObject();
        FluidIngredient ingredient = json.has("fluidTag") ? new FluidIngredient.FluidTagIngredient() : new FluidIngredient.FluidStackIngredient();
        ingredient.readInternal(json);

        if (!json.has("amount"))
            throw new JsonSyntaxException("Fluid ingredient has to define an amount");
        ingredient.amount = GsonHelper.getAsInt(json, "amount");
        return ingredient;
    }

    public static class FluidStackIngredient extends FluidIngredient {
        protected Fluid fluid;
        protected CompoundTag tagToMatch;

        public FluidStackIngredient() {
        tagToMatch = new CompoundTag();
        }

         protected void fixFlowing() {
            if(fluid instanceof FlowingFluid flowingFluid) {
                fluid = flowingFluid.getSource();
            }
         }

        @Override
        protected boolean testInternal(FluidStack t) {
            if (!FluidHelper.isSame(t, fluid))
                return false;
            if (tagToMatch.isEmpty())
                return true;
            CompoundTag tag = t.getOrCreateTag();
            return tag.copy()
                    .merge(tagToMatch)
                    .equals(tag);
        }

        @Override
        protected void readInternal(FriendlyByteBuf buffer) {
            fluid = buffer.readRegistryId();
            tagToMatch = buffer.readNbt();
        }

        @Override
        protected void writeInternal(FriendlyByteBuf buffer) {
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
            buffer.writeNbt(tagToMatch);
        }

        @Override
        protected void readInternal(JsonObject json) {
            FluidStack stack = FluidHelper.deserializeFluidStack(json);
            fluid = stack.getFluid();
            tagToMatch = stack.getOrCreateTag();
        }

        @Override
        protected void writeInternal(JsonObject json) {
            json.addProperty("fluid", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).toString());
            json.add("nbt", JsonParser.parseString(tagToMatch.toString()));
        }

        @Override
        protected List<FluidStack> determineMatchingFluidStacks() {
            return ImmutableList.of(tagToMatch.isEmpty() ? new FluidStack(fluid, amount)
                    : new FluidStack(fluid, amount, tagToMatch));
        }
    }

    public static class FluidTagIngredient extends FluidIngredient {
        protected TagKey<Fluid> tag;

        @Override
        protected boolean testInternal(FluidStack t) {
            if (tag != null)
                return FluidHelper.isTag(t, tag);
            for (FluidStack accepted : getMatchingFluidStacks())
                if (FluidHelper.isSame(accepted, t))
                    return true;
            return false;
        }

        @Override
        protected void readInternal(FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            matchingFluidStacks = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                matchingFluidStacks.add(buffer.readFluidStack());
        }

        @Override
        protected void writeInternal(FriendlyByteBuf buffer) {
            // Tag has to be resolved on the server before sending
            List<FluidStack> matchingFluidStacks = getMatchingFluidStacks();
            buffer.writeVarInt(matchingFluidStacks.size());
            matchingFluidStacks.stream()
                    .forEach(buffer::writeFluidStack);
        }

        @Override
        protected void readInternal(JsonObject json) {
            ResourceLocation name = new ResourceLocation(GsonHelper.getAsString(json, "fluidTag"));
            tag = FluidTags.create(name);
        }

        @Override
        protected void writeInternal(JsonObject json) {
            json.addProperty("fluidTag", tag.location()
                    .toString());
        }

        @Override
        protected List<FluidStack> determineMatchingFluidStacks() {
            return ForgeRegistries.FLUIDS.tags()
                    .getTag(tag)
                    .stream()
                    .map(f -> {
                        if (f instanceof FlowingFluid)
                            return ((FlowingFluid) f).getSource();
                        return f;
                    })
                    .distinct()
                    .map(f -> new FluidStack(f, amount))
                    .collect(Collectors.toList());
        }
    }

    public static class FluidHelper {
        public static boolean isSame(FluidStack stack, Fluid fluid){
            if (fluid instanceof FlowingFluid ff) {
                Fluid source = ff.getSource();
                stack.getFluid().isSame(source);
            }
            return stack.getFluid().isSame(fluid);
        }

        public static boolean isSame(FluidStack stack1, FluidStack stack2) {
            return stack1.getFluid() == stack2.getFluid();
        }

        public static boolean isTag(FluidStack stack, TagKey<Fluid> tag) {
            return stack.getFluid().is(tag);
        }

        public static FluidStack deserializeFluidStack(JsonObject json) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
            if (fluid == null)
                throw new JsonSyntaxException("Unknown fluid '" + id + "'");
            if (fluid == Fluids.EMPTY)
                throw new JsonSyntaxException("Invalid empty fluid '" + id + "'");
            int amount = GsonHelper.getAsInt(json, "amount");
            FluidStack stack = new FluidStack(fluid, amount);

            if (!json.has("nbt"))
                return stack;

            try {
                JsonElement element = json.get("nbt");
                stack.setTag(TagParser.parseTag(
                        element.isJsonObject() ?  new GsonBuilder().disableHtmlEscaping().create().toJson(element) : GsonHelper.convertToString(element, "nbt")));

            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }

            return stack;
        }

    }

}
