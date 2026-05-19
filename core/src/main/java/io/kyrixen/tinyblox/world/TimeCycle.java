package io.kyrixen.tinyblox.world;

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

    public void updateDayTime(float deltaTime) {
     
        time += deltaTime / Constants.FULL_TIME_CYCLE;        
        time %= 1f;

        if(time < 0.25f) dayTime = DayTime.DAWN;
        else if(time < 0.5f) dayTime = DayTime.DAY;
        else if(time < 0.75f) dayTime = DayTime.SUNSET;
        else dayTime = DayTime.NIGHT;

        brightness = 0.2f + 1.05f * (0.5f + 0.5f * MathUtils.sin(time * MathUtils.PI2));
    
    }

    // Getters //

    public DayTime getDayTime() { return this.dayTime; }
    public float getTime() { return this.time; }

    public float getBrightness() {return this.brightness; }

    // Setters //

    public void setDayTime(DayTime dayTime) {
        this.dayTime = dayTime;
        this.setTime(dayTime.getTime());
    }

    public void setTime(float time) {
        this.time = time;
    }

}
