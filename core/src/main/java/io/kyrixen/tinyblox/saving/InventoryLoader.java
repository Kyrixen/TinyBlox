package io.kyrixen.tinyblox.saving;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.inventory.Inventory;
import io.kyrixen.tinyblox.entities.inventory.ItemRegister;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.blueprints.InventoryBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.InventoryBlueprint.InventoryStack;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class InventoryLoader {
    
    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Convertor helper
    public static void convertToInventory(InventoryBlueprint ib, Inventory inventory) {

        if(ib.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + ib.formatVersion);
        inventory.clear();

        if(ib.stacks == null) return;
        for(InventoryStack inventoryStack : ib.stacks) {
            if(ItemRegister.getItemByName(inventoryStack.item) == null) continue;
            inventory.set(ItemRegister.getItemByName(inventoryStack.item), (byte) inventoryStack.amount);
        }

        inventory.setCurrentSlot(ib.hotbarSlot);

    }

    // Load inventory
    public static void load(MobEntity mobEntity) {

        // File to write
        String fileName = getInventoryFolder() + "/inventory_" + mobEntity.id() + ".json";
        String inventoryData = Platform.fileManager.readFile(fileName);
        if(inventoryData == null) return;

        InventoryBlueprint inventoryBlueprint = json.fromJson(InventoryBlueprint.class, inventoryData);
        if(inventoryBlueprint.formatVersion != Constants.SAVE_FORMAT_VERSION) Logger.LOGGER.error("LOADER", "Invalid format version to load: "  + inventoryBlueprint.formatVersion);
        
        convertToInventory(inventoryBlueprint, mobEntity.getInventory());

    }

    
    // Get inventory folder path
    private static String getInventoryFolder() {
        return WorldManager.worldFolder.path() + "/inventories";
    }

}
