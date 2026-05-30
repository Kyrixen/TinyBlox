package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Selector extends Entity {

    // Entity using the selector
    private MobEntity mob;

    // Entitys inventory
    private Inventory mobEntityInventory;

    // For sound
    private final Sfx sfxManager;

    // Place delay
    private long lastPlace = 0L;
    private float placeDelay = 0.30f;

    // Break vars
    private float miningProgress;
    private int targetX;
    private int targetY;

    // Max distance that cursor can be from mob
    private final byte REACH = 2;


    public Selector(MobEntity mob, Sfx sfxManager) {

        super(Utils.generateEntityID(), mob.x(), mob.y(), mob.width(), mob.height());

        // Initialize the selector with the given mob
        this.mob = mob;
        this.mobEntityInventory = mob.getInventory();
        this.sfxManager = sfxManager;

        this.lastPlace = System.currentTimeMillis();
    
    }

    public void update(Camera camera) {
        
        float mouseWorldX = Peripheral.getMouseX() / camera.zoom + camera.x;
        float mouseWorldY = (Constants.WINDOW_HEIGHT - Peripheral.getMouseY()) / camera.zoom + camera.y;

        int tileX = (int)(mouseWorldX / Constants.GRID_SIZE);
        int tileY = (int)(mouseWorldY / Constants.GRID_SIZE);

        // Keep selector same size as player
        this.width = this.mob.width;
        this.height = this.mob.height;

        Vector2 distance = new Vector2(tileX - mob.x / Constants.GRID_SIZE, tileY - mob.y / Constants.GRID_SIZE);

        if(distance.len() > REACH) {
            distance.nor();
            distance.scl(REACH);
        }

        tileX = mob.x / Constants.GRID_SIZE + Math.round(distance.x);
        tileY = mob.y / Constants.GRID_SIZE + Math.round(distance.y);

        this.x = tileX * Constants.GRID_SIZE;
        this.y = tileY * Constants.GRID_SIZE;
        
        if(this.x == mob.x && this.y == mob.y) return;
        
    }

    public void render(ShapeRenderer sr, Camera camera) {
        sr.setColor(Color.WHITE);
        sr.rect((this.x - camera.x) * camera.zoom, (this.y - camera.y) * camera.zoom, this.width * camera.zoom, this.height * camera.zoom);
    }


    // Checkers //

    // Check if can hit mob
    public void checkHit(int damage, ArrayList<Entity> entities) {

        // Check for mouse interaction
        MobEntity e = EntityCollision.checkMobEntityCollision(this, entities);

        if(e != null){
            if(e.damage(damage)) sfxManager.hitentity.play(Utils.getFloatSound(40), MathUtils.random(0.85f, 1.15f), 0f);
        }

    }

    // Check if can place tile
    public void checkPlace(Terrain terrain, ArrayList<Entity> entities) {

        if(System.currentTimeMillis() - lastPlace < placeDelay * 1000) return;
        
        MobEntity e = EntityCollision.checkMobEntityCollision(this, entities);
        if(e != null) return;

        if(!mobEntityInventory.currentItem().canPlace()) return;

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        int playerTileX = mob.x() / Constants.GRID_SIZE;
        int playerTileY = mob.y() / Constants.GRID_SIZE;

        if(tileX == playerTileX && tileY == playerTileY) return;

        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        if(chunk == null) return;

        byte placeLevel = mob.level();
        if(placeLevel - 1 < Constants.MIN_WORLD_HEIGHT) return;

        Tile current = chunk.getTileStack(localTileX, localTileY).get(placeLevel);
        
        if(current != null && current.type() != TileType.AIR) {
            placeLevel = (byte) (placeLevel - 1);
            current = chunk.getTileStack(localTileX, localTileY).get(placeLevel); 
            if(current != null && current.type() != TileType.AIR) return;
        }

        if(placeLevel >= Constants.MAX_WORLD_HEIGHT) return;
        if(this.mobEntityInventory.getCurrentStack().isEmpty()) return;

        chunk.getTileStack(localTileX, localTileY).set(new Tile(this.mobEntityInventory.getCurrentStack().getItem().toTileType(), placeLevel), placeLevel);
        sfxManager.place.play(Utils.getFloatSound(15), MathUtils.random(0.95f, 1.05f), 0f);

        mobEntityInventory.getCurrentStack().remove((byte) 1);
        Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.mobEntityInventory.toString());

        this.lastPlace = System.currentTimeMillis();

    }

    // Check if can destroy tile
    public void checkDestroy(float deltaTime, Terrain terrain, ArrayList<Entity> entities) {

        MobEntity e = EntityCollision.checkMobEntityCollision(this, entities);
        if(e != null) { miningProgress = 0f; return; }

        int tileX = this.x / Constants.GRID_SIZE;
        int tileY = this.y / Constants.GRID_SIZE;
        int playerTileX = mob.x() / Constants.GRID_SIZE;
        int playerTileY = mob.y() / Constants.GRID_SIZE;

        if(tileX == playerTileX && tileY == playerTileY) return;

        byte localTileX = (byte) (tileX % terrain.size);
        byte localTileY = (byte) (tileY % terrain.size);

        short chunkPosX = (short) (tileX / terrain.size);
        short chunkPosY = (short) (tileY / terrain.size);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        if(chunk == null) return;

        Tile current = chunk.getTileStack(localTileX, localTileY).get(mob.level());
        
        if(current == null || current.type() == TileType.AIR) {
            current = chunk.getTileStack(localTileX, localTileY).get((byte) (mob.level() - 1)); 
            if(current == null || current.type() == TileType.AIR) return;
        }

        if(!(tileX == targetX && tileY == targetY)) { 
        
            miningProgress = 0f;
            targetX = tileX;
            targetY = tileY;
        
        }
        
        miningProgress += deltaTime * mobEntityInventory.currentItem().getMiningSpeed();
        
        if(miningProgress < current.type().getMiningTime()) return;

        if(current.level() <= 0) return;

        Item dropItem = current.getItem();
        chunk.getTileStack(localTileX, localTileY).popAtLayer(current.level()); sfxManager.destroy.play(Utils.getFloatSound(25), MathUtils.random(0.95f, 1.05f), 0f);
        entities.add(new ItemEntity(Utils.generateEntityID(), this.x + Constants.GRID_SIZE / 4, this.y + Constants.GRID_SIZE / 4, sfxManager, dropItem, this.mob));

        miningProgress = 0f;

    }
    
    // Drop one item from MobEntity inventory
    public void dropItem(ArrayList<Entity> entities) {

        ItemStack currenStack = mobEntityInventory.getCurrentStack();
        if(currenStack == null) return;
        if(currenStack.isEmpty()) return;

        ItemEntity itemEntity = new ItemEntity(Utils.generateEntityID(), this.x() + MathUtils.random(-3, 3), this.y() + MathUtils.random(-3, 3), sfxManager, currenStack.getItem(), mob);
        entities.add(itemEntity);

        currenStack.remove((byte) 1);

    }

}
