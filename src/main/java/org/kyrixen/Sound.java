package org.kyrixen;


import javax.sound.sampled.*;
import java.io.IOException;


public class Sound {

    private Clip sound;


    public static Sound powerUp;
    public static Sound explosion;
    public static Sound hit;
    public static Sound pickUpCoin;
    public static Sound walk;


    public Sound(String path) {
    
        try {

            AudioInputStream audioSource = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));

            sound = AudioSystem.getClip();

            sound.open(audioSource);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    
    public void play() {
        
        if (sound != null) {
            sound.start();
        }
    
    }

    
    public void loop() {
    
        if (sound != null) {
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        }
    
    }

    
    public void stop() {
    
        if (sound != null)
            sound.stop();
    
    }

    public void close() {
    
        if (sound != null)
            sound.close();
    
    }

    public void setVolume(float volume) {
    
        if (sound != null) {
     
            FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(volume) * 20);
            gainControl.setValue(dB);
     
        }
    
    }

    public boolean isRunning() {
        return sound != null && sound.isRunning();
    }


    public void reset() {
        if (sound != null) {
            sound.setFramePosition(0);
        }
    }

    
    public static void initSounds() {

        powerUp = new Sound("/assets/sounds/powerUp.wav");
        explosion = new Sound("/assets/sounds/explosion.wav");
        hit = new Sound("/assets/sounds/hit.wav");
        pickUpCoin = new Sound("/assets/sounds/pickupCoin.wav");
        walk = new Sound("/assets/sounds/walk.wav");


        powerUp.stop();
        powerUp.reset();

        explosion.stop();
        explosion.reset();

        hit.stop();
        hit.reset();
        
        pickUpCoin.stop();
        pickUpCoin.reset();

        walk.stop();
        walk.reset();
        walk.setVolume(Helper.getFloatVolume(30)); // Set walk sound to 30% volume

    }

}
