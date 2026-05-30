package io.kyrixen.tinyblox.world.chunk.tile;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

// Tile stack (used for storing)
public class TileStack  {

    private List<Tile> tiles;

    public TileStack(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public TileStack() {
        this.tiles = new ArrayList<>();
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

            if(tile == null) continue;
            if(tile.type() == TileType.VOID) continue;
            if(tile.type() == TileType.AIR) continue;

            return tile;

        }

        return null;

    }

    public Tile bottom() {

        for(int i = 0; i < tiles.size(); i++) {

            Tile tile = tiles.get(i);

            if(tile == null) continue;
            if(tile.type() == TileType.VOID) continue;
            if(tile.type() == TileType.AIR) continue;

            return tile;

        }

        return null;

    }

    @Deprecated
    public void push(Tile tile) {
        if(getAtLevel(tile.level()) != null) return;

        for(int i = 0; i < tiles.size(); i++) {
            
            if(this.tiles.get(i).level() < tile.level()) continue;
            
            this.tiles.add(i, tile);
            
            return;
        
        }

        this.tiles.add(tile);
    }

    @Deprecated
    public void pop() {
        if(this.isEmpty()) return;
        this.tiles.remove(tiles.size() - 1);
    }

    public void popAtLayer(byte level) {

        if(level < 0 || level >= this.tiles.size()) return;

        Tile tile = this.tiles.get(level);
        if(tile == null) return;

        this.tiles.set(level, new Tile(TileType.AIR, level));

    }

    public void set(Tile tile, byte level) {

        while(this.tiles.size() <= level) { this.tiles.add(null); }
        this.tiles.set(level, tile);

    }

    public Tile get(byte height) {

        if(height < 0 || height >= tiles.size()) return null;
        return tiles.get(height);

    }

    public Tile getTopTerrain() {

        for(int i = tiles.size() - 1; i >= 0; i--) {

            Tile tile = tiles.get(i);

            if(tile == null) continue;
            if(!tile.type().isTerrainish()) continue;

            return tile;

        }

        return null;

    }

    @Deprecated
    public Tile getAtLevel(byte level) {
        for(Tile tile : tiles) { if(tile == null) continue; if(tile.level() == level) return tile; }
        return null;
    }

    public boolean isEmpty() {

        for(Tile tile : tiles) {

            if(tile == null) continue;
            if(tile.type() == TileType.VOID) continue;
            if(tile.type() == TileType.AIR) continue;

            return false;

        }

        return true;

    }

}
