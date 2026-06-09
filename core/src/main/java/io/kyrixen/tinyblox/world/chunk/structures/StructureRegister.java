package io.kyrixen.tinyblox.world.chunk.structures;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.world.chunk.structures.Structure.Rarity;
import io.kyrixen.tinyblox.world.chunk.tile.Tile.TileType;

public class StructureRegister {
    
    // Structure holder
    private static final List<Structure> STRUCTURES = new ArrayList<>();
    
    // Helper items holders //

    public static Structure LAMP_MONUMENT;
    public static Structure ORE_PILE;
    public static Structure WATCHTOWER_RUIN;

    static {

        StructureBuilder sb = new StructureBuilder("LAMP MONUMENT", (byte) 3, (byte) 3, Rarity.UNCOMMON);

        sb.fill(TileType.WOOD);

        sb.setTile((byte) 1, (byte) 0, TileType.STONE);
        sb.setTile((byte) 0, (byte) 1, TileType.STONE);
        sb.setTile((byte) 2, (byte) 1, TileType.STONE);
        sb.setTile((byte) 1, (byte) 2, TileType.STONE);

        sb.setTile((byte) 1, (byte) 1, TileType.CAGED_LAMP);

        LAMP_MONUMENT = sb.build();


        sb = new StructureBuilder("ORE PILE", (byte) 2, (byte) 2, Rarity.COMMON);

        sb.fill(TileType.STONE);
        sb.setTile((byte) 0, (byte) 0, TileType.IRON);
        sb.setTile((byte) 1, (byte) 1, TileType.COAL);
    
        ORE_PILE = sb.build();


        sb = new StructureBuilder("WATCHTOWER RUIN", (byte) 5, (byte) 5, Rarity.RARE);

        sb.fill(TileType.STONE);

        sb.setTile((byte) 0, (byte) 0, TileType.WOOD);
        sb.setTile((byte) 4, (byte) 0, TileType.WOOD);
        sb.setTile((byte) 0, (byte) 4, TileType.WOOD);
        sb.setTile((byte) 4, (byte) 4, TileType.WOOD);

        sb.setTile((byte) 2, (byte) 4, TileType.AIR);

        sb.setTile((byte) 1, (byte) 4, TileType.GLASS);
        sb.setTile((byte) 3, (byte) 4, TileType.GLASS);

        sb.setTile((byte) 0, (byte) 2, TileType.GLASS);
        sb.setTile((byte) 4, (byte) 2, TileType.GLASS);

        sb.setTile((byte) 2, (byte) 2, TileType.CAGED_LAMP);

        sb.setTile((byte) 1, (byte) 1, TileType.AIR);
        sb.setTile((byte) 3, (byte) 3, TileType.AIR);
        sb.setTile((byte) 1, (byte) 3, TileType.AIR);

        sb.setTile((byte) 3, (byte) 1, TileType.COAL);

        WATCHTOWER_RUIN = sb.build();

    }

    public static void initStructures() {

        if(!STRUCTURES.isEmpty()) return;

        STRUCTURES.add(LAMP_MONUMENT);
        STRUCTURES.add(ORE_PILE);
        STRUCTURES.add(WATCHTOWER_RUIN);

    }


    // Getter
    public static List<Structure> getStructures() { return STRUCTURES; }

}
