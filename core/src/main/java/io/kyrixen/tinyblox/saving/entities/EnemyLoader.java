package io.kyrixen.tinyblox.saving.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Bomber;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.Slime;
import io.kyrixen.tinyblox.entities.mob.Voidling;
import io.kyrixen.tinyblox.saving.InventoryLoader;
import io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;

public class EnemyLoader {
    
    // Json parser
    private static final Json json = new Json();  
    

    // Convertor helper
    public static Enemy convertToEnemy(EnemyBlueprint enb, SoundManager soundManager) {

        if(enb.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + enb.formatVersion);

        Enemy enemy;
        switch (enb.enemyType.toUpperCase()) {
    
            case "ENEMY":
                enemy = new Enemy(enb.id, enb.x, enb.y, soundManager);
                break;
    
            case "SLIME":
                enemy = new Slime(enb.id, enb.x, enb.y, soundManager);
                break;
        
            case "BOMBER":
                enemy = new Bomber(enb.id, enb.x, enb.y, soundManager);
                break;

            case "VOIDLING":
                enemy = new Voidling(enb.id, enb.x, enb.y, soundManager);
                break;
    
            default:
                enemy = new Enemy(enb.id, enb.x, enb.y, soundManager);
                break;
        
        }

        enemy.setLevel(enb.level);
        
        enemy.setHealth(enb.health);
        enemy.setInvincible(enb.invincible);
        enemy.setAutoRegenerate(enb.autoRegenerate);

        enemy.setStamina(enb.stamina);
        enemy.setTireless(enb.tireless);
        enemy.setAutoRecover(enb.autoRecover);

        enemy.setChasing(enb.chasing);
        enemy.setAttackDamage(enb.attackDamage);
        enemy.setActivationRange(enb.activationRange);

        InventoryLoader.load(enemy);
    
        return enemy;

    }   

    // Load Enemy
    public static List<Enemy> load(ChunkPos chunkPos, SoundManager soundManager) {

        List<Enemy> enemies = new ArrayList<>();
        String fileName = getEntityFolder() + "/entities_" + chunkPos.getChunkX() + "_" + chunkPos.getChunkY() + ".json";

        String entitiesData;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            entitiesData = new String(bytes);
        } catch (IOException e) { return enemies; }

        EntityChunkBlueprint ecb = json.fromJson(EntityChunkBlueprint.class, entitiesData);
        if(ecb.formatVersion != Constants.SAVE_FORMAT_VERSION) {
            Logger.LOGGER.error("LOADER", "Invalid format version for entities chunk save " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY() + ": " + ecb.formatVersion);
            return enemies;
        }
        Logger.LOGGER.debug("LOADER", "Loaded entities save: " + chunkPos.getChunkX() + ", " + chunkPos.getChunkY());        
        

        if(ecb.entities == null) return enemies;
        for(SavedEntity se : ecb.entities) {
            if(se.type.equals("Enemy")) enemies.add(convertToEnemy(json.fromJson(EnemyBlueprint.class, se.data), soundManager));
        }

        return enemies;

    }


    // Get entity folder path
    private static String getEntityFolder() {
        return WorldManager.worldFolder + "/entities";
    }

}


