package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.Camera;

public class TileRenderer {
    
    // Flip helper enum
    public enum FlipType {

        NONE(false, false),
        X_AXIS(true, false),
        Y_AXIS(false, true),
        XY_AXIS(true, true);
    
        private final boolean flipX;
        private final boolean flipY;

        FlipType(boolean flipX, boolean flipY) {
            this.flipX = flipX;
            this.flipY = flipY;
        }

        public boolean getFlipX() { return this.flipX; }

        public boolean getFlipY() { return this.flipY; }

    }


    private Camera camera;
    private TextureManager tex;

    public TileRenderer(Camera camera, TextureManager textureManager) {
        this.camera = camera;
        this.tex = textureManager;
    }

    // Draw a single tile / texture with camera offset
    public void draw(TextureID tile, int x, int y, FlipType flip, SpriteBatch batch) {

        // Apply camera offset
        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = Constants.GRID_SIZE * camera.zoom;
        float renderH = Constants.GRID_SIZE * camera.zoom;

        Texture tileTex = tex.getTexture(tile);

        if (tex == null || tex.getTexture(tile) == null) Logger.LOGGER.warn("TILE_RENDERER", "Tile texture not loaded: " + tile.toString());

        batch.draw(tileTex, screenX, screenY, renderW, renderH, 0, 0, tileTex.getWidth(), tileTex.getHeight(), flip.getFlipX(), flip.getFlipY());

    }

    // Draw a single tile / texture with camera offset and custom width / height
    public void draw(TextureID tile, int x, int y, float w, float h, FlipType flip, SpriteBatch batch) {

        // Apply camera offset
        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = w * camera.zoom;
        float renderH = h * camera.zoom;

        Texture tileTex = tex.getTexture(tile);

        if (tex == null || tex.getTexture(tile) == null) Logger.LOGGER.warn("TILE_RENDERER", "Tile texture not loaded: " + tile.toString());

        batch.draw(tileTex, screenX, screenY, renderW, renderH, 0, 0, tileTex.getWidth(), tileTex.getHeight(), flip.getFlipX(), flip.getFlipY());

    }

    // Draw one tile from a tileset
    public void drawTileset(TextureID tileset, int x, int y, int tileX, int tileY, int tileSize, FlipType flip, SpriteBatch batch) {
        
        int srcX = tileX * tileSize;
        int srcY = tileY * tileSize;

        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = Constants.GRID_SIZE * camera.zoom;
        float renderH = Constants.GRID_SIZE * camera.zoom;

        Texture tilesetTex = tex.getTexture(tileset);

        batch.draw(tilesetTex, screenX, screenY, renderW, renderH, srcX, srcY, tileSize, tileSize, flip.getFlipX(), flip.getFlipY());
    
    }

    // Draw one tile from a tileset and with custom width and height
    public void drawTileset(TextureID tileset, int x, int y, float w, float h, int tileX, int tileY, int tileSize, FlipType flip, SpriteBatch batch) {
        
        int srcX = tileX * tileSize;
        int srcY = tileY * tileSize;

        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = w * camera.zoom;
        float renderH = h * camera.zoom;

        Texture tilesetTex = tex.getTexture(tileset);

        batch.draw(tilesetTex, screenX, screenY, renderW, renderH, srcX, srcY, tileSize, tileSize, flip.getFlipX(), flip.getFlipY());
    
    }

}
