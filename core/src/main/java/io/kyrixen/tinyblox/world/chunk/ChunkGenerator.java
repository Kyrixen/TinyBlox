package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.math.MathUtils;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.structures.Structure;
import io.kyrixen.tinyblox.world.chunk.structures.StructureRegister;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ChunkGenerator {

    // Generate the chunk
    public static void generateChunk(Chunk chunk, FastNoiseLite noise) {

        // World size in tiles
        int worldTilesX = Constants.MAP_WIDTH;
        int worldTilesY = Constants.MAP_HEIGHT;

        // World size in chunks
        int worldChunksX = (worldTilesX + chunk.getChunkSize() - 1) / chunk.getChunkSize();
        int worldChunksY = (worldTilesY + chunk.getChunkSize() - 1) / chunk.getChunkSize();

        // Safety: do not generate invalid chunks
        if (chunk.getX() < 0 || chunk.getY() < 0 || chunk.getX() >= worldChunksX || chunk.getY() >= worldChunksY) { chunk.loaded = false; chunk.rendered = false; return; }

        for (byte tx = 0; tx < chunk.getChunkSize(); tx++) {
            for (byte ty = 0; ty < chunk.getChunkSize(); ty++) {

                // Tile position in WORLD TILE coordinates
                int tileX = chunk.getX() * chunk.getChunkSize() + tx;
                int tileY = chunk.getY() * chunk.getChunkSize() + ty;

                // Skip tiles outside world tile bounds
                if (tileX < 0 || tileY < 0 || tileX >= worldTilesX || tileY >= worldTilesY) continue;
                

                float terrainNoise = (noise.GetNoise(tileX, tileY) + 1f) / 2f;
                float materialNoise = (noise.GetNoise(tileX + 9999, tileY + 9999) + 1f) / 2f;

                int variation = MathUtils.floor(terrainNoise * (Constants.MAX_TERRAIN_HEIGHT - Constants.MIN_TERRAIN_HEIGHT));
                byte level = (byte) (Constants.MIN_TERRAIN_HEIGHT + variation);
                
                TileType type;
                for(byte currentLevel = 0; currentLevel <= level; currentLevel++) {

                    int levelDiff = level - currentLevel;
                    
                    if(currentLevel == 0) {
                        type = TileType.WATER;
                    } else if(levelDiff == 0) {
                        
                        if(currentLevel >= 13) type = TileType.STONE;
                        else type = TileType.GRASS;
                    
                    } else if(levelDiff <= 1) {
                        type = TileType.DIRT;
                    } else {
                    
                        if(materialNoise < 0.70f) type = TileType.STONE;
                        else if(materialNoise < 0.85f) type = TileType.COAL;
                        else type = TileType.IRON;
                    
                    }

                    chunk.getTileStack(tx, ty).set(new Tile(type, currentLevel), currentLevel);

                }

                Tile topTile = chunk.getTileStack(tx, ty).getTopTerrain();
                if(topTile == null) continue;

                if(topTile.type() != TileType.DIRT || topTile.level() > Constants.MAX_TERRAIN_HEIGHT * 0.75f) continue;

                chunk.getTileStack(tx, ty).set(new Tile(TileType.GRASS, topTile.level()), topTile.level());

            }

        }

        chunk.loaded = true;
        chunk.rendered = chunk.loaded;

    }

    // Tries to place trees
    public static void spawnTree(Chunk chunk, int maxAttempts) {

        byte TREE_RADIUS = 1;
        byte FREE_SPACE_RADIUS = 1;

        for(int attempts = 0; attempts < maxAttempts; attempts++) {

            byte choosenX = (byte) MathUtils.random(3, chunk.getChunkSize() - 4); 
            byte choosenY = (byte) MathUtils.random(3, chunk.getChunkSize() - 4);

            TileStack tileStack = chunk.getTileStack(choosenX, choosenY);
            if(tileStack == null) continue;

            Tile topTile = tileStack.getTopTerrain();
            if(topTile == null) continue;
           
            if(topTile.level() < Constants.MIN_TERRAIN_HEIGHT) continue;
            if(topTile.level() + 2 > Constants.MAX_TERRAIN_HEIGHT) continue;
           
            if(topTile.type() != TileType.GRASS) continue;

            byte baseLevel = topTile.level();
            if(tileStack.top().level() > baseLevel) continue;

            boolean canSpawn = true;
            for(byte neighborX = (byte) -(TREE_RADIUS + FREE_SPACE_RADIUS); neighborX <= TREE_RADIUS + FREE_SPACE_RADIUS; neighborX++) {

                for(byte neighborY = (byte) -(TREE_RADIUS + FREE_SPACE_RADIUS); neighborY <= TREE_RADIUS + FREE_SPACE_RADIUS; neighborY++) {

                    if(neighborX == 0 && neighborY == 0) continue;
                    if(chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)) == null) continue;
                    if(chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)).top() == null) continue;

                    TileStack neighborStack = chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY));
                    if(neighborStack == null) continue;
                    Tile neighborTop = neighborStack.getTopTerrain();

                    if(neighborTop == null || neighborTop.level() != baseLevel) { canSpawn = false; break; }
                    if(neighborStack.top().level() > baseLevel) { canSpawn = false; break; }
                    
                }

            }

            if(!canSpawn) continue;

            chunk.getTileStack(choosenX, choosenY).set(new Tile(TileType.WOOD, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));
            chunk.getTileStack(choosenX, choosenY).set(new Tile(TileType.LEAVES, (byte) (baseLevel + 2)), (byte) (baseLevel + 2));

            for(byte neighborX = (byte) -TREE_RADIUS; neighborX <= TREE_RADIUS; neighborX++) {

                for(byte neighborY = (byte) -TREE_RADIUS; neighborY <= TREE_RADIUS; neighborY++) {

                    if(neighborX == 0 && neighborY == 0) continue;
                    if(chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)) == null) continue;

                    chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)).set(new Tile(TileType.LEAVES, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));

                }

            }

        }

    }

    // Tries to spawn structure
    public static void spawnStructure(Chunk chunk, int maxAttempts) {

        for(Structure structure : StructureRegister.getStructures()) {
            if(!MathUtils.randomBoolean(structure.getRarity().getChance())) continue;
            placeStructure(chunk, structure, maxAttempts);
        }

    }

    // Checks if can place the structure
    public static boolean canPlaceStructure(Chunk chunk, Structure structure, byte centerX, byte centerY) {

        // Some safety measures
        if(chunk == null) return false;
        
        int left   = centerX - structure.getWidth() / 2;
        int right  = centerX + structure.getWidth() / 2;
        int top    = centerY - structure.getHeight() / 2;
        int bottom = centerY + structure.getHeight() / 2;
        
        if(top < 0 || bottom >= chunk.getChunkSize()) return false;
        if(left < 0 || right >= chunk.getChunkSize()) return false;


        TileStack centerStack = chunk.getTileStack(centerX, centerY);
        if(centerStack == null) return false;
        
        Tile centerTile = centerStack.getTopTerrain();
        if(centerTile == null) return false;
        byte centerLevel = (byte) (centerTile.level() + 1);

        if(centerLevel > Constants.MAX_WORLD_HEIGHT) return false;

        for(byte structureX = 0; structureX < structure.getWidth(); structureX++) {
            for(byte structureY = 0; structureY < structure.getHeight(); structureY++) {

                TileStack currentStack = chunk.getTileStack((byte) (left + structureX), (byte) (top + structureY));
                if(currentStack == null) return false;
            
                Tile currentTile = currentStack.get(centerLevel);
                if(currentTile != null && currentTile.type() != TileType.AIR) return false;
                
                Tile belowTile = currentStack.get(centerTile.level());
                if(belowTile == null || belowTile.type() == TileType.AIR) return false;
            
            }
        }

        return true;

    }

    // Place structure
    public static void placeStructure(Chunk chunk, Structure structure, int maxAttempts) {

        for(int attempts = 0; attempts < maxAttempts; attempts++) {

            byte choosenX = (byte) MathUtils.random(structure.getWidth(), chunk.getChunkSize() - structure.getWidth()); 
            byte choosenY = (byte) MathUtils.random(structure.getHeight(), chunk.getChunkSize() - structure.getHeight());

            if(!canPlaceStructure(chunk, structure, choosenX, choosenY)) continue;

            byte baseLevel = (byte) (chunk.getTileStack(choosenX, choosenY).getTopTerrain().level() + 1);

            int cornerX = choosenX - structure.getWidth() / 2;
            int cornerY  = choosenY - structure.getHeight() / 2;

            for(byte structureX = 0; structureX < structure.getWidth(); structureX++) {
                for(byte structureY = 0; structureY < structure.getHeight(); structureY++) {

                    Tile structureTile = structure.getMap()[structureX][structureY];
                    if(structureTile.type() == TileType.AIR) continue;

                    Tile currentTile = new Tile(structureTile.type(), baseLevel);

                    chunk.getTileStack((byte) (cornerX + structureX), (byte) (cornerY + structureY)).set(currentTile, baseLevel);

                }
            }

            Logger.LOGGER.debug("STRUCTURE", "Placed structure " + structure.getName() + " at: " + choosenX + ", " + choosenY);

            return;
        
        }

    }
    
}
