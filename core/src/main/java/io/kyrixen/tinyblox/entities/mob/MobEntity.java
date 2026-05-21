package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.world.Terrain;

public class MobEntity extends Entity implements Stats.Health, Stats.Stamina  {

    // Timings
    protected float sprintDelay = 0.20f;
    protected long lastSprint = 0L;

    protected float damageDelay = 0.50f;
    protected long lastDamage = 0L;

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

    public MobEntity(int id, int x, int y, Sfx soundManager) {
        
        super(id, x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

        this.soundManager = soundManager;

        this.inventory = new Inventory(this.hotbarSlotCount);
    
    }


    @Override
    // Update mob entity
    public void update(float deltaTime, Terrain terrain) {

        super.update(deltaTime, terrain);

        this.exhausted = stamina <= 0 && !tireless;

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


    // Getters
    public float health() { return health; }
    public float stamina() { return stamina; }
    
    public Inventory getInventory() { return inventory; }

    @Override
    public String toString() {
        return "MobEntity(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
