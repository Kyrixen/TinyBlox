package io.kyrixen.tinyblox.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Engine;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.settings.Settings;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.UIRenderer;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.graphics.RendererStack;

public class Menu implements Screen {

    public boolean exit = false;
    
    private final Main main;

    private SoundManager uiSoundManager;

    private Button playButton;
    private Button settingsButton;

    private final RendererStack rendererStack;

    private final TextureManager tex;
    private final UIRenderer uiRenderer;

    private static final TextureID brownButton = new TextureID("tinyblox", TextureType.UI,"brown_button");

    public Menu(Main main, RendererStack rendererStack, TextureManager tex) {
        this.main = main;
        this.rendererStack = rendererStack;
        this.tex = tex;
        this.uiRenderer = new UIRenderer(tex);
    }

    @Override
    public void show() {
        
        this.uiSoundManager = new SoundManager();

        this.playButton = new Button(uiSoundManager);
        this.settingsButton = new Button(uiSoundManager);

        init();
    
    }


    private void init() {
        
        tex.loadBackgrounds();
        tex.loadUI();
        
        uiSoundManager.loadUI();

        playButton.init(Constants.GRID_SIZE * 17, 128, 48 * 5, 16 * 5, "PLAY", 1.5f);
        playButton.initTexture(tex.getTexture(brownButton));
    
        settingsButton.init(Constants.GRID_SIZE * 17, 32, 48 * 5, 16 * 5, "SETTINGS", 1.5f);
        settingsButton.initTexture(tex.getTexture(brownButton));
    
    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        update(delta);
        draw();

    }

    private void update(float delta) {

        playButton.updateState();
        settingsButton.updateState();

        if(playButton.pressed()) main.setScreen(new Engine(tex));
        if(settingsButton.pressed()) main.setScreen(new Settings(this.main, this.rendererStack, this.tex, this.uiRenderer));

    }

    private void draw() {

        ScreenUtils.clear(Color.CYAN);
        
        uiRenderer.showMenuBackground(rendererStack);

        rendererStack.batch.begin();
        playButton.render(rendererStack);
        settingsButton.render(rendererStack);
        rendererStack.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        Logger.LOGGER.info("MENU", "Resizing window!");

    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {

        Logger.LOGGER.info("MENU", "On menu cleanup");

        if(rendererStack != null) rendererStack.dispose();
        
        playButton.dispose();
        settingsButton.dispose();

        uiSoundManager.cleanup();
    
    }

}
    

