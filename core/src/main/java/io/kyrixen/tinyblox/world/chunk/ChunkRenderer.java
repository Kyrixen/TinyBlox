package io.kyrixen.tinyblox.world.chunk;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.TimeCycle;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer.FlipType;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class ChunkRenderer {

    // Tile renderer
    private final TileRenderer tileRenderer;

    // Terrain texture ID
    private final TextureID terrainTileset = new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles");
    
    // Chunk size
    private static final byte CHUNK_SIZE = Constants.CHUNK_SIZE;


    public ChunkRenderer(TileRenderer tileRenderer) { this.tileRenderer = tileRenderer; }

    // Render above chunk
    public void renderAbove(Chunk c, Player player, boolean tileAbovePlayer, RendererStack rendererStack) {

        // Check if can render chunk
        if (!canRender(c)) return;

        SpriteBatch batch = rendererStack.batch;


        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                TileStack tileStack = c.getTileStack(tx, ty);
                if(tileStack == null || tileStack.isEmpty()) continue;
                
                int globalX = getGlobalX(c, tx);
                int globalY = getGlobalY(c, ty);

                float tileCenterX = globalX + Constants.GRID_SIZE / 2f;
                float tileCenterY = globalY + Constants.GRID_SIZE / 2f;
                float playerCenterX = player.x() + player.width() / 2f;
                float playerCenterY = player.y() + player.height() / 2f;

                float distX = tileCenterX - playerCenterX;
                float distY = tileCenterY - playerCenterY;
                int dist = (int) Vector2.len(distX, distY);
                
                List<Tile> transparentTiles = new ArrayList<>();
                for(byte layer = (byte) (tileStack.stackSize() - 1); layer > Constants.MIN_WORLD_HEIGHT; layer--) {

                    Tile stackedTile = tileStack.get(layer);

                    if(stackedTile == null) continue;
                    if(stackedTile.level() <= player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;

                    if(stackedTile.type().isTransparent()) { transparentTiles.add(stackedTile); continue; }

                    Color light = c.getLight(tx, ty, layer);

                    // Draw first visible opaque tile
                    batch.setColor(light.r, light.g, light.b, 1f);

                    int levelDiff = stackedTile.level() - player.level();

                    // Smaller reveal when player is deeper underground
                    int playerDepth = Constants.MAX_TERRAIN_HEIGHT - player.level();

                    float revealRadius = Constants.ROOF_REVEAL_RADIUS * Constants.GRID_SIZE - playerDepth * 2f;
                    revealRadius = Math.max(Constants.GRID_SIZE, revealRadius);

                    if(tileAbovePlayer && levelDiff > 0 && dist <= revealRadius) {

                        float alpha = dist / revealRadius;
                        alpha = MathUtils.clamp(alpha, 0f, 1f);

                        // Smoothstep
                        alpha = alpha * alpha * (3f - 2f * alpha);

                        // Base roof transparency
                        alpha *= 0.4f;

                        // Thicker roof = more transparent
                        alpha *= Math.max(0.25f, 1f - levelDiff * 0.15f);

                        batch.setColor(light.r, light.g, light.b, alpha);

                    }

                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);

                    // Draw transparent tile on top
                    for(int i = transparentTiles.size() - 1; i >= 0; i--) {
                        tileRenderer.drawTileset(terrainTileset, globalX, globalY, transparentTiles.get(i).tileX(), transparentTiles.get(i).tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                    }

                    batch.setColor(1f, 1f, 1f, 1f);

                    break;
                    
                }
            
            }

        }
        
    }


    // Render lower chunk
    public void renderLower(Chunk c, Player player, RendererStack rendererStack) {

        // Check if can render chunk
        if (!canRender(c)) return;

        SpriteBatch batch = rendererStack.batch;


        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                TileStack tileStack = c.getTileStack(tx, ty);
                if(tileStack == null || tileStack.isEmpty()) continue;
                
                int globalX = getGlobalX(c, tx);
                int globalY = getGlobalY(c, ty);

                List<Tile> transparentTiles = new ArrayList<>();
                for(byte layer = (byte) (tileStack.stackSize() - 1); layer > Constants.MIN_WORLD_HEIGHT; layer--) {

                    Tile stackedTile = tileStack.get(layer);

                    if(stackedTile == null) continue;
                    if(stackedTile.level() > player.level()) continue;
                    if(stackedTile.tileX() == -1 || stackedTile.tileY() == -1) continue;

                    if(stackedTile.type().isTransparent()) { transparentTiles.add(stackedTile); continue; }

                    Color light = c.getLight(tx, ty, layer);

                    // Draw first visible opaque tile
                    batch.setColor(light.r, light.g, light.b, 1f);
                    tileRenderer.drawTileset(terrainTileset, globalX, globalY, stackedTile.tileX(), stackedTile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);

                    // Draw transparent tile on top
                    for(int i = transparentTiles.size() - 1; i >= 0; i--) {
                        tileRenderer.drawTileset(terrainTileset, globalX, globalY, transparentTiles.get(i).tileX(), transparentTiles.get(i).tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                    }
                    
                    batch.setColor(1f, 1f, 1f, 1f);

                    break;
                    
                }
            
            }

        }
        
    }

    // Render depth for the top tile
    public void renderDepthOverlay(Chunk c, Player player, TimeCycle timeCycle, RendererStack rendererStack) {

        // Check if can render overlay for chunk
        if (!canRender(c)) return;

        SpriteBatch batch = rendererStack.batch;


        float lightBrightness = timeCycle.getBrightness();

        // Render each tile
        for (byte tx = 0; tx < CHUNK_SIZE; tx++) {
            for (byte ty = 0; ty < CHUNK_SIZE; ty++) {
                
                Tile tile = c.getTileStack(tx, ty).top();

                if(tile == null) continue;
                
                int globalX = getGlobalX(c, tx);
                int globalY = getGlobalY(c, ty);

                int levelDiff = Math.abs(tile.level() - player.level());
                float normalized = (float) levelDiff / (Constants.MAX_TERRAIN_HEIGHT - Constants.MIN_TERRAIN_HEIGHT);
                
                float alpha = normalized * 0.65f;
                alpha = Math.min(alpha, 0.55f);

                if(tile.level() > player.level()) batch.setColor(0.6f, 0.6f, 0.6f, alpha * lightBrightness);
                else if(tile.level() < player.level()) batch.setColor(0.15f, 0.15f, 0.15f, alpha * lightBrightness);
                else batch.setColor(1f, 1f, 1f, 0f);
                
                tileRenderer.drawTilesetOutline(terrainTileset, globalX, globalY, tile.tileX(), tile.tileY(), Constants.GRID_SIZE, FlipType.NONE, rendererStack);
                
                batch.setColor(1f, 1f, 1f, 1f);
            
            }
        
        }

    }

    // Draw edges on different heights
    public void drawHeightEdges(Chunk c, Terrain terrain, RendererStack rendererStack) {

        if(!canRender(c)) return;

        Camera cam = rendererStack.camera;
        float tileSize = Constants.GRID_SIZE * cam.zoom;

        ShapeRenderer shapeRenderer = rendererStack.shape;
        shapeRenderer.setColor(0f, 0f, 0f, 1f);


        for(byte localX = 0; localX < CHUNK_SIZE; localX++) {
            for(byte localY = 0; localY < CHUNK_SIZE; localY++) {

                int worldX = c.getX() * CHUNK_SIZE + localX;
                int worldY = c.getY() * CHUNK_SIZE + localY;
                
                byte current = terrain.getWorldLevel(worldX, worldY);
                if(current <= 0) continue;


                byte left = terrain.getWorldLevel(worldX - 1, worldY);
                byte right = terrain.getWorldLevel(worldX + 1, worldY);
                byte top = terrain.getWorldLevel(worldX, worldY + 1);
                byte bottom = terrain.getWorldLevel(worldX, worldY - 1);
                if(current == left && current == right && current == top && current == bottom) continue;


                int tileX = worldX * Constants.GRID_SIZE;
                int tileY = worldY * Constants.GRID_SIZE;
                float screenX = (tileX - cam.x) * cam.zoom;
                float screenY = (tileY - cam.y) * cam.zoom;
 
                
                if(left < current) shapeRenderer.line(screenX, screenY, screenX, screenY + tileSize);
                if(right < current) shapeRenderer.line(screenX + tileSize, screenY, screenX + tileSize, screenY + tileSize);
                if(top < current) shapeRenderer.line(screenX, screenY + tileSize, screenX + tileSize, screenY + tileSize);
                if(bottom < current) shapeRenderer.line(screenX, screenY, screenX + tileSize, screenY);
        
            }
        }

    }

    // Draw edges on different heights
    public void drawCurrentHeightEdges(Chunk c, Player player, Terrain terrain, RendererStack rendererStack) {

        if(!canRender(c)) return;

        Camera cam = rendererStack.camera;
        float tileSize = Constants.GRID_SIZE * cam.zoom;

        ShapeRenderer shapeRenderer = rendererStack.shape;
        shapeRenderer.setColor(0f, 0f, 0f, 1f);


        for(byte localX = 0; localX < CHUNK_SIZE; localX++) {
            for(byte localY = 0; localY < CHUNK_SIZE; localY++) {

                int worldX = c.getX() * CHUNK_SIZE + localX;
                int worldY = c.getY() * CHUNK_SIZE + localY;
                
                byte current = terrain.getVisibleLevel(worldX, worldY, player.level());
                if(current <= 0) continue;


                byte left = terrain.getVisibleLevel(worldX - 1, worldY, player.level());
                byte right = terrain.getVisibleLevel(worldX + 1, worldY, player.level());
                byte top = terrain.getVisibleLevel(worldX, worldY + 1, player.level());
                byte bottom = terrain.getVisibleLevel(worldX, worldY - 1, player.level());
                if(current == left && current == right && current == top && current == bottom) continue;


                int tileX = worldX * Constants.GRID_SIZE;
                int tileY = worldY * Constants.GRID_SIZE;
                float screenX = (tileX - cam.x) * cam.zoom;
                float screenY = (tileY - cam.y) * cam.zoom;
 
                
                if(left < current) shapeRenderer.line(screenX, screenY, screenX, screenY + tileSize);
                if(right < current) shapeRenderer.line(screenX + tileSize, screenY, screenX + tileSize, screenY + tileSize);
                if(top < current) shapeRenderer.line(screenX, screenY + tileSize, screenX + tileSize, screenY + tileSize);
                if(bottom < current) shapeRenderer.line(screenX, screenY, screenX + tileSize, screenY);
        
            }
        }

    }


    // Helpers //

    private boolean canRender(Chunk c) {

        int worldChunksX = Math.max(1, (Constants.MAP_WIDTH + CHUNK_SIZE - 1) / CHUNK_SIZE);
        int worldChunksY = Math.max(1, (Constants.MAP_HEIGHT + CHUNK_SIZE - 1) / CHUNK_SIZE);

        return c.isRendered() && !(c.getX() < 0 || c.getX() >= worldChunksX || c.getY() < 0 || c.getY() >= worldChunksY);

    }


    private int getGlobalX(Chunk c, byte tx) {
        return (c.getX() * CHUNK_SIZE + tx) * Constants.GRID_SIZE;
    }

    private int getGlobalY(Chunk c, byte ty) {
        return (c.getY() * CHUNK_SIZE + ty) * Constants.GRID_SIZE;
    }

}
