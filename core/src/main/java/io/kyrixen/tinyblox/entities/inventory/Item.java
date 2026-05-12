package io.kyrixen.tinyblox.entities.inventory;

// Item object used for inventory
public enum Item {
    
    NONE(0),
    GRASS(24),
    DIRT(24),
    WATER(24),
    STONE(24);

    // Max stackable size
    private final byte maxSize;

    // Constructor
    Item(int maxSize) {
        this.maxSize = (byte) maxSize;
    }

    // Getter
    public byte getMaxSize() { return this.maxSize; }

}
