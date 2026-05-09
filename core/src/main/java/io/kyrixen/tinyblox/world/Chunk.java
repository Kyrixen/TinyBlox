package io.kyrixen.tinyblox.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.Chunk.Tile.TileType;
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
    public boolean rendered;
    public boolean modified;

    // Textures helper
    private Textures tex;

    // Stores chunk tiles
    private Tile[][] chunk;

    // Tile class
    public static class Tile {

        // Tile type enum
        public static enum TileType {
        
            AIR,
            GRASS,
            DIRT,
            WATER,
            STONE
        
        }

        // Texture atlas coords
        int tileX, tileY;

        // Tile data
        TileType type;

        // Terrain height
        byte height;

        // Collision
        boolean solid;

        // Constructs tile
        public Tile(TileType type, byte height) {

            this.type = type;

            this.tileX = getTileX(type);
            this.tileY = getTileY(type);
            
            this.height = height;

            this.solid = this.height >= 1;

        }

        // Map tileX to tileset via type
        private static int getTileX(TileType type) {

            switch (type) {
                case GRASS: return 1;
                case STONE: return 0;
                case DIRT : return 0;
                case WATER: return 1;
                case AIR  : return 0;
                default   : return 0;
            }

        }


        // Map tileY to tileset via type
        private static int getTileY(TileType type) {
        
            switch (type) {
                case GRASS: return 0;
                case STONE: return 1;
                case DIRT : return 0;
                case WATER: return 1;
                case AIR  : return 2;
                default   : return 0;
            }
        
        }
        
        // Helper functions //

        public boolean solid(){ return solid; }

        public TileType type() { return type; }

        public byte height() { return height; }

        public void updateSolid() {
            this.solid = this.height >= 1;
            if(this.type == TileType.AIR) solid = true;
        }

        @Override
        public String toString() {
            return "Tile{ solid=" + solid + ", type=" + type + ", height=" + height + " }";
        }

    }

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

    // Generate the chunk
    public void generate(FastNoiseLite noise) {

        chunk = new Tile[CHUNK_SIZE][CHUNK_SIZE];

        // World size in tiles
        int worldTilesX = Constants.MAP_WIDTH;
        int worldTilesY = Constants.MAP_HEIGHT;

        // World size in chunks
        int worldChunksX = (worldTilesX + CHUNK_SIZE - 1) / CHUNK_SIZE;
        int worldChunksY = (worldTilesY + CHUNK_SIZE - 1) / CHUNK_SIZE;

        // Safety: do not generate invalid chunks
        if (cX < 0 || cY < 0 || cX >= worldChunksX || cY >= worldChunksY) {
            loaded = false;
            rendered = false;
            return;
        }

        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {

                // Tile position in WORLD TILE coordinates
                int tileX = cX * CHUNK_SIZE + tx;
                int tileY = cY * CHUNK_SIZE + ty;

                // Skip tiles outside world tile bounds
                if (tileX < 0 || tileY < 0 ||
                    tileX >= worldTilesX || tileY >= worldTilesY) {
                    continue;
                }

                float t = noise.GetNoise(tileX, tileY);

                float height = (t + 1f) / 2f;
                TileType type;

                if (height < 0.30f)      type = TileType.WATER;
                else if (height < 0.55f) type = TileType.DIRT;
                else if (height < 0.80f) type = TileType.GRASS;
                else                     type = TileType.STONE;

                byte layer;
                switch(type) {

                    case AIR:
                        layer = -1;
                        break;

                    case STONE:
                        layer = 1;
                        break;

                    default:
                        layer = 0;
                        break;

                }                
                
                Tile tile = new Tile(type, (byte) layer);
                this.setTile(tx, ty, tile);

            }

        }

        loaded = true;
        rendered = loaded;

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
 
    // Tile collision
    public static Tile blockCollision(Entity e){

        for(Chunk c : Terrain.chunks.values()){
            for (byte tx = 0; tx < c.CHUNK_SIZE; tx++) {
                for (byte ty = 0; ty < c.CHUNK_SIZE; ty++) {

                    Chunk.Tile t = c.getTile(tx, ty);
                    if(t == null) continue;

                    int globalX = (c.getX() * c.CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                    int globalY = (c.getY() * c.CHUNK_SIZE + ty) * Constants.GRID_SIZE;
                    
                    if(e.x() < globalX + Constants.GRID_SIZE && e.x() + e.width() > globalX && e.y() < globalY + Constants.GRID_SIZE && e.y() + e.height() > globalY) return t;
                
                }
            }

        }

        return null;

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
