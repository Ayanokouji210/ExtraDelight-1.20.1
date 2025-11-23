package com.lance5057.extradelight.integration.jei.interpreters;

import com.lance5057.extradelight.ExtraDelightComponents;
import com.lance5057.extradelight.items.dynamicfood.api.DynamicItemComponent;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DynamicItemInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
	public static final DynamicItemInterpreter INSTANCE = new DynamicItemInterpreter();
    String NONE = "sweet_berries";
//	@Override
//	public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
//		DynamicItemComponent d = ingredient.get(ExtraDelightComponents.DYNAMIC_FOOD);
//		if (d == null)
//			return "sweet_berries";
//		return d.graphics().get(0);
//	}

    @Override
    public String apply(ItemStack stack, UidContext uidContext) {
        ExtraDelightComponents.IDynamicFood d = stack.getCapability(ExtraDelightComponents.DYNAMIC_FOOD).resolve().get();
        List<String> list = ExtraDelightComponents.IDynamicFood.read(stack);
        if(list==null || list.isEmpty()) {
            return "sweet_berries";
        }
        return list.get(0);
    }

}
