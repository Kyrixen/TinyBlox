package io.kyrixen.tinyblox.menu.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.graphics.UI.Button;
import io.kyrixen.tinyblox.graphics.UI.Slider;
import io.kyrixen.tinyblox.menu.Menu;
import io.kyrixen.tinyblox.menu.settings.UISettingsAddon.ToggleButton;
import io.kyrixen.tinyblox.menu.settings.UISettingsAddon.ToggleButton.ToggleButtonState;
import io.kyrixen.tinyblox.sound.UISounds;
import io.kyrixen.tinyblox.utils.Utils;

public class Settings implements Screen {
    

    public boolean exit = false;
    
    private Main main;

    private Button exitButton;
    private ToggleButton fpsButton;
    private ToggleButton vsyncButton;

    private Slider fpsSlider;
    private Slider musicSlider;

    private UISounds uiSoundManager;

    private SpriteBatch batch;

    public Settings(Main main) {
        this.main = main;
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

        Textures.initSettingsBackground();
    
        exitButton.init(Constants.GRID_SIZE * 17, 32, 48 * 5, 16 * 5, "EXIT", 48);
        exitButton.initTexture(Textures.grayButton);

        fpsButton.init(Constants.GRID_SIZE * 17, 288, 48 * 5, 16 * 5, "SHOW FPS", 48);
        fpsButton.initTexture(Textures.grayButton);

        vsyncButton.init(Constants.GRID_SIZE * 17, 374, 48 * 5, 16 * 5, "VSYNC", 48);
        vsyncButton.initTexture(Textures.grayButton);
        vsyncButton.setToggleState(ToggleButtonState.ON);

        fpsSlider.init(Constants.GRID_SIZE * 10, 128, 128 * 4, 16 * 4, 0.24f, 1000f, "FPS");
        fpsSlider.initTexture(Textures.whiteSlider, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY);

        musicSlider.init(Constants.GRID_SIZE * 10, 208, 128 * 4, 16 * 4, 0.70f, 100f, "VOLUME");
        musicSlider.initTexture(Textures.whiteSlider, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY);

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

        if(exitButton.pressed()) main.setScreen(new Menu(main));
        if(fpsButton.pressed()) Constants.SHOW_FPS = !Constants.SHOW_FPS;
        if(vsyncButton.pressed()) { Constants.VSYNC = !Constants.VSYNC; Utils.updateVsync(); }

        if(fpsSlider.pressed()) { Constants.FPS = fpsSlider.getValue(); Utils.updateFPS(); }
        if(musicSlider.pressed()) { Constants.VOLUME = musicSlider.getValue(); }

    }

    private void draw() {

        ScreenUtils.clear(Color.LIGHT_GRAY);

        Textures.showSettingsBackground(batch);

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

        System.out.println("Resizing window!");

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

        System.out.println("On settings cleanup");

        if(batch != null) batch.dispose();

        exitButton.dispose();
        fpsButton.dispose();
        vsyncButton.dispose();

        uiSoundManager.cleanup();

    }

}
