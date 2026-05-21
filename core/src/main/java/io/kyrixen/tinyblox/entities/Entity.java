package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.collision.TerrainCollision;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.TileRenderer;


// Implement stats
public class Entity {

    // Entity speed
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
    protected int x;
    protected int y;

    // Dimensions
    protected int width;
    protected int height;

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
    protected long lastMove = 0L;

    // Texture and type of entity
    protected TextureID texture = null;

    // Constructs entity
    public Entity(int id, int x, int y, int w, int h) {

        this.id = id;
        
        this.x = x;
        this.y = y;

        this.width = w;
        this.height = h;

        this.setSpeed(Speed.SLOW);

    }


    // Get texture (default entity texture)
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "entity");
    }

    // Tries to move
    public boolean tryMove(Terrain terrain){
        moving = TerrainCollision.queryMove(this, terrain);
        return moving;
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

    }

    // Render entity
    public void render(TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch){

        // Get brightness
        float brightness = 0.5f + timeCycle.getBrightness() * 0.5f;

        batch.setColor(brightness, brightness, brightness, 1.0f);
        tileRenderer.draw(this.texture, x, y, batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

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


    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setLevel(byte level) { this.level = level; }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

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
