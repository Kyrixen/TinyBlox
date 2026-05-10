package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.entities.Entity;

public class EntityCollision {

    // Check collision between entities
    public static boolean checkCollision(Entity e1, Entity e2) {
        
        return (e1.x() < e2.x() + e2.width() &&
                e1.x() + e1.width() > e2.x() &&
                e1.y() < e2.y() + e2.height() &&
                e1.y() + e1.height() > e2.y());
    
    }    

}
