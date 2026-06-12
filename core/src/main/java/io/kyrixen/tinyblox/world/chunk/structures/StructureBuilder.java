package io.kyrixen.tinyblox.world.chunk.structures;

import io.kyrixen.tinyblox.world.chunk.structures.Structure.Rarity;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class StructureBuilder {

    // Build name
    private final String structureName;

    // Build dimensions
    private final byte w, h, layers;

    // Build rarity
    private final Rarity structureRarity;

    // Structure tile data
    private TileStack[][] buildedStructure;


    // Construct new structure build
    public StructureBuilder(String structureName, byte width, byte height, byte layers, Rarity structureRarity) {

        this.structureName = structureName;

        this.w = width;
        this.h = height;
        this.layers = layers;

        this.structureRarity = structureRarity;

        buildedStructure = new TileStack[w][h];
        for (int tw = 0; tw < this.w; tw++) {
            for (int th = 0; th < this.h; th++) {
                buildedStructure[tw][th] = new TileStack();
            }
        }

        this.fill(TileType.AIR);

    }


    // Set tile at position
    public void setTile(byte tX, byte tY, byte tL, TileType type) {

        Tile t = new Tile(type, (byte) tL);
        buildedStructure[tX][tY].set(t, tL);

    }

    // Fill entire build with
    public void fill(TileType type) {

        for(int tx = 0; tx < this.w; tx++) {
            for(int ty = 0; ty < this.h; ty++) {
                for(byte level = 0; level < layers; level++) {
                    buildedStructure[tx][ty].set(new Tile(type, (byte) level), level); 
                }
            }
        }
    
    }

    // Fill entire layer build with
    public void fillLayer(TileType type, byte layer) {

        for (int tx = 0; tx < this.w; tx++) {
            for (int ty = 0; ty < this.h; ty++) {
        
                Tile tile = new Tile(type, layer);
    
                buildedStructure[tx][ty].set(tile, layer);
        
            }
        }
    
    }

    
    // Build structure
    public Structure build() { return new Structure(structureName, w, h, layers, structureRarity, buildedStructure); }

}
