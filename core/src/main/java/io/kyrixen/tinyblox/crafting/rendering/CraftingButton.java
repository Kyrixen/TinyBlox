package io.kyrixen.tinyblox.crafting.rendering;

import io.kyrixen.tinyblox.utils.TinyIdentifier;

public class CraftingButton {

    // Recipe button state enum
    public enum CraftingButtonState {

        IDLE,
        SELECTED,
        HOVER

    }

    private CraftingButtonState state;

    // Position
    private int x, y;

    // Dimension
    private final int w, h;

    // Container for the textures
    private final TinyIdentifier idleTexture;
    private final TinyIdentifier hoverTexture;
    private final TinyIdentifier selectedTexture;

    // Constructor of the recipe button
    public CraftingButton(int x, int y, int w, int h, TinyIdentifier hover, TinyIdentifier idle, TinyIdentifier selected) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.hoverTexture = hover;
        this.idleTexture = idle;
        this.selectedTexture = selected;
        
        this.state = CraftingButtonState.IDLE;

    }


    // Getters //

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public int getWidth() { return this.w; }
    public int getHeight() { return this.h; }

    public CraftingButtonState getState() { return this.state; }

    public TinyIdentifier getTexture() { 
        
        switch (state) {

            case HOVER:
                return this.hoverTexture;

            case IDLE:    
                return this.idleTexture;

            case SELECTED:
                return this.selectedTexture;

            default:
                return this.idleTexture;
        
        }
    
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    // Setters //

    public void setState(CraftingButtonState state) { this.state = state; }

}
