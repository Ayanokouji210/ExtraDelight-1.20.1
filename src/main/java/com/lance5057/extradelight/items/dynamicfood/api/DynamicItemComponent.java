package com.lance5057.extradelight.items.dynamicfood.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lance5057.extradelight.ExtraDelightComponents;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class DynamicItemComponent implements ExtraDelightComponents.IDynamicFood  {
    private List<String> graphics;

    public DynamicItemComponent(List<String> graphics) {
        this.graphics = graphics != null ? new ArrayList<>(graphics) : new ArrayList<>();
        if(this.graphics.isEmpty()){
            this.graphics.add("orange");
        }
    }

    public List<String> graphics() {
        return Collections.unmodifiableList(graphics);
    }

    public void setGraphics(List<String> graphics) {
        this.graphics = graphics != null ? graphics : new ArrayList<>();
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag graphicsList = new ListTag();
        for (String graphic : graphics) {
            graphicsList.add(StringTag.valueOf(graphic));
        }
        tag.put("graphics", graphicsList);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        graphics.clear();
        ListTag graphicsList = tag.getList("graphics", Tag.TAG_STRING);
        for (int i = 0; i < graphicsList.size(); i++) {
            graphics.add(graphicsList.getString(i));
        }
    }

    public static DynamicItemComponent fromNBT(CompoundTag tag) {
        List<String> graphics = new ArrayList<>();
        if (tag.contains("graphics")) {
            ListTag graphicsList = tag.getList("graphics", Tag.TAG_STRING);
            for (int i = 0; i < graphicsList.size(); i++) {
                graphics.add(graphicsList.getString(i));
            }
        }
        return new DynamicItemComponent(graphics);
    }
}
