package org.kyrixen;

import java.awt.event.KeyEvent;

public class Controller {

    public void init() {
        // Initialization code if needed
    }

    // Update controller for entity
    public void update(Entity entity) {
    
            entity.dirX = 0;
            entity.dirY = 0;

            // Checks for input
            if (Input.keyPressed(KeyEvent.VK_W)) entity.dirY = -1;
            if (Input.keyPressed(KeyEvent.VK_S)) entity.dirY = 1;
            if (Input.keyPressed(KeyEvent.VK_A)) entity.dirX = -1;
            if (Input.keyPressed(KeyEvent.VK_D)) entity.dirX = 1;
            
            // Checks for sprinting
            entity.sprint();
            
    }

}
