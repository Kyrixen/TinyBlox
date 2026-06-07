// Kyrixen: Sorry i have no energy for explaining.

package io.kyrixen.tinyblox.entities.mob;

import java.util.ArrayList;
import java.util.Iterator;

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
import io.kyrixen.tinyblox.utils.Utils;
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


    public Player(int id, int x, int y, Camera camera, SoundManager soundManager) {
    
        super(id, x, y, soundManager);

        this.hotbarSlotCount = 6;
        this.inventory = new Inventory(this.hotbarSlotCount);
        this.inventoryRenderer = new InventoryRenderer(inventory);

        this.craftingManager = new Crafting(this.inventory);

        this.sprintDelay = 0.15f;
        this.damageDelay = 0.50f;

        this.setSpeed(Speed.NORMAL);

        this.health = 100;
        this.maxHealth = 100;

        this.stamina = 100;
        this.maxStamina = 100;
        
        this.invincible = false;
        this.tireless = false;

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

        autoRecover(true, deltaTime);
        autoRegenerate(true, deltaTime);

        exhausted = stamina <= 0 && !tireless;

        if(System.currentTimeMillis() - lastMove < speed.getMoveDelay() * 1000) return;
        if(dirX == 0 && dirY == 0) { moving = false; return; }

        updateFlip();
        moving = tryMove(terrain);

        if(moving) soundManager.getSound(WALK_SOUND).play(Utils.getFloatSound(15), RandomUtils.randomFloat(0.9f, 1.1f), 0f);

        lastMove = System.currentTimeMillis();

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
    public void checkDropPickup(ArrayList<Entity> entities) {

        if(inventory.isFull()) return;

        Iterator<Entity> iterator = entities.iterator();

        while (iterator.hasNext()) {

            Entity e = iterator.next();

            if (e == this) continue;
            if (!(e instanceof ItemEntity)) continue;
            if (!EntityCollision.checkTileCollision(this, e)) continue;

            ItemEntity itemEntity = (ItemEntity) e;
            Item itemDrop = itemEntity.getItem();

            if(!this.inventory.add(itemDrop, (byte) 1)) continue;

            itemEntity.pickup();

            iterator.remove();
    
            Logger.LOGGER.debug("PLAYER", "Player inventory: " + this.inventory.toString());

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

    // Climb up method
    public void tryClimbUp(Terrain terrain) {

        TileStack tileStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(tileStack == null) return;

        Tile current = tileStack.get(this.level);
        if(current == null) return;

        if(this.level + 1 > Constants.MAX_WORLD_HEIGHT) return;
        if(!current.type().isClimbable()) return;

        Tile above = tileStack.get((byte)(this.level + 1));
        if(above != null && !above.type().isPassable() && !above.type().isClimbable()) return;

        this.level++;

    }

    // Climb down method
    public void tryClimbDown(Terrain terrain) {

        TileStack tileStack = terrain.getWorldTileStack(x / Constants.GRID_SIZE, y / Constants.GRID_SIZE);
        if(tileStack == null) return;
        
        if(this.level - 1 < Constants.MIN_WORLD_HEIGHT) return;

        Tile current = tileStack.get(this.level);
        Tile below = tileStack.get((byte)(this.level - 1));

        boolean canClimb = (current != null && current.type().isClimbable()) || (below != null && below.type().isClimbable());
        if(!canClimb) return;

        if(below != null && !below.type().isPassable()) return;

        this.level--;

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
    public String toString() {
        return "Player(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", stamina: " + this.stamina + ", moving: " + Boolean.toString(this.moving) + " }";
    }


}
