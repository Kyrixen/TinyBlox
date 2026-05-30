package io.kyrixen.tinyblox.world.chunk;

import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ChunkBuilder {

    private final byte size;

    private Tile[][] buildedChunk;

    public ChunkBuilder(byte size) {

        this.size = size;

        buildedChunk = new Tile[size][size];

    }

    public void setTile(byte tX, byte tY, TileType type, boolean solid, byte height) {

        Tile t = new Tile(type, height);

        buildedChunk[tX][tY] = t;

    }

    public void fill(TileType type, byte height) {

        for (int tx = 0; tx < this.size; tx++) {
        
            for (int ty = 0; ty < this.size; ty++) {
        
                Tile tile = new Tile(type, height);
        
                buildedChunk[tx][ty] = tile;
        
            }
        
        }
    
    }

    public Tile[][] build() { return buildedChunk; }

}
