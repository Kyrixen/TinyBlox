package io.kyrixen.tinyblox.entities.inventory;

public class ItemStack {
    
    private Item item;
    private byte count;

    public ItemStack(Item item, byte count) {
        this.item = item;
        this.count = count;
    }

    // Getters //

    public boolean isEmpty() {
        return this.count <= 0 || this.item == Item.NONE;
    }

    public boolean isFull() {
        return this.count >= item.getMaxSize();
    }

    public Item getItem() {
        return this.item;
    }

    public byte getCount() {
        return this.count;
    }

    // Setters //

    public void add(byte count) {
        this.count += count;
        if(this.count > item.getMaxSize()) { this.count = item.getMaxSize(); }
    }

    public void remove(byte count) {
        this.count -= count;
        if(this.count <= 0) { this.count = 0; this.item = Item.NONE; }
    }

    public void setCount(byte amount) {
        this.count = amount;
        if(this.count <= 0) { this.count = 0; this.item = Item.NONE; }
    }

    public void setItem(Item item) {
        this.item = item;
        if(this.item == Item.NONE) this.count = 0;
    }

}
