package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.Camera;

public class Chunk {

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

    // Own camera object
    private Camera cam;

    // Chunk properties
    public boolean loaded;
    public boolean rendered;
    public boolean modified;

    // Textures helper
    private Textures tex;

    // Stores chunk tiles
    private Tile[][] chunk;


    // Construct chunk
    public Chunk(int x, int y, int size, boolean loaded, Textures tex, Camera cam){

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        this.chunk = new Tile[CHUNK_SIZE][CHUNK_SIZE];
        
        this.tex = tex;
        this.cam = cam;
        
        this.modified = false;
        this.loaded = loaded;
        this.rendered = loaded;
        
    }

    // Render chunk
    public void render(SpriteBatch batch) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        // Check if textures are loaded
        if (tex == null || tex.terrainTileset == null) {
            System.out.println("Warning: Textures or terrainTileset not loaded for chunk " + cX + "," + cY);
            return;
        }

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                if(this.getTile(tx, ty) == null) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                tex.drawTileset(tex.terrainTileset, globalX, globalY, Constants.GRID_SIZE, Constants.GRID_SIZE, this.getTile(tx, ty).tileX, this.getTile(tx, ty).tileY, Constants.GRID_SIZE, batch);
            }
        }
        
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

    // Check loading
    public void checkIfOnScreen() {

        int chunkWorldSize = CHUNK_SIZE * Constants.GRID_SIZE;
        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);

        // Camera chunk coordinates (top-left of screen)
        int camChunkX = cam.x / chunkWorldSize;
        int camChunkY = cam.y / chunkWorldSize;

        int buffer = Constants.BUFFER; // Number of chunks beyond camera view

        // Determine rendered chunk range
        // Determine rendered chunk range
        int left   = Math.max(0, camChunkX - buffer);
        int right  = Math.min(worldChunksX - 1, camChunkX + cam.RENDER_DISTANCE + buffer);
        int top    = Math.max(0, camChunkY - buffer);
        int bottom = Math.min(worldChunksY - 1, camChunkY + cam.RENDER_DISTANCE + buffer);

        // Keep generated chunk data in RAM; only toggle rendering.
        rendered = loaded && cX >= left && cX <= right && cY >= top && cY <= bottom;

    }

    public Tile[][] get() {
        return this.chunk;
    }

    public void set(Tile[][] tiles) {
    
        if (tiles == null) return;

        chunk = new Tile[CHUNK_SIZE][CHUNK_SIZE];
    
        for (byte localX = 0; localX < this.CHUNK_SIZE; localX++) {
            for (byte localY = 0; localY < this.CHUNK_SIZE; localY++) {
            
                Tile tile = tiles[localX][localY];
                if (tile == null) continue;

                if (localX >= 0 && localX < CHUNK_SIZE && localY >= 0 && localY < CHUNK_SIZE) this.setTile(localX, localY, tile);
        
            }
        }
    
        modified = true;
        loaded = true;
        rendered = loaded;
    
    }

    public void setTile(byte tx, byte ty, Tile tile) {

        if (tile == null) return;

        tile.updateSolid();

        tile.height = (byte) Math.max(-1, Math.min(tile.height, 3));

        this.chunk[tx][ty] = tile;

        modified = true;

    }

    public Tile getTile(byte localX, byte localY) {
        return this.chunk[localX][localY];
    }

    // Unload resources
    public void cleanup() {

        // Clear local tile map
        if (chunk != null) chunk = null;

        // Mark unloaded and remove references
        loaded = false;
        rendered = false;
        cam = null;
        tex = null;
        

    }

}
