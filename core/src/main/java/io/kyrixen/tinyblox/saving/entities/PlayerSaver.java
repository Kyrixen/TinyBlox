package io.kyrixen.tinyblox.saving.entities;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.saving.blueprints.entities.PlayerBlueprint;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class PlayerSaver {

    // Json parser
    private static final Json json = new Json();
    static {
        json.setOutputType(OutputType.json);
        json.setUsePrototypes(false);
    }


    // Helper for converting
    public static PlayerBlueprint convertToBlueprint(Player player) {

        PlayerBlueprint pb = new PlayerBlueprint();
        pb.formatVersion = Constants.SAVE_FORMAT_VERSION;
        
        pb.id = player.id();

        pb.x = player.x();
        pb.y = player.y();
        pb.level = player.level();

        pb.width = player.width();
        pb.height = player.height();

        pb.health = player.getHealth();
        pb.invincible = player.isInvincible();
        pb.autoRegenerate = player.getAutoRegenerate();

        pb.stamina = player.getStamina();
        pb.tireless = player.isTireless();
        pb.autoRecover = player.getAutoRecover();

        pb.inMenu = player.isInMenu();

        return pb;

    }

    // Save Player
    public static void save(Player player) {

        // Blueprint
        PlayerBlueprint pb = convertToBlueprint(player);

        // File to write
        String fileName = getPlayerFolder() + "/player.json";

        // Collected data
        String playerData = json.prettyPrint(pb);
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(playerData);
            fileWriter.close();            
        } catch (IOException e) { Logger.LOGGER.error("SAVER", "Cannot create player save file: " + e); }

    }

    
    // Get player folder path
    private static String getPlayerFolder() {
        return WorldManager.worldFolder.path();
    }

    
}
