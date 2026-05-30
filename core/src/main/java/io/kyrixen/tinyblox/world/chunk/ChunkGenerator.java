package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.math.MathUtils;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

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
                if (tileX < 0 || tileY < 0 || tileX >= worldTilesX || tileY >= worldTilesY) continue;
                

                float terrainNoise = (noise.GetNoise(tileX, tileY) + 1f) / 2f;
                float materialNoise = (noise.GetNoise(tileX + 9999, tileY + 9999) + 1f) / 2f;

                int variation = MathUtils.floor(terrainNoise * (Constants.MAX_WORLD_HEIGHT - Constants.MIN_WORLD_HEIGHT));
                byte level = (byte) (Constants.MIN_WORLD_HEIGHT + variation);
                
                TileType type;
                for(byte currentLevel = 0; currentLevel <= level; currentLevel++) {

                    int levelDiff = level - currentLevel;
                    
                    if(currentLevel == 0) {
                        type = TileType.WATER;
                    } else if(levelDiff == 0) {
                        
                        if(currentLevel >= 13) type = TileType.STONE;
                        else type = TileType.GRASS;
                    
                    } else if(levelDiff <= 2) {
                        type = TileType.DIRT;
                    } else {
                    
                        if(materialNoise < 0.90f) type = TileType.STONE;
                        else type = TileType.IRON;
                    
                    }

                    chunk.getTileStack(tx, ty).set(new Tile(type, currentLevel), currentLevel);

                }

                Tile topTile = chunk.getTileStack(tx, ty).getTopTerrain();
                if(topTile == null) continue;

                if(topTile.type() != TileType.DIRT || topTile.level() > Constants.MAX_WORLD_HEIGHT * 0.75f) continue;

                chunk.getTileStack(tx, ty).set(new Tile(TileType.GRASS, topTile.level()), topTile.level());

            }

        }

        chunk.loaded = true;
        chunk.rendered = chunk.loaded;

    }
    
}
