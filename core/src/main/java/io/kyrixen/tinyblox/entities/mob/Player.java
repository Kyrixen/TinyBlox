// Kyrixen: Sorry i have no energy for explaining.

package io.kyrixen.tinyblox.entities.mob;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.collision.EntityCollision;
import io.kyrixen.tinyblox.crafting.Crafting;
import io.kyrixen.tinyblox.crafting.rendering.CraftingRenderer;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.ItemEntity;
import io.kyrixen.tinyblox.entities.Selector;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.InventoryRenderer;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Player extends MobEntity {

    private boolean inMenu = false;

    private final Selector selector;
    private final Camera camera;

    private final InventoryRenderer inventoryRenderer;
    private final Crafting craftingManager;

    private final SoundID WALK_SOUND = new SoundID("tinyblox", SoundType.HUD, "walk");


    public Player(float x, float y, Camera camera, SoundManager soundManager) {
    
        super(x, y, soundManager);

        this.hotbarSlotCount = 6;
        this.inventory = new Inventory(this.hotbarSlotCount);
        this.inventoryRenderer = new InventoryRenderer(inventory);

        this.craftingManager = new Crafting(this.inventory);

        this.sprintDelay = 0.15f;
        this.damageDelay = 0.40f * Constants.DIFFICULTY.getDiffMult();

        this.setSpeed(Speed.NORMAL);

        this.health = 150 / Constants.DIFFICULTY.getDiffMult();
        this.maxHealth = (int) (150 / Constants.DIFFICULTY.getDiffMult());

        this.stamina = 125 / Constants.DIFFICULTY.getDiffMult();
        this.maxStamina = (int) (125 / Constants.DIFFICULTY.getDiffMult());
        
        this.invincible = false;
        this.tireless = false;

        this.selector = new Selector(this, soundManager);
        this.camera = camera;

        this.lastMove = System.currentTimeMillis();
    
    }
    
    public Player(int id, float x, float y, Camera camera, SoundManager soundManager) {
    
        super(id, x, y, soundManager);

        this.hotbarSlotCount = 6;
        this.inventory = new Inventory(this.hotbarSlotCount);
        this.inventoryRenderer = new InventoryRenderer(inventory);

        this.craftingManager = new Crafting(this.inventory);

        this.sprintDelay = 0.15f;
        this.damageDelay = 0.40f * Constants.DIFFICULTY.getDiffMult();

        this.setSpeed(Speed.NORMAL);

        this.health = 150 / Constants.DIFFICULTY.getDiffMult();
        this.maxHealth = (int) (150 / Constants.DIFFICULTY.getDiffMult());

        this.stamina = 125 / Constants.DIFFICULTY.getDiffMult();
        this.maxStamina = (int) (125 / Constants.DIFFICULTY.getDiffMult());
        
        this.invincible = false;
        this.tireless = false;

        this.autoRegenerate = true;
        this.autoRecover = true;

        this.selector = new Selector(this, soundManager);
        this.camera = camera;

        this.lastMove = System.currentTimeMillis();
    
    }
    

    @Override
    public void initTexture() {
       this.texture = new TextureID("tinyblox", TextureType.ENTITY, "player");
    }

    @Override
    public void update(float deltaTime, Terrain terrain) {

        autoRecover(deltaTime);
        autoRegenerate(deltaTime);

        super.update(deltaTime, terrain);

    }

    public void updateSelector() {
        if(this.inMenu) return;
        selector.update(camera);
    }

    // Checks inventory hotbar change
    public void checkInventoryScrolling(int scroll) {

        if(scroll < 0) inventory.previousSlot();
        if(scroll > 0) inventory.nextSlot();

    }

    // Checks if any item drops near
    public void checkDropPickup(Terrain terrain) {

        if(inventory.isFull()) return;
        
        for(Entity e : terrain.getNearbyEntities((int) x() / Constants.GRID_SIZE, (int) y() / Constants.GRID_SIZE, 1)) {

            if(e == this) continue;
            if(!(e instanceof ItemEntity)) continue;
            
            if(!EntityCollision.checkTileCollision(this, e)) continue;

            ItemEntity itemEntity = (ItemEntity)e;
            Item itemDrop = itemEntity.getItem();

            if(!inventory.add(itemDrop, (byte)1)) continue;

            itemEntity.pickup();
            terrain.removeEntity(itemEntity);

            Logger.LOGGER.debug("PLAYER", "Player inventory: " + inventory.toString());

        }
    
    }

    public void toggleMenuStat() { this.inMenu = !inMenu; }


    public void renderSelector(RendererStack rendererStack) {
        selector.render(rendererStack);
    }

    public void renderInvetory(TextureManager tex, RendererStack rendererStack) {
        inventoryRenderer.render(tex, rendererStack);
    }

    public void renderCraftingMenu(CraftingRenderer craftingRenderer, RendererStack rendererStack) {
        craftingManager.render(craftingRenderer, rendererStack);
    }

    public void drawInventoryHighlight(RendererStack rendererStack) {
        inventoryRenderer.drawHighlight(rendererStack);
    }

    // Overrides //

    @Override
    public boolean damage(int damage) {
    
        if(invincible) return false;        
        if(System.currentTimeMillis() - lastDamage < damageDelay * 1000) return false;

        this.health -= damage;

        // Cap min health
        if(this.health < 0) this.health = 0;
    
        lastDamage = System.currentTimeMillis();
        
        return true;
    
    }


    // Sprinting logic
    public void sprint(){

        if(isExhausted()) { setSpeed(Speed.NORMAL); return; }
            
        this.setSpeed(Speed.SPEEDY);
                    
        if(System.currentTimeMillis() - lastSprint < sprintDelay * 1000) return;

        stamina -= 5; 
        lastSprint = System.currentTimeMillis();
    
    }

    // Throws player inv
    @Override
    public void throwLoot(MobEntity mob, Terrain terrain) {

        for(byte i = 0; i < getInventory().getMaxStorage(); i++) {

            if(getInventory().getSlot((i)).isEmpty()) continue;

            Item itemType = getInventory().getSlot(i).getItem();
            int itemCount = getInventory().getSlot(i).getCount();

            for(int j = 0; j < itemCount; j++) {
                terrain.addEntity(new ItemEntity(this.x() + RandomUtils.randomInt(-3, 3), this.y() + RandomUtils.randomInt(-3, 3), soundManager, itemType, mob));
            }

        }

    }

    // Tries to go one layer above
    public void tryStepUp(Terrain terrain) {

        if(dirX == 0 && dirY == 0) return;
        if(level() + 1 > Constants.MAX_WORLD_HEIGHT) return;

        TileStack nextStack = terrain.getWorldTileStack((int) (x() / Constants.GRID_SIZE) + dirX, (int) (y() / Constants.GRID_SIZE) + dirY());
        if(nextStack == null) return;

        Tile nextTile = nextStack.get((byte) (level() + 1));
        Tile nextBelowTile = nextStack.get((byte) (level()));
        if(nextBelowTile == null) return;

        if(!nextBelowTile.type().canSupport()) return;
        if(nextTile != null && !nextTile.type().isEmpty()) return;

        this.setLevel((byte) (level + 1));
        this.tryMove(terrain);

        this.soundManager.getSound(WALK_SOUND).play(MiscUtils.getFloatSound(20), RandomUtils.randomFloat(1.15f, 1.3f), 0f);

        lastMove = System.currentTimeMillis();

    }
    
    // Tries to go one layer down
    public void tryStepDown(Terrain terrain) {

        if(dirX == 0 && dirY == 0) return;
        if(level() - 1 < Constants.MIN_WORLD_HEIGHT) return;

        TileStack nextStack = terrain.getWorldTileStack((int) (x() / Constants.GRID_SIZE) + dirX, (int) (y() / Constants.GRID_SIZE) + dirY());
        if(nextStack == null) return;

        Tile nextTile = nextStack.get((byte) (level() - 1));
        Tile nextBelowTile = nextStack.get((byte) (level() - 2));
        if(nextBelowTile == null) return;

        if(!nextBelowTile.type().canSupport()) return;
        if(nextTile != null && !nextTile.type().isEmpty()) return;

        this.setLevel((byte) (level - 1));
        this.tryMove(terrain);

        this.soundManager.getSound(WALK_SOUND).play(MiscUtils.getFloatSound(20), RandomUtils.randomFloat(0.75f, 0.85f), 0f);

        lastMove = System.currentTimeMillis();

    }


    // Getters

    public boolean isInMenu() { return inMenu; }

    private long last_debug_print;
    public void stats(Camera camera){
        if(System.currentTimeMillis() - last_debug_print < 3.0f * 1000) return;
        Logger.LOGGER.debug("PLAYER", "Player Health: " + this.health + " | Player Stamina: " + this.stamina + " | Player Pos: (" + this.x + ", " + this.level + ", " + this.y + ") | Camera: (" + camera.x + ", " + camera.y + ")");
        last_debug_print = System.currentTimeMillis();
    }

    public Selector getSelector() {
        return this.selector;
    }

    public InventoryRenderer getInventoryRenderer() {
        return this.inventoryRenderer;
    }

    public Crafting getCraftingManager() {
        return this.craftingManager;
    }


    @Override
    protected void onMove() {
        soundManager.getSound(WALK_SOUND).play(MiscUtils.getFloatSound(15), RandomUtils.randomFloat(0.9f, 1.1f), 0f);
    }

    
    @Override
    public String toString() {
        return "Player(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", stamina: " + this.stamina + ", moving: " + Boolean.toString(this.moving) + " }";
    }


}
