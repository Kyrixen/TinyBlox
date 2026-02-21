package io.kyrixen.tinyblox.world;

import io.kyrixen.tinyblox.entities.Entity;

public class Camera {

    // Camera position
    public int x;
    public int y;

    // Window size
    public int viewWidth;
    public int viewHeight;

    // Like Minecraft
    public int RENDER_DISTANCE;


    // Setup camera
    public Camera(int viewWidth, int viewHeight, int  RENDER_DISTANCE) {
        
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        this.RENDER_DISTANCE = RENDER_DISTANCE;
    
    }

    // Follow entity
    public void follow(Entity target) {

        if (target == null) return;

        // Center camera on entity
        x += (target.x() - x - viewWidth / 2);
        y += (target.y() - y - viewHeight / 2);

    }

    // Unload resources
    public void cleanup(){

        x = 0;
        y = 0;

        viewWidth = 0;
        viewHeight = 0;

        RENDER_DISTANCE = 0;

    }

}
