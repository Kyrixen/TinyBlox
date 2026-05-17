package io.kyrixen.tinyblox.entities.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.Textures;

public class InventoryRenderer {
    
    private Inventory inventoryRender;

    private static final float SLOT_SIZE = Constants.GRID_SIZE * 5;

    private static final float SLOT_SPACING = 90f;

    public InventoryRenderer(Inventory inventory) {
        this.inventoryRender = inventory;
    }

    public void render(SpriteBatch batch) {
        renderSlots(batch);
        renderCounts(batch);
        renderItems(batch);
    }

    public void renderSlots(SpriteBatch batch) {

        float slotX = Gdx.graphics.getWidth() - SLOT_SPACING;
        float slotY = Gdx.graphics.getHeight() - SLOT_SPACING;
        
        batch.begin();
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) { batch.draw(Textures.hotbarSlot, slotX, slotY - (i * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE); }
        batch.end();

    }

    public void renderItems(SpriteBatch batch) {



    }

    public void renderCounts(SpriteBatch batch) {


        
    }

    public void drawHighlight(ShapeRenderer shapeRenderer) {

        float slotX = Gdx.graphics.getWidth() - SLOT_SPACING;
        float slotY = Gdx.graphics.getHeight() - SLOT_SPACING;
        
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(slotX, slotY - (inventoryRender.getCurrentSlot() * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE);
        shapeRenderer.end();

    }

}
