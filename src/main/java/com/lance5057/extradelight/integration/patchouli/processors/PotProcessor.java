package com.lance5057.extradelight.integration.patchouli.processors;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.RecipeHolder;
import com.lance5057.extradelight.integration.patchouli.processors.RecipeHolder;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public class PotProcessor implements IComponentProcessor {
	protected Recipe<?> recipe;

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			return null;
		}
		return switch (key) {
		case "recipe" -> IVariable.wrap(recipe.getId().toString());
		case "output" -> IVariable.from(recipe.getResultItem(level.registryAccess()));
		case "container" ->
			IVariable.from(((CookingPotRecipe) recipe).getOutputContainer());
		case "heading" ->
			IVariable.from(recipe.getResultItem(level.registryAccess()).getHoverName());
		default -> null;
		};
	}

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = ResourceLocation.parse(variables.get("recipe").toString());
		RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
		recipe = manager.byKey(id).orElse(null);
	}
}
