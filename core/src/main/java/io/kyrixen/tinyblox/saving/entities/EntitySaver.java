package io.kyrixen.tinyblox.saving.entities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;

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
    public static List<SavedEntity> save(Chunk chunk) {

        List<SavedEntity> convertedEntities = new ArrayList<>();

        for(Entity e : chunk.getEntities()) {

            if(e.getClass() != Entity.class) continue;

            // Blueprint
            SavedEntity nextEntity = new SavedEntity();
            nextEntity.type = "Entity";
            nextEntity.id = e.id();
            nextEntity.data = json.toJson(convertToBlueprint(e));

            convertedEntities.add(nextEntity);

        }

        return convertedEntities;

    }

    // Save helper
    public static void saveSavedEntities(List<SavedEntity> savedEntities, String fileName) {
            
        // Create blueprint
        EntityChunkBlueprint ecb = new EntityChunkBlueprint();
        ecb.formatVersion = Constants.SAVE_FORMAT_VERSION;
        ecb.entities = savedEntities.toArray(new SavedEntity[savedEntities.size()]);

        // Collected data
        String entityData = json.prettyPrint(ecb);
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

}
