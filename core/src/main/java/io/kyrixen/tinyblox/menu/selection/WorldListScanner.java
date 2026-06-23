package io.kyrixen.tinyblox.menu.selection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class WorldListScanner {

    // Json parser
    private static final Json json = new Json();


    // Get the world.json (info of the world)
    public static List<WorldBlueprint> getWorlds() {

        List<WorldBlueprint> foundWorlds = new ArrayList<>();
        
        List<Path> worldPaths = new ArrayList<>();
        try {
            try(Stream<Path> stream = Files.list(Paths.get(WorldManager.worldsFolder.path()))) {
                worldPaths.addAll(stream.collect(Collectors.toList()));
            }

        } catch (IOException e) { Logger.LOGGER.error("SCANNER", "Failed to scan worlds: " + e); }


        for(Path worldPath : worldPaths) {
            if(!Files.isDirectory(worldPath)) continue;
            foundWorlds.add(readConvert(worldPath.getFileName().toString()));
        }
        
        if(foundWorlds.isEmpty()) Logger.LOGGER.info("SCANNER", "No worlds found");

        return foundWorlds;

    }

    // Read the file
    private static WorldBlueprint readConvert(String worldName) {

        String filePath = WorldManager.worldsFolder + "/" + worldName + "/world.json";

        String worldData = null;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            worldData = new String(bytes);
        } catch (IOException e) { Logger.LOGGER.error("SCANNER", "Couldnt find world info for " + worldName + ": " + e); }

        WorldBlueprint worldBlueprint = json.fromJson(WorldBlueprint.class, worldData);

        return worldBlueprint;

    }

    public static void deleteWorld(String worldName) {

        String filePath = WorldManager.worldsFolder + "/" + worldName;
    
        try {

            Files.walk(Paths.get(filePath)).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) { Logger.LOGGER.error("SCANNER", "Couldnt delete file " + path.getFileName().toString() + ": " + e); }
            });

        } catch (IOException e) { Logger.LOGGER.error("SCANNER", "Couldnt delete world " + worldName + ": " + e); }
    
    }

}
