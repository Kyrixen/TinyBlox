package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.chunk.Tile.TileType;

// Item object used for inventory
public enum Item {
    
    NONE(0, null),
    GRASS(24, new TextureID("tinyblox", TextureType.TERRAIN, "grass")),
    DIRT(24, new TextureID("tinyblox", TextureType.TERRAIN, "dirt")),
    WATER(24, new TextureID("tinyblox", TextureType.TERRAIN, "water")),
    STONE(24, new TextureID("tinyblox", TextureType.TERRAIN, "stone"));


    // Max stackable size
    private final byte maxSize;
    private final TextureID textureID;

    // Constructor
    Item(int maxSize, TextureID textureID) {
        this.maxSize = (byte) maxSize;
        this.textureID = textureID;
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

            default:
                return TileType.AIR;

        }

    }

    public TextureID textureID() {
        return this.textureID;
    }

}
