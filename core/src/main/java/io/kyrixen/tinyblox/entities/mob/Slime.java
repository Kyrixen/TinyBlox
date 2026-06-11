package io.kyrixen.tinyblox.entities.mob;

import java.util.ArrayList;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class Slime extends Enemy {
    
    protected float slimify_delay = 2.30f;
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

        if(chasing) slimify_delay = 1.15f;
        else slimify_delay = 2.30f;

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
    public void throwLoot(MobEntity mob, ArrayList<Entity> entities) {
        
        soundManager.getSound(EXPLOSION_SOUND).play(MiscUtils.getFloatSound(35), RandomUtils.randomFloat(0.85f, 1.25f), 0f);
    
        int itemCount = RandomUtils.randomInt(1, 6);

        for(int j = 0; j < itemCount; j++) {
            entities.add(new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), soundManager, ItemRegister.SLIME, mob));
        }

    }


    @Override
    public String toString() {
        return "Slime(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
