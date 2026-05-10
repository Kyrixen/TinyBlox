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
    byte height;

    // Collision
    boolean solid;

    // Constructs tile
    public Tile(TileType type, byte height) {

        this.type = type;

        this.tileX = getTileX(type);
        this.tileY = getTileY(type);
        
        this.height = height;

        this.solid = this.height >= 1;

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

    public boolean solid(){ return solid; }

    public TileType type() { return type; }

    public byte height() { return height; }

    public void updateSolid() {
        this.solid = this.height >= 1;
        if(this.type == TileType.AIR) solid = true;
    }

    @Override
    public String toString() {
        return "Tile{ solid=" + solid + ", type=" + type + ", height=" + height + " }";
    }

}