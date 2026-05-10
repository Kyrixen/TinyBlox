package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.Tile;

public class TerrainCollision {

    // Try to move entity
    public static boolean queryMove(Entity e, Terrain terrain) {

        if (e.dirX() == 0 && e.dirY() == 0) return false;

        int tileSize = Constants.GRID_SIZE;

        int nextX = e.x() + e.dirX() * tileSize;
        int nextY = e.y() + e.dirY() * tileSize;

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
                short chunkX = (short)(tileX / terrain.size);
                short chunkY = (short)(tileY / terrain.size);

                Chunk c = terrain.getChunk(chunkX, chunkY);

                if (c == null || !c.loaded) continue;

                // Local tile
                byte localX = (byte)(tileX % c.getChunkSize());
                byte localY = (byte)(tileY % c.getChunkSize());

                Tile tile = c.getTileStack(localX, localY).top();

                if (tile == null) continue;

                // Tile world cords
                int tx = tileX * tileSize;
                int ty = tileY * tileSize;

                // Collision overlap check
                if (nextX < tx + tileSize && nextX + e.width() > tx && nextY < ty + tileSize && nextY + e.height() > ty) {

                    // Walkable only if not air(void) and exactly one level below entity
                    boolean walkable = tile.type() != Tile.TileType.AIR && tile.level() == e.level() - 1;
                    if (!walkable) return false;
                
                }
            
            }
        
        }

        // Move allowed
        e.setX(nextX);
        e.setY(nextY);

        return true;
    
    }

}
