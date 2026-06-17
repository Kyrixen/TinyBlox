package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.crafting.recipe.RecipeRegister;
import io.kyrixen.tinyblox.crafting.rendering.CraftingRenderer;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.FPSCounter;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.RendererUtils;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.EnemySpawner;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.TimeCycle.DayTime;
import io.kyrixen.tinyblox.world.chunk.structures.StructureRegister;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;

public class Engine implements Screen {

    // Player
    private Player player;

    // Flag for exiting
    public boolean exit = false;

    // List of entities
    private final ArrayList<Entity> entities = new ArrayList<>();

    // Module components
    private Controller controller;
    private final TextureManager textures;
    private final RendererStack rendererStack;
    private TileRenderer tileRenderer;
    private CraftingRenderer craftingRenderer;
    private Terrain terrain;
    private TimeCycle timeCycle;
    private FPSCounter fpsCounter;
    private SoundManager soundManager;
    private EnemySpawner enemySpawner;


    public Engine(RendererStack rendererStack, TextureManager tex) {
        this.textures = tex;
        this.rendererStack = rendererStack;
    }

    @Override
    public void show() {

        // Module components init
        controller = new Controller();
        tileRenderer = new TileRenderer(textures);
        craftingRenderer = new CraftingRenderer(textures);
        terrain = new Terrain(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, tileRenderer, (int) Math.floor(Math.random() * Integer.MAX_VALUE), 0.007f);
        timeCycle = new TimeCycle();
        fpsCounter = new FPSCounter();
        soundManager = new SoundManager();
        enemySpawner = new EnemySpawner(soundManager);

        init();

    }


    private void init() {

        // Initialize sprites
        textures.loadHUD();
        textures.loadGame();

        // Sound init
        soundManager.loadSFX();
        soundManager.loadHUD();

        // Load items
        ItemRegister.initItems();

        // Load recipes
        RecipeRegister.initRecipes();

        // Load structures
        StructureRegister.initStructures();

        // Terrain init
        terrain.init();

        // Time init
        timeCycle.setDayTime(DayTime.DAY);

        // Spawn cords
        int[] spawn = MiscUtils.spawnNearCenter(terrain);

        // Create player
        player = new Player(spawn[0], spawn[1], rendererStack.camera, soundManager);
        player.setLevel((byte) spawn[2]);

        // Add to list
        entities.add(player);

        // Create enemy
        Enemy enemy1 = new Enemy(spawn[0] + Constants.GRID_SIZE * 2 , spawn[1] + Constants.GRID_SIZE * 2, soundManager);

        // Add to list
        entities.add(enemy1);

        // Configure enemy
        enemy1.setTarget(player);
        enemy1.setChasing(true);
        enemy1.setLevel((byte) spawn[2]);

        Entity.initTextureAll(entities);

        player.getInventory().add(ItemRegister.LADDER, (byte) 12);
        player.getInventory().add(ItemRegister.STONE, (byte) 12);
    
    }

    // Game loop
    @Override
    public void render(float delta) {

        if(exit || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    
        update(delta);
        render();

        RendererUtils.limitFPS();

    }

    private void update(float delta) {

        Camera camera = rendererStack.camera;

        timeCycle.updateDayTime(delta);
        terrain.rebuildLighting(timeCycle);
        enemySpawner.updateSpawnRate(timeCycle);
        controller.update(delta, player, terrain, entities);

        enemySpawner.spawn(player, entities, terrain);
        enemySpawner.update(player, entities);

        Entity.updateAll(delta, terrain, entities);
        terrain.update(camera);

        // Update camera
        camera.follow(player);
        player.updateSelector();
        player.checkDropPickup(entities);

        
        for (Entity e : entities) {

            if(!(e instanceof Enemy)) continue;
            Enemy en = (Enemy) e;

            en.checkPlayer(player);

        }

        player.stats(camera);

        if(player.isDead()) {
            Logger.LOGGER.debug("ENGINE", "Player is dead! Health: " + player.health() + " | Game Over.");
            exit = true;
                
        }

        ArrayList<Entity> spawnedEntities = new ArrayList<>();
        entities.removeIf(e -> {

            if(!(e instanceof MobEntity)) return false;
            MobEntity mob = (MobEntity) e;

            if(!(mob instanceof Player) && mob.isDead()){
            
                if(mob instanceof Enemy) { 
                    Enemy enemy = (Enemy) mob;
                    enemy.throwLoot(player, spawnedEntities);
                }
                
                return true;
            
            }

            return false;
        
        });
        entities.addAll(spawnedEntities);

    }

    private void render() {

        // Get renderers
        SpriteBatch batch = rendererStack.batch;
        ShapeRenderer shape = rendererStack.shape;

        // Clear window
        RendererUtils.clear();

        // Lower World
        batch.begin();
        terrain.renderLower(player, rendererStack);
        batch.end();

        // World highlights
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        // Entities
        batch.begin();
        Entity.renderAll(terrain, player, tileRenderer, entities, rendererStack);
        batch.end();

        // Above Terrain and Terrain Depth Overlay
        batch.begin();
        terrain.renderAbove(player, rendererStack);
        terrain.renderDepthOverlay(player, timeCycle, rendererStack);
        batch.end();

        shape.begin(ShapeType.Line);
        terrain.drawHeightEdges(player, rendererStack);
        player.renderSelector(rendererStack);
        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // UI
        batch.begin();
        player.renderInvetory(textures, rendererStack);
        player.renderCraftingMenu(craftingRenderer, rendererStack);
        fpsCounter.printFPS(rendererStack);
        batch.end();


        // UI highlights
        shape.begin(ShapeType.Line);
        player.drawInventoryHighlight(rendererStack);
        shape.end();
    
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
        if(textures != null) { textures.cleanup(); }

        terrain.cleanup();

        if(soundManager != null) soundManager.cleanup();
        if(rendererStack != null) rendererStack.dispose();
    
    }

}
