package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class TerrainCollision {

    // Try to move entity
    public static boolean queryMove(Entity e, Terrain terrain) {

        if (e.dirX() == 0 && e.dirY() == 0) return false;

        int tileSize = Constants.GRID_SIZE;

        int nextX = (int) (e.x() + e.dirX() * tileSize);
        int nextY = (int) (e.y() + e.dirY() * tileSize);

        // World bounds
        int worldPixelWidth = Constants.MAP_WIDTH * tileSize;
        int worldPixelHeight = Constants.MAP_HEIGHT * tileSize;

        if (nextX < 0 || nextY < 0 || nextX + e.width() > worldPixelWidth || nextY + e.height() > worldPixelHeight) return false;

        // Tiles collided by entity
        int leftTile   = nextX / tileSize;
        int rightTile  = (nextX + e.width() - 1) / tileSize;
        int topTile    = nextY / tileSize;
        int bottomTile = (nextY + e.height() - 1) / tileSize;

        for (int tileX = leftTile; tileX <= rightTile; tileX++) {
            for (int tileY = topTile; tileY <= bottomTile; tileY++) {

                // Find chunk
                short chunkX = (short) (tileX / Constants.CHUNK_SIZE);
                short chunkY = (short) (tileY / Constants.CHUNK_SIZE);

                Chunk c = terrain.getChunk(chunkX, chunkY);

                if (c == null) continue;

                // Local tile
                byte localX = (byte) (tileX % Constants.CHUNK_SIZE);
                byte localY = (byte) (tileY % Constants.CHUNK_SIZE);

                TileStack tileStack = c.getTileStack(localX, localY);

                Tile floor = tileStack.get((byte) (e.level() - 1));
                Tile entityLevel = tileStack.get(e.level());

                // Tile world cords
                int tx = tileX * tileSize;
                int ty = tileY * tileSize;

                // Collision overlap check
                if (nextX < tx + tileSize && nextX + e.width() > tx && nextY < ty + tileSize && nextY + e.height() > ty) {

                    // Movement requires supporting terrain or climbable tile
                    boolean ground = floor != null && floor.type().canSupport();
                    boolean climbable = entityLevel != null && entityLevel.type().isClimbable();

                    boolean blocked = (!ground && !climbable) || (entityLevel != null && !entityLevel.type().isPassable() && !climbable);

                    if(blocked) return false;
                
                }
            
            }
        
        }

        // Move allowed
        e.setX(nextX);
        e.setY(nextY);

        return true;
    
    }

}
