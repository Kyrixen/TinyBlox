package io.kyrixen.tinyblox.saving.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.entities.MobEntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.entities.convertors.EnemyConvertor;
import io.kyrixen.tinyblox.saving.entities.convertors.EntityConvertor;
import io.kyrixen.tinyblox.saving.entities.convertors.MobEntityConvertor;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;

public class EntityHandler {
    
    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Load entities chunk
    public static List<Entity> load(Chunk chunk, SoundManager soundManager) {

        List<Entity> entities = new ArrayList<>();
        String fileName = getEntityFolder() + "/entities_" + chunk.getX() + "_" + chunk.getY() + ".json";

        String entitiesData = Platform.fileManager.readFile(fileName);
        if(entitiesData == null) return entities;

        EntityChunkBlueprint ecb = json.fromJson(EntityChunkBlueprint.class, entitiesData);
        if(ecb.formatVersion != Constants.SAVE_FORMAT_VERSION) {
            Logger.LOGGER.error("LOADER", "Invalid format version for entities chunk save " + chunk.getX() + ", " + chunk.getY() + ": " + ecb.formatVersion);
            return entities;
        }
        Logger.LOGGER.debug("LOADER", "Loaded entities save: " + chunk.getX() + ", " + chunk.getY());        
        

        if(ecb.entities == null) return entities;
        for(SavedEntity se : ecb.entities) {
            if(se.type.equals("Entity")) entities.add(EntityConvertor.convertToEntity(json.fromJson(EntityBlueprint.class, se.data)));
            else if(se.type.equals("MobEntity")) entities.add(MobEntityConvertor.convertToMobEntity(json.fromJson(MobEntityBlueprint.class, se.data), soundManager));
            else if(se.type.equals("Enemy")) entities.add(EnemyConvertor.convertToEnemy(json.fromJson(EnemyBlueprint.class, se.data), soundManager));
        }

        return entities;

    }
    
    // Saves entity
    public static List<SavedEntity> save(Chunk chunk) {

        List<SavedEntity> convertedEntities = new ArrayList<>();

        for(Entity e : chunk.getEntities()) {

            // Blueprint
            SavedEntity nextEntity = new SavedEntity();
            nextEntity.id = e.id();

            if(e.getClass() == Entity.class) {
                nextEntity.type = "Entity";
                nextEntity.data = json.toJson(EntityConvertor.convertToBlueprint(e));
                convertedEntities.add(nextEntity);
            }
            else if(e.getClass() == MobEntity.class) {
                nextEntity.type = "MobEntity";
                nextEntity.data = json.toJson(MobEntityConvertor.convertToBlueprint((MobEntity) e));
                convertedEntities.add(nextEntity);
            }
            else if(e instanceof Enemy) {
                nextEntity.type = "Enemy";
                nextEntity.data = json.toJson(EnemyConvertor.convertToBlueprint((Enemy) e));
                convertedEntities.add(nextEntity);
            }
            
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
        Platform.fileManager.writeFile(fileName, entityData);

    }

    // Get entity folder path
    public static String getEntityFolder() {
        return WorldManager.worldFolder.path() + "/entities";
    }

}
