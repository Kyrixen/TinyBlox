package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.collision.TerrainCollision;
import io.kyrixen.tinyblox.graphics.Renderer;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;


// Implement stats
public class Entity implements Stats.Health, Stats.Stamina {

    public static enum EntityType {
        
        DEFAULT,
        PLAYER,
        ENEMY

    }

    public static enum Speed {

        NONE,
        SLOW,
        NORMAL,
        SPEEDY,
        SONIC

    }


    // Cords
    int x;
    int y;

    // Dimensions
    int width;
    int height;
    protected byte level = 1;

    // Identifier
    protected int id;

    // RNG identifier
    protected long rng;
    
    // Directions
    protected int dirX = 0;
    protected int dirY = 0;
    
    // Last directions
    protected int lastDirX = 0;
    protected int lastDirY = 0;
    
    // Movement
    protected boolean moving = false;
    protected Speed speed = Speed.SLOW;

    //Timings
    protected long lastDelay = 0L;
    protected float moveDelay = 0.50f;

    protected float sprintDelay = 0.20f;
    protected long lastSprint = 0L;

    protected float damageDelay = 0.50f;
    protected long lastDamage = 0L;

    // Texture and type of entity
    Texture texture;
    EntityType type;

    // Terrain helper
    protected Terrain terrain;

    // Stats //

    protected float health;
    protected int maxHealth;
    protected boolean invincible = true;
        
    protected float stamina = 100;
    protected int maxStamina = 100;

    protected boolean exhausted = false;
    protected boolean tireless = true;

    protected boolean autoRegenerate = true;
    protected boolean autoRecover = true;

    Sfx soundManager;

    // Constructs entity
    public Entity(int id, int x, int y, int width, int height, Terrain terrain, Sfx soundManager) {

        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.terrain = terrain;
        this.rng = (long) Math.floor(Math.random() * Integer.MAX_VALUE);

        this.texture = null;
        this.soundManager = soundManager;

        // Defaults
        this.type = EntityType.DEFAULT;

        this.setSpeed(Speed.SLOW);

    }

    // Get texture
    public Texture initTexture(Textures textures) {

        if(this.type == EntityType.ENEMY) this.texture = textures.enemyTexture;
        if(this.type == EntityType.PLAYER) this.texture = textures.playerTexture;
        else this.texture = textures.entityTexture;
        
        return this.texture;
    
    }

    // Tries to move
    public boolean tryMove(Terrain terrain){
        moving = dirX != 0 || dirY != 0;
        return TerrainCollision.queryMove(this, terrain);
    }

    // Update entity
    public void update(float deltaTime) {

        if(System.currentTimeMillis() - lastDelay >= moveDelay * 1000) {
        
            tryMove(terrain);

            // Resets dirs
            dirX = 0;
            dirY = 0;

            lastDelay = System.currentTimeMillis();
        
        }

        // Updates vars
        this.exhausted = stamina <= 0 && !tireless;

    }

    // Render entity
    public void render(Textures textures, Renderer renderer, Camera camera, TimeCycle timeCycle, SpriteBatch batch){

        float brightness = 0.5f + timeCycle.getBrightness() * 0.5f;

        batch.setColor(brightness, brightness, brightness, 1.0f);
        textures.draw(this.texture, x, y, width, height, batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }


    // Health interface methods //

    @Override
    public float getHealth() { return health; }

    @Override
    public void setHealth(int health) { this.health = health; }

    @Override
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Override
    public boolean isDead() { return health <= 0 && !invincible; }

    @Override
    public void invincible(boolean invincible) { this.invincible = invincible; }

    @Override
    public void kill() { if(!invincible) this.health = 0; }


    @Override
    public boolean damage(int damage) {
    
        if(!invincible) {
            
            if(System.currentTimeMillis() - lastDamage >= damageDelay * 1000) {

                // Damage
                this.health -= damage;
                
                if(this.health < 0) this.health = 0;
            
                lastDamage = System.currentTimeMillis();

                return true;
            
            } else {} 
        
        }

        return false;
    
    }


    @Override
    public void heal(int amount) {
    
        this.health += amount;
        if(this.health > maxHealth) this.health = maxHealth;
    
    }


    // Stamina interface methods //

    @Override
    public float getStamina() { return stamina; }

    @Override
    public void setStamina(int stamina) { this.stamina = stamina; }

    @Override
    public void setMaxStamina(int maxStamina) { this.maxStamina = maxStamina; }

    @Override
    public boolean isExhausted() { return exhausted; }

    @Override
    public void exhaust() { if(!tireless) this.exhausted = true; }

    @Override
    public void tireless(boolean tireless) { this.tireless = tireless; }


    @Override
    public void consume(int amount) {
    
        if(!tireless){
    
            this.stamina -= amount;

            if(this.stamina < 0) this.stamina = 0;
        
        }
    
    }


    @Override
    public void recover(int amount) {

        this.stamina += amount;
        if(this.stamina > maxStamina) this.stamina = maxStamina;

    }


    @Override
    public void autoRegenerate(boolean state, float delta) {
        if(state && System.currentTimeMillis() - lastDamage >= (damageDelay + 1.5f) * 1000) health += 5 * delta;
        if(health > maxHealth) health = maxHealth;  
        if(health < 0) health = 0;
    }


    @Override
    public void autoRecover(boolean state, float delta) {
        if(state && System.currentTimeMillis() - lastSprint >= (sprintDelay + 1.5f) * 1000) stamina += 5 * delta;
        if(stamina > maxStamina) stamina = maxStamina;
        if(stamina < 0) stamina = 0;  
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
        this.updateSpeed();
    }

    public void updateSpeed() {

        switch (speed) {

            case NONE:                
                this.moveDelay = Float.MAX_VALUE;
                break;
            
            case SLOW:
                this.moveDelay = 0.55f;
                break;
                            
            case NORMAL:
                this.moveDelay = 0.30f;                
                break;
                            
            case SPEEDY:
                this.moveDelay = 0.15f;
                break;
                            
            case SONIC:
                this.moveDelay = 0.05f;
                break;

        }

    }

    // Unload resources
    public void cleanup() {

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

    // Sprinting
    public void sprint(){

        if(!isExhausted()){ 

            this.setSpeed(Speed.NORMAL); // Faster movement when sprinting
            
            if(System.currentTimeMillis() - lastSprint >= sprintDelay * 1000) {
                stamina -= 5; 
                lastSprint = System.currentTimeMillis();
            }
        
        }
        
        else this.setSpeed(Speed.SLOW);
    
    }

    // Helper func
    public static void updateAll(float deltaTime, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            e.update(deltaTime);
        }
    }

    // Helper func
    public static void renderAll(Textures textures, Renderer renderer, TimeCycle timeCycle, ArrayList<Entity> entities, SpriteBatch batch, Camera camera) {
        for (Entity e : entities) {
            e.render(textures, renderer, camera, timeCycle, batch);
        }
    }

    // Helper func too
    public static void initTextureAll(Textures textures, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            e.initTexture(textures);
        }
    }

    // Return func
    public boolean isMoving(){ return moving; }

    public int x() { return x; }
    public int y() { return y; }
    public byte level() { return level; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setLevel(byte level) { this.level = level; }

    public int dirX() { return dirX; }
    public int dirY() { return dirY; }

    public void setDirX(int dirX) {
        this.dirX = dirX;
        if (dirX != 0) {
            this.lastDirX = dirX;
            this.lastDirY = 0;
        }
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
        if (dirY != 0) {
            this.lastDirY = dirY;
            this.lastDirX = 0;
        }
    }

    public int width() { return width; }
    public int height() { return height; }

    public float health() { return health; }
    public float stamina() { return stamina; }

    public EntityType type() { return type; }

}

// P.S. : My hands hurts after describing all code
