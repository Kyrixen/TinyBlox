package org.kyrixen;


public class Camera {

    public int x;
    public int y;

    public int viewWidth;
    public int viewHeight;
    public int RENDER_DISTANCE;


    public Camera(int viewWidth, int viewHeight, int  RENDER_DISTANCE) {
        
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.RENDER_DISTANCE = RENDER_DISTANCE;
    
    }


    public void follow(Entity target) {

        if (target == null) return;

        // Center camera on entity
        x += (target.x - x - viewWidth / 2);
        y += (target.y - y - viewHeight / 2);

    }

}
