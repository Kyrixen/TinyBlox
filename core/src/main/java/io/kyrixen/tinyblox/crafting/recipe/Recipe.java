package io.kyrixen.tinyblox.crafting.recipe;

import io.kyrixen.tinyblox.inventory.ItemStack;
import io.kyrixen.tinyblox.utils.TinyIdentifier;

public class Recipe {

    // Recipe vars
    private final TinyIdentifier recipeID;
    private final ItemStack[] ingredients;
    private final ItemStack output;

    // Constructs recipe
    public Recipe(TinyIdentifier recipeID, ItemStack[] ingredients, ItemStack output) {
        this.recipeID = recipeID;
        this.ingredients = ingredients;
        this.output = output;
    }


    // Getters //

    public String getName() { return this.recipeID.getID().replace("_", " ").toUpperCase(); }
    public TinyIdentifier getID() { return this.recipeID; }
    public ItemStack[] getIngredients() { return this.ingredients.clone(); }
    public ItemStack getOutput() { return new ItemStack(output.getItem(), output.getCount()); }

}
