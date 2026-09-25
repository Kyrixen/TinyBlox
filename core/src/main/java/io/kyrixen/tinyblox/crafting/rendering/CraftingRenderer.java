package io.kyrixen.tinyblox.crafting.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.crafting.recipe.Recipe;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.inventory.ItemStack;


public class CraftingRenderer {

    // Texture Manager var
    private final TextureManager tex;

    private final GlyphLayout layout = new GlyphLayout();

    // Renderer constructor
    public CraftingRenderer(TextureManager tex) {
        this.tex = tex;
    }


    // Renders button
    public void renderCraftingButton(CraftingButton button, RendererStack rendererStack) {
        rendererStack.batch.draw(tex.getTexture(button.getTexture()), button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }
    
    // Renders recipe button (the auto adjust bs did AI)
    public void renderRecipeButton(int scaleMult, RecipeButton button, RendererStack rendererStack) {

        SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;

        Recipe recipe = button.getRecipe();

        float uiScale = Math.min(Gdx.graphics.getWidth() / 800f, Gdx.graphics.getHeight() / 600f) * 0.75f;

        int padding = Math.round(4 * scaleMult * uiScale);
        int freeSpace = button.getWidth() - Math.round(20 * scaleMult * uiScale) - padding;
        float maxScale = 2f * uiScale;
        String recipeName = recipe.getName();

        // Background
        batch.draw(tex.getTexture(button.getTexture()), button.getX(), button.getY(), button.getWidth(), button.getHeight());

        // Output icon
        int outputX = (int) (button.getX() + 2 * scaleMult * uiScale);
        int outputY = (int) (button.getY() + 2 * scaleMult * uiScale);

        batch.draw(tex.getTexture(recipe.getOutput().getItem().textureID()), outputX, outputY, 16 * scaleMult * uiScale, 16 * scaleMult * uiScale);

        // Recipe name
        font.getData().setScale(1f);
        layout.setText(font, recipeName);
        float scale = Math.min(maxScale, freeSpace / layout.width);
        font.getData().setScale(scale);
        layout.setText(font, recipeName);
        
        font.draw(batch, recipeName, button.getX() + 20 * scaleMult * uiScale, button.getY() + 16 * scaleMult * uiScale);

        // Ingredients row
        ItemStack[] ingredients = recipe.getIngredients();

        int iconSize = (int) (8 * scaleMult * uiScale);
        int slotWidth = (int) (iconSize + 20 * uiScale);
        int totalWidth = ingredients.length * slotWidth;

        int startX = (int) (button.getX() + 20 * scaleMult * uiScale + ((button.getWidth() - 22 * scaleMult * uiScale) - totalWidth) / 2f);
        int ingredientY = (int) (button.getY() + 2 * scaleMult * uiScale);

        font.getData().setScale(0.5f * uiScale);

        for(int i = 0; i < ingredients.length; i++) {

            ItemStack ingredient = ingredients[i];
            int ingredientX = startX + i * slotWidth;

            batch.draw(tex.getTexture(ingredient.getItem().textureID()), ingredientX, ingredientY, iconSize, iconSize);
            font.draw(batch, Byte.toString(ingredient.getCount()), ingredientX + iconSize + 2 * uiScale, ingredientY + iconSize - 8 * uiScale);

        }

        font.getData().setScale(1f);

    }

    // Renders container
    public void renderRecipeContainer(RecipeContainer container, RendererStack rendererStack) {
        
        rendererStack.batch.draw(tex.getTexture(container.getTextureID()), container.getX(), container.getY(), container.getWidth(), container.getHeight());
        
        float uiScale = Math.min(Gdx.graphics.getWidth() / 800f, Gdx.graphics.getHeight() / 600f) * 0.75f;
        
        rendererStack.font.getData().setScale(1.5f * uiScale);
        layout.setText(rendererStack.font, "CRAFTING");
        
        rendererStack.font.draw(rendererStack.batch, "CRAFTING", container.getX() + (container.getWidth() - layout.width) / 2f, container.getY() + container.getHeight() - container.getHeight() / 12f);
        
        rendererStack.font.getData().setScale(1f);
    
    }

}
