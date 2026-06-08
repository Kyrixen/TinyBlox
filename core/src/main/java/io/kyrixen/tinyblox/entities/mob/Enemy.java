package io.kyrixen.tinyblox.entities.mob;

import java.util.ArrayList;

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
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Enemy extends MobEntity {

    // What entity it targets (99% it is player but why not :D)
    protected MobEntity target;

    // Enemy attack damage
    protected int attackDamage;

    // If its chasing entity
    protected boolean chasing;

    // Count the stuck count
    protected int stuckCounter = 0;

    protected final SoundID HIT_PLAYER_SOUND = new SoundID("tinyblox", SoundType.SFX, "hit_player");
    protected final SoundID EXPLOSION_SOUND = new SoundID("tinyblox", SoundType.SFX, "explosion");


    public Enemy(int x, int y, SoundManager soundManager) {
        
        super(x, y, soundManager);

        this.chasing = false;

        this.attackDamage = 20;
        this.damageDelay = 0.50f;

        this.health = 50;
        this.maxHealth = 50;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = true;

        this.setSpeed(Speed.NORMAL);

        this.lastMove = System.currentTimeMillis();
    
    }


    @Override
    public void initTexture() {
       this.texture = new TextureID("tinyblox", TextureType.ENTITY, "enemy");
    }


    @Override
    public void update(float deltaTime, Terrain terrain) {

        super.update(deltaTime, terrain);

        if(chasing) chaseTarget(terrain);
        else wanderAround(terrain);

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
    public void checkHit(Player player){

        if (EntityCollision.checkTileCollision(player, this)) {
                        
            Logger.LOGGER.debug("ENTITY", "Collision detected between player and enemy!");

            if(player.damage(attackDamage)) soundManager.getSound(HIT_PLAYER_SOUND).play(Utils.getFloatSound(40), RandomUtils.randomFloat(0.85f, 1.15f), 0f); 
                        
        }

    }

    // Get loot from enemy
    public void throwLoot(MobEntity mob, ArrayList<Entity> entities) {

        soundManager.getSound(EXPLOSION_SOUND).play(Utils.getFloatSound(35), RandomUtils.randomFloat(0.85f, 1.25f), 0f);
        int loopCount = RandomUtils.randomInt(1, 3);

        for(int i = 0; i < loopCount; i++) {

            Item itemType = ItemRegister.getItemList().get(RandomUtils.randomInt(0, ItemRegister.getItemList().size() - 1));
            if(!itemType.canRoll()) continue;
            
            int itemCount = RandomUtils.randomInt(1, 3);

            for(int j = 0; j < itemCount; j++) {
                entities.add(new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), soundManager, itemType, mob));
            }

        }

    }


    // Wander logic
    protected void wanderAround(Terrain terrain) {

        int direction;

        for(int attempt = 0; attempt < 4; attempt++) {

            dirX = 0;
            dirY = 0;
            
            direction = RandomUtils.randomInt(1, 4);
            
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

            if(canMoveDirection(terrain)) return;

        }

    }

    // Chase the target
    protected void chaseTarget(Terrain terrain) {

        // Safety check
        if (target == null) return;

        int dx = target.x() - this.x();
        int dy = target.y() - this.y();

        // Calculate distance
        int distX = Math.abs(dx);
        int distY = Math.abs(dy);

        if(stuckCounter > 5) { stuckCounter = 0; wanderAround(terrain); return; }

        // Choose best
        if(distX > distY) {

            if(dx > 0) dirX = 1;
            else dirX = -1;

            if(canMoveDirection(terrain)) { stuckCounter = 0; return; }
            stuckCounter++;

        } else {

            if(dy > 0) dirY = 1;
            else dirY = -1;

            if(canMoveDirection(terrain)) { stuckCounter = 0; return; }
            stuckCounter++;

        }

        wanderAround(terrain);
    
    }

    // Movement helper
    protected boolean canMoveDirection(Terrain terrain) {
            
        int enemyCenterX = x() / width();
        int enemyCenterY = y() / height();

        TileStack enemyStack = terrain.getWorldTileStack(enemyCenterX + dirX, enemyCenterY + dirY);
        if(enemyStack == null) return false;
        
        Tile nextBelowTile = enemyStack.get((byte) (level() - 1));
        Tile nextTile = enemyStack.get(level());
        if(nextBelowTile == null) return false;

        if(nextBelowTile.type().isEmpty() || nextBelowTile.type() == TileType.VOID) return false;
        if(nextTile != null && !nextTile.type().isEmpty()) return false;

        return true;

    }


    @Override
    public String toString() {
        return "Enemy(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
