package io.kyrixen.tinyblox.sound;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sfx {
    
    public Sound walk;
    public Sound explosion;
    public Sound hitentity;
    public Sound hitplayer;
    public Sound pickupitem;
    public Sound powerup;
    public Sound destroy;
    public Sound place;

    ArrayList<Sound> sfxs = new ArrayList<>();

    public Sfx(){

        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/walk.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/explosion.wav"));
        hitentity = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/hitEntity.wav"));
        hitplayer = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/hitPlayer.wav"));
        pickupitem = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/pickupItem.wav"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/powerUp.wav"));
        destroy = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/destroy.wav"));
        place = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/place.wav"));

        sfxs.add(walk);
        sfxs.add(explosion);
        sfxs.add(hitentity);
        sfxs.add(hitplayer);
        sfxs.add(pickupitem);
        sfxs.add(powerup);
        sfxs.add(destroy);
        sfxs.add(place);

    }

    public void cleanup(){

        for(Sound sfx : sfxs){ sfx.dispose(); }

        sfxs.clear();

    }

}
