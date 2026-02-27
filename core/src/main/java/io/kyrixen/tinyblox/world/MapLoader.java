package io.kyrixen.tinyblox.world;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.graphics.Textures;

public class MapLoader {
    
    private int width;
    private int height;

    private int size;
    private int seed;

    private String path;

    // DTOs for JSON serialization
    public static class MapData {

        public float version = 1.0f;
        public int size;
        public int width;
        public int height;
        public int seed;
        public ArrayList<ChunkData> chunks = new ArrayList<>();
    
    }

    public static class ChunkData {

        public int cX;
        public int cY;
        public boolean loaded;
        public boolean modified;
        public int[] types;
        
        // Backward compatibility with old format.
        public ArrayList<TileData> tiles = new ArrayList<>();
    
    }

    public static class TileData {
    
        public int x;
        public int y;
        public int tileX;
        public int tileY;
        public boolean solid;
        public String type;
    
    }

    public MapLoader(String path, int width, int height, int size) {

        this.path = path;

        this.width = width;
        this.height = height;

        this.size = size;
        this.seed = Terrain.seed;

    }

    public void save(String pathName) {

        Path out = resolve(pathName);
        
        try {

            if (out.getParent() != null) {
                Files.createDirectories(out.getParent());
            }

            // Stream JSON directly to disk to avoid huge in-memory copies.
            try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
                writer.write("{\n");
                writer.write("  \"version\": 1.0,\n");
                writer.write("  \"size\": ");
                writer.write(Integer.toString(this.size));
                writer.write(",\n");
                writer.write("  \"width\": ");
                writer.write(Integer.toString(this.width));
                writer.write(",\n");
                writer.write("  \"height\": ");
                writer.write(Integer.toString(this.height));
                writer.write(",\n");
                writer.write("  \"seed\": ");
                writer.write(Integer.toString(this.seed));
                writer.write(",\n");
                writer.write("  \"chunks\": [\n");

                boolean firstChunk = true;

                for (Map.Entry<String, Chunk> entry : Terrain.chunks.entrySet()) {
                    Chunk sourceChunk = entry.getValue();
                    if (sourceChunk == null) continue;

                    String[] parts = entry.getKey().split(",");
                    if (parts.length != 2) continue;

                    if (!firstChunk) writer.write(",\n");
                    firstChunk = false;

                    writer.write("    {\n");
                    writer.write("      \"cX\": ");
                    writer.write(parts[0]);
                    writer.write(",\n");
                    writer.write("      \"cY\": ");
                    writer.write(parts[1]);
                    writer.write(",\n");
                    writer.write("      \"loaded\": ");
                    writer.write(Boolean.toString(sourceChunk.loaded));
                    writer.write(",\n");
                    writer.write("      \"modified\": ");
                    writer.write(Boolean.toString(sourceChunk.modified));
                    writer.write(",\n");
                    writer.write("      \"types\": [");

                    boolean firstTile = true;

                    for (int tx = 0; tx < this.size; tx++) {
                        for (int ty = 0; ty < this.size; ty++) {
                            Chunk.Tile sourceTile = sourceChunk.chunk.get(tx + "," + ty);
                            if (sourceTile == null) continue;

                            if (!firstTile) writer.write(',');
                            firstTile = false;

                            writer.write(Integer.toString(encodeType(sourceTile.type())));
                        }
                    }

                    writer.write("]\n");
                    writer.write("    }");
                }

                writer.write("\n");
                writer.write("  ]\n");
                writer.write("}\n");
            }
        
        } catch (IOException e) {
            throw new RuntimeException("Failed to save map to " + out, e);
        }

    }

    // Example loader: reads JSON into DTO and rebuilds Terrain.chunks.
    public MapData load(String pathName, int chunkSize, Textures tex, Camera cam) {

        Path in = resolve(pathName);
        String jsonText;

        try {
            jsonText = new String(Files.readAllBytes(in), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read map from " + in, e);
        }

        Json json = new Json();
        MapData mapData = json.fromJson(MapData.class, jsonText);
        
        if (mapData == null) {
            throw new RuntimeException("Map JSON is invalid: " + in);
        }

        Terrain.chunks.clear();

        for (ChunkData chunkData : mapData.chunks) {
        
            Chunk chunk = new Chunk(chunkData.cX, chunkData.cY, chunkSize, chunkData.loaded, tex, cam);
            chunk.chunk.clear();

            if (chunkData.types != null && chunkData.types.length > 0) {
                int index = 0;

                for (int tx = 0; tx < chunkSize; tx++) {
                    for (int ty = 0; ty < chunkSize; ty++) {
                        if (index >= chunkData.types.length) break;

                        String type = decodeType(chunkData.types[index++]);
                        int worldTileX = chunkData.cX * chunkSize + tx;
                        int worldTileY = chunkData.cY * chunkSize + ty;

                        int worldX = worldTileX * io.kyrixen.tinyblox.Constants.GRID_SIZE;
                        int worldY = worldTileY * io.kyrixen.tinyblox.Constants.GRID_SIZE;

                        Chunk.Tile tile = new Chunk.Tile(worldX, worldY, type);
                        chunk.chunk.put(chunk.generateKey(tx, ty), tile);
                    }
                }
            } else {
                // Backward compatibility with older verbose format.
                for (TileData tileData : chunkData.tiles) {
                    String type = tileData.type;
                    Chunk.Tile tile = new Chunk.Tile(tileData.x, tileData.y, type);

                    int localX = (tileData.x / io.kyrixen.tinyblox.Constants.GRID_SIZE) - (chunkData.cX * chunkSize);
                    int localY = (tileData.y / io.kyrixen.tinyblox.Constants.GRID_SIZE) - (chunkData.cY * chunkSize);
                    chunk.chunk.put(chunk.generateKey(localX, localY), tile);
                }
            }

            Terrain.chunks.put(chunkData.cX + "," + chunkData.cY, chunk);
        
        }

        this.width = mapData.width;
        this.height = mapData.height;
        return mapData;

    }

    private Path resolve(String fileName) {
        return Paths.get(path).resolve(fileName).normalize();
    }

    private int encodeType(String type) {
        if (type == null) return 0;
        switch (type.toLowerCase()) {
            case "grass": return 1;
            case "stone": return 2;
            case "dirt": return 3;
            case "water": return 4;
            default: return 0;
        }
    }

    private String decodeType(int id) {
        switch (id) {
            case 1: return "grass";
            case 2: return "stone";
            case 3: return "dirt";
            case 4: return "water";
            default: return "dirt";
        }
    }


    public void destroy() {

        this.path = null;
        this.width = 0;
        this.height = 0;
        this.size = 0;
        this.seed = 0;

    }

}
