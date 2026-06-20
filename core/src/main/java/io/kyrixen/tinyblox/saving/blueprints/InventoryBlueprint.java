package io.kyrixen.tinyblox.saving.blueprints;

public class InventoryBlueprint {

    public int formatVersion;

    public byte hotbarSlot;
    public InventoryStack[] stacks;


    public static class InventoryStack {
        public String item;
        public int amount;
    }

}
