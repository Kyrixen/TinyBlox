package io.kyrixen.tinyblox.world.chunk.tile;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.chunk.tile.TileType.PreferedMiningType;

public class TileRegister {
    
    // Tiles holder
    private static final List<TileType> TILES = new ArrayList<>();
    
    // Helper item holders //

    public static final TileType AIR = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "air"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), -1, -1).passable(true).transparent(true).empty(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "none"));
    public static final TileType VOID = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "void"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 1, 1).passable(false).empty(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "none"));
    public static final TileType LEAVES = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "leaves"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 2, 0).miningTime(0.10f).prefferedMining(PreferedMiningType.WOOD).passable(false).slipperyModifier(1.05f).support(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "leaves"));
    public static final TileType GRASS = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "grass"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 1, 0).miningTime(0.20f).passable(false).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "grass"));
    public static final TileType DIRT = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "dirt"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 0, 0).miningTime(0.35f).passable(false).slipperyModifier(0.95f).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "dirt"));
    public static final TileType WATER = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "water"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 0, 1).passable(true).slipperyModifier(0.70f).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "water"));
    public static final TileType STONE = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "stone"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 3, 0).miningTime(1.0f).prefferedMining(PreferedMiningType.STONE).passable(false).slipperyModifier(0.9f).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "stone"));
    public static final TileType COAL = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "coal"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 3, 2).miningTime(1.50f).prefferedMining(PreferedMiningType.STONE).passable(false).slipperyModifier(0.85f).support(true).lightLevel(0.05f).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "coal"));
    public static final TileType IRON = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "iron"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 3, 1).miningTime(1.75f).prefferedMining(PreferedMiningType.STONE).passable(false).slipperyModifier(0.85f).support(true).lightLevel(0.10f).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "iron"));
    public static final TileType WOOD = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "wood"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 2, 1).miningTime(0.60f).prefferedMining(PreferedMiningType.WOOD).passable(false).support(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "wood"));
    public static final TileType LADDER = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "ladder"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 0, 2).miningTime(0.35f).prefferedMining(PreferedMiningType.WOOD).passable(true).slipperyModifier(1.15f).support(true).climbable(true).transparent(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "ladder"));
    public static final TileType CAGED_LAMP = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "caged_lamp"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 1, 2).miningTime(0.6f).prefferedMining(PreferedMiningType.STONE).passable(false).slipperyModifier(0.95f).support(true).lightLevel(0.75f).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "caged_lamp"));
    public static final TileType SLIME_TILE = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "slime_tile"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 2, 2).miningTime(0.30f).passable(false).slipperyModifier(0.55f).support(true).lightLevel(0.01f).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "slime"));
    public static final TileType SAND = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "sand"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 1, 3).miningTime(0.4f).passable(false).slipperyModifier(0.95f).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "sand"));
    public static final TileType GLASS = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "glass"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 0, 3).miningTime(0.15f).passable(false).slipperyModifier(0.85f).support(true).transparent(true).lightLevel(0.02f).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "glass"));
    public static final TileType CLAY = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "clay"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 2, 3).miningTime(0.3f).passable(false).slipperyModifier(1.05f).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "clay"));
    public static final TileType BRICK = new TileType(new TinyIdentifier("tinyblox", IdentifierType.TILE, "brick"), new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), 3, 3).miningTime(0.9f).passable(false).slipperyModifier(0.95f).support(true).terrain(true).itemDropID(new TinyIdentifier("tinyblox", IdentifierType.ITEM, "brick"));

    
    // Inits internal tiles
    public static void initTiles() {

        if(!TILES.isEmpty()) return;

        TILES.add(AIR);
        TILES.add(VOID);
        TILES.add(LEAVES);
        TILES.add(GRASS);
        TILES.add(DIRT);
        TILES.add(WATER);
        TILES.add(STONE);
        TILES.add(COAL);
        TILES.add(IRON);
        TILES.add(WOOD);
        TILES.add(LADDER);
        TILES.add(CAGED_LAMP);
        TILES.add(SLIME_TILE);
        TILES.add(SAND);
        TILES.add(GLASS);
        TILES.add(CLAY);
        TILES.add(BRICK);

    }


    // Getters //

    // Get items list
    public static List<TileType> getTileList() { return TILES; }

    // Search tile by ID
    public static TileType getTileByID(TinyIdentifier id) {

        if(id == null) return null;

        for(TileType tile : TILES) {
            if(tile.getTileID().equals(id)) return tile;
        }

        return null;

    }


    // Setter //

    public static void add(TileType tile) {
        if(TILES.contains(tile)) return;
        TILES.add(tile);
    }

}
