package io.kyrixen.tinyblox.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import fastnoiselite.FastNoiseLite;
import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.entities.Entity;
import io.kyrixen.tinyblox.entities.mob.Enemy;
import io.kyrixen.tinyblox.entities.mob.MobEntity;
import io.kyrixen.tinyblox.entities.mob.Player;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint;
import io.kyrixen.tinyblox.saving.world.ChunkLoader;
import io.kyrixen.tinyblox.saving.world.ChunkSaver;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.world.chunk.Chunk;
import io.kyrixen.tinyblox.world.chunk.ChunkPos;
import io.kyrixen.tinyblox.world.chunk.ChunkRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.Tile;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileStack;

public class Terrain {

    // Dimensions
    private final int w;
    private final int h;

    // Seed
    private final int seed;

    // Renderer
    private final ChunkRenderer chunkRenderer;

    // Noise generator
    private final FastNoiseLite noise;

    // For storing chunks
    private final HashMap<ChunkPos, Chunk> chunks = new HashMap<>();

    //Chunk update vars
    private long lastChunkUpdate;
    private float chunkUpdateDelay = 0.5f;


    // Constructs terrain
    public Terrain(int w, int h, TileRenderer tileRenderer, int seed, float frequency) {

        WorldBlueprint wb = WorldManager.loadWorld(Constants.WORLD_NAME, seed, frequency);

        this.seed = wb.worldSeed;
        this.w = w;
        this.h = h;

        this.chunkRenderer = new ChunkRenderer(tileRenderer);

        noise = new FastNoiseLite();

        // Sets noise generator properties
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetSeed(this.seed);
        noise.SetFrequency(wb.worldFrequency);

    }


    // Pre-generate chunks
    public void init(SoundManager soundManager) {

        Logger.LOGGER.debug("WORLD", "Seed: " + seed);
        chunks.clear();

        for(short cx = (short) (getChunkCountX() / 2 - 3); cx < (short) (getChunkCountY() / 2 + 3); cx++) {
            for(short cy = (short) (getChunkCountX() / 2 - 3); cy < (short) (getChunkCountY() / 2 + 3); cy++) {
                
                ChunkPos pos = new ChunkPos(cx, cy);
                chunks.put(pos, ChunkLoader.load(pos, noise));
                
                Chunk chunk = this.getChunk(cx, cy);
                
                chunk.getEntities().addAll(WorldManager.loadEntities(chunk, soundManager));
                Entity.initTextureAll(chunk.getEntities());

                Logger.LOGGER.debug("WORLD", "Loaded chunk: " + cx + ", " + cy);

            }
        }

        Logger.LOGGER.debug("WORLD", "Chunks size: " + chunks.size());

    }

    // Update terrain
    public void update(Camera camera, TimeCycle timeCycle) {

        chunkRenderer.setAmbient(timeCycle.getBrightnessColor());

        boolean rebuildLighting = false;
        for(Chunk c : chunks.values()) {

            c.checkIfOnScreen(camera);
            if(c.isRendered()) c.checkIfModified();
            if(c.needLightRebuild()) rebuildLighting = true;
        
        }    

        if(rebuildLighting) { rebuildLighting(); }

    }

    // Updates entities in chunks
    public void updateEntities(float deltaTime, Player player) {

        // Those small imigrants took 3 hours to fix
        List<EntityMove> entityMigrations = new ArrayList<>();
        for(Chunk c : chunks.values()) {

            Entity.updateAll(deltaTime, this, c.getEntities());
            for (Entity e : c.getEntities()) {

                if(!(e instanceof Enemy)) continue;
                Enemy en = (Enemy) e;

                en.checkPlayer(player);
            
            }

            checkDeadEntities(c, player);

            for(Entity e : c.getEntities()) {

                short currentChunkX = (short) Math.floorDiv(e.x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
                short currentChunkY = (short) Math.floorDiv(e.y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

                if(currentChunkX == c.getX() && currentChunkY == c.getY()) continue;

                Chunk newChunk = this.getChunk(currentChunkX, currentChunkY);
                if(newChunk == null) continue;

                entityMigrations.add(new EntityMove(e, c, newChunk));

            }

        }



        // Check for imigrants
        for(EntityMove migration : entityMigrations) {

            migration.origin.getEntities().remove(migration.imigrant);
            
            if(migration.host.getEntities().contains(migration.imigrant)) continue;
            migration.host.getEntities().add(migration.imigrant);

        }

    }

    // Loads / Unloads chunks
    public void updateLoadedChunks(Player player, SoundManager soundManager) {

        if(System.currentTimeMillis() - lastChunkUpdate < chunkUpdateDelay * 1000) return;

        short playerChunkX = (short) Math.floorDiv(player.x() / player.width(), Constants.CHUNK_SIZE);
        short playerChunkY = (short) Math.floorDiv(player.y() / player.height(), Constants.CHUNK_SIZE);

        // Checks loading
        boolean rebuildLighting = false;
        for(short cx = (short) (playerChunkX - Constants.LOAD_DISTANCE); cx < playerChunkX + Constants.LOAD_DISTANCE; cx++) {
            for(short cy = (short) (playerChunkY - Constants.LOAD_DISTANCE); cy < playerChunkY + Constants.LOAD_DISTANCE; cy++) {
            
                if(cx < 0 || cx >= getChunkCountX() || cy < 0 || cy >= getChunkCountY()) continue;

                Chunk c = getChunk(cx, cy);
                if(c != null) continue;
                
                c = ChunkLoader.load(new ChunkPos(cx, cy), noise);
                c.getEntities().addAll(WorldManager.loadEntities(c, soundManager));
                Entity.initTextureAll(c.getEntities());

                chunks.put(new ChunkPos(cx, cy), c);

                if(c.needLightRebuild()) rebuildLighting = true;
                Logger.LOGGER.debug("WORLD", "Loaded chunk: " + cx + ", " + cy);

            }   
        }

        if(rebuildLighting) rebuildLighting();


        // Check unloading
        List<ChunkPos> unloadedChunksPos = new ArrayList<>();
        for(ChunkPos chunkPos : chunks.keySet()) {

            int distX = Math.abs(chunkPos.getChunkX() - playerChunkX);
            int distY = Math.abs(chunkPos.getChunkY() - playerChunkY);

            if(distX >= Constants.UNLOAD_DISTANCE && distY >= Constants.UNLOAD_DISTANCE) unloadedChunksPos.add(chunkPos);

        }

        for(ChunkPos unloadedChunkPos : unloadedChunksPos) {

            Chunk c = getChunk(unloadedChunkPos.getChunkX(), unloadedChunkPos.getChunkY());
            if(c != null && c.isModified()) ChunkSaver.save(c);
            WorldManager.saveEntities(c);

            chunks.remove(unloadedChunkPos);
            Logger.LOGGER.debug("WORLD", "Unloaded chunk: " + unloadedChunkPos.getChunkX() + ", " + unloadedChunkPos.getChunkY());

        }

        lastChunkUpdate = System.currentTimeMillis();

    }

    // Checks dead entities
    private void checkDeadEntities(Chunk chunk, Player player) {

        List<Enemy> deadEnemies = new ArrayList<>();
        for(Entity e : chunk.getEntities()) {

            if(!(e instanceof Enemy)) continue;
            Enemy enemy = (Enemy) e;

            if(enemy.isDead()) deadEnemies.add(enemy);
        
        }

        for(Enemy enemy : deadEnemies) { enemy.throwLoot(player, this); }
        
        chunk.getEntities().removeIf(e -> {

            if(!(e instanceof MobEntity) || e instanceof Player) return false;
            MobEntity mob = (MobEntity) e;

            return mob.isDead();

        });

    }


    // Render chunks entities
    public void renderEntities(Player player, TileRenderer tileRenderer, RendererStack rendererStack) {

        for(Chunk c : chunks.values()) {
            if(!c.isRendered()) continue;
            Entity.renderAll(this, player, tileRenderer, c.getEntities(), rendererStack);
        }

    }

    // Render lower visible chunks
    public void renderLower(Player player, RendererStack rendererStack) {
        
        for(Chunk c : chunks.values()) {
            chunkRenderer.renderLower(c, player, rendererStack);
        }

    }

    // Render above visible chunks
    public void renderAbove(Player player, RendererStack rendererStack) {
        
        boolean tileAbovePlayer = false;

        TileStack playerTileStack = this.getWorldTileStack(player.x() / Constants.GRID_SIZE, player.y() / Constants.GRID_SIZE);
        if(playerTileStack != null) {
        
            for(byte level = 0; level < playerTileStack.stackSize(); level++) {

                Tile current = playerTileStack.get(level);
                if(current == null || current.type().isEmpty()) continue;

                if(player.level() < current.level()) tileAbovePlayer = true;

            }
       
        }


        for(Chunk c : chunks.values()) {
            chunkRenderer.renderAbove(c, player, tileAbovePlayer, rendererStack);
        }

    }


    // Render overlay for visible chunks
    public void renderDepthOverlay(Player player, TimeCycle timeCycle, RendererStack rendererStack) {
        
        for(Chunk c : chunks.values()) {
            chunkRenderer.renderDepthOverlay(c, player, timeCycle, rendererStack);
        }
    
    }

    // Draw edges on different heights
    public void drawHeightEdges(Player player, RendererStack rendererStack) {  

        for(Chunk c : chunks.values()) {

            chunkRenderer.drawHeightEdges(c, this, rendererStack);
            chunkRenderer.drawCurrentHeightEdges(c, player, this, rendererStack);

        }

    }


    // Update chunk light
    public void rebuildLighting() {
        
        long start = System.currentTimeMillis();

        for(Chunk c : chunks.values()) {

            c.resetLocalLighting();
            c.setLightRebuild(false);
            
            // If not visible dont render
            if(!c.isRendered()) continue;

            for(byte localX = 0; localX < c.get().length; localX++) {
                for(byte localY = 0; localY < c.get().length; localY++) {

                    TileStack tileStack = c.getTileStack(localX, localY);
                    if(tileStack == null) continue;

                    for(byte level = 0; level < tileStack.height(); level++) {
                    
                        Tile current = tileStack.get(level);
                        if(current == null || current.type().getLightLevel() <= 0f) continue;

                        int worldX = c.getX() * Constants.CHUNK_SIZE + localX;
                        int worldY = c.getY() * Constants.CHUNK_SIZE + localY;

                        applyRadialLight(worldX, worldY, current.level(), current.type().getLightLevel());
                        
                    }
                }
            }
    
            
        }

        Logger.LOGGER.debug("LIGHTING", "Lighting rebuild took: " + (System.currentTimeMillis() - start) + "ms");

    }

    // Applies lighting
    private void applyRadialLight(int worldX, int worldY, byte worldLayer, float lightLevel) {

        for(byte tx = -Constants.LIGHT_RADIUS; tx <= Constants.LIGHT_RADIUS; tx++) {
            for(byte ty = -Constants.LIGHT_RADIUS; ty <= Constants.LIGHT_RADIUS; ty++) {
                for(byte tz = -Constants.LIGHT_RADIUS; tz <= Constants.LIGHT_RADIUS; tz++) {
                
                    int targetWorldX = worldX + tx;
                    int targetWorldY = worldY + ty;
                    byte targetWorldLayer = (byte) (worldLayer + tz);
                    
                    if(targetWorldLayer < Constants.MIN_WORLD_HEIGHT) continue;
                    if(targetWorldLayer > Constants.MAX_WORLD_HEIGHT) continue;

                    short chunkX = (short) Math.floorDiv(targetWorldX, Constants.CHUNK_SIZE);
                    short chunkY = (short) Math.floorDiv(targetWorldY, Constants.CHUNK_SIZE);

                    Chunk c = this.getChunk(chunkX, chunkY);
                    if(c == null) continue;


                    int localLightX = Math.floorMod(targetWorldX, Constants.CHUNK_SIZE);
                    int localLightY = Math.floorMod(targetWorldY, Constants.CHUNK_SIZE);

                    float sourceDist = Vector3.len(tx, ty, tz * 3);
                    if(sourceDist > Constants.LIGHT_RADIUS) continue;

                    float lowerance = 1f - (sourceDist / Constants.LIGHT_RADIUS);
                    lowerance = Math.max(0f, lowerance);

                    Color lightColor = c.getLight((byte) localLightX, (byte) localLightY, targetWorldLayer);


                    lightColor.r += 1f * lowerance * lightLevel;
                    lightColor.g += 0.8f * lowerance * lightLevel;
                    lightColor.b += 0.5f * lowerance * lightLevel;

                    lightColor.r = Math.min(lightColor.r, 1.25f);
                    lightColor.g = Math.min(lightColor.g, 1.25f);
                    lightColor.b = Math.min(lightColor.b, 1.25f);
                
                }
            }
        }

    }
    

    // Find chunk
    public Chunk getChunk(short cX, short cY) {
        return chunks.get(new ChunkPos(cX, cY));
    }

    // Global TileStack accessor
    public TileStack getWorldTileStack(int worldX, int worldY) {

        short chunkX = (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE);

        Chunk chunk = this.getChunk(chunkX, chunkY);

        if(chunk == null) return null;

        byte localX = (byte) Math.floorMod(worldX, Constants.CHUNK_SIZE);
        byte localY = (byte) Math.floorMod(worldY, Constants.CHUNK_SIZE);

        return chunk.getTileStack(localX, localY);

    }

    // Get world height at cordinates
    public byte getWorldLevel(int worldX, int worldY) {

        TileStack stack = getWorldTileStack(worldX, worldY);

        if(stack == null) return 0;

        Tile top = stack.top();

        if(top == null) return 0;

        return top.level();
    
    }

    // Get visible level
    public byte getVisibleLevel(int worldX, int worldY, byte visibleLevel) {

        TileStack stack = getWorldTileStack(worldX, worldY);
        if(stack == null) return getWorldLevel(worldX, worldY);

        for(byte level = visibleLevel; level >= Constants.MIN_WORLD_HEIGHT; level--) {

            Tile tile = stack.get(level);

            if(tile == null) continue;
            if(tile.type().isEmpty()) continue;

            return level;

        }

        return getWorldLevel(worldX, worldY);
    
    }

    // Get world light color at cordinates
    public Color getLightColor(int worldX, int worldY, byte worldLayer) {

        short chunkX = (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE);

        byte localX = (byte) Math.floorMod(worldX, Constants.CHUNK_SIZE);
        byte localY = (byte) Math.floorMod(worldY, Constants.CHUNK_SIZE);

        Chunk c = getChunk(chunkX, chunkY);
        if(c == null) return Color.WHITE;

        return c.getLight(localX, localY, worldLayer);

    }

    // Get nearby entities
    public List<Entity> getNearbyEntities(int worldX, int worldY, int tileRadius) {

        List<Entity> nearbyEntities = new ArrayList<>();

        short chunkX = (short) Math.floorDiv(worldX, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(worldY, Constants.CHUNK_SIZE);

        Chunk current = this.getChunk(chunkX, chunkY);
        if(current != null) nearbyEntities.addAll(current.getEntities());

        byte localX = (byte) Math.floorMod(worldX, Constants.CHUNK_SIZE);
        byte localY = (byte) Math.floorMod(worldY, Constants.CHUNK_SIZE);

        boolean left   = localX - tileRadius < 0;
        boolean right  = localX + tileRadius >= Constants.CHUNK_SIZE;
        boolean top    = localY + tileRadius >= Constants.CHUNK_SIZE;
        boolean bottom = localY - tileRadius < 0;

        if(left) {
            Chunk neighbor = this.getChunk((short) (chunkX - 1), chunkY);
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());
        }
        if(right) {
            Chunk neighbor = this.getChunk((short) (chunkX + 1), chunkY);
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());
        }
        if(top) {
            Chunk neighbor = this.getChunk(chunkX, (short) (chunkY - 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());
        }
        if(bottom) {
            Chunk neighbor = this.getChunk(chunkX, (short) (chunkY + 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());
        }

        if(left && top) {
            Chunk neighbor = this.getChunk((short) (chunkX - 1), (short) (chunkY - 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());    
        }
        if(left && bottom) {
            Chunk neighbor = this.getChunk((short) (chunkX - 1), (short) (chunkY + 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());    
        }
        if(right && top) {
            Chunk neighbor = this.getChunk((short) (chunkX + 1), (short) (chunkY - 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());    
        }
        if(right && bottom) {
            Chunk neighbor = this.getChunk((short) (chunkX + 1), (short) (chunkY + 1));
            if(neighbor != null) nearbyEntities.addAll(neighbor.getEntities());    
        }

        return nearbyEntities;

    }

    // Remove entity
    public void removeEntity(Entity entity) {

        short chunkX = (short) Math.floorDiv(entity.x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(entity.y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

        Chunk entitiesChunk = this.getChunk(chunkX, chunkY);
        if(entitiesChunk == null) return;

        entitiesChunk.getEntities().remove(entity);

    }

    // Add entity
    public void addEntity(Entity entity) {

        short chunkX = (short) Math.floorDiv(entity.x() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);
        short chunkY = (short) Math.floorDiv(entity.y() / Constants.GRID_SIZE, Constants.CHUNK_SIZE);

        Chunk entitiesChunk = this.getChunk(chunkX, chunkY);
        if(entitiesChunk == null) return;

        entitiesChunk.getEntities().add(entity);

    }


    // Get seed
    public int getSeed() { return this.seed; }

    // Get frequency
    public float getFrequency() { return this.noise.getFrequency(); }

    // Get chunk renderer
    public Color getAmbientColor() { return this.chunkRenderer.getAmbient(); }
    
    // Get chunk map
    public HashMap<ChunkPos, Chunk> getChunks() { return this.chunks; }

    // Chunk count helpers
    public int getChunkCountX() { return (w + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE; }
    public int getChunkCountY() { return (h + Constants.CHUNK_SIZE - 1) / Constants.CHUNK_SIZE; }


    // Unload resources
    public void cleanup() {

        // Cleanup all chunks
        if (chunks != null) {
        
            for (Chunk c : chunks.values()) {
                if (c != null) c.cleanup();
            }
        
            chunks.clear();
        
        }
        
    }


    // Helper class
    static class EntityMove {
        
        public Entity imigrant;
        public Chunk origin;
        public Chunk host;

        // Helper constructor
        public EntityMove(Entity imigrant, Chunk origin, Chunk host) {
            this.imigrant = imigrant;
            this.host = host;
            this.origin = origin;
        }

    }


}
