package io.kyrixen.tinyblox.saving.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.saving.InventoryLoader;
import io.kyrixen.tinyblox.saving.blueprints.entities.MobEntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;

public class MobEntityLoader {
    
    // Json parser
    private static final Json json = new Json();  


    // Convertor helper
    public static MobEntity convertToMobEntity(MobEntityBlueprint meb, SoundManager soundManager) {

        if(meb.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + meb.formatVersion);

        MobEntity mobEntity = new MobEntity(meb.id, meb.x, meb.y, soundManager);
        mobEntity.setLevel(meb.level);
        
        mobEntity.setHealth(meb.health);
        mobEntity.setInvincible(meb.invincible);
        mobEntity.setAutoRegenerate(meb.autoRegenerate);

        mobEntity.setStamina(meb.stamina);
        mobEntity.setTireless(meb.tireless);
        mobEntity.setAutoRecover(meb.autoRecover);
        
        InventoryLoader.load(mobEntity);

        return mobEntity;

    }   

    // Load MobEntity
    public static List<MobEntity> load(Chunk chunk, SoundManager soundManager) {

        List<MobEntity> mobEntities = new ArrayList<>();
        String fileName = getEntityFolder() + "/entities_" + chunk.getX() + "_" + chunk.getY() + ".json";

        String entitiesData;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            entitiesData = new String(bytes);
        } catch (IOException e) { return mobEntities; }

        EntityChunkBlueprint ecb = json.fromJson(EntityChunkBlueprint.class, entitiesData);
        if(ecb.formatVersion != Constants.SAVE_FORMAT_VERSION) {
            Logger.LOGGER.error("LOADER", "Invalid format version for entities chunk save " + chunk.getX() + ", " + chunk.getY() + ": " + ecb.formatVersion);
            return mobEntities;
        }
        Logger.LOGGER.debug("LOADER", "Loaded entities save: " + chunk.getX() + ", " + chunk.getY());        
        

        if(ecb.entities == null) return mobEntities;
        for(SavedEntity se : ecb.entities) {
            if(se.type.equals("MobEntity")) mobEntities.add(convertToMobEntity(json.fromJson(MobEntityBlueprint.class, se.data), soundManager));
        }

        return mobEntities;

    }


    // Get entity folder path
    private static String getEntityFolder() {
        return WorldManager.worldFolder + "/entities";
    }

}
