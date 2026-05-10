package io.kyrixen.tinyblox.world.chunk;

// Tile class
public class Tile {

    // Tile type enum
    public static enum TileType {
    
        AIR,
        GRASS,
        DIRT,
        WATER,
        STONE
    
    }

    // Texture atlas coords
    int tileX, tileY;

    // Tile data
    TileType type;

    // Terrain height
    byte level;

    // Constructs tile
    public Tile(TileType type, byte height) {

        this.type = type;

        this.tileX = getTileX(type);
        this.tileY = getTileY(type);
        
        this.level = height;

    }

    // Map tileX to tileset via type
    private static int getTileX(TileType type) {

        switch (type) {
            case GRASS: return 1;
            case STONE: return 0;
            case DIRT : return 0;
            case WATER: return 1;
            case AIR  : return 0;
            default   : return 0;
        }

    }


    // Map tileY to tileset via type
    private static int getTileY(TileType type) {
    
        switch (type) {
            case GRASS: return 0;
            case STONE: return 1;
            case DIRT : return 0;
            case WATER: return 1;
            case AIR  : return 2;
            default   : return 0;
        }
    
    }
    
    // Helper functions //

    public TileType type() { return type; }

    public byte level() { return level; }

    @Override
    public String toString() {
        return "Tile{ type=" + type + ", level=" + level + " }";
    }

}