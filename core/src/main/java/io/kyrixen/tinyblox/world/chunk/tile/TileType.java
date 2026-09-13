package io.kyrixen.tinyblox.world.chunk.tile;

import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

// Tile type enum
public class TileType {

    // Mining category enum
    public enum PreferedMiningType {
    
        NONE,
        WOOD,
        STONE,
    
    }


    // Tile identifier and atlas texture
    private final TinyIdentifier tileID;
    private final TinyIdentifier atlasID;

    // Texture atlas coords
    private final int tileX, tileY;
    
    private float mining_time = 0.20f;
    private boolean climbable = false;
    private boolean passable = true;
    private float slipperyModifier = 1f;
    private boolean terrain = false;
    private boolean support = false;
    private boolean transparent = false;
    private PreferedMiningType miningType = PreferedMiningType.NONE;
    private float lightLevel = 0f;
    private boolean empty = false;
    private TinyIdentifier itemDropID = new TinyIdentifier("tinyblox", IdentifierType.ITEM, "none");


    TileType(TinyIdentifier tileID, TinyIdentifier atlasID, int tileX, int tileY) {
        
        this.tileX = tileX;
        this.tileY = tileY;
        
        this.tileID = tileID;
        this.atlasID = atlasID;

    }


    // Getters //

    public TinyIdentifier getTileID() { return this.tileID; }
    public TinyIdentifier getAtlasID() { return this.atlasID; }

    public int getTileX() { return this.tileX; }
    public int getTileY() { return this.tileY; }
    
    public float getMiningTime() { return this.mining_time; }
    public PreferedMiningType getPreferedMining() { return this.miningType; }
    
    public float getSlipperyModifier() { return this.slipperyModifier; }

    public float getLightLevel() { return this.lightLevel; }

    public TinyIdentifier getItemDropID() { return this.itemDropID; }

    public boolean isClimbable() { return this.climbable; }
    public boolean isPassable() { return this.passable; }
    public boolean isTerrain() { return this.terrain; }
    public boolean isTransparent() { return this.transparent; }
    public boolean canSupport() { return this.support; }
    
    public boolean isEmpty() { return this.empty; }


    // Setters //

    public TileType miningTime(float mining_time) { this.mining_time = mining_time; return this; }
    public TileType prefferedMining(PreferedMiningType miningType) { this.miningType = miningType; return this; }
    
    public TileType slipperyModifier(float slipperyModifier) { this.slipperyModifier = slipperyModifier; return this; }
    
    public TileType lightLevel(float lightLevel) { this.lightLevel = lightLevel; return this; }

    public TileType itemDropID(TinyIdentifier itemDropID) { this.itemDropID = itemDropID; return this; }
    
    public TileType climbable(boolean climbable) { this.climbable = climbable; return this; }
    public TileType passable(boolean passable) { this.passable = passable; return this; }
    public TileType terrain(boolean terrain) { this.terrain = terrain; return this; }
    public TileType transparent(boolean transparent) { this.transparent = transparent; return this; }
    public TileType support(boolean support) { this.support = support; return this; }

    public TileType empty(boolean empty) { this.empty = empty; return this; }

}
