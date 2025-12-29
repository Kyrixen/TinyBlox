package org.kyrixen;


import java.util.HashMap;

import fastnoiselite.FastNoiseLite;

import static org.lwjgl.opengl.GL11.*;


public class Chunk {

    private int CHUNK_SIZE = 3;
    private int cX;
    private int cY;


    public static final int MAX_X = Constants.MAP_WIDTH;
    public static final int MAX_Y = Constants.MAP_HEIGHT;
    public static final int MIN_X = 0;
    public static final int MIN_Y = 0;


    private Camera cam;

    public boolean loaded;

    private Textures tex;

    HashMap<String, Tile> chunk = new HashMap<>();


    public record Tile(int x, int y, String type, int tileX, int tileY, boolean solid, boolean loaded) {

        // Constructor automatically sets tileX and tileY from type
        public Tile(int x, int y, String type) {

            this(
                x,
                y,
                type,
                getTileX(type),
                getTileY(type),
                getSolid(type),
                true // loaded by default
            );

        }


        // Map type to tileX (column) in 2x2 tileset
        private static int getTileX(String type) {

            return switch (type.toLowerCase()) {
                case "grass" -> 1;
                case "stone" -> 0;
                case "dirt"  -> 0;
                case "water" -> 1;
                default      -> 0;
            };

        }


        // Map type to tileY (row) in 2x2 tileset
        private static int getTileY(String type) {
        
            return switch (type.toLowerCase()) {
                case "grass" -> 0;
                case "stone" -> 1;
                case "dirt"  -> 0;
                case "water" -> 1;
                default      -> 0;
            };
        
        }


        private static boolean getSolid(String type){

                return switch(type.toLowerCase()){
                    case "grass" -> true;
                    case "dirt"  -> true;
                    case "stone" -> false;
                    case "water" -> false;
                    default      -> false;

                };

        }
    
    }



    public Chunk(int x, int y, int size, boolean loaded, Textures tex, Camera cam){

        this.cX = x;
        this.cY = y;;
        this.CHUNK_SIZE = size;
        this.loaded = loaded;
        this.tex = tex;
        this.cam = cam;

    }


    public String generateKey(int tx, int ty){
        return Integer.toString(tx) + "," + Integer.toString(ty);
    }


    public void generate(FastNoiseLite noise) {

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {

                int worldX = (cX * CHUNK_SIZE + x) * Constants.GRID_SIZE;
                int worldY = (cY * CHUNK_SIZE + y) * Constants.GRID_SIZE;

                // Skip tiles outside world bounds
                if (worldX >= MAX_X || worldY >= MAX_Y || worldX < MIN_X || worldY < MIN_Y) continue;

                String type;
                float t = noise.GetNoise(cX * CHUNK_SIZE + x, cY * CHUNK_SIZE + y);

                if (t < -0.3f) type = "water";
                else if (t < 0.0f) type = "stone";
                else if (t < 0.5f) type = "dirt";
                else type = "grass";

                Tile tile = new Tile(worldX, worldY, type);
                chunk.put(x + "," + y, tile);
            }
        }

        loaded = !chunk.isEmpty(); // only mark as loaded if there are tiles
    }



    public void render() {

        if (!loaded) return;

        if (cX < 0 || cX >= MAX_X / Constants.GRID_SIZE || cY < 0 || cY >= MAX_Y / Constants.GRID_SIZE) return;


        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, tex.terrainTileset);

        glBegin(GL_QUADS);

        for (Tile tile : chunk.values()) {
            int x = tile.x - cam.x;
            int y = tile.y - cam.y;
            int s = Constants.GRID_SIZE;

            float u0 = tile.tileX * 0.5f;
            float v0 = tile.tileY * 0.5f;
            float u1 = u0 + 0.5f;
            float v1 = v0 + 0.5f;

            glTexCoord2f(u0, v1); glVertex2f(x, y);
            glTexCoord2f(u1, v1); glVertex2f(x + s, y);
            glTexCoord2f(u1, v0); glVertex2f(x + s, y + s);
            glTexCoord2f(u0, v0); glVertex2f(x, y + s);
        }

        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }



    public void load(){

        if(loaded) return;

        loaded = true;

    }


    public void unload(){

        if(!loaded) return;

        loaded = false;

    }


    public void checkIfOnScreen() {

        int chunkWorldSize = CHUNK_SIZE * Constants.GRID_SIZE;

        // Camera chunk coordinates (top-left of screen)
        int camChunkX = cam.x / chunkWorldSize;
        int camChunkY = cam.y / chunkWorldSize;

        int buffer = 2; // number of chunks beyond camera view

        // Determine visible chunk range
        int left   = Math.max(0, camChunkX - buffer);
        int right  = Math.min((Constants.MAP_WIDTH / chunkWorldSize) - 1, camChunkX + cam.RENDER_DISTANCE + buffer);
        int top    = Math.max(0, camChunkY - buffer);
        int bottom = Math.min((Constants.MAP_HEIGHT / chunkWorldSize) - 1, camChunkY + cam.RENDER_DISTANCE + buffer);

        // Load chunk if inside visible area
        loaded = (cX >= left && cX <= right && cY >= top && cY <= bottom);

    }


    

}
