package io.kyrixen.tinyblox.saving.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;

public class EntityLoader {
    
    // Json parser
    private static final Json json = new Json();    


    // Convertor helper
    public static Entity convertToEntity(EntityBlueprint eb) {

        if(eb.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + eb.formatVersion);
        Entity entity = new Entity(eb.id, eb.x, eb.y, eb.width, eb.height);
        entity.setLevel(eb.level);

        return entity;

    }

    // Load entities chunk
    public static List<Entity> load(ChunkPos chunkPos) {

        List<Entity> entities = new ArrayList<>();
        String fileName = getEntityFolder() + "/entities_" + chunkPos.getChunkX() + "_" + chunkPos.getChunkY() + ".json";

        String entitiesData;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            entitiesData = new String(bytes);
        } catch (IOException e) { return entities; }

        EntityChunkBlueprint ecb = json.fromJson(EntityChunkBlueprint.class, entitiesData);
        if(ecb.formatVersion != Constants.SAVE_FORMAT_VERSION) {
            Logger.LOGGER.error("LOADER", "Invalid format version for entities chunk save " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY() + ": " + ecb.formatVersion);
            return entities;
        }
        Logger.LOGGER.debug("LOADER", "Loaded entities save: " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY());        
        

        if(ecb.entities == null) return entities;
        for(SavedEntity se : ecb.entities) {
            if(se.type.equals("Entity")) entities.add(convertToEntity(json.fromJson(EntityBlueprint.class, se.data)));
        }

        return entities;

    }


    // Get entity folder path
    private static String getEntityFolder() {
        return WorldManager.worldFolder + "/entities";
    }

}
