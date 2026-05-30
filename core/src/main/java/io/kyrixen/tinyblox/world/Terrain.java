package io.kyrixen.tinyblox.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Terrain {

    // For passing the chunk size
    public final byte size;
    
    // Dimensions
    private int w;
    private int h;

    // Seed
    public static int seed;

    // Renderer
    private TileRenderer tileRenderer;

    // Noise generator
    private FastNoiseLite noise;

    // For storing chunks
    public static HashMap<ChunkPos, Chunk> chunks = new HashMap<>();

    // Constructs terrain
    public Terrain(int w, int h, TileRenderer tileRenderer, int seed, float frequency) {

        this.size = Constants.CHUNK_SIZE;

        Terrain.seed = seed;
        
        this.w = w;
        this.h = h;

        this.tileRenderer = tileRenderer;

        noise = new FastNoiseLite();

        // Sets noise generator properties
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetSeed(seed);
        noise.SetFrequency(frequency);
    
    }

    // Pre-generate chunks
    public void init() {

        Logger.LOGGER.debug("WORLD", "Seed: " + seed);

        chunks.clear();
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        for(short x = 0; x < chunkCountX; x++){

            for(short y = 0; y < chunkCountY; y++){

                Chunk c = new Chunk(x, y, size, true);
        
                //ChunkBuilder cb = new ChunkBuilder(x, y, size);
                
                //cb.fill(TileType.GRASS, (byte) 0);
                //cb.setTile((byte) 5, (byte) 9, TileType.AIR, false, (byte) 0);
                //cb.setTile((byte) 10, (byte) 0, TileType.WATER, true, (byte) 0);
                //cb.setTile((byte) 7, (byte) 10, TileType.DIRT, false, (byte) 0);
                //cb.setTile((byte) 5, (byte) 9, TileType.STONE, true, (byte) 0);
                
                ChunkGenerator.generate(c, noise);
                c.tryToSpawnTree(10);
                //c.set(cb.build());
                
                // Store chunk
                chunks.put(new ChunkPos(x, y), c);

                //cb.cleanup();

            }

        }

        Logger.LOGGER.debug("WORLD", "Chunks size: " + chunks.size());

    }

    // Render lower visible chunks
    public void renderLower(Player player, TimeCycle timeCycle, SpriteBatch batch) {
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));

                if (c == null) continue;

                // If not visible dont render
                if(!c.rendered) continue;

                c.renderLower(player, tileRenderer, timeCycle, batch);

            }

        }

    }

    // Render above visible chunks
    public void renderAbove(Player player, TimeCycle timeCycle, SpriteBatch batch) {
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        
        boolean tileAbovePlayer = false;

        TileStack playerTileStack = this.getWorldTileStack(player.x() / Constants.GRID_SIZE, player.y() / Constants.GRID_SIZE);
        if(playerTileStack != null) {
            Tile above = playerTileStack.get((byte) (player.level() + 1));
            tileAbovePlayer = above != null && above.type() != TileType.AIR;
        }


        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));

                if (c == null) continue;

                // If not visible dont render
                if(!c.rendered) continue;

                c.renderAbove(player, tileAbovePlayer, tileRenderer, timeCycle, batch);

            }

        }

    }

    

    // Render overlay for visible chunks
    public void renderDepthOverlay(Camera camera, Player player, TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch) {
        
        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;
        
        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));

                if (c == null) continue;

                // If not visible dont render
                if(!c.rendered) continue;
    
                c.renderDepthOverlay(camera, player, timeCycle, tileRenderer, batch);
    
            }
    
        }
    
    }

    // Find chunk if doesnt exist create chunk
    public Chunk getOrCreateChunk(short cX, short cY) {

        ChunkPos cPos = new ChunkPos(cX, cY);

        if(!chunks.containsKey(cPos)){

            Chunk c = new Chunk(cX, cY, size, true);
            ChunkGenerator.generate(c, noise);
            chunks.put(cPos, c);

            Logger.LOGGER.debug("WORLD", "Generated new chunk!");

        }

        return chunks.get(cPos);

    }

    // Find chunk
    public Chunk getChunk(short cX, short cY) {
        return chunks.get(new ChunkPos(cX, cY));
    }

    // Global TileStack accessor
    public TileStack getWorldTileStack(int worldX, int worldY) {

        short chunkX = (short) Math.floorDiv(worldX, this.size);
        short chunkY = (short) Math.floorDiv(worldY, this.size);

        Chunk chunk = this.getChunk(chunkX, chunkY);

        if(chunk == null) return null;

        byte localX = (byte) Math.floorMod(worldX, chunk.getChunkSize());
        byte localY = (byte) Math.floorMod(worldY, chunk.getChunkSize());

        return chunk.getTileStack(localX, localY);

    }

    // Get world height at cordinates
    public byte getWorldLevel(int worldX, int worldY) {

        TileStack stack = getWorldTileStack(worldX, worldY);

        if(stack == null) return 0;

        Tile top = stack.top();

        if(top == null) return 0;

        return top.level();
    
    }

    // Draw edges on different heights
    public void drawHeightEdges(Camera cam, ShapeRenderer  shapeRenderer) {

        shapeRenderer.setColor(0f, 0f, 0f, 1f);

        int startX = (int) (cam.x / Constants.GRID_SIZE);
        int startY = (int) (cam.y / Constants.GRID_SIZE);
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);

        int endX = (int) (startX + Constants.WINDOW_WIDTH / (Constants.GRID_SIZE * cam.zoom)) + 2;
        int endY = (int) (startY + Constants.WINDOW_HEIGHT / (Constants.GRID_SIZE * cam.zoom)) + 2;
        endX = Math.min(this.w, endX);
        endY = Math.min(this.h, endY);

        float tileSize = Constants.GRID_SIZE * cam.zoom;

        for(int worldX = startX; worldX < endX; worldX++){

            for(int worldY = startY; worldY < endY; worldY++){

                byte current = this.getWorldLevel(worldX, worldY);

                if(current <= 0) continue;

                byte left = this.getWorldLevel(worldX - 1, worldY);
                byte right = this.getWorldLevel(worldX + 1, worldY);
                byte top = this.getWorldLevel(worldX, worldY + 1);
                byte bottom = this.getWorldLevel(worldX, worldY - 1);
        
                if(current == left && current == right && current == top && current == bottom) continue;

                int tileX = worldX * Constants.GRID_SIZE;
                int tileY = worldY * Constants.GRID_SIZE;

                float screenX = (tileX - cam.x) * cam.zoom;
                float screenY = (tileY - cam.y) * cam.zoom;
 
                if(left < current) shapeRenderer.line(screenX, screenY, screenX, screenY + tileSize);
                if(right < current) shapeRenderer.line(screenX + tileSize, screenY, screenX + tileSize, screenY + tileSize);
                if(top < current) shapeRenderer.line(screenX, screenY + tileSize, screenX + tileSize, screenY + tileSize);
                if(bottom < current) shapeRenderer.line(screenX, screenY, screenX + tileSize, screenY);
            
            }

        }   

    }

    // Update terrain
    public void update(Camera camera) {

        int chunkCountX = (w + size - 1) / size;
        int chunkCountY = (h + size - 1) / size;

        for(short cx = 0; cx < chunkCountX; cx++){

            for(short cy = 0; cy < chunkCountY; cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;
                c.checkIfOnScreen(camera);

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
        
    }

}
