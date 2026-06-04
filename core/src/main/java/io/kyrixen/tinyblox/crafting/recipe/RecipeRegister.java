package io.kyrixen.tinyblox.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;

public class RecipeRegister {
    
    // Recipe holder
    private static final List<Recipe> RECIPES = new ArrayList<>();

    // Inits internal recipes
    public static void initRecipes() {

        if(!RECIPES.isEmpty()) return;

        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(Item.WOOD, (byte) 2) }, new ItemStack(Item.WOODEN_PICKAXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(Item.WOOD, (byte) 1), new ItemStack(Item.STONE, (byte) 1) }, new ItemStack(Item.STONE_PICKAXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(Item.WOOD, (byte) 1), new ItemStack(Item.IRON, (byte) 4) }, new ItemStack(Item.CAGED_LAMP, (byte) 4)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(Item.WOOD, (byte) 4) }, new ItemStack(Item.LADDER, (byte) 12)));

    }

    // Getter
    public static List<Recipe> getRecipes() { return RECIPES; }

}
