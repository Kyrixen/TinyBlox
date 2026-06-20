package io.kyrixen.tinyblox.entities.mob;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Bomber extends Enemy {
    
    // Dont even try to make it int and do a 1000, trust me - Kyrixen
    private final byte EXPLOSION_RADIUS = 4;
    
    private boolean activated = false;

    private final static SoundID DETONATE_SOUND = new SoundID("tinyblox", SoundType.SFX, "bomber_detonate");

    private final float detonate_time = 1.2f;
    private long firstImpulse = 0L;


    public Bomber(int x, int y, SoundManager soundManager) {
    
        super(x, y, soundManager);
    
        this.attackDamage = 65;

        this.maxHealth = 15;
        this.health = 15;

        this.activationRange = 2;

        this.setSpeed(Speed.SPEEDY);

    }

    public Bomber(int id, int x, int y, SoundManager soundManager) {
    
        super(id, x, y, soundManager);
    
        this.attackDamage = 65;

        this.maxHealth = 15;
        this.health = 15;

        this.activationRange = 2;

        this.setSpeed(Speed.SPEEDY);

    }


    @Override
    public void render(Terrain terrain, Player player, TileRenderer tileRenderer, RendererStack rendererStack) {

        if(!activated) { super.render(terrain, player, tileRenderer, rendererStack); return; }

        SpriteBatch batch = rendererStack.batch;

        // Get brightness
        Color brightnessColor = new Color(terrain.getLightColor(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE, level()));

        brightnessColor.r = 0.5f + brightnessColor.r * 0.5f;
        brightnessColor.g = 0.5f + brightnessColor.g * 0.5f;
        brightnessColor.b = 0.5f + brightnessColor.b * 0.5f;


        int levelDiff = level() - player.level();
        
        float alpha = 1f;
        alpha = MathUtils.clamp(1f - levelDiff * 0.15f, 0.4f, 1f);

        float multiplier = 1f + levelDiff * 0.15f;
        multiplier = MathUtils.clamp(multiplier, 0.3f, 1.5f);

        brightnessColor.r *= multiplier;
        brightnessColor.g *= multiplier;
        brightnessColor.b *= multiplier;


        float progress = (System.currentTimeMillis() - firstImpulse) / (detonate_time * 1000f);
        progress = MathUtils.clamp(progress, 0f, 1f);

        long interval = (long) (150 - progress * 120);
        interval = Math.max(interval, 30);

        boolean blink = (System.currentTimeMillis() / interval) % 2 == 0;

        if(blink) {
            brightnessColor.r *= 1.8f;
            brightnessColor.g *= 1.0f;
            brightnessColor.b *= 0.3f;
        }


        batch.setColor(brightnessColor.r, brightnessColor.g, brightnessColor.b, alpha);
        tileRenderer.draw(this.texture, x, y, this.flip, rendererStack);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    @Override
    public void update(float deltaTime, Terrain terrain) {
        
        if(!activated) { super.update(deltaTime, terrain); return; }

        if(System.currentTimeMillis() - firstImpulse < detonate_time * 1000) return;

        explode(terrain);

        kill();

    }


    // Bomber explode func
    private void explode(Terrain terrain) {

        TileStack bomberStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(bomberStack == null) return;
        Tile belowBomberTile = bomberStack.get((byte) (level - 1));
        if(belowBomberTile == null) return;

        if(belowBomberTile.type() == TileType.WATER) return;

        int radius = EXPLOSION_RADIUS * Constants.GRID_SIZE;

        for(int nextWorldX = x - radius; nextWorldX <= x + radius; nextWorldX += Constants.GRID_SIZE) {
            for(int nextWorldY = y - radius; nextWorldY <= y + radius; nextWorldY += Constants.GRID_SIZE) {

                TileStack currentStack = terrain.getWorldTileStack(nextWorldX / Constants.GRID_SIZE, nextWorldY / Constants.GRID_SIZE);
                if(currentStack == null) continue;

                for(byte layer = (byte) (level() - EXPLOSION_RADIUS); layer < level() + EXPLOSION_RADIUS; layer++) {

                    int dx = nextWorldX - x;
                    int dy = nextWorldY - y;
                    int dz = (layer - level()) * Constants.GRID_SIZE;

                    if(dx * dx + dy * dy + dz * dz > radius * radius) continue;

                    Tile currentTile = currentStack.get(layer);
                    if(currentTile == null || currentTile.type() == TileType.WATER || currentTile.level() < 2) continue;

                    currentStack.removeAtLayer(layer);

                } 

            }
        }

    }

    @Override
    public void checkPlayer(Player player) {
        super.updateTarget(player);
        if(!activated && EntityCollision.checkEntityDistance(player, this) <= 2.0f && level() == player.level()) { activated = true; firstImpulse = System.currentTimeMillis(); player.damage(attackDamage); soundManager.getSound(DETONATE_SOUND).play(MiscUtils.getFloatSound(45), RandomUtils.randomFloat(0.90f, 1.1f), 0f); }
    }


    @Override
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "bomber");
    }


    @Override
    public String toString() {
        return "Bomber(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
