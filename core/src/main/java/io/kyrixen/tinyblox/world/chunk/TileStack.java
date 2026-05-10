package io.kyrixen.tinyblox.world.chunk;

import java.util.ArrayList;
import java.util.List;

// Tile stack (used for storing)
public class TileStack  {

    private List<Tile> tiles;

    public TileStack(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public TileStack() {
        this.tiles = new ArrayList<>();
    }

    public byte height() {
        return (byte) this.tiles.size();
    }

    public Tile top() {
        if(this.isEmpty()) return null;
        return this.tiles.get(tiles.size() - 1);
    }

    public Tile bottom() {
        if(this.isEmpty()) return null;
        return this.tiles.get(0);
    }

    public void push(Tile tile) {
        this.tiles.add(tile);
    }

    public void pop() {
        if(this.isEmpty()) return;
        this.tiles.remove(tiles.size() - 1);
    }

    public void set(Tile tile, byte height) {
        this.tiles.set(height, tile);
    }

    public Tile get(byte height) {
        return this.tiles.get(height);
    }

    public boolean isEmpty() {
        return this.tiles.isEmpty();
    }

    public boolean solid() {
        Tile top = top();
        return top != null && top.solid();
    }

}
