package io.kyrixen.tinyblox.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.TileRenderer;

public class ItemEntity extends Entity {
    
    // Item type
    private final Item item;

    public ItemEntity(int id, int x, int y, Sfx soundManager, Item item) {
        super(id, x, y, soundManager);
        this.item = item;
        this.texture = item.textureID();
    }

    @Override
    // Render item entity
    public void render(TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch){

        // Get brightness
        float brightness = 0.5f + timeCycle.getBrightness() * 0.5f;

        batch.setColor(brightness, brightness, brightness, 1.0f);
        tileRenderer.draw(this.texture, x, y, Constants.GRID_SIZE / 3f, Constants.GRID_SIZE / 3f, batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    public Item getItem() {
        return this.item;
    }

}
