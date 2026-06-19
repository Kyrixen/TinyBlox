package io.kyrixen.tinyblox.saving;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.utils.Json;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.saving.blueprints.ChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.ChunkBlueprint.ChunkStack;
import io.kyrixen.tinyblox.saving.blueprints.ChunkBlueprint.ChunkStack.ChunkTile;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class ChunkLoader {
    
    // Json parser
    private static final Json json = new Json();


    // Convertor helper
    public static Chunk convertToChunk(short cx, short cy, ChunkBlueprint bc, FastNoiseLite noise) {

        if(bc.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + bc.formatVersion);

        Chunk chunk = new Chunk(cx, cy, noise.getSeed());

        ChunkGenerator.generateChunk(chunk, noise);
        ChunkGenerator.generateCave(chunk, 10);
        ChunkGenerator.spawnOre(chunk, TileType.COAL, 2);
        ChunkGenerator.spawnOre(chunk, TileType.IRON, 1);
        ChunkGenerator.spawnTree(chunk, 10);
        ChunkGenerator.spawnStructure(chunk, 10);


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
    public static Chunk load(short cx, short cy, FastNoiseLite noise) {

        String fileName = getChunkFolder() + "/chunk_" + cx + "_" + cy + ".json";

        String chunkData;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            chunkData = new String(bytes);
        } catch (IOException e) { return null; }

        Logger.LOGGER.debug("LOADER", "Loaded chunk save: " + cx + ", " + cy);

        ChunkBlueprint cb = json.fromJson(ChunkBlueprint.class, chunkData);
        return convertToChunk(cx, cy, cb, noise);

    }

    // Get chunk folder path
    private static String getChunkFolder() {
        return WorldManager.worldFolder + "/chunks";
    }
    
}
