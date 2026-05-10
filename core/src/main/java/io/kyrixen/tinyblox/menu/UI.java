package io.kyrixen.tinyblox.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.sound.UISounds;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;


public class UI {

    // Button
    public static class Button {

        public static enum ButtonState {

            PRESS,
            HOVER,
            NOACTION

        }

        // Position
        protected int x, y;

        // Size
        protected int w, h;

        // Textures
        protected Texture buttonTexture;

        // Pressed
        protected ButtonState state = ButtonState.NOACTION;

        protected boolean wasHovering = false;
        protected boolean wasPressed = false;

        // Button text
        protected String text;

        // Sound Manager
        protected UISounds uiSoundManager;
                
        protected BitmapFont font;
        protected GlyphLayout layout = new GlyphLayout();

        public Button(UISounds uiSoundManager) {
            this.uiSoundManager = uiSoundManager;
        }

        public void init(int x, int y, int w, int h, String text, int fontSize) {

            this.x = x;
            this.y = y;

            this.w = w;
            this.h = h;

            this.text = text;

            generateFont("fonts/editundo.ttf", fontSize);

        }

        protected void generateFont(String path, int size) {

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));

            FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = size;

            font = generator.generateFont(parameter);

            generator.dispose();

        }

        public void initTexture(Texture buttonTexture) {
            this.buttonTexture = buttonTexture;
        }

        public void render(SpriteBatch batch) {
        
            switch (state) {

                case NOACTION:
                    batch.setColor(1f, 1f, 1f, 1f);
                    batch.draw(buttonTexture, x, y, w, h);
                    break;

                case HOVER:
                    batch.setColor(0.7f, 0.7f, 0.7f, 1f);
                    batch.draw(buttonTexture, x, y, w, h);
                    break;

                case PRESS:
                    batch.setColor(0.5f, 0.5f, 0.5f, 1f);
                    batch.draw(buttonTexture, x, y, w, h);
                    break;
            
            }

            batch.setColor(1f, 1f, 1f, 1f);


            layout.setText(font, text);

            float textX = x + (w - layout.width) / 2f;
            float textY = y + (h + layout.height) / 2f;

            font.draw(batch, text, textX, textY);
        
        }

        public void updateState() {

            int mX = Gdx.input.getX();
            int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

            boolean hover = mX >= x && mX <= x + w && mY >= y && mY <= y + h;

            if(hover) {

                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { state = ButtonState.PRESS; if(!wasPressed) { uiSoundManager.click.play(Utils.getFloatSound(100)); wasPressed = true; } }
                else { state = ButtonState.HOVER; if(!wasHovering) { uiSoundManager.hollow.play(Utils.getFloatSound(80)); wasHovering = true; } wasPressed = false; }

            } else { state = ButtonState.NOACTION; wasHovering = false; wasPressed = false; }
        
        }
        
        public void dispose() {
            if(font != null) font.dispose();
        }

        // Get methods
        public ButtonState getState() { return this.state; }
        
        public boolean pressed() {
            return this.getState() == ButtonState.PRESS && Gdx.input.justTouched();
        }

        public boolean hovered() {
            return this.getState() == ButtonState.HOVER;
        }

        public boolean idle() {
            return this.getState() == ButtonState.NOACTION;
        }
        
    }

    // Cursor
    public static class Cursor {



    }

    // Box (Dialogue window)
    public static class Box {

        // Position
        private int x, y;

        // Size
        private int w, h;

        // Textures
        private Texture corner;
        private Texture side;
        private Texture center;

        private int tile; // Size of one tile

        // Init instance
        public void init(int x, int y, int w, int h) {

            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        
        }

        // Init textures
        public void initTexture(Texture corner, Texture side, Texture center) {
        
            this.corner = corner;
            this.side = side;
            this.center = center;
            this.tile = corner.getWidth();
        
        }

        // Render dialog box
        public void render(SpriteBatch batch) {

            if (corner == null) return;

            // Corners //
            drawCorner(batch, x, y, 0);                         // Top-left
            drawCorner(batch, x + w - tile, y, 90);             // Top-right
            drawCorner(batch, x + w - tile, y + h - tile, 180); // Bottom-right
            drawCorner(batch, x, y + h - tile, 270);            // Bottom-left

            // Top and Bottom //
            for (int i = tile; i < w - tile; i += tile) {
                
                batch.draw(side, x + i, y, tile, tile);
                drawRotated(batch, side, x + i, y + h - tile, 180); // Bottom
            
            }

            // Left and Right //
            for (int i = tile; i < h - tile; i += tile) {
                
                drawRotated(batch, side, x, y + i, 270);           // Left
                drawRotated(batch, side, x + w - tile, y + i, 90); // Right
            
            }

            // Center //
            for (int ix = tile; ix < w - tile; ix += tile) {
                for (int iy = tile; iy < h - tile; iy += tile) {
                    
                    batch.draw(center, x + ix, y + iy, tile, tile);
            
                }

            }

        }

        // Draw rotated corner
        private void drawCorner(SpriteBatch batch, int x, int y, int angle) {
            drawRotated(batch, corner, x, y, angle);
        }

        // Rotate around top-left
        private void drawRotated(SpriteBatch batch, Texture tex, float x, float y, float angleDeg) {
            
            float tileW = tile;
            float tileH = tile;

            batch.draw(tex, x, y, tileW / 2f, tileH / 2f, tileW, tileH, 1f, 1f, angleDeg, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
        
        }
   
    }

    // Switch (Toggle)
    public static class Switch {


    
    }

    // Slider
    public static class Slider {

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

}
