package org.kyrixen;


import static org.lwjgl.glfw.GLFW.glfwGetTime;


public class Entity implements Stats.Health, Stats.Stamina {

    int x;
    int y;
    int width;
    int height;
    
    protected int dirX = 0;
    protected int dirY = 0;
    protected int lastDirX = 0;
    protected int lastDirY = 0;

    int texture;
    String type;

    protected float damageDelay = 0.50f;
    protected float lastDamage = 0.0f;

    protected int health;
    protected int maxHealth;
    protected boolean invincible = true;
        
    protected int stamina = 100;
    protected int maxStamina = 100;

    protected boolean exhausted = false;
    protected boolean tireless = true;


    public Entity(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.texture = 0;

        this.type = "default";

    }

 
    public int initTexture(Textures textures) {
        this.texture = textures.entityTexture;
        return this.texture;
    }


    public void update(float deltaTime, long window) {

        x += dirX * width;
        y += dirY * height;


        dirX = 0;
        dirY = 0;

        if(stamina <= 0 && !tireless){ this.exhausted = true; }
        else { this.exhausted = false; }

    }


    public void render(Textures textures){
        textures.drawVBO(this.texture, x, y, width, height);
    }


    // Health interface methods
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
    public void damage(int damage) {
    
        if(!invincible) {
            
            if(glfwGetTime() - lastDamage >= damageDelay) {
            
                Sound.hit.stop();
                Sound.hit.reset();
                Sound.hit.play();    

                this.health -= damage;
                
                if(this.health < 0) this.health = 0;
            
                lastDamage = (float) glfwGetTime();
            
            } else {} 
        
        }
    
    }


    @Override
    public void heal(int amount) {
    
        this.health += amount;
        if(this.health > maxHealth) this.health = maxHealth;
    
    }


    // Stamina interface methods
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



}