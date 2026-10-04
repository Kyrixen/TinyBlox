package io.kyrixen.tinyblox.graphics.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    private final float fps;
    private final TextureRegion[] frames;

    private int currentFrame = 0;
    private float speedMult = 1.0f;
    

    public Animation(Texture animationAtlas, int columns, int rows, float fps) {
        
        this.frames = new TextureRegion[columns * rows];
        this.fps = fps;

        TextureRegion[][] regions = TextureRegion.split(animationAtlas, animationAtlas.getWidth() / columns, animationAtlas.getHeight() / rows); 

        int frame = 0;
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                this.frames[frame++] = regions[row][column];
            }
        }

    }


    private float accumTime = 0f;
    public void advance(float delta) {
    
        accumTime += delta;
        
        this.currentFrame = (int) (accumTime * fps * speedMult);
        if(currentFrame >= this.frames.length) currentFrame = 0;
    
    }

    public void reset() {
        accumTime = 0f;
        this.currentFrame = 0;
    }
    
    
    public void setFrame(int frame) {
        if(frame < 0 || frame >= this.frames.length) return;
        this.currentFrame = frame;
    }

    public void nextFrame() {
        currentFrame++;
        if(currentFrame >= this.frames.length) currentFrame = 0;
    }

    public void previousFrame() {
        currentFrame--;
        if(currentFrame < 0) currentFrame = this.frames.length - 1;
    }


    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMult = speedMultiplier;
    }


    public int getFramesCount() {
        return this.frames.length;
    }

    public float getFPS() {
        return this.fps;
    }

    public TextureRegion getCurrentFrame() {
        return this.frames[currentFrame];
    }

}
