package io.kyrixen.tinyblox.sound;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sfx {
    
    public Sound walk;
    public Sound explosion;
    public Sound hitentity;
    public Sound hitplayer;
    public Sound pickupcoin;
    public Sound powerup;

    ArrayList<Sound> sfxs = new ArrayList<>();

    public Sfx(){

        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/walk.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/explosion.wav"));
        hitentity = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/hitEntity.wav"));
        hitplayer = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/hitPlayer.wav"));
        pickupcoin = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/pickupCoin.wav"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/powerUp.wav"));

        sfxs.add(walk);
        sfxs.add(explosion);
        sfxs.add(hitentity);
        sfxs.add(hitplayer);
        sfxs.add(pickupcoin);
        sfxs.add(powerup);

    }

    public void cleanup(){

        for(Sound sfx : sfxs){ sfx.dispose(); }

        sfxs.clear();

    }

}
