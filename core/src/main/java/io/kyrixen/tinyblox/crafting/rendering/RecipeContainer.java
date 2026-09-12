package io.kyrixen.tinyblox.crafting.rendering;

import io.kyrixen.tinyblox.utils.TinyIdentifier;

public class RecipeContainer {

    // Position
    private final int x, y;

    // Dimension
    private final int w, h;

    // Container for the textures
    private final TinyIdentifier textureID;


    // Constructor of the crafting menu
    public RecipeContainer(int x, int y, int w, int h, TinyIdentifier textureID) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.textureID = textureID;
        
    }


    // Getters //

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public int getWidth() { return this.w; }
    public int getHeight() { return this.h; }

    public TinyIdentifier getTextureID() { return this.textureID; }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

}
