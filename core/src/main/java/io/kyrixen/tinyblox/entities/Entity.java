package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.TerrainCollision;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer.FlipType;


// Implement stats
public class Entity {

    // Entity speed
    public enum Speed {

        NONE(Float.MAX_VALUE),
        SNAIL(1.15f),
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
    protected final int width;
    protected final int height;

    // Height
    protected byte level = 1;

    // Identifier
    protected final int id;
    
    // Directions
    protected int dirX = 0;
    protected int dirY = 0;
    
    // Movement
    protected boolean moving = false;
    protected Speed speed = Speed.SLOW;
    protected long lastMove = 0L;
    protected FlipType flip = FlipType.NONE;

    // Texture and type of entity
    protected TextureID texture = null;

    // Constructs entity
    public Entity(int x, int y, int w, int h) {

        this.id = MiscUtils.generateEntityID();
        
        this.x = x;
        this.y = y;

        this.width = w;
        this.height = h;

        this.setSpeed(Speed.SLOW);
        this.updateFlip();

    }


    // Get texture (default entity texture)
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "entity");
    }

    // Tries to move
    protected boolean tryMove(Terrain terrain){
        moving = TerrainCollision.queryMove(this, terrain);
        return moving;
    }

    protected void updateFlip() {
        if(dirX < 0) this.flip = FlipType.X_AXIS;
        else if(dirX > 0) this.flip = FlipType.NONE;
    }


    // Update entity
    public void update(float deltaTime, Terrain terrain) {

        TileStack entityStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(entityStack == null) return;
        
        Tile belowTile = entityStack.get((byte) (level() - 1));
        if(belowTile == null || !belowTile.type().canSupport()) {

            for(byte nextLevel = this.level(); nextLevel > Constants.MIN_WORLD_HEIGHT; nextLevel--) {

                Tile nextTile = entityStack.get(nextLevel);

                if(nextTile == null) continue;
                if(nextTile.type().isEmpty()) continue;
                if(!nextTile.type().canSupport()) continue;

                this.setLevel((byte)(nextLevel + 1));
                break;
            
            }

            belowTile = entityStack.get((byte)(level() - 1));
            if(belowTile == null || !belowTile.type().canSupport()) return;

        }

        if(System.currentTimeMillis() - lastMove >= speed.getMoveDelay() / belowTile.type().getSlipperyModifier() * 1000) {
        
            updateFlip();
            
            if(dirX == 0 && dirY == 0) { moving = false; return; }
            this.moving = tryMove(terrain);

            if(moving) onMove();

            // Resets dirs
            dirX = 0;
            dirY = 0;

            lastMove = System.currentTimeMillis();
        
        }

    }

    // Render entity
    public void render(Terrain terrain, Player player, TileRenderer tileRenderer, RendererStack rendererStack){

        SpriteBatch batch = rendererStack.batch;

        // Get brightness
        Color brightnessColor = new Color(terrain.getLightColor(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE, level()));

        brightnessColor.r = 0.5f + brightnessColor.r * 0.5f;
        brightnessColor.g = 0.5f + brightnessColor.g * 0.5f;
        brightnessColor.b = 0.5f + brightnessColor.b * 0.5f;

        int levelDiff = level() - player.level();
        
        float alpha = 1f;
        alpha = MathUtils.clamp(1f - levelDiff * 0.15f, 0.4f, 1f);

        if(this != player) {

            float multiplier = 1f + levelDiff * 0.15f;
            multiplier = MathUtils.clamp(multiplier, 0.3f, 1.5f);

            brightnessColor.r *= multiplier;
            brightnessColor.g *= multiplier;
            brightnessColor.b *= multiplier;

        }

        batch.setColor(brightnessColor.r, brightnessColor.g, brightnessColor.b, alpha);
        tileRenderer.draw(this.texture, x, y, this.flip, rendererStack);
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
    public static void renderAll(Terrain terrain, Player player, TileRenderer tileRenderer, ArrayList<Entity> entities, RendererStack rendererStack) {
        for (Entity e : entities) {
            e.render(terrain, player, tileRenderer, rendererStack);
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
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }


    protected void onMove() {}


    @Override
    public String toString() {
        return "Entity(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
