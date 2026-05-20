package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;

public class Renderer {

    // Texture for missing texture
    private static final TextureID MISSING_TEXTURE = new TextureID("tinyblox", TextureType.MISC, "missing_texture");

    public void clear() {
        ScreenUtils.clear(Color.DARK_GRAY);
    }

    // Draws missing texture (something like null block in minecraft)
    public static void drawMissingTexture(float x, float y, float w, float h, TextureManager tex, SpriteBatch batch) {
        batch.draw(tex.getTexture(MISSING_TEXTURE), x, y, w, h);
    }

    // FPS Limiter method (empty because handled via libGDX itself)
    public void limitFPS() {}

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
