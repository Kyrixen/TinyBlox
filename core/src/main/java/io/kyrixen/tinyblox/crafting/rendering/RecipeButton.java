package io.kyrixen.tinyblox.crafting.rendering;

import io.kyrixen.tinyblox.crafting.recipe.Recipe;
import io.kyrixen.tinyblox.graphics.texture.TextureID;

public class RecipeButton extends CraftingButton {

    // Recipe var
    private Recipe recipe;

    // Constructs button
    public RecipeButton(Recipe recipe, int x, int y, int w, int h, TextureID hover, TextureID idle, TextureID selected) {

        super(x, y, w, h, hover, idle, selected);

        this.recipe = recipe;

    }

    
    // Getter
    public Recipe getRecipe() { return this.recipe; }

    // Setter
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

}
