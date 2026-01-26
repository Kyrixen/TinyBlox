package org.kyrixen;


import java.awt.Graphics2D;

import org.kyrixen.Chunk.Tile;


public class Utils {
    
    // Not used (used in LWJGL version)
    public static float getFloatColor(int color) {

        float floatColor = color / 255.0f;
        
        return floatColor;
    
    }

    // For sounds
    public static float getFloatVolume(int volume) {

        float floatVolume = volume / 100.0f;
        
        return floatVolume;
    
    }

    // Check collision between entities
    public static boolean checkCollision(Entity e1, Entity e2) {
        
        return (e1.x < e2.x + e2.width &&
                e1.x + e1.width > e2.x &&
                e1.y < e2.y + e2.height &&
                e1.y + e1.height > e2.y);
    
    }

    // For better func
    public static void betterTilesetDraw(int x, int y, int tix, int tiy, Textures texture, Graphics2D g){
        texture.drawTileset(texture.terrainTileset, x, y, Constants.GRID_SIZE, Constants.GRID_SIZE, tix, tiy, Constants.GRID_SIZE, g);
    }

    // Spawns at safe location
    public static int spawnY() {

        // Initial spawn
        int spawnY = Constants.MAP_HEIGHT / 2;

        while (true) {

            boolean solidFound = false;

            // Loop through tiles
            for (Tile tile : Terrain.tiles) {

                // Check if safe
                if (tile.getY() == spawnY && !tile.solid()) {

                    solidFound = true;

                    int dir = (int) (Math.random() * 2); // 0 or 1

                    if (dir == 0) spawnY -= Constants.GRID_SIZE;
                    else spawnY += Constants.GRID_SIZE;

                    break; // stop checking tiles for this spawnY

                }

            }

            if (!solidFound) break; // Safe spot found

        }

        return spawnY;

    }

    // Spawns at safe location
    public static int spawnX() {

        // Initial spawn
        int spawnX = Constants.MAP_WIDTH / 2;

        while (true) {

            boolean solidFound = false;

            //Loop through tiles
            for (Tile tile : Terrain.tiles) {

                //Check if solid
                if (tile.getX() == spawnX && !tile.solid()) {

                    solidFound = true;

                    int dir = (int) (Math.random() * 2); // 0 or 1

                    if (dir == 0) spawnX -= Constants.GRID_SIZE;
                    else spawnX += Constants.GRID_SIZE;

                    break; // Stop checking tiles for this spawnY

                }

            }

            if (!solidFound) break; // Safe spot found

        }

        return spawnX;

    }

}