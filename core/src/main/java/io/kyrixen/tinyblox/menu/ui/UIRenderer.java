package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.graphics.RendererStack;

public class UIRenderer {

    // Texture Manager var
    private final TextureManager tex;

    // Background textures
    private static final TinyIdentifier menuBackgroundImage = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "menu_background");
    private static final TinyIdentifier settingsBackgroundImage = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "settings_background");
    private static final TinyIdentifier selectionBackgroundImage = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "selection_background");


    public UIRenderer(TextureManager tex) {
        this.tex = tex;
    }

    // Show Menu background
    public void showMenuBackground(RendererStack rendererStack){

		SpriteBatch batch = rendererStack.batch;

        batch.begin();
        batch.draw(tex.getTexture(menuBackgroundImage), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

    // Show Settings background
    public void showSettingsBackground(RendererStack rendererStack){

		SpriteBatch batch = rendererStack.batch;

        batch.begin();
        batch.draw(tex.getTexture(settingsBackgroundImage), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

    // Show Settings background
    public void showSelectionBackground(RendererStack rendererStack){

		SpriteBatch batch = rendererStack.batch;
        ShapeRenderer shape = rendererStack.shape;

        batch.begin();
        batch.draw(tex.getTexture(selectionBackgroundImage), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeType.Filled);
        
        shape.setColor(0f, 0f, 0f, 0.5f);
        shape.rect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

}
