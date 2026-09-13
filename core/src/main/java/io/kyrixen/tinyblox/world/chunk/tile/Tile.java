package io.kyrixen.tinyblox.world.chunk.tile;

// Tile class
public class Tile {

    // Tile data
    private final TileType type;

    // Terrain height
    private final byte level;

    // Constructs tile
    public Tile(TileType type, byte level) {

        this.type = type;
        this.level = level;

    }

    // Helper functions //

    public TileType type() { return type; }
    public byte level() { return level; }


    @Override
    public String toString() {
        return "Tile{ type=" + type + ", level=" + level + " }";
    }

}
