package io.kyrixen.tinyblox.menu.selection;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class WorldListScanner {

    // Json parser
    private static final Json json = new Json();


    // Get the world.json (info of the world)
    public static List<WorldBlueprint> getWorlds() {

        List<WorldBlueprint> foundWorlds = new ArrayList<>();
        
        List<String> worldPaths = Platform.fileManager.listDir(WorldManager.worldsFolder.path());

        for(String worldPath : worldPaths) {

            if(!Platform.fileManager.isDir(worldPath)) continue;
            
            WorldBlueprint worldBlueprint = readConvert(Platform.fileManager.getEndpoint(worldPath));
            if(worldBlueprint == null) continue;
            
            foundWorlds.add(worldBlueprint);
        
        }
        
        if(foundWorlds.isEmpty()) Logger.LOGGER.info("SCANNER", "No worlds found");

        return foundWorlds;

    }

    // Read the file
    private static WorldBlueprint readConvert(String worldName) {

        String filePath = WorldManager.worldsFolder + "/" + worldName + "/world.json";

        String worldData = Platform.fileManager.readFile(filePath);
        if(worldData == null) return null;
        
        Logger.LOGGER.info("SCANNER", "Reading: " + filePath);
        WorldBlueprint worldBlueprint = json.fromJson(WorldBlueprint.class, worldData);


        return worldBlueprint;

    }

    public static void deleteWorld(String worldName) {

        String filePath = WorldManager.worldsFolder + "/" + worldName;
        Platform.fileManager.deleteDir(filePath);
    
    }

}
