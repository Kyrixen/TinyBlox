package io.kyrixen.tinyblox.saving.entities;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.saving.InventoryLoader;
import io.kyrixen.tinyblox.saving.blueprints.entities.PlayerBlueprint;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.FileManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.Camera;

public class PlayerLoader {
    
    // Json parser
    private static final Json json = new Json();  
    

    // Converts to Player
    public static Player convertToPlayer(PlayerBlueprint pb, Camera camera, SoundManager soundManager) {

        if(pb.formatVersion != Constants.SAVE_FORMAT_VERSION) throw new RuntimeException("Invalid save format: " + pb.formatVersion);

        Player player = new Player(pb.id, pb.x, pb.y,  camera, soundManager);
        player.setLevel(pb.level);
        
        player.setHealth(pb.health);
        player.setInvincible(pb.invincible);
        player.setAutoRegenerate(pb.autoRegenerate);

        player.setStamina(pb.stamina);
        player.setTireless(pb.tireless);
        player.setAutoRecover(pb.autoRecover);

        //if(player.isInMenu() != pb.inMenu) player.toggleMenuStat();
        
        InventoryLoader.load(player);

        return player;

    }    
    
    // Load Player
    public static Player load(Camera camera, SoundManager soundManager) {

        String fileName = getPlayerFolder() + "/player.json";
        Player player = null;

        String playerData = FileManager.readFile(fileName);
        if(playerData == null) return player;

        PlayerBlueprint pb = json.fromJson(PlayerBlueprint.class, playerData);
        if(pb.formatVersion != Constants.SAVE_FORMAT_VERSION) {
            Logger.LOGGER.error("LOADER", "Invalid format version for player save: " + pb.formatVersion);
            return player;
        }
        Logger.LOGGER.debug("LOADER", "Loaded player save");

        return convertToPlayer(pb, camera, soundManager);

    }

    
    // Get player folder path
    private static String getPlayerFolder() {
        return WorldManager.worldFolder.path();
    }

}
