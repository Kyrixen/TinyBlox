package org.kyrixen;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.opengl.GL;


public class Engine {

    private long window;

    int test;

    public Player player;

    

    public static ArrayList<Entity> entities = new ArrayList<>();

    private Renderer renderer;
    private Controller controller;
    public Textures textures;
    protected Selector selector;
    private Camera camera;
    private Terrain terrain;
    private FPSCounter fpsCounter;
    
    

    public Engine() {
    
        camera = new Camera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 3);
        renderer = new Renderer(camera);
        controller = new Controller();
        textures = new Textures(camera);
        terrain = new Terrain(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, 8, textures, camera);
        fpsCounter = new FPSCounter();

    }


    public void run() {
    
        init();
        game();
        cleanup();
    
    }


    public void processEvents() {
        glfwPollEvents();
    }


    public void updateAll(float deltaTime, long window) {
        for (Entity e : entities) {
            e.update(deltaTime, window);
        }
    }


    public void renderAll(Textures textures) {
        for (Entity e : entities) {
            e.render(textures);
        }
    }


    public void initTextureAll(Textures textures) {
        for (Entity e : entities) {
            e.initTexture(textures);
        }
    }


    private void init() {
    
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        // Use default OpenGL context for maximum compatibility
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, "TinyEngine", 0, 0);

        if (window == 0) throw new RuntimeException("Failed to create window");

        // Make OpenGL commands affect this window
        glfwMakeContextCurrent(window);

        // Enable VSync
        glfwSwapInterval(0);

        // Show the window
        glfwShowWindow(window);

        // Initialize OpenGL functions for LWJGL
        GL.createCapabilities();

        // Set up 2D coordinates: top-left = (0,0) for legacy OpenGL compatibility
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize renderer and controller
        renderer.init();
        controller.init();
        textures.initTextures();
        Sound.initSounds();

        int spawnX = Constants.MAP_WIDTH / 2;
        int spawnY = Constants.MAP_HEIGHT / 2;

        player = new Player(spawnX, spawnY, Constants.GRID_SIZE, Constants.GRID_SIZE);

        entities.add(player);

        Enemy enemy1 = new Enemy(400, 400, Constants.GRID_SIZE, Constants.GRID_SIZE);
        entities.add(enemy1);

        enemy1.setTarget(player);
        enemy1.setChasing(false);

        selector = new Selector(player);

        initTextureAll(textures);
        terrain.init();

        test = textures.loadVBO("/assets/textures/terrain/dirt.png");

    }


    private void game() {
    
        float lastTime = (float) glfwGetTime();


        while (!glfwWindowShouldClose(window)) {


            float currentTime = (float) glfwGetTime();
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

        
            controller.update(window, player);

            updateAll(deltaTime, window);
            selector.update(deltaTime);
            terrain.update();
            camera.follow(player);
            terrain.separate(player);


            for (Entity e : entities) {

                if (e.type.equals("enemy") && !e.type.equals("player")) {

                    if (Helper.checkCollision(player, e)) {
                        
                        System.out.println("Collision detected between player and enemy!");
                        
                        player.damage(25);
                        
                    }
                
                }
            
            }

            System.out.println("Player Health: " + player.health + " | Player Pos: (" + player.x + ", " + player.y + ") | Camera: (" + camera.x + ", " + camera.y + ")");

            if(player.isDead()) {
                System.out.println("Player is dead! Health: " + player.health + " | Game Over.");
                glfwSetWindowShouldClose(window, true);
                
            }

            if(test == 0) System.out.println("FAILED TO LOAD");

            renderer.clear();
            //textures.draw(textures.dirtTexture, 50, 50, Constants.GRID_SIZE, Constants.GRID_SIZE);
            terrain.render();
            renderer.drawGrid();
            renderAll(textures);
            //selector.render(renderer);
            //textures.draw(test, player.x - Constants.GRID_SIZE, player.y - Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);

            fpsCounter.update();
            System.out.println("FPS: " + fpsCounter.getFPS());


            // Check for OpenGL errors
            int error = glGetError();
            if (error != GL_NO_ERROR) {
                System.out.println("OpenGL Error: " + error);
            }


            renderer.present(window);

            processEvents();
    

        }
    
    }


    private void cleanup() {

        for (Entity e : entities) {
            entities.remove(e);
        }

        textures.cleanupVBO();

        glfwDestroyWindow(window);
        glfwTerminate();

    }

}
