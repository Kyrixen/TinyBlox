package io.kyrixen.tinyblox.saving.world;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.saving.InventorySaver;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.saving.entities.EnemyLoader;
import io.kyrixen.tinyblox.saving.entities.EnemySaver;
import io.kyrixen.tinyblox.saving.entities.EntityLoader;
import io.kyrixen.tinyblox.saving.entities.EntitySaver;
import io.kyrixen.tinyblox.saving.entities.MobEntityLoader;
import io.kyrixen.tinyblox.saving.entities.MobEntitySaver;
import io.kyrixen.tinyblox.saving.entities.PlayerLoader;
import io.kyrixen.tinyblox.saving.entities.PlayerSaver;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.MiscUtils;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;


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

        return wb;

    }

    // Loads world entities
    public static Player loadEntities(Terrain terrain, ArrayList<Entity> entities, Camera camera, SoundManager soundManager) {

        // Spawn cords
        int[] spawn = MiscUtils.spawnNearCenter(terrain);

        // Create player
        Player freshPlayer = new Player(spawn[0], spawn[1], camera, soundManager);
        freshPlayer.setLevel((byte) spawn[2]);


        Map<Integer, Entity> loadedEntities = new HashMap<>();

        List<Path> files = null;
        try {
            files = Files.walk(Paths.get(EntitySaver.getEntityFolder())).filter(path -> path.toString().endsWith(".json")).collect(Collectors.toList());
        } catch (IOException e) { Logger.LOGGER.error("LOADER", "Couldnt list entities files: " + e); }
        if(files == null) return freshPlayer;

        List<ChunkPos> chunksPos = new ArrayList<>();
        for(Path filePath : files) {
            String[] cords = filePath.getFileName().toString().replace("entities_", "").replace(".json", "").split("_");
            chunksPos.add(new ChunkPos(Short.parseShort(cords[0]), Short.parseShort(cords[1])));
        }

        for(ChunkPos pos : chunksPos) {
            for(Entity e : EntityLoader.load(pos)) { loadedEntities.put(e.id(), e); }
            for(MobEntity e : MobEntityLoader.load(pos, soundManager)) { loadedEntities.put(e.id(), e); }
            for(Enemy e : EnemyLoader.load(pos, soundManager)) { loadedEntities.put(e.id(), e); }
        }


        Player player = PlayerLoader.load(camera, soundManager);
        if(player == null || player.isDead()) player = freshPlayer;

        loadedEntities.put(player.id(), player);
        entities.addAll(loadedEntities.values());
        
        return player;

    }

    // Saves world
    public static void saveWorld(Terrain terrain, ArrayList<Entity> entities) {

        for(short cx = 0; cx < terrain.getChunkCountX(); cx++){
            for(short cy = 0; cy < terrain.getChunkCountY(); cy++){

                Chunk c = terrain.getChunk(cx, cy);
                if(c == null) continue;

                if(c.isModified()) { ChunkSaver.save(c); c.setModified(false); }

            }
        }  

        for(Entity entity : entities) {

            if(entity instanceof Player) PlayerSaver.save((Player) entity);
            else if(entity instanceof Enemy) EnemySaver.save((Enemy) entity);
            else if(entity instanceof MobEntity) MobEntitySaver.save((MobEntity) entity);
            else EntitySaver.save(entity);

            if(!(entity instanceof MobEntity)) continue;
            InventorySaver.save((MobEntity) entity);

        }
        
        
    }


    // Loads world info
    public static WorldBlueprint loadWorldInfo(String worldName) {

        String folder = worldsFolder + "/" + worldName.toLowerCase().replace(" ", "_");
        worldFolder = Gdx.files.local(folder);

        FileHandle worldFile = worldFolder.child("world.json");
        if(!worldFile.exists()) return null;

        return json.fromJson(WorldBlueprint.class, worldFile);

    }

}
