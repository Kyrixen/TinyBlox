package io.kyrixen.tinyblox.saving.entities.convertors;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint;

public class EntityConvertor {
    
    // Converts to Entity
    public static Entity convertToEntity(EntityBlueprint eb) {

        if(eb.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + eb.formatVersion);
        Entity entity = new Entity(eb.id, eb.x, eb.y, eb.width, eb.height);
        entity.setLevel(eb.level);

        return entity;

    }

    // Converts to blueprint
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

}
