package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.sound.SoundID;
import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.graphics.RendererStack;

// Button
public class Button {

    public static enum ButtonState {

        PRESS,
        HOVER,
        NOACTION

    }

    // Position
    protected int x, y;

    // Size
    protected int w, h;
    protected float scale;

    // Textures
    protected Texture buttonTexture;

    // Pressed
    protected ButtonState state = ButtonState.NOACTION;

    protected boolean wasHovering = false;
    protected boolean wasPressed = false;

    // Button text
    protected String text;

    // Sound Manager
    protected final SoundManager uiSoundManager;
            
    protected GlyphLayout layout = new GlyphLayout();

    protected final SoundID HOLLOW_SOUND = new SoundID("tinyblox", SoundType.UI, "hollow");
    protected final SoundID CLICK_SOUND = new SoundID("tinyblox", SoundType.UI, "click");

    public Button(SoundManager uiSoundManager) {
        this.uiSoundManager = uiSoundManager;
    }

    public void init(int x, int y, int w, int h, String text, float scale) {

        this.x = x;
        this.y = y;
        
        this.w = w;
        this.h = h;
        this.scale = scale;
        
        this.text = text;
        
    }


    public void initTexture(Texture buttonTexture) {
        this.buttonTexture = buttonTexture;
    }

    public void render(RendererStack rendererStack) {
    
		SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;
    
        font.getData().setScale(scale);

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

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { state = ButtonState.PRESS; if(!wasPressed) { uiSoundManager.getSound(CLICK_SOUND).play(MiscUtils.getFloatSound(100)); wasPressed = true; } }
            else { state = ButtonState.HOVER; if(!wasHovering) { uiSoundManager.getSound(HOLLOW_SOUND).play(MiscUtils.getFloatSound(80)); wasHovering = true; } wasPressed = false; }

        } else { state = ButtonState.NOACTION; wasHovering = false; wasPressed = false; }
    
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
