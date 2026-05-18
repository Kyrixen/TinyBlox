package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity.EntityType;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Logger;
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

    // Entitys inventory
    private Inventory entityInventory;

    // For sound
    private final Sfx sfxManager;


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
    final ShapeRenderer sr;

    // Max distance that cursor can be from entity
    private final byte REACH = 2;


    public Selector(Entity entity, Sfx sfxManager) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entityInventory = entity.inventory;
        this.sfxManager = sfxManager;
        this.sr = new ShapeRenderer();

        this.lastPlace = System.currentTimeMillis();
        this.lastBreak = System.currentTimeMillis();
    
    }


    public void update(Camera camera) {
        
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
        
    }

    public void render(Camera camera) {

        sr.setColor(Color.WHITE);
        
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect((this.x - camera.x) * camera.zoom, (this.y - camera.y) * camera.zoom, this.width * camera.zoom, this.height * camera.zoom);
        sr.end();
    
    }


    // Checkers //

    // Check if can hit entity
    public void checkHit(int damage, ArrayList<Entity> entities) {

        // Check for mouse interaction
        Entity e = checkEntityCollision(entities);

        if(e != null){
            if(e.damage(damage)) sfxManager.hitentity.play(Utils.getFloatSound(40));
        }

    }

    // Check if can place tile
    public void checkPlace(Terrain terrain, ArrayList<Entity> entities) {

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
        if(current.level() >= 2) return;
        if(this.entityInventory.getCurrentStack().getItem() == Item.NONE) return;

        chunk.getTileStack(localTileX, localTileY).push(new Tile(entityInventory.getCurrentStack().getItem().toTileType(), (byte) (current.level() + 1))); sfxManager.place.play(Utils.getFloatSound(20));
        
        entityInventory.getCurrentStack().remove((byte) 1);
        Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.entityInventory.toString());

        this.lastPlace = System.currentTimeMillis();

    }

    // Check if can destroy tile
    public void checkDestroy(Terrain terrain, ArrayList<Entity> entities) {

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

        this.entityInventory.add(current.getItem(), (byte) 1);
        Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.entityInventory.toString());
        
        if(current.level() <= 0) { chunk.getTileStack(localTileX, localTileY).push(new Tile(TileType.AIR, (byte) -1)); sfxManager.destroy.play(Utils.getFloatSound(30)); }
        else { chunk.getTileStack(localTileX, localTileY).pop(); sfxManager.destroy.play(Utils.getFloatSound(35)); }

        this.lastBreak = System.currentTimeMillis();

    }

    // Checks entity collision
    public Entity checkEntityCollision(ArrayList<Entity> entities) {

        for (Entity e : entities) {

            if (e.type() == EntityType.PLAYER) continue;

            if (e.x < this.x + this.width && e.x + e.width > this.x && e.y < this.y + this.height && e.y + e.height > this.y) return e;

        }

        return null;
    
    }

}
