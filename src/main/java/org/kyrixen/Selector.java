package org.kyrixen;


public class Selector {

    protected Entity entity;

    protected int x;
    protected int y;

    protected int width;
    protected int height;


    public Selector(Entity entity) {
        // Initialize the selector with the given entity
        this.entity = entity;
    }


    public void update(float deltaTime) {


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

        } else return;

        
        this.width = this.entity.width;
        this.height = this.entity.height;

    }


    public void render(Renderer renderer) {
        renderer.drawRect(this.x, this.y, this.width, this.height, 255, 255, 255);
    }

}
