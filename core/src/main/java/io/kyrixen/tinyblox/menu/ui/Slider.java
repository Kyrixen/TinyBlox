package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.sound.UISounds;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;

// Slider
public class Slider {

    // Position
    protected int x, y;
    
    // Dimensions
    protected int w, h;

    // Value
    protected float minValue = 0f;
    protected float maxValue = 100f;

    // Slider specific
    protected float percent = 0;

    protected UISounds uiSoundManager;

    protected Texture outlineTexture;
    
    protected Color barColor;
    protected Color hoverBarColor;
    protected Color percentageColor;

    protected boolean dragging = false;
    protected boolean hover = false;
    protected boolean pressed = false;

    protected boolean wasDragged = false;
    protected boolean wasHovered = false;

    private ShapeRenderer shapeRenderer;

    protected String name;

    protected BitmapFont font;
    protected GlyphLayout layout = new GlyphLayout();

    public Slider(UISounds uiSoundManager) {
        this.uiSoundManager = uiSoundManager;
    }

    public void init(int x, int y, int w, int h, float percent, float maxValue, String name) {

        this.x = x;
        this.y = y;
        
        this.w = w;
        this.h = h;

        this.percent = percent;
        this.maxValue = maxValue;

        this.name = name;

        this.shapeRenderer = new ShapeRenderer();

        generateFont("fonts/editundo.ttf", 48);

    }

    public void initTexture(Texture outlineTexture, Color barColor, Color hoverBarColor, Color percentageColor) {
    
        this.outlineTexture = outlineTexture;
        this.barColor = barColor;
        this.hoverBarColor = hoverBarColor;
        this.percentageColor = percentageColor;
    
    }

    protected void generateFont(String path, int size) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;

        font = generator.generateFont(parameter);

        generator.dispose();

    }

    public void updateState() {

        int mX = Gdx.input.getX();
        int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

        hover = mX >= x && mX <= x + w && mY >= y && mY <= y + h;

        if(hover && Peripheal.mousePressed(Input.Buttons.LEFT)) { dragging = true; pressed = true; if(!wasDragged) { uiSoundManager.slider.play(Utils.getFloatSound(50)); wasDragged = true; } }
        if(hover) { if(!wasHovered) { uiSoundManager.options.play(Utils.getFloatSound(70)); wasHovered = true; } }
        if(!hover) wasHovered = false;

        if(!Peripheal.mousePressed(Input.Buttons.LEFT)) { dragging = false; wasDragged = false; pressed = false; }

        if(dragging) {
            percent = (float)(mX - x) / w;
            percent = Math.max(0f, Math.min(1f, percent));
        }

    }

    public void render(SpriteBatch batch) {

        // Draw outline/background
        batch.begin();
        batch.draw(outlineTexture, x, y, w, h);
        batch.end();

        // Filled portion
        int startX = x + 6;
        int startY = y + 6;
        int barWidth = (int)((w - 12) * percent);
        int barHeight = h - 12;

        shapeRenderer.begin(ShapeType.Filled);

        if(hover) shapeRenderer.setColor(hoverBarColor);
        else shapeRenderer.setColor(barColor);
        
        shapeRenderer.rect(startX, startY, barWidth, barHeight);
        
        shapeRenderer.end();

        batch.begin();
    
        layout.setText(font, name + " " + Integer.toString(this.getValue()));
        font.setColor(percentageColor);

        float textX = x + (w - layout.width) / 2f;
        float textY = y + (h + layout.height) / 2f;

        font.draw(batch, name + " " + Integer.toString(this.getValue()), textX, textY);
    
        batch.end();
    
    }

    // Getters
    public int getValue() { return (int)(minValue + percent * (maxValue - minValue)); }
    
    public boolean pressed() { return this.pressed; }

}