package org.kyrixen;


import java.util.ArrayList;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class Engine {

    // Player
    private Player player;

    // Flag for exiting
    public boolean exit = false;

    // List of entities
    private static ArrayList<Entity> entities = new ArrayList<>();

    // Module components
    private Renderer renderer;
    private Controller controller;
    private Textures textures;
    private Camera camera;
    private Terrain terrain;
    private FPSCounter fpsCounter;
    public SoundManager soundManager;

    // Window components
    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics2D g;
    
    public Engine() {
    
        // Module components init
        camera = new Camera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.RENDER_DISTANCE);
        renderer = new Renderer(camera);
        controller = new Controller();
        textures = new Textures(camera);
        terrain = new Terrain(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, 8, textures, camera, (int) Math.floor(Math.random() * Integer.MAX_VALUE), 0.05f, false);
        fpsCounter = new FPSCounter();
        soundManager = new SoundManager();

    }

    // Synchronous runner
    public void run() {
    
        init();
        game();
        cleanup();
    
    }


    private void init() {

        // Init window
        this.canvas = Main.canvas;

        // Ensure Canvas is realized
        canvas.requestFocus();
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
    
        // Initialize renderer and controller
        renderer.init();
        controller.init();
        textures.initTextures();

        // Spawn cords
        int spawnX = Constants.MAP_WIDTH / 2;
        int spawnY = Constants.MAP_HEIGHT / 2;

        // Create player
        player = new Player(0, Utils.spawnX(), Utils.spawnY(), Constants.GRID_SIZE, Constants.GRID_SIZE, entities, terrain, soundManager);

        // Add to list
        entities.add(player);

        // Create enemy
        Enemy enemy1 = new Enemy(0, spawnX + Constants.GRID_SIZE * 2, spawnY + Constants.GRID_SIZE * 2, Constants.GRID_SIZE, Constants.GRID_SIZE, terrain);
        
        // Add to list
        entities.add(enemy1);

        // Configure enemy
        enemy1.setTarget(player);
        enemy1.setChasing(true);

        Entity.initTextureAll(textures, entities);
        
        // Terrain init
        terrain.init();

        // Window closing
        Main.frame.addWindowListener(new WindowAdapter() {
        
            @Override
            public void windowClosing(WindowEvent e) {
                exit = true; // Stop engine loop
                System.out.println("Stopping");
            }
        
        });

    }

    // Game loop
    private void game() {
    
        float lastTime = (float) System.currentTimeMillis() / 1000;

        while (!exit) {

            // STOP if window / canvas is gone
            if (!canvas.isDisplayable()) {
                exit = true;
                break;
            }

            float currentTime = (float) System.currentTimeMillis() / 1000;
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            controller.update(player);

            Entity.updateAll(deltaTime, entities);
            terrain.update();

            // Update camera
            camera.follow(player);

            for (Entity e : entities) {

                if(e.type == "player") continue;

                e.check(player);
            
            }

            player.stats(camera);

            if(player.isDead()) {
                System.out.println("Player is dead! Health: " + player.health + " | Game Over.");
                exit = true;
                
            }

            entities.removeIf(e -> {
                if(!e.type.equals("player") && e.isDead()){
                    e.cleanup();

                    return true;
                }
                return false;
            });

            //if(Chunk.blockCollision(player).type() == "dirt") System.out.println("Standing on dirt!");

            if (bs == null) break;

            try {
                g = (Graphics2D) bs.getDrawGraphics();
            } catch (IllegalStateException e) {
                break; // window is already destroyed
            }

            renderer.clear(g);
            try {
                terrain.render(g);
            } catch (Exception e) {
                System.out.println("Error rendering terrain: " + e.getMessage());
                e.printStackTrace();
            }
            renderer.drawGrid(g);
            Entity.renderAll(textures, renderer, entities, g, camera);

            fpsCounter.update();

            fpsCounter.printFPS();

            g.dispose();
            bs.show();
            
        }

    }


    private void cleanup() {

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

        //terrain.cleanup();
        camera.cleanup();
        fpsCounter.cleanup();
        renderer.cleanup();
        
        bs = null;
        canvas = null;
        g = null;

        System.gc(); // Help GC

    }


}
