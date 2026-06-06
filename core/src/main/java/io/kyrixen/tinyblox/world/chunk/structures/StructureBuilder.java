package io.kyrixen.tinyblox.world.chunk.structures;

import io.kyrixen.tinyblox.world.chunk.structures.Structure.Rarity;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class StructureBuilder {

    // Build name
    private final String structureName;

    // Build dimensions
    private final byte w, h;

    // Build rarity
    private final Rarity structureRarity;

    // Structure tile data
    private Tile[][] buildedStructure;


    // Construct new structure build
    public StructureBuilder(String structureName, byte width, byte height, Rarity structureRarity) {

        this.structureName = structureName;

        this.w = width;
        this.h = height;

        this.structureRarity = structureRarity;

        buildedStructure = new Tile[w][h];
        for (int tw = 0; tw < this.w; tw++) {
            for (int th = 0; th < this.h; th++) {
                Tile tile = new Tile(TileType.AIR, (byte) 1);
                buildedStructure[tw][th] = tile;
            }
        }

    }


    // Set tile at position
    public void setTile(byte tX, byte tY, TileType type) {

        Tile t = new Tile(type, (byte) 1);

        buildedStructure[tX][tY] = t;

    }

    // Fill entire build with
    public void fill(TileType type) {

        for (int tx = 0; tx < this.w; tx++) {
            for (int ty = 0; ty < this.h; ty++) {
        
                Tile tile = new Tile(type, (byte) 1);
        
                buildedStructure[tx][ty] = tile;
        
            }
        }
    
    }

    
    // Build structure
    public Structure build() { return new Structure(structureName, w, h, structureRarity, buildedStructure); }

}
