package io.kyrixen.tinyblox.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

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
    private static final TinyIdentifier hotbarSlot = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "hotbar_slot");

    // UI size var
    private float uiScale = 1f;

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
            batch.draw(tex.getTexture(hotbarSlot), this.getSlotX(), this.getSlotY() - (i * SLOT_SPACING * uiScale), SLOT_SIZE * uiScale, SLOT_SIZE * uiScale);
        }
        
    }

    // Renders items
    private void renderItems(TextureManager tex, SpriteBatch batch) {
    
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
        
            if(inventoryRender.getSlot(i).isEmpty()) continue;

            float itemX = this.getSlotX() + (SLOT_SIZE - ITEM_SIZE) * uiScale / 2f;
            float itemY = (this.getSlotY() - (i * SLOT_SPACING * uiScale)) + (SLOT_SIZE * uiScale - ITEM_SIZE * uiScale) / 2f;
            
            Texture itemTexture = tex.getTexture(inventoryRender.getSlot(i).getItem().textureID());

            batch.draw(itemTexture, itemX, itemY, ITEM_SIZE * uiScale, ITEM_SIZE * uiScale);
        
        }
        
    }

    // Renders item counts
    private void renderCounts(RendererStack rendererStack) {

        rendererStack.font.getData().setScale(0.75f * uiScale);

        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            if(inventoryRender.getSlot(i).isEmpty()) continue;
            rendererStack.font.draw(rendererStack.batch, String.valueOf(inventoryRender.getSlot(i).getCount()), this.getSlotX() + 6 * uiScale, this.getSlotY() - (i * SLOT_SPACING * uiScale) + 18 * uiScale);
        }
        
        rendererStack.font.getData().setScale(1f);

    }

    // Draws current hotbar
    public void drawHighlight(RendererStack rendererStack) {
        
        if(!visible) return;

        ShapeRenderer shapeRenderer = rendererStack.shape;

        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(this.getSlotX(), this.getSlotY() - (inventoryRender.getCurrentSlot() * SLOT_SPACING  * uiScale), SLOT_SIZE * uiScale, SLOT_SIZE * uiScale);

    }


    // Reconfigure slider size on resize
    public void resize(int width, int height) {
        uiScale = Math.min(width / 800f, height / 600f);
    }


    // Helper getters //

    private float getSlotX() {
        return Gdx.graphics.getWidth() - SLOT_SPACING * uiScale;
    }

    private float getSlotY() {
        return Gdx.graphics.getHeight() - SLOT_SPACING * uiScale;
    }

    // Setters //

    public void toggleRendering() {
        this.visible = !visible;
    }

}
