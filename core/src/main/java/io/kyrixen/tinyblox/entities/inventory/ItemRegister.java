package io.kyrixen.tinyblox.entities.inventory;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ItemRegister {
    
    // Item holder
    private static final List<Item> ITEMS = new ArrayList<>();

    // Helper items holders //

    public final static Item NONE = new Item("none", Utils.generateItemID(), null, false, (byte) 0);
    public final static Item GRASS = new Item("grass", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "grass"), true, (byte) 24, TileType.GRASS);
    public final static Item DIRT = new Item("dirt", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "dirt"), true, (byte) 24, TileType.DIRT);
    public final static Item STONE = new Item("stone", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "stone"), true, (byte) 24, TileType.STONE);
    public final static Item WATER = new Item("water", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "water"), false, (byte) 24, TileType.WATER);
    public final static Item IRON = new Item("iron", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "iron_ore"), false, (byte) 12, TileType.IRON);
    public final static Item WOOD = new Item("wood", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "wood"), true, (byte) 24, TileType.WOOD);
    public final static Item LEAVES = new Item("leaves", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "leaves"), false, (byte) 24, TileType.LEAVES);
    public final static Item LADDER = new Item("ladder", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "ladder"), true, (byte) 36, TileType.LADDER);
    public final static Item CAGED_LAMP = new Item("caged_lamp", Utils.generateItemID(), new TextureID("tinyblox", TextureType.TERRAIN, "caged_lamp"), true, (byte) 12, TileType.CAGED_LAMP);

    public final static Item WOODEN_PICKAXE = new Equipment("wooden_pickaxe", Utils.generateItemID(), new TextureID("tinyblox", TextureType.HUD, "wooden_pickaxe"), true, 1.25f);
    public final static Item STONE_PICKAXE = new Equipment("stone_pickaxe", Utils.generateItemID(), new TextureID("tinyblox", TextureType.HUD, "stone_pickaxe"), true, 2f);
    

    // Inits internal items
    public static void initItems() {

        if(!ITEMS.isEmpty()) return;

        ITEMS.add(NONE);
        ITEMS.add(GRASS);
        ITEMS.add(DIRT);
        ITEMS.add(WATER);
        ITEMS.add(STONE);
        ITEMS.add(IRON);
        ITEMS.add(LADDER);
        ITEMS.add(WOOD);        
        ITEMS.add(LEAVES);
        ITEMS.add(CAGED_LAMP);

        ITEMS.add(WOODEN_PICKAXE);
        ITEMS.add(STONE_PICKAXE);

    }

    // Getters //

    // Get items list
    public static List<Item> getItemList() { return ITEMS; }

    // Search item by name
    public static Item getItemByName(String name) {

        for(Item item : ITEMS) {
            if(item.getItemName().toLowerCase().equals(name.toLowerCase())) return item;
        }

        return null;

    }

    // Search item by ID
    public static Item getItemByID(int id) {

        for(Item item : ITEMS) {
            if(item.getItemID() == id) return item;
        }

        return null;

    }

}
