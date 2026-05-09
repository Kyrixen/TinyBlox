package io.kyrixen.tinyblox.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.Chunk.Tile;
import io.kyrixen.tinyblox.world.fastnoiselite.FastNoiseLite;

public class Terrain {

    // For passing the chunk size
    public final byte size;
    
    // Helper texture
    private Textures tex;
    
    // Dimensions
    private int w;
    private int h;

    // Seed
    public static int seed;

    // Camera helper
    private Camera cam;

    // Noise generator
    private FastNoiseLite noise;

    // For storing chunks
    public static HashMap<ChunkPos, Chunk> chunks = new HashMap<>();

    // Constructs terrain
    public Terrain(int w, int h, byte size, Textures texture, Camera camera, int seed, float frequency, boolean multiplayer) {
        
        this.size = size;

        Terrain.seed = seed;

        this.tex = texture;
        this.cam = camera;
        
        this.w = w;
        this.h = h;

        noise = new FastNoiseLite();

        // Sets noise generator properties
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetSeed(seed);
        noise.SetFrequency(frequency);
    
    }

    // Pre-generate chunks
    public void init() {

        System.out.println("Seed: " + seed);

        chunks.clear();
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        for(short x = 0; x < chunkCountX; x++){

            for(short y = 0; y < chunkCountY; y++){

                Chunk c = new Chunk(x, y, size, true, tex, cam);
        
                //ChunkBuilder cb = new ChunkBuilder(x, y, size);
                
                //cb.fill(TileType.GRASS, (byte) 0);
                //cb.setTile((byte) 5, (byte) 9, TileType.AIR, false, (byte) 0);
                //cb.setTile((byte) 10, (byte) 0, TileType.WATER, true, (byte) 0);
                //cb.setTile((byte) 7, (byte) 10, TileType.DIRT, false, (byte) 0);
                //cb.setTile((byte) 5, (byte) 9, TileType.STONE, true, (byte) 0);
                
                c.generate(noise);
                //c.set(cb.build());
                
                // Store chunk
                chunks.put(new ChunkPos(x, y), c);

                //cb.cleanup();

            }

        }

        System.out.println("Chunks size: " + chunks.size());

    }

    // Render visible chunks
    public void render(SpriteBatch batch) {
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));

                if (c == null) continue;

                // If not visible dont render
                if(!c.rendered) continue;

                c.render(batch);

            }

        }

    }

    public int getChunkSize() {
        return size;
    }

    public String generateKey(int cX, int cY){
        return Integer.toString(cX) + "," + Integer.toString(cY);
    }

    // Find chunk
    public Chunk getChunk(short cX, short cY){

        ChunkPos cPos = new ChunkPos(cX, cY);

        if(!chunks.containsKey(cPos)){

            Chunk c = new Chunk(cX, cY, size, true, tex, cam);
            c.generate(noise);
            chunks.put(cPos, c);

            System.out.println("Generated new chunk!");

        }

        return chunks.get(cPos);

    }

    // Update terrain
    public void update(){
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;

        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;
                c.checkIfOnScreen();

            }

        }        

    }

    // Helper for Entity.java
    public void tryMove(Entity e, Terrain terrain) {

        if (e.dirX() == 0 && e.dirY() == 0) return;

        int tileSize = Constants.GRID_SIZE;

        int nextX = e.x() + e.dirX() * tileSize;
        int nextY = e.y() + e.dirY() * tileSize;

        // World bounds check in pixels (map size is stored in tiles)
        int worldPixelWidth = Constants.MAP_WIDTH * tileSize;
        int worldPixelHeight = Constants.MAP_HEIGHT * tileSize;
        if (nextX < 0 || nextY < 0 || nextX + e.width() > worldPixelWidth || nextY + e.height() > worldPixelHeight) return;

        // Determine chunk range the entity could touch
        int chunkSize = terrain.getChunkSize();

        short startChunkX = (short) (nextX / (chunkSize * tileSize));
        short startChunkY = (short) (nextY / (chunkSize * tileSize));
        int endChunkX   = (nextX + e.width() - 1)  / (chunkSize * tileSize);
        int endChunkY   = (nextY + e.height() - 1) / (chunkSize * tileSize);

        // Iterate over relevant chunks
        for (short cx = startChunkX; cx <= endChunkX; cx++) {
            for (short cy = startChunkY; cy <= endChunkY; cy++) {

                Chunk c = terrain.getChunk(cx, cy);

                if (!c.loaded) continue;

                // Check each tile in the chunk
                for (byte localX = 0; localX < c.CHUNK_SIZE; localX++) {
                    for (byte localY = 0; localY < c.CHUNK_SIZE; localY++) {
                        
                        Tile tile = c.chunk[localX][localY];

                        if (tile == null) continue;
                        if (!tile.solid()) continue;

                        int tx = tile.getX();
                        int ty = tile.getY();

                        // AABB collision
                        if (nextX < tx + tileSize &&
                            nextX + e.width() > tx &&
                            nextY < ty + tileSize &&
                            nextY + e.height() > ty) {
                            return; // Blocked
                        }
                
                    }
                
                }
            
            }
        
        }

        // No collision, move allowed
        e.setX(nextX);
        e.setY(nextY);
    
    }

    // Unload resources
    public void cleanup() {

        // Cleanup all chunks
        if (chunks != null) {
        
            for (Chunk c : chunks.values()) {
                if (c != null) c.cleanup();
            }
        
            chunks.clear();
        
        }

        tex = null;
        cam = null;
        noise = null;

        w = 0;
        h = 0;

        System.gc(); // Help GC

    }

    // Chunk position helper
    public static class ChunkPos {
    
        private final short chunkX;
        private final short chunkY;
        
        public ChunkPos(short cx, short cy) {
            this.chunkX = cx;
            this.chunkY = cy;
        }

        public short getChunkX() {
            return this.chunkX;
        }

        public short getChunkY() {
            return this.chunkY;
        }

        @Override
        public boolean equals(Object o) {
        
            if(this == o) return true;
            if(!(o instanceof ChunkPos)) return false;

            ChunkPos cP = (ChunkPos) o;

            return this.chunkX == cP.chunkX && this.chunkY == cP.chunkY;

        }

        @Override
        public int hashCode() {
            return 31 * chunkX + chunkY;
        }

    }

}
