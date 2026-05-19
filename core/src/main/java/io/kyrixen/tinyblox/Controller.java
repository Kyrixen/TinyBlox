package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Input;

import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.Player;
import io.kyrixen.tinyblox.entities.Entity.Speed;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.world.Terrain;

public class Controller {

    // Update controller for entity
    public void update(float deltaTime, Player player, Terrain terrain, ArrayList<Entity> entities) {
    
            player.setDirX(0);
            player.setDirY(0);

            // Checks for movement input
            if (Peripheal.keyPressed(Input.Keys.W)) player.setDirY(1);
            if (Peripheal.keyPressed(Input.Keys.S)) player.setDirY(-1);
            if (Peripheal.keyPressed(Input.Keys.A)) player.setDirX(-1);
            if (Peripheal.keyPressed(Input.Keys.D)) player.setDirX(1);

            // Selector input logic
            if(Peripheal.mousePressed(Input.Buttons.RIGHT)) player.getSelector().checkPlace(terrain, entities);
            if(Peripheal.mousePressed(Input.Buttons.LEFT)) { player.getSelector().checkDestroy(deltaTime, terrain, entities); player.getSelector().checkHit(30, entities); }

            // Inventory input logic
            player.checkInventoryScrolling(Peripheal.mouseScroll());
            if(Peripheal.keyJustPressed(Input.Keys.I)) player.getInventoryRenderer().toggleRendering();

            // Checks for sprinting
            if(Peripheal.anyWASDPressed() && Peripheal.keyPressed(Input.Keys.SHIFT_LEFT)) player.sprint(); else player.setSpeed(Speed.NORMAL);

    }

}
