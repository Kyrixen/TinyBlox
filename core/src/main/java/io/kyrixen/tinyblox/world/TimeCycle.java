package io.kyrixen.tinyblox.world;

import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;

public class TimeCycle {
      
    public static enum DayTime {

        SUNSET,
        DAY,
        DAWN,
        NIGHT

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

    public DayTime getDayTime() { return this.dayTime; }

    public float getBrightness() {return this.brightness; }

}
