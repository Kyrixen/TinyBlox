package org.kyrixen;


import java.awt.Graphics2D;
import java.awt.Color;


public class Renderer {

    // Own camera
    private Camera camera;

    // Pass the camera
    public Renderer(Camera camera) {
        this.camera = camera;
    }

    public void init() {
        // Init code if needed
    }

    public void clear(Graphics2D g) {
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.MAP_HEIGHT);
    }

    // Draw grid
    public void drawGrid(Graphics2D g) {
        
        // Set grid color (black)
        g.setColor(new Color(0, 0, 0));

        // Calculate visible world coordinates
        int worldLeft = camera.x;
        int worldRight = camera.x + Constants.WINDOW_WIDTH;
        int worldTop = camera.y;
        int worldBottom = camera.y + Constants.WINDOW_HEIGHT;

        // Vertical grid lines
        int startX = (worldLeft / Constants.GRID_SIZE) * Constants.GRID_SIZE;
        int endX = ((worldRight / Constants.GRID_SIZE) + 1) * Constants.GRID_SIZE;
        
        for (int x = startX; x <= endX; x += Constants.GRID_SIZE) {
            int screenX = x - camera.x; // convert to screen coordinates
            g.drawLine(screenX, 0, screenX, Constants.WINDOW_HEIGHT);
        }

        // Horizontal grid lines
        int startY = (worldTop / Constants.GRID_SIZE) * Constants.GRID_SIZE;
        int endY = ((worldBottom / Constants.GRID_SIZE) + 1) * Constants.GRID_SIZE;
        
        for (int y = startY; y <= endY; y += Constants.GRID_SIZE) {
            int screenY = y - camera.y; // convert to screen coordinates
            g.drawLine(0, screenY, Constants.WINDOW_WIDTH, screenY);
        }
    
    }

    // Unload resources
    public void cleanup(){
        camera = null;
    }

}
