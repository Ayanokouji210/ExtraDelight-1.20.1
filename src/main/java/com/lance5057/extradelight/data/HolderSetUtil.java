package com.lance5057.extradelight.data;

import net.minecraft.core.HolderSet;

import java.util.List;

public class HolderSetUtil {
    @SuppressWarnings("unchecked")
    public static <T> HolderSet<T> empty() {
        try {
            // 尝试通过反射访问 EMPTY 字段
            java.lang.reflect.Field emptyField = HolderSet.Direct.class.getDeclaredField("EMPTY");
            emptyField.setAccessible(true);
            return (HolderSet<T>) emptyField.get(null);
        } catch (Exception e) {
            // 如果 EMPTY 字段不存在，创建一个新的空实例
            return HolderSet.direct();
        }
    }
}