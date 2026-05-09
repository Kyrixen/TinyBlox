package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity.EntityType;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Chunk;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.Chunk.Tile;
import io.kyrixen.tinyblox.world.Chunk.Tile.TileType;

public class Selector {

    // Entity using the selector
    private Entity entity;

    // For sound
    private final Sfx sfxManager;

    // Helper Array
    private ArrayList<Entity> entities;

    // Cords
    private int x;
    private int y;

    // Dimensions
    private int width;
    private int height;

    // Place delay
    private long lastPlace = 0L;
    private float placeDelay = 0.15f;

    // Renderer
    ShapeRenderer sr;


    public Selector(Entity entity, ArrayList<Entity> entities, Sfx sfxManager) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entities = entities;
        this.sfxManager = sfxManager;
        this.sr = new ShapeRenderer();

        this.lastPlace = System.currentTimeMillis();
    
    }


    public void update(Terrain terrain, int damage) {
        
        // Set selector size 
        this.width = this.entity.width; // same width
        this.height = this.entity.height; // same height

        // Determine position based on last direction
        if (this.entity.lastDirX == 1) {         // right
        
            this.x = this.entity.x + this.entity.width; 
            this.y = this.entity.y;
        
        } else if (this.entity.lastDirX == -1) { // left
        
            this.x = this.entity.x - this.width; 
            this.y = this.entity.y;
        
        } else if (this.entity.lastDirY == 1) {  // down
        
            this.x = this.entity.x;
            this.y = this.entity.y + this.entity.height;
        
        } else if (this.entity.lastDirY == -1) { // up
        
            this.x = this.entity.x;
            this.y = this.entity.y - this.height;
        
        }

        // Keep selector same size as player
        this.width = this.entity.width;
        this.height = this.entity.height;

        if(System.currentTimeMillis() - lastPlace >= placeDelay * 1000) {
        
            checkPlace(terrain);
            this.lastPlace = System.currentTimeMillis();
        
        }

        checkDestroy(terrain);

        checkHit(damage);

    }

    private void checkHit(int damage) {

        // Check for mouse interaction
        Entity e = checkEntityCollision(entities);

        if(e != null && Peripheal.mousePressed(Input.Buttons.LEFT)){
            e.damage(damage);
            sfxManager.hitentity.play(Utils.getFloatVolume(40));
        }

    }

    private void checkPlace(Terrain terrain) {

        if(!Peripheal.mousePressed(Input.Buttons.RIGHT)) return;
        
        Entity e = checkEntityCollision(entities);
        if(e != null) return;

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        Tile current = chunk.getTile(localTileX, localTileY);

        if(current == null) return;

        chunk.setTile(localTileX, localTileY, new Tile(TileType.DIRT, (byte) (current.height() + 1)));

    }

    private void checkDestroy(Terrain terrain) {

        if(!Peripheal.mousePressed(Input.Buttons.LEFT)) return;
        
        Entity e = checkEntityCollision(entities);
        if(e != null) return;

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);

        chunk.setTile(localTileX, localTileY, new Tile(TileType.AIR, (byte) -1));

    }

    public void render(Camera camera) {

        sr.setColor(Color.WHITE);
        
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect((this.x - camera.x) * camera.zoom, (this.y - camera.y) * camera.zoom, this.width * camera.zoom, this.height * camera.zoom);
        sr.end();
    
    }


    public Entity checkEntityCollision(ArrayList<Entity> entities) {

        for (Entity e : entities) {

            if (e.type() == EntityType.PLAYER) continue;

            if (e.x < this.x + this.width && e.x + e.width > this.x && e.y < this.y + this.height && e.y + e.height > this.y) return e;

        }

        return null;
    
    }

    public Tile checkTile(Terrain terrain) {

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);

        return chunk.getTile(localTileX, localTileY);

    }

    public void cleanup(){
    
        entity = null;
        entities = null;
        
        x = 0;
        y = 0;
     
        width = 0;
        height = 0;

    }

}
