package io.kyrixen.tinyblox.saving.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.saving.blueprints.entities.MobEntityBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.world.chunk.Chunk;

public class MobEntitySaver {

    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
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

    // Save MobEntity
    public static List<SavedEntity> save(Chunk chunk) {

        List<SavedEntity> convertedMobEntities = new ArrayList<>();

        for(Entity e : chunk.getEntities()) {

            if(e.getClass() != MobEntity.class) continue;

            // Blueprint
            SavedEntity nextEntity = new SavedEntity();
            nextEntity.type = "MobEntity";
            nextEntity.id = e.id();
            nextEntity.data = json.toJson(convertToBlueprint((MobEntity) e));

            convertedMobEntities.add(nextEntity);

        }
        
        return convertedMobEntities;

    }

}
