package io.kyrixen.tinyblox.entities.inventory;

public class Inventory {

    private byte hotbarSlot = 0;
    private final ItemStack[] slots;

    public Inventory(byte slotCount) {
        slots = new ItemStack[slotCount];
        for(byte i = 0; i < slots.length; i++) { slots[i] = new ItemStack(ItemRegister.NONE, (byte) 0); }
    }


    // Setters //

    public void setCurrentSlot(byte slot) {
        hotbarSlot = slot;
        if(slot >= slots.length || slot < 0) hotbarSlot = 0;
    }

    public void push(ItemStack itemStack) {
        for(byte i = 0; i < slots.length; i++) {
            if(slots[i].isEmpty()) { slots[i] = itemStack; return; }
        }
    }

    public boolean add(Item item, byte count) {

        ItemStack itemStack = getAvailableStack(item);

        if(itemStack == null) return false;
        if(itemStack.isEmpty()) itemStack.setItem(item);

        itemStack.add(count);
        
        return true;

    }

    public void remove(Item item, byte count) {

        ItemStack itemStack = getStack(item);

        if(itemStack == null) return;

        itemStack.remove(count);

        if(itemStack.isEmpty()) itemStack.setItem(ItemRegister.NONE);

    }

    public void set(Item item, byte count) {

        ItemStack itemStack = getStack(item);

        if(itemStack == null) itemStack = getSlot(getEmptySlot());
        if(itemStack == null) return;

        itemStack.setItem(item);
        itemStack.setCount(count);

    }


    // Getters //

    public Item currentItem() {
        return slots[hotbarSlot].getItem();
    }

    public ItemStack getCurrentStack() {
        return slots[hotbarSlot];
    }

    public byte getCurrentSlot() {
        return hotbarSlot;
    }

    public byte getMaxStorage() {
        return (byte) slots.length;
    }

    public ItemStack getSlot(byte position) {

        if(position >= this.getMaxStorage()) return null;
        return slots[position];

    }

    public void nextSlot() {
        hotbarSlot++;
        if(hotbarSlot >= slots.length) hotbarSlot = 0;
    }

    public void previousSlot() {
        hotbarSlot--;
        if(hotbarSlot < 0) hotbarSlot = (byte) (this.getMaxStorage() - 1);
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

    public int getTotalItemCount(Item item) {

        int total = 0;

        for(ItemStack itemStack : slots) {
            if(itemStack.getItem() != item) continue;
            total += itemStack.getCount();
        }

        return total;

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

    public void clear() {
        for(byte i = 0; i < slots.length; i++) { slots[i] = new ItemStack(ItemRegister.NONE, (byte) 0); }
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("Inventory {\n");

        for(byte i = 0; i < this.getMaxStorage(); i++) {

            if(i == hotbarSlot) builder.append("> ");
            else builder.append("  ");

            builder.append("Slot " + i + " { item=" + this.getSlot(i).getItem() + ", count=" + this.getSlot(i).getCount() + "} \n");
        
        }

        builder.append("}");

        return builder.toString();

    }

}
