package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity.EntityType;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.Tile;
import io.kyrixen.tinyblox.world.chunk.Tile.TileType;

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
    private float placeDelay = 0.30f;

    // Break delay
    private long lastBreak = 0L;
    private float breakDelay = 0.55f;
    
    // Renderer
    ShapeRenderer sr;

    // Max distance that cursor can be from entity
    private final byte REACH = 2;


    public Selector(Entity entity, ArrayList<Entity> entities, Sfx sfxManager) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entities = entities;
        this.sfxManager = sfxManager;
        this.sr = new ShapeRenderer();

        this.lastPlace = System.currentTimeMillis();
        this.lastBreak = System.currentTimeMillis();
    
    }


    public void update(Terrain terrain, Camera camera, int damage) {
        
        float mouseWorldX = Peripheal.getMouseX() / camera.zoom + camera.x;
        float mouseWorldY = (Constants.WINDOW_HEIGHT - Peripheal.getMouseY()) / camera.zoom + camera.y;

        int tileX = (int)(mouseWorldX / Constants.GRID_SIZE);
        int tileY = (int)(mouseWorldY / Constants.GRID_SIZE);

        // Keep selector same size as player
        this.width = this.entity.width;
        this.height = this.entity.height;

        Vector2 distance = new Vector2(tileX - entity.x / Constants.GRID_SIZE, tileY - entity.y / Constants.GRID_SIZE);

        if(distance.len() > REACH) {
            distance.nor();
            distance.scl(REACH);
        }

        tileX = entity.x / Constants.GRID_SIZE + Math.round(distance.x);
        tileY = entity.y / Constants.GRID_SIZE + Math.round(distance.y);

        this.x = tileX * Constants.GRID_SIZE;
        this.y = tileY * Constants.GRID_SIZE;
        
        if(this.x == entity.x && this.y == entity.y) return;

        checkPlace(terrain);
        checkDestroy(terrain);
        checkHit(damage);

    }

    private void checkHit(int damage) {

        // Check for mouse interaction
        Entity e = checkEntityCollision(entities);

        if(e != null && Peripheal.mousePressed(Input.Buttons.LEFT)){
            if(e.damage(damage)) sfxManager.hitentity.play(Utils.getFloatSound(40));
        }

    }

    private void checkPlace(Terrain terrain) {

        if(!Peripheal.mousePressed(Input.Buttons.RIGHT)) return;
        if(System.currentTimeMillis() - lastPlace < placeDelay * 1000) return;
        
        Entity e = checkEntityCollision(entities);
        if(e != null) return;

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        Tile current = chunk.getTileStack(localTileX, localTileY).top();

        if(current == null) return;

        if(current.level() >= 2);
        else chunk.getTileStack(localTileX, localTileY).push(new Tile(TileType.DIRT, (byte) (current.level() + 1)));

        this.lastPlace = System.currentTimeMillis();

    }

    private void checkDestroy(Terrain terrain) {

        if(!Peripheal.mousePressed(Input.Buttons.LEFT)) return;
        if(System.currentTimeMillis() - lastBreak < breakDelay * 1000) return;
        
        Entity e = checkEntityCollision(entities);
        if(e != null) return;

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);

        Tile current = chunk.getTileStack(localTileX, localTileY).top();

        if(current == null) return;
        if(current.type() == TileType.AIR) return;
        
        if(current.level() <= 0) chunk.getTileStack(localTileX, localTileY).push(new Tile(TileType.AIR, (byte) -1));
        else chunk.getTileStack(localTileX, localTileY).pop();

        this.lastBreak = System.currentTimeMillis();

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

    public void cleanup(){
    
        entity = null;
        entities = null;
        
        x = 0;
        y = 0;
     
        width = 0;
        height = 0;

    }

}
