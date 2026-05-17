// Kyrixen: Sorry i have no energy for explaining.

package io.kyrixen.tinyblox.entities;


import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.InventoryRenderer;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;

public class Player extends Entity {

    private Selector selector;
    private Camera camera;

    private InventoryRenderer inventoryRenderer;

    public Player(int id, int x, int y, Camera camera, Sfx soundManager) {
    
        super(id, x, y, soundManager);
        
        this.type = EntityType.PLAYER;

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
    public void initTexture(Textures textures) {
       this.texture = textures.playerTexture;
    }


    @Override
    public void update(float deltaTime, Terrain terrain) {

        autoRecover(true, deltaTime);
        autoRegenerate(true, deltaTime);

        exhausted = stamina <= 0 && !tireless;

        int scroll = Peripheal.mouseScroll();
        if(scroll < 0) inventory.previousSlot();
        if(scroll > 0) inventory.nextSlot();

        if(System.currentTimeMillis() - lastMove < speed.getMoveDelay() * 1000) return;
        if(dirX == 0 && dirY == 0) { moving = false; return; }

        moving = tryMove(terrain);

        if(moving) soundManager.walk.play(Utils.getFloatSound(15));

        lastMove = System.currentTimeMillis();

    }

    public void updateSelector(Terrain terrain, ArrayList<Entity> entites) {
        selector.update(terrain, camera, entites, 30);
    }

    public void renderSelector(Camera camera) {
        selector.render(camera);
    }

    public void renderInvetory(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        inventoryRenderer.render(batch);
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


    @Override
    public void sprint(){

        if(isExhausted() || !Peripheal.anyWASDPressed() || !Peripheal.keyPressed(Input.Keys.SHIFT_LEFT)) { setSpeed(Speed.NORMAL); return; }
            
        this.setSpeed(Speed.SPEEDY);
                    
        if(System.currentTimeMillis() - lastSprint < sprintDelay * 1000) return;

        stamina -= 5; 
        lastSprint = System.currentTimeMillis();
    
    }

    // Getters

    private long last_debug_print;
    public void stats(Camera camera){
        if(System.currentTimeMillis() - last_debug_print < 3.0f * 1000) return;
        Logger.LOGGER.debug("PLAYER", "Player Health: " + this.health + " | Player Stamina: " + this.stamina + " | Player Pos: (" + this.x + ", " + this.y + ") | Camera: (" + camera.x + ", " + camera.y + ")");
        last_debug_print = System.currentTimeMillis();
    }

    public Selector getSelector() {
        return this.selector;
    }

    public InventoryRenderer getInventoryRenderer() {
        return this.inventoryRenderer;
    }

}
