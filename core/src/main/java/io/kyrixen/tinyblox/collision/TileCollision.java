package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class TileCollision {
    
    // Entity Tile collision
    public static Tile getEntityTile(Entity e, Terrain terrain) {

        int globalTileX = (int) (e.x() + e.width() / 2f) / Constants.GRID_SIZE;
        int globalTileY = (int) (e.y() + e.height() / 2f) / Constants.GRID_SIZE;

        short entityChunkX = (short) (globalTileX / Constants.CHUNK_SIZE);
        short entityChunkY = (short) (globalTileY / Constants.CHUNK_SIZE);

        Chunk entityChunk = terrain.getChunk(entityChunkX, entityChunkY);
        if(entityChunk == null) return null;

        byte localTileX = (byte) (globalTileX % Constants.CHUNK_SIZE);
        byte localTileY = (byte) (globalTileY % Constants.CHUNK_SIZE);

        TileStack entityStack = entityChunk.getTileStack(localTileX, localTileY);
        if(entityStack == null) return null;

        Tile entityTile = entityStack.get(e.level());
        if(entityTile == null) return null;

        return entityTile;

    }

}
