package io.kyrixen.tinyblox.world;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Chunk.Tile;
import io.kyrixen.tinyblox.world.Chunk.Tile.TileType;

public class ChunkBuilder {

    private short cX;
    private short cY;
    private byte size;

    private Tile[][] buildedChunk;

    public ChunkBuilder(short cx, short cy, byte size) {

        this.cX = cx;
        this.cY = cy;
        this.size = size;

        buildedChunk = new Tile[size][size];

    }

    public void setTile(byte tX, byte tY, TileType type, boolean solid, byte height) {

        int globalX = (this.cX * this.size + tX) * Constants.GRID_SIZE;
        int globalY = (this.cY * this.size + tY) * Constants.GRID_SIZE;

        Tile t = new Tile(globalX, globalY, type, height);
        t.solid = solid;

        buildedChunk[tX][tY] = t;

    }

    public void fill(TileType type, byte height) {

        for (int tx = 0; tx < this.size; tx++) {
        
            for (int ty = 0; ty < this.size; ty++) {
        
                int globalX = (this.cX * this.size + tx) * Constants.GRID_SIZE;
                int globalY = (this.cY * this.size + ty) * Constants.GRID_SIZE;
        
                Tile tile = new Tile(globalX, globalY, type, height);
        
                buildedChunk[tx][ty] = tile;
        
            }
        
        }
    
    }

    public Tile[][] build() { return buildedChunk; }

    public void cleanup() {

        buildedChunk = new Tile[size][size];

        this.cX = 0;
        this.cY = 0;
        this.size = 0;

    }

}
