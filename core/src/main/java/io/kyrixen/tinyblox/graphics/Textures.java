package io.kyrixen.tinyblox.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Camera;

public class Textures {

    // List of loaded textures
    private static final ArrayList<Texture> loadedTextures = new ArrayList<>();

    // Camera Object
    private Camera camera;


    // Create texture vars
    public static Texture menuBackgroundImage;
    public static Texture settingsBackgroundImage;

    public Texture playerTexture;
    public Texture enemyTexture;
    public Texture entityTexture;
    
    public Texture grassTexture;
    public Texture stoneTexture;
    public Texture dirtTexture;
    public Texture waterTexture;
    public Texture airTexture;
    public Texture terrainTileset;
    
    // UI Textures
    public static Texture dialogCorner;
    public static Texture dialogSide;
    public static Texture dialogCenter;
    public static Texture brownButton;
    public static Texture grayButton;
    public static Texture whiteToggleButton;
    public static Texture whiteSlider;

    // Constructor (Init camera var)
    public Textures(Camera camera) {
        this.camera = camera;
    }


    // Load texture
    public static Texture load(String path) {
    
        System.out.println("[Textures] Loading: " + path);
    
        try {

            Texture image = new Texture(path);
            loadedTextures.add(image);

            return image;

        } catch(NullPointerException e) {
            
            System.err.println("[Textures][ERROR] File not found: " + path);

        }

        return null;

    }

    // Draw a single tile / texture with camera offset
    public void draw(Texture tex, int x, int y, int w, int h, SpriteBatch batch) {

        if (tex == null) return;

        // Apply camera offset
        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = w * camera.zoom;
        float renderH = h * camera.zoom;

        batch.draw(tex, screenX, screenY, renderW, renderH, 0, 0, tex.getWidth(), tex.getHeight(), false, false);

    }

    // Draw one tile from a tileset
    public void drawTileset(Texture tileset, int x, int y, int w, int h, int tileX, int tileY, int tileSize, SpriteBatch batch) {
        
        if (tileset == null) return;

        int srcX = tileX * tileSize;
        int srcY = tileY * tileSize;

        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = w * camera.zoom;
        float renderH = h * camera.zoom;

        batch.draw(tileset, screenX, screenY, renderW, renderH, srcX, srcY, tileSize, tileSize, false, false);
    
    }

    // Show Menu background
    public static void initMenuBackground(){
        menuBackgroundImage = load("textures/background/menu_background.png");
    }

    // Show Menu background
    public static void showMenuBackground(SpriteBatch batch){

        batch.begin();
        batch.draw(menuBackgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }
    
    // Show Settings background
    public static void initSettingsBackground(){
        settingsBackgroundImage = load("textures/background/settings_background.png");
    }

    // Show Settings background
    public static void showSettingsBackground(SpriteBatch batch){

        batch.begin();
        batch.draw(settingsBackgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

    // Load textures
    public void initTextures() {
    
        playerTexture = load("textures/entities/player.png");
        enemyTexture = load("textures/entities/enemy.png");
        entityTexture = load("textures/entities/entity.png");

        grassTexture = load("textures/terrain/grass.png");
        stoneTexture = load("textures/terrain/stone.png");
        dirtTexture = load("textures/terrain/dirt.png");
        waterTexture = load("textures/terrain/water.png");
        airTexture = load("textures/terrain/air.png");
        terrainTileset = load("textures/terrain/terrain.png");
    
    }

    public static void initUITextures() {

        dialogCorner = load("textures/ui/dialog/dialog_corner.png");
        dialogSide = load("textures/ui/dialog/dialog_side.png");
        dialogCenter = load("textures/ui/dialog/dialog_center.png");
        brownButton = load("textures/ui/button/brown_button.png");
        grayButton = load("textures/ui/button/gray_button.png");
        whiteToggleButton = load("textures/ui/button/white_toggle_button.png");
        whiteSlider = load("textures/ui/slider/white_slider.png");
    
    }


    // Cleanup resources
    public void cleanup() {

        for(Texture tex : loadedTextures) { tex.dispose(); }

        // Clear loaded textures list
        loadedTextures.clear(); // removes references to Textures

        // Null other references
        camera = null;

    }
 
}
