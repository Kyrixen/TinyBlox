package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.world.chunk.Tile.TileType;

// Item object used for inventory
public enum Item {
    
    NONE(0),
    GRASS(24),
    DIRT(24),
    WATER(24),
    STONE(24);

    // Max stackable size
    private final byte maxSize;

    // Constructor
    Item(int maxSize) {
        this.maxSize = (byte) maxSize;
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

}
