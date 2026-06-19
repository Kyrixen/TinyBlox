package io.kyrixen.tinyblox.saving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.crafting.recipe.Recipe;
import io.kyrixen.tinyblox.crafting.recipe.RecipeRegister;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.saving.blueprints.RecipeBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.RecipeBlueprint.RecipeStack;
import io.kyrixen.tinyblox.utils.Logger;


public class RecipeLoader {

    private static final Json json = new Json();
    private static final FileHandle recipeFolder = Gdx.files.internal("recipes");


    public static Recipe load(String path) {
    
        FileHandle recipeFile = Gdx.files.internal(path);

        RecipeBlueprint rp = json.fromJson(RecipeBlueprint.class, recipeFile);    
        if(rp.formatVersion != Constants.BLUEPRINT_FORMAT_VERSION) throw new RuntimeException("Unsupported format version: " + rp.formatVersion);

        if(rp.ingredients == null) throw new RuntimeException("Recipe has no ingredients: " + recipeFile.name());
        if(rp.result == null) throw new RuntimeException("Recipe has no result: " + recipeFile.name());

        Logger.LOGGER.debug("LOADER", "Loaded recipe: " + recipeFile.name());

        ItemStack[] ingredients = new ItemStack[rp.ingredients.length];
        ItemStack output;

        for(int i = 0; i < rp.ingredients.length; i++) {

            RecipeStack ingredient = rp.ingredients[i];

            Item item = ItemRegister.getItemByName(ingredient.item);
            if(item == null) throw new RuntimeException("Unknown item: " + ingredient.item);
            
            byte count = (byte) ingredient.amount;

            ingredients[i] = new ItemStack(item, count);

        }


        RecipeStack result = rp.result;

        Item item = ItemRegister.getItemByName(result.item);
        if(item == null) throw new RuntimeException("Unknown item: " + result.item);

        byte count = (byte) result.amount;

        output = new ItemStack(item, count);


        return new Recipe(ingredients, output);

    }

    public static void loadAll() {

        FileHandle[] files = recipeFolder.list(".json");

        for(FileHandle recipeFile : files) {
            RecipeRegister.add(load(recipeFile.path()));
        }

    }

}
