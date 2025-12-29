package org.kyrixen;


import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.net.URL;
import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;
//import static org.lwjgl.system.MemoryStack.stackPush;


public class Textures {

    private final ArrayList<Integer> loadedTextures = new ArrayList<>();

    private Camera camera;


    public int playerTexture;
    public int enemyTexture;
    public int entityTexture;
    public int grassTexture;
    public int stoneTexture;
    public int dirtTexture;
    public int waterTexture;
    public int terrainTileset;


    public Textures(Camera camera) {
        this.camera = camera;
    }


    /** Load texture and return OpenGL ID */
    @Deprecated
    public int load(String classpathPath) {
        System.out.println("[Textures][Deprecated] Loading: " + classpathPath);
        return loadVBO(classpathPath);
    }


    /** Load texture into GPU and return OpenGL ID */
    public int loadVBO(String classpathPath) {
    
        System.out.println("[Textures] Loading: " + classpathPath);
    
        URL url = Textures.class.getResource(classpathPath);
    
        if (url == null) {
            System.err.println("[Textures][ERROR] File not found: " + classpathPath);
            return 0;
        }

        ByteBuffer image;
        int width, height;

        try (MemoryStack stack = MemoryStack.stackPush()) {
    
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
    
            image = STBImage.stbi_load(url.getPath(), w, h, comp, 4);
    
            if (image == null) {
                System.err.println("[Textures][ERROR] STB failed: " + STBImage.stbi_failure_reason());
                return 0;
            }

            width = w.get(0);
            height = h.get(0);
    
        }

        int tex = glGenTextures();
    
        loadedTextures.add(tex);
        glBindTexture(GL_TEXTURE_2D, tex);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        STBImage.stbi_image_free(image);

        return tex;
    }


    /** Draw texture at position */
    @Deprecated
    public void draw(int tex, int x, int y, int w, int h) {
        drawVBO(tex, x, y, w, h);
    }


    /** Draw a single tile using VBO */
    /** Draw a single tile using VBO (currently using immediate mode for compatibility) */
    public void drawVBO(int tex, int x, int y, int w, int h) {
    
        if (tex == 0) return;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, tex);

        glPushMatrix();
        glColor3f(1.0f, 1.0f, 1.0f); // white = show full texture
        glTranslatef(x - (float) camera.x, y - (float) camera.y, 0f);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(0, 0);
        glTexCoord2f(1, 0); glVertex2f(w, 0);
        glTexCoord2f(1, 1); glVertex2f(w, h);
        glTexCoord2f(0, 1); glVertex2f(0, h);
        glEnd();

        glPopMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    
    }

    
    /** Draw tileset tile */
    @Deprecated
    public void drawTileset(int tileset, int x, int y, int w, int h, int tileX, int tileY, int tileSize, int atlasWidth, int atlasHeight) {
        drawTilesetVBO(tileset, x, y, w, h, tileX, tileY, tileSize, atlasWidth, atlasHeight);
    }


    /** Draw tileset tile using VBO (currently using immediate mode for compatibility) */
    public void drawTilesetVBO(int tileset, int x, int y, int w, int h, int tileX, int tileY, int tileSize, int atlasWidth, int atlasHeight) {
        
        if (tileset == 0) return;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, tileset);

        glPushMatrix();
        glColor3f(1.0f, 1.0f, 1.0f); // white = show full texture
        glTranslatef(x - (float) camera.x, y - (float) camera.y, 0f);

        float u0 = (float)(tileX * tileSize) / atlasWidth;
        float u1 = (float)((tileX + 1) * tileSize) / atlasWidth;
        float v0 = 1f - (float)((tileY + 1) * tileSize) / atlasHeight;
        float v1 = 1f - (float)(tileY * tileSize) / atlasHeight;

        glBegin(GL_QUADS);
        glTexCoord2f(u0, v0); glVertex2f(0, 0);
        glTexCoord2f(u1, v0); glVertex2f(w, 0);
        glTexCoord2f(u1, v1); glVertex2f(w, h);
        glTexCoord2f(u0, v1); glVertex2f(0, h);
        glEnd();

        glPopMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    
    }


    /** Load default textures */
    public void initTextures() {
        playerTexture = loadVBO("/assets/textures/entities/player.png");
        enemyTexture = loadVBO("/assets/textures/entities/enemy.png");
        entityTexture = loadVBO("/assets/textures/entities/entity.png");
        grassTexture = loadVBO("/assets/textures/terrain/grass.png");
        stoneTexture = loadVBO("/assets/textures/terrain/stone.png");
        dirtTexture = loadVBO("/assets/textures/terrain/dirt.png");
        waterTexture = loadVBO("/assets/textures/terrain/water.png");
        terrainTileset = loadVBO("/assets/textures/terrain/terrain.png");
    }


    /** Cleanup texture resources */
    public void cleanupVBO() {
    
        for (int tex : loadedTextures) {
            glDeleteTextures(tex);
        }
    
        loadedTextures.clear();
    
    }


}