package io.kyrixen.tinyblox.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Camera;

public class Textures {

    // List of loaded textures
    private final ArrayList<Texture> loadedTextures = new ArrayList<>();

    // Camera Object
    private Camera camera;


    // Create texture vars
    public Texture backgroundImage;

    public Texture playerTexture;
    public Texture enemyTexture;
    public Texture entityTexture;
    
    public Texture grassTexture;
    public Texture stoneTexture;
    public Texture dirtTexture;
    public Texture waterTexture;
    public Texture terrainTileset;
    
    public Texture dialogCorner;
    public Texture dialogSide;
    public Texture dialogCenter;


    // Constructor (Init camera var)
    public Textures(Camera camera) {
        this.camera = camera;
    }


    // Load texture
    public Texture load(String path) {
    
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

    // Show background
    public void showBackground(SpriteBatch batch){

        backgroundImage = load("textures/background/background.png");

        batch.begin();
        batch.draw(backgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

    // Load textures
    public void initTextures() {
    
        playerTexture = load("textures/entities/player16.png");
        enemyTexture = load("textures/entities/enemy16.png");
        entityTexture = load("textures/entities/entity16.png");

        grassTexture = load("textures/terrain/grass.png");
        stoneTexture = load("textures/terrain/stone.png");
        dirtTexture = load("textures/terrain/dirt.png");
        waterTexture = load("textures/terrain/water.png");
        terrainTileset = load("textures/terrain/terrain16.png");
    
        dialogCorner = load("textures/ui/dialog/dialog_corner.png");
        dialogSide = load("textures/ui/dialog/dialog_side.png");
        dialogCenter = load("textures/ui/dialog/dialog_center.png");
    
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
