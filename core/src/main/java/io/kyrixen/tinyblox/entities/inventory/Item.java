package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.chunk.Tile.TileType;

// Item object used for inventory
public enum Item {
    
    NONE(0, null, 0),
    GRASS(24, new TextureID("tinyblox", TextureType.TERRAIN, "grass"), 1),
    DIRT(24, new TextureID("tinyblox", TextureType.TERRAIN, "dirt"), 2),
    WATER(24, new TextureID("tinyblox", TextureType.TERRAIN, "water"), 3),
    STONE(24, new TextureID("tinyblox", TextureType.TERRAIN, "stone"), 4),
    WOOD(24, new TextureID("tinyblox", TextureType.TERRAIN, "wood"), 5),
    LEAVES(24, new TextureID("tinyblox", TextureType.TERRAIN, "leaves"), 6);

    // Max stackable size
    private final byte maxSize;
    private final TextureID textureID;
    private final int itemID;

    // Constructor
    Item(int maxSize, TextureID textureID, int itemID) {
        this.maxSize = (byte) maxSize;
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

            case WOOD:
                return TileType.WOOD;

            case LEAVES:
                return TileType.LEAVES;

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

    public static Item fromInt(int id) {

        for(Item item : values()) {
            if(item.itemID == id) return item;
        }

        return NONE;
    
    }

}
