package io.kyrixen.tinyblox.saving.blueprints;

public class ChunkBlueprint {

    public int formatVersion;
    public ChunkStack[] stacks;

    
    public static class ChunkStack {
    
        public ChunkTile[] tiles;

        public byte x;
        public byte y;

        public static class ChunkTile {
            public String tile;
            public byte level;
        }
                
    }

}
