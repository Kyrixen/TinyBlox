package io.kyrixen.tinyblox.tutorial;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Controller;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.crafting.recipe.RecipeRegister;
import io.kyrixen.tinyblox.crafting.rendering.CraftingRenderer;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.NPC;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.inventory.ItemRegister;
import io.kyrixen.tinyblox.menu.Menu;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.RendererUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.TimeCycle.DayTime;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkGenerator;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRegister;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Tutorial implements Screen {
    
    private final Main main;

    private Player player;
    private NPC npc;
    private Enemy enemy = null;

    private final RendererStack rendererStack;
    private final TextureManager textureManager;

    private TileRenderer tileRenderer;
    private CraftingRenderer craftingRenderer;
    private SoundManager soundManager;
    private TutorialTerrain tutorialTerrain;

    private TimeCycle timeCycle;
    private Controller controller;

    private boolean controlsDone = false;
    private boolean sprintDone = false;

    private boolean blockDestroyed = false;
    private boolean inventoryOpened = false;
    private boolean blockPlaced = false;
    
    private boolean cutTree = false;
    private boolean openCrafting = false;
    private boolean craftSword = false;

    private boolean terrainPlaced = false;
    private boolean terrainIntroduced = false;
    private boolean walkUp = false;
    private boolean walkDown = false;
    
    private boolean terrainArena = false;
    private boolean terrainArenaPlaced = false;
    private boolean fightEnemy = false;
    private boolean tutorialEnd = false;


    public Tutorial(Main main, RendererStack rendererStack, TextureManager textureManager) {
        this.main = main;
        this.rendererStack = rendererStack;
        this.textureManager = textureManager;
    }   

    @Override
    public void show() {

        this.soundManager = new SoundManager();

        this.tileRenderer = new TileRenderer(textureManager);
        this.craftingRenderer = new CraftingRenderer(textureManager);

        this.tutorialTerrain = new TutorialTerrain(tileRenderer);

        this.timeCycle = new TimeCycle();

        this.controller = new Controller();

        init();

    }

    private void init() {

        // Sets input processor
        Gdx.input.setInputProcessor(new Peripheral());

        // Initialize sprites
        textureManager.loadHUD();
        textureManager.loadUI();
        textureManager.loadGame();

        // Sound init
        soundManager.loadSFX();
        soundManager.loadHUD();
        soundManager.loadUI();

        // Load tiles
        TileRegister.initTiles();

        // Load items
        ItemRegister.initItems();

        // Load recipes
        RecipeRegister.initRecipes();

        // Terrain init
        tutorialTerrain.init(soundManager);

        // Time init
        timeCycle.setDayTime(DayTime.DAY);
        timeCycle.updateDayTime(0.3f);
        tutorialTerrain.rebuildLighting();


        this.npc = new NPC(32, 16, soundManager);
        this.npc.setLevel((byte) 5);
        this.npc.initTexture();
        this.npc.initDialogue(textureManager);
        this.npc.getDialogue().setLines(new String[]{"HELLO! (PRESS LEFT MOUSE BUTTON OR ENTER TO CONTINUE)", "IM JERRY! YOUR GUIDE!", "ILL HELP YOU WITH THE CONTROLS AND OTHER COOL STUFF!", "SO FIRST... THE CONTROLS! USE WASD TO MOVE! TRY IT YOURSELF!", "WHEN YOU GET HANG OF IT CLICK ME!", "(LEFT MOUSE BUTTON OR ENTER TO EXIT DIALOG)"});

        this.player = new Player(64, 16, rendererStack.camera, soundManager);
        this.player.setLevel((byte) 5);
        this.player.initTexture();
        player.getInventoryRenderer().toggleRendering();

    }

    @Override
    public void render(float deltaTime) {
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) main.setScreen(new Menu(main, rendererStack, textureManager));
    
        update(deltaTime);
        render();

        RendererUtils.limitFPS();
    
    }



    private void update(float deltaTime) {

        int scroll = Peripheral.mouseScroll();

        Camera camera = rendererStack.camera;

        controller.update(deltaTime, player, scroll, tutorialTerrain);

        player.update(deltaTime, tutorialTerrain);

        // Checks map border (to prevent distraction or getting lost)
        if(player.x() > Constants.GRID_SIZE * Constants.CHUNK_SIZE || player.y() > Constants.GRID_SIZE * Constants.CHUNK_SIZE / 4 * 3) { player.setX(64); player.setY(16); player.setLevel((byte) 5); player.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1)); }

        npc.update(deltaTime, tutorialTerrain);
        tutorialTerrain.updateEntities(deltaTime, player);


        // Tutorial checkers
        
        if(!controlsDone && npc.getDialogue().hasEnded() && Peripheral.anyWASDPressed()) {
            controlsDone = true;
            npc.getDialogue().setLines(new String[]{"NICE!", "THAT WOULD BE FOR THE MOVEMENT!", "NOW TRY TO SPRINT!", "TO SPRINT, HOLD LEFT CTRL WHILE MOVING!", "THEN AGAIN, WHEN YOU GET THE HANG OF IT, TELL ME!", "(LEFT MOUSE BUTTON OR ENTER TO EXIT DIALOG)"});
        }

        if(controlsDone && !sprintDone && npc.getDialogue().hasEnded() && Peripheral.keyJustPressed(Keys.CONTROL_LEFT)) {
            sprintDone = true;
            npc.getDialogue().setLines(new String[]{"WOAH... WHAT A SPEED!!", "YOURE NATURAL!", "NOW... TRY TO DESTROY A TILE!", "HOLD LEFT MOUSE BUTTON OR H TO DESTROY A TILE!", "TO MOVE CURSOR USE MOUSE OR IJKL!"});
            player.getInventory().nextSlot();
        }

        if(sprintDone && !blockDestroyed && npc.getDialogue().hasEnded() && (Peripheral.mouseJustPressed(Buttons.LEFT) || Peripheral.keyJustPressed(Keys.H))) {
            blockDestroyed = true;
            npc.getDialogue().setLines(new String[]{"EXCELENT!", "NOW LETS LOOK AT OUR STASH OF TILES!", "USE E TO OPEN AN INVENTORY", "IF YOU WANT SOMETIMES TO CLOSE IT, PRESS E AGAIN!"});
        }
        
        if(blockDestroyed && !inventoryOpened && npc.getDialogue().hasEnded() && Peripheral.keyJustPressed(Keys.E)) {
            inventoryOpened = true;
            npc.getDialogue().setLines(new String[]{"HMMM... THATS A LOT OF TILES!", "USE MOUSE SCROLL OR KEYS O AND P TO SCROLL THROUGH OUR INVENTORY!"});
        }
        
        if(inventoryOpened && !blockPlaced && npc.getDialogue().hasEnded() && (scroll != 0 || Peripheral.keyJustPressed(Keys.O) || Peripheral.keyJustPressed(Keys.P))) {
            blockPlaced = true;
            npc.getDialogue().setLines(new String[]{"NOW... TRY TO SELECT SOME TILE AND PLACE IT!", "PRESS RIGHT MOUSE BUTTON OR U TO PLACE A TILE!"});
        }

        if(blockPlaced && !cutTree && (Peripheral.mouseJustPressed(Buttons.RIGHT) || Peripheral.keyJustPressed(Keys.U))) {
            
            cutTree = true;
            npc.getDialogue().setLines(new String[]{"LOOK AT THAT PIECE OF TILE!", "ITS AMAZING!", "NOW.. LETS CUT DOWN A TREE!", "USE YOUR KNOWLEDGE TO FIGURE IT OUT AND THEN COME BACK TO ME WHEN YOU HAVE THE WOOD!"});        
        
            Chunk chunk = tutorialTerrain.getChunk((short) 0, (short) 0);

            byte choosenX = 6;
            byte choosenY = 6;

            byte baseLevel = 4;
            byte TREE_RADIUS = 1;

            chunk.getTileStack(choosenX, choosenY).set(new Tile(TileRegister.WOOD, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));
            chunk.getTileStack(choosenX, choosenY).set(new Tile(TileRegister.LEAVES, (byte) (baseLevel + 2)), (byte) (baseLevel + 2));

            for(byte neighborX = (byte) -TREE_RADIUS; neighborX <= TREE_RADIUS; neighborX++) {

                for(byte neighborY = (byte) -TREE_RADIUS; neighborY <= TREE_RADIUS; neighborY++) {

                    if(neighborX == 0 && neighborY == 0) continue;
                    if(chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)) == null) continue;

                    chunk.getTileStack((byte) (choosenX + neighborX), (byte) (choosenY + neighborY)).set(new Tile(TileRegister.LEAVES, (byte) (baseLevel + 1)), (byte) (baseLevel + 1));

                }

            }
        
        }

        if(cutTree && !openCrafting && npc.getDialogue().hasEnded() && player.getInventory().contains(ItemRegister.WOOD)) {
            openCrafting = true;
            player.getInventory().add(ItemRegister.WOOD, (byte) 1);
            npc.getDialogue().setLines(new String[]{"NICE! NOW WE HAVE WOOD!", "USE THE WOOD TO CRAFT A WOODEN SWORD!", "TO OPEN CRAFTING MENU PRESS C! TO CLOSE IT USE C AGAIN!", "YOU CAN SCROLL THROUGH RECIPES BY SCROLLING WITH MOUSE OR BY PRESSING KEYS O AND P!"});
        }

        if(openCrafting && !craftSword && npc.getDialogue().hasEnded() && player.getInventory().contains(ItemRegister.WOODEN_SWORD)) {
            craftSword = true;
            npc.getDialogue().setLines(new String[]{"LOOK AT THAT SHARP EDGE!", "NOW YOU CAN DEFEND YOURSELF!", "NOW... GIMME A SEC... ILL MAKE THIS WORLD A BIT BETTER!"});
        
        }

        if(craftSword && !terrainPlaced && npc.getDialogue().hasEnded() && npc.getDialogue().getCurrentLine() == 2) {

            terrainPlaced = true;

            FastNoiseLite noise = new FastNoiseLite();
            noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            noise.SetSeed(0);
            noise.SetFrequency(0.012f);

            for(Chunk chunk : tutorialTerrain.getChunks().values()) {
                chunk.set(new TileStack[12][12]);
            }

            for(Chunk chunk : tutorialTerrain.getChunks().values()) {
                ChunkGenerator.generateChunk(chunk, noise);
            }

            player.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1));
            npc.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (npc.x() / Constants.GRID_SIZE), (int) (npc.y() / Constants.GRID_SIZE)) + 1));

        }

        if(terrainPlaced && !terrainIntroduced && npc.getDialogue().hasEnded()) {
            terrainIntroduced = true;
            npc.getDialogue().setLines(new String[]{"NICE... NOW THE WORLD IS DONE!", "SEE THOSE BLACK LINES?", "THEY SHOW WHERE ONE LAYER ENDS.", "TRY WALKING UP TO ONE."});
        }

        if(terrainIntroduced && !walkUp && npc.getDialogue().hasEnded() && npc.getDialogue().getCurrentLine() == 3) {
            walkUp = true;
            npc.getDialogue().setLines(new String[]{"SEE?... THE DARKER LAYER IS LOWER.", "THE LIGHTER ONE IS HIGHER.", "NOW TRY CLIMB ONE!", "HOLD SPACE WHILE MOVING!"});
        }

        if(walkUp && !walkDown && npc.getDialogue().hasEnded() && Peripheral.anyWASDPressed() && Peripheral.keyJustPressed(Keys.SPACE)) {
            walkDown = true;
            npc.setY((int) npc.y() + Constants.GRID_SIZE * 2);
            npc.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1));  
            npc.getDialogue().setLines(new String[]{"NICE!", "TO GO DOWN, HOLD LEFT SHIFT WHILE MOVING!"});
        }

        if(walkDown && !terrainArena && npc.getDialogue().hasEnded() && Peripheral.anyWASDPressed() && Peripheral.keyJustPressed(Keys.SHIFT_LEFT)) {
            terrainArena = true;
            npc.setY((int) npc.y() - Constants.GRID_SIZE * 2);
            npc.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1));  
            npc.getDialogue().setLines(new String[]{"WOAH!.. YOU ARE TINYBLOX MASTER!", "NOW... LETS MAKE THIS A LITTLE MORE INTERESTING..."});
            npc.getDialogue().activate();
        }

        if(terrainArena && !terrainArenaPlaced && npc.getDialogue().hasEnded() && npc.getDialogue().getCurrentLine() == 1) {
            terrainArenaPlaced = true;

            FastNoiseLite noise = new FastNoiseLite();
            noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            noise.SetSeed(6);
            noise.SetFrequency(0.012f);

            for(Chunk chunk : tutorialTerrain.getChunks().values()) {
                ChunkGenerator.generateChunk(chunk, noise);
            }

            player.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1));
            npc.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (npc.x() / Constants.GRID_SIZE), (int) (npc.y() / Constants.GRID_SIZE)) + 1));

        }

        if(terrainArenaPlaced && !fightEnemy && npc.getDialogue().hasEnded()) {
            
            fightEnemy = true;

            npc.getDialogue().setLines(new String[]{"NOW... YOU CAN TRY TO DEFEAT ENEMY!", "YOU CAN USE WOODEN SWORD YOU HAVE CRAFTED!", "ITS QUITE EASY! YOU CAN USE TERRAIN HEIGHT AS ADVANTAGE!", "PRESS LEFT MOUSE BUTTON OR H TO HIT AN ENTITY!", "JUST... DONT HIT ME PLEASE!"});
            npc.getDialogue().activate();

            enemy = new Enemy(player.x() + Constants.GRID_SIZE * 2, player.y() + Constants.GRID_SIZE * 2, soundManager);
            enemy.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (enemy.x() / Constants.GRID_SIZE), (int) (enemy.y() / Constants.GRID_SIZE)) + 1));
            enemy.initTexture();

            tutorialTerrain.addEntity(enemy);
            
        }

        if(fightEnemy && !tutorialEnd && npc.getDialogue().hasEnded() && enemy.isDead()) {
            tutorialEnd = true;
            npc.getDialogue().setLines(new String[]{"VERY WELL!", "YOU HAVE SUCCESSFULY COMPLETED THIS TUTORIAL!", "THERES MUCH MORE TO TINYBLOX THAT THIS TUTORIAL CANNOT (SOME WORD)!", "NOW... GO! FAREWELL TRAVELER!"});
            npc.getDialogue().activate();
        }

        if(tutorialEnd && npc.getDialogue().hasEnded()) main.setScreen(new Menu(main, rendererStack, textureManager));


        // Update tutorialTerrain
        tutorialTerrain.updateLoadedChunks(player, soundManager);
        tutorialTerrain.update(camera, timeCycle);

        // Update camera
        camera.follow(player);
        player.updateSelector();
        player.checkDropPickup(tutorialTerrain);

        player.stats(camera);

        if(player.isDead()) {
            Logger.LOGGER.debug("TUTORIAL", "Player is dead! Health: " + player.getHealth() + " | Game Over.");

            player.throwLoot(player, tutorialTerrain);

            player.getInventory().clear();
                       
            player.setX(64);
            player.setY(16);
            player.setLevel((byte) (tutorialTerrain.getWorldLevel((int) (player.x() / Constants.GRID_SIZE), (int) (player.y() / Constants.GRID_SIZE)) + 1));
            player.setHealth(100);
            
        }

    }

    private void render() {

        // Get renderers
        SpriteBatch batch = rendererStack.batch;
        ShapeRenderer shape = rendererStack.shape;

        // Clear window
        RendererUtils.clear();

        // Lower World
        batch.begin();
        tutorialTerrain.renderLower(player, rendererStack);
        batch.end();

        // World highlights
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        // Entities
        batch.begin();
        tutorialTerrain.renderEntities(player, tileRenderer, rendererStack);
        npc.render(tutorialTerrain, player, tileRenderer, rendererStack);
        player.render(tutorialTerrain, player, tileRenderer, rendererStack);
        batch.end();

        // Above Terrain and Terrain Depth Overlay
        batch.begin();
        tutorialTerrain.renderAbove(player, rendererStack);
        tutorialTerrain.renderDepthOverlay(player, timeCycle, rendererStack);
        batch.end();

        shape.begin(ShapeType.Line);
        tutorialTerrain.drawHeightEdges(player, rendererStack);
        player.renderSelector(rendererStack);
        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // UI
        batch.begin();
        player.renderInvetory(textureManager, rendererStack);
        player.renderCraftingMenu(craftingRenderer, rendererStack);
        npc.renderDialog(rendererStack);
        batch.end();


        // UI highlights
        shape.begin(ShapeType.Line);
        player.drawInventoryHighlight(rendererStack);
        shape.end();

    }


    @Override
    public void resize(int width, int height) {
    
        if(width <= 0 || height <= 0) return;

        Constants.WINDOW_HEIGHT = height;
        Constants.WINDOW_WIDTH = width;

        Logger.LOGGER.info("TUTORIAL", "Resizing window!");
    
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
    
        Logger.LOGGER.info("TUTORIAL", "On cleanup");

        // Clean up textureManager
        if(textureManager != null) { textureManager.cleanup(); }

        tutorialTerrain.cleanup();

        if(soundManager != null) soundManager.cleanup();
        if(rendererStack != null) rendererStack.dispose();

    }

}
