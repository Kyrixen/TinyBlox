package io.kyrixen.tinyblox.menu.creator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.FrequencyType;

public class CreatorHelper extends InputAdapter {

    // Enum for selected state
    enum SelectedPrompt {
    
        NAME,
        SEED,
        FREQUENCY
    
    }


    // Selection
    private SelectedPrompt selected = SelectedPrompt.NAME;

    // World vars
    private String worldName = "";
    private String seed = String.valueOf(RandomUtils.randomInt(Integer.MAX_VALUE - 1));
    private FrequencyType frequency = FrequencyType.NORMAL;

    // Text layout helper
    private GlyphLayout layout = new GlyphLayout();


    // Update creator helper
    public void update(int mouseX, int mouseY, boolean pressed) {
        
        if(pressed && insideName(mouseX, mouseY)) selected = SelectedPrompt.NAME;
        if(pressed && insideSeed(mouseX, mouseY)) selected = SelectedPrompt.SEED;
        if(pressed && insideFrequency(mouseX, mouseY)) selected = SelectedPrompt.FREQUENCY;
        

        // That diabolical calculation did AI
        if(selected == SelectedPrompt.FREQUENCY && pressed && insideFrequency(mouseX, mouseY)) {
            FrequencyType[] values = FrequencyType.values();
            frequency = values[(frequency.ordinal() + 1) % values.length];
        }

    }

    // Render prompts
    public void renderText(RendererStack rendererStack) {

        SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;

        font.getData().setScale(1f);

        String line = "_____________________";

        layout.setText(font, line);
        float centerLine = (Constants.WINDOW_WIDTH - layout.width) / 2f;

        font.draw(batch, line, centerLine, 460);
        font.draw(batch, line, centerLine, 340);
        font.draw(batch, line, centerLine, 220);


        String nameLabel = "MAP NAME:";
        String seedLabel = "SEED:";
        String frequencyLabel = "FREQUENCY:";

        layout.setText(font, nameLabel);
        float nameX = (Constants.WINDOW_WIDTH - layout.width) / 2f;
        font.draw(batch, nameLabel, nameX, 500);

        layout.setText(font, seedLabel);
        float seedX = (Constants.WINDOW_WIDTH - layout.width) / 2f;
        font.draw(batch, seedLabel, seedX, 380);

        layout.setText(font, frequencyLabel);
        float frequencyX = (Constants.WINDOW_WIDTH - layout.width) / 2f;
        font.draw(batch, frequencyLabel, frequencyX, 260);

        font.getData().setScale(1f);

    } 

    // Render player input
    public void renderInput(RendererStack rendererStack) {

        SpriteBatch batch = rendererStack.batch;
        BitmapFont font = rendererStack.font;

        font.getData().setScale(0.85f);

        if(selected == SelectedPrompt.NAME) font.setColor(1f, 1f, 0f, 1f);
        layout.setText(font, worldName);
        font.draw(batch, worldName, (Constants.WINDOW_WIDTH - layout.width) / 2f, 470);
        font.setColor(1f, 1f, 1f ,1f);

        if(selected == SelectedPrompt.SEED) font.setColor(1f, 1f, 0f, 1f);
        layout.setText(font, seed);
        font.draw(batch, seed, (Constants.WINDOW_WIDTH - layout.width) / 2f, 350);
        font.setColor(1f, 1f, 1f ,1f);

        if(selected == SelectedPrompt.FREQUENCY) font.setColor(1f, 1f, 0f, 1f);
        layout.setText(font, frequency.name().replace("_", " "));
        font.draw(batch, frequency.name().replace("_", " "), (Constants.WINDOW_WIDTH - layout.width) / 2f, 230);
        font.setColor(1f, 1f, 1f ,1f);

        font.getData().setScale(1f);

    }

    // Gets the info of the created world
    public WorldBlueprint getWorldInfo() {

        WorldBlueprint worldInfo = new WorldBlueprint();

        worldInfo.formatVersion = Constants.SAVE_FORMAT_VERSION;

        if(worldName.isEmpty()) worldInfo.worldName = "NEW MAP";
        else worldInfo.worldName = worldName;

        if(seed.isEmpty()) worldInfo.worldSeed = RandomUtils.randomInt(Integer.MAX_VALUE - 1);
        else worldInfo.worldSeed = Integer.parseInt(seed);

        worldInfo.worldFrequency = frequency.name();
        worldInfo.lastEntityID = 0;

        return worldInfo;

    }


    // Helpers //

    private boolean insideName(int mouseX, int mouseY) {
        return mouseX >= 250 && mouseX <= 550 && mouseY >= 440 && mouseY <= 490;
    }

    private boolean insideSeed(int mouseX, int mouseY) {
        return mouseX >= 250 && mouseX <= 550 && mouseY >= 320 && mouseY <= 370;
    }

    private boolean insideFrequency(int mouseX, int mouseY) {
        return mouseX >= 250 && mouseX <= 550 && mouseY >= 200 && mouseY <= 250;
    }



    // Input processor handling //

    // Key typing override
    @Override
    public boolean keyTyped(char character) {

        if(selected == SelectedPrompt.NAME) {
            if((Character.isLetterOrDigit(character) || character == ' ') && worldName.length() < 20) worldName += Character.toUpperCase(character);
        } else if(selected == SelectedPrompt.SEED && seed.length() < 9) {
            if(Character.isDigit(character)) seed += character;
        }


        return true;
    
    }

    // Backspace override
    @Override
    public boolean keyDown(int keycode) {

        if(keycode != Input.Keys.BACKSPACE) return true;

        if(selected == SelectedPrompt.NAME && worldName.length() > 0) worldName = worldName.substring(0, worldName.length() - 1);
        if(selected == SelectedPrompt.SEED && seed.length() > 0) seed = seed.substring(0, seed.length() - 1);

        return true;

    }

}
