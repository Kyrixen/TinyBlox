package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class MobEntity extends Entity implements Stats.Health, Stats.Stamina  {

    // Timings
    protected float sprintDelay = 0.20f;
    protected long lastSprint = 0L;

    protected float damageDelay = 0.50f;
    protected long lastDamage = 0L;

    // Inventory of the entity
    protected Inventory inventory;
    protected byte hotbarSlotCount = 3;

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
    protected final SoundManager soundManager;

    public MobEntity(float x, float y, SoundManager soundManager) {
        
        super(x, y, Constants.GRID_SIZE, Constants.GRID_SIZE);

        this.soundManager = soundManager;

        this.inventory = new Inventory(this.hotbarSlotCount);
    
    }

    public MobEntity(int id, float x, float y, SoundManager soundManager) {
        
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

    @Override
    public boolean getAutoRegenerate() { return autoRegenerate; }

    @Override
    public boolean isInvincible() { return invincible; }
    
    // Setters
    
    @Override
    public void setHealth(float health) { this.health = health; }

    @Override
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Override
    public void setInvincible(boolean invincible) { this.invincible = invincible; }

    @Override
    public void setAutoRegenerate(boolean state) { this.autoRegenerate = state; }


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
    public void autoRegenerate(float delta) {
        if(autoRegenerate && System.currentTimeMillis() - lastDamage >= (damageDelay + 1.5f) * 1000) health += 5 * delta;
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

    @Override
    public boolean getAutoRecover() { return autoRecover; }

    @Override
    public boolean isTireless() { return tireless; }

    // Setters

    @Override
    public void setStamina(float stamina) { this.stamina = stamina; }

    @Override
    public void setMaxStamina(int maxStamina) { this.maxStamina = maxStamina; }

    @Override
    public void setTireless(boolean tireless) { this.tireless = tireless; }

    @Override
    public void setAutoRecover(boolean state) { this.autoRecover = state; }


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
    public void autoRecover(float delta) {
        if(autoRecover && System.currentTimeMillis() - lastSprint >= (sprintDelay + 1.5f) * 1000) stamina += 5 * delta;
        if(stamina > maxStamina) stamina = maxStamina;
        if(stamina < 0) stamina = 0;  
    }


    // Get loot from mob
    public void throwLoot(MobEntity mob, Terrain terrain) {

        int loopCount = RandomUtils.randomInt(1, 3);

        for(int i = 0; i < loopCount; i++) {

            Item itemType = ItemRegister.getItemList().get(RandomUtils.randomInt(0, ItemRegister.getItemList().size() - 1));
            if(!itemType.canRoll()) continue;
            
            int itemCount = RandomUtils.randomInt(1, 3);

            for(int j = 0; j < itemCount; j++) {
                terrain.addEntity(new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), soundManager, itemType, mob));
            }

        }

    }


    // Climb up method
    public void tryClimbUp(Terrain terrain) {

        TileStack tileStack = terrain.getWorldTileStack((int) x / Constants.GRID_SIZE, (int) y / Constants.GRID_SIZE);
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

        TileStack tileStack = terrain.getWorldTileStack((int) x / Constants.GRID_SIZE, (int) y / Constants.GRID_SIZE);
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
    public Inventory getInventory() { return inventory; }

    @Override
    public String toString() {
        return "MobEntity(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
