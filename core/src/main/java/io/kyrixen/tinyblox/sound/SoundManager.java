package io.kyrixen.tinyblox.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.kyrixen.tinyblox.sound.SoundID.SoundType;
import io.kyrixen.tinyblox.utils.Logger;

public class SoundManager {

    // List of loaded sounds
    private final Map<SoundID, Sound> loadedSounds = new HashMap<>();

    // Sound for missing sound
    private static final SoundID MISSING_SOUND = new SoundID("tinyblox", SoundType.MISC, "missing_sound");

    // Load missing sound sound
    public SoundManager() {
        this.load(MISSING_SOUND, "sounds/misc/missing_sound.wav");
    }

    // Load sound
    public void load(SoundID identifier, String path) {
    
        Logger.LOGGER.debug("SOUND", "Loading: " + path);
    
        try {

            Sound asset = Gdx.audio.newSound(Gdx.files.internal(path));

            if(loadedSounds.containsKey(identifier)) { Logger.LOGGER.error("SOUND", "ID already registered!: " + identifier.toString()); return; }

            loadedSounds.put(identifier, asset);

        } catch(GdxRuntimeException e) {
            Logger.LOGGER.error("SOUND", "File not found: " + path);
        }

    }

    // Get sound
    public Sound getSound(SoundID identifier) {
    
        Sound asset = loadedSounds.get(identifier);

        if(asset == null) { Logger.LOGGER.error("SOUND", "Sound not loaded: " + identifier.toString());  return loadedSounds.get(MISSING_SOUND); }

        return asset;
    
    }

    // Load functions //

    // Load UI sounds
    public void loadUI() {

        this.load(new SoundID("tinyblox", SoundType.UI, "hollow"), "sounds/ui/hollow.wav");
        this.load(new SoundID("tinyblox", SoundType.UI, "slider"), "sounds/ui/slider.wav");
        this.load(new SoundID("tinyblox", SoundType.UI, "click"), "sounds/ui/click.wav");
        this.load(new SoundID("tinyblox", SoundType.UI, "options"), "sounds/ui/options.wav");
    
    }

    // Load HUD sounds
    public void loadHUD() {

        this.load(new SoundID("tinyblox", SoundType.HUD, "walk"), "sounds/hud/walk.wav");
        this.load(new SoundID("tinyblox", SoundType.HUD, "place"), "sounds/hud/place.wav");
        this.load(new SoundID("tinyblox", SoundType.HUD, "destroy"), "sounds/hud/destroy.wav");

    }

    // Load SFX sounds
    public void loadSFX() {

        this.load(new SoundID("tinyblox", SoundType.SFX, "explosion"), "sounds/sfx/explosion.wav");
        this.load(new SoundID("tinyblox", SoundType.SFX, "bomber_detonate"), "sounds/sfx/bomberDetonate.wav");
        this.load(new SoundID("tinyblox", SoundType.SFX, "hit_player"), "sounds/sfx/hitPlayer.wav");
        this.load(new SoundID("tinyblox", SoundType.SFX, "hit_enemy"), "sounds/sfx/hitEnemy.wav");
        this.load(new SoundID("tinyblox", SoundType.SFX, "pickup_item"), "sounds/sfx/pickupItem.wav");
        this.load(new SoundID("tinyblox", SoundType.SFX, "powerup"), "sounds/sfx/powerup.wav");

    }


    // Cleanup resources
    public void cleanup() {

        for(Sound sound : loadedSounds.values()) { sound.dispose(); }

        // Clear loaded sounds list
        loadedSounds.clear();

    }
 
}
