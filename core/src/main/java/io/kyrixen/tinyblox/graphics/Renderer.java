package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.world.Camera;

public class Renderer {

    // Own camera
    private Camera camera;

    // Missing texture colors
    private static final Color MISSING_GREEN = Color.valueOf("2fac60");
    private static final Color MISSING_BLUE = Color.valueOf("217095");


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

    @Deprecated
    // Draw grid
    public void drawGrid(ShapeRenderer shapeRenderer) {

        int worldLeft = camera.x;
        int worldRight = camera.x + (int) (camera.viewWidth / camera.zoom);
        int worldTop = camera.y;
        int worldBottom = camera.y + (int) (camera.viewHeight / camera.zoom);
        
        int grid = Constants.GRID_SIZE;

        int startX = Math.floorDiv(worldLeft, grid) * grid;
        int endX = (Math.floorDiv(worldRight, grid) + 1) * grid;

        int startY = Math.floorDiv(worldTop, grid) * grid;
        int endY = (Math.floorDiv(worldBottom, grid) + 1) * grid;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        for (int x = startX; x <= endX; x += grid) {
            float screenX = (x - camera.x) * camera.zoom;
            shapeRenderer.line(screenX, 0, screenX, camera.viewHeight);
        }
    
        for (int y = startY; y <= endY; y += grid) {
            float screenY = (y - camera.y) * camera.zoom;
            shapeRenderer.line(0, screenY, camera.viewWidth, screenY);
        }

        shapeRenderer.end();
    
    }

    // Draws missing texture (something like null block in minecraft)
    public static void drawMissingTexture(float x, float y, float w, float h, ShapeRenderer shapeRenderer) {

        shapeRenderer.begin(ShapeType.Filled);

        shapeRenderer.setColor(MISSING_GREEN);
        shapeRenderer.rect(x, y, w / 2f, h);

        shapeRenderer.setColor(MISSING_BLUE);
        shapeRenderer.rect(x + w / 2f, y, w / 2f, h);

        shapeRenderer.setColor(1f, 1f, 1f, 1f);

        shapeRenderer.end();

    }

    // Unload resources
    public void cleanup(){
        camera = null;
    }

}
