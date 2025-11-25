package com.lance5057.extradelight.capabilities;

import com.lance5057.extradelight.ExtraDelightComponents;
import net.minecraft.nbt.CompoundTag;

import javax.swing.*;

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
