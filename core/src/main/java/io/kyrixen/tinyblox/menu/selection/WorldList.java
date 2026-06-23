package io.kyrixen.tinyblox.menu.selection;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.menu.selection.uiselectionaddon.WorldSlot;
import io.kyrixen.tinyblox.menu.selection.uiselectionaddon.WorldSlot.WorldSlotState;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;

public class WorldList {

    private WorldSlot[] worldSlots = new WorldSlot[4];

    private int selectedSlot = 0;


    public WorldList(Texture worldSlotTexture) {

        for(int i = 0; i < worldSlots.length; i++) {

            int x = 230;
            int y = 480 - i * 110;

            worldSlots[i] = new WorldSlot(x, y, 340, 100, worldSlotTexture);

        }

    }


    public void update(int mouseX, int mouseY, boolean pressed) {

        for(int i = 0; i < worldSlots.length; i++) {
        
            WorldSlot worldSlot = worldSlots[i];
            if(worldSlot.getWorld() == null) continue;
        
            boolean hover = worldSlot.contains(mouseX, mouseY);

            if(hover && pressed) {
                selectedSlot = i;
                Constants.CURRENT_WORLD = worldSlots[selectedSlot].getWorld().worldName;
            }

            if(i == selectedSlot) worldSlot.setState(WorldSlotState.SELECTED);
            else if(hover) worldSlot.setState(WorldSlotState.HOVER);
            else worldSlot.setState(WorldSlotState.IDLE);
        
        }

    }


    public void updateWorldSlots(List<WorldBlueprint> worlds) {

        for(WorldSlot slot : worldSlots) { slot.setWorld(null); }

        for(int i = 0; i < worlds.size(); i++) {
            if(i >= worldSlots.length) return;
            worldSlots[i].setWorld(worlds.get(i));
            worldSlots[i].setCompatibility(worlds.get(i).formatVersion == Constants.SAVE_FORMAT_VERSION);
        }
    }

    public void render(RendererStack rendererStack) {
        for(WorldSlot worldSlot : worldSlots) {
            worldSlot.render(rendererStack);
        }
    }

    public void deleteWorld() {

        System.out.println("Deleting world " + worldSlots[selectedSlot].getWorld().worldName);
    
        WorldListScanner.deleteWorld(worldSlots[selectedSlot].getWorld().worldName.toLowerCase().replace(" ", "_"));
        updateWorldSlots(WorldListScanner.getWorlds());
    
    }

    public boolean canLoad() {
        return this.worldSlots[selectedSlot].isCompatible() && this.worldSlots[selectedSlot].getWorld() != null;
    }

    public WorldBlueprint getWorld() {
        return this.worldSlots[selectedSlot].getWorld();
    }

}
