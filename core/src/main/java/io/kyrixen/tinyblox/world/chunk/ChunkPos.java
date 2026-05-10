package io.kyrixen.tinyblox.world.chunk;

// Chunk position helper
public class ChunkPos {

    private final short chunkX;
    private final short chunkY;
    
    public ChunkPos(short cx, short cy) {
        this.chunkX = cx;
        this.chunkY = cy;
    }

    public short getChunkX() {
        return this.chunkX;
    }

    public short getChunkY() {
        return this.chunkY;
    }

    @Override
    public boolean equals(Object o) {
    
        if(this == o) return true;
        if(!(o instanceof ChunkPos)) return false;

        ChunkPos cP = (ChunkPos) o;

        return this.chunkX == cP.chunkX && this.chunkY == cP.chunkY;

    }

    @Override
    public int hashCode() {
        return 31 * chunkX + chunkY;
    }

}
