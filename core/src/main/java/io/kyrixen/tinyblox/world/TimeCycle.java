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

        // Dawn
        if(time < 0.25f) {

            dayTime = DayTime.DAWN;
            float progress = time / 0.25f;

            tintR = MathUtils.lerp(0.75f, 1f, progress);
            tintG = MathUtils.lerp(0.8f, 1f, progress);
            tintB = MathUtils.lerp(0.95f, 1f, progress);

        }

        // Day
        else if(time < 0.5f) {

            dayTime = DayTime.DAY;
            float progress = (time - 0.25f) / 0.25f;

            tintR = MathUtils.lerp(1f, 1f, progress);
            tintG = MathUtils.lerp(1f, 0.7f, progress);
            tintB = MathUtils.lerp(1f, 0.55f, progress);

        }

        // Sunset
        else if(time < 0.75f) {

            dayTime = DayTime.SUNSET;
            float progress = (time - 0.5f) / 0.25f;

            tintR = MathUtils.lerp(1f, 0.4f, progress);
            tintG = MathUtils.lerp(0.7f, 0.45f, progress);
            tintB = MathUtils.lerp(0.55f, 0.65f, progress);

        }

        // Night
        else {

            dayTime = DayTime.NIGHT;
            float progress = (time - 0.75f) / 0.25f;

            tintR = MathUtils.lerp(0.4f, 0.75f, progress);
            tintG = MathUtils.lerp(0.45f, 0.8f, progress);
            tintB = MathUtils.lerp(0.65f, 0.95f, progress);

        }

        brightness = 0.35f + 0.8f * (0.5f + 0.5f * MathUtils.sin((time - 0.25f) * MathUtils.PI2));
        brightness = MathUtils.clamp(brightness, 0f, 1.25f);

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
