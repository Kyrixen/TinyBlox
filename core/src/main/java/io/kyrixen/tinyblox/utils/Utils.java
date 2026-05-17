package io.kyrixen.tinyblox.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.Tile;

public class Utils {

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
            for (byte localX = 0; localX < c.getChunkSize(); localX++) {
                for (byte localY = 0; localY < c.getChunkSize(); localY++) {

                    Tile t = c.getTileStack(localX, localY).top(); 

                    if (t == null) continue;
                    if(t.type() == Tile.TileType.AIR) continue;
                    if(t.level() != 0) continue;
                   
                    int globalX = (c.getX() * c.getChunkSize() + localX) * Constants.GRID_SIZE;
                    int globalY = (c.getY() * c.getChunkSize() + localY) * Constants.GRID_SIZE;
                   
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

        Logger.LOGGER.debug("WORLD", "Safe spawn found at: " + bestTileX + ", " + bestTileY);
        return new int[]{bestTileX, bestTileY};

    }

    public static float getFloatSound(int volume) {
        float baseSound = volume / 100.0f;
        return baseSound / (100f / Constants.VOLUME);
    }

}
