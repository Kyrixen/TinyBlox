package io.kyrixen.tinyblox.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;

public class RecipeRegister {
    
    // Recipe holder
    private static final List<Recipe> RECIPES = new ArrayList<>();

    // Inits internal recipes
    public static void initRecipes() {

        if(!RECIPES.isEmpty()) return;

        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 2) }, new ItemStack(ItemRegister.WOODEN_SWORD, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.STONE, (byte) 1) }, new ItemStack(ItemRegister.STONE_SWORD, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.IRON, (byte) 1) }, new ItemStack(ItemRegister.IRON_SWORD, (byte) 1)));

        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 3) }, new ItemStack(ItemRegister.WOODEN_PICKAXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.STONE, (byte) 2) }, new ItemStack(ItemRegister.STONE_PICKAXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.IRON, (byte) 2) }, new ItemStack(ItemRegister.IRON_PICKAXE, (byte) 1)));

        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 4) }, new ItemStack(ItemRegister.WOODEN_AXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.STONE, (byte) 3) }, new ItemStack(ItemRegister.STONE_AXE, (byte) 1)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 1), new ItemStack(ItemRegister.IRON, (byte) 3) }, new ItemStack(ItemRegister.IRON_AXE, (byte) 1)));
        

        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.WOOD, (byte) 4) }, new ItemStack(ItemRegister.LADDER, (byte) 12)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.COAL, (byte) 1), new ItemStack(ItemRegister.IRON, (byte) 4) }, new ItemStack(ItemRegister.CAGED_LAMP, (byte) 4)));
        RECIPES.add(new Recipe(new ItemStack[] { new ItemStack(ItemRegister.SAND, (byte) 1) }, new ItemStack(ItemRegister.GLASS, (byte) 2)));

    }

    // Getter
    public static List<Recipe> getRecipes() { return RECIPES; }

}
