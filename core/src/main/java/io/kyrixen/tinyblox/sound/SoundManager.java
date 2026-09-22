package io.kyrixen.tinyblox.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class SoundManager {

    // List of loaded sounds
    private final Map<TinyIdentifier, Sound> loadedSounds = new HashMap<>();

    // Sound for missing sound
    private static final TinyIdentifier MISSING_SOUND = new TinyIdentifier("tinyblox", IdentifierType.SOUND, "missing_sound");

    // Load missing sound sound
    public SoundManager() {
        this.load(MISSING_SOUND, "misc/missing_sound.ogg");
    }

    // Load sound
    public void load(TinyIdentifier identifier, String file) {
    
        String path = identifier.getNamespace() + "/sounds/" + file;

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
    public Sound getSound(TinyIdentifier identifier) {
    
        Sound asset = loadedSounds.get(identifier);

        if(asset == null) { Logger.LOGGER.error("SOUND", "Sound not loaded: " + identifier.toString());  return loadedSounds.get(MISSING_SOUND); }

        return asset;
    
    }

    // Load functions //

    // Load UI sounds
    public void loadUI() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "hollow"), "ui/hollow.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "slider"), "ui/slider.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "click"), "ui/click.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "options"), "ui/options.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "key_type"), "ui/typeKey.ogg");
    
    }

    // Load HUD sounds
    public void loadHUD() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "walk"), "hud/walk.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "place"), "hud/place.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "destroy"), "hud/destroy.ogg");

    }

    // Load SFX sounds
    public void loadSFX() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "explosion"), "sfx/explosion.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "bomber_detonate"), "sfx/bomberDetonate.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "hit_player"), "sfx/hitPlayer.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "hit_enemy"), "sfx/hitEnemy.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "pickup_item"), "sfx/pickupItem.ogg");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.SOUND, "powerup"), "sfx/powerup.ogg");

    }


    // Cleanup resources
    public void cleanup() {

        for(Sound sound : loadedSounds.values()) { sound.dispose(); }

        // Clear loaded sounds list
        loadedSounds.clear();

    }
 
}
