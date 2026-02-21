package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FPSCounter {

    private final BitmapFont fpsFont;

    public FPSCounter() {
        fpsFont = new BitmapFont(); // Default font
        fpsFont.getData().setScale(2f); // Larger text
    }

    public int getFPS() { return Gdx.graphics.getFramesPerSecond(); }

    public void printFPS(SpriteBatch batch) {
        fpsFont.draw(batch, "FPS: " + getFPS(), 25, Gdx.graphics.getHeight() - 25);
    }

    public void cleanup() {
        fpsFont.dispose();
    }
    
}
