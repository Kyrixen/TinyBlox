package io.kyrixen.tinyblox.saving;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.blueprints.InventoryBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.InventoryBlueprint.InventoryStack;
import io.kyrixen.tinyblox.saving.world.WorldManager;

public class InventorySaver {

    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
    public static InventoryBlueprint convertToBlueprint(Inventory inventory) {

        InventoryBlueprint bi = new InventoryBlueprint();
        bi.formatVersion = Constants.SAVE_FORMAT_VERSION;

        bi.stacks = new InventoryStack[inventory.getMaxStorage()];
        for(byte i = 0; i < inventory.getMaxStorage(); i++) {

            ItemStack itemStack = inventory.getSlot(i);
            
            InventoryStack inventoryStack = new InventoryStack();
            inventoryStack.item = itemStack.getItem().getItemName();
            inventoryStack.amount = itemStack.getCount();

            bi.stacks[i] = inventoryStack;

        }

        bi.hotbarSlot = inventory.getCurrentSlot();

        return bi;

    }

    // Save inventory
    public static void save(MobEntity mobEntity) {
        
        if(mobEntity.getInventory().isEmpty()) return;

        InventoryBlueprint inventoryBlueprint = convertToBlueprint(mobEntity.getInventory());

        // File to write
        String fileName = getInventoryFolder() + "/inventory_" + mobEntity.id() + ".json";

        // Collected data
        String inventoryData = json.prettyPrint(inventoryBlueprint);
        Platform.fileManager.writeFile(fileName, inventoryData);

    }


    // Get inventory folder path
    private static String getInventoryFolder() {
        return WorldManager.worldFolder.path() + "/inventories";
    }

}
