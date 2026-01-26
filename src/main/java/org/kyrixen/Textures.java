package org.kyrixen;


import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;


public class Textures {

    // List of loaded textures
    private final ArrayList<BufferedImage> loadedTextures = new ArrayList<>();

    // Camera Object
    private Camera camera;


    // Create texture vars
    public BufferedImage playerTexture;
    public BufferedImage enemyTexture;
    public BufferedImage entityTexture;
    public BufferedImage grassTexture;
    public BufferedImage stoneTexture;
    public BufferedImage dirtTexture;
    public BufferedImage waterTexture;
    public BufferedImage terrainTileset;


    // Constructor (Init camera var)
    public Textures(Camera camera) {
        this.camera = camera;
    }


    // Load texture
    public BufferedImage load(String path) {
    
        System.out.println("[Textures] Loading: " + path);
    
        try {

            File file = new File(path);
        
            BufferedImage image = ImageIO.read(file);
            loadedTextures.add(image);

            return image;

        } catch(IOException | NullPointerException e) {
            
            System.err.println("[Textures][ERROR] File not found: " + path);

            e.printStackTrace();

        }

        return null;

    }

    // Draw a single tile / texture with camera offset
    public void draw(BufferedImage tex, int x, int y, int w, int h, Graphics2D g) {

        if (tex == null) return;

        // Apply camera offset
        int screenX = x - camera.x;
        int screenY = y - camera.y;

        g.drawImage(tex, screenX, screenY, screenX + w, screenY + h, 0, 0, w, h, null);

    }

    // Draw tileset tile
    public void drawTileset(BufferedImage tileset, int x, int y, int w, int h, int tileX, int tileY, int tileSize, Graphics2D g) {

            if (tileset == null) return;

            int srcX = tileX * tileSize;
            int srcY = tileY * tileSize;

            g.drawImage(tileset, x - camera.x, y - camera.y, x - camera.x + w, y - camera.y + h, srcX, srcY, srcX + tileSize, srcY + tileSize, null); //Dont ask me how it works, i dont know either
        
    }


    // Load textures
    public void initTextures() {
    
        playerTexture = load("assets/textures/entities/player.png");
        enemyTexture = load("assets/textures/entities/enemy.png");
        entityTexture = load("assets/textures/entities/entity.png");
        grassTexture = load("assets/textures/terrain/grass.png");
        stoneTexture = load("assets/textures/terrain/stone.png");
        dirtTexture = load("assets/textures/terrain/dirt.png");
        waterTexture = load("assets/textures/terrain/water.png");
        terrainTileset = load("assets/textures/terrain/terrain.png");
    
    }


    // Cleanup resources
    public void cleanup() {

        // Clear loaded textures list
        loadedTextures.clear(); // removes references to BufferedImages

        // Null other references
        camera = null;

    }
 
}