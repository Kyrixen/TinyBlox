package io.kyrixen.tinyblox.saving.blueprints;

public class RecipeBlueprint {

    public int formatVersion;

    public RecipeStack[] ingredients;
    public RecipeStack result;


    public static class RecipeStack {
        public String item;
        public int amount;
    }

}
