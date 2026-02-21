package io.kyrixen.tinyblox.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.fastnoiselite.FastNoiseLite;

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
    public boolean modified;

    // Textures helper
    private Textures tex;

    // Stores chunk tiles
    public HashMap<String, Tile> chunk = new HashMap<>();

    // Tile class
    public static class Tile {

        // Cords
        int x, y;
        
        // Type
        String type;
        
        // Helper for tileset loading
        int tileX, tileY;

        // Tile properties
        boolean solid, loaded;

        // Constructs tile
        public Tile(int x, int y, String type) {

            this.x = x;
            this.y = y;

            this.type = type;
            
            this.tileX = getTileX(type);
            this.tileY = getTileY(type);
            
            this.solid = getSolid(type);
            this.loaded = true; // Loaded by default

        }


        // Map tileX to tileset via type
        private static int getTileX(String type) {

            switch (type.toLowerCase()) {
                case "grass": return 1;
                case "stone": return 0;
                case "dirt" : return 0;
                case "water": return 1;
                default     : return 0;
            }

        }


        // Map tileY to tileset via type
        private static int getTileY(String type) {
        
            switch (type.toLowerCase()) {
                case "grass": return 0;
                case "stone": return 1;
                case "dirt" : return 0;
                case "water": return 1; 
                default     : return 0;
            }
        
        }

        // Get solid via type
        private static boolean getSolid(String type){

            switch(type.toLowerCase()){
                case "grass": return false;
                case "dirt" : return false;
                case "stone": return true;
                case "water": return true;
                default     : return false;
            }

        }

        // Helper functions //

        public int getY(){ return y; }

        public int getX(){ return x; }

        public boolean solid(){ return solid; }

    }

    // Construct chunk
    public Chunk(int x, int y, int size, boolean loaded, Textures tex, Camera cam){

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        
        this.tex = tex;
        this.cam = cam;
        
        this.modified = false;
        this.loaded = loaded;
        
    }

    // Helper func
    public String generateKey(int tx, int ty){
        return Integer.toString(tx) + "," + Integer.toString(ty);
    }

    // Generate the chunk
    public void generate(FastNoiseLite noise) {

        chunk.clear();

        // World size in tiles
        int worldTilesX = Constants.MAP_WIDTH;
        int worldTilesY = Constants.MAP_HEIGHT;

        // World size in chunks
        int worldChunksX = worldTilesX / CHUNK_SIZE;
        int worldChunksY = worldTilesY / CHUNK_SIZE;

        //System.out.println("Here");

        // Safety: do not generate invalid chunks
        if (cX < 0 || cY < 0 || cX >= worldChunksX || cY >= worldChunksY) {
            loaded = false;
            return;
        }

        //System.out.println("Here too");

        for (int tx = 0; tx < CHUNK_SIZE; tx++) {
            for (int ty = 0; ty < CHUNK_SIZE; ty++) {

                // Tile position in WORLD TILE coordinates
                int tileX = cX * CHUNK_SIZE + tx;
                int tileY = cY * CHUNK_SIZE + ty;

                // Skip tiles outside world tile bounds
                if (tileX < 0 || tileY < 0 ||
                    tileX >= worldTilesX || tileY >= worldTilesY) {
                    continue;
                }

                // Convert tile coords → world PIXELS
                int worldX = tileX * Constants.GRID_SIZE;
                int worldY = tileY * Constants.GRID_SIZE;

                float t = noise.GetNoise(tileX, tileY);

                String type;

                if (t < -0.3f)      type = "water";
                else if (t < 0.0f)  type = "stone";
                else if (t < 0.5f)  type = "dirt";
                else                type = "grass";

                Tile tile = new Tile(worldX, worldY, type);

                chunk.put(tx + "," + ty, tile);

                //System.out.println(tile);

                //Terrain.tiles.add(tile);

            }

        }

        //System.out.println("Chunk size: " + this.chunk.size());

        loaded = !chunk.isEmpty();

    }

    // Render chunk
    public void render(SpriteBatch batch) {

        // Check if can render chunk
        if (!loaded) return;
        if (cX < 0 || cX >= MAX_X / Constants.GRID_SIZE || cY < 0 || cY >= MAX_Y / Constants.GRID_SIZE) return;

        // Check if textures are loaded
        if (tex == null || tex.terrainTileset == null) {
            System.out.println("Warning: Textures or terrainTileset not loaded for chunk " + cX + "," + cY);
            return;
        }

        // Render each tile
        for (Tile tile : chunk.values()) {
            tex.drawTileset(tex.terrainTileset, tile.getX(), tile.getY(), Constants.GRID_SIZE, Constants.GRID_SIZE, tile.tileX, tile.tileY, Constants.GRID_SIZE, batch);
        }
        
    }

    public void load(){

        if(loaded) return;

        loaded = true;

    }

    public void unload(){

        if(!loaded) return;

        loaded = false;

    }

    public int getX(){ return this.cX; }

    public int getY(){ return this.cY; }

    // Check loading
    public void checkIfOnScreen() {

        int chunkWorldSize = CHUNK_SIZE * Constants.GRID_SIZE;

        // Camera chunk coordinates (top-left of screen)
        int camChunkX = cam.x / chunkWorldSize;
        int camChunkY = cam.y / chunkWorldSize;

        int buffer = Constants.BUFFER; // Number of chunks beyond camera view

        // Determine visible chunk range
        // Determine visible chunk range
        int left   = Math.max(0, camChunkX - buffer);
        int right  = Math.min((Constants.MAP_WIDTH / chunkWorldSize) - 1, camChunkX + cam.RENDER_DISTANCE + buffer);
        int top    = Math.max(0, camChunkY - buffer);
        int bottom = Math.min((Constants.MAP_HEIGHT / chunkWorldSize) - 1, camChunkY + cam.RENDER_DISTANCE + buffer);

        // Load chunk if inside visible area
        loaded = (cX >= left && cX <= right && cY >= top && cY <= bottom);

    }
 
    // Tile collision
    public static Tile blockCollision(Entity e){

        for(Chunk c : Terrain.chunks.values()){
            for(Chunk.Tile t : c.chunk.values()){

                if(e.x() < t.x + Constants.GRID_SIZE && e.x() + e.width() > t.x && e.y() < t.y + Constants.GRID_SIZE && e.y() + e.height() > t.y) return t;

            }

        }

        return null;

    }

    // Unload resources
    public void cleanup() {

        // Clear local tile map
        if (chunk != null) chunk.clear();

        // Mark unloaded and remove references
        loaded = false;
        cam = null;
        tex = null;
        

    }

}
