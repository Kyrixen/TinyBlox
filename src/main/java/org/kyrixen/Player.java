package org.kyrixen;


import static org.lwjgl.glfw.GLFW.glfwGetTime;


public class Player extends Entity {

    float lastDelay = 0.0f;
    float moveDelay = 0.30f;


    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "player";

        this.damageDelay = 0.50f;

        this.health = 100;
        this.maxHealth = 100;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = false;

        lastDelay = (float) glfwGetTime();
    }
    

    @Override
    public int initTexture(Textures textures) {
       this.texture = textures.playerTexture;
       return this.texture;
    }


    @Override
    public void update(float deltaTime, long window) {
        
        if(glfwGetTime() - lastDelay >= moveDelay) {

            if(Helper.anyKeyPressed(window)) {
                
                Sound.walk.stop();
                Sound.walk.reset();
                
                Sound.walk.play();

            }


            x += dirX * width;
            y += dirY * height;

            // Keep player within map bounds
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x + width > Constants.MAP_WIDTH) x = Constants.MAP_WIDTH - width;
            if (y + height > Constants.MAP_HEIGHT) y = Constants.MAP_HEIGHT - height;
           
            lastDelay = (float) glfwGetTime();

        }

        dirX = 0;
        dirY = 0;

        if(stamina <= 0 && !tireless){ this.exhausted = true; }
        else { this.exhausted = false; }
    
    }

}