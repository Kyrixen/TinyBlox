package io.kyrixen.tinyblox.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Box (Dialogue window)
public class Box {

    // Position
    private int x, y;

    // Size
    private int w, h;

    // Textures
    private Texture corner;
    private Texture side;
    private Texture center;

    private int tile; // Size of one tile

    // Init instance
    public void init(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    
    }

    // Init textures
    public void initTexture(Texture corner, Texture side, Texture center) {
    
        this.corner = corner;
        this.side = side;
        this.center = center;
        this.tile = corner.getWidth();
    
    }

    // Render dialog box
    public void render(SpriteBatch batch) {

        if (corner == null) return;

        // Corners //
        drawCorner(batch, x, y, 0);                         // Top-left
        drawCorner(batch, x + w - tile, y, 90);             // Top-right
        drawCorner(batch, x + w - tile, y + h - tile, 180); // Bottom-right
        drawCorner(batch, x, y + h - tile, 270);            // Bottom-left

        // Top and Bottom //
        for (int i = tile; i < w - tile; i += tile) {
            
            batch.draw(side, x + i, y, tile, tile);
            drawRotated(batch, side, x + i, y + h - tile, 180); // Bottom
        
        }

        // Left and Right //
        for (int i = tile; i < h - tile; i += tile) {
            
            drawRotated(batch, side, x, y + i, 270);           // Left
            drawRotated(batch, side, x + w - tile, y + i, 90); // Right
        
        }

        // Center //
        for (int ix = tile; ix < w - tile; ix += tile) {
            for (int iy = tile; iy < h - tile; iy += tile) {
                
                batch.draw(center, x + ix, y + iy, tile, tile);
        
            }

        }

    }

    // Draw rotated corner
    private void drawCorner(SpriteBatch batch, int x, int y, int angle) {
        drawRotated(batch, corner, x, y, angle);
    }

    // Rotate around top-left
    private void drawRotated(SpriteBatch batch, Texture tex, float x, float y, float angleDeg) {
        
        float tileW = tile;
        float tileH = tile;

        batch.draw(tex, x, y, tileW / 2f, tileH / 2f, tileW, tileH, 1f, 1f, angleDeg, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
    
    }

}