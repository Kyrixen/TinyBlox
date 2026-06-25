package io.kyrixen.tinyblox.saving.entities.convertors;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.saving.InventoryLoader;
import io.kyrixen.tinyblox.saving.blueprints.entities.MobEntityBlueprint;
import io.kyrixen.tinyblox.sound.SoundManager;

public class MobEntityConvertor {

    // Converts to MobEntity
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

    // Converts to blueprint
    public static MobEntityBlueprint convertToBlueprint(MobEntity mobEntity) {

        MobEntityBlueprint meb = new MobEntityBlueprint();
        meb.formatVersion = Constants.SAVE_FORMAT_VERSION;
        
        meb.id = mobEntity.id();

        meb.x = mobEntity.x();
        meb.y = mobEntity.y();
        meb.level = mobEntity.level();

        meb.width = mobEntity.width();
        meb.height = mobEntity.height();

        meb.health = mobEntity.getHealth();
        meb.invincible = mobEntity.isInvincible();
        meb.autoRegenerate = mobEntity.getAutoRegenerate();

        meb.stamina = mobEntity.getStamina();
        meb.tireless = mobEntity.isTireless();
        meb.autoRecover = mobEntity.getAutoRecover();

        return meb;

    }

}
