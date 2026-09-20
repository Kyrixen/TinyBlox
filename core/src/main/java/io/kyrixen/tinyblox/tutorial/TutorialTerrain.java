package io.kyrixen.tinyblox.tutorial;

import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.world.FrequencyType;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkBuilder;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
import io.kyrixen.tinyblox.world.chunk.tile.TileRegister;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class TutorialTerrain extends Terrain {
 
    public TutorialTerrain(TileRenderer tileRenderer) {
        super(1, 1, tileRenderer, 0, FrequencyType.FLAT);
    }


    @Override 
    // Pre-generate chunks
    public void init(SoundManager soundManager) {

        this.getChunks().clear();

        this.getChunks().put(new ChunkPos((short) 0, (short) 0), new Chunk(new ChunkPos((short) 0, (short) 0), 0));
        this.getChunks().put(new ChunkPos((short) 1, (short) 0), new Chunk(new ChunkPos((short) 1, (short) 0), 0));
        this.getChunks().put(new ChunkPos((short) 0, (short) 1), new Chunk(new ChunkPos((short) 0, (short) 1), 0));
        this.getChunks().put(new ChunkPos((short) 1, (short) 1), new Chunk(new ChunkPos((short) 1, (short) 1), 0));
        
        ChunkBuilder chunkBuilder = new ChunkBuilder();
        chunkBuilder.fill(TileRegister.STONE, (byte) 1);
        chunkBuilder.fill(TileRegister.STONE, (byte) 2);
        chunkBuilder.fill(TileRegister.DIRT, (byte) 3);
        chunkBuilder.fill(TileRegister.GRASS, (byte) 4);

        TileStack[][] tileMap = chunkBuilder.build();

        for(Chunk chunk : this.getChunks().values()) {
            chunk.resetLocalLighting();
            chunk.set(tileMap);
        }

    }

    @Override
    public void updateLoadedChunks(Player player, SoundManager soundManager) {}

}
