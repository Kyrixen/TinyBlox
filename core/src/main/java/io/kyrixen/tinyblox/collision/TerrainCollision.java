package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.Tile;

public class TerrainCollision {

    @Deprecated
    // Helper for Entity
    public static void tryMove(Entity e, Terrain terrain) {

        if (e.dirX() == 0 && e.dirY() == 0) return;

        int tileSize = Constants.GRID_SIZE;

        int nextX = e.x() + e.dirX() * tileSize;
        int nextY = e.y() + e.dirY() * tileSize;

        // World bounds check in pixels (map size is stored in tiles)
        int worldPixelWidth = Constants.MAP_WIDTH * tileSize;
        int worldPixelHeight = Constants.MAP_HEIGHT * tileSize;
        if (nextX < 0 || nextY < 0 || nextX + e.width() > worldPixelWidth || nextY + e.height() > worldPixelHeight) return;

        // Determine chunk range the entity could touch
        int chunkSize = terrain.size;

        short startChunkX = (short) (nextX / (chunkSize * tileSize));
        short startChunkY = (short) (nextY / (chunkSize * tileSize));
        int endChunkX   = (nextX + e.width() - 1)  / (chunkSize * tileSize);
        int endChunkY   = (nextY + e.height() - 1) / (chunkSize * tileSize);

        // Iterate over relevant chunks
        for (short cx = startChunkX; cx <= endChunkX; cx++) {
            for (short cy = startChunkY; cy <= endChunkY; cy++) {

                Chunk c = terrain.getChunk(cx, cy);

                if (!c.loaded) continue;

                // Check each tile in the chunk
                for (byte localX = 0; localX < c.getChunkSize(); localX++) {
                    for (byte localY = 0; localY < c.getChunkSize(); localY++) {
                        
                        Tile tile = c.getTileStack(localX, localY).top();

                        if (tile == null) continue;
                        if (!tile.solid()) continue;

                        int globalX = (c.getX() * c.getChunkSize() + localX) * Constants.GRID_SIZE;
                        int globalY = (c.getY() * c.getChunkSize() + localY) * Constants.GRID_SIZE;
                        
                        // AABB collision
                        if (nextX < globalX + tileSize &&
                            nextX + e.width() > globalX &&
                            nextY < globalY + tileSize &&
                            nextY + e.height() > globalY) {
                            return; // Blocked
                        }
                
                    }
                
                }
            
            }
        
        }

        // No collision, move allowed
        e.setX(nextX);
        e.setY(nextY);
    
    }


    // Try to move entity
    public static boolean queryMove(Entity e, Terrain terrain) {

        if (e.dirX() == 0 && e.dirY() == 0) return false;

        int tileSize = Constants.GRID_SIZE;

        int nextX = e.x() + e.dirX() * tileSize;
        int nextY = e.y() + e.dirY() * tileSize;

        // World bounds check in pixels (map size is stored in tiles)
        int worldPixelWidth = Constants.MAP_WIDTH * tileSize;
        int worldPixelHeight = Constants.MAP_HEIGHT * tileSize;

        if (nextX < 0 || nextY < 0 || nextX + e.width() > worldPixelWidth || nextY + e.height() > worldPixelHeight) return false;

        // Entity bounds in Tile coordinates
        int leftTile   = nextX / tileSize;
        int rightTile  = (nextX + e.width() - 1) / tileSize;
        int topTile    = nextY / tileSize;
        int bottomTile = (nextY + e.height() - 1) / tileSize;


        // Only check touched tiles
        for (int tileX = leftTile; tileX <= rightTile; tileX++) {
            for (int tileY = topTile; tileY <= bottomTile; tileY++) {

                // Find chunk
                short chunkX = (short) (tileX / terrain.size);
                short chunkY = (short) (tileY / terrain.size);

                Chunk c = terrain.getChunk(chunkX, chunkY);

                if (c == null || !c.loaded) continue;

                // Local tile position INSIDE chunk
                byte localX = (byte) (tileX % c.getChunkSize());
                byte localY = (byte) (tileY % c.getChunkSize());

                Tile tile = c.getTileStack(localX, localY).top();

                if (tile == null) continue;
                if (!tile.solid()) continue;

                // Convert tile coords -> WORLD PIXELS
                int tx = tileX * tileSize;
                int ty = tileY * tileSize;

                // Collision
                if (nextX < tx + tileSize && nextX + e.width() > tx && nextY < ty + tileSize && nextY + e.height() > ty) return false;

            }
        
        }

        // No collision, move allowed
        e.setX(nextX);
        e.setY(nextY);

        return true;
    
    }

}
