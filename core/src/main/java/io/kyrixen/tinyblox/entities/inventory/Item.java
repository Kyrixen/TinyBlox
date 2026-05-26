package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.chunk.Tile.TileType;

// Item object used for inventory
public enum Item {
    
    NONE(0, 1f, false, false, null, 0),
    GRASS(24, 0.5f, true, true, new TextureID("tinyblox", TextureType.TERRAIN, "grass"), 1),
    DIRT(24, 0.5f, true, true, new TextureID("tinyblox", TextureType.TERRAIN, "dirt"), 2),
    WATER(24, 0.5f, false, true, new TextureID("tinyblox", TextureType.TERRAIN, "water"), 3),
    STONE(24, 0.5f, true, true, new TextureID("tinyblox", TextureType.TERRAIN, "stone"), 4),
    IRON(12, 0.25f, false, true, new TextureID("tinyblox", TextureType.TERRAIN, "iron_ore"), 5),
    WOOD(24, 0.5f, true, true, new TextureID("tinyblox", TextureType.TERRAIN, "wood"), 6),
    LEAVES(24, 0.5f, true,  true, new TextureID("tinyblox", TextureType.TERRAIN, "leaves"), 7),
    LADDER(36, 0.75f, true, true, new TextureID("tinyblox", TextureType.TERRAIN, "ladder"), 8),
    WOODEN_PICKAXE(1, 1.25f, true, false, new TextureID("tinyblox", TextureType.HUD, "wooden_pickaxe"), 9),
    STONE_PICKAXE(1, 2.0f, true, false, new TextureID("tinyblox", TextureType.HUD, "stone_pickaxe"), 10);

    // Max stackable size
    private final byte maxSize;
    private final TextureID textureID;
    private final int itemID;
    private final float miningSpeed;
    private final boolean placeable;
    private final boolean obtainable;

    // Constructor
    Item(int maxSize, float miningSpeed,boolean obtainable, boolean placeable, TextureID textureID, int itemID) {
        this.maxSize = (byte) maxSize;
        this.miningSpeed = miningSpeed;
        this.obtainable = obtainable;
        this.placeable = placeable;
        this.textureID = textureID;
        this.itemID = itemID;
    }

    // Getter
    public byte getMaxSize() { return this.maxSize; }

        public TileType toTileType() {

        switch (this) {
            
            case GRASS:        
                return TileType.GRASS;
            
            case DIRT:        
                return TileType.DIRT;
            
            case WATER:        
                return TileType.WATER;
            
            case STONE:        
                return TileType.STONE;

            case IRON:
                return TileType.IRON;

            case WOOD:
                return TileType.WOOD;

            case LEAVES:
                return TileType.LEAVES;

            case LADDER:
                return TileType.LADDER;

            default:
                return TileType.AIR;

        }

    }

    public TextureID textureID() {
        return this.textureID;
    }

    public int getItemID() {
        return this.itemID;
    }

    public float getMiningSpeed() {
        return this.miningSpeed;
    }

    public boolean canPlace() {
        return this.placeable;
    }

    public boolean canRoll() {
        return this.obtainable;
    }

    public static Item fromInt(int id) {

        for(Item item : values()) {
            if(item.itemID == id) return item;
        }

        return NONE;
    
    }

}
