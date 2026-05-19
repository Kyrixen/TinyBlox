package io.kyrixen.tinyblox.entities.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;

public class InventoryRenderer {
    
    // Inventory to render
    private Inventory inventoryRender;

    // Font specific vars
    protected BitmapFont font;
    protected GlyphLayout layout = new GlyphLayout();

    // UI Spacer vars
    private static final float SLOT_SIZE = Constants.GRID_SIZE * 5;
    private static final float SLOT_SPACING = 90f;

    // Inventory visibility
    private boolean visible = true;

    // Slot texture
    private static final TextureID hotbarSlot = new TextureID("tinyblox", TextureType.HUD, "hotbar_slot");

    public InventoryRenderer(Inventory inventory) {
        this.inventoryRender = inventory;
        generateFont("fonts/editundo.ttf", 32);
    }

    // Full render method
    public void render(TextureManager tex, SpriteBatch batch) {

        if(!visible) return;

        batch.begin();
        renderSlots(tex, batch);
        renderCounts(batch);
        renderItems(batch);
        batch.end();

    }

    // Renders item slots
    public void renderSlots(TextureManager tex, SpriteBatch batch) {
        
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            batch.draw(tex.getTexture(hotbarSlot), this.getSlotX(), this.getSlotY() - (i * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE);
        }
        
    }

    // Renders items
    public void renderItems(SpriteBatch batch) {
    
        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            if(inventoryRender.getSlot(i).isEmpty()) continue;
            font.draw(batch, inventoryRender.getSlot(i).getItem().name(), this.getSlotX() + 6, this.getSlotY() - (i * SLOT_SPACING) + 52);
        }
        
    }

    // Renders item counts
    public void renderCounts(SpriteBatch batch) {

        for(byte i = 0; i < inventoryRender.getMaxStorage(); i++) {
            if(inventoryRender.getSlot(i).isEmpty()) continue;
            font.draw(batch, String.valueOf(inventoryRender.getSlot(i).getCount()), this.getSlotX() + 6, this.getSlotY() - (i * SLOT_SPACING) + 18);
        }
        
    }

    // Draws current hotbar
    public void drawHighlight(ShapeRenderer shapeRenderer) {
        
        if(!visible) return;

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(this.getSlotX(), this.getSlotY() - (inventoryRender.getCurrentSlot() * SLOT_SPACING), SLOT_SIZE, SLOT_SIZE);
        shapeRenderer.end();

    }

    // Generates temporary font
    private void generateFont(String path, int size) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;

        font = generator.generateFont(parameter);

        generator.dispose();

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
