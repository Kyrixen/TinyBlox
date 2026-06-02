package io.kyrixen.tinyblox.world.chunk.tile;

import io.kyrixen.tinyblox.entities.inventory.Item;

// Tile class
public class Tile {

    // Tile type enum
    public static enum TileType {
        
        AIR(9999999f, true, false, false, false, 0f),
        VOID(9999999f, false, false, false, false, 0f),
        LEAVES(0.10f, false, true, false, false, 0f),
        GRASS(0.20f, false, true, false, true, 0f),
        DIRT(0.35f, false, true, false, true, 0f),
        WATER(9999999f, true, false, false, true, 0f),
        STONE(0.75f, false, true, false, true, 0f),
        IRON(1.25f, false, true, false, false, 0.15f),
        WOOD(0.50f, false, true, false, false, 0f),
        LADDER(0.45f, true, true, true, false, 0f),
        CAGED_LAMP(0.6f, false, true, false, false, 0.75f);

        private final float mining_time;
        private final boolean climbable;
        private final boolean passable;
        private final boolean terrain;
        private final boolean support;
        private final float lightLevel;

        TileType(float mining_time, boolean passable, boolean support, boolean climbable, boolean terrain, float lightLevel) {
            this.mining_time = mining_time;
            this.passable = passable;
            this.climbable = climbable;
            this.terrain = terrain;
            this.support = support;
            this.lightLevel = lightLevel;
        }

        public float getMiningTime() { return this.mining_time; }
        public float getLightLevel() { return this.lightLevel; }

        public boolean isClimbable() { return this.climbable; }
        public boolean isPassable() { return this.passable; }
        public boolean isTerrain() { return this.terrain; }
        public boolean canSupport() { return this.support; }
        
        public boolean isEmpty() { return this == TileType.AIR; }
    
    }

    // Texture atlas coords
    private final int tileX, tileY;

    // Tile data
    private final TileType type;

    // Terrain height
    private final byte level;

    // Constructs tile
    public Tile(TileType type, byte level) {

        this.type = type;

        this.tileX = getTileX(type);
        this.tileY = getTileY(type);
        
        this.level = level;

    }

    // Map tileX to tileset via type
    private static int getTileX(TileType type) {

        switch (type) {
            case AIR        : return -1;
            case GRASS      : return 1;
            case STONE      : return 3;
            case IRON       : return 3;
            case DIRT       : return 0;
            case WATER      : return 0;
            case VOID       : return 1;
            case WOOD       : return 2;
            case LEAVES     : return 2;
            case LADDER     : return 0;
            case CAGED_LAMP : return 1;
            default         : return 0;
        }

    }


    // Map tileY to tileset via type
    private static int getTileY(TileType type) {
    
        switch (type) {
            case AIR        : return -1;
            case GRASS      : return 0;
            case STONE      : return 0;
            case IRON       : return 1;
            case DIRT       : return 0;
            case WATER      : return 1;
            case VOID       : return 1;
            case WOOD       : return 1; 
            case LEAVES     : return 0;
            case LADDER     : return 2;
            case CAGED_LAMP : return 2;
            default         : return 0;
        }
    
    }
    
    // Helper functions //

    public TileType type() { return type; }

    public byte level() { return level; }

    public int tileX() { return tileX; }
    public int tileY() { return tileY; }

    public Item getItem() {

        switch (type) {
            
            case GRASS:        
                return Item.GRASS;
            
            case DIRT:        
                return Item.DIRT;
            
            case WATER:        
                return Item.WATER;
            
            case STONE:        
                return Item.STONE;

            case IRON:
                return Item.IRON;

            case WOOD:
                return Item.WOOD;

            case LEAVES:
                return Item.LEAVES;

            case LADDER:
                return Item.LADDER;

            case CAGED_LAMP:
                return Item.CAGED_LAMP;

            default:
                return Item.NONE;

        }

    }

    @Override
    public String toString() {
        return "Tile{ type=" + type + ", level=" + level + " }";
    }

}
