package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.entities.Enemy;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.Player;
import io.kyrixen.tinyblox.entities.Entity.EntityType;
import io.kyrixen.tinyblox.graphics.FPSCounter;
import io.kyrixen.tinyblox.graphics.Renderer;
import io.kyrixen.tinyblox.graphics.Textures;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;

public class Engine implements Screen {

    // Player
    private Player player;

    // Flag for exiting
    public boolean exit = false;

    // List of entities
    private final static ArrayList<Entity> entities = new ArrayList<>();

    // Module components
    private Renderer renderer;
    private Controller controller;
    private Textures textures;
    private Camera camera;
    private Terrain terrain;
    private FPSCounter fpsCounter;
    public Sfx soundManager;

    SpriteBatch batch;
    ShapeRenderer shape;


    @Override
    public void show() {
    
        // Module components init
        camera = new Camera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.RENDER_DISTANCE, 3f);
        renderer = new Renderer(camera);
        controller = new Controller();
        textures = new Textures(camera);
        terrain = new Terrain(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, (byte) 12, textures, camera, (int) Math.floor(Math.random() * Integer.MAX_VALUE), 0.03f, false);
        fpsCounter = new FPSCounter();
        soundManager = new Sfx();
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        init();

    }


    private void init() {

        // Initialize renderer and controller
        renderer.init();
        controller.init();
        textures.initTextures();

        // Terrain init
        terrain.init();

        // Spawn cords
        int[] spawn = Utils.spawnNearCenter();

        // Create player
        player = new Player(0, spawn[0], spawn[1], Constants.GRID_SIZE, Constants.GRID_SIZE, entities, terrain, soundManager);

        // Add to list
        entities.add(player);

        // Create enemy
        Enemy enemy1 = new Enemy(0, spawn[0] + Constants.GRID_SIZE , spawn[1] + Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE, terrain, soundManager);
        
        // Add to list
        entities.add(enemy1);

        // Configure enemy
        enemy1.setTarget(player);
        enemy1.setChasing(true);

        Entity.initTextureAll(textures, entities);

    }

    // Game loop
    @Override
    public void render(float delta) {

        if (exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    
        update(delta);
        render();

    }

    private void update(float delta) {

        controller.update(player);

        Entity.updateAll(delta, entities);
        terrain.update();

        // Update camera
        camera.follow(player);

        for (Entity e : entities) {

            if(e.type() == EntityType.ENEMY){ Enemy en = (Enemy) e; en.check(player); }

        }

        player.stats(camera);

        if(player.isDead()) {
            System.out.println("Player is dead! Health: " + player.health() + " | Game Over.");
            exit = true;
                
        }

        entities.removeIf(e -> {

            if(e.type() != EntityType.PLAYER && e.isDead()){

                if(e.type() == EntityType.ENEMY) soundManager.explosion.play(Utils.getFloatVolume(35));
                
                e.cleanup();

                return true;
            }

            return false;
        
        });

    }

    private void render() {

        renderer.clear();
           
        batch.begin();

        try {
            terrain.render(batch);
        } catch (Exception e) {
            System.out.println("Error rendering terrain: " + e.getMessage());
        }
            

        Entity.renderAll(textures, renderer, entities, batch, camera);

        batch.end();

        renderer.drawGrid(shape);
        player.renderSelector(camera);

        batch.begin();
        fpsCounter.printFPS(batch);
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

        System.out.println("On cleanup");
  
        // Call cleanup on all entities, without removing them from the list
        for (Entity e : entities) {
            e.cleanup();  // call Entity own cleanup
        }

        // Clear the list after cleanup
        entities.clear();


        // Clean up textures
        if (textures != null) {
            textures.cleanup();
        }

        terrain.cleanup();
        camera.cleanup();
        fpsCounter.cleanup();
        renderer.cleanup();

        if (soundManager != null) soundManager.cleanup();
        
        if (batch != null) batch.dispose();
        if (shape != null) shape.dispose();

        System.gc(); // Help GC    
    
    }



}
