package io.kyrixen.tinyblox.saving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.saving.blueprints.StructureBlueprint;
import io.kyrixen.tinyblox.utils.Logger;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.chunk.structures.Structure;
import io.kyrixen.tinyblox.world.chunk.structures.StructureBuilder;
import io.kyrixen.tinyblox.world.chunk.structures.StructureRegister;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;
import io.kyrixen.tinyblox.world.chunk.structures.Structure.Rarity;

public class StructureLoader {

    // Json parser
    private static final Json json = new Json();

    // Structure folder
    private static final FileHandle assetsManifest = Gdx.files.internal("assets.txt");


    // Loads structure from file
    public static Structure load(String path) {
    
        FileHandle structureFile = Gdx.files.internal(path);

        StructureBlueprint bp = json.fromJson(StructureBlueprint.class, structureFile);    
        if(bp.formatVersion != Constants.BLUEPRINT_FORMAT_VERSION) throw new RuntimeException("Unsupported format version: " + bp.formatVersion);

        byte height = (byte) bp.layers[0].length;
        byte width = (byte) bp.layers[0][0].length;
        byte layers = (byte) bp.layers.length;
        
        Logger.LOGGER.debug("LOADER", "Loaded structure blueprint: " + structureFile.name());


        String[] pathParts = path.split("/");
        if(pathParts.length < 3 || !pathParts[pathParts.length - 2].equals("structures")) Logger.LOGGER.error("LOADER", "Invalid structure: " + path);
        String namespace = pathParts[pathParts.length - 3];
        String structureName = structureFile.nameWithoutExtension();

        StructureBuilder sb = new StructureBuilder(new TinyIdentifier(namespace, IdentifierType.STRUCTURE, structureName), width, height, layers, Rarity.valueOf(bp.rarity));
        for(byte layer = 0; layer < bp.layers.length; layer++) {
            for(byte y = 0; y < bp.layers[layer].length; y++) {
                for(byte x = 0; x < bp.layers[layer][y].length; x++) {

                    String tileName = bp.layers[layer][y][x];
                    TileType type = TileType.valueOf(tileName);

                    if(type == TileType.AIR) continue;
                    sb.setTile(x, y, layer, type);

                }
            }
        }

        return sb.build();

    }

    // Auto load all structures in folder
    public static void loadAll() {

        String[] entries = assetsManifest.readString().split("\n");
        for(String entry : entries) {
            entry = entry.trim();
            if(entry.contains("/structures/") && entry.endsWith(".json")) StructureRegister.add(load(entry));
        }

    }

}
