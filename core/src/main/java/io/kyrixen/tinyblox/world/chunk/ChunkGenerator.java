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

                float t = noise.GetNoise(tileX, tileY);

                float height = (t + 1f) / 2f;
                TileType type;

                if (height < 0.30f)      type = TileType.WATER;
                else if (height < 0.55f) type = TileType.DIRT;
                else if (height < 0.80f) type = TileType.GRASS;
                else                     type = TileType.STONE;

                byte layer;
                switch(type) {

                    case AIR:
                        layer = -1;
                        break;

                    case STONE:
                        layer = 1;
                        break;

                    default:
                        layer = 0;
                        break;

                }                
                
                Tile tile = new Tile(type, (byte) layer);
                chunk.getTileStack(tx, ty).push(tile);

            }

        }

        chunk.loaded = true;
        chunk.rendered = chunk.loaded;

    }
    
}
