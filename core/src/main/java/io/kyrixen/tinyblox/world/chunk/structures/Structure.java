package io.kyrixen.tinyblox.world.chunk.structures;

import io.kyrixen.tinyblox.world.chunk.tile.Tile;

// Structure class
public class Structure {

    // Rarity enum
    public enum Rarity {
        
        COMMON(0.10f),
        UNCOMMON(0.04f),
        RARE(0.01f),
        LEGENDARY(0.002f);


        // Chance of spawning
        private final float spawnChance;


        // Setup rarity
        Rarity(float spawnChance) {
            this.spawnChance = spawnChance;
        }


        // Getter
        public float getChance() { return this.spawnChance; }

    }


    // Structure name
    private final String name;

    // Structure dimensions
    private final byte w, h;

    // Rarity
    private final Rarity structureRarity;
    
    // Blocks of the structure
    private final Tile[][] structureMap;


    // Creates structure
    public Structure(String name, byte w, byte h, Rarity rarity, Tile[][] structureMap) {

        this.name = name;
        
        this.w = w;
        this.h = h;
        
        this.structureRarity = rarity;
        
        this.structureMap = structureMap;
    
    }


    // Getters //

    public String getName() { return this.name; }

    public byte getWidth() { return this.w; }
    public byte getHeight() { return this.h; }

    public Rarity getRarity() { return this.structureRarity; }

    public Tile[][] getMap() { return this.structureMap; }


}
