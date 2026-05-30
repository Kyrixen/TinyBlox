package io.kyrixen.tinyblox.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;

public class RendererUtils {

    public static void clear() {
        ScreenUtils.clear(Color.DARK_GRAY);
    }

    // FPS Limiter method (empty because handled via libGDX itself)
    public static void limitFPS() {}

    // Update methods //

    public static void updateVsync(boolean vsync) {
        Constants.VSYNC = vsync;
        Gdx.graphics.setVSync(Constants.VSYNC);
    }
    
    public static void updateFPS(int fps) {
        Constants.FPS = fps;
        Gdx.graphics.setForegroundFPS(Constants.FPS);
    }

}
