package io.kyrixen.tinyblox.saving.entities;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;

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
    public static void save(Enemy enemy) {

        // Blueprint
        SavedEntity nextEntity = new SavedEntity();
        nextEntity.type = "Enemy";
        nextEntity.id = enemy.id();
        nextEntity.data = json.toJson(convertToBlueprint(enemy));
        
        // File to write
        String fileName = EntitySaver.getEntityFolder() + "/entities_" + EntitySaver.getChunkX(enemy.x()) + "_" + EntitySaver.getChunkY(enemy.y()) + ".json";

        EntitySaver.saveSavedEntity(nextEntity, fileName);

    }

}
