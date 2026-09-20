package io.kyrixen.tinyblox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;

import io.kyrixen.tinyblox.entities.Entity.Speed;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.world.Terrain;

public class Controller {

    // Update controller for entity
    public void update(float deltaTime, Player player, int scroll,  Terrain terrain) {
    
            player.setDirX(0);
            player.setDirY(0);

            if(!player.isInMenu()) {

                // Checks for movement input
                if(Peripheral.keyPressed(Input.Keys.W)) player.setDirY(1);
                if(Peripheral.keyPressed(Input.Keys.S)) player.setDirY(-1);
                if(Peripheral.keyPressed(Input.Keys.A)) player.setDirX(-1);
                if(Peripheral.keyPressed(Input.Keys.D)) player.setDirX(1);

                // Climb movement input
                if(Peripheral.keyJustPressed(Input.Keys.SPACE)) player.tryClimbUp(terrain);
                if(Peripheral.keyJustPressed(Input.Keys.SHIFT_LEFT)) player.tryClimbDown(terrain);

                // Step movement input
                if(Peripheral.anyWASDPressed() && Peripheral.keyPressed(Input.Keys.SPACE)) player.tryStepUp(terrain);
                if(Peripheral.anyWASDPressed() && Peripheral.keyPressed(Input.Keys.SHIFT_LEFT)) player.tryStepDown(terrain);

                // Selector input logic
                if(Peripheral.mousePressed(Input.Buttons.RIGHT)) player.getSelector().checkPlace(terrain);
                else if(Peripheral.keyPressed(Input.Keys.U)) player.getSelector().checkPlace(terrain);
                if(Peripheral.mousePressed(Input.Buttons.LEFT)) { player.getSelector().checkDestroy(deltaTime, terrain); player.getSelector().checkHit(terrain); }
                else if(Peripheral.keyPressed(Input.Keys.H)) { player.getSelector().checkDestroy(deltaTime, terrain); player.getSelector().checkHit(terrain); }
                if(Peripheral.keyJustPressed(Input.Keys.I)) player.getSelector().move(0, 1);
                if(Peripheral.keyJustPressed(Input.Keys.K)) player.getSelector().move(0, -1);
                if(Peripheral.keyJustPressed(Input.Keys.J)) player.getSelector().move(-1, 0);
                if(Peripheral.keyJustPressed(Input.Keys.L)) player.getSelector().move(1, 0);
                
                // Inventory input logic
                player.checkInventoryScrolling(scroll);
                if(Peripheral.keyJustPressed(Input.Keys.O)) player.getInventory().previousSlot();
                if(Peripheral.keyJustPressed(Input.Keys.P)) player.getInventory().nextSlot();
                if(Peripheral.keyJustPressed(Input.Keys.E)) player.getInventoryRenderer().toggleRendering();
                if(Peripheral.keyJustPressed(Input.Keys.Q)) player.getSelector().dropItem(terrain);

                // Checks for sprinting
                if(Gdx.app.getType() == ApplicationType.WebGL) {
                    if(Peripheral.anyWASDPressed() && Peripheral.keyPressed(Input.Keys.SHIFT_LEFT)) player.sprint();
                    else player.setSpeed(Speed.NORMAL);
                } else {
                    if(Peripheral.anyWASDPressed() && Peripheral.keyPressed(Input.Keys.CONTROL_LEFT)) player.sprint();
                    else player.setSpeed(Speed.NORMAL);
                }

            }

            // Crafting controls
            if(Peripheral.keyJustPressed(Input.Keys.C)) { player.getCraftingManager().toggle(); player.toggleMenuStat(); }
            player.getCraftingManager().update(Peripheral.getMouseX(), Constants.WINDOW_HEIGHT - Peripheral.getMouseY(), Peripheral.mouseJustPressed(Input.Buttons.LEFT));
            player.getCraftingManager().updateScroll(scroll);
			if(Peripheral.keyJustPressed(Input.Keys.O)) player.getCraftingManager().previousRecipe();
			if(Peripheral.keyJustPressed(Input.Keys.P)) player.getCraftingManager().nextRecipe();
			
    }

}
