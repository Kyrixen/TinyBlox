package io.kyrixen.tinyblox.world.chunk;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.chunk.Tile.TileType;
import io.kyrixen.tinyblox.world.fastnoiselite.FastNoiseLite;

public class ChunkGenerator {

    // Generate the chunk
    public static void generate(Chunk chunk, FastNoiseLite noise) {

        // World size in tiles
        int worldTilesX = Constants.MAP_WIDTH;
        int worldTilesY = Constants.MAP_HEIGHT;

        // World size in chunks
        int worldChunksX = (worldTilesX + chunk.getChunkSize() - 1) / chunk.getChunkSize();
        int worldChunksY = (worldTilesY + chunk.getChunkSize() - 1) / chunk.getChunkSize();

        // Safety: do not generate invalid chunks
        if (chunk.getX() < 0 || chunk.getY() < 0 || chunk.getX() >= worldChunksX || chunk.getY() >= worldChunksY) {
            chunk.loaded = false;
            chunk.rendered = false;
            return;
        }

        for (byte tx = 0; tx < chunk.getChunkSize(); tx++) {
            for (byte ty = 0; ty < chunk.getChunkSize(); ty++) {

                // Tile position in WORLD TILE coordinates
                int tileX = chunk.getX() * chunk.getChunkSize() + tx;
                int tileY = chunk.getY() * chunk.getChunkSize() + ty;

                // Skip tiles outside world tile bounds
                if (tileX < 0 || tileY < 0 ||
                    tileX >= worldTilesX || tileY >= worldTilesY) {
                    continue;
                }

                float terrainNoise = (noise.GetNoise(tileX, tileY) + 1f) / 2f;
                float materialNoise = (noise.GetNoise(tileX + 9999, tileY + 9999) + 1f) / 2f;

                byte level;
                if(terrainNoise < 0.55f)      level = 0;
                else if(terrainNoise < 0.80f) level = 1;
                else                          level = 2;

                TileType type;
                if(level == 0) {

                    if(materialNoise < 0.20f) type = TileType.WATER;
                    else if(materialNoise < 0.50f) type = TileType.DIRT;
                    else type = TileType.GRASS;
                
                } else if(level == 1) {
                
                    if(materialNoise < 0.60f)type = TileType.DIRT;
                    else type = TileType.STONE;
                
                } else type = TileType.STONE;
                

                Tile tile = new Tile(type, level);
                chunk.getTileStack(tx, ty).push(tile);

            }

        }

        chunk.loaded = true;
        chunk.rendered = chunk.loaded;

    }
    
}
