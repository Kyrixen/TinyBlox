package io.kyrixen.tinyblox.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.Chunk;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.Chunk.Tile;

public class Utils {
    
    // Not used (used in LWJGL version)
    public static float getFloatColor(int color) {

        float floatColor = color / 255.0f;
        
        return floatColor;
    
    }

    @Deprecated
    // For sounds
    public static float getFloatVolume(int volume) {

        float floatVolume = volume / 100.0f;
        
        return floatVolume;
    
    }

    // Check collision between entities
    public static boolean checkCollision(Entity e1, Entity e2) {
        
        return (e1.x() < e2.x() + e2.width() &&
                e1.x() + e1.width() > e2.x() &&
                e1.y() < e2.y() + e2.height() &&
                e1.y() + e1.height() > e2.y());
    
    }

    // For better func
    public static void betterTilesetDraw(int x, int y, int tix, int tiy, Textures texture, SpriteBatch batch){
        texture.drawTileset(texture.terrainTileset, x, y, Constants.GRID_SIZE, Constants.GRID_SIZE, tix, tiy, Constants.GRID_SIZE, batch);
    }

    // Finds a safe spawn near the center using only chunks
    public static int[] spawnNearCenter() {

        int centerX = (Constants.MAP_WIDTH * Constants.GRID_SIZE) / 2;
        int centerY = (Constants.MAP_HEIGHT * Constants.GRID_SIZE) / 2;

        int bestTileX = centerX;
        int bestTileY = centerY;
        double bestDist = Double.MAX_VALUE;
                
        // Iterate over all chunks
        for (Chunk c : Terrain.chunks.values()) {
            for (byte localX = 0; localX < Terrain.getChunkSize(); localX++) {
                for (byte localY = 0; localY < Terrain.getChunkSize(); localY++) {

                    Tile t = c.getTile(localX, localY); 

                    if (t == null) continue;
                    if (t.solid()) continue; // Skip solid tiles

                    int globalX = (c.getX() * Terrain.getChunkSize() + localX) * Constants.GRID_SIZE;
                    int globalY = (c.getY() * Terrain.getChunkSize() + localY) * Constants.GRID_SIZE;

                    // Distance to center
                    double dx = globalX - centerX;
                    double dy = globalY - centerY;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    // Keep closest
                    if (dist < bestDist) {
                        bestDist = dist;
                        bestTileX = globalX;
                        bestTileY = globalY;
                    }
            
                }
            
            }

        }

        System.out.println("Safe spawn found at: " + bestTileX + ", " + bestTileY);
        return new int[]{bestTileX, bestTileY};

    }

    public static void updateVsync() {
        Gdx.graphics.setVSync(Constants.VSYNC);
    }
    
    public static void updateFPS() {
        Gdx.graphics.setForegroundFPS(Constants.FPS);
    }

    public static float getFloatSound(int volume) {
        float baseSound = volume / 100.0f;
        return baseSound / (100f / Constants.VOLUME);
    }

}
