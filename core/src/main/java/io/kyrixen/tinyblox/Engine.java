package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.entities.Enemy;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.Player;
import io.kyrixen.tinyblox.entities.Entity.EntityType;
import io.kyrixen.tinyblox.graphics.FPSCounter;
import io.kyrixen.tinyblox.graphics.Renderer;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.TileRenderer;

public class Engine implements Screen {

    // Player
    private Player player;

    // Flag for exiting
    public boolean exit = false;

    // List of entities
    private ArrayList<Entity> entities = new ArrayList<>();

    // Module components
    private Renderer renderer;
    private Controller controller;
    private TextureManager textures;
    private TileRenderer tileRenderer;
    private Camera camera;
    private Terrain terrain;
    private TimeCycle timeCycle;
    private FPSCounter fpsCounter;
    public Sfx soundManager;

    SpriteBatch batch;
    ShapeRenderer shape;


    public Engine(TextureManager tex) {
        this.textures = tex;
    }

    @Override
    public void show() {
    
        // Renderers init
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // Module components init
        camera = new Camera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.RENDER_DISTANCE, 3f);
        renderer = new Renderer(camera);
        controller = new Controller();
        tileRenderer = new TileRenderer(camera, textures, shape);
        terrain = new Terrain(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, tileRenderer, shape, (int) Math.floor(Math.random() * Integer.MAX_VALUE), 0.03f);
        timeCycle = new TimeCycle();
        fpsCounter = new FPSCounter();
        soundManager = new Sfx();

        init();

    }


    private void init() {

        // Initialize renderer
        renderer.init();

        // Initialize sprites
        textures.loadHUD();
        textures.loadGame();

        // Terrain init
        terrain.init();

        // Spawn cords
        int[] spawn = Utils.spawnNearCenter();

        // Create player
        player = new Player(0, spawn[0], spawn[1], camera, soundManager);

        // Add to list
        entities.add(player);

        // Create enemy
        Enemy enemy1 = new Enemy(0, spawn[0] + Constants.GRID_SIZE , spawn[1] + Constants.GRID_SIZE, soundManager);
        
        // Add to list
        entities.add(enemy1);

        // Configure enemy
        enemy1.setTarget(player);
        enemy1.setChasing(true);

        Entity.initTextureAll(entities);

    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    
        update(delta);
        render();

        renderer.limitFPS();

    }

    private void update(float delta) {

        timeCycle.updateDayTime(delta);

        controller.update(player, terrain, entities);

        Entity.updateAll(delta, terrain, entities);
        terrain.update(camera);

        // Update camera
        camera.follow(player);
        player.updateSelector();

        for (Entity e : entities) {

            if(e.type() == EntityType.ENEMY){ Enemy en = (Enemy) e; en.check(player); }

        }

        player.stats(camera);

        if(player.isDead()) {
            Logger.LOGGER.debug("ENGINE", "Player is dead! Health: " + player.health() + " | Game Over.");
            exit = true;
                
        }

        entities.removeIf(e -> {

            if(e.type() != EntityType.PLAYER && e.isDead()){
                if(e.type() == EntityType.ENEMY) soundManager.explosion.play(Utils.getFloatSound(35));
                return true;
            }

            return false;
        
        });

    }

    private void render() {

        renderer.clear();
           
        batch.begin();

        terrain.render(batch, timeCycle);    

        Entity.renderAll(timeCycle, tileRenderer, entities, batch);

        batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeType.Filled);
        terrain.renderDepthOverlay(camera, shape, timeCycle);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        shape.begin(ShapeType.Line);
        terrain.drawHeightEdges(camera, shape);
        shape.end();

        player.renderSelector(camera);
        player.renderInvetory(textures, batch, shape);

        batch.begin();
        fpsCounter.printFPS(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        Constants.WINDOW_HEIGHT = height;
        Constants.WINDOW_WIDTH = width;

        Logger.LOGGER.info("ENGINE", "Resizing window!");

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

        Logger.LOGGER.info("ENGINE", "On cleanup");

        // Clear the list after cleanup
        entities.clear();

        // Clean up textures
        if (textures != null) { textures.cleanup(); }

        terrain.cleanup();
        camera.cleanup();
        fpsCounter.cleanup();
        renderer.cleanup();

        if (soundManager != null) soundManager.cleanup();
        if (batch != null) batch.dispose();
        if (shape != null) shape.dispose();
    
    }

}
