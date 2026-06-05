package io.kyrixen.tinyblox.crafting.recipe;

import io.kyrixen.tinyblox.entities.inventory.ItemStack;

public class Recipe {

    // Recipe vars
    private final ItemStack[] ingredients;
    private final ItemStack output;

    // Constructs recipe
    public Recipe(ItemStack[] ingredients, ItemStack output) {
        this.ingredients = ingredients;
        this.output = output;
    }


    // Getters //

    public String getName() { return this.output.getItem().getItemName().toUpperCase().replace("_", " "); }
    public ItemStack[] getIngredients() { return this.ingredients.clone(); }
    public ItemStack getOutput() { return new ItemStack(output.getItem(), output.getCount()); }

}
