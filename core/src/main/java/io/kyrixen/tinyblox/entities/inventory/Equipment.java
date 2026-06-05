package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;

public class Equipment extends Item {

    // The mining efficency of the equipment
    private final float miningSpeed;
    

    // Constructor
    public Equipment(String name, int itemID, TextureID textureID, boolean obtainable, float miningSpeed) {
        super(name, itemID, textureID, obtainable, (byte) 1);
        this.miningSpeed = miningSpeed;
    }


    // Getters //

    public float getMiningSpeed() { return this.miningSpeed; }

}
