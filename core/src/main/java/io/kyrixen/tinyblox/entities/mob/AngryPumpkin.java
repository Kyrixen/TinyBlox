package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.inventory.Item;
import io.kyrixen.tinyblox.inventory.ItemRegister;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.Terrain;

public class AngryPumpkin extends Enemy {
    
    private static final Item[] loot_drops = new Item[]{ItemRegister.PUMPKIN, ItemRegister.NONE, ItemRegister.WOOD, ItemRegister.COAL};


    public AngryPumpkin(float x, float y, SoundManager soundManager) {
    
        super(x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox_halloween", IdentifierType.ENTITY, "angry_pumpkin");

        this.attackDamage = 20 * (int) Constants.DIFFICULTY.getDiffMult();

        this.maxHealth = (int) (40 / Constants.DIFFICULTY.getDiffMult());
        this.health = 30 / Constants.DIFFICULTY.getDiffMult();

        this.activationRange = 1;

        this.setSpeed(Speed.NORMAL);

    }

    public AngryPumpkin(int id, float x, float y, SoundManager soundManager) {
    
        super(id, x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox_halloween", IdentifierType.ENTITY, "angry_pumpkin");

        this.attackDamage = 25 * (int) Constants.DIFFICULTY.getDiffMult();

        this.maxHealth = (int) (50 * Constants.DIFFICULTY.getDiffMult());
        this.health = 40 * Constants.DIFFICULTY.getDiffMult();

        this.activationRange = 1;

        this.autoRegenerate = true;

        this.setSpeed(Speed.NORMAL);

    }


    @Override
    public void initTexture() {
        this.texture = new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "angry_pumpkin");
    }


    @Override 
    public void throwLoot(MobEntity mob, Terrain terrain) {

        int loopCount = RandomUtils.randomInt(1, 3);

        for(int i = 0; i < loopCount; i++) {

            Item itemType = loot_drops[RandomUtils.randomInt(loot_drops.length - 1)];
            if(!itemType.canRoll()) continue;
            

            terrain.addEntity(new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), soundManager, itemType, mob));

        }

    }


    @Override
    public String toString() {
        return "Angry Pumpkin(" + this.currentID + ")[" + this.entityID + "] { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " IIIMMM PUMPKIIIIIN! }";
    }


}
