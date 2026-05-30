package io.kyrixen.tinyblox.utils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;

public class Utils {

    // Entity ID counter var
    private static int LAST_ENTITY_ID = 0;

    // Finds a safe spawn near the center using only chunks
    public static int[] spawnNearCenter(Terrain terrain) {

        int centerX = (Constants.MAP_WIDTH * Constants.GRID_SIZE) / 2;
        int centerY = (Constants.MAP_HEIGHT * Constants.GRID_SIZE) / 2;

        int bestTileX = centerX;
        int bestTileY = centerY;
        byte bestLevel = 0;

        double bestDist = Double.MAX_VALUE;
                
        // Iterate over all chunks
        for (Chunk c : terrain.getChunks().values()) {
            for (byte localX = 0; localX < c.getChunkSize(); localX++) {
                for (byte localY = 0; localY < c.getChunkSize(); localY++) {

                    Tile t = c.getTileStack(localX, localY).getTopTerrain();

                    if (t == null) continue;
                    if(t.level() < Constants.MIN_WORLD_HEIGHT) continue;
                    if(t.level() + 1 >= Constants.MAX_WORLD_HEIGHT) continue;
                   
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
                        bestLevel = t.level();
                    }
            
                }
            
            }

        }

        Logger.LOGGER.debug("WORLD", "Safe spawn found at: " + bestTileX + ", " + bestTileY);
        return new int[]{bestTileX, bestTileY, bestLevel + 1};

    }

    public static float getFloatSound(int volume) {
        float baseSound = volume / 100.0f;
        return baseSound / (100f / Constants.VOLUME);
    }

    public static int generateEntityID() {
        return LAST_ENTITY_ID++;
    }

}
