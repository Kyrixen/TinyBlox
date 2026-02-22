package io.kyrixen.tinyblox.world;

import io.kyrixen.tinyblox.entities.Entity;

public class Camera {

    // Camera position
    public int x;
    public int y;

    // Window size
    public int viewWidth;
    public int viewHeight;
    public float zoom;

    // Like Minecraft
    public int RENDER_DISTANCE;


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

        // Center camera on entity using world-space view size.
        x += (target.x() - x - worldViewWidth / 2);
        y += (target.y() - y - worldViewHeight / 2);

    }

    // Unload resources
    public void cleanup(){

        x = 0;
        y = 0;

        viewWidth = 0;
        viewHeight = 0;
        zoom = 0f;

        RENDER_DISTANCE = 0;

    }

}
