package io.kyrixen.tinyblox.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.entities.inventory.Equipment;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;

public class Selector extends Entity {

    // Entity using the selector
    private final MobEntity mob;

    // Entitys inventory
    private final Inventory mobEntityInventory;

    // For sound
    private final SoundManager sfxManager;

    // Place delay
    private long lastPlace = 0L;
    private float placeDelay = 0.30f;

    // Break vars
    private float miningProgress;
    private Tile targetTile;

    // Max distance that cursor can be from mob
    private final byte REACH = 2;

    private final SoundID HIT_ENEMY_SOUND = new SoundID("tinyblox", SoundType.SFX, "hit_enemy");
    private final SoundID PLACE_SOUND = new SoundID("tinyblox", SoundType.HUD, "place");
    private final SoundID DESTROY_SOUND = new SoundID("tinyblox", SoundType.HUD, "destroy");


    public Selector(MobEntity mob, SoundManager sfxManager) {

        super(mob.x(), mob.y(), mob.width(), mob.height());

        // Initialize the selector with the given mob
        this.mob = mob;
        this.mobEntityInventory = mob.getInventory();
        this.sfxManager = sfxManager;

        this.lastPlace = System.currentTimeMillis();
    
    }

    public void update(Camera camera) {
        
        float mouseWorldX = Peripheral.getMouseX() / camera.zoom + camera.x;
        float mouseWorldY = (Constants.WINDOW_HEIGHT - Peripheral.getMouseY()) / camera.zoom + camera.y;

        int tileX = (int) (mouseWorldX / Constants.GRID_SIZE);
        int tileY = (int) (mouseWorldY / Constants.GRID_SIZE);

        Vector2 distance = new Vector2(tileX - mob.x / Constants.GRID_SIZE, tileY - mob.y / Constants.GRID_SIZE);

        if(distance.len() > REACH) {
            distance.nor();
            distance.scl(REACH);
        }

        tileX = (int) mob.x / Constants.GRID_SIZE + Math.round(distance.x);
        tileY = (int) mob.y / Constants.GRID_SIZE + Math.round(distance.y);

        this.x = tileX * Constants.GRID_SIZE;
        this.y = tileY * Constants.GRID_SIZE;
        this.setLevel(mob.level());
        
        if(this.x == mob.x && this.y == mob.y) return;
        
    }

    public void render(RendererStack rendererStack) {
    
        ShapeRenderer sr = rendererStack.shape;
        Camera camera = rendererStack.camera;

        sr.setColor(Color.WHITE);
        sr.rect((this.x - camera.x) * camera.zoom, (this.y - camera.y) * camera.zoom, this.width * camera.zoom, this.height * camera.zoom);
    
    }


    // Checkers //

    // Check if can hit mob
    public void checkHit(Terrain terrain) {

        // Check for mouse interaction
        MobEntity e = EntityCollision.checkMobEntityCollision(this, terrain.getNearbyEntities((int) x() / Constants.GRID_SIZE, (int) y() / Constants.GRID_SIZE, REACH));

        float damage = 20;

        Item currentItem = this.mobEntityInventory.currentItem();
        if(currentItem instanceof Equipment) { Equipment equipment = (Equipment) currentItem; damage = damage * equipment.getAttackDamage(); }
        else damage = damage * 0.25f;


        if(e != null){
            if(e.damage((int) damage)) sfxManager.getSound(HIT_ENEMY_SOUND).play(MiscUtils.getFloatSound(40), RandomUtils.randomFloat(0.85f, 1.15f), 0f);
        }

    }

    // Check if can place tile
    public void checkPlace(Terrain terrain) {

        if(System.currentTimeMillis() - lastPlace < placeDelay * 1000) return;

        MobEntity e = EntityCollision.checkMobEntityCollision(this, terrain.getNearbyEntities((int) x() / Constants.GRID_SIZE, (int) y() / Constants.GRID_SIZE, REACH));
        if(e != null) return;

        if(!mobEntityInventory.currentItem().canPlace()) return;

        int tileX = (int) this.x / Constants.GRID_SIZE;
        int tileY = (int) this.y / Constants.GRID_SIZE;
        int playerTileX = (int) mob.x() / Constants.GRID_SIZE;
        int playerTileY = (int) mob.y() / Constants.GRID_SIZE;

        if(tileX == playerTileX && tileY == playerTileY) return;

        byte localTileX = (byte) Math.floorMod(tileX, Constants.CHUNK_SIZE);
        byte localTileY = (byte) Math.floorMod(tileY, Constants.CHUNK_SIZE);

        short chunkPosX = (short) Math.floorDiv(tileX, Constants.CHUNK_SIZE);
        short chunkPosY = (short) Math.floorDiv(tileY, Constants.CHUNK_SIZE);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        if(chunk == null) return;

        byte placeLevel = (byte) (mob.level() - 1);
        if(placeLevel < Constants.MIN_WORLD_HEIGHT) return;

        Tile current = chunk.getTileStack(localTileX, localTileY).get(placeLevel);
        
        if(current != null && !current.type().isEmpty()) {
            placeLevel = (byte) (placeLevel + 1);
            current = chunk.getTileStack(localTileX, localTileY).get(placeLevel); 
            if(current != null && !current.type().isEmpty()) return;
        }

        if(placeLevel >= Constants.MAX_WORLD_HEIGHT) return;

        ItemStack currentStack = this.mobEntityInventory.getCurrentStack();        
        if(currentStack.isEmpty()) return;
        if(!currentStack.getItem().canPlace()) return;

        chunk.getTileStack(localTileX, localTileY).set(new Tile(this.mobEntityInventory.getCurrentStack().getItem().getTileVariant(), placeLevel), placeLevel);
        sfxManager.getSound(PLACE_SOUND).play(MiscUtils.getFloatSound(15), RandomUtils.randomFloat(0.95f, 1.05f), 0f);

        mobEntityInventory.getCurrentStack().remove((byte) 1);
        Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.mobEntityInventory.toString());

        this.lastPlace = System.currentTimeMillis();

    }

    // Check if can destroy tile
    public void checkDestroy(float deltaTime, Terrain terrain) {

        MobEntity e = EntityCollision.checkMobEntityCollision(this, terrain.getNearbyEntities((int) x() / Constants.GRID_SIZE, (int) y() / Constants.GRID_SIZE, REACH));
        if(e != null) { miningProgress = 0f; return; }

        int tileX = (int) this.x / Constants.GRID_SIZE;
        int tileY = (int) this.y / Constants.GRID_SIZE;
        int playerTileX = (int) mob.x() / Constants.GRID_SIZE;
        int playerTileY = (int) mob.y() / Constants.GRID_SIZE;

        if(tileX == playerTileX && tileY == playerTileY) return;

        byte localTileX = (byte) Math.floorMod(tileX, Constants.CHUNK_SIZE);
        byte localTileY = (byte) Math.floorMod(tileY, Constants.CHUNK_SIZE);

        short chunkPosX = (short) Math.floorDiv(tileX, Constants.CHUNK_SIZE);
        short chunkPosY = (short) Math.floorDiv(tileY, Constants.CHUNK_SIZE);

        Chunk chunk = terrain.getChunk(chunkPosX, chunkPosY);
        if(chunk == null) return;

        Tile current = chunk.getTileStack(localTileX, localTileY).get(mob.level());
        
        if(current == null || current.type().isEmpty()) {
            current = chunk.getTileStack(localTileX, localTileY).get((byte) (mob.level() - 1)); 
            if(current == null || current.type().isEmpty()) { miningProgress = 0f; return; }
        }

        if(current != targetTile) { 
            miningProgress = 0f;
            targetTile = current; 
        }
        
        Item currentItem = mobEntityInventory.currentItem();
        if(currentItem instanceof Equipment) { 
        
            Equipment equipment = (Equipment) currentItem;
            
            switch (current.type().getPreferedMining()) {
                
                case NONE:
                    miningProgress += deltaTime * 1f;    
                    break;

                case WOOD:
                    miningProgress += deltaTime * equipment.getWoodMiningSpeed();
                    break;
                
                case STONE:
                    miningProgress += deltaTime * equipment.getStoneMiningSpeed();
                    break;

            }
        
        } else miningProgress += deltaTime * 0.5f;

        
        if(miningProgress < current.type().getMiningTime()) return;

        if(current.level() <= 0) return;

        Item dropItem = current.getItem();
        chunk.getTileStack(localTileX, localTileY).removeAtLayer(current.level()); sfxManager.getSound(DESTROY_SOUND).play(MiscUtils.getFloatSound(25), RandomUtils.randomFloat(0.95f, 1.05f), 0f);
        
        short chunkX = (short) Math.floorDiv((int) x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv((int) y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

        Chunk entitiesChunk = terrain.getChunk(chunkX, chunkY);
        if(entitiesChunk == null) return;
        
        entitiesChunk.getEntities().add(new ItemEntity(this.x + Constants.GRID_SIZE / 4, this.y + Constants.GRID_SIZE / 4, sfxManager, dropItem, this.mob));

        miningProgress = 0f;
        targetTile = null;

    }
    
    // Drop one item from MobEntity inventory
    public void dropItem(Terrain terrain) {

        short chunkX = (short) Math.floorDiv((int) x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv((int) y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

        Chunk entitiesChunk = terrain.getChunk(chunkX, chunkY);
        if(entitiesChunk == null) return;

        ItemStack currenStack = mobEntityInventory.getCurrentStack();
        if(currenStack == null) return;
        if(currenStack.isEmpty()) return;

        ItemEntity itemEntity = new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), sfxManager, currenStack.getItem(), mob);
        entitiesChunk.getEntities().add(itemEntity);

        currenStack.remove((byte) 1);

    }

}
