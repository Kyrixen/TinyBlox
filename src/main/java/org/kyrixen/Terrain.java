package org.kyrixen;


import java.awt.Graphics2D;
import java.util.HashMap;

import fastnoiselite.FastNoiseLite;


public class Terrain {

    // For passing the chunk_size
    private int size;
    
    // Helper texture
    private Textures tex;
    
    // Dimensions
    private int w;
    private int h;

    // Camera helper
    private Camera cam;

    // Noise generator
    private FastNoiseLite noise;

    // For storing chunks
    public static HashMap<String, Chunk> chunks = new HashMap<>();

    // Constructs terrain
    public Terrain(int w, int h, int size, Textures texture, Camera camera, int seed, float frequency, boolean multiplayer) {
        
        this.size = size;

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
        
        for(int x = 0; x < w / size; x++){

            for(int y = 0; y < h / size; y++){

                Chunk c = new Chunk(x, y, size, true, tex, cam);
                
                c.generate(noise);

                // Store chunk
                chunks.put(generateKey(x, y), c);

            }

        }

        System.out.println("Chunks size: " + chunks.size());

    }

    // Render visible chunks
    public void render(Graphics2D g) {
        
        for(int cx = 0; cx < w / size; cx++){

            for(int cy = 0; cy < h / size; cy++){

                Chunk c = chunks.get(generateKey(cx, cy));

                // If isnt loaded dont render
                if(!c.loaded) continue;

                c.render(g);

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
    public Chunk getChunk(int cX, int cY){

        String key = generateKey(cX, cY);

        if(!chunks.containsKey(key)){

            Chunk c = new Chunk(cX, cY, size, true, tex, cam);
            c.generate(noise);
            chunks.put(key, c);

        }

        return chunks.get(key);

    }

    // Update terrain
    public void update(){

        for(int cx = 0; cx < w / size; cx++){

            for(int cy = 0; cy < h / size; cy++){

                Chunk c = chunks.get(generateKey(cx, cy));
                c.checkIfOnScreen();

            }

        }        

    }

    // Helper for Entity.java
    public void tryMove(Entity e, Terrain terrain) {

        if (e.dirX == 0 && e.dirY == 0) return;

        int tileSize = Constants.GRID_SIZE;
        int nextX = e.x + e.dirX * tileSize;
        int nextY = e.y + e.dirY * tileSize;

        // World bounds check
        if (nextX < 0 || nextY < 0 ||
            nextX + e.width > Constants.MAP_WIDTH ||
            nextY + e.height > Constants.MAP_HEIGHT) return;

        // Determine chunk range the entity could touch
        int chunkSize = terrain.getChunkSize();
        int startChunkX = nextX / (chunkSize * tileSize);
        int startChunkY = nextY / (chunkSize * tileSize);
        int endChunkX   = (nextX + e.width)  / (chunkSize * tileSize);
        int endChunkY   = (nextY + e.height) / (chunkSize * tileSize);

        // Iterate over relevant chunks
        for (int cx = startChunkX; cx <= endChunkX; cx++) {
            for (int cy = startChunkY; cy <= endChunkY; cy++) {

                Chunk c = terrain.getChunk(cx, cy);
                if (!c.loaded) continue;

                // Check each tile in the chunk
                for (Chunk.Tile tile : c.chunk.values()) {

                    if (!tile.solid()) continue;

                    int tx = tile.getX();
                    int ty = tile.getY();

                    // AABB collision
                    if (nextX < tx + tileSize &&
                        nextX + e.width > tx &&
                        nextY < ty + tileSize &&
                        nextY + e.height > ty) {
                        return; // Blocked
                    }
                }
            }
        }

        // No collision, move allowed
        e.x = nextX;
        e.y = nextY;
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
        size = 0;

        System.gc(); // Help GC

    }

}
