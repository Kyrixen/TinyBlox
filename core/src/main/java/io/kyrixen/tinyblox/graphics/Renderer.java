package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Camera;

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

    public void clear() {
        ScreenUtils.clear(Color.DARK_GRAY);
    }

    // Draw grid
    public void drawGrid(ShapeRenderer shapeRenderer) {
        int worldLeft = camera.x;
        int worldRight = camera.x + camera.viewWidth;
        int worldTop = camera.y;
        int worldBottom = camera.y + camera.viewHeight;
        int grid = Constants.GRID_SIZE;

        int startX = Math.floorDiv(worldLeft, grid) * grid;
        int endX = (Math.floorDiv(worldRight, grid) + 1) * grid;

        int startY = Math.floorDiv(worldTop, grid) * grid;
        int endY = (Math.floorDiv(worldBottom, grid) + 1) * grid;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        for (int x = startX; x <= endX; x += grid) {
            int screenX = x - camera.x;
            shapeRenderer.line(screenX, 0, screenX, camera.viewHeight);
        }
    
        for (int y = startY; y <= endY; y += grid) {
            int screenY = y - camera.y;
            shapeRenderer.line(0, screenY, camera.viewWidth, screenY);
        }

        shapeRenderer.end();
    
    }

    // Unload resources
    public void cleanup(){
        camera = null;
    }

}
