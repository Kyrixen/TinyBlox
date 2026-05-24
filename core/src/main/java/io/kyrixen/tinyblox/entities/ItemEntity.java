package io.kyrixen.tinyblox.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.inventory.Item;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.sound.Sfx;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.TileRenderer.FlipType;

public class ItemEntity extends Entity {

    // Floating offsets
    private float floatOffsetY = 3f;
    private float floatOffsetX = 2f;

    // Item type
    private final Item item;

    // Player
    private final MobEntity mob;

    Sfx soundManager;

    public ItemEntity(int id, int x, int y, Sfx soundManager, Item item, MobEntity mob) {

        super(id, x, y, Constants.GRID_SIZE / 3, Constants.GRID_SIZE / 3);

        this.soundManager = soundManager;

        this.item = item;
        this.mob = mob;
        this.texture = item.textureID();

        this.x += MathUtils.random(-3, 3);
        this.y += MathUtils.random(-3, 3);

        this.dirY = 1;
        this.dirX = 1;

    }


    @Override
    // Update item entity
    public void update(float deltaTime, Terrain terrain) {

        floatOffsetY += dirY * 10f * deltaTime;
        floatOffsetX += dirX * 1f * deltaTime;
        if (Math.abs(floatOffsetY) > 3f) dirY = dirY  * -1;
        if (Math.abs(floatOffsetX) > 2f) dirX = dirX  * -1;
        
        float itemCenterX = x + width / 2f;
        float itemCenterY = y + height / 2f;
        float mobCenterX = mob.x() + mob.width() / 2f;
        float mobCenterY = mob.y() + mob.height() / 2f;

        float dx = mobCenterX - itemCenterX;
        float dy = mobCenterY - itemCenterY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if(distance <= 1f || distance > 64f) return;

        x += Math.round(dx / distance * 60f * deltaTime);
        y += Math.round(dy / distance * 60f * deltaTime);

    }


    @Override
    // Render item entity
    public void render(TimeCycle timeCycle, TileRenderer tileRenderer, SpriteBatch batch){

        // Get brightnesses
        float brightness = 0.5f + timeCycle.getBrightness() * 0.5f;
        float itemBrightness = brightness * 0.9f + 0.1f;

        batch.setColor(itemBrightness + 0.15f, itemBrightness + 0.15f, itemBrightness + 0.15f, 1.0f);
        tileRenderer.draw(this.texture, (int) (x + floatOffsetX), (int) (y + floatOffsetY), Constants.GRID_SIZE / 2f, Constants.GRID_SIZE / 2f, FlipType.NONE, batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    // Pick up the item
    public Item pickup() {
        soundManager.pickupitem.play(Utils.getFloatSound(40), MathUtils.random(0.95f, 1.2f), 0f);
        return this.item;
    }

    @Override
    public String toString() {
        return "ItemEntity(" + this.id + ") { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + " }";
    }


}
