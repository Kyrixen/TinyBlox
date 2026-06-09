package io.kyrixen.tinyblox.world.chunk.tile;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

// Tile stack (used for storing)
public class TileStack  {

    private final List<Tile> tiles;

    public TileStack(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public TileStack() {
        this.tiles = new ArrayList<>();
        set(new Tile(TileType.VOID, (byte) 0), (byte) 0);
    }

    public byte stackSize() {
        return (byte) tiles.size();
    }

    public byte height() {

        Tile top = this.top();
        if(top == null) return 0;

        return (byte) (top.level() + 1);

    }

    public Tile top() {

        for(int i = tiles.size() - 1; i >= 0; i--) {

            Tile tile = tiles.get(i);
            if(!isTerrain(tile)) continue;

            return tile;

        }

        return null;

    }

    public Tile bottom() {

        for(int i = 0; i < tiles.size(); i++) {

            Tile tile = tiles.get(i);
            if(!isTerrain(tile)) continue;

            return tile;

        }

        return null;

    }

    public void removeAtLayer(byte level) {

        if(level < 0 || level >= this.tiles.size()) return;

        Tile tile = this.tiles.get(level);
        if(tile == null) return;

        this.tiles.set(level, new Tile(TileType.AIR, level));

    }

    public void set(Tile tile, byte level) {

        while(this.tiles.size() <= level) { this.tiles.add(null); }
        this.tiles.set(level, tile);

    }

    public Tile get(byte level) {

        if(level < 0 || level >= tiles.size()) return null;
        return tiles.get(level);

    }

    public Tile getTopTerrain() {

        for(int i = tiles.size() - 1; i >= 0; i--) {

            Tile tile = tiles.get(i);

            if(tile == null) continue;
            if(!tile.type().isTerrain()) continue;

            return tile;

        }

        return null;

    }

    public boolean isEmpty() {

        for(Tile tile : tiles) {
            if(tile == null) continue;
            if(tile.type().isEmpty()) continue;
            return false;
        }
        
        return true;

    }

    // Helper method
    private boolean isTerrain(Tile tile) {

        if(tile == null) return false;
        if(tile.type() == TileType.VOID) return false;
        if(tile.type().isEmpty()) return false;

        return true;

    }

}
