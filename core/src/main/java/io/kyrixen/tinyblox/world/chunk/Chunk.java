package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.TimeCycle;

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
    private TextureManager tex;

    // Stores chunk tiles
    private TileStack[][] chunk;

    private final TextureID terrainTileset = new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles");

    // Construct chunk
    public Chunk(int x, int y, int size, boolean loaded, TextureManager tex, Camera cam){

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        this.chunk = new TileStack[CHUNK_SIZE][CHUNK_SIZE];

        for(byte xPos = 0; xPos < CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < CHUNK_SIZE; yPos++) {
                this.chunk[xPos][yPos] = new TileStack();
            }
        }
        
        this.tex = tex;
        this.cam = cam;
        
        this.modified = false;
        this.loaded = loaded;
        this.rendered = loaded;
        
    }

    // Render chunk
    public void render(TileRenderer tileRenderer, SpriteBatch batch, TimeCycle timeCycle) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        // Check if textures are loaded
        if (tex == null || tex.getTexture(terrainTileset) == null) Logger.LOGGER.warn("WORLD", "Textures or terrainTileset not loaded for chunk " + cX + "," + cY);

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                Tile tile = this.getTileStack(tx, ty).top();

                if(tile == null) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;
                
                batch.setColor(timeCycle.getBrightness(), timeCycle.getBrightness(), timeCycle.getBrightness(), 1.0f);

                tileRenderer.drawTileset(terrainTileset, globalX, globalY, tile.tileX, tile.tileY, Constants.GRID_SIZE, batch);
                
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

            }
        }
        
    }

    public void renderDepthOverlay(ShapeRenderer shapeRenderer, TimeCycle timeCycle) {

        // Check if can render overlay for chunk
        if (!loaded || !rendered) return;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        // Check if textures are loaded
        if (tex == null || tex.getTexture(terrainTileset) == null) {
            Logger.LOGGER.warn("WORLD", "Textures not loaded for chunk " + cX + "," + cY);
            return;
        }

        float light = timeCycle.getBrightness();

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                Tile tile = this.getTileStack(tx, ty).top();

                if(tile == null) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                switch(tile.level()) {

                    case -1:
                        shapeRenderer.setColor(0f, 0f, 0f, 0.20f * light);
                        break;

                    case 0:
                        continue;

                    case 1:
                        shapeRenderer.setColor(1f, 1f, 1f, 0.20f * light);
                        break;

                    case 2:
                        shapeRenderer.setColor(1f, 1f, 1f, 0.40f * light);
                        break;
                }

                shapeRenderer.rect((globalX - cam.x) * cam.zoom, (globalY - cam.y) * cam.zoom, Constants.GRID_SIZE * cam.zoom, Constants.GRID_SIZE * cam.zoom);
            
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

        for (byte localX = 0; localX < this.CHUNK_SIZE; localX++) {
            for (byte localY = 0; localY < this.CHUNK_SIZE; localY++) {
            
                Tile tile = tiles[localX][localY].top();
                if (tile == null) continue;

                if (localX >= 0 && localX < CHUNK_SIZE && localY >= 0 && localY < CHUNK_SIZE) this.getTileStack(localX, localY).push(tile);
        
            }
        }
    
        modified = true;
        loaded = true;
        rendered = loaded;
    
    }

    public TileStack getTileStack(byte localX, byte localY) {
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
