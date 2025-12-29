package org.kyrixen;


import java.util.HashMap;
import fastnoiselite.FastNoiseLite;


public class Terrain {
    
    private int size;
    private Textures tex;
    private int w;
    private int h;
    private Camera cam;

    private final FastNoiseLite noise;

    HashMap<String, Chunk> chunks = new HashMap<>();


    public Terrain(int w, int h, int size, Textures texture, Camera camera) {
        
        this.size = size;
        this.tex = texture;
        this.cam = camera;
        this.w = w;
        this.h = h;

        noise = new FastNoiseLite();
    
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.05f);
    
    }


    public void init() {
        
        for(int x = 0; x < w / size; x++){

            for(int y = 0; y < h / size; y++){

                Chunk c = new Chunk(x, y, size, true, tex, cam);
                
                c.generate(noise);
                chunks.put(generateKey(x, y), c);

            }

        }

    }


    public void render() {
        
        for(int cx = 0; cx < w / size; cx++){

            for(int cy = 0; cy < h / size; cy++){

                Chunk c = chunks.get(generateKey(cx, cy));

                if(!c.loaded) continue;

                c.render();

            }

        }

    }


    public int getChunkSize() {
        return size;
    }


    public String generateKey(int cX, int cY){
        return Integer.toString(cX) + "," + Integer.toString(cY);
    }


    public Chunk getChunk(int cX, int cY){

        String key = generateKey(cX, cY);

        if(!chunks.containsKey(key)){

            Chunk c = new Chunk(cX, cY, size, true, tex, cam);
            c.generate(noise);
            chunks.put(key, c);

        }

        return chunks.get(key);

    }


    public void update(){

        for(int cx = 0; cx < w / size; cx++){

            for(int cy = 0; cy < h / size; cy++){

                Chunk c = chunks.get(generateKey(cx, cy));
                c.checkIfOnScreen();

            }

        }        

    }


    public void separate(Entity e) {

        int chunkSize = getChunkSize(); // tiles per chunk
        int chunkWorldSize = chunkSize * Constants.GRID_SIZE;

        // Determine which chunks the entity overlaps
        int startChunkX = e.x / chunkWorldSize;
        int endChunkX   = (e.x + e.width) / chunkWorldSize;
        int startChunkY = e.y / chunkWorldSize;
        int endChunkY   = (e.y + e.height) / chunkWorldSize;

        
        for (int cx = startChunkX; cx <= endChunkX; cx++) {
    
            for (int cy = startChunkY; cy <= endChunkY; cy++) {
    
                Chunk c = getChunk(cx, cy);
    
                if (c != null && c.loaded) {
    
                    for (Chunk.Tile tile : c.chunk.values()) {
    
                        if (!tile.solid()) continue; // skip non-solid

                        // Calculate overlap on X axis
                        int overlapX = Math.min(e.x + e.width - tile.x(), tile.x() + Constants.GRID_SIZE - e.x);
                        int overlapY = Math.min(e.y + e.height - tile.y(), tile.y() + Constants.GRID_SIZE - e.y);

                        if (overlapX > 0 && overlapY > 0) { // collision detected
    
                            // Resolve the smallest axis first
                            if (overlapX < overlapY) {
                                if (e.x + e.width / 2 < tile.x() + Constants.GRID_SIZE / 2)
                                    e.x -= overlapX; // move left
                                else
                                    e.x += overlapX; // move right
                            } else {
                                if (e.y + e.height / 2 < tile.y() + Constants.GRID_SIZE / 2)
                                    e.y -= overlapY; // move up
                                else
                                    e.y += overlapY; // move down
                           }
    
                        }
    
                    }
    
                }
    
            }
    
        }
    
    }





}
