package com.lance5057.extradelight.capabilities;

import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.items.components.ChillComponent;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Chill implements ExtraDelightComponents.IChillComponent {
    private Integer time;

    public Chill(int time) {
        this.time = time;
    }

    @Override
    public int getChill() {
        return this.time;
    }

//    @Override
//    public void setChill(ChillComponent chill) {
//        this.component = chill;
//    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt =new CompoundTag();
        nbt.putInt("chill",this.time);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        if (compoundTag.contains("chill")) {
            this.time = compoundTag.getInt("chill");
        }

    }

}
