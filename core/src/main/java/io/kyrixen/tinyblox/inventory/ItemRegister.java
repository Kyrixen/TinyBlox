package io.kyrixen.tinyblox.inventory;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.inventory.Equipment.EquipmentType;
import io.kyrixen.tinyblox.inventory.Equipment.ToolTier;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ItemRegister {
    
    // Item holder
    private static final List<Item> ITEMS = new ArrayList<>();

    // Helper items holders //

    public final static Item NONE = new Item("none", MiscUtils.generateItemID(), null, false, (byte) 0);
    public final static Item GRASS = new Item("grass", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "grass"), true, (byte) 24, TileType.GRASS);
    public final static Item DIRT = new Item("dirt", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dirt"), true, (byte) 24, TileType.DIRT);
    public final static Item STONE = new Item("stone", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone"), true, (byte) 24, TileType.STONE);
    public final static Item WATER = new Item("water", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "water"), false, (byte) 24, TileType.WATER);
    public final static Item COAL = new Item("coal", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "coal_ore"), false, (byte) 12, TileType.COAL);
    public final static Item IRON = new Item("iron", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_ore"), false, (byte) 12, TileType.IRON);
    public final static Item WOOD = new Item("wood", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wood"), true, (byte) 24, TileType.WOOD);
    public final static Item LEAVES = new Item("leaves", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "leaves"), false, (byte) 24, TileType.LEAVES);
    public final static Item LADDER = new Item("ladder", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "ladder"), true, (byte) 36, TileType.LADDER);
    public final static Item CAGED_LAMP = new Item("caged_lamp", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "caged_lamp"), true, (byte) 12, TileType.CAGED_LAMP);
    public final static Item SLIME = new Item("slime", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime_tile"), false, (byte) 6, TileType.SLIME_TILE);
    public final static Item SAND = new Item("sand", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "sand"), true, (byte) 36, TileType.SAND);
    public final static Item GLASS = new Item("glass", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "glass"), true, (byte) 36, TileType.GLASS);
    public final static Item CLAY = new Item("clay", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "clay"), true, (byte) 12, TileType.CLAY);
    public final static Item BRICK = new Item("brick", MiscUtils.generateItemID(), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "brick"), true, (byte) 24, TileType.BRICK);


    public final static Item WOODEN_SWORD = new Equipment("wooden_sword", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_sword"), true, ToolTier.WOOD, EquipmentType.WEAPON);
    public final static Item STONE_SWORD = new Equipment("stone_sword", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_sword"), true, ToolTier.STONE, EquipmentType.WEAPON);
    public final static Item IRON_SWORD = new Equipment("iron_sword", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_sword"), false, ToolTier.IRON, EquipmentType.WEAPON);

    public final static Item WOODEN_PICKAXE = new Equipment("wooden_pickaxe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_pickaxe"), true, ToolTier.WOOD, EquipmentType.PICKAXE);
    public final static Item STONE_PICKAXE = new Equipment("stone_pickaxe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_pickaxe"), true, ToolTier.STONE, EquipmentType.PICKAXE);
    public final static Item IRON_PICKAXE = new Equipment("iron_pickaxe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_pickaxe"), false, ToolTier.IRON, EquipmentType.PICKAXE);

    public final static Item WOODEN_AXE = new Equipment("wooden_axe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_axe"), true, ToolTier.WOOD, EquipmentType.AXE);
    public final static Item STONE_AXE = new Equipment("stone_axe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_axe"), true, ToolTier.STONE, EquipmentType.AXE);
    public final static Item IRON_AXE = new Equipment("iron_axe", new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_axe"), false, ToolTier.IRON, EquipmentType.AXE);


    // Inits internal items
    public static void initItems() {

        if(!ITEMS.isEmpty()) return;

        ITEMS.add(NONE);
        ITEMS.add(GRASS);
        ITEMS.add(DIRT);
        ITEMS.add(WATER);
        ITEMS.add(STONE);
        ITEMS.add(COAL);
        ITEMS.add(IRON);
        ITEMS.add(LADDER);
        ITEMS.add(WOOD);        
        ITEMS.add(LEAVES);
        ITEMS.add(CAGED_LAMP);
        ITEMS.add(SAND);
        ITEMS.add(GLASS);
        ITEMS.add(CLAY);
        ITEMS.add(BRICK);


        ITEMS.add(WOODEN_SWORD);
        ITEMS.add(STONE_SWORD);
        ITEMS.add(IRON_SWORD);

        ITEMS.add(WOODEN_PICKAXE);
        ITEMS.add(STONE_PICKAXE);
        ITEMS.add(IRON_PICKAXE);

        ITEMS.add(WOODEN_AXE);
        ITEMS.add(STONE_AXE);
        ITEMS.add(IRON_AXE);

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
