package io.kyrixen.tinyblox.menu.selection.uiselectionaddon;

import com.badlogic.gdx.graphics.Texture;

import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.Popup;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class PopupSelect extends Popup {

    // Popup select vars
    protected final Button cancelButton;
    protected boolean result = false;


    // Texture constants
    private static final TinyIdentifier BUTTON_OK_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"green_button");
    private static final TinyIdentifier BUTTON_CANCEL_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"red_button");


    public PopupSelect(SoundManager uiSoundManager, String popupInfo) {
        super(uiSoundManager, popupInfo);
        this.cancelButton = new Button(uiSoundManager);
    }


    @Override
    public void init(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;

        this.baseX = x;
        this.baseY = y;

        this.baseW = w;
        this.baseH = h;


        float buttonScale = (w / 200f) * 1.5f;

        this.okButton.init(x + (int) (w - (48 * buttonScale) * 2 + (4 * buttonScale)) / 2 + (int) (48 * buttonScale) + (int) (4 * buttonScale), y + (int) (h * 0.1f), (int) (48 * buttonScale), (int) (16 * buttonScale), "YES", 1.0f);
        this.cancelButton.init(x + (int) (w - (48 * buttonScale) * 2 + (4 * buttonScale)) / 2, y + (int) (h * 0.1f), (int) (48 * buttonScale), (int) (16 * buttonScale), "NO", 1.0f);

    }

    public void initTexture(Texture popupSelectTexture, TextureManager tex) {
        this.popupTex = popupSelectTexture;
        this.okButton.initTexture(tex.getTexture(BUTTON_OK_TEXTURE));
        this.cancelButton.initTexture(tex.getTexture(BUTTON_CANCEL_TEXTURE));
    }


    @Override 
    public void render(RendererStack rendererStack) {

        if(!show) return;
        
        super.render(rendererStack);
        cancelButton.render(rendererStack);
    
    }

    @Override 
    public void updateState(float deltaTime) {

        if(!show) return;
        
        super.updateState(deltaTime);
        cancelButton.updateState();

        if(okButton.pressed()) result = true;

        if(cancelButton.pressed()) show = false;
        if(cancelButton.pressed()) result = false;
    
    }


    @Override 
    public void resize(int width, int height) {
        super.resize(width, height);
        cancelButton.resize(width, height);
    }


    public boolean getResult() { return this.result; }

}
