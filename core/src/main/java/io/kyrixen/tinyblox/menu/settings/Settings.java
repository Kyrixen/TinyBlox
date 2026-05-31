package io.kyrixen.tinyblox.menu.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.Menu;
import io.kyrixen.tinyblox.menu.settings.uisettingsaddon.ToggleButton;
import io.kyrixen.tinyblox.menu.settings.uisettingsaddon.ToggleButton.ToggleButtonState;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.Slider;
import io.kyrixen.tinyblox.menu.ui.UIRenderer;
import io.kyrixen.tinyblox.sound.UISounds;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.RendererUtils;

public class Settings implements Screen {

    public boolean exit = false;
    
    private final Main main;

    private Button exitButton;
    private ToggleButton fpsButton;
    private ToggleButton vsyncButton;

    private Slider fpsSlider;
    private Slider musicSlider;

    private UISounds uiSoundManager;

    private SpriteBatch batch;

    private final TextureManager tex;
    private final UIRenderer uiRenderer;

    private static final TextureID grayButton = new TextureID("tinyblox", TextureType.UI,"gray_button");
    private static final TextureID whiteSlider = new TextureID("tinyblox", TextureType.UI,"white_slider");

    public Settings(Main main, TextureManager textureManager, UIRenderer uiRenderer) {
        this.main = main;
        this.tex = textureManager;
        this.uiRenderer = uiRenderer;
    }

    @Override
    public void show() {

        this.batch = new SpriteBatch();
        
        this.uiSoundManager = new UISounds();

        this.exitButton = new Button(uiSoundManager);
        this.fpsButton = new ToggleButton(uiSoundManager);
        this.vsyncButton = new ToggleButton(uiSoundManager);

        this.fpsSlider = new Slider(uiSoundManager);
        this.musicSlider = new Slider(uiSoundManager);

        init();
    
    }


    private void init() {
    
        exitButton.init(Constants.GRID_SIZE * 17, 32, 48 * 5, 16 * 5, "EXIT", 1.5f);
        exitButton.initTexture(tex.getTexture(grayButton));

        fpsButton.init(Constants.GRID_SIZE * 17, 288, 48 * 5, 16 * 5, "SHOW FPS", 1.5f);
        fpsButton.initTexture(tex.getTexture(grayButton));

        vsyncButton.init(Constants.GRID_SIZE * 17, 374, 48 * 5, 16 * 5, "VSYNC", 1.5f);
        vsyncButton.initTexture(tex.getTexture(grayButton));
        vsyncButton.setToggleState(ToggleButtonState.ON);

        fpsSlider.init(Constants.GRID_SIZE * 10, 128, 128 * 4, 16 * 4, 0.24f, 1000f, "FPS");
        fpsSlider.initTexture(tex.getTexture(whiteSlider), Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY);

        musicSlider.init(Constants.GRID_SIZE * 10, 208, 128 * 4, 16 * 4, 0.70f, 100f, "VOLUME");
        musicSlider.initTexture(tex.getTexture(whiteSlider), Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY);

    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        update(delta);
        draw();

    }

    private void update(float delta) {

        exitButton.updateState();
        fpsButton.updateState();
        vsyncButton.updateState();

        fpsSlider.updateState();
        musicSlider.updateState();

        if(exitButton.pressed()) main.setScreen(new Menu(main, tex));
        if(fpsButton.pressed()) Constants.SHOW_FPS = !Constants.SHOW_FPS;
        if(vsyncButton.pressed()) { RendererUtils.updateVsync(!Constants.VSYNC); }

        if(fpsSlider.pressed()) { RendererUtils.updateFPS(fpsSlider.getValue()); }
        if(musicSlider.pressed()) { Constants.VOLUME = musicSlider.getValue(); }

    }

    private void draw() {

        ScreenUtils.clear(Color.LIGHT_GRAY);

        uiRenderer.showSettingsBackground(batch);

        batch.begin();
        exitButton.render(batch);
        fpsButton.render(batch);
        vsyncButton.render(batch);
        batch.end();

        fpsSlider.render(batch);
        musicSlider.render(batch);

    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        Logger.LOGGER.info("SETTINGS", "Resizing window!");

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

        Logger.LOGGER.info("SETTINGS", "On settings cleanup");

        if(batch != null) batch.dispose();

        exitButton.dispose();
        fpsButton.dispose();
        vsyncButton.dispose();

        uiSoundManager.cleanup();

    }

}
