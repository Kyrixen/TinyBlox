package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class Dialog {
    
    // Position
    private int x, y;
    private int baseX, baseY;

    // Size
    private int w, h;
    private int baseW, baseH;

    private float offsetX = 0f;
    private float offsetY = 0f;

    // Dialogue vars
    private String[] lines;
    
    private byte currentLine = 0;
    private int currentSymbol = 0;
    
    private boolean dialogueEnd = false;

    private float symbolTimer = 0f;
    private float symbolDelay = 0.06f;

    private boolean muteSound = false;

    private boolean active = false;

    // Sound Manager
    protected final SoundManager uiSoundManager;

    // Texture
    private Texture dialogTex;

    private static final TinyIdentifier KEY_TYPE_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "key_type");


    public Dialog(SoundManager uiSoundManager) {
        
        this.uiSoundManager = uiSoundManager;
        this.lines = new String[]{};

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

    }

    // Init texture
    public void initTexture(Texture dialogTexture) {
        this.dialogTex = dialogTexture;
    }


    public void render(RendererStack rendererStack) {

        if(!active || dialogueEnd || lines.length == 0) return;

        rendererStack.batch.draw(dialogTex, x, y, w, h);

        float uiScale = Math.min(Constants.WINDOW_WIDTH / 800f, Constants.WINDOW_HEIGHT / 600f);
        rendererStack.font.getData().setScale(0.80f * uiScale);
        
        String visibleLine = lines[currentLine].substring(0, currentSymbol);
        rendererStack.font.draw(rendererStack.batch, visibleLine, x + 30 * uiScale, y + h - 30 * uiScale, w - 45 * uiScale, Align.left, true);
        
        rendererStack.font.getData().setScale(1f);
    
    }

    public void updateState(float delta) {

        if(!active || dialogueEnd || lines.length == 0) return;

        String line = lines[currentLine];

        symbolTimer += delta;
        if(symbolTimer >= symbolDelay) {

            symbolTimer = 0f;

            if(currentSymbol < line.length()) {
                
                char currentChar = line.charAt(currentSymbol);

                if(currentChar == '(') { muteSound = true; symbolDelay = 0.03f; }
                if(currentChar == ')') { muteSound = false; symbolDelay = 0.06f; }
                if(currentChar == '.') symbolDelay = 0.24f;
                
                if(currentChar != ' ' && currentChar != ')' && !muteSound) uiSoundManager.getSound(KEY_TYPE_SOUND).play(MiscUtils.getFloatSound(50), RandomUtils.randomFloat(0.95f, 1.05f), 0f); 

                if(symbolDelay == 0.24f && currentChar != '.') symbolDelay = 0.06f;

                currentSymbol++;

            }

        }

        if(Peripheral.mouseJustPressed(Input.Buttons.LEFT) || Peripheral.keyJustPressed(Input.Keys.ENTER)) {

            if(currentSymbol < line.length()) { currentSymbol = line.length(); return; }
            if(currentLine + 1 >= lines.length) { dialogueEnd = true; active = false; return; }

            currentLine++;
            currentSymbol = 0;
            muteSound = false;
    
        }
    
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
    
    }


    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public void activate() {
        this.active = true;
        this.dialogueEnd = false;
        this.currentLine = 0;
        this.currentSymbol = 0;
        this.symbolTimer = 0f;
        this.muteSound = false;
    }

    public void deactivate() { this.active = false; }

    public void setCurrentLine(byte line) { if(line > lines.length) currentLine = (byte) (lines.length - 1); currentLine = line; }

    public int getCurrentLine() { return this.currentLine; }

    public boolean hasEnded() { return this.dialogueEnd; }

}
