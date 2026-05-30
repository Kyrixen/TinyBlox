package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import io.kyrixen.tinyblox.Constants;

public class FPSCounter {

    protected BitmapFont fpsFont;
    protected final GlyphLayout layout = new GlyphLayout();

    public FPSCounter() {
        generateFont("fonts/editundo.ttf", 40);
    }

    public int getFPS() { return Gdx.graphics.getFramesPerSecond(); }

    public void printFPS(SpriteBatch batch) {

        if(!Constants.SHOW_FPS) return;

        fpsFont.draw(batch, "FPS: " + getFPS(), 25, Gdx.graphics.getHeight() - 25);
    }

    public void cleanup() {
        fpsFont.dispose();
    }

    private void generateFont(String path, int size) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;

        fpsFont = generator.generateFont(parameter);

        generator.dispose();

    }

}
