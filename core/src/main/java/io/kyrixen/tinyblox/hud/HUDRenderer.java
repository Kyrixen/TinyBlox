package io.kyrixen.tinyblox.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;

public class HUDRenderer {

    // Texture Manager var
    private final TextureManager textureManager;


    // HUD Components //

    private final Bar healthBar;
    private final Bar staminaBar;


    // Textures indentifiers //

    private static final TinyIdentifier HEALTH_BAR_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "health_bar");
    private static final TinyIdentifier STAMINA_BAR_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stamina_bar");


    // Renderer constructor
    public HUDRenderer(TextureManager textureManager) {

        this.textureManager = textureManager;
    
        this.healthBar = new Bar(Constants.WINDOW_WIDTH, 175, 250, 50, 100, 100);
        this.staminaBar = new Bar(Constants.WINDOW_WIDTH, 125, 250, 50, 100, 100);
    
        this.healthBar.initTexture(HEALTH_BAR_TEXTURE);
        this.staminaBar.initTexture(STAMINA_BAR_TEXTURE);

    }


    // Global //

    public void update(MobEntity mobEntity) {

        this.healthBar.setValue(mobEntity.getHealth());
        this.healthBar.setMaxValue(mobEntity.getMaxHealth());

        this.staminaBar.setValue(mobEntity.getStamina());
        this.staminaBar.setMaxValue(mobEntity.getMaxStamina());

    }

    public void render(RendererStack rendererStack) {

        // Render bars
        renderHudBar(healthBar, "Health", rendererStack);
        renderHudBar(staminaBar, "Stamina", rendererStack);

    }


    public void resize(int width, int height) {

        // Resize bars
        healthBar.resize(width, height);
        staminaBar.resize(width, height);

    }


    // Helpers //
    
    // Renders bar
    private void renderHudBar(Bar bar, String text, RendererStack rendererStack) {

        SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;

        float valuePercent = bar.getBarValue() / (float) bar.getBarMaxVal();

        batch.draw(textureManager.getTexture(bar.getTextureID()), bar.getX(), bar.getY(), bar.getWidth() * valuePercent, bar.getHeight());

        float uiScale = Math.min(Gdx.graphics.getWidth() / 800f, Gdx.graphics.getHeight() / 600f);
        font.getData().setScale(uiScale * 0.85f);
        font.draw(rendererStack.batch, text.toUpperCase() + ": " + Integer.toString((int) bar.getBarValue()), bar.getX(), bar.getY() + (bar.getHeight() + rendererStack.font.getCapHeight()) / 2f, bar.getWidth(), Align.center, false);
        font.getData().setScale(1f);    
            
    }

}
