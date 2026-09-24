package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class Popup {
  
    // Position
    private int x, y;
    private int baseX, baseY;

    // Size
    private int w, h;
    private int baseW, baseH;

    private float offsetX = 0f;
    private float offsetY = 0f;


    // Popup vars
    protected final String text;
    protected final Button button;
    protected boolean show = false;


    // Sound Manager
    protected final SoundManager uiSoundManager;

    // Texture
    private Texture popupTex;


    private static final TinyIdentifier POP_UP_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "pop_up");


    public Popup(SoundManager uiSoundManager, String popupInfo) {
        
        this.uiSoundManager = uiSoundManager;
        this.text = popupInfo;

        this.button = new Button(uiSoundManager);

    }

    public void init(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;

        this.baseX = x;
        this.baseY = y;

        this.baseW = w;
        this.baseH = h;


        float buttonScale = (w / 200f) * 2f;
        this.button.init((int) (x + w - 48 * buttonScale) / 2, y + (int) (h * 0.1f), (int) (48 * buttonScale), (int) (16 * buttonScale), "OK", 1.0f);

    }

    // Init texture
    public void initTexture(Texture popupTexture, Texture buttonTexture) {
        this.popupTex = popupTexture;
        this.button.initTexture(buttonTexture);
    }


    public void render(RendererStack rendererStack) {

        if(!show) return;

        rendererStack.batch.draw(popupTex, x, y, w, h);

        float uiScale = Math.min(Constants.WINDOW_WIDTH / 800f, Constants.WINDOW_HEIGHT / 600f);
        rendererStack.font.getData().setScale(1.0f * uiScale);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(rendererStack.font, text, Color.WHITE, w - (40f * uiScale) * 2f, Align.center, true);

        rendererStack.font.draw(rendererStack.batch, layout, x + (40f * uiScale), y + h - (40f * uiScale));

        button.render(rendererStack);
    
    }

    public void updateState(float delta) {
    
        if(!show) return;
    
        button.updateState();
        if(button.pressed()) show = false;
    
    }    
    

    // Reconfigure slider size on resize
    public void resize(int width, int height) {

        float uiScale = Math.min(width / 800f, height / 600f);

        offsetX = (width - 800f * uiScale) / 2f;
        offsetY = (height - 600f * uiScale) / 2f;

        x = Math.round(offsetX + baseX * uiScale);
        y = Math.round(offsetY + baseY * uiScale);

        w = Math.round(baseW * uiScale);
        h = Math.round(baseH * uiScale);

        button.resize(width, height);
    
    }


    // Show the dialog and wait while it is closed
    public void showAndWait() {
        this.show = true;
        uiSoundManager.getSound(POP_UP_SOUND).play(MiscUtils.getFloatSound(45), RandomUtils.randomFloat(0.95f, 1.05f), 1.0f);
    }

    public boolean isShowing() { return this.show; }

}
