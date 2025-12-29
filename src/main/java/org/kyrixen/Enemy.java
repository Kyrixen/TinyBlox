package org.kyrixen;


import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.Random;


public class Enemy extends Entity {

    private Random random = new Random();

    float lastDelay = 0.0f;
    float moveDelay = 0.50f;

    Entity target;
    boolean chasing;


    public Enemy(int x, int y, int width, int height) {
        
        super(x, y, width, height);
        this.type = "enemy";
        this.chasing = false;

        this.damageDelay = 0.25f;

        this.health = 50;
        this.maxHealth = 50;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = true;

        lastDelay = (float) glfwGetTime();
    
    }


    @Override
    public int initTexture(Textures textures) {
       this.texture = textures.enemyTexture;
       return this.texture;
    }


    public void setTarget(Entity target) {
        this.target = target;
    }


    public void setChasing(boolean chasing) {
        this.chasing = chasing;
    }


    @Override
    public void update(float deltaTime, long window) {
      
        if(glfwGetTime() - lastDelay >= moveDelay) {   
        
            if(chasing){

                // If there is no target, do nothing
                if (target == null) return;

                // Calculate distance to target
                int dx = target.x - this.x;
                int dy = target.y - this.y;

                // Decide which direction to move
                if (Math.abs(dx) > Math.abs(dy)) {

                    // Move LEFT or RIGHT
                    if (dx > 0) {
                        dirX = 1;   // move right
                    } else {
                        dirX = -1;  // move left
                    }

                } else if (dy != 0) {

                    // Move UP or DOWN
                    if (dy > 0) {
                        dirY = 1;   // move down
                    } else {
                        dirY = -1;  // move up
                    }

                }
                // If dx == 0 and dy == 0 → already at target → no movement



        
            } else{

                 
            
                int direction = random.nextInt(4) + 1;
        

                switch (direction) {
                    case 1:
                        dirY = -1; // Up
                        break;
                    case 2:
                        dirY = 1; // Down
                        break;
                    case 3:
                        dirX = -1; // Left
                        break;
                    case 4:
                        dirX = 1; // Right
                        break;
                }
            
            }

            
            x += dirX * width;
            y += dirY * height;


            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x + width > Constants.MAP_WIDTH) x = Constants.MAP_WIDTH - width;
            if (y + height > Constants.MAP_HEIGHT) y = Constants.MAP_HEIGHT - height;
 
            
            if(stamina <= 0 && !tireless){ this.exhausted = true; }
            else { this.exhausted = false; }

            lastDelay = (float) glfwGetTime();

            // Reset movement
            dirX = 0;
            dirY = 0;

        }

    }

    @Override
    public void render(Textures textures) {
        textures.drawVBO(texture, x, y, width, height);
    }

}
