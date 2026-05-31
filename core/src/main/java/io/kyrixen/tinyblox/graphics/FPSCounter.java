package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;

public class FPSCounter {

    private BitmapFont fpsFont;

    public FPSCounter() {
        fpsFont = new BitmapFont(Gdx.files.internal("fonts/tinyblox_font.fnt"));
        fpsFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

    public void printFPS(SpriteBatch batch) {

        if (!Constants.SHOW_FPS) return;
        fpsFont.draw(batch, "FPS: " + getFPS(), 25, Gdx.graphics.getHeight() - 25);
    
    }

    public void cleanup() {
        fpsFont.dispose();
    }

}