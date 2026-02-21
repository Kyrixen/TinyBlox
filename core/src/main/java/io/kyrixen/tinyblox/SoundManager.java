package io.kyrixen.tinyblox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    
    public Sound walk;
    public Sound explosion;
    public Sound hitentity;
    public Sound hitplayer;
    public Sound pickupcoin;
    public Sound powerup;

    ArrayList<Sound> sounds = new ArrayList<>();

    public SoundManager(){

        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        hitentity = Gdx.audio.newSound(Gdx.files.internal("sounds/hitEntity.wav"));
        hitplayer = Gdx.audio.newSound(Gdx.files.internal("sounds/hitPlayer.wav"));
        pickupcoin = Gdx.audio.newSound(Gdx.files.internal("sounds/pickupCoin.wav"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("sounds/powerUp.wav"));

        sounds.add(walk);
        sounds.add(explosion);
        sounds.add(hitentity);
        sounds.add(hitplayer);
        sounds.add(pickupcoin);
        sounds.add(powerup);

    }

    public void cleanup(){

        for(Sound sound : sounds){ sound.dispose(); }

        sounds.clear();

    }

}
