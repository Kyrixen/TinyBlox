package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class AngryPumpkin extends Enemy {
    
    public AngryPumpkin(float x, float y, SoundManager soundManager) {
    
        super(x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox_halloween", IdentifierType.ENTITY, "angry_pumpkin");

        this.attackDamage = 40 * (int) Constants.DIFFICULTY.getDiffMult();

        this.maxHealth = (int) (60 / Constants.DIFFICULTY.getDiffMult());
        this.health = 40 / Constants.DIFFICULTY.getDiffMult();

        this.activationRange = 1;

        this.setSpeed(Speed.NORMAL);

    }

    public AngryPumpkin(int id, float x, float y, SoundManager soundManager) {
    
        super(id, x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox_halloween", IdentifierType.ENTITY, "angry_pumpkin");

        this.attackDamage = 40 * (int) Constants.DIFFICULTY.getDiffMult();

        this.maxHealth = (int) (50 * Constants.DIFFICULTY.getDiffMult());
        this.health = 40 * Constants.DIFFICULTY.getDiffMult();

        this.activationRange = 1;

        this.autoRegenerate = true;

        this.setSpeed(Speed.SPEEDY);

    }


    @Override
    public void initTexture() {
        this.texture = new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "angry_pumpkin");
    }


    @Override
    public String toString() {
        return "Angry Pumpkin(" + this.currentID + ")[" + this.entityID + "] { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", chasing:" + Boolean.toString(this.chasing) + ", moving: " + Boolean.toString(this.moving) + " IIIMMM PUMPKIIIIIN! }";
    }


}
