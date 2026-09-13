package io.kyrixen.tinyblox.inventory;

import io.kyrixen.tinyblox.utils.TinyIdentifier;

// Item object used for inventory
public class Item {
    
    // Item vars //

    private final byte maxSize;
    private final TinyIdentifier textureID;
    private final TinyIdentifier itemID;
    private final boolean obtainable;
    private final TinyIdentifier tileVariantID;

    
    // Constructor for Item
    public Item(TinyIdentifier itemID, TinyIdentifier textureID, boolean obtainable, byte maxSize) {
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariantID = null;
    }
    
    // Constructor for Item / Tile
    public Item(TinyIdentifier itemID, TinyIdentifier textureID, boolean obtainable, byte maxSize, TinyIdentifier tileID) {
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariantID = tileID;
    }
    

    // Getters //

    public String getItemName() { return this.itemID.getID(); }
    public byte getMaxSize() { return this.maxSize; }

    public TinyIdentifier textureID() { return this.textureID; }
    public TinyIdentifier getItemID() { return this.itemID; }

    public boolean canPlace() { return this.tileVariantID != null; }
    public boolean canRoll() { return this.obtainable; }

    public TinyIdentifier getTileVariantID() { return this.tileVariantID; }


    // Override
    @Override
    public String toString() {
        return "Item { " + this.itemID.toString().toUpperCase() + " }";
    }

}
