package io.kyrixen.tinyblox.inventory;

import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

// Item object used for inventory
public class Item {
    
    // Item vars //

    private final byte maxSize;
    private final TinyIdentifier textureID;
    private final TinyIdentifier itemID;
    private final boolean obtainable;
    private final TileType tileVariant;

    
    // Constructor for Item
    public Item(TinyIdentifier itemID, TinyIdentifier textureID, boolean obtainable, byte maxSize) {
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariant = null;
    }
    
    // Constructor for Item / Tile
    public Item(TinyIdentifier itemID, TinyIdentifier textureID, boolean obtainable, byte maxSize, TileType tile) {
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariant = tile;
    }
    

    // Getters //

    public String getItemName() { return this.itemID.getID(); }
    public byte getMaxSize() { return this.maxSize; }

    public TinyIdentifier textureID() { return this.textureID; }
    public TinyIdentifier getItemID() { return this.itemID; }

    public boolean canPlace() { return this.tileVariant != null; }
    public boolean canRoll() { return this.obtainable; }

    public TileType getTileVariant() { return this.tileVariant; }


    // Override
    @Override
    public String toString() {
        return "Item { " + this.itemID.toString().toUpperCase() + " }";
    }

}
