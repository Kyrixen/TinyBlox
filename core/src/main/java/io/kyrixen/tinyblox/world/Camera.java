package io.kyrixen.tinyblox.world;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;

public class Camera {

    // Camera position
    public int x;
    public int y;

    // Window size
    public final int viewWidth;
    public final int viewHeight;
    public float zoom;

    // Like Minecraft
    public final int RENDER_DISTANCE;


    // Setup camera
    public Camera(int viewWidth, int viewHeight, int RENDER_DISTANCE, float zoom) {
        
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.zoom = zoom;

        this.RENDER_DISTANCE = RENDER_DISTANCE;
    
    }

    // Follow entity
    public void follow(Entity target) {

        if (target == null) return;

        int worldViewWidth = (int) (viewWidth / zoom);
        int worldViewHeight = (int) (viewHeight / zoom);

        // Center camera on entity center using world-space view size.
        int targetCenterX = target.x() + target.width() / 2;
        int targetCenterY = target.y() + target.height() / 2;
        x += (targetCenterX - x - worldViewWidth / 2);
        y += (targetCenterY - y - worldViewHeight / 2);

        int worldPixelWidth = Constants.MAP_WIDTH * Constants.GRID_SIZE;
        int worldPixelHeight = Constants.MAP_HEIGHT * Constants.GRID_SIZE;

        int maxX = Math.max(0, worldPixelWidth - worldViewWidth);
        int maxY = Math.max(0, worldPixelHeight - worldViewHeight);

        x = Math.max(0, Math.min(x, maxX));
        y = Math.max(0, Math.min(y, maxY));

    }

}
