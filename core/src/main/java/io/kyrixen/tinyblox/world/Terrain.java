package io.kyrixen.tinyblox.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
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
                
                ChunkGenerator.generate(c, noise);
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

    // Find chunk
    public Chunk getChunk(short cX, short cY){

        ChunkPos cPos = new ChunkPos(cX, cY);

        if(!chunks.containsKey(cPos)){

            Chunk c = new Chunk(cX, cY, size, true, tex, cam);
            ChunkGenerator.generate(c, noise);
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

}
