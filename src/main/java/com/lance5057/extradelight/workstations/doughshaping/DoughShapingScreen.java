package com.lance5057.extradelight.workstations.doughshaping;

import com.lance5057.extradelight.workstations.doughshaping.recipes.DoughShapingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DoughShapingScreen extends AbstractContainerScreen<DoughShapingMenu> {
	 private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
	    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
	    private static final ResourceLocation RECIPE_SELECTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_selected");
	    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted");
	    private static final ResourceLocation RECIPE_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe");
	    private static final ResourceLocation BG_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/stonecutter.png");
	    private static final int SCROLLER_WIDTH = 12;
	    private static final int SCROLLER_HEIGHT = 15;
	    private static final int RECIPES_COLUMNS = 4;
	    private static final int RECIPES_ROWS = 3;
	    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
	    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
	    private static final int SCROLLER_FULL_HEIGHT = 54;
	    private static final int RECIPES_X = 52;
	    private static final int RECIPES_Y = 14;
	    private float scrollOffs;
	    private boolean scrolling;
	    private int startIndex;
	    private boolean displayRecipes;

	    public DoughShapingScreen(DoughShapingMenu p_99310_, Inventory p_99311_, Component p_99312_) {
	        super(p_99310_, p_99311_, p_99312_);
	        p_99310_.registerUpdateListener(this::containerChanged);
	        --this.titleLabelY;
	    }

	    @Override
	    public void render(GuiGraphics p_281735_, int p_282517_, int p_282840_, float p_282389_) {
	        super.render(p_281735_, p_282517_, p_282840_, p_282389_);
	        this.renderTooltip(p_281735_, p_282517_, p_282840_);
	    }

	    @Override
	    protected void renderBg(GuiGraphics graphics, float mouseX, int mouseY, int tick) {
//	        int i = this.leftPos;
//	        int j = this.topPos;
//	        graphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
//	        int k = (int)(41.0F * this.scrollOffs);
//	        ResourceLocation resourcelocation = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
//	        graphics.blit(resourcelocation, i + 119, j + 15 + k, 12, 15,mouseY, tick);
//	        int l = this.leftPos + 52;
//	        int i1 = this.topPos + 14;
//	        int j1 = this.startIndex + 12;
//	        this.renderButtons(graphics, mouseY, tick, l, i1, j1);
//	        this.renderRecipes(graphics, l, i1, j1);

			int left = this.leftPos;
			int top = this.topPos;
			graphics.blit(BG_LOCATION, left, top, 0, 0, this.imageWidth, this.imageHeight);

			int scroll =(int)(this.scrollOffs*41f);
			int scrollBarU =this.isScrollBarActive()?176:188;
			int scrollBarV =0;

			graphics.blit(BG_LOCATION,left+119,top+15+scroll,scrollBarU,scrollBarV,12,15);
			this.renderButtons(graphics,mouseY,tick,left+52,top+14,startIndex+12);
			this.renderRecipes(graphics,left+52,top+14,startIndex+12);


	    }

	    @Override
	    protected void renderTooltip(GuiGraphics p_282396_, int p_283157_, int p_282258_) {
	        super.renderTooltip(p_282396_, p_283157_, p_282258_);
	        if (this.displayRecipes) {
	            int i = this.leftPos + 52;
	            int j = this.topPos + 14;
	            int k = this.startIndex + 12;
	            List<DoughShapingRecipe> list = this.menu.getRecipes();

	            for(int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
	                int i1 = l - this.startIndex;
	                int j1 = i + i1 % 4 * 16;
	                int k1 = j + i1 / 4 * 18 + 2;
	                if (p_283157_ >= j1 && p_283157_ < j1 + 16 && p_282258_ >= k1 && p_282258_ < k1 + 18) {
	                    p_282396_.renderTooltip(this.font, list.get(l).getResultItem(this.minecraft.level.registryAccess()), p_283157_, p_282258_);
	                }
	            }
	        }
	    }

	    private void renderButtons(GuiGraphics graphics, int mouseY, int tick, int startX, int startY, int p_282688_) {
	        for(int i = this.startIndex; i < p_282688_ && i < this.menu.getNumRecipes(); ++i) {
	            int j = i - this.startIndex;
	            int k = startX + j % 4 * 16;
	            int l = j / 4;
	            int i1 = startY + l * 18 + 2;
	            ResourceLocation resourcelocation;
	            if (i == this.menu.getSelectedRecipeIndex()) {
	                resourcelocation = RECIPE_SELECTED_SPRITE;
					graphics.blit(BG_LOCATION, k, i1 - 1, 0, 184,15,17);
	            } else if (mouseY >= k && tick >= i1 && mouseY < k + 16 && tick < i1 + 18) {
	                resourcelocation = RECIPE_HIGHLIGHTED_SPRITE;
					graphics.blit(BG_LOCATION, k, i1 - 1, 0, 202,15,17);
	            } else {
	                resourcelocation = RECIPE_SPRITE;
					graphics.blit(BG_LOCATION, k, i1 - 1, 0, 166,15,17);
	            }

	            //graphics.blit(resourcelocation, k, i1 - 1, 16, 18,15,17);
	        }
	    }

	    private void renderRecipes(GuiGraphics graphics, int p_282658_, int p_282563_, int p_283352_) {
	        List<DoughShapingRecipe> list = this.menu.getRecipes();

	        for(int i = this.startIndex; i < p_283352_ && i < this.menu.getNumRecipes(); ++i) {
	            int j = i - this.startIndex;
	            int k = p_282658_ + j % 4 * 16;
	            int l = j / 4;
	            int i1 = p_282563_ + l * 18 + 2;
	            graphics.renderItem(list.get(i).getResultItem(this.minecraft.level.registryAccess()), k, i1);
	        }
	    }

	    @Override
	    public boolean mouseClicked(double p_99318_, double p_99319_, int p_99320_) {
	        this.scrolling = false;
	        if (this.displayRecipes) {
	            int i = this.leftPos + 52;
	            int j = this.topPos + 14;
	            int k = this.startIndex + 12;

	            for(int l = this.startIndex; l < k; ++l) {
	                int i1 = l - this.startIndex;
	                double d0 = p_99318_ - (double)(i + i1 % 4 * 16);
	                double d1 = p_99319_ - (double)(j + i1 / 4 * 18);
	                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 16.0 && d1 < 18.0 && this.menu.clickMenuButton(this.minecraft.player, l)) {
	                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.MAGMA_CUBE_SQUISH, 1.0F));
	                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
	                    return true;
	                }
	            }

	            i = this.leftPos + 119;
	            j = this.topPos + 9;
	            if (p_99318_ >= (double)i && p_99318_ < (double)(i + 12) && p_99319_ >= (double)j && p_99319_ < (double)(j + 54)) {
	                this.scrolling = true;
	            }
	        }

	        return super.mouseClicked(p_99318_, p_99319_, p_99320_);
	    }

	    @Override
	    public boolean mouseDragged(double p_99322_, double p_99323_, int p_99324_, double p_99325_, double p_99326_) {
	        if (this.scrolling && this.isScrollBarActive()) {
	            int i = this.topPos + 14;
	            int j = i + 54;
	            this.scrollOffs = ((float)p_99323_ - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
	            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
	            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5) * 4;
	            return true;
	        } else {
	            return super.mouseDragged(p_99322_, p_99323_, p_99324_, p_99325_, p_99326_);
	        }
	    }

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (this.isScrollBarActive()) {
			int i = this.getOffscreenRows();
			float f = (float)pDelta / (float)i;
			this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
		}

		return true;
	}

//	@Override
//	    public boolean mouseScrolled(double p_99314_, double p_99315_, double p_99316_, double p_295672_) {
//	        if (this.isScrollBarActive()) {
//	            int i = this.getOffscreenRows();
//	            float f = (float)p_295672_ / (float)i;
//	            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
//	            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
//	        }
//
//	        return true;
//	    }

	    private boolean isScrollBarActive() {
	        return this.displayRecipes && this.menu.getNumRecipes() > 12;
	    }

	    protected int getOffscreenRows() {
	        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
	    }

	    private void containerChanged() {
	        this.displayRecipes = this.menu.hasInputItem();
	        if (!this.displayRecipes) {
	            this.scrollOffs = 0.0F;
	            this.startIndex = 0;
	        }
	    }
}