package io.kyrixen.tinyblox.saving.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.world.chunk.Chunk;

public class EnemySaver {
 
    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
    public static EnemyBlueprint convertToBlueprint(Enemy enemy) {

        EnemyBlueprint enb = new EnemyBlueprint();
        enb.formatVersion = Constants.SAVE_FORMAT_VERSION;
        
        enb.id = enemy.id();

        enb.x = enemy.x();
        enb.y = enemy.y();
        enb.level = enemy.level();

        enb.width = enemy.width();
        enb.height = enemy.height();

        enb.health = enemy.getHealth();
        enb.invincible = enemy.isInvincible();
        enb.autoRegenerate = enemy.getAutoRegenerate();

        enb.stamina = enemy.getStamina();
        enb.tireless = enemy.isTireless();
        enb.autoRecover = enemy.getAutoRecover();

        enb.enemyType = enemy.getClass().getSimpleName();
        enb.chasing = enemy.isChasing();
        enb.activationRange = enemy.getActivationRange();
        enb.attackDamage = enemy.getAttackDamage();

        return enb;

    }

    // Save Enemy
    public static List<SavedEntity> save(Chunk chunk) {

        List<SavedEntity> convertedEnemies = new ArrayList<>();

        for(Entity e : chunk.getEntities()) {

            if(!(e instanceof Enemy)) continue;

            // Blueprint
            SavedEntity nextEntity = new SavedEntity();
            nextEntity.type = "Enemy";
            nextEntity.id = e.id();
            nextEntity.data = json.toJson(convertToBlueprint((Enemy) e));

            convertedEnemies.add(nextEntity);

        }
        
        return convertedEnemies;

    }

}
