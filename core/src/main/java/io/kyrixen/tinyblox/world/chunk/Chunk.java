package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer.FlipType;

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

    // Chunk properties
    public boolean loaded;
    public boolean rendered;
    public boolean modified;

    // Stores chunk tiles
    private TileStack[][] chunk;

    private final TextureID terrainTileset = new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles");

    // Construct chunk
    public Chunk(int x, int y, int size, boolean loaded){

        this.cX = x;
        this.cY = y;

        this.CHUNK_SIZE = size;
        this.chunk = new TileStack[CHUNK_SIZE][CHUNK_SIZE];

        for(byte xPos = 0; xPos < CHUNK_SIZE; xPos++) {
            for(byte yPos = 0; yPos < CHUNK_SIZE; yPos++) {
                this.chunk[xPos][yPos] = new TileStack();
            }
        }
        
        this.modified = false;
        this.loaded = loaded;
        this.rendered = loaded;
        
    }

    // Render above chunk
    public void renderAbove(Player player, boolean tileAbovePlayer, TileRenderer tileRenderer, TimeCycle timeCycle, SpriteBatch batch) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

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
                
                float brightness = timeCycle.getBrightness();

                float tileCenterX = globalX + Constants.GRID_SIZE / 2f;
                float tileCenterY = globalY + Constants.GRID_SIZE / 2f;
                float playerCenterX = player.x() + player.width() / 2f;
                float playerCenterY = player.y() + player.height() / 2f;

                float distX = tileCenterX - playerCenterX;
                float distY = tileCenterY - playerCenterY;
                int dist = (int) Vector2.len(distX, distY);
                
                float revealRadius = Constants.ROOF_REVEAL_RADIUS * Constants.GRID_SIZE;

                for(byte layer = 0; layer < tileStack.stackSize(); layer++) {

                    Tile stackedTile = tileStack.get(layer);
                    if(stackedTile == null) continue;
                    if(stackedTile.level() <= player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;

                    batch.setColor(brightness, brightness, brightness, 1f);
                    
                    int levelDiff = stackedTile.level() - player.level();                    

                    if(tileAbovePlayer && levelDiff > 0 && dist <= revealRadius) {

                        // Tbh this math wizard alpha calculation thing did ai
                        float alpha = dist / revealRadius;

                        alpha = MathUtils.clamp(alpha, 0f, 1f);
                        alpha = alpha * alpha * (3f - 2f * alpha);
                        alpha = MathUtils.clamp(alpha, 0.45f, 1f);
                        
                        batch.setColor(brightness, brightness, brightness, alpha);
                    
                    }
                    
                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, batch);
                    batch.setColor(1f, 1f, 1f, 1f);

                }
            
            }

        }
        
    }


    // Render lower chunk
    public void renderLower(Player player, TileRenderer tileRenderer, TimeCycle timeCycle, SpriteBatch batch) {

        // Check if can render chunk
        if (!loaded || !rendered) return;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        float brightness = timeCycle.getBrightness();

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                TileStack tileStack = this.getTileStack(tx, ty);
                if(tileStack == null || tileStack.isEmpty()) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                for(byte layer = 0; layer < tileStack.stackSize(); layer++) {

                    Tile stackedTile = tileStack.get(layer);
                    if(stackedTile == null) continue;
                    if(stackedTile.level() > player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;
                    
                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, batch);
                    batch.setColor(brightness, brightness, brightness, 1f);

                }
            
            }

        }
        
    }

    // Tries to place trees
    public void tryToSpawnTree(int maxAttempts) {

        byte TREE_RADIUS = 1;
        byte FREE_SPACE_RADIUS = 1;

        for(int attempts = 0; attempts < maxAttempts; attempts++) {

            byte choosenX = (byte) MathUtils.random(3, this.getChunkSize() - 4); 
            byte choosenY = (byte) MathUtils.random(3, this.getChunkSize() - 4);

            TileStack tileStack = this.getTileStack(choosenX, choosenY);
            if(tileStack == null) continue;

            Tile topTile = tileStack.getTopTerrain();
            if(topTile == null) continue;
           
            if(topTile.level() < Constants.MIN_TERRAIN_HEIGHT) continue;
            if(topTile.level() + 2 > Constants.MAX_TERRAIN_HEIGHT) continue;
           
            if(topTile.type() != TileType.GRASS) continue;

            byte baseLevel = topTile.level();
            if(tileStack.top().level() > baseLevel) continue;

            boolean canSpawn = true;
            for(byte neighborX = (byte) -(TREE_RADIUS + FREE_SPACE_RADIUS); neighborX <= TREE_RADIUS + FREE_SPACE_RADIUS; neighborX++) {

                for(byte neighborY = (byte) -(TREE_RADIUS + FREE_SPACE_RADIUS); neighborY <= TREE_RADIUS + FREE_SPACE_RADIUS; neighborY++) {

                    if(neighborX == 0 && neighborY == 0) continue;
                    if(this.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)) == null) continue;
                    if(this.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)).top() == null) continue;

                    TileStack neighborStack = this.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY));
                    if(neighborStack == null) continue;
                    Tile neighborTop = neighborStack.getTopTerrain();

                    if(neighborTop == null || neighborTop.level() != baseLevel) { canSpawn = false; break; }
                    if(neighborStack.top().level() > baseLevel) { canSpawn = false; break; }
                    
                }

            }

            if(!canSpawn) continue;

            this.getTileStack(choosenX, choosenY).set(new Tile(TileType.WOOD, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));
            this.getTileStack(choosenX, choosenY).set(new Tile(TileType.LEAVES, (byte) (baseLevel + 2)), (byte) (baseLevel + 2));

            for(byte neighborX = (byte) -TREE_RADIUS; neighborX <= TREE_RADIUS; neighborX++) {

                for(byte neighborY = (byte) -TREE_RADIUS; neighborY <= TREE_RADIUS; neighborY++) {

                    if(neighborX == 0 && neighborY == 0) continue;
                    if(this.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)) == null) continue;

                    this.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)).set(new Tile(TileType.LEAVES, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));

                }

            }

        }

    }

    // Render depth for the top tile
    public void renderDepthOverlay(Camera cam, Player player, TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch) {

        // Check if can render overlay for chunk
        if (!loaded || !rendered) return;

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);
        
        if (cX < 0 || cX >= worldChunksX || cY < 0 || cY >= worldChunksY) return;

        float light = timeCycle.getBrightness();

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                Tile tile = this.getTileStack(tx, ty).top();

                if(tile == null) continue;
                
                int globalX = (cX * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
                int globalY = (cY * CHUNK_SIZE + ty) * Constants.GRID_SIZE;

                int levelDiff = Math.abs(tile.level() - player.level());
                float normalized = (float) levelDiff / (Constants.MAX_TERRAIN_HEIGHT - Constants.MIN_TERRAIN_HEIGHT);
                
                float alpha = normalized * 0.55f;
                alpha = Math.min(alpha, 0.5f);             

                if(tile.level() > player.level()) batch.setColor(1f, 1f, 1f, alpha * light);
                else if(tile.level() < player.level()) batch.setColor(0.15f, 0.15f, 0.15f, alpha * light);
                else batch.setColor(1f, 1f, 1f, 0f);
                
                tileRenderer.drawTilesetOutline(terrainTileset, globalX, globalY, tile.tileX(), tile.tileY(), Constants.GRID_SIZE, FlipType.NONE, batch);
                
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

    // Check loading
    public void checkIfOnScreen(Camera cam) {

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
        return this.chunk[localX][localY];
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
