package org.kyrixen;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Selector {

    private Entity entity;
    private ArrayList<Entity> entities;

    private int x;
    private int y;

    private int width;
    private int height;


    public Selector(Entity entity, ArrayList<Entity> entities) {

        // Initialize the selector with the given entity
        this.entity = entity;
        this.entities = entities;
    
    }


    public void update(int damage) {

        // Update logic for the selector can be added here if needed
        if (this.entity.dirX == 1) {

            this.x = this.entity.x + this.entity.width;
            this.y = this.entity.y;

        } else if (this.entity.dirX == -1) {

            this.x = this.entity.x - this.entity.width;
            this.y = this.entity.y;

        } else if (this.entity.dirY == 1) {

            this.x = this.entity.x;
            this.y = this.entity.y + this.entity.height;

        } else if (this.entity.dirY == -1) {

            this.x = this.entity.x;
            this.y = this.entity.y - this.entity.height;

        } else {
            // Use last direction if no current input
            if (this.entity.lastDirX == 1) {

                this.x = this.entity.x + this.entity.width;
                this.y = this.entity.y;

            } else if (this.entity.lastDirX == -1) {

                this.x = this.entity.x - this.entity.width;
                this.y = this.entity.y;

            } else if (this.entity.lastDirY == 1) {

                this.x = this.entity.x;
                this.y = this.entity.y + this.entity.height;

            } else if (this.entity.lastDirY == -1) {

                this.x = this.entity.x;
                this.y = this.entity.y - this.entity.height;

            } else return;
        }

        this.width = this.entity.width;
        this.height = this.entity.height;

        if(checkEntityCollision(entities) != null && Input.mousePressed(MouseEvent.BUTTON1)){

        Entity e = checkEntityCollision(entities);

        e.damage(damage);
            
        }

    }


    public void render(Graphics2D g) {
        g.setColor(new Color(255, 255, 255));
        g.drawRect(this.x, this.y, this.width, this.height);
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
