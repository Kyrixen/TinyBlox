package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.TerrainCollision;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.TileRenderer;


// Implement stats
public class Entity implements Stats.Health, Stats.Stamina {

    // Entity Enums //

    public enum EntityType {
        
        DEFAULT,
        PLAYER,
        ENEMY

    }

    public enum Speed {

        NONE(Float.MAX_VALUE),
        SLOW(0.55f),
        NORMAL(0.30f),
        SPEEDY(0.15f),
        SONIC(0.05f);

        private final float moveDelay;

        Speed(float moveDelay) { this.moveDelay = moveDelay; }
        public float getMoveDelay() { return this.moveDelay; }

    }


    // Cords
    int x;
    int y;

    // Dimensions
    int width;
    int height;

    // Height
    protected byte level = 1;

    // Identifier
    protected int id;
    
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
    protected long lastMove = 0L;

    protected float sprintDelay = 0.20f;
    protected long lastSprint = 0L;

    protected float damageDelay = 0.50f;
    protected long lastDamage = 0L;

    // Texture and type of entity
    TextureID texture = null;
    EntityType type;

    // Inventory of the entity
    Inventory inventory;
    byte hotbarSlotCount = 3;

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

    // Sound manager
    Sfx soundManager;

    // Constructs entity
    public Entity(int id, int x, int y, Sfx soundManager) {

        this.id = id;
        
        this.x = x;
        this.y = y;

        this.width = Constants.GRID_SIZE;
        this.height = Constants.GRID_SIZE;

        this.soundManager = soundManager;

        this.inventory = new Inventory(this.hotbarSlotCount);

        // Defaults
        this.type = EntityType.DEFAULT;

        this.setSpeed(Speed.SLOW);

    }


    // Get texture (default entity texture)
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "entity");
    }

    // Tries to move
    public boolean tryMove(Terrain terrain){
        moving = dirX != 0 || dirY != 0;
        return TerrainCollision.queryMove(this, terrain);
    }


    // Update entity
    public void update(float deltaTime, Terrain terrain) {

        if(System.currentTimeMillis() - lastMove >= speed.getMoveDelay() * 1000) {
        
            tryMove(terrain);

            // Resets dirs
            dirX = 0;
            dirY = 0;

            lastMove = System.currentTimeMillis();
        
        }

        // Updates vars
        this.exhausted = stamina <= 0 && !tireless;

    }

    // Render entity
    public void render(TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch){

        // Get brightness
        float brightness = 0.5f + timeCycle.getBrightness() * 0.5f;

        batch.setColor(brightness, brightness, brightness, 1.0f);
        tileRenderer.draw(this.texture, x, y, batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }


    // Health interface methods //

    // Getters

    @Override
    public float getHealth() { return health; }
    
    @Override
    public boolean isDead() { return health <= 0 && !invincible; }
    
    // Setters
    
    @Override
    public void setHealth(int health) { this.health = health; }

    @Override
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Override
    public void setInvincible(boolean invincible) { this.invincible = invincible; }


    // Health specific funcs

    @Override
    public void kill() { if(!invincible) this.health = 0; }


    @Override
    public boolean damage(int damage) {
    
        if(invincible) return false;
        if(System.currentTimeMillis() - lastDamage < damageDelay * 1000) return false;

        // Damage
        health -= damage;
        
        // Cap min health
        if(health < 0) health = 0;
    
        lastDamage = System.currentTimeMillis();

        return true;    

    }

    @Override
    public void heal(int amount) {
    
        this.health += amount;
        if(this.health > maxHealth) this.health = maxHealth;
    
    }

    // Auto regenerates health of entity
    @Override
    public void autoRegenerate(boolean state, float delta) {
        if(state && System.currentTimeMillis() - lastDamage >= (damageDelay + 1.5f) * 1000) health += 5 * delta;
        if(health > maxHealth) health = maxHealth;  
        if(health < 0) health = 0;
    }


    // Stamina interface methods //

    // Getters

    @Override
    public float getStamina() { return stamina; }

    @Override
    public boolean isExhausted() { return exhausted; }

    public Speed getSpeed() { return this.speed; }

    // Setters

    @Override
    public void setStamina(int stamina) { this.stamina = stamina; }

    @Override
    public void setMaxStamina(int maxStamina) { this.maxStamina = maxStamina; }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    @Override
    public void setTireless(boolean tireless) { this.tireless = tireless; }


    // Stamina funcs specific

    @Override
    public void exhaust() { if(!tireless) this.exhausted = true; }

    @Override
    public void consume(int amount) {
    
        if(tireless) return;

        this.stamina -= amount;
        if(this.stamina < 0) this.stamina = 0;
    
    }

    @Override
    public void recover(int amount) {

        this.stamina += amount;
        if(this.stamina > maxStamina) this.stamina = maxStamina;

    }

    // Auto recovers stamina
    @Override
    public void autoRecover(boolean state, float delta) {
        if(state && System.currentTimeMillis() - lastSprint >= (sprintDelay + 1.5f) * 1000) stamina += 5 * delta;
        if(stamina > maxStamina) stamina = maxStamina;
        if(stamina < 0) stamina = 0;  
    }


    // Global for all entities //

    // Helper func (updates all entites)
    public static void updateAll(float deltaTime, Terrain terrain, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            e.update(deltaTime, terrain);
        }
    }

    // Helper func (renders all entites)
    public static void renderAll(TimeCycle timeCycle, TileRenderer tileRenderer, ArrayList<Entity> entities, SpriteBatch batch) {
        for (Entity e : entities) {
            e.render(timeCycle, tileRenderer, batch);
        }
    }

    // Helper func too (init textures for all entites)
    public static void initTextureAll(ArrayList<Entity> entities) {
        for (Entity e : entities) {
            e.initTexture();
        }
    }


    // Getters
    public boolean isMoving(){ return moving; }

    public int x() { return x; }
    public int y() { return y; }
    public byte level() { return level; }

    public int dirX() { return dirX; }
    public int dirY() { return dirY; }


    public int width() { return width; }
    public int height() { return height; }

    public float health() { return health; }
    public float stamina() { return stamina; }

    public EntityType type() { return type; }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setLevel(byte level) { this.level = level; }

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

}
