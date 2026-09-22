package io.kyrixen.tinyblox.saving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.crafting.recipe.Recipe;
import io.kyrixen.tinyblox.crafting.recipe.RecipeRegister;
import io.kyrixen.tinyblox.inventory.Item;
import io.kyrixen.tinyblox.inventory.ItemRegister;
import io.kyrixen.tinyblox.inventory.ItemStack;
import io.kyrixen.tinyblox.saving.blueprints.RecipeBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.RecipeBlueprint.RecipeStack;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;


public class RecipeLoader {

    private static final Json json = new Json();
    private static final FileHandle assetsManifest = Gdx.files.internal("assets.txt");


    public static Recipe load(String path) {
    
        FileHandle recipeFile = Gdx.files.internal(path);

        RecipeBlueprint rp = json.fromJson(RecipeBlueprint.class, recipeFile);    
        if(rp.formatVersion != Constants.BLUEPRINT_FORMAT_VERSION) throw new RuntimeException("Unsupported format version: " + rp.formatVersion);

        if(rp.ingredients == null) throw new RuntimeException("Recipe has no ingredients: " + recipeFile.name());
        if(rp.result == null) throw new RuntimeException("Recipe has no result: " + recipeFile.name());

        Logger.LOGGER.debug("LOADER", "Loaded recipe: " + recipeFile.name());

        
        String[] pathParts = path.split("/");
        if(pathParts.length < 3 || !pathParts[pathParts.length - 2].equals("recipes")) Logger.LOGGER.error("LOADER", "Invalid recipe: " + path);
        String namespace = pathParts[pathParts.length - 3];
        String recipeName = recipeFile.nameWithoutExtension();


        ItemStack[] ingredients = new ItemStack[rp.ingredients.length];
        ItemStack output;

        for(int i = 0; i < rp.ingredients.length; i++) {

            RecipeStack ingredient = rp.ingredients[i];


TinyIdentifier ingredientID = TinyIdentifier.fromString(ingredient.item);

System.out.println("NAMESPACE: "
    + ItemRegister.CLAY.getItemID().getNamespace()
    + " / "
    + ingredientID.getNamespace());

System.out.println("TYPE: "
    + ItemRegister.CLAY.getItemID().getType()
    + " / "
    + ingredientID.getType());

System.out.println("ID: "
    + ItemRegister.CLAY.getItemID().getID()
    + " / "
    + ingredientID.getID());

System.out.println("NAMESPACE EQ: "
    + ItemRegister.CLAY.getItemID().getNamespace()
        .equals(ingredientID.getNamespace()));

System.out.println("TYPE EQ: "
    + ItemRegister.CLAY.getItemID().getType()
        .equals(ingredientID.getType()));

System.out.println("ID EQ: "
    + ItemRegister.CLAY.getItemID().getID()
        .equals(ingredientID.getID()));

Item item = ItemRegister.getItemByID(ingredientID);
if(item == null) throw new RuntimeException("Unknown item: " + ingredient.item);

            byte count = (byte) ingredient.amount;

            ingredients[i] = new ItemStack(item, count);

        }


        RecipeStack result = rp.result;

        Item item = ItemRegister.getItemByID(TinyIdentifier.fromString(result.item));
        if(item == null) throw new RuntimeException("Unknown item: " + result.item);

        byte count = (byte) result.amount;

        output = new ItemStack(item, count);


        TinyIdentifier identifier = new TinyIdentifier(namespace, IdentifierType.RECIPE, recipeName);
        System.out.println(identifier.toString());

        return new Recipe(identifier, ingredients, output);

    }

    public static void loadAll() {

        String[] entries = assetsManifest.readString().split("\n");
        for(String entry : entries) {
            entry = entry.trim();
            if(entry.contains("/recipes/") && entry.endsWith(".json")) RecipeRegister.add(load(entry));
        }

    }

}
