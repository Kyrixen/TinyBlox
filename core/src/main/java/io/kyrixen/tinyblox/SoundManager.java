package org.kyrixen;


import io.kyrixen.audio.Sound;
import java.io.File;
import java.util.ArrayList;


public class SoundManager {
    
    Sound walk;
    Sound explosion;
    Sound hitentity;
    Sound hitplayer;
    Sound pickupcoin;
    Sound powerup;

    ArrayList<Sound> sounds = new ArrayList<>();

    public SoundManager(){

        walk = new Sound(new File("assets/sounds/walk.wav"));
        explosion = new Sound(new File("assets/sounds/explosion.wav"));
        hitentity = new Sound(new File("assets/sounds/hitEntity.wav"));
        hitplayer = new Sound(new File("assets/sounds/hitPlayer.wav"));
        pickupcoin = new Sound(new File("assets/sounds/pickupCoin.wav"));
        powerup = new Sound(new File("assets/sounds/powerUp.wav"));

        sounds.add(walk);
        sounds.add(explosion);
        sounds.add(hitentity);
        sounds.add(hitplayer);
        sounds.add(pickupcoin);
        sounds.add(powerup);

        walk.setVolume(Utils.getFloatVolume(20));

    }

    public void cleanup(){

        for(Sound sound : sounds){
            sound.close();
        }

        Sound.cleanup();

    }

}
