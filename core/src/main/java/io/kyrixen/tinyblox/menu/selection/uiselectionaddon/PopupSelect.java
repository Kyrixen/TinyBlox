package io.kyrixen.tinyblox.menu.selection.uiselectionaddon;

import com.badlogic.gdx.graphics.Texture;

import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.Popup;
import io.kyrixen.tinyblox.sound.SoundManager;

public class PopupSelect extends Popup {

    // Popup select vars
    protected final Button cancelButton;
    protected boolean result = false;


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

    public void initTexture(Texture popupSelectTexture, Texture okButtonTexture, Texture cancelButtonTexture) {
        this.popupTex = popupSelectTexture;
        this.okButton.initTexture(okButtonTexture);
        this.cancelButton.initTexture(cancelButtonTexture);
    }


    @Override 
    public void render(RendererStack rendererStack) {

        if(!show) return;
        
        super.render(rendererStack);
        cancelButton.render(rendererStack);
    
    }

    @Override 
    public void updateState() {

        if(!show) return;
        
        super.updateState();
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
