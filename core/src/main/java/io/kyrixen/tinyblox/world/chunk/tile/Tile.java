package io.kyrixen.tinyblox.world.chunk.tile;

import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;

// Tile class
public class Tile {

    // Tile type enum
    public static enum TileType {

        AIR(9999999f, PreferedMiningType.NONE, true, 1.0f, false, false, false, true, 0f),
        VOID(9999999f, PreferedMiningType.NONE, false, 1.0f, false, false, false, false, 0f),
        LEAVES(0.10f, PreferedMiningType.WOOD, false, 1.05f, true, false, false, false, 0f),
        GRASS(0.20f, PreferedMiningType.NONE, false, 1f, true, false, true, false, 0f),
        DIRT(0.35f, PreferedMiningType.NONE, false, 0.95f, true, false, true, false, 0f),
        WATER(9999999f, PreferedMiningType.NONE, true, 0.70f, false, false, true, false, 0f),
        STONE(1.0f, PreferedMiningType.STONE, false, 0.9f, true, false, true, false, 0f),
        COAL(1.50f, PreferedMiningType.STONE, false, 0.85f, true, false, false, false, 0.05f),
        IRON(1.75f, PreferedMiningType.STONE, false, 0.85f, true, false, false, false, 0.10f),
        WOOD(0.60f, PreferedMiningType.WOOD, false, 1f, true, false, false, false, 0f),
        LADDER(0.35f, PreferedMiningType.WOOD, true, 1.15f, true, true, false, true, 0f),
        CAGED_LAMP(0.6f, PreferedMiningType.STONE, false, 0.95f, true, false, false, false, 0.75f),
        SLIME_TILE(0.30f, PreferedMiningType.NONE, false, 0.55f, true, false, false, false, 0.01f),
        SAND(0.4f, PreferedMiningType.NONE, false, 0.95f, true, false, true, false, 0f),
        GLASS(0.15f, PreferedMiningType.NONE, false, 0.85f, true, false, false, true, 0.02f),
        CLAY(0.3f, PreferedMiningType.NONE, false, 1.05f, true, false, true, false, 0f),
        BRICK(0.9f, PreferedMiningType.NONE, false, 0.95f, true, false, true, false, 0f);

        // Mining category enum
        public enum PreferedMiningType {
        
            NONE,
            WOOD,
            STONE,
        
        }


        private final float mining_time;
        private final boolean climbable;
        private final boolean passable;
        private final float slipperyModifier;
        private final boolean terrain;
        private final boolean support;
        private final boolean transparent;
        private final PreferedMiningType miningType;
        private final float lightLevel;

        TileType(float mining_time, PreferedMiningType miningType, boolean passable, float slipperyModifier, boolean support, boolean climbable, boolean terrain, boolean transparent, float lightLevel) {
            this.mining_time = mining_time;
            this.miningType = miningType;
            this.passable = passable;
            this.slipperyModifier = slipperyModifier;
            this.climbable = climbable;
            this.terrain = terrain;
            this.support = support;
            this.transparent = transparent;
            this.lightLevel = lightLevel;
        }

        public float getMiningTime() { return this.mining_time; }
        public PreferedMiningType getPreferedMining() { return this.miningType; }
        
        public float getSlipperyModifier() { return this.slipperyModifier; }

        public float getLightLevel() { return this.lightLevel; }

        public boolean isClimbable() { return this.climbable; }
        public boolean isPassable() { return this.passable; }
        public boolean isTerrain() { return this.terrain; }
        public boolean isTransparent() { return this.transparent; }
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
            case COAL       : return 3;
            case IRON       : return 3;
            case DIRT       : return 0;
            case WATER      : return 0;
            case VOID       : return 1;
            case WOOD       : return 2;
            case LEAVES     : return 2;
            case LADDER     : return 0;
            case CAGED_LAMP : return 1;
            case SLIME_TILE : return 2;
            case SAND       : return 1;
            case GLASS      : return 0;
            case CLAY       : return 2;
            case BRICK      : return 3;   
            default         : return 0;
        }

    }


    // Map tileY to tileset via type
    private static int getTileY(TileType type) {
    
        switch (type) {
            case AIR        : return -1;
            case GRASS      : return 0;
            case STONE      : return 0;
            case COAL       : return 2;
            case IRON       : return 1;
            case DIRT       : return 0;
            case WATER      : return 1;
            case VOID       : return 1;
            case WOOD       : return 1; 
            case LEAVES     : return 0;
            case LADDER     : return 2;
            case CAGED_LAMP : return 2;
            case SLIME_TILE : return 2;
            case SAND       : return 3;
            case GLASS      : return 3;
            case CLAY       : return 3;
            case BRICK      : return 3;
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
                return ItemRegister.GRASS;
            
            case DIRT:        
                return ItemRegister.DIRT;
            
            case WATER:        
                return ItemRegister.WATER;
            
            case STONE:        
                return ItemRegister.STONE;

            case COAL:
                return ItemRegister.COAL;

            case IRON:
                return ItemRegister.IRON;

            case WOOD:
                return ItemRegister.WOOD;

            case LEAVES:
                return ItemRegister.LEAVES;

            case LADDER:
                return ItemRegister.LADDER;

            case CAGED_LAMP:
                return ItemRegister.CAGED_LAMP;

            case SLIME_TILE:
                return ItemRegister.SLIME;

            case GLASS:
                return ItemRegister.GLASS;

            case SAND:
                return ItemRegister.SAND;

            case CLAY:
                return ItemRegister.CLAY;

            case BRICK:
                return ItemRegister.BRICK;

            default:
                return ItemRegister.NONE;

        }

    }

    @Override
    public String toString() {
        return "Tile{ type=" + type + ", level=" + level + " }";
    }

}
