package org.kyrixen;


import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


// Implement stats
public class Entity implements Stats.Health, Stats.Stamina {

    // Cords
    int x;
    int y;

    // Dimensions
    int width;
    int height;

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

    //Timings
    protected long lastDelay = 0L;
    protected float moveDelay = 0.50f;

    protected float sprintDelay = 0.20f;
    protected long lastSprint = 0L;

    protected float damageDelay = 0.50f;
    protected long lastDamage = 0L;

    //Texture and type of entity
    BufferedImage texture;
    String type;

    // Terrain helper
    protected Terrain terrain;

    // Stats //

    protected int health;
    protected int maxHealth;
    protected boolean invincible = true;
        
    protected int stamina = 100;
    protected int maxStamina = 100;

    protected boolean exhausted = false;
    protected boolean tireless = true;

    protected boolean autoRegenerate = true;
    protected boolean autoRecover = true;

    SoundManager soundManager;

    // Constructs entity
    public Entity(int id, int x, int y, int width, int height, Terrain terrain, SoundManager soundManager) {

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
        this.type = "default";

    }

    // Get texture
    public BufferedImage initTexture(Textures textures) {

        if(this.type.equals("enemy")) this.texture = textures.enemyTexture;
        if(this.type.equals("player")) this.texture = textures.playerTexture;
        else this.texture = textures.entityTexture;
        
        return this.texture;
    
    }

    // Tries to move
    public void tryMove(Terrain terrain){
        if(dirX != 0 || dirY != 0){ moving = true; } else { moving = false; }
        terrain.tryMove(this, terrain);
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
        if(stamina <= 0 && !tireless){ this.exhausted = true; }
        else { this.exhausted = false; }

    }

    // Render entity
    public void render(Textures textures, Renderer renderer, Camera camera, Graphics2D g){
        textures.draw(this.texture, x, y, width, height, g);
    }


    // Health interface methods //

    @Override
    public int getHealth() { return health; }

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
    public int getStamina() { return stamina; }

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
    public void autoRegenerate(boolean state) {
        if(state && System.currentTimeMillis() - lastDamage >= (damageDelay + 1.5f) * 1000) health += 5;  
    }


    @Override
    public void autoRecover(boolean state) {
        if(state && System.currentTimeMillis() - lastSprint >= (sprintDelay + 1.5f) * 1000) stamina += 5;  
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
            moveDelay = 0.20f; // Faster movement when sprinting
            
            if(System.currentTimeMillis() - lastSprint >= sprintDelay * 1000) {
                stamina -= 5; 
                lastSprint = System.currentTimeMillis();
            }
        
        }
        
        else moveDelay = 0.50f;
    
    }

    // Helper func
    public static void updateAll(float deltaTime, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            e.update(deltaTime);
        }
    }

    // Helper func
    public static void renderAll(Textures textures, Renderer renderer, ArrayList<Entity> entities, Graphics2D g, Camera camera) {
        for (Entity e : entities) {
            e.render(textures, renderer, camera, g);
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


}

// P.S. : My hands hurts after describing all code
