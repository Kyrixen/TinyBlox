package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.Color;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Chunk {

    // Chunk seed
    private final long chunkSeed;

    // Chunk cords
    private final int cX;
    private final int cY;

    // Chunk properties
    public boolean rendered;

    // Stores chunk tiles
    private TileStack[][] chunk;

    // Stores light level for tiles
    private Color[][][] lightLevel;

    // Construct chunk
    public Chunk(int x, int y, int seed){

        this.chunkSeed = RandomUtils.mixSeed(seed, x * 341873128712L ^ y * 132897987541L);

        this.cX = x;
        this.cY = y;

        this.chunk = new TileStack[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
        this.lightLevel = new Color[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE][Constants.MAX_WORLD_HEIGHT + 1];

        for(byte xPos = 0; xPos < Constants.CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < Constants.CHUNK_SIZE; yPos++) {
            
                this.chunk[xPos][yPos] = new TileStack();

                for(byte layer = 0; layer <= Constants.MAX_WORLD_HEIGHT; layer++) {
                    this.lightLevel[xPos][yPos][layer] = new Color(1f, 1f, 1f, 1f);
                }

            }
        }

        this.rendered = false;
        
    }


    public int getX(){ return this.cX; }

    public int getY(){ return this.cY; }

    public long getChunkSeed() { return this.chunkSeed; }


    // Reset default lighting
    public void resetAmbientLighting(Color ambient) {

        for(byte xPos = 0; xPos < lightLevel.length; xPos++) {
            for(byte yPos = 0; yPos < lightLevel.length; yPos++) {
                for(byte layer = 0; layer < lightLevel[xPos][yPos].length; layer++) {
                    this.setLight(xPos, yPos, layer, ambient);
                }
            }
        }

    }

    // Check loading
    public void checkIfOnScreen(Camera cam) {

        int chunkWorldSize = Constants.CHUNK_SIZE * Constants.GRID_SIZE;
        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE);

        int camCenterX = cam.x + (int) (Constants.WINDOW_WIDTH / (2f * cam.zoom));
        int camCenterY = cam.y + (int) (Constants.WINDOW_HEIGHT / (2f * cam.zoom));

        // Camera chunk coordinates (top-left of screen)
        int camChunkX = camCenterX / chunkWorldSize;
        int camChunkY = camCenterY / chunkWorldSize;

        // Determine rendered chunk range
        int left   = Math.max(0, camChunkX - cam.RENDER_DISTANCE);
        int right  = Math.min(worldChunksX - 1, camChunkX + cam.RENDER_DISTANCE);
        int top    = Math.max(0, camChunkY - cam.RENDER_DISTANCE);
        int bottom = Math.min(worldChunksY - 1, camChunkY + cam.RENDER_DISTANCE);

        // Keep generated chunk data in RAM; only toggle rendering.
        rendered = cX >= left && cX <= right && cY >= top && cY <= bottom;

    }

    public TileStack[][] get() {
        return this.chunk;
    }

    public void set(TileStack[][] tiles) {
    
        if (tiles == null) return;

        chunk = new TileStack[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
    
        for(byte xPos = 0; xPos < Constants.CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < Constants.CHUNK_SIZE; yPos++) {
                chunk[xPos][yPos] = new TileStack();
            }
        }

        for(byte localX = 0; localX < Constants.CHUNK_SIZE; localX++) {
            for(byte localY = 0; localY < Constants.CHUNK_SIZE; localY++) {

                TileStack sourceStack = tiles[localX][localY];

                TileStack currentStack = this.getTileStack(localX, localY);

                if(sourceStack == null) continue;

                for(byte level = 0; level < sourceStack.stackSize(); level++) {

                    Tile tile = sourceStack.get(level);

                    if(tile == null) continue;

                    currentStack.set(tile, level);

                }

            }
        }
    
        rendered = true;
    
    }

    public TileStack getTileStack(byte localX, byte localY) {
        if(localX < 0 || localX >= Constants.CHUNK_SIZE || localY < 0 || localY >= Constants.CHUNK_SIZE) return null;
        return this.chunk[localX][localY];
    }

    public Color getLight(byte xPos, byte yPos, byte layer) {
        if(xPos < 0 || xPos >= Constants.CHUNK_SIZE || yPos < 0 || yPos >= Constants.CHUNK_SIZE || layer > Constants.MAX_WORLD_HEIGHT || layer < Constants.MIN_WORLD_HEIGHT) return null;
        return this.lightLevel[xPos][yPos][layer];
    }

    public void setLight(byte xPos, byte yPos, byte layer, Color light) {
        this.lightLevel[xPos][yPos][layer].set(light);
    }

    // Unload resources
    public void cleanup() {

        // Clear local tile map
        if (chunk != null) chunk = null;

        // Mark unloaded and remove references
        rendered = false;
        
    }

}
