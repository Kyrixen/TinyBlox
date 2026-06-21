package io.kyrixen.tinyblox.utils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;

public class MiscUtils {

    // Entity ID counter var
    private static int LAST_ENTITY_ID = 0;

    // Item ID counter var
    private static int LAST_ITEM_ID = 0;
    
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
            for (byte localX = 0; localX < Constants.CHUNK_SIZE; localX++) {
                for (byte localY = 0; localY < Constants.CHUNK_SIZE; localY++) {

                    Tile t = c.getTileStack(localX, localY).getTopTerrain();

                    if (t == null) continue;
                    if(t.level() < Constants.MIN_TERRAIN_HEIGHT) continue;
                    if(t.level() + 1 >= Constants.MAX_TERRAIN_HEIGHT) continue;
                   
                    int globalX = (c.getX() * Constants.CHUNK_SIZE + localX) * Constants.GRID_SIZE;
                    int globalY = (c.getY() * Constants.CHUNK_SIZE + localY) * Constants.GRID_SIZE;
                   
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

    public static void initEntityID(int lastEntityID) {
        LAST_ENTITY_ID = lastEntityID;
    }

    public static int getCurrentEntityID() {
        return LAST_ENTITY_ID;
    }

    public static int generateItemID() {
        return LAST_ITEM_ID++;
    }

}
