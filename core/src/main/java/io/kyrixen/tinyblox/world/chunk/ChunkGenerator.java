package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.math.MathUtils;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.RandomUtils;
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
                        else if(currentLevel <= 7) { 
                            if(materialNoise < 0.75f) type = TileType.SAND;
                            else type = TileType.CLAY; 
                        }
                        else type = TileType.GRASS;
                        
                    } else if(levelDiff <= 1) {
                        type = TileType.DIRT;
                    } else type = TileType.STONE;

                    chunk.getTileStack(tx, ty).set(new Tile(type, currentLevel), currentLevel);

                }
                        
            }

        }

        chunk.loaded = true;
        chunk.rendered = chunk.loaded;

    }

    // Generate cave
    public static void generateCave(Chunk chunk) {

        FastNoiseLite caveNoise = new FastNoiseLite();
        caveNoise.SetSeed((int) chunk.getChunkSeed() ^ 0xCAFE);
        caveNoise.SetFrequency(0.03f);

        // World size in tiles
        int worldTilesX = Constants.MAP_WIDTH;
        int worldTilesY = Constants.MAP_HEIGHT;
        
        int caveColumns = 0;
        for (byte tx = 0; tx < chunk.getChunkSize(); tx++) {
            for (byte ty = 0; ty < chunk.getChunkSize(); ty++) {

                // Tile position in WORLD TILE coordinates
                int tileX = chunk.getX() * chunk.getChunkSize() + tx;
                int tileY = chunk.getY() * chunk.getChunkSize() + ty;

                // Skip tiles outside world tile bounds
                if (tileX < 0 || tileY < 0 || tileX >= worldTilesX || tileY >= worldTilesY) continue;

                if(caveNoise.GetNoise(tileX, tileY) < 0.85f) continue;
                caveColumns++;
                
                TileStack tileStack = chunk.getTileStack(tx, ty);
                
                Tile topTerrain = tileStack.getTopTerrain();
                if(topTerrain == null) continue;

                byte roofLevel = (byte) (topTerrain.level() - 3);
                for(byte level = 1; level < roofLevel; level++) {

                    if(tileStack.get(level) == null) continue;
                    if(tileStack.get(level).type() != TileType.STONE) continue;

                    tileStack.set(new Tile(TileType.AIR, level), level);
                
                }

            }
        }

        if(caveColumns > 0) {

            TileStack stack = chunk.getTileStack((byte) 6, (byte) 6);

            Tile top = stack.getTopTerrain();

            if(top != null) {

                stack.set(new Tile(TileType.AIR, top.level()), top.level());
                stack.set(new Tile(TileType.AIR, (byte)(top.level() - 1)), (byte)(top.level() - 1));

            }
        }

        if(caveColumns > 0) Logger.LOGGER.debug("CAVE", "Generated cave at chunk(" + chunk.getX() + "," + chunk.getY() + ") with cave colummns: " + caveColumns);
    
    }

    // Tries to place trees
    public static void spawnTree(Chunk chunk, int maxAttempts) {

        byte TREE_RADIUS = 1;
        byte FREE_SPACE_RADIUS = 1;

        long treeSeed = RandomUtils.mixSeed(chunk.getChunkSeed(), 454654464352545L);
        RandomUtils random = new RandomUtils(treeSeed);

        for(int attempts = 0; attempts < maxAttempts; attempts++) {

            byte choosenX = random.seedByte((byte) 3, (byte) (chunk.getChunkSize() - 4)); 
            byte choosenY = random.seedByte((byte) 3, (byte) (chunk.getChunkSize() - 4));

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

    // Tries to spawn ore
    public static void spawnOre(Chunk chunk, TileType tileType, int maxAttempts) {

        long oreSeed = RandomUtils.mixSeed(chunk.getChunkSeed(), tileType.hashCode() ^ 7655254381564L);
        RandomUtils random = new RandomUtils(oreSeed);

        byte oreSize = random.seedByte((byte) 1, (byte) 2);

        for(int attempt = 0; attempt < maxAttempts; attempt++) {

            byte choosenX = random.seedByte(oreSize, (byte) (chunk.getChunkSize() - oreSize));
            byte choosenY = random.seedByte(oreSize, (byte) (chunk.getChunkSize() - oreSize));

            TileStack tileStack = chunk.getTileStack(choosenX, choosenY);
            if(tileStack == null) continue;

            byte choosenLevel = -1;
            for(int levelAttempt = 0; levelAttempt < maxAttempts; levelAttempt++) {

                byte level = random.seedByte(Constants.MIN_WORLD_HEIGHT, tileStack.height());

                Tile currentTile = tileStack.get(level);

                if(currentTile == null) continue;
                if(currentTile.type().isEmpty() || currentTile.type() != TileType.STONE) continue;

                choosenLevel = level;

                break;

            }

            if(choosenLevel < 0) continue;

            for(byte neighborX = (byte) -oreSize; neighborX <= oreSize; neighborX++) {
                for(byte neighborY = (byte) -oreSize; neighborY <= oreSize; neighborY++) {
                
                    TileStack neighborStack = chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY));
                    if(neighborStack == null) continue;
                    Tile neighborTile = neighborStack.get(choosenLevel);
                    if(neighborTile == null) continue;

                    if(neighborTile.type() != TileType.STONE) continue;

                    neighborStack.set(new Tile(tileType, choosenLevel), choosenLevel);

                }
            }

        }

    }

    // Tries to spawn structure
    public static void spawnStructure(Chunk chunk, int maxAttempts) {

        long structureSeed = RandomUtils.mixSeed(chunk.getChunkSeed(), 1798635L);

        RandomUtils random = new RandomUtils(structureSeed);

        for(Structure structure : StructureRegister.getStructures()) {
            if(!random.seedBoolean(structure.getRarity().getChance())) continue;
            placeStructure(structureSeed, chunk, structure, maxAttempts);
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

        if(centerLevel + structure.getLayers() > Constants.MAX_WORLD_HEIGHT) return false;


        for(byte structureX = 0; structureX < structure.getWidth(); structureX++) {
            for(byte structureY = 0; structureY < structure.getHeight(); structureY++) {

                TileStack currentStack = chunk.getTileStack((byte) (left + structureX), (byte) (top + structureY));
                if(currentStack == null) return false;
                
                Tile belowTile = currentStack.get(centerTile.level());
                if(belowTile == null || belowTile.type().isEmpty()) return false;
            
                for(byte structureLevel = 0; structureLevel < structure.getLayers(); structureLevel++) {
                    
                    Tile currentTile = currentStack.get((byte) (centerLevel + structureLevel));
                    if(currentTile != null && !currentTile.type().isEmpty()) return false;

                }

            }
        }

        return true;

    }

    // Place structure
    public static void placeStructure(long structureSeed, Chunk chunk, Structure structure, int maxAttempts) {

        long placementSeed =  RandomUtils.mixSeed(structureSeed, structure.getName().hashCode());

        RandomUtils random = new RandomUtils(placementSeed);

        for(int attempts = 0; attempts < maxAttempts; attempts++) {

            byte choosenX = random.seedByte((byte) structure.getWidth(), (byte) (chunk.getChunkSize() - structure.getWidth())); 
            byte choosenY = random.seedByte((byte) structure.getHeight(), (byte) (chunk.getChunkSize() - structure.getHeight()));

            if(!canPlaceStructure(chunk, structure, choosenX, choosenY)) continue;

            byte baseLevel = (byte) (chunk.getTileStack(choosenX, choosenY).getTopTerrain().level() + 1);

            int cornerX = choosenX - structure.getWidth() / 2;
            int cornerY  = choosenY - structure.getHeight() / 2;

            for(byte structureX = 0; structureX < structure.getWidth(); structureX++) {
                for(byte structureY = 0; structureY < structure.getHeight(); structureY++) {

                    TileStack structureStack = structure.getMap()[structureX][structureY];
                    
                    for(byte layer = 0; layer < structure.getLayers(); layer++) {

                        Tile structureTile = structureStack.get(layer);
                        if(structureTile == null) continue;
                        if(structureTile.type().isEmpty()) continue;

                        Tile currentTile = new Tile(structureTile.type(), (byte) (baseLevel + layer));
                        chunk.getTileStack((byte) (cornerX + structureX), (byte) (cornerY + structureY)).set(currentTile, (byte) (baseLevel + layer));

                    }


                }
            }

            Logger.LOGGER.debug("STRUCTURE", "Placed structure " + structure.getName() + " at: " + choosenX + ", " + choosenY);

            return;
        
        }

    }
    
}
