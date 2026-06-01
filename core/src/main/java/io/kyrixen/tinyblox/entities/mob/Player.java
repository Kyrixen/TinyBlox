// Kyrixen: Sorry i have no energy for explaining.

package io.kyrixen.tinyblox.entities.mob;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.entities.Selector;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.InventoryRenderer;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Player extends MobEntity {

    private final Selector selector;
    private final Camera camera;

    private InventoryRenderer inventoryRenderer;

    private final SoundID WALK_SOUND = new SoundID("tinyblox", SoundType.HUD, "walk");


    public Player(int id, int x, int y, Camera camera, SoundManager soundManager) {
    
        super(id, x, y, soundManager);

        this.hotbarSlotCount = 6;
        this.inventory = new Inventory(this.hotbarSlotCount);
        this.inventoryRenderer = new InventoryRenderer(inventory);

        this.sprintDelay = 0.15f;
        this.damageDelay = 0.50f;

        this.setSpeed(Speed.NORMAL);

        this.health = 100;
        this.maxHealth = 100;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = false;

        this.selector = new Selector(this, soundManager);
        this.camera = camera;

        this.lastMove = System.currentTimeMillis();
    
    }
    

    @Override
    public void initTexture() {
       this.texture = new TextureID("tinyblox", TextureType.ENTITY, "player");
    }

    @Override
    public void update(float deltaTime, Terrain terrain) {

        autoRecover(true, deltaTime);
        autoRegenerate(true, deltaTime);

        exhausted = stamina <= 0 && !tireless;

        if(System.currentTimeMillis() - lastMove < speed.getMoveDelay() * 1000) return;
        if(dirX == 0 && dirY == 0) { moving = false; return; }

        updateFlip();
        moving = tryMove(terrain);

        if(moving) soundManager.getSound(WALK_SOUND).play(Utils.getFloatSound(15), MathUtils.random(0.9f, 1.1f), 0f);

        lastMove = System.currentTimeMillis();

    }

    public void updateSelector() {
        selector.update(camera);
    }

    // Checks inventory hotbar change
    public void checkInventoryScrolling(int scroll) {

        if(scroll < 0) inventory.previousSlot();
        if(scroll > 0) inventory.nextSlot();

    }

    // Checks if any item drops near
    public void checkDropPickup(ArrayList<Entity> entities) {

        if(inventory.isFull()) return;

        Iterator<Entity> iterator = entities.iterator();

        while (iterator.hasNext()) {

            Entity e = iterator.next();

            if (e == this) continue;
            if (!(e instanceof ItemEntity)) continue;
            if (!EntityCollision.checkTileCollision(this, e)) continue;

            ItemEntity itemEntity = (ItemEntity) e;
            Item itemDrop = itemEntity.getItem();

            if(!this.inventory.add(itemDrop, (byte) 1)) continue;

            itemEntity.pickup();

            iterator.remove();
    
            Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.inventory.toString());

        }
    
    }

    // Temporary debug craft
    public void tryCraftWoodenPickaxe() {

        ItemStack woodStack = inventory.getStack(Item.WOOD);
        if(woodStack == null) return;

        if(woodStack.getCount() < 2) return;

        woodStack.remove((byte) 2);

        ItemStack pickaxeStack = inventory.getSlot(inventory.getEmptySlot());
        pickaxeStack.setItem(Item.WOODEN_PICKAXE);
        pickaxeStack.setCount((byte) 1);


    }

    // Temporary debug craft
    public void tryCraftStonePickaxe() {

        ItemStack woodStack = inventory.getStack(Item.WOOD);
        ItemStack stoneStack = inventory.getStack(Item.STONE);

        if(woodStack == null) return;
        if(stoneStack == null) return;


        if(woodStack.getCount() < 1) return;
        if(stoneStack.getCount() < 1) return;

        woodStack.remove((byte) 1);
        stoneStack.remove((byte) 1);

        ItemStack pickaxeStack = inventory.getSlot(inventory.getEmptySlot());
        pickaxeStack.setItem(Item.STONE_PICKAXE);
        pickaxeStack.setCount((byte) 1);

    }

    public void renderSelector(ShapeRenderer shape, Camera camera) {
        selector.render(shape, camera);
    }

    public void renderInvetory(TextureManager tex, SpriteBatch batch) {
        inventoryRenderer.render(tex, batch);
    }

    public void drawInventoryHighlight(ShapeRenderer shapeRenderer) {
        inventoryRenderer.drawHighlight(shapeRenderer);
    }

    // Overrides //

    @Override
    public boolean damage(int damage) {
    
        if(invincible) return false;        
        if(System.currentTimeMillis() - lastDamage < damageDelay * 1000) return false;

        this.health -= damage;

        // Cap min health
        if(this.health < 0) this.health = 0;
    
        lastDamage = System.currentTimeMillis();
        
        return true;
    
    }


    // Sprinting logic
    public void sprint(){

        if(isExhausted()) { setSpeed(Speed.NORMAL); return; }
            
        this.setSpeed(Speed.SPEEDY);
                    
        if(System.currentTimeMillis() - lastSprint < sprintDelay * 1000) return;

        stamina -= 5; 
        lastSprint = System.currentTimeMillis();
    
    }

    // Climb up method
    public void tryClimbUp(Terrain terrain) {

        TileStack tileStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(tileStack == null) return;

        Tile current = tileStack.get(this.level);
        if(current == null) return;

        if(this.level + 1 > Constants.MAX_WORLD_HEIGHT) return;
        if(!current.type().isClimbable()) return;

        Tile above = tileStack.get((byte)(this.level + 1));
        if(above != null && !above.type().isPassable() && !above.type().isClimbable()) return;

        this.level++;

    }

    // Climb down method
    public void tryClimbDown(Terrain terrain) {

        TileStack tileStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(tileStack == null) return;
        
        if(this.level - 1 < Constants.MIN_WORLD_HEIGHT) return;

        Tile current = tileStack.get(this.level);
        Tile below = tileStack.get((byte)(this.level - 1));

        boolean canClimb = (current != null && current.type().isClimbable()) || (below != null && below.type().isClimbable());
        if(!canClimb) return;

        if(below != null && !below.type().isPassable()) return;

        this.level--;

    }


    // Getters

    private long last_debug_print;
    public void stats(Camera camera){
        if(System.currentTimeMillis() - last_debug_print < 3.0f * 1000) return;
        Logger.LOGGER.debug("PLAYER", "Player Health: " + this.health + " | Player Stamina: " + this.stamina + " | Player Pos: (" + this.x + ", " + this.level + ", " + this.y + ") | Camera: (" + camera.x + ", " + camera.y + ")");
        last_debug_print = System.currentTimeMillis();
    }

    public Selector getSelector() {
        return this.selector;
    }

    public InventoryRenderer getInventoryRenderer() {
        return this.inventoryRenderer;
    }

    @Override
    public String toString() {
        return "Player(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", stamina: " + this.stamina + ", moving: " + Boolean.toString(this.moving) + " }";
    }


}
