package io.kyrixen.tinyblox.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
import io.kyrixen.tinyblox.world.chunk.ChunkRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Terrain {

    // Dimensions
    private final int w;
    private final int h;

    // Seed
    private final int seed;

    // Renderer
    private final ChunkRenderer chunkRenderer;

    // Noise generator
    private final FastNoiseLite noise;

    // For storing chunks
    private final HashMap<ChunkPos, Chunk> chunks = new HashMap<>();


    // Constructs terrain
    public Terrain(int w, int h, TileRenderer tileRenderer, int seed, float frequency) {

        this.seed = seed;
        
        this.w = w;
        this.h = h;

        this.chunkRenderer = new ChunkRenderer(tileRenderer);

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

        for(short x = 0; x < getChunkCountX(); x++){
            for(short y = 0; y < getChunkCountY(); y++){

                Chunk c = new Chunk(x, y, seed);
                
                ChunkGenerator.generateChunk(c, noise);
                ChunkGenerator.generateCave(c);
                ChunkGenerator.spawnOre(c, TileType.COAL, 2);
                ChunkGenerator.spawnOre(c, TileType.IRON, 1);
                ChunkGenerator.spawnTree(c, 10);
                ChunkGenerator.spawnStructure(c, 10);
                

                // Store chunk
                chunks.put(new ChunkPos(x, y), c);

            }
        }

        Logger.LOGGER.debug("WORLD", "Chunks size: " + chunks.size());

    }

    // Update terrain
    public void update(Camera camera) {

        for(short cx = 0; cx < getChunkCountX(); cx++){

            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;
                c.checkIfOnScreen(camera);

            }

        }        

    }


    // Render lower visible chunks
    public void renderLower(Player player, RendererStack rendererStack) {
        
        for(short cx = 0; cx < getChunkCountX(); cx++){
            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;

                chunkRenderer.renderLower(c, player, rendererStack);

            }
        }

    }

    // Render above visible chunks
    public void renderAbove(Player player, RendererStack rendererStack) {
        
        boolean tileAbovePlayer = false;

        TileStack playerTileStack = this.getWorldTileStack(player.x() / Constants.GRID_SIZE, player.y() / Constants.GRID_SIZE);
        if(playerTileStack != null) {
        
            for(byte level = 0; level < playerTileStack.stackSize(); level++) {

                Tile current = playerTileStack.get(level);
                if(current == null || current.type().isEmpty()) continue;

                if(player.level() < current.level()) tileAbovePlayer = true;

            }
       
        }


        for(short cx = 0; cx < getChunkCountX(); cx++){
            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;

                chunkRenderer.renderAbove(c, player, tileAbovePlayer, rendererStack);

            }
        }

    }


    // Render overlay for visible chunks
    public void renderDepthOverlay(Player player, TimeCycle timeCycle, RendererStack rendererStack) {
        
        for(short cx = 0; cx < getChunkCountX(); cx++){

            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;

                chunkRenderer.renderDepthOverlay(c, player, timeCycle, rendererStack);
    
            }
    
        }
    
    }

    // Draw edges on different heights
    public void drawHeightEdges(Player player, RendererStack rendererStack) {  

        for(short cx = 0; cx < getChunkCountX(); cx++){
            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;

                chunkRenderer.drawHeightEdges(c, this, rendererStack);
                chunkRenderer.drawCurrentHeightEdges(c, player, this, rendererStack);

            }
        }

    }


    // Update chunk light
    public void rebuildLighting(TimeCycle timeCycle) {
        
        for(short cx = 0; cx < getChunkCountX(); cx++){
            for(short cy = 0; cy < getChunkCountY(); cy++){

                Chunk c = chunks.get(new ChunkPos(cx, cy));
                if (c == null) continue;

                // If not visible dont render
                if(!c.rendered) continue;

                c.resetAmbientLighting(timeCycle.getBrightnessColor());

                for(byte localX = 0; localX < c.get().length; localX++) {
                    for(byte localY = 0; localY < c.get().length; localY++) {

                        TileStack tileStack = c.getTileStack(localX, localY);
                        if(tileStack == null) continue;

                        for(byte level = 0; level < tileStack.height(); level++) {
                        
                            Tile current = tileStack.get(level);
                            if(current == null || current.type().getLightLevel() <= 0f) continue;

                            int worldX = cx * Constants.CHUNK_SIZE + localX;
                            int worldY = cy * Constants.CHUNK_SIZE + localY;

                            applyRadialLight(worldX, worldY, current.level(), current.type().getLightLevel());
                            
                        }
                    }
                }
        
            }
        }

    }

    // Applies lighting
    private void applyRadialLight(int worldX, int worldY, byte worldLayer, float lightLevel) {

        for(byte tx = -Constants.LIGHT_RADIUS; tx <= Constants.LIGHT_RADIUS; tx++) {
            for(byte ty = -Constants.LIGHT_RADIUS; ty <= Constants.LIGHT_RADIUS; ty++) {
                for(byte tz = -Constants.LIGHT_RADIUS; tz <= Constants.LIGHT_RADIUS; tz++) {
                
                    int targetWorldX = worldX + tx;
                    int targetWorldY = worldY + ty;
                    byte targetWorldLayer = (byte) (worldLayer + tz);
                    
                    if(targetWorldLayer < Constants.MIN_WORLD_HEIGHT) continue;
                    if(targetWorldLayer > Constants.MAX_WORLD_HEIGHT) continue;

                    short chunkX = (short) Math.floorDiv(targetWorldX, Constants.CHUNK_SIZE);
                    short chunkY = (short) Math.floorDiv(targetWorldY, Constants.CHUNK_SIZE);

                    Chunk c = this.getChunk(chunkX, chunkY);
                    if(c == null) continue;


                    int localLightX = Math.floorMod(targetWorldX, Constants.CHUNK_SIZE);
                    int localLightY = Math.floorMod(targetWorldY, Constants.CHUNK_SIZE);

                    float sourceDist = Vector3.len(tx, ty, tz * 3);
                    if(sourceDist > Constants.LIGHT_RADIUS) continue;

                    float lowerance = 1f - (sourceDist / Constants.LIGHT_RADIUS);
                    lowerance = Math.max(0f, lowerance);

                    Color lightColor = c.getLight((byte) localLightX, (byte) localLightY, targetWorldLayer);


                    lightColor.r += 1f * lowerance * lightLevel;
                    lightColor.g += 0.8f * lowerance * lightLevel;
                    lightColor.b += 0.5f * lowerance * lightLevel;

                    lightColor.r = Math.min(lightColor.r, 1.25f);
                    lightColor.g = Math.min(lightColor.g, 1.25f);
                    lightColor.b = Math.min(lightColor.b, 1.25f);
                
                }
            }
        }

    }
    

    // Find chunk
    public Chunk getChunk(short cX, short cY) {
        return chunks.get(new ChunkPos(cX, cY));
    }
    
    // Find chunk if doesnt exist create chunk
    public Chunk getOrCreateChunk(short cX, short cY) {

        ChunkPos cPos = new ChunkPos(cX, cY);

        if(!chunks.containsKey(cPos)){

            Chunk c = new Chunk(cX, cY, seed);
            ChunkGenerator.generateChunk(c, noise);
            chunks.put(cPos, c);

            Logger.LOGGER.debug("WORLD", "Generated new chunk!");

        }

        return chunks.get(cPos);

    }


    // Global TileStack accessor
    public TileStack getWorldTileStack(int worldX, int worldY) {

        short chunkX = (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE);

        Chunk chunk = this.getChunk(chunkX, chunkY);

        if(chunk == null) return null;

        byte localX = (byte) Math.floorMod(worldX, Constants.CHUNK_SIZE);
        byte localY = (byte) Math.floorMod(worldY, Constants.CHUNK_SIZE);

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

    // Get visible level
    public byte getVisibleLevel(int worldX, int worldY, byte visibleLevel) {

        TileStack stack = getWorldTileStack(worldX, worldY);
        if(stack == null) return getWorldLevel(worldX, worldY);

        for(byte level = visibleLevel; level >= Constants.MIN_WORLD_HEIGHT; level--) {

            Tile tile = stack.get(level);

            if(tile == null) continue;
            if(tile.type().isEmpty()) continue;

            return level;

        }

        return getWorldLevel(worldX, worldY);
    
    }

    // Get world light color at cordinates
    public Color getLightColor(int worldX, int worldY, byte worldLayer) {

        short chunkX = (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE);

        byte localX = (byte) Math.floorMod(worldX, Constants.CHUNK_SIZE);
        byte localY = (byte) Math.floorMod(worldY, Constants.CHUNK_SIZE);

        Chunk c = getChunk(chunkX, chunkY);
        if(c == null) return Color.WHITE;

        return c.getLight(localX, localY, worldLayer);

    }


    // Get seed
    public int getSeed() { return this.seed; }
    
    // Get chunk map
    public HashMap<ChunkPos, Chunk> getChunks() { return this.chunks; }

    // Chunk count helpers
    private int getChunkCountX() { return (w + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE; }
    private int getChunkCountY() { return (h + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE; }


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
