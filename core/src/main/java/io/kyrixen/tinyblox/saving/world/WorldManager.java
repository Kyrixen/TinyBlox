package io.kyrixen.tinyblox.saving.world;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.Terrain;


public class WorldManager {

    // Json parser
    private static final Json json = new Json(OutputType.json);
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }

    // Folders
    private static final FileHandle worldsFolder = Gdx.files.local("worlds");
    public static FileHandle worldFolder;


    // Saves world
    public static void createWorld(Terrain terrain, String worldName) {

        String folder = worldsFolder + "/" + worldName.toLowerCase().replace(" ", "_");
        worldFolder = Gdx.files.local(folder);
        try { 

            Files.createDirectories(Paths.get(folder));
            Files.createDirectories(Paths.get(folder + "/chunks"));
            Files.createDirectories(Paths.get(folder + "/entities"));
            Files.createDirectories(Paths.get(folder + "/inventories"));
            
            WorldBlueprint wb = new WorldBlueprint();
            wb.formatVersion = Constants.SAVE_FORMAT_VERSION;
            wb.worldName = worldName;
            wb.worldSeed = terrain.getSeed();
            wb.worldFrequency = terrain.getFrequency();
            String worldData = json.prettyPrint(wb);
            
            FileWriter fileWriter = new FileWriter(folder + "/world.json");
            fileWriter.write(worldData);
            fileWriter.close();

        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Cannot create world dir: " + e); }

    }

    // Loads world
    public static WorldBlueprint loadWorld(String worldName) {

        String folder = worldsFolder + "/" + worldName.toLowerCase().replace(" ", "_");
        worldFolder = Gdx.files.local(folder);

        FileHandle worldFile = worldFolder.child("world.json");
        if(!worldFile.exists()) return null;

        return json.fromJson(WorldBlueprint.class, worldFile);

    }

}
