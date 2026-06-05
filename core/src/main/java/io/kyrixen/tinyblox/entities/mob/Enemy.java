package io.kyrixen.tinyblox.entities.mob;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Terrain;

public class Enemy extends MobEntity {

    // What entity it targets (99% it is player but why not :D)
    private MobEntity target;

    // If its chasing entity
    private boolean chasing;

    private final SoundID HIT_PLAYER_SOUND = new SoundID("tinyblox", SoundType.SFX, "hit_player");
    private final SoundID EXPLOSION_SOUND = new SoundID("tinyblox", SoundType.SFX, "explosion");


    public Enemy(int id, int x, int y, SoundManager soundManager) {
        
        super(id, x, y, soundManager);

        this.chasing = false;

        this.damageDelay = 0.50f;

        this.health = 50;
        this.maxHealth = 50;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = true;

        this.lastMove = System.currentTimeMillis();
    
    }


    @Override
    public void initTexture() {
       this.texture = new TextureID("tinyblox", TextureType.ENTITY, "enemy");
    }


    @Override
    public void update(float deltaTime, Terrain terrain) {

        if(System.currentTimeMillis() - lastMove < speed.getMoveDelay() * 1000) return;  

        if(chasing){
            chaseTarget();
        } else{
            wanderAround();
        }
        

        updateFlip();
        moving = tryMove(terrain);

        exhausted = stamina <= 0 && !tireless;

        lastMove = System.currentTimeMillis();

        // Reset movement
        dirX = 0;
        dirY = 0;

    }


    // Enemy specific code //

    // Setters

    public void setTarget(MobEntity target) {
        this.target = target;
    }


    public void setChasing(boolean chasing) {
        this.chasing = chasing;
    }


    // Checks collision
    public void check(MobEntity player){

        if (EntityCollision.checkTileCollision(player, this)) {
                        
            Logger.LOGGER.debug("ENTITY", "Collision detected between player and enemy!");

            if(player.damage(25)) soundManager.getSound(HIT_PLAYER_SOUND).play(Utils.getFloatSound(40), MathUtils.random(0.85f, 1.15f), 0f); 
                        
        }

    }

    // Get loot from enemy
    public void throwLoot(MobEntity mob, ArrayList<Entity> entities) {

        soundManager.getSound(EXPLOSION_SOUND).play(Utils.getFloatSound(35), MathUtils.random(0.85f, 1.25f), 0f);
        int loopCount = MathUtils.random(1, 3);

        for(int i = 0; i < loopCount; i++) {

            Item itemType = ItemRegister.getItemList().get(MathUtils.random(0, ItemRegister.getItemList().size() - 1));
            if(!itemType.canRoll()) continue;
            
            int itemCount = MathUtils.random(1, itemType.getMaxSize());
            itemCount = MathUtils.clamp(itemCount, 0, 8);

            for(int j = 0; j < itemCount; j++) {
                entities.add(new ItemEntity(Utils.generateEntityID(), this.x() + MathUtils.random(-3, 3), this.y() + MathUtils.random(-3, 3), soundManager, itemType, mob));
            }

        }

    }

    // Wander logic
    private void wanderAround() {

        int direction = MathUtils.random(1, 4);

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

    // Chase the target (Yeah this is the one of three or two parts which is ai generated (will be reworked better by human))
    private void chaseTarget() {

        // If there is no target, do nothing
        if (target == null) return;

        // Calculate distance to target
        int dx = target.x() - this.x;
        int dy = target.y() - this.y;

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

        // If dx == 0 and dy == 0 -> already at target -> no movement

    }

    @Override
    public String toString() {
        return "Enemy(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
