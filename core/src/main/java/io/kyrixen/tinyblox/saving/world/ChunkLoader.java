package io.kyrixen.tinyblox.saving.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.utils.Json;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint.ChunkStack;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint.ChunkStack.ChunkTile;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ChunkLoader {
    
    // Json parser
    private static final Json json = new Json();


    // Convertor helper
    public static Chunk convertToChunk(ChunkPos chunkPos, ChunkBlueprint bc, FastNoiseLite noise) {

        if(bc.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + bc.formatVersion);

        Chunk chunk = createChunk(chunkPos, noise);

        for(ChunkStack cStack : bc.stacks) {

            TileStack stack = chunk.getTileStack(cStack.x, cStack.y);
            for(ChunkTile cTile : cStack.tiles) {
                stack.set(new Tile(TileType.valueOf(cTile.tile), cTile.level), cTile.level);
            }

            stack.setModified(false);

        }

        chunk.setModified(false);

        return chunk;

    }

    // Loades chunk
    public static Chunk load(ChunkPos chunkPos, FastNoiseLite noise) {

        String fileName = getChunkFolder() + "/chunk_" + chunkPos.getChunkX() + "_" + chunkPos.getChunkY() + ".json";

        String chunkData;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            chunkData = new String(bytes);
        } catch (IOException e) { return createChunk(chunkPos, noise); }

        ChunkBlueprint cb = json.fromJson(ChunkBlueprint.class, chunkData);
        if(cb.formatVersion != Constants.SAVE_FORMAT_VERSION) Logger.LOGGER.error("LOADER", "Invalid format version for chunk save " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY() + ": " + cb.formatVersion);
        Logger.LOGGER.debug("LOADER", "Loaded chunk save: " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY());
        return convertToChunk(chunkPos, cb, noise);

    }


    // Generates chunk
    public static Chunk createChunk(ChunkPos chunkPos, FastNoiseLite noise) {

        Chunk chunk = new Chunk(chunkPos.getChunkX(), chunkPos.getChunkY(), noise.getSeed());

        ChunkGenerator.generateChunk(chunk, noise);
        ChunkGenerator.generateCave(chunk, 10);
        ChunkGenerator.spawnOre(chunk, TileType.COAL, 2);
        ChunkGenerator.spawnOre(chunk, TileType.IRON, 1);
        ChunkGenerator.spawnTree(chunk, 10);
        ChunkGenerator.spawnStructure(chunk, 10);

        chunk.setModified(false);

        return chunk;

    }

    // Get chunk folder path
    private static String getChunkFolder() {
        return WorldManager.worldFolder + "/chunks";
    }
    
}
