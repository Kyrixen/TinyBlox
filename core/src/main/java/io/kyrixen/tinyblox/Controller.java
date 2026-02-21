package io.kyrixen.tinyblox;

import com.badlogic.gdx.Input;

import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.utils.Peripheal;

public class Controller {

    public void init() {
        // Initialization code if needed
    }

    // Update controller for entity
    public void update(Entity entity) {
    
            entity.setDirX(0);
            entity.setDirY(0);

            // Checks for input
            if (Peripheal.keyPressed(Input.Keys.W)) entity.setDirY(1);
            if (Peripheal.keyPressed(Input.Keys.S)) entity.setDirY(-1);
            if (Peripheal.keyPressed(Input.Keys.A)) entity.setDirX(-1);
            if (Peripheal.keyPressed(Input.Keys.D)) entity.setDirX(1);
            
            // Checks for sprinting
            entity.sprint();
            
    }

}
