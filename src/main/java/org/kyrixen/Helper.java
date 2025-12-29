package org.kyrixen;


import static org.lwjgl.glfw.GLFW.*;


public class Helper {
    
    public static float getFloatColor(int color) {

        float floatColor = color / 255.0f;
        
        return floatColor;
    
    }


    public static float getFloatVolume(int volume) {

        float floatVolume = volume / 100.0f;
        
        return floatVolume;
    
    }


    public static boolean keyPressed(long window, int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }


    public static boolean keyReleased(long window, int key) {
        return glfwGetKey(window, key) == GLFW_RELEASE;
    }


    public static boolean anyKeyPressed(long window) {
    
        for (int key = GLFW_KEY_SPACE; key <= GLFW_KEY_LAST; key++) {
    
            if (glfwGetKey(window, key) == GLFW_PRESS) {
                return true; // some key is pressed
            }
    
        }
    
        return false;
    
    }


    public static boolean anyKeyReleased(long window) {

        for (int key = GLFW_KEY_SPACE; key <= GLFW_KEY_LAST; key++) {
        
            if (glfwGetKey(window, key) == GLFW_RELEASE) {
                return true; // some key is released
            }
        
        }
        
        return false;
    
    }
    

    public static boolean checkCollision(Entity e1, Entity e2) {
        
        return (e1.x < e2.x + e2.width &&
                e1.x + e1.width > e2.x &&
                e1.y < e2.y + e2.height &&
                e1.y + e1.height > e2.y);
    
    }


    public static void betterTilesetDraw(int x, int y, int tix, int tiy, Textures texture){
        texture.drawTilesetVBO(texture.terrainTileset, x, y, Constants.GRID_SIZE, Constants.GRID_SIZE, tix, tiy, Constants.GRID_SIZE, Constants.GRID_SIZE * 2, Constants.GRID_SIZE * 2);
    }


}