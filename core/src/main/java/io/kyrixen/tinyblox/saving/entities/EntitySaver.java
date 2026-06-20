package io.kyrixen.tinyblox.saving.entities;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class EntitySaver {
    
    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
    public static EntityBlueprint convertToBlueprint(Entity entity) {

        EntityBlueprint eb = new EntityBlueprint();
        eb.formatVersion = Constants.SAVE_FORMAT_VERSION;
        
        eb.id = entity.id();

        eb.x = entity.x();
        eb.y = entity.y();
        eb.level = entity.level();

        eb.width = entity.width();
        eb.height = entity.height();

        return eb;

    }

    // Saves entity
    public static void save(Entity entity) {

        // Blueprint
        SavedEntity nextEntity = new SavedEntity();
        nextEntity.type = "Entity";
        nextEntity.id = entity.id();
        nextEntity.data = json.toJson(convertToBlueprint(entity));

        // File to write
        String fileName = getEntityFolder() + "/entities_" + getChunkX(entity.x()) + "_" + getChunkY(entity.y()) + ".json";

        saveSavedEntity(nextEntity, fileName);

    }

    // Save helper
    public static void saveSavedEntity(SavedEntity savedEntity, String fileName) {
        
        EntityChunkBlueprint previous = null;
        EntityChunkBlueprint merged = new EntityChunkBlueprint();
        
        // Data stack
        Map<Integer, SavedEntity> entities = new HashMap<>();

        // Read old save
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            String oldData = new String(bytes);
            previous = json.fromJson(EntityChunkBlueprint.class, oldData);
        } catch (IOException e) {}

        // If there is an old save get from it the stacks
        if(previous != null && previous.entities != null) {
            for(SavedEntity se : previous.entities) {
                entities.put(se.id, se);
            }
        }

        // Add new entity
        entities.put(savedEntity.id, savedEntity);

        merged.formatVersion = Constants.SAVE_FORMAT_VERSION;
        merged.entities = entities.values().toArray(new SavedEntity[entities.size()]);

        // Collected data
        String entityData = json.prettyPrint(merged);
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(entityData);
            fileWriter.close();            
        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Cannot create entity save file: " + e); }

    }


    // Get entity folder path
    public static String getEntityFolder() {
        return WorldManager.worldFolder + "/entities";
    }

    // Get entity chunk x
    public static short getChunkX(int worldX) {
        return (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE * Constants.GRID_SIZE);
    }

    // Get entity chunk y
    public static short getChunkY(int worldY) {
        return (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE * Constants.GRID_SIZE);
    }

}
