package io.kyrixen.tinyblox.crafting.rendering;

import io.kyrixen.tinyblox.utils.TinyIdentifier;

public class RecipeContainer {

    // Position
    private int x, y;
    private int baseX, baseY;

    // Dimension
    private int w, h;
    private int baseW, baseH;

    // Container for the textures
    private final TinyIdentifier textureID;


    // Constructor of the crafting menu
    public RecipeContainer(int x, int y, int w, int h, TinyIdentifier textureID) {

        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;

        this.baseX = x;
        this.baseY = y;
        
        this.baseW = w;
        this.baseH = h;

        this.textureID = textureID;
        
    }


    // Reconfigure container size on resize
    public void resize(int width, int height) {

        float uiScale = Math.min(width / 800f, height / 600f) * 0.75f;

        x = Math.round(baseX * uiScale);
        y = Math.round(baseY * uiScale);

        w = Math.round(baseW * uiScale);
        h = Math.round(baseH * uiScale);
        
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
