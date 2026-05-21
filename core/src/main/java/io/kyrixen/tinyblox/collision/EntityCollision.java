package io.kyrixen.tinyblox.collision;

import java.util.ArrayList;

import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;

public class EntityCollision {

    // Check collision between entities
    public static boolean checkCollision(Entity e1, Entity e2) {
        
        return (e1.x() < e2.x() + e2.width() &&
                e1.x() + e1.width() > e2.x() &&
                e1.y() < e2.y() + e2.height() &&
                e1.y() + e1.height() > e2.y());
    
    }    

    // Checks mob collision
    public static MobEntity checkMobEntityCollision(Entity entity, ArrayList<Entity> entities) {

        for (Entity e : entities) {

            if(!(e instanceof MobEntity)) return null;
            MobEntity mob = (MobEntity) e;

            if (mob instanceof Player) continue;

            if (checkCollision(entity, mob)) return mob;

        }

        return null;
    
    }

    // Checks entity collision
    public static Entity checkEntityCollision(Entity entity, ArrayList<Entity> entities) {

        for (Entity e : entities) {

            if (e instanceof Player) continue;
            if (checkCollision(entity, e)) return e;

        }

        return null;
    
    }

}
