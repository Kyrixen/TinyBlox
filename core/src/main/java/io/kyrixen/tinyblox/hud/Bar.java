package io.kyrixen.tinyblox.hud;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.utils.TinyIdentifier;

public class Bar {

    // Position
    private int x, y;
    private float anchorX, anchorY;

    // Dimension
    private int w, h;
    private int baseW, baseH;

    // Values
    private float barVal;
    private int barMaxVal;

    // Texture
    private TinyIdentifier textureID;


    public Bar(int x, int y, int w, int h, float barValue, int barMaxValue) {
    
        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;

        this.anchorX = x / (float) Constants.WINDOW_WIDTH;
        this.anchorY = y / (float) Constants.WINDOW_HEIGHT;
        
        this.baseW = w;
        this.baseH = h;

        this.barVal = barValue;
        this.barMaxVal = barMaxValue;

    }

    public void initTexture(TinyIdentifier texIdentifier) {
        this.textureID = texIdentifier;
    }


    // Getters //

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public int getWidth() { return this.w; }
    public int getHeight() { return this.h; }

    public float getBarValue() { return this.barVal; }
    public int getBarMaxVal() { return this.barMaxVal; }

    public TinyIdentifier getTextureID() { return  this.textureID; }


    // Setters //

    public void setValue(float value) {

        this.barVal = value;
        if(barVal > barMaxVal) barVal = barMaxVal;

    }

    public void setMaxValue(int maxValue) {
        this.barMaxVal = maxValue;
    }


    // Reconfigure bar size on resize
    public void resize(int width, int height) {
    
        float uiScale = Math.min(width / 800f, height / 600f) * 0.75f;

        w = Math.round(baseW * uiScale);
        h = Math.round(baseH * uiScale);

        x = Math.round(width * anchorX) - w - 10;
        y = Math.round(height * anchorY);
    
    }

}
