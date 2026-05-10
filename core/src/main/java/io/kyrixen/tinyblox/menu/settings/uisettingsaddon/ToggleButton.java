package io.kyrixen.tinyblox.menu.settings.uisettingsaddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.sound.UISounds;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;

// Toggle button
public class ToggleButton extends Button {

    public static enum ToggleButtonState {

        ON,
        OFF,
        DISABLED

    }

    private ToggleButtonState toggleState = ToggleButtonState.DISABLED;

    public ToggleButton(UISounds uiSoundManager) {
        super(uiSoundManager);
        this.toggleState = ToggleButtonState.OFF;
    }
    
    @Override
    public void render(SpriteBatch batch) {

        float r = 1f;
        float g = 1f;
        float b = 1f;

        // Toggle appearance
        switch (toggleState) {

            case ON:
                r *= 0.7f;
                g *= 0.7f;
                b *= 0.7f;
                break;

            case OFF:
                break;

            case DISABLED:
                r *= 0.4f;
                g *= 0.4f;
                b *= 0.4f;
                break;
        
        }

        // Hover / press appearance
        switch (state) {

            case NOACTION:
                break;

            case HOVER:
                r *= 0.85f;
                g *= 0.85f;
                b *= 0.85f;
                break;

            case PRESS:
                r *= 0.6f;
                g *= 0.6f;
                b *= 0.6f;
                break;
        
        }

        batch.setColor(r, g, b, 1f);
        batch.draw(buttonTexture, x, y, w, h);

        batch.setColor(1f, 1f, 1f, 1f);

        layout.setText(font, text);

        float textX = x + (w - layout.width) / 2f;
        float textY = y + (h + layout.height) / 2f;

        font.draw(batch, text, textX, textY);
    
    }

    @Override
    public void updateState() {

        int mX = Gdx.input.getX();
        int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean hover = mX >= x && mX <= x + w && mY >= y && mY <= y + h;

        if(hover) {

            if(Peripheal.mousePressed(Input.Buttons.LEFT) && Gdx.input.justTouched()) {
            
                state = ButtonState.PRESS;
            
                if(!wasPressed) {
            
                    uiSoundManager.click.play(Utils.getFloatSound(100));
                    wasPressed = true;
            
                    if(toggleState == ToggleButtonState.DISABLED) return;

                    if(toggleState == ToggleButtonState.OFF) toggleState = ToggleButtonState.ON; 
                    else toggleState = ToggleButtonState.OFF;
            
                }
            
            } else { 
            
                state = ButtonState.HOVER; 
            
                if(!wasHovering) {
                    uiSoundManager.hollow.play(Utils.getFloatSound(80));
                    wasHovering = true;
                }
            
                wasPressed = false;
            
            }

        } else { 
        
            state = ButtonState.NOACTION;
        
            wasHovering = false;
            wasPressed = false;
        
        }

    }

    // Getters
    public ToggleButtonState getToggleState() { return this.toggleState; }

    public boolean enabled() {
        return toggleState == ToggleButtonState.ON;
    }

    public void setToggleState(ToggleButtonState toggleButtonState) {
        this.toggleState = toggleButtonState;
    }

}
