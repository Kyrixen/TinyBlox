package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundManager;

public class Slime extends Enemy {
    
    public Slime(int x, int y, SoundManager soundManager) {
        
        super(x, y, soundManager);

        this.attackDamage = 10;

        this.maxHealth = 75;
        this.health = 75;
        
        this.setSpeed(Speed.SLOW);

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
