package io.kyrixen.tinyblox.saving.world;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.saving.InventorySaver;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint.SavedEntity;
import io.kyrixen.tinyblox.saving.entities.EnemyLoader;
import io.kyrixen.tinyblox.saving.entities.EnemySaver;
import io.kyrixen.tinyblox.saving.entities.EntityLoader;
import io.kyrixen.tinyblox.saving.entities.EntitySaver;
import io.kyrixen.tinyblox.saving.entities.MobEntityLoader;
import io.kyrixen.tinyblox.saving.entities.MobEntitySaver;
import io.kyrixen.tinyblox.saving.entities.PlayerSaver;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;   


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
    public static void createWorld(String worldName, int seed, float frequency) {

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
            wb.worldSeed = seed;
            wb.worldFrequency = frequency;
            wb.lastEntityID = 0;
            String worldData = json.prettyPrint(wb);
            
            FileWriter fileWriter = new FileWriter(folder + "/world.json");
            fileWriter.write(worldData);
            fileWriter.close();

        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Cannot create world dir: " + e); }

    }

    // Loads world
    public static WorldBlueprint loadWorld(String worldName, int seed, float frequency) {

        WorldBlueprint wb = loadWorldInfo(worldName);
        if(wb == null) { createWorld(worldName, seed, frequency); wb = loadWorldInfo(worldName); }
        MiscUtils.initEntityID(wb.lastEntityID);

        return wb;

    }

    // Loads chunk entities
    public static List<Entity> loadEntities(Chunk chunk, SoundManager soundManager) {

        List<Entity> chunkEntities = new ArrayList<>();

        chunkEntities.addAll(EntityLoader.load(chunk));
        chunkEntities.addAll(MobEntityLoader.load(chunk, soundManager));
        chunkEntities.addAll(EnemyLoader.load(chunk, soundManager));

        if(!chunkEntities.isEmpty()) Logger.LOGGER.debug("LOADER", "Loading entities for chunk " + chunk.getX() + "," + chunk.getY() + ": " + chunkEntities.toString());

        return chunkEntities;

    }

    // Save chunk entities
    public static void saveEntities(Chunk chunk) {

        // Saved entities list
        List<SavedEntity> savedEntities = new ArrayList<>();

        // Merge all entities
        savedEntities.addAll(EntitySaver.save(chunk));
        savedEntities.addAll(MobEntitySaver.save(chunk));
        savedEntities.addAll(EnemySaver.save(chunk));
        
        for(Entity entity : chunk.getEntities()) {
            if(entity instanceof MobEntity) InventorySaver.save((MobEntity) entity);
        }

        // File to write
        String fileName = EntitySaver.getEntityFolder() + "/entities_" + chunk.getX() + "_" + chunk.getY() + ".json";
        try { Files.deleteIfExists(Paths.get(fileName)); } catch(IOException e) {}
        
        if(savedEntities.isEmpty()) return;
        EntitySaver.saveSavedEntities(savedEntities, fileName);

    }

    // Saves world
    public static void saveWorld(Terrain terrain, Player player) {

        for(short cx = 0; cx < terrain.getChunkCountX(); cx++){
            for(short cy = 0; cy < terrain.getChunkCountY(); cy++){

                Chunk c = terrain.getChunk(cx, cy);
                if(c == null) continue;

                if(c.isModified()) { ChunkSaver.save(c); c.setModified(false); }
                saveEntities(c);

            }
        }  

        PlayerSaver.save(player);
        InventorySaver.save(player);

        saveWorldInfo(Constants.WORLD_NAME);
        
    }


    // Loads world info
    public static WorldBlueprint loadWorldInfo(String worldName) {

        String folder = worldsFolder + "/" + worldName.toLowerCase().replace(" ", "_");
        worldFolder = Gdx.files.local(folder);

        FileHandle worldFile = worldFolder.child("world.json");
        if(!worldFile.exists()) return null;

        return json.fromJson(WorldBlueprint.class, worldFile);

    }

    // Saves world info
    public static void saveWorldInfo(String worldName) {
        
        try { 

            String folder = worldsFolder + "/" + worldName.toLowerCase().replace(" ", "_");

            WorldBlueprint wb = loadWorldInfo(worldName);
            if(wb == null) { Logger.LOGGER.error("SAVER", "Failed to save world info"); return; }
            wb.formatVersion = Constants.SAVE_FORMAT_VERSION;
            wb.lastEntityID = MiscUtils.getCurrentEntityID();
            String worldData = json.prettyPrint(wb);
            
            FileWriter fileWriter = new FileWriter(folder + "/world.json");
            fileWriter.write(worldData);
            fileWriter.close();

        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Failed to save world info: " + e); }  

    }

}
