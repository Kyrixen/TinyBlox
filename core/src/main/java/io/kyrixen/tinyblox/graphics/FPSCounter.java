package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.Gdx;

import io.kyrixen.tinyblox.Constants;

public class FPSCounter {

    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

    public void printFPS(RendererStack rendererStack) {

        if (!Constants.SHOW_FPS) return;
        rendererStack.font.getData().setScale(1f);
        rendererStack.font.draw(rendererStack.batch, "FPS: " + getFPS(), 25, Gdx.graphics.getHeight() - 25);
    
    }

}