package io.kyrixen.tinyblox.collision;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;

public class EntityCollision {

    // Check AABB collision between entities
    public static boolean checkAABBCollision(Entity e1, Entity e2) {
        
        return (e1.x() < e2.x() + e2.width() &&
                e1.x() + e1.width() > e2.x() &&
                e1.y() < e2.y() + e2.height() &&
                e1.y() + e1.height() > e2.y());
    
    }    

    // Check Tile collision between entities
    public static boolean checkTileCollision(Entity e1, Entity e2) {

        int e1TileX = (e1.x() + e1.width() / 2) / Constants.GRID_SIZE;
        int e2TileX = (e2.x() + e2.width() / 2) / Constants.GRID_SIZE;

        int e1TileY = (e1.y() + e1.height() / 2) / Constants.GRID_SIZE;
        int e2TileY = (e2.y() + e2.height() / 2) / Constants.GRID_SIZE;

        return e1TileX == e2TileX && e1TileY == e2TileY && e1.level() == e2.level();

    }

    // Checks mob collision
    public static MobEntity checkMobEntityCollision(Entity entity, List<Entity> entities) {

        for (Entity e : entities) {

            if(!(e instanceof MobEntity)) continue;
            MobEntity mob = (MobEntity) e;

            if (mob instanceof Player) continue;

            if (checkTileCollision(entity, mob)) return mob;

        }

        return null;
    
    }

    // Checks entity collision
    public static Entity checkEntityCollision(Entity entity, List<Entity> entities) {

        for (Entity e : entities) {

            if (e instanceof Player) continue;
            if (checkTileCollision(entity, e)) return e;

        }

        return null;
    
    }

    // Checks entity distance
    public static float checkEntityDistance(Entity e1, Entity e2) {
        return Vector2.dst(e1.x() / e1.width(), e1.y() / e1.height(), e2.x() / e2.width(), e2.y() / e2.height());
    }

}
