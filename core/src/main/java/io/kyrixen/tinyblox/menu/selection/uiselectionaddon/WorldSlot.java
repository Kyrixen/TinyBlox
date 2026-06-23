package io.kyrixen.tinyblox.menu.selection.uiselectionaddon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    // Dimensions
    private int w, h;

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
        String worldFrequency = "FREQ: " + FrequencyType.valueOf(world.worldFrequency);
        String worldVersion = "VER: " + world.formatVersion;

        font.getData().setScale(0.75f);
        font.draw(batch, worldName, x + 8, y + h - 6);
        font.draw(batch, worldSeed, x + 8, y + h - 30);
        font.draw(batch, worldFrequency, x + 8, y + h - 54);

        if(!compatible) font.setColor(1f, 0f, 0f, 1f);
        font.draw(batch, worldVersion, x + 8, y + h - 78);
        font.setColor(1f, 1f, 1f, 1f);

        font.getData().setScale(1f);

    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
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