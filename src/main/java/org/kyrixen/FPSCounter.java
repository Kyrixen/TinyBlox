package org.kyrixen;


import java.awt.Font;
import java.awt.Graphics2D;


public class FPSCounter {

    private int frames = 0;
    private int fps = 0;

    private long lastTime;

    private Font fpsFont;

    // Construct the counter
    public FPSCounter() {
        lastTime = System.currentTimeMillis();
        fpsFont = new Font("Arial", Font.BOLD, 30); // Set font
    }

    // Call once per frame
    public void update() {

        frames++;

        long now = System.currentTimeMillis();

        if (now - lastTime >= 1000) { // 1 second
            fps = frames;
            frames = 0;
            lastTime = now;
        }
    
    }

    // Get FPS as var
    public int getFPS() {
        return fps;
    }

    // Print FPS on screen
    public void printFPS(Graphics2D g) {
        g.setFont(fpsFont);
        g.drawString("FPS: " + fps, 25, 50);
    }

    // Free resources
    public void cleanup() {

        frames = 0;
        fps = 0;
        lastTime = 0;
        fpsFont = null;
    
    }

}

