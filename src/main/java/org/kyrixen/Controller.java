package org.kyrixen;


import static org.lwjgl.glfw.GLFW.*;


public class Controller {

    public void init() {
        // Initialization code if needed
    }


    public void update(long window, Entity entity) {
    
            if (Helper.keyPressed(window, GLFW_KEY_W)) entity.dirY = -1;
            if (Helper.keyPressed(window, GLFW_KEY_S)) entity.dirY = 1;
            if (Helper.keyPressed(window, GLFW_KEY_A)) entity.dirX = -1;
            if (Helper.keyPressed(window, GLFW_KEY_D)) entity.dirX = 1;
            if (Helper.keyPressed(window, GLFW_KEY_LEFT_SHIFT)) entity.consume(10);
            
            if (Helper.keyPressed(window, GLFW_KEY_G)){

                Sound.powerUp.play();

                while(Sound.powerUp.isRunning()) {
                    // Wait for the sound to finish
                }

                Sound.powerUp.reset();

            }

    }

}
