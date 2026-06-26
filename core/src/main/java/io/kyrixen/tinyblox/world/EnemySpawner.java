package io.kyrixen.tinyblox.world;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.mob.Bomber;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.entities.mob.Slime;
import io.kyrixen.tinyblox.entities.mob.Voidling;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.RandomUtils;
import io.kyrixen.tinyblox.world.TimeCycle.DayTime;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class EnemySpawner {

    private float spawnTimer = 8f / Constants.DIFFICULTY.getDiffMult();
    private long lastSpawn = 0L;

    private final SoundManager soundManager;

    public EnemySpawner(SoundManager soundManager) {
        this.soundManager = soundManager;
        lastSpawn = System.currentTimeMillis();
    }

    public void spawn(Player player, Terrain terrain) {

        if(System.currentTimeMillis() - lastSpawn < spawnTimer * 1000) return;

        short playerChunkX = (short) ((player.x() / Constants.GRID_SIZE) / Constants.CHUNK_SIZE);
        short playerChunkY = (short) ((player.y() / Constants.GRID_SIZE) / Constants.CHUNK_SIZE);

        short pickedChunkX = (short) RandomUtils.randomInt(playerChunkX - Constants.RENDER_DISTANCE, playerChunkX + Constants.RENDER_DISTANCE);
        short pickedChunkY = (short) RandomUtils.randomInt(playerChunkY - Constants.RENDER_DISTANCE, playerChunkY + Constants.RENDER_DISTANCE);
        Chunk pickedChunk = terrain.getChunk(pickedChunkX, pickedChunkY);
        
        if(pickedChunk == null) return;

        long enemiesChunkCount = pickedChunk.getEntities().stream().filter(e -> e instanceof Enemy).count();
        if(enemiesChunkCount >= Constants.MAX_ENTITY_CHUNK) return;

        byte pickedLocalX = (byte) RandomUtils.randomInt(0, Constants.CHUNK_SIZE - 1);
        byte pickedLocalY = (byte) RandomUtils.randomInt(0, Constants.CHUNK_SIZE - 1);
        TileStack tileStack = pickedChunk.getTileStack(pickedLocalX, pickedLocalY);
        Tile top = tileStack.getTopTerrain();

        if(top == null) return;
        if(top.level() + 1 != player.level()) return;

        int worldX = (pickedChunkX * Constants.CHUNK_SIZE + pickedLocalX) * Constants.GRID_SIZE;
        int worldY = (pickedChunkY * Constants.CHUNK_SIZE + pickedLocalY) * Constants.GRID_SIZE;

        int enemyType = RandomUtils.randomInt(1, 4);
        Enemy newEnemy = null;

        switch (enemyType) {

            case 1:
                newEnemy = new Enemy(worldX, worldY, this.soundManager);    
                break;

            case 2:
                newEnemy = new Slime(worldX, worldY, this.soundManager);    
                break;
            
            case 3:
                newEnemy = new Bomber(worldX, worldY, this.soundManager);
                break;

            case 4:
                newEnemy = new Voidling(worldX, worldY, this.soundManager);
                break;

        }

        newEnemy.initTexture();

        newEnemy.setLevel((byte) (top.level() + 1));
        
        pickedChunk.getEntities().add(newEnemy);

        lastSpawn = System.currentTimeMillis();

        Logger.LOGGER.debug("ENEMY_SPAWNER", "Chunk(" + pickedChunkX + "," + pickedChunkY + ") entities: " + pickedChunk.getEntities().toString());

    }


    public void updateSpawnRate(TimeCycle timeCycle) {
        if(timeCycle.getDayTime() == DayTime.NIGHT) this.setSpawnTimer(5f / Constants.DIFFICULTY.getDiffMult());
        else this.setSpawnTimer(8f / Constants.DIFFICULTY.getDiffMult());
        if(timeCycle.getDayTime() != DayTime.NIGHT && Constants.DIFFICULTY == Difficulty.EASY) this.setSpawnTimer(99999999f);
    }

    public void setSpawnTimer(float spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

}
