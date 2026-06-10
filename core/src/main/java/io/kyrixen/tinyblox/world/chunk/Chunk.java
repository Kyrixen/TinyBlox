package io.kyrixen.tinyblox.world.chunk;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer.FlipType;

public class Chunk {

    // Chunk seed
    private final long chunkSeed;

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

    // Chunk properties
    public boolean loaded;
    public boolean rendered;
    public boolean modified;

    // Stores chunk tiles
    private TileStack[][] chunk;

    // Stores light level for tiles
    private Color[][] lightLevel;


    private final TextureID terrainTileset = new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles");

    // Construct chunk
    public Chunk(int x, int y, int size, int seed, boolean loaded){

        this.chunkSeed = RandomUtils.mixSeed(seed, x * 341873128712L ^ y * 132897987541L);

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        this.chunk = new TileStack[CHUNK_SIZE][CHUNK_SIZE];
        this.lightLevel = new Color[CHUNK_SIZE][CHUNK_SIZE];

        for(byte xPos = 0; xPos < CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < CHUNK_SIZE; yPos++) {
                this.chunk[xPos][yPos] = new TileStack();
                this.lightLevel[xPos][yPos] = new Color(1f, 1f, 1f, 1f);
            }
        }
        
        this.modified = false;
        this.loaded = loaded;
        this.rendered = loaded;
        
    }

    // Render above chunk
    public void renderAbove(Player player, boolean tileAbovePlayer, TileRenderer tileRenderer, RendererStack rendererStack) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

        SpriteBatch batch = rendererStack.batch;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                TileStack tileStack = this.getTileStack(tx, ty);
                if(tileStack == null || tileStack.isEmpty()) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                float tileCenterX = globalX + Constants.GRID_SIZE / 2f;
                float tileCenterY = globalY + Constants.GRID_SIZE / 2f;
                float playerCenterX = player.x() + player.width() / 2f;
                float playerCenterY = player.y() + player.height() / 2f;

                Color light = this.getLight(tx, ty);

                float distX = tileCenterX - playerCenterX;
                float distY = tileCenterY - playerCenterY;
                int dist = (int) Vector2.len(distX, distY);
                
                float revealRadius = Constants.ROOF_REVEAL_RADIUS * Constants.GRID_SIZE;

                List<Tile> transparentTiles = new ArrayList<>();
                for(byte layer = (byte) (tileStack.stackSize() - 1); layer > Constants.MIN_WORLD_HEIGHT; layer--) {

                    Tile stackedTile = tileStack.get(layer);

                    if(stackedTile == null) continue;
                    if(stackedTile.level() <= player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;

                    if(stackedTile.type().isTransparent()) { transparentTiles.add(stackedTile); continue; }

                    // Draw first visible opaque tile
                    batch.setColor(light.r, light.g, light.b, 1f);

                    int levelDiff = stackedTile.level() - player.level();

                    if(tileAbovePlayer && levelDiff > 0 && dist <= revealRadius) {

                        float alpha = dist / revealRadius;

                        alpha = MathUtils.clamp(alpha, 0f, 1f);
                        alpha = alpha * alpha * (3f - 2f * alpha);
                        alpha = MathUtils.clamp(alpha, 0.25f, 0.65f);

                        batch.setColor(light.r, light.g, light.b, alpha);

                    }

                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);

                    // Draw transparent tile on top
                    for(int i = transparentTiles.size() - 1; i >= 0; i--) {
                        tileRenderer.drawTileset(terrainTileset, globalX, globalY, transparentTiles.get(i).tileX(), transparentTiles.get(i).tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                    }

                    batch.setColor(1f, 1f, 1f, 1f);

                    break;
                    
                }
            
            }

        }
        
    }


    // Render lower chunk
    public void renderLower(Player player, TileRenderer tileRenderer, RendererStack rendererStack) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

        SpriteBatch batch = rendererStack.batch;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                TileStack tileStack = this.getTileStack(tx, ty);
                if(tileStack == null || tileStack.isEmpty()) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                Color light = this.getLight(tx, ty);

                List<Tile> transparentTiles = new ArrayList<>();
                for(byte layer = (byte) (tileStack.stackSize() - 1); layer > Constants.MIN_WORLD_HEIGHT; layer--) {

                    Tile stackedTile = tileStack.get(layer);

                    if(stackedTile == null) continue;
                    if(stackedTile.level() > player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;

                    if(stackedTile.type().isTransparent()) { transparentTiles.add(stackedTile); continue; }

                    // Draw first visible opaque tile
                    batch.setColor(light.r, light.g, light.b, 1f);
                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);

                    // Draw transparent tile on top
                    for(int i = transparentTiles.size() - 1; i >= 0; i--) {
                        tileRenderer.drawTileset(terrainTileset, globalX, globalY, transparentTiles.get(i).tileX(), transparentTiles.get(i).tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                    }
                    
                    batch.setColor(1f, 1f, 1f, 1f);

                    break;
                    
                }
            
            }

        }
        
    }

    // Render depth for the top tile
    public void renderDepthOverlay(Player player, TimeCycle timeCycle, TileRenderer tileRenderer, RendererStack rendererStack) {

        // Check if can render overlay for chunk
        if (!loaded || !rendered) return;

        SpriteBatch batch = rendererStack.batch;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        float lightBrightness = timeCycle.getBrightness();

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                Tile tile = this.getTileStack(tx, ty).top();

                if(tile == null) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                int levelDiff = Math.abs(tile.level() - player.level());
                float normalized = (float) levelDiff / (Constants.MAX_TERRAIN_HEIGHT - Constants.MIN_TERRAIN_HEIGHT);
                
                float alpha = normalized * 0.65f;
                alpha = Math.min(alpha, 0.55f);

                if(tile.level() > player.level()) batch.setColor(0.97f, 0.97f, 0.97f, alpha * lightBrightness);
                else if(tile.level() < player.level()) batch.setColor(0.15f, 0.15f, 0.15f, alpha * lightBrightness);
                else batch.setColor(1f, 1f, 1f, 0f);
                
                tileRenderer.drawTilesetOutline(terrainTileset, globalX, globalY, tile.tileX(), tile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                
                batch.setColor(1f, 1f, 1f, 1f);
            
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

    public long getChunkSeed() { return this.chunkSeed; }


    // Reset default lighting
    public void resetAmbientLighting(Color ambient) {

        for(byte xPos = 0; xPos < lightLevel.length; xPos++) {
            for(byte yPos = 0; yPos < lightLevel.length; yPos++) {
            
                this.setLight(xPos, yPos, ambient);

            }
        }

    }

    // Check loading
    public void checkIfOnScreen(Camera cam) {

        int chunkWorldSize = CHUNK_SIZE * Constants.GRID_SIZE;
        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);

        int camCenterX = cam.x + (int) (Constants.WINDOW_WIDTH / (2f * cam.zoom));
        int camCenterY = cam.y + (int) (Constants.WINDOW_HEIGHT / (2f * cam.zoom));

        // Camera chunk coordinates (top-left of screen)
        int camChunkX = camCenterX / chunkWorldSize;
        int camChunkY = camCenterY / chunkWorldSize;

        int buffer = Constants.BUFFER; // Number of chunks beyond camera view

        // Determine rendered chunk range
        int left   = Math.max(0, camChunkX - cam.RENDER_DISTANCE - buffer);
        int right  = Math.min(worldChunksX - 1, camChunkX + cam.RENDER_DISTANCE + buffer);
        int top    = Math.max(0, camChunkY - cam.RENDER_DISTANCE - buffer);
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

        for(byte localX = 0; localX < this.CHUNK_SIZE; localX++) {
            for(byte localY = 0; localY < this.CHUNK_SIZE; localY++) {

                TileStack sourceStack = tiles[localX][localY];

                TileStack currentStack = this.getTileStack(localX, localY);

                if(sourceStack == null) continue;

                for(byte level = 0; level < sourceStack.stackSize(); level++) {

                    Tile tile = sourceStack.get(level);

                    if(tile == null) continue;

                    currentStack.set(tile, level);

                }

            }
        }
    
        modified = true;
        loaded = true;
        rendered = loaded;
    
    }

    public TileStack getTileStack(byte localX, byte localY) {
        if(localX < 0 || localX >= this.getChunkSize() || localY < 0 || localY >= this.getChunkSize()) return null;
        return this.chunk[localX][localY];
    }

    public Color getLight(byte xPos, byte yPos) {
        if(xPos < 0 || xPos >= this.getChunkSize() || yPos < 0 || yPos >= this.getChunkSize()) return null;
        return this.lightLevel[xPos][yPos];
    }

    public void setLight(byte xPos, byte yPos, Color light) {
        this.lightLevel[xPos][yPos].set(light);
    }

    // Unload resources
    public void cleanup() {

        // Clear local tile map
        if (chunk != null) chunk = null;

        // Mark unloaded and remove references
        loaded = false;
        rendered = false;
        

    }

}
