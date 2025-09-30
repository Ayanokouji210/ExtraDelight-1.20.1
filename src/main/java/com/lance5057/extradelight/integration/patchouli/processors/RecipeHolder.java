package com.lance5057.extradelight.integration.patchouli.processors;

//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public record RecipeHolder<T extends Recipe<?>>(ResourceLocation id, T value) {

    public RecipeHolder(ResourceLocation id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof RecipeHolder) {
                RecipeHolder<?> recipeholder = (RecipeHolder)other;
                if (this.id.equals(recipeholder.id)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public ResourceLocation id() {
        return this.id;
    }

    public T value() {
        return this.value;
    }

}
