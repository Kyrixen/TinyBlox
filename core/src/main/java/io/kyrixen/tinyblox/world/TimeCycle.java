package io.kyrixen.tinyblox.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;

public class TimeCycle {
      
    public static enum DayTime {

        DAWN(0f),
        DAY(0.25f),
        SUNSET(0.50f),
        NIGHT(0.75f);

        private final float day_time;

        DayTime(float day_time) {
            this.day_time = day_time;
        }

        public float getTime() { return this.day_time; }

    }

    private float time;

    private DayTime dayTime;
    
    private float brightness = 0.25f;
    private float tintR = 1f;
    private float tintG = 1f;
    private float tintB = 1f;

    public void updateDayTime(float deltaTime) {
     
        time += deltaTime / Constants.FULL_TIME_CYCLE;        
        time %= 1f;


        if(time < 0.25f) {

            dayTime = DayTime.DAWN;
          
            tintR = 0.75f;
            tintG = 0.8f;
            tintB = 0.95f;

        } else if(time < 0.5f) {
          
            dayTime = DayTime.DAY;
          
            tintR = 1f;
            tintG = 1f;
            tintB = 1f;

        } else if(time < 0.75f) {
          
            dayTime = DayTime.SUNSET;
          
            tintR = 1f;
            tintG = 0.7f;
            tintB = 0.55f;

        } else {

            dayTime = DayTime.NIGHT;
            
            tintR = 0.4f;
            tintG = 0.45f;
            tintB = 0.65f;

        }

        brightness = 0.2f + 1.05f * (0.5f + 0.5f * MathUtils.sin(time * MathUtils.PI2));
    
    }

    // Getters //

    public DayTime getDayTime() { return this.dayTime; }
    public float getTime() { return this.time; }

    public float getBrightness() { return this.brightness; }
    public Color getBrightnessColor() { return new Color(tintR * brightness, tintG * brightness, tintB * brightness, 1f); }

    // Setters //

    public void setDayTime(DayTime dayTime) {
        this.dayTime = dayTime;
        this.setTime(dayTime.getTime());
    }

    public void setTime(float time) {
        this.time = time;
    }

}
