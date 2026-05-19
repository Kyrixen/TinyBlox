package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;

public class UIRenderer {

    // Texture Manager var
    private TextureManager tex;

    // Background textures
    private static final TextureID menuBackgroundImage = new TextureID("tinyblox", TextureType.BACKGROUND, "menu_background");
    private static final TextureID settingsBackgroundImage = new TextureID("tinyblox", TextureType.BACKGROUND, "settings_background");

    public UIRenderer(TextureManager tex) {
        this.tex = tex;
    }

    // Show Menu background
    public void showMenuBackground(SpriteBatch batch){

        batch.begin();
        batch.draw(tex.getTexture(menuBackgroundImage), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

        // Show Settings background
    public void showSettingsBackground(SpriteBatch batch){

        batch.begin();
        batch.draw(tex.getTexture(settingsBackgroundImage), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        batch.end();

    }

}
