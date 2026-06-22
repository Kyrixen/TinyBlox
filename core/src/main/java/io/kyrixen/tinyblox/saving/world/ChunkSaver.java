package io.kyrixen.tinyblox.saving.world;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint.ChunkStack;
import io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint.ChunkStack.ChunkTile;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class ChunkSaver {

    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
    public static ChunkBlueprint convertToBlueprint(Chunk chunk) {

        ChunkBlueprint bc = new ChunkBlueprint();
        bc.formatVersion = Constants.SAVE_FORMAT_VERSION;

        List<ChunkStack> cStacks = new ArrayList<>();

        for(byte tx = 0; tx < Constants.CHUNK_SIZE; tx++) {
            for(byte ty = 0; ty < Constants.CHUNK_SIZE; ty++) {
        
                TileStack tileStack = chunk.getTileStack(tx, ty);
                if(tileStack == null) continue;

                if(!tileStack.isModified()) continue;
                if(tileStack.isEmpty()) continue;

                ChunkStack cStack = new ChunkStack();

                cStack.x = tx;
                cStack.y = ty;

                List<ChunkTile> cTiles = new ArrayList<>();

                for(byte level = 0; level < tileStack.stackSize(); level++) {

                    Tile currentTile = tileStack.get(level);

                    if(currentTile == null) continue;

                    ChunkTile cTile = new ChunkTile();

                    cTile.tile = currentTile.type().name();
                    cTile.level = currentTile.level();

                    cTiles.add(cTile);

                }

                if(cTiles.isEmpty()) continue;
                cStack.tiles = cTiles.toArray(new ChunkTile[cTiles.size()]);

                cStacks.add(cStack);

            }
        }

        bc.stacks = cStacks.toArray(new ChunkStack[cStacks.size()]);      

        return bc;

    }

    // Saves chunk
    public static void save(Chunk chunk) {

        // Blueprints
        ChunkBlueprint next = convertToBlueprint(chunk);
        ChunkBlueprint previous = null;
        ChunkBlueprint merged = new ChunkBlueprint();

        // Data stack
        Map<String, ChunkStack> stacks = new HashMap<>();
        
        // File to write
        String fileName = getChunkFolder() + "/chunk_" + chunk.getX() + "_" + chunk.getY() + ".json";

        // Read old save
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            String oldData = new String(bytes);
            previous = json.fromJson(ChunkBlueprint.class, oldData);
        } catch (IOException e) {}

        // If there is an old save get from it the stacks
        if(previous != null && previous.stacks != null) {
            for(ChunkStack chunkStack : previous.stacks) {
                stacks.put(chunkStack.x + "," + chunkStack.y, chunkStack);
            }
        }

        // Add new stacks
        for(ChunkStack chunkStack : next.stacks) {
            stacks.put(chunkStack.x + "," + chunkStack.y, chunkStack);
        }

        merged.formatVersion = Constants.SAVE_FORMAT_VERSION;
        merged.stacks = stacks.values().toArray(new ChunkStack[stacks.size()]);

        // Collected data
        String chunkData = json.prettyPrint(merged);

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(chunkData);
            fileWriter.close();            
        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Cannot create chunk save file: " + e); }

        chunk.setModified(false);

    }

    // Get chunk folder path
    private static String getChunkFolder() {
        return WorldManager.worldFolder + "/chunks";
    }

}
