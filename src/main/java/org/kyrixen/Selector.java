package org.kyrixen;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Selector {

    // Entity using the selector
    private Entity entity;

    // Helper Array
    private ArrayList<Entity> entities;

    // Cords
    private int x;
    private int y;

    // Dimensions
    private int width;
    private int height;


    public Selector(Entity entity, ArrayList<Entity> entities) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entities = entities;
    
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
        if(checkEntityCollision(entities) != null && Input.mousePressed(MouseEvent.BUTTON1)){

        Entity e = checkEntityCollision(entities);

        e.damage(damage);
            
        }

    }


    public void render(Graphics2D g, Camera camera) {
        g.setColor(new Color(255, 255, 255));
        g.drawRect(this.x - camera.x, this.y - camera.y, this.width, this.height);
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
