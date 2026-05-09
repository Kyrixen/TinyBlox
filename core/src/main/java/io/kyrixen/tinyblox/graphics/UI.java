package io.kyrixen.tinyblox.graphics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import io.kyrixen.tinyblox.sound.UISounds;


public class UI {

    // Button
    public static class Button {

        public static enum ButtonState {

            PRESS,
            HOVER,
            NOACTION

        }

        // Position
        private int x, y;

        // Size
        private int w, h;

        // Textures
        private Texture buttonTexture;

        // Pressed
        private ButtonState state = ButtonState.NOACTION;

        private boolean wasHovering = false;
        private boolean wasPressed = false;

        // Button text
        private String text;

        // Sound Manager
        private UISounds uiSoundManager;
                
        private BitmapFont font;
        private GlyphLayout layout = new GlyphLayout();

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

        private void generateFont(String path, int size) {

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

                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { state = ButtonState.PRESS; if(!wasPressed) { uiSoundManager.click.play(1.0f); wasPressed = true; } }
                else { state = ButtonState.HOVER; if(!wasHovering) { uiSoundManager.hollow.play(0.8f); wasHovering = true; } wasPressed = false; }

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



    }

}
