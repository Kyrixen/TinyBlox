package io.kyrixen.tinyblox.world;

import java.util.ArrayList;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Chunk.Tile;

public class ChunkBuilder {

    private int x;
    private int y;
    private int size;

    private ArrayList<Tile> buildedChunk;

    public ChunkBuilder(int x, int y, int size) {

        this.x = x;
        this.y = y;
        this.size = size;

        buildedChunk = new ArrayList<>();

    }

    public void setTile(int tX, int tY, String type) {

        if(type == null) return;

        int totalX = (this.x * this.size + tX) * Constants.GRID_SIZE;
        int totalY = (this.y * this.size + tY) * Constants.GRID_SIZE;

        Tile t = new Tile(totalX, totalY, type);

        for(int i = 0; i < buildedChunk.size(); i++) {
            
            Tile current = buildedChunk.get(i);
        
            if(current.getX() == totalX && current.getY() == totalY) {
                buildedChunk.set(i, t); 
                return;
            }
        
        }
        
        buildedChunk.add(t);

    }

    public void fill(String type) {

        for (int tx = 0; tx < this.size; tx++) {
        
            for (int ty = 0; ty < this.size; ty++) {
        
                int worldX = (this.x * this.size + tx) * Constants.GRID_SIZE;
                int worldY = (this.y * this.size + ty) * Constants.GRID_SIZE;
        
                Tile tile = new Tile(worldX, worldY, type);
        
                buildedChunk.add(tile);
        
            }
        
        }
    
    }

    public ArrayList<Tile> build() { return buildedChunk; }

    public void cleanup() {

        buildedChunk.clear();

        this.x = 0;
        this.y = 0;
        this.size = 0;

    }

}
