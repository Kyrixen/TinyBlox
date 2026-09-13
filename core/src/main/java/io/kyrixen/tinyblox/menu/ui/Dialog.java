package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

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

    // Size
    private int w, h;

    // Dialogue vars
    private String[] lines;
    
    private byte currentLine = 0;
    private int currentSymbol = 0;
    
    private boolean dialogueEnd = false;

    private float symbolTimer = 0f;
    private float symbolDelay = 0.06f;

    private boolean muteSound = false;

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

    }

    // Init texture
    public void initTexture(Texture dialogTexture) {
        this.dialogTex = dialogTexture;
    }


    public void render(RendererStack rendererStack) {

        if(lines.length == 0 || dialogueEnd) return;

        rendererStack.batch.draw(dialogTex, x, y, w, h);

        rendererStack.font.getData().setScale(0.80f);
        String visibleLine = lines[currentLine].substring(0, currentSymbol);
        rendererStack.font.draw(rendererStack.batch, visibleLine, x + 30, y + h - 30, w - 45, Align.left, true);
    
    }

    public void updateState(float delta) {

        if(dialogueEnd || lines.length == 0) return;

        String line = lines[currentLine];

        symbolTimer += delta;
        if(symbolTimer >= symbolDelay) {

            symbolTimer = 0f;

            if(currentSymbol < line.length()) {
                
                char currentChar = line.charAt(currentSymbol);

                if(currentChar == '(') muteSound = true;
                if(currentChar == ')') muteSound = false;
                
                if(currentChar != ' ' && currentChar != ')' && !muteSound) uiSoundManager.getSound(KEY_TYPE_SOUND).play(MiscUtils.getFloatSound(50), RandomUtils.randomFloat(0.95f, 1.05f), 0f); 

                currentSymbol++;

            }

        }

        if(Peripheral.keyJustPressed(Input.Keys.SPACE)) {

            if(currentSymbol < line.length()) { currentSymbol = line.length(); return; }
            if(currentLine + 1 >= lines.length) { dialogueEnd = true; return; }

            currentLine++;
            currentSymbol = 0;
            muteSound = false;
    
        }
    
    }            


    public void setLines(String[] lines) {
        this.lines = lines;
    }


    public boolean hasEnded() { return this.dialogueEnd; }

}
