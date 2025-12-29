package org.kyrixen;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Renderer {

    private Camera camera;


    public Renderer(Camera camera) {
        this.camera = camera;
    }


    public void init() {

        glClearColor(Helper.getFloatColor(50), Helper.getFloatColor(50), Helper.getFloatColor(50), Helper.getFloatColor(255));
        clear();

    }


    public void render() {

    }


    public void fillRect(int x, int y, int width, int height, int r, int g, int b) {

        int screenX = (int)(x - this.camera.x);
        int screenY = (int)(y - this.camera.y);

        glColor3f(Helper.getFloatColor(r), Helper.getFloatColor(g), Helper.getFloatColor(b));
        glBegin(GL_QUADS);
        glVertex2f(screenX, screenY);
        glVertex2f(screenX + width, screenY);
        glVertex2f(screenX + width, screenY + height);
        glVertex2f(screenX, screenY + height);
        glEnd();

    }


    public void drawRect(int x, int y, int width, int height, int r, int g, int b) {

        glColor3f(Helper.getFloatColor(r), Helper.getFloatColor(g), Helper.getFloatColor(b));
        glBegin(GL_LINE_LOOP);
        glVertex2f(x - camera.x, y - camera.y);
        glVertex2f(x + width - camera.x, y - camera.y);
        glVertex2f(x + width - camera.x, y + height - camera.y);
        glVertex2f(x - camera.x, y + height - camera.y);
        glEnd();

    }


    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }


    public void present(long window) {

        glfwSwapBuffers(window);

    }


    public void drawGrid() {

        glColor3f(Helper.getFloatColor(0), Helper.getFloatColor(0), Helper.getFloatColor(0));

        glBegin(GL_LINES);

        // Calculate world coordinates for screen bounds
        int worldLeft = camera.x;
        int worldRight = camera.x + Constants.WINDOW_WIDTH;
        int worldTop = camera.y;
        int worldBottom = camera.y + Constants.WINDOW_HEIGHT;

        // Find the first grid line to the left of the visible area
        int startX = (worldLeft / Constants.GRID_SIZE) * Constants.GRID_SIZE;
        // Find the last grid line to the right of the visible area
        int endX = ((worldRight / Constants.GRID_SIZE) + 1) * Constants.GRID_SIZE;

        // Draw vertical grid lines
        for (int x = startX; x <= endX; x += Constants.GRID_SIZE) {
            int screenX = x - camera.x;
            glVertex2f(screenX, 0);
            glVertex2f(screenX, Constants.WINDOW_HEIGHT);
        }

        // Find the first grid line above the visible area
        int startY = (worldTop / Constants.GRID_SIZE) * Constants.GRID_SIZE;
        // Find the last grid line below the visible area
        int endY = ((worldBottom / Constants.GRID_SIZE) + 1) * Constants.GRID_SIZE;

        // Draw horizontal grid lines
        for (int y = startY; y <= endY; y += Constants.GRID_SIZE) {
            int screenY = y - camera.y;
            glVertex2f(0, screenY);
            glVertex2f(Constants.WINDOW_WIDTH, screenY);
        }

        glEnd();

    }

}
