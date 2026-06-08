package io.kyrixen.tinyblox.crafting;

import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.crafting.recipe.RecipeRegister;
import io.kyrixen.tinyblox.crafting.rendering.CraftingButton;
import io.kyrixen.tinyblox.crafting.rendering.CraftingRenderer;
import io.kyrixen.tinyblox.crafting.rendering.RecipeButton;
import io.kyrixen.tinyblox.crafting.rendering.RecipeContainer;
import io.kyrixen.tinyblox.crafting.rendering.CraftingButton.CraftingButtonState;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;

public class Crafting {

    // Visibility tag
    private boolean activated = false;

    // Recipe selected
    private int selected;
    private int scrollOffset = 0;
    
    // Holders inventory
    private final Inventory inventory;

    // UI widgets //

    private final RecipeContainer container;
    private final CraftingButton craftingButton;
    private final RecipeButton[] recipeButtons;

    // Helpers //
    
    private final static TextureID BUTTON_HOVER_TEXTURE = new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_hover");
    private final static TextureID BUTTON_IDLE_TEXTURE = new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_idle");
    private final static TextureID BUTTON_SELECTED_TEXTURE = new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_selected");
        
    private final int scaleMult = 3;


    // Constructs crafting menu
    public Crafting(Inventory inventory) {
        
        this.inventory = inventory;

        int containerW = 96 * scaleMult;
        int containerH = 160 * scaleMult;
        int containerX = 20;
        int containerY = (Constants.WINDOW_HEIGHT - containerH) / 2;

        // Container
        this.container = new RecipeContainer(containerX, containerY, containerW, containerH, new TextureID("tinyblox", TextureType.HUD, "crafting_menu_container"));

        // x: 20 y: 120 w: 96 h: 160

        // Craft button
        this.craftingButton = new CraftingButton(containerX + 18, containerY + 24, 84 * scaleMult, 20 * scaleMult, BUTTON_HOVER_TEXTURE, BUTTON_IDLE_TEXTURE, BUTTON_SELECTED_TEXTURE);
        
        this.recipeButtons = new RecipeButton[3];

        int gap = 2 * scaleMult;
                
        int buttonW = 84 * scaleMult;
        int buttonH = 20 * scaleMult;

        int rowSpacing = buttonH + gap;

        int buttonX = containerX + 18;
        int recipeStartY = containerY + (containerH - 84) - buttonH - gap;

        for(int i = 0; i < recipeButtons.length; i++) {
            if(RecipeRegister.getRecipes().get(i) == null) continue;
            this.recipeButtons[i] = new RecipeButton(RecipeRegister.getRecipes().get(i), buttonX, recipeStartY - rowSpacing * i, buttonW, buttonH, BUTTON_HOVER_TEXTURE, BUTTON_IDLE_TEXTURE, BUTTON_SELECTED_TEXTURE);
        }

        this.updateScroll(0);

    }


    public void update(int mouseX, int mouseY, boolean pressed) {

        for(int i = 0; i < recipeButtons.length; i++) {
        
            RecipeButton button = recipeButtons[i];
            if(button == null) continue;
        
            boolean hover = button.contains(mouseX, mouseY);

            if(hover && pressed) selected = i;

            if(i == selected) button.setState(CraftingButtonState.SELECTED);
            else if(hover) button.setState(CraftingButtonState.HOVER);
            else button.setState(CraftingButtonState.IDLE);
        
        }

        boolean hover = craftingButton.contains(mouseX, mouseY);

        if(hover &&  pressed) craftingButton.setState(CraftingButtonState.SELECTED);
        else if(hover) craftingButton.setState(CraftingButtonState.HOVER);
        else craftingButton.setState(CraftingButtonState.IDLE);


        if(craftingButton.getState() == CraftingButtonState.SELECTED) { if(canCraft()) craft(); }


        
    }

    public void updateScroll(int scrollDir) {

        if(scrollDir < 0) scrollOffset--;
        else if(scrollDir > 0) scrollOffset++;

        int maxRecipesCount = Math.max(0, RecipeRegister.getRecipes().size() - recipeButtons.length);
        scrollOffset = MathUtils.clamp(scrollOffset, 0, maxRecipesCount);

        for(int i = 0; i < recipeButtons.length; i++) { recipeButtons[i].setRecipe(RecipeRegister.getRecipes().get(scrollOffset + i)); }

    }

    public void render(CraftingRenderer craftingRenderer, RendererStack rendererStack) {

        if(!activated) return;

        // Container
        craftingRenderer.renderRecipeContainer(container, rendererStack);

        // Recipe buttons
        for(RecipeButton button : recipeButtons) {
            if(button == null) continue;
            craftingRenderer.renderRecipeButton(scaleMult, button, rendererStack);
        }

        // Craft button
        craftingRenderer.renderCraftingButton(craftingButton, rendererStack);
        rendererStack.font.getData().setScale(1.5f);
        rendererStack.font.draw(rendererStack.batch, "CRAFT", craftingButton.getX() + craftingButton.getWidth() / 4.2f, craftingButton.getY() + craftingButton.getHeight() / 1.36f);

    }


    // Helpers //

    private boolean canCraft() {
        
        if(recipeButtons[selected] == null) return false;

        if(inventory.getAvailableStack(recipeButtons[selected].getRecipe().getOutput().getItem()) == null) return false;

        for(ItemStack ingredientStack : recipeButtons[selected].getRecipe().getIngredients()) {
            int itemCount = inventory.getTotalItemCount(ingredientStack.getItem());
            if(itemCount < ingredientStack.getCount()) return false;
        }

        return true;

    }

    private void craft() {

        for(ItemStack ingredientStack : recipeButtons[selected].getRecipe().getIngredients()) {
            ItemStack itemStack = inventory.getStack(ingredientStack.getItem());
            itemStack.remove(ingredientStack.getCount());            
        }

        inventory.add(recipeButtons[selected].getRecipe().getOutput().getItem(), recipeButtons[selected].getRecipe().getOutput().getCount());

    }


    // Setters //
    
    public void toggle() { this.activated = !activated; }

}