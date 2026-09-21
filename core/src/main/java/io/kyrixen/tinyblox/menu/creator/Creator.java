package io.kyrixen.tinyblox.menu.creator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.selection.Selection;
import io.kyrixen.tinyblox.menu.ui.Button;
import io.kyrixen.tinyblox.menu.ui.UIRenderer;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class Creator implements Screen {
    
    public boolean exit = false;
    
    private final Main main;

    private Button cancelButton;
    private Button doneButton;

    private SoundManager uiSoundManager;

    private final RendererStack rendererStack;

    private final TextureManager tex;
    private final UIRenderer uiRenderer;
    private final CreatorHelper creatorHelper;

    private static final TinyIdentifier grayButton = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"gray_button");
    private static final TinyIdentifier redButton = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"red_button");


    public Creator(Main main, RendererStack rendererStack, TextureManager tex, UIRenderer uiRenderer) {
        this.main = main;
        this.rendererStack = rendererStack;
        this.tex = tex;
        this.uiRenderer = uiRenderer;
        this.creatorHelper = new CreatorHelper();
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(creatorHelper);

        this.uiSoundManager = new SoundManager();
    
        this.cancelButton = new Button(uiSoundManager);
        this.doneButton = new Button(uiSoundManager);

        init();
    
    }


    private void init() {

        uiSoundManager.loadUI();

        cancelButton.init(20, 8, 48 * 5, 16 * 5, "CANCEL", 1.5f);
        cancelButton.initTexture(tex.getTexture(redButton));

        doneButton.init(540, 8, 48 * 5, 16 * 5, "DONE", 1.5f);
        doneButton.initTexture(tex.getTexture(grayButton));

    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        update(delta);
        draw();

    }

    private void update(float delta) {

        creatorHelper.update(Peripheral.getMouseX(), Constants.WINDOW_HEIGHT - Peripheral.getMouseY(), Gdx.input.justTouched());

        cancelButton.updateState();
        doneButton.updateState();

        if(doneButton.pressed()) { WorldManager.createWorld(creatorHelper.getWorldInfo()); Gdx.input.setInputProcessor(null); main.setScreen(new Selection(main, rendererStack, tex, uiRenderer));}
        if(cancelButton.pressed()) { Gdx.input.setInputProcessor(new Peripheral()); main.setScreen(new Selection(main, rendererStack, tex, uiRenderer)); }

    }

    private void draw() {

        ScreenUtils.clear(0.15f, 0.40f, 0.65f, 1f);

        uiRenderer.showSelectionBackground(rendererStack);

        rendererStack.batch.begin();
        creatorHelper.renderText(rendererStack);
        creatorHelper.renderInput(rendererStack);
        cancelButton.render(rendererStack);
        doneButton.render(rendererStack);
        rendererStack.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        Constants.WINDOW_WIDTH = width;
        Constants.WINDOW_HEIGHT = height;

        creatorHelper.resize(width, height);

        cancelButton.resize(width, height);
        doneButton.resize(width, height);

        rendererStack.resize(width, height);

        Logger.LOGGER.info("CREATOR", "Resizing window!");

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
