package org.kyrixen;


public class FPSCounter {

    // Init helper vars
    private int frames = 0;
    private int fps = 0;
    private double lastFpsFrame;


    // Build object
    public FPSCounter() {
        lastFpsFrame = System.currentTimeMillis();
    }

    
    // Should call once per screen update
    public void update() {
    
        frames++;
        double currentTime = System.currentTimeMillis();
    
        if (currentTime / 1000 - lastFpsFrame >= 1.0) {

            fps = frames;
            frames = 0;
            lastFpsFrame += 1.0;
        
        }
    
    }


    // Current FPS
    public int getFPS() {
        return fps;
    }

    // Print FPS to console
    public void printFPS() {
        System.out.println("FPS: " + fps);
    }


    // Unload resources
    public void cleanup(){

        frames = 0;
        fps = 0;
        lastFpsFrame = 0;

    }

}
