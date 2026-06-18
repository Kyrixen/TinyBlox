package io.kyrixen.tinyblox.entities.mob;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Voidling extends Enemy {
    
    private Player player = null;

    public Voidling(int x, int y, SoundManager soundManager) {
        
        super(x, y, soundManager);

        this.maxHealth = 60;
        this.health = 60;

        this.attackDamage = 25;

        this.activationRange = 3;

        this.setSpeed(Speed.SNAIL);

    }

    @Override
    public void update(float deltaTime, Terrain terrain) {

        super.update(deltaTime, terrain);

        if(player != null) teleportPlayer(player, terrain);

    }

    @Override
    public void render(Terrain terrain, Player player, TileRenderer tileRenderer, RendererStack rendererStack) {

        SpriteBatch batch = rendererStack.batch;

        // Get brightness
        Color brightnessColor = new Color(terrain.getLightColor(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE, level()));

        brightnessColor.r = 0.5f + brightnessColor.r * 0.5f;
        brightnessColor.g = 0.5f + brightnessColor.g * 0.5f;
        brightnessColor.b = 0.5f + brightnessColor.b * 0.5f;


        int levelDiff = level() - player.level();
        
        float alpha = 0.45f;
        alpha = MathUtils.clamp(1f - levelDiff * 0.15f, 0.15f, 0.6f);

        float multiplier = 1f + levelDiff * 0.15f;
        multiplier = MathUtils.clamp(multiplier, 0.3f, 1.5f);

        brightnessColor.r *= multiplier;
        brightnessColor.g *= multiplier;
        brightnessColor.b *= multiplier;

        batch.setColor(brightnessColor.r, brightnessColor.g, brightnessColor.b, alpha);
        tileRenderer.draw(this.texture, x, y, this.flip, rendererStack);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    private void teleportPlayer(Player player, Terrain terrain) {

        for(int attempt = 0; attempt < 100; attempt++) {

            int choosenX = RandomUtils.randomInt(player.x() - 100 * Constants.GRID_SIZE, player.x() + 100 * Constants.GRID_SIZE);
            int choosenY = RandomUtils.randomInt(player.y() - 100 * Constants.GRID_SIZE, player.y() + 100 * Constants.GRID_SIZE);
            choosenX -= choosenX % Constants.GRID_SIZE;
            choosenY -= choosenY % Constants.GRID_SIZE;

            TileStack choosenStack = terrain.getWorldTileStack(choosenX / Constants.GRID_SIZE, choosenY / Constants.GRID_SIZE);
            if(choosenStack == null) continue;

            if(choosenStack.height() >= Constants.MAX_WORLD_HEIGHT) continue;
            Tile choosenBelowTile = choosenStack.getTopTerrain();
            if(choosenBelowTile == null) continue;
        
            Tile choosenTile = choosenStack.get((byte) (choosenBelowTile.level() + 1));
            if(choosenTile != null && !choosenTile.type().isEmpty()) continue;

            player.setX(choosenX);
            player.setY(choosenY);
            player.setLevel((byte) (choosenBelowTile.level() + 1));

            this.player = null;

            return;

        }
    
    }

    @Override
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "voidling");
    }

    @Override
    public void checkPlayer(Player player) {

        if(this.player != null) return;
        if(!EntityCollision.checkTileCollision(player, this)) return;

        this.player = player;
        player.damage(attackDamage);

    }

    @Override
    public String toString() {
        return "Voidling(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
