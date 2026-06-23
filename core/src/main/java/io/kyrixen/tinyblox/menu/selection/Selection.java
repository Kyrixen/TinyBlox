package io.kyrixen.tinyblox.menu.selection;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Engine;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.creator.Creator;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.UIRenderer;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;

public class Selection implements Screen {
    
    public boolean exit = false;
    
    private final Main main;

    private Button loadButton;
    private Button createButton;
    private Button deleteButton;

    private SoundManager uiSoundManager;

    private final RendererStack rendererStack;

    private final TextureManager tex;
    private final UIRenderer uiRenderer;
    private final WorldList worldList;

    private static final TextureID grayButton = new TextureID("tinyblox", TextureType.UI,"gray_button");
    private static final TextureID redButton = new TextureID("tinyblox", TextureType.UI,"red_button");
    private static final TextureID worldSlot = new TextureID("tinyblox", TextureType.UI, "world_slot");


    public Selection(Main main, RendererStack rendererStack, TextureManager tex, UIRenderer uiRenderer) {
        this.main = main;
        this.rendererStack = rendererStack;
        this.tex = tex;
        this.uiRenderer = uiRenderer;
        this.worldList = new WorldList(tex.getTexture(worldSlot));
    }

    @Override
    public void show() {
        
        this.uiSoundManager = new SoundManager();

        this.loadButton = new Button(uiSoundManager);
        this.createButton = new Button(uiSoundManager);
        this.deleteButton = new Button(uiSoundManager);

        init();
    
    }


    private void init() {
        
        uiSoundManager.loadUI();

        loadButton.init(280, 8, 240, 80, "LOAD", 1.5f);
        loadButton.initTexture(tex.getTexture(grayButton));

        createButton.init(20, 8, 48 * 5, 16 * 5, "CREATE", 1.5f);
        createButton.initTexture(tex.getTexture(grayButton));

        deleteButton.init(540, 8, 48 * 5, 16 * 5, "DELETE", 1.5f);
        deleteButton.initTexture(tex.getTexture(redButton));

        List<WorldBlueprint> worlds = new ArrayList<>(WorldListScanner.getWorlds());
        worldList.updateWorldSlots(worlds);

    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        update(delta);
        draw();

    }

    private void update(float delta) {

        worldList.update(Peripheral.getMouseX(), Constants.WINDOW_HEIGHT - Peripheral.getMouseY(), Gdx.input.justTouched());

        loadButton.updateState();
        createButton.updateState();
        deleteButton.updateState();

        if(createButton.pressed()) main.setScreen(new Creator(main, rendererStack, tex, uiRenderer));
        if(loadButton.pressed() && worldList.canLoad()) { Constants.CURRENT_WORLD = worldList.getWorld().worldName; main.setScreen(new Engine(rendererStack, tex)); }
        if(deleteButton.pressed()) { worldList.deleteWorld(); }
    
    }

    private void draw() {

        ScreenUtils.clear(0.45f, 0.70f, 0.95f, 1f);

        uiRenderer.showSelectionBackground(rendererStack);

        rendererStack.batch.begin();
        worldList.render(rendererStack);
        loadButton.render(rendererStack);
        createButton.render(rendererStack);
        deleteButton.render(rendererStack);
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
        
        uiSoundManager.cleanup();
    
    }

}
