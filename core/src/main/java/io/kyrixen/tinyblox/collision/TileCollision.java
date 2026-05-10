package io.kyrixen.tinyblox.collision;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.Tile;

public class TileCollision {
    
    // Tile collision
    public static Tile blockCollision(Entity e){

        for(Chunk c : Terrain.chunks.values()){
            for (byte tx = 0; tx < c.getChunkSize(); tx++) {
                for (byte ty = 0; ty < c.getChunkSize(); ty++) {

                    Tile t = c.getTile(tx, ty);
                    if(t == null) continue;

                    int globalX = (c.getX() * c.getChunkSize() + tx) * Constants.GRID_SIZE;
                    int globalY = (c.getY() * c.getChunkSize() + ty) * Constants.GRID_SIZE;
                    
                    if(e.x() < globalX + Constants.GRID_SIZE && e.x() + e.width() > globalX && e.y() < globalY + Constants.GRID_SIZE && e.y() + e.height() > globalY) return t;
                
                }
            }

        }

        return null;

    }

}
