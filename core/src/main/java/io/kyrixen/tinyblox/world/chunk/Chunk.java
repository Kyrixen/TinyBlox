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

    // Count of tiles in chunk
    private final int CHUNK_SIZE;

    // Chunk cords
    private final int cX;
    private final int cY;

    // Define max and min values
    public static final int MAX_X = Constants.MAP_WIDTH;
    public static final int MAX_Y = Constants.MAP_HEIGHT;
    public static final int MIN_X = 0;
    public static final int MIN_Y = 0;

    // Chunk properties
    public boolean loaded;
    public boolean rendered;
    public boolean modified;

    // Stores chunk tiles
    private TileStack[][] chunk;

    // Stores light level for tiles
    private Color[][] lightLevel;

    // Construct chunk
    public Chunk(int x, int y, int size, int seed, boolean loaded){

        this.chunkSeed = RandomUtils.mixSeed(seed, x * 341873128712L ^ y * 132897987541L);

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        this.chunk = new TileStack[CHUNK_SIZE][CHUNK_SIZE];
        this.lightLevel = new Color[CHUNK_SIZE][CHUNK_SIZE];

        for(byte xPos = 0; xPos < CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < CHUNK_SIZE; yPos++) {
                this.chunk[xPos][yPos] = new TileStack();
                this.lightLevel[xPos][yPos] = new Color(1f, 1f, 1f, 1f);
            }
        }
        
        this.modified = false;
        this.loaded = loaded;
        this.rendered = loaded;
        
    }

    
    public void load(){

        if(!loaded) return;

        rendered = true;

    }

    public void unload(){

        if(!rendered) return;

        rendered = false;

    }

    public int getX(){ return this.cX; }

    public int getY(){ return this.cY; }

    public int getChunkSize() { return this.CHUNK_SIZE; }

    public long getChunkSeed() { return this.chunkSeed; }


    // Reset default lighting
    public void resetAmbientLighting(Color ambient) {

        for(byte xPos = 0; xPos < lightLevel.length; xPos++) {
            for(byte yPos = 0; yPos < lightLevel.length; yPos++) {
            
                this.setLight(xPos, yPos, ambient);

            }
        }

    }

    // Check loading
    public void checkIfOnScreen(Camera cam) {

        int chunkWorldSize = CHUNK_SIZE * Constants.GRID_SIZE;
        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);

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
        rendered = loaded && cX >= left && cX <= right && cY >= top && cY <= bottom;

    }

    public TileStack[][] get() {
        return this.chunk;
    }

    public void set(TileStack[][] tiles) {
    
        if (tiles == null) return;

        chunk = new TileStack[CHUNK_SIZE][CHUNK_SIZE];
    
        for(byte xPos = 0; xPos < CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < CHUNK_SIZE; yPos++) {
                chunk[xPos][yPos] = new TileStack();
            }
        }

        for(byte localX = 0; localX < this.CHUNK_SIZE; localX++) {
            for(byte localY = 0; localY < this.CHUNK_SIZE; localY++) {

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
    
        modified = true;
        loaded = true;
        rendered = loaded;
    
    }

    public TileStack getTileStack(byte localX, byte localY) {
        if(localX < 0 || localX >= this.getChunkSize() || localY < 0 || localY >= this.getChunkSize()) return null;
        return this.chunk[localX][localY];
    }

    public Color getLight(byte xPos, byte yPos) {
        if(xPos < 0 || xPos >= this.getChunkSize() || yPos < 0 || yPos >= this.getChunkSize()) return null;
        return this.lightLevel[xPos][yPos];
    }

    public void setLight(byte xPos, byte yPos, Color light) {
        this.lightLevel[xPos][yPos].set(light);
    }

    // Unload resources
    public void cleanup() {

        // Clear local tile map
        if (chunk != null) chunk = null;

        // Mark unloaded and remove references
        loaded = false;
        rendered = false;
        

    }

}
