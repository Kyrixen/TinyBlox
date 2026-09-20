package io.kyrixen.tinyblox.world.chunk;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.TileType;

public class ChunkBuilder {

    private TileStack[][] buildedChunk;

    
    public ChunkBuilder() {

        buildedChunk = new TileStack[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];

        for(int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for(int y = 0; y < Constants.CHUNK_SIZE; y++) {
                buildedChunk[x][y] = new TileStack();
            }
        }

    }

    public void setTile(byte tX, byte tY, TileType type, byte height) {

        Tile t = new Tile(type, height);
        buildedChunk[tX][tY].set(t, height);

    }

    public void fill(TileType type, byte height) {

        for(int tx = 0; tx < Constants.CHUNK_SIZE; tx++) {
            for(int ty = 0; ty < Constants.CHUNK_SIZE; ty++) {
        
                Tile tile = new Tile(type, height);
                buildedChunk[tx][ty].set(tile, height);
        
            }
        }
    
    }

    public TileStack[][] build() { return buildedChunk; }

}
