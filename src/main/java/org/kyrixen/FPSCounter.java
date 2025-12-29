package org.kyrixen;


import static org.lwjgl.glfw.GLFW.glfwGetTime;


public class FPSCounter {

    private int frames = 0;
    private int fps = 0;
    private double lastFpsFrame;


    public FPSCounter() {
        lastFpsFrame = glfwGetTime();
    }

    
    /** Call once per frame */
    public void update() {
    
        frames++;
    
        double currentTime = glfwGetTime();
    
        if (currentTime - lastFpsFrame >= 1.0) { // 1 second passed
            fps = frames;
            frames = 0;
            lastFpsFrame += 1.0;
        }
    
    }


    /** Returns the current FPS */
    public int getFPS() {
        return fps;
    }


    /** Optional: print FPS to console */
    public void printFPS() {
        System.out.println("FPS: " + fps);
    }

}
