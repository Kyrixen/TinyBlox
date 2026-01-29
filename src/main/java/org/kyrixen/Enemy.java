package org.kyrixen;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Enemy extends Entity {

    private Random random = new Random();


    Entity target;
    boolean chasing;


    public Enemy(int id, int x, int y, int width, int height, Terrain terrain, SoundManager soundManager) {
        
        super(id, x, y, width, height, terrain, soundManager);
        this.type = "enemy";
        this.chasing = false;
        this.terrain = terrain;

        this.damageDelay = 0.50f;

        this.health = 50;
        this.maxHealth = 50;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = true;

        lastDelay = System.currentTimeMillis();
    
    }


    @Override
    public BufferedImage initTexture(Textures textures) {
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
    public void update(float deltaTime) {
      
        if(System.currentTimeMillis() - lastDelay >= moveDelay * 1000) {   
        
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

            
            tryMove(terrain);
 
            
            if(stamina <= 0 && !tireless){ this.exhausted = true; }
            else { this.exhausted = false; }

            lastDelay = System.currentTimeMillis();

            // Reset movement
            dirX = 0;
            dirY = 0;

        }

    }

    @Override
    public void render(Textures textures, Renderer renderer, Camera camera, Graphics2D g) {
        textures.draw(texture, x, y, width, height, g);
    }

    // Checks collision
    public void check(Entity player){

        if (Utils.checkCollision(player, this)) {
                        
            System.out.println("Collision detected between player and enemy!");

            if(!soundManager.hitplayer.isRunning() && player.damage(25)) soundManager.hitplayer.play();
                        
        }

    }


    @Override
    public void cleanup(){

        random = null;
        soundManager = null;

        lastDelay = 0L;
        moveDelay = 0.0f;

        target = null;
        chasing = false;


        texture = null;

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


}
