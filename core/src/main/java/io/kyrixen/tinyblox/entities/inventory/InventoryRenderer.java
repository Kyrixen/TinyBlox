package io.kyrixen.tinyblox.entities.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;

public class InventoryRenderer {
    
    // Inventory to render
    private final Inventory inventoryRender;

    // UI Spacer vars
    private static final float SLOT_SIZE = Constants.GRID_SIZE * 5;
    private static final float SLOT_SPACING = 90f;
    private static final float ITEM_SIZE = SLOT_SIZE - 32;

    // Inventory visibility
    private boolean visible = true;

    // Slot texture
    private static final TextureID hotbarSlot = new TextureID("tinyblox", TextureType.HUD, "hotbar_slot");

    public InventoryRenderer(Inventory inventory) {
        this.inventoryRender = inventory;
    }

    // Full render method
    public void render(TextureManager tex, RendererStack rendererStack) {

        if(!visible) return;

        SpriteBatch batch = rendererStack.batch;

        renderSlots(tex, batch);
        renderCounts(rendererStack);
        renderItems(tex, batch);

    }

    // Renders item slots
    private void renderSlots(TextureManager tex, SpriteBatch batch) {
        
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            batch.draw(tex.getTexture(hotbarSlot), this.getSlotX(), this.getSlotY() - (i * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE);
        }
        
    }

    // Renders items
    private void renderItems(TextureManager tex, SpriteBatch batch) {
    
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
        
            if(inventoryRender.getSlot(i).isEmpty()) continue;

            float itemX = this.getSlotX() + (SLOT_SIZE - ITEM_SIZE) / 2f;
            float itemY = (this.getSlotY() - (i * SLOT_SPACING)) + (SLOT_SIZE - ITEM_SIZE) / 2f;
            
            Texture itemTexture = tex.getTexture(inventoryRender.getSlot(i).getItem().textureID());

            batch.draw(itemTexture, itemX, itemY, ITEM_SIZE, ITEM_SIZE);
        
        }
        
    }

    // Renders item counts
    private void renderCounts(RendererStack rendererStack) {

        rendererStack.font.getData().setScale(0.75f);

        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            if(inventoryRender.getSlot(i).isEmpty()) continue;
            rendererStack.font.draw(rendererStack.batch, String.valueOf(inventoryRender.getSlot(i).getCount()), this.getSlotX() + 6, this.getSlotY() - (i * SLOT_SPACING) + 18);
        }
        
    }

    // Draws current hotbar
    public void drawHighlight(RendererStack rendererStack) {
        
        if(!visible) return;

        ShapeRenderer shapeRenderer = rendererStack.shape;

        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(this.getSlotX(), this.getSlotY() - (inventoryRender.getCurrentSlot() * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE);

    }

    // Helper getters //

    private float getSlotX() {
        return Gdx.graphics.getWidth() - SLOT_SPACING;
    }

    private float getSlotY() {
        return Gdx.graphics.getHeight() - SLOT_SPACING;
    }

    // Setters //

    public void toggleRendering() {
        this.visible = !visible;
    }

}
