package io.kyrixen.tinyblox.crafting.rendering;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.crafting.recipe.Recipe;
import io.kyrixen.tinyblox.entities.inventory.ItemStack;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;


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

        int padding = 4 * scaleMult;
        int freeSpace = button.getWidth() - (20 * scaleMult) - padding;
        float maxScale = 2f;
        String recipeName = recipe.getName();


        // Background
        batch.draw(tex.getTexture(button.getTexture()), button.getX(), button.getY(), button.getWidth(), button.getHeight());

        // Output icon
        int outputX = button.getX() + 2 * scaleMult;
        int outputY = button.getY() + 2 * scaleMult;

        batch.draw(tex.getTexture(recipe.getOutput().getItem().textureID()), outputX, outputY, 16 * scaleMult, 16 * scaleMult);

        // Recipe name
        font.getData().setScale(1f);
        layout.setText(font, recipeName);
        float scale = Math.min(maxScale, freeSpace / layout.width);
        font.getData().setScale(scale);
        layout.setText(font, recipeName);
        
        font.draw(batch, recipeName, button.getX() + 20 * scaleMult, button.getY() + 16 * scaleMult);

        // Ingredients row
        ItemStack[] ingredients = recipe.getIngredients();

        int iconSize = 8 * scaleMult;
        int slotWidth = iconSize + 20;
        int totalWidth = ingredients.length * slotWidth;

        int startX = button.getX() + 20 * scaleMult + ((button.getWidth() - 22 * scaleMult) - totalWidth) / 2;
        int ingredientY = button.getY() + 2 * scaleMult;

        font.getData().setScale(0.5f);

        for(int i = 0; i < ingredients.length; i++) {

            ItemStack ingredient = ingredients[i];
            int ingredientX = startX + i * slotWidth;

            batch.draw(tex.getTexture(ingredient.getItem().textureID()), ingredientX, ingredientY, iconSize, iconSize);
            font.draw(batch, Byte.toString(ingredient.getCount()), ingredientX + iconSize + 2, ingredientY + iconSize - 8);

        }

    }

    // Renders container
    public void renderRecipeContainer(RecipeContainer container, RendererStack rendererStack) {
        rendererStack.batch.draw(tex.getTexture(container.getTextureID()), container.getX(), container.getY(), container.getWidth(), container.getHeight());
        rendererStack.font.getData().scale(0.75f);
        rendererStack.font.draw(rendererStack.batch, "CRAFTING", container.getX() + container.getWidth() / 7.2f, container.getY() + container.getHeight() - container.getHeight() / 12f);
    }

}
