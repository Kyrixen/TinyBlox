package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Slime extends Enemy {
    
    protected final float slimify_delay = 2.30f;
    protected long lastSlimify = 0L;

    public Slime(int x, int y, SoundManager soundManager) {
        
        super(x, y, soundManager);

        this.attackDamage = 10;

        this.maxHealth = 75;
        this.health = 75;

        this.lastSlimify = System.currentTimeMillis();

        this.setSpeed(Speed.SLOW);

    }


    @Override
    public void update(float deltaTime, Terrain terrain) {

        super.update(deltaTime, terrain);

        if(System.currentTimeMillis() - lastSlimify < slimify_delay * 1000) return;

        TileStack slimeStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(slimeStack == null) return;

        byte belowLevel = (byte) (level() - 1);

        Tile belowTile = slimeStack.get(belowLevel);
        if(belowTile == null || !belowTile.type().isTerrain()) return;
        if(belowTile.type() == TileType.SLIME_TILE) return;
        
        slimeStack.set(new Tile(TileType.SLIME_TILE, belowLevel), belowLevel);

        this.lastSlimify = System.currentTimeMillis();

    }


    @Override
    public void initTexture() {
        this.texture = new TextureID("tinyblox", TextureType.ENTITY, "slime");
    }


    @Override
    public String toString() {
        return "Slime(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
