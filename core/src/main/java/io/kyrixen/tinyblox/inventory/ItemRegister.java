package io.kyrixen.tinyblox.inventory;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.inventory.Equipment.EquipmentType;
import io.kyrixen.tinyblox.inventory.Equipment.ToolTier;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.chunk.tile.TileRegister;

public class ItemRegister {
    
    // Item holder
    private static final List<Item> ITEMS = new ArrayList<>();

    // Helper items holders //

    public final static Item NONE = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "none"), null, false, (byte) 0);
    public final static Item GRASS = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "grass"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "grass"), true, (byte) 24, TileRegister.GRASS.getTileID());
    public final static Item DIRT = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "dirt"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dirt"), true, (byte) 24, TileRegister.DIRT.getTileID());
    public final static Item STONE = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "stone"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone"), true, (byte) 24, TileRegister.STONE.getTileID());
    public final static Item WATER = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "water"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "water"), false, (byte) 24, TileRegister.WATER.getTileID());
    public final static Item COAL = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "coal"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "coal_ore"), false, (byte) 12, TileRegister.COAL.getTileID());
    public final static Item IRON = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "iron"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_ore"), false, (byte) 12, TileRegister.IRON.getTileID());
    public final static Item WOOD = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "wood"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wood"), true, (byte) 24, TileRegister.WOOD.getTileID());
    public final static Item LEAVES = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "leaves"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "leaves"), false, (byte) 24, TileRegister.LEAVES.getTileID());
    public final static Item LADDER = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "ladder"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "ladder"), true, (byte) 36, TileRegister.LADDER.getTileID());
    public final static Item CAGED_LAMP = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "caged_lamp"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "caged_lamp"), true, (byte) 12, TileRegister.CAGED_LAMP.getTileID());
    public final static Item SLIME = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "slime"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime_tile"), false, (byte) 6, TileRegister.SLIME_TILE.getTileID());
    public final static Item SAND = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "sand"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "sand"), true, (byte) 36, TileRegister.SAND.getTileID());
    public final static Item GLASS = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "glass"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "glass"), true, (byte) 36, TileRegister.GLASS.getTileID());
    public final static Item CLAY = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "clay"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "clay"), true, (byte) 12, TileRegister.CLAY.getTileID());
    public final static Item BRICK = new Item(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "brick"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "brick"), true, (byte) 24, TileRegister.BRICK.getTileID());


    public final static Item WOODEN_SWORD = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "wooden_sword"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_sword"), true, ToolTier.WOOD, EquipmentType.WEAPON);
    public final static Item STONE_SWORD = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "stone_sword"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_sword"), true, ToolTier.STONE, EquipmentType.WEAPON);
    public final static Item IRON_SWORD = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "iron_sword"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_sword"), false, ToolTier.IRON, EquipmentType.WEAPON);

    public final static Item WOODEN_PICKAXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "wooden_pickaxe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_pickaxe"), true, ToolTier.WOOD, EquipmentType.PICKAXE);
    public final static Item STONE_PICKAXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "stone_pickaxe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_pickaxe"), true, ToolTier.STONE, EquipmentType.PICKAXE);
    public final static Item IRON_PICKAXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "iron_pickaxe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_pickaxe"), false, ToolTier.IRON, EquipmentType.PICKAXE);

    public final static Item WOODEN_AXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "wooden_axe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_axe"), true, ToolTier.WOOD, EquipmentType.AXE);
    public final static Item STONE_AXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "stone_axe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_axe"), true, ToolTier.STONE, EquipmentType.AXE);
    public final static Item IRON_AXE = new Equipment(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "iron_axe"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_axe"), false, ToolTier.IRON, EquipmentType.AXE);


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

    // Search item by ID
    public static Item getItemByID(TinyIdentifier id) {

        if(id == null) return null;

        for(Item item : ITEMS) {
            if(item.getItemID().equals(id)) return item;
        }

        return null;

    }


    // Setter //

    public static void add(Item item) {
        if(ITEMS.contains(item)) return;
        ITEMS.add(item);
    }

}
