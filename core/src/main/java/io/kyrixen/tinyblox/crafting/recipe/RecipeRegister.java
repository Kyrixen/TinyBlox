package io.kyrixen.tinyblox.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.saving.RecipeLoader;
import io.kyrixen.tinyblox.utils.Logger;

public class RecipeRegister {
    
    // Recipe holder
    private static final List<Recipe> RECIPES = new ArrayList<>();

    // Inits internal recipes
    public static void initRecipes() {

        if(!RECIPES.isEmpty()) return;
        
        RecipeLoader.loadAll();
        Logger.LOGGER.debug("REGISTER", "Loaded recipes count: " + RECIPES.size());

    }

    // Getter
    public static List<Recipe> getRecipes() { return RECIPES; }

    // Setter
    public static void add(Recipe recipe) { RECIPES.add(recipe); }

}
