package io.kyrixen.tinyblox.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.animation.Animation;
import io.kyrixen.tinyblox.graphics.animation.AnimationManager;
import io.kyrixen.tinyblox.inventory.Equipment;
import io.kyrixen.tinyblox.inventory.Inventory;
import io.kyrixen.tinyblox.inventory.Item;
import io.kyrixen.tinyblox.inventory.ItemRegister;
import io.kyrixen.tinyblox.inventory.ItemStack;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRegister;

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
    private Animation miningAnimation;

	// Mouse vars
	private float lastMouseX = 0f;
	private float lastMouseY = 0f;
	
    // Max distance that cursor can be from mob
    private final byte REACH = 2;

    private final TinyIdentifier HIT_ENEMY_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "hit_enemy");
    private final TinyIdentifier PLACE_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "place");
    private final TinyIdentifier DESTROY_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "destroy");

    // Breaking animation
    private final TinyIdentifier BREAKING_TILE_ANIM = new TinyIdentifier("tinyblox", IdentifierType.ANIMATION, "breaking_tile");


    public Selector(MobEntity mob, SoundManager sfxManager, AnimationManager animManager) {

        super(mob.x(), mob.y(), mob.width(), mob.height());
        this.entityID = new TinyIdentifier("tinyblox", IdentifierType.ENTITY, "selector");

        // Initialize the selector with the given mob
        this.mob = mob;
        this.mobEntityInventory = mob.getInventory();
        this.sfxManager = sfxManager;
        this.miningAnimation = animManager.getAnimation(BREAKING_TILE_ANIM);

        this.lastPlace = System.currentTimeMillis();
    
    }

    public void update(Camera camera) {
        
        float mouseWorldX = Peripheral.getMouseX() / camera.zoom + camera.x;
        float mouseWorldY = (Constants.WINDOW_HEIGHT - Peripheral.getMouseY()) / camera.zoom + camera.y;

		if(mouseWorldX == lastMouseX && mouseWorldY == lastMouseY) return;

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
        
        lastMouseX = mouseWorldX;
        lastMouseY = mouseWorldY;        
    
    }

    public void move(int dirX, int dirY) {
        
        float newWorldX = this.x + Constants.GRID_SIZE * dirX;
        float newWorldY = this.y + Constants.GRID_SIZE * dirY;
        
        int tileX = (int) (newWorldX / Constants.GRID_SIZE);
        int tileY = (int) (newWorldY / Constants.GRID_SIZE);

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
        
    }


    public void render(RendererStack rendererStack, Terrain terrain) {
    
        SpriteBatch sb = rendererStack.batch;
        ShapeRenderer sr = rendererStack.shape;
        Camera camera = rendererStack.camera;

        if(miningProgress > 0f) {

            float ambientR = terrain.getAmbientColor().r;
            float ambientG = terrain.getAmbientColor().g;
            float ambientB = terrain.getAmbientColor().b;
            
            byte heightLevel = terrain.getWorldLevel((int) (x() / width()), (int) (y() / height));

            int layersAbove = Math.max(0, heightLevel - level());
            float alpha = Math.min(layersAbove / 6f, 1f);
            alpha = 1f - alpha * 0.70f;

            if(level() >= heightLevel) { ambientR += 0.75f; ambientG += 0.75f; ambientB += 0.75f; }
            else if(level() < heightLevel) { ambientR -= 0.45f; ambientG -= 0.45f; ambientB -= 0.45f; }

            sb.setColor(ambientR, ambientG, ambientB, alpha);
            sb.begin();
            sb.draw(miningAnimation.getCurrentFrame(), (this.x - camera.x) * camera.zoom, (this.y - camera.y) * camera.zoom, this.width * camera.zoom, this.height * camera.zoom);
            sb.end();
            sb.setColor(1f, 1f, 1f, 1f);
            
        }

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

        chunk.getTileStack(localTileX, localTileY).set(new Tile(TileRegister.getTileByID(this.mobEntityInventory.getCurrentStack().getItem().getTileVariantID()), placeLevel), placeLevel);
        sfxManager.getSound(PLACE_SOUND).play(MiscUtils.getFloatSound(15), RandomUtils.randomFloat(0.95f, 1.05f), 0f);

        mobEntityInventory.getCurrentStack().remove((byte) 1);
        Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.mobEntityInventory.toString());

        this.lastPlace = System.currentTimeMillis();

    }

    // Check if can destroy tile
    public void checkDestroy(float deltaTime, Terrain terrain) {

        MobEntity e = EntityCollision.checkMobEntityCollision(this, terrain.getNearbyEntities((int) x() / Constants.GRID_SIZE, (int) y() / Constants.GRID_SIZE, REACH));
        if(e != null) { miningProgress = 0f; miningAnimation.reset(); return; }

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
            if(current == null || current.type().isEmpty()) { miningProgress = 0f; miningAnimation.reset(); return; }
        }

        if(current != targetTile) { 
            miningProgress = 0f;
            targetTile = current; 
            miningAnimation.reset();
        }
        

        float miningSpeed = 0.5f;
        Item currentItem = mobEntityInventory.currentItem();
        if(currentItem instanceof Equipment) { 
        
            Equipment equipment = (Equipment) currentItem;
            
            switch(current.type().getPreferedMining()) {
                
                case NONE:
                    miningSpeed = 1f;    
                    break;

                case WOOD:
                    miningSpeed = equipment.getWoodMiningSpeed();
                    break;
                
                case STONE:
                    miningSpeed = equipment.getStoneMiningSpeed();
                    break;

            }
        
        }
        miningProgress += deltaTime * miningSpeed;


        float miningTime = current.type().getMiningTime();
        miningAnimation.setSpeedMultiplier((miningAnimation.getFramesCount() * miningSpeed) / (miningTime * miningAnimation.getFPS()));
        miningAnimation.advance(deltaTime);


        if(miningProgress < current.type().getMiningTime()) return;

        if(current.level() <= 0) return;

        Item dropItem = ItemRegister.getItemByID(current.type().getItemDropID());
        chunk.getTileStack(localTileX, localTileY).removeAtLayer(current.level()); sfxManager.getSound(DESTROY_SOUND).play(MiscUtils.getFloatSound(25), RandomUtils.randomFloat(0.95f, 1.05f), 0f);
        
        short chunkX = (short) Math.floorDiv((int) x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv((int) y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

        Chunk entitiesChunk = terrain.getChunk(chunkX, chunkY);
        if(entitiesChunk == null) return;
        
        entitiesChunk.getEntities().add(new ItemEntity(this.x + Constants.GRID_SIZE / 4, this.y + Constants.GRID_SIZE / 4, sfxManager, dropItem, this.mob));

        miningProgress = 0f;
        targetTile = null;

    }

    public void resetDestroy() {
        miningProgress = 0f;
        targetTile = null;
        miningAnimation.reset();
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
