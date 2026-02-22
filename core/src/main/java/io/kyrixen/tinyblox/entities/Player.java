// Kyrixen: Sorry i have no energy for explaining.

package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.SoundManager;
import io.kyrixen.tinyblox.graphics.Renderer;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;

public class Player extends Entity {

    Selector selector;

    public Player(int id, int x, int y, int width, int height, ArrayList<Entity> entities, Terrain terrain, SoundManager soundManager) {
    
        super(id, x, y, width, height, null, soundManager);
        
        this.type = "player";

        this.moveDelay = 0.30f;
        this.sprintDelay = 0.15f;
        this.damageDelay = 0.50f;

        this.health = 100;
        this.maxHealth = 100;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = false;

        this.terrain = terrain;

        selector = new Selector(this, entities, soundManager);

        lastDelay = System.currentTimeMillis();
    
    }
    

    @Override
    public Texture initTexture(Textures textures) {
       this.texture = textures.playerTexture;
       return this.texture;
    }


    @Override
    public void update(float deltaTime) {
        
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastDelay >= moveDelay * 1000) {

            // Move if there's input
            if(dirX != 0 || dirY != 0) {
                moving = true;
            
                soundManager.walk.play(Utils.getFloatVolume(10));

                tryMove(terrain);

                } else { 

                moving = false;

            }
            
            selector.update(30);
            lastDelay = currentTime;

        }

        autoRecover(false);
        autoRegenerate(false);
        
        this.exhausted = stamina <= 0 && !tireless;

    }


    @Override
    public void render(Textures textures, Renderer renderer, Camera camera, SpriteBatch batch){

        textures.draw(this.texture, x, y, width, height, batch);

    }

    public void renderSelector(Camera camera) {
        selector.render(camera);
    }


    @Override
    public void cleanup(){

    try {
        if (selector != null) selector.cleanup();
    } catch (Exception e) {
        System.out.println("Failed to cleanup selector: " + e.getMessage());
    }

        // Nullify references to help GC
        type = null;
        terrain = null;
        texture = null;
        soundManager = null;

        x = 0;
        y = 0;
        width = 0;
        height = 0;

        dirX = 0;
        dirY = 0;
        lastDirX = 0;
        lastDirY = 0;

        damageDelay = 0.0f;
        lastDamage = 0L;

        sprintDelay = 0.0f;
        lastSprint = 0L;

        moveDelay = 0.0f;
        lastDelay = 0L;


        health = 0;
        maxHealth = 0;
        invincible = false;

        stamina = 0;
        maxStamina = 0;

        exhausted = false;
        tireless = false;


    }


    @Override
    public boolean damage(int damage) {
    
        if(!invincible) {
            
            if(System.currentTimeMillis() - lastDamage >= damageDelay * 1000) {

                this.health -= damage;
                
                if(this.health < 0) this.health = 0;
            
                lastDamage = System.currentTimeMillis();
                
                return true;

            } else {} 
        
        }

        return false;
    
    }


    @Override
    public void sprint(){

        if(!isExhausted() && Peripheal.anyWASDPressed() && Peripheal.keyPressed(Input.Keys.SHIFT_LEFT)){ 
            moveDelay = 0.15f; // Faster movement when sprinting
                        
            if(System.currentTimeMillis() - lastSprint >= sprintDelay * 1000) {
                stamina -= 5; 
                lastSprint = System.currentTimeMillis();
            }
        
        }
        
        else moveDelay = 0.30f;
    
    }


    public void stats(Camera camera){

        System.out.println("Player Health: " + this.health + " | Player Stamina: " + this.stamina + " | Player Pos: (" + this.x + ", " + this.y + ") | Camera: (" + camera.x + ", " + camera.y + ")");

    }




}
