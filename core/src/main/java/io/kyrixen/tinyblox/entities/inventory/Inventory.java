package io.kyrixen.tinyblox.entities.inventory;

public class Inventory {

    private byte hotbarSlot = 0;
    private final ItemStack[] slots;

    public Inventory(byte slotCount) {
        slots = new ItemStack[slotCount];
        for(byte i = 0; i < slots.length; i++) { slots[i] = new ItemStack(Item.NONE, (byte) 0); }
    }


    // Setters //

    public void push(ItemStack itemStack) {
        for(byte i = 0; i < slots.length; i++) {
            if(slots[i].isEmpty()) { slots[i] = itemStack; return; }
        }
    }

    public void add(Item item, byte count) {

        ItemStack itemStack = getAvailableStack(item);

        if(itemStack == null) return;
        if(itemStack.isEmpty()) itemStack.setItem(item);

        itemStack.add(count);

    }

    public void remove(Item item, byte count) {

        ItemStack itemStack = getStack(item);

        if(itemStack == null) return;

        itemStack.remove(count);

        if(itemStack.isEmpty()) itemStack.setItem(Item.NONE);

    }

    public void set(Item item, byte count) {

        ItemStack itemStack = getStack(item);

        if(itemStack == null) return;

        itemStack.setCount(count);

    }


    // Getters //

    public Item currentItem() {
        return slots[hotbarSlot].getItem();
    }

    public ItemStack currentStack() {
        return slots[hotbarSlot];
    }

    public byte getMaxStorage() {
        return (byte) slots.length;
    }

    public void nextSlot() {
        hotbarSlot++;
        if(hotbarSlot >= slots.length) hotbarSlot = 0;
    }

    public void previousSlot() {
        hotbarSlot--;
        if(hotbarSlot < 0) hotbarSlot = (byte) (slots.length - 1);
    }

        public byte getEmptySlot() {
        for(byte i = 0; i < slots.length; i++) {
            if(slots[i].isEmpty()) return i;
        }
        return -1;
    }

    public ItemStack getStack(Item item) {
        for(ItemStack itemStack : slots) {
            if(itemStack.getItem() == item && !itemStack.isEmpty()) return itemStack;
        }
        return null;
    }

    public ItemStack getAvailableStack(Item item) {

        for(ItemStack itemStack : slots) {
            if(itemStack.getItem() == item && !itemStack.isFull()) return itemStack;
        }

        byte emptySlot = getEmptySlot();

        if(emptySlot != -1) return slots[emptySlot];

        return null;

    }

    public boolean isEmpty() {
        for(ItemStack itemStack : slots) { if(!itemStack.isEmpty()) return false; }
        return true;
    }

    public boolean isFull() {
        for(ItemStack itemStack : slots) { if(!itemStack.isFull()) return false; }
        return true;
    }

    public boolean contains(Item item) {
        for(ItemStack itemStack : slots) {
            if(itemStack.getItem() == item && !itemStack.isEmpty()) { return true; }
        }
        return false;
    }

}