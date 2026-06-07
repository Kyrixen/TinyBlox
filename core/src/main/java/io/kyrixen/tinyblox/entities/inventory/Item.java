package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

// Item object used for inventory
public class Item {
    
    // Item vars //

    private final String name;
    private final byte maxSize;
    private final TextureID textureID;
    private final int itemID;
    private final boolean obtainable;
    private final TileType tileVariant;

    
    // Constructor for Item
    public Item(String name, int itemID, TextureID textureID, boolean obtainable, byte maxSize) {
        this.name = name;
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariant = null;
    }
    
    // Constructor for Item / Tile
    public Item(String name, int itemID, TextureID textureID, boolean obtainable, byte maxSize, TileType tile) {
        this.name = name;
        this.maxSize = maxSize;
        this.obtainable = obtainable;
        this.textureID = textureID;
        this.itemID = itemID;
        this.tileVariant = tile;
    }
    

    // Getters //

    public String getItemName() { return this.name; }
    public byte getMaxSize() { return this.maxSize; }

    public TextureID textureID() { return this.textureID; }
    public int getItemID() { return this.itemID; }

    public boolean canPlace() { return this.tileVariant != null; }
    public boolean canRoll() { return this.obtainable; }

    public TileType getTileVariant() { return this.tileVariant; }


    // Override
    @Override
    public String toString() {
        return "Item { " + this.name.toUpperCase() + " }";
    }

}
