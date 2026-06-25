package io.kyrixen.tinyblox.saving.entities.convertors;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Bomber;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.Slime;
import io.kyrixen.tinyblox.entities.mob.Voidling;
import io.kyrixen.tinyblox.saving.InventoryLoader;
import io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint;
import io.kyrixen.tinyblox.sound.SoundManager;

public class EnemyConvertor {

    // Converts to Enemy
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

    // Converts to blueprint
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

}
