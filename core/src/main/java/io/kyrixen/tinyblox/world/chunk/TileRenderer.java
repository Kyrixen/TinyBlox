package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.Renderer;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.Camera;

public class TileRenderer {
    
    private Camera camera;
    private TextureManager tex;

    public TileRenderer(Camera camera, TextureManager textureManager) {
        this.camera = camera;
        this.tex = textureManager;
    }

    // Draw a single tile / texture with camera offset
    public void draw(TextureID tile, int x, int y, SpriteBatch batch) {

        // Apply camera offset
        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = Constants.GRID_SIZE * camera.zoom;
        float renderH = Constants.GRID_SIZE * camera.zoom;

        Texture tileTex = tex.getTexture(tile);

        if (tex == null || tex.getTexture(tile) == null) Logger.LOGGER.warn("TILE_RENDERER", "Tile texture not loaded: " + tile.toString());

        if (tileTex == null) { Renderer.drawMissingTexture(screenX, screenY, renderW, renderH, tex, batch); }
        else batch.draw(tileTex, screenX, screenY, renderW, renderH, 0, 0, tileTex.getWidth(), tileTex.getHeight(), false, false);


    }

    // Draw one tile from a tileset
    public void drawTileset(TextureID tileset, int x, int y, int tileX, int tileY, int tileSize, SpriteBatch batch) {
        
        int srcX = tileX * tileSize;
        int srcY = tileY * tileSize;

        float screenX = (x - camera.x) * camera.zoom;
        float screenY = (y - camera.y) * camera.zoom;
        float renderW = Constants.GRID_SIZE * camera.zoom;
        float renderH = Constants.GRID_SIZE * camera.zoom;

        Texture tilesetTex = tex.getTexture(tileset);

        // Check if textures are loaded
        if (tex == null || tex.getTexture(tileset) == null) Logger.LOGGER.warn("TILE_RENDERER", "Tile texture not loaded: " + tileset.toString());

        if (tilesetTex == null) { Renderer.drawMissingTexture(screenX, screenY, renderW, renderH, tex, batch); }
        else batch.draw(tilesetTex, screenX, screenY, renderW, renderH, srcX, srcY, tileSize, tileSize, false, false);
    
    }

}
