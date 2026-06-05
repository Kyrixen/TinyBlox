package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Input;

import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.Entity.Speed;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.world.Terrain;

public class Controller {

    // Update controller for entity
    public void update(float deltaTime, Player player, Terrain terrain, ArrayList<Entity> entities) {
    
            player.setDirX(0);
            player.setDirY(0);

            if(!player.isInMenu()) {

                // Checks for movement input
                if (Peripheral.keyPressed(Input.Keys.W)) player.setDirY(1);
                if (Peripheral.keyPressed(Input.Keys.S)) player.setDirY(-1);
                if (Peripheral.keyPressed(Input.Keys.A)) player.setDirX(-1);
                if (Peripheral.keyPressed(Input.Keys.D)) player.setDirX(1);

                // Climb movement input
                if(Peripheral.keyJustPressed(Input.Keys.SPACE)) player.tryClimbUp(terrain);
                if(Peripheral.keyJustPressed(Input.Keys.SHIFT_LEFT)) player.tryClimbDown(terrain);

                // Selector input logic
                if(Peripheral.mousePressed(Input.Buttons.RIGHT)) player.getSelector().checkPlace(terrain, entities);
                if(Peripheral.mousePressed(Input.Buttons.LEFT)) { player.getSelector().checkDestroy(deltaTime, terrain, entities); player.getSelector().checkHit(entities); }

                // Inventory input logic
                player.checkInventoryScrolling(Peripheral.mouseScroll());
                if(Peripheral.keyJustPressed(Input.Keys.I)) player.getInventoryRenderer().toggleRendering();
                if(Peripheral.keyJustPressed(Input.Keys.Q)) player.getSelector().dropItem(entities);

                // Checks for sprinting
                if(Peripheral.anyWASDPressed() && Peripheral.keyPressed(Input.Keys.CONTROL_LEFT)) player.sprint(); else player.setSpeed(Speed.NORMAL);

            }

            // Crafting controls
            if(Peripheral.keyJustPressed(Input.Keys.C)) { player.getCraftingManager().toggle(); player.toggleMenuStat(); }
            player.getCraftingManager().update(Peripheral.getMouseX(), Constants.WINDOW_HEIGHT - Peripheral.getMouseY(), Peripheral.mouseJustPressed(Input.Buttons.LEFT));
            player.getCraftingManager().updateScroll(Peripheral.mouseScroll());

    }

}
