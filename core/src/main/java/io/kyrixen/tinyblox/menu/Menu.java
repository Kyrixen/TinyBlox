package io.kyrixen.tinyblox.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Engine;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.graphics.UI.Button;
import io.kyrixen.tinyblox.sound.UISounds;

public class Menu implements Screen {

    public boolean exit = false;
    
    private Main main;

    private UISounds uiSoundManager;

    private Button playButton;

    private SpriteBatch batch;

    public Menu(Main main) {
        this.main = main;
    }

    @Override
    public void show() {

        this.batch = new SpriteBatch();
        this.uiSoundManager = new UISounds();
        this.playButton = new Button(uiSoundManager);

        init();
    }


    private void init() {
        
        Textures.initBackground();
        Textures.initUITextures();

        playButton.init(Constants.GRID_SIZE * 17, 128, 48 * 5, 16 * 5, "PLAY", 48);
        playButton.initTexture(Textures.button);

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
        
        if(playButton.pressed()) main.setScreen(new Engine());
    
    }

    private void draw() {

        ScreenUtils.clear(Color.CYAN);
        
        Textures.showBackground(batch);

        batch.begin();
        playButton.render(batch);
        batch.end();

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

        System.out.println("On menu cleanup");

        if(batch != null) batch.dispose();
        playButton.dispose();

        uiSoundManager.cleanup();
    
    }

}
    

