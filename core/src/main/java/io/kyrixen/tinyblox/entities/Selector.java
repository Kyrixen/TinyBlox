package io.kyrixen.tinyblox.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.kyrixen.tinyblox.SoundManager;
import io.kyrixen.tinyblox.utils.Peripheal;
import io.kyrixen.tinyblox.utils.Utils;
import io.kyrixen.tinyblox.world.Camera;

public class Selector {

    // Entity using the selector
    private Entity entity;

    // For sound
    private final SoundManager soundManager;

    // Helper Array
    private ArrayList<Entity> entities;

    // Cords
    private int x;
    private int y;

    // Dimensions
    private int width;
    private int height;

    // Renderer
    ShapeRenderer sr;


    public Selector(Entity entity, ArrayList<Entity> entities, SoundManager soundManager) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entities = entities;
        this.soundManager = soundManager;
        this.sr = new ShapeRenderer();
    
    }


    public void update(int damage) {
        
        // Set selector size 
        this.width = this.entity.width; // same width
        this.height = this.entity.height; // same height

        // Determine position based on last direction
        if (this.entity.lastDirX == 1) {         // right
        
            this.x = this.entity.x + this.entity.width; 
            this.y = this.entity.y;
        
        } else if (this.entity.lastDirX == -1) { // left
        
            this.x = this.entity.x - this.width; 
            this.y = this.entity.y;
        
        } else if (this.entity.lastDirY == 1) {  // down
        
            this.x = this.entity.x;
            this.y = this.entity.y + this.entity.height;
        
        } else if (this.entity.lastDirY == -1) { // up
        
            this.x = this.entity.x;
            this.y = this.entity.y - this.height;
        
        }

        // Keep selector same size as player
        this.width = this.entity.width;
        this.height = this.entity.height;

        // Check for mouse interaction
        if(checkEntityCollision(entities) != null && Peripheal.mousePressed(Input.Buttons.LEFT)){

            Entity e = checkEntityCollision(entities);

            e.damage(damage);

            soundManager.hitentity.play(Utils.getFloatVolume(20));
            
        }

    }


    public void render(Camera camera) { 
        sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(this.x - camera.x, this.y - camera.y, this.width, this.height);
        sr.end();
    }


    public Entity checkEntityCollision(ArrayList<Entity> entities) {

        for (Entity e : entities) {

            if (e.type.equals("player")) continue;

            if (e.x < this.x + this.width && e.x + e.width > this.x && e.y < this.y + this.height && e.y + e.height > this.y) return e;

        }

        return null;
    
    }


    public void cleanup(){
    
        entity = null;
        entities = null;
        
        x = 0;
        y = 0;
     
        width = 0;
        height = 0;

    }

}
