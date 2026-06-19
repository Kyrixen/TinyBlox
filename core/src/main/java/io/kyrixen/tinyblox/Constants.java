package io.kyrixen.tinyblox;

// Global vars
public class Constants {

    // Window size
    public static int WINDOW_WIDTH = 800;
    public static int WINDOW_HEIGHT = 600;

    // Sound settings
    public static int VOLUME = 70;

    // FPS settings
    public static int FPS = 240;
    public static boolean SHOW_FPS = false;
    public static boolean VSYNC = true;

    // World stuff
    public static final int MAP_WIDTH = 256;
    public static final int MAP_HEIGHT = 256;

    public static final byte MAX_WORLD_HEIGHT = 20;
    public static final byte MIN_WORLD_HEIGHT = 0;
    
    public static final byte MAX_TERRAIN_HEIGHT = 16;
    public static final byte MIN_TERRAIN_HEIGHT = 6;

    public static final byte LIGHT_RADIUS = 4;

    // Tile stuff
    public static final int GRID_SIZE = 16;
    public static final byte CHUNK_SIZE = 12;

    // Rendering stuff
    public static final byte RENDER_DISTANCE = 2;
    public static final byte ROOF_REVEAL_RADIUS = 3;

    // Versions
    public static final String VERSION = "dev-1";
    public static final int BLUEPRINT_FORMAT_VERSION = 1;

    // Dev settings
    public static final boolean DEBUG = true;

    // Day and Night Cycle
    public static final float FULL_TIME_CYCLE = 300f;

    // Entity settings
    public static final byte MAX_ENTITY_COUNT = 8;

}
