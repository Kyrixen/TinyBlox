package io.kyrixen.tinyblox.menu.selection.uiselectionaddon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.world.FrequencyType;

// World slot widget
public class WorldSlot {

    // World slot state enum
    public enum WorldSlotState {

        IDLE,
        SELECTED,
        HOVER

    }


    // Position
    private int x, y;
    private int baseX, baseY;

    // Dimensions
    private int w, h;
    private int baseW, baseH;

    // Texture
    private Texture texture;

    // World info
    private WorldBlueprint world;

    // State
    private WorldSlotState state = WorldSlotState.IDLE;

    // Combatibility
    private boolean compatible = true;


    // Constructor
    public WorldSlot(int x, int y, int w, int h, Texture texture) {

        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;

        this.baseX = x;
        this.baseY = y;

        this.baseW = w;
        this.baseH = h;
        
        this.texture = texture;
        
    }


    public void render(RendererStack rendererStack) {

        if(world == null) return;

        SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;

        if(state == WorldSlotState.SELECTED) batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        else if(state == WorldSlotState.HOVER) batch.setColor(0.7f, 0.7f, 0.7f, 1f);
        else batch.setColor(1f, 1f, 1f, 1f);

        batch.draw(texture, x, y, w, h);
        batch.setColor(1f, 1f, 1f, 1f);

        String worldName = "MAP: " + world.worldName.toUpperCase();
        String worldSeed = "SEED: " + world.worldSeed;
        String worldFrequency = "FREQ: " + FrequencyType.valueOf(world.worldFrequency).name().replace("_", " ");
        String worldVersion = "VER: " + world.formatVersion;

        float uiScale = Math.min(Constants.WINDOW_WIDTH / 800f, Constants.WINDOW_HEIGHT / 600f);
        font.getData().setScale(0.75f * uiScale);
        
        font.draw(batch, worldName, x + 8 * uiScale, y + h - 6 * uiScale);
        font.draw(batch, worldSeed, x + 8 * uiScale, y + h - 30 * uiScale);
        font.draw(batch, worldFrequency, x + 8 * uiScale, y + h - 54 * uiScale);

        if(!compatible) font.setColor(1f, 0f, 0f, 1f);
        font.draw(batch, worldVersion, x + 8 * uiScale, y + h - 78 * uiScale);
        font.setColor(1f, 1f, 1f, 1f);

        font.getData().setScale(1f);

    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    
    // Reconfigure world slot size on resize
    public void resize(int width, int height) {

        float uiScale = Math.min(width / 800f, height / 600f);

        float offsetX = (width - 800f * uiScale) / 2f;
        float offsetY = (height - 600f * uiScale) / 2f;

        x = Math.round(offsetX + baseX * uiScale);
        y = Math.round(offsetY + baseY * uiScale);

        w = Math.round(baseW * uiScale);
        h = Math.round(baseH * uiScale);
    
    }


    // Helpers //

    public void setState(WorldSlotState state) {
        this.state = state;
    }

    public void setWorld(WorldBlueprint world) {
        this.world = world;
    }

    public void setCompatibility(boolean compatible) {
        this.compatible = compatible;
    }

    public WorldSlotState getState() { return this.state; }

    public WorldBlueprint getWorld() { return this.world; }

    public boolean isCompatible() { return this.compatible; }

}