package io.kyrixen.tinyblox.world.chunk.structures;

import java.util.ArrayList;
import java.util.List;

import io.kyrixen.tinyblox.saving.StructureLoader;
import io.kyrixen.tinyblox.utils.Logger;

public class StructureRegister {
    
    // Structure holder
    private static final List<Structure> STRUCTURES = new ArrayList<>();

    public static void initStructures() {
        
        if(!STRUCTURES.isEmpty()) return;
        
        StructureLoader.loadAll();
        Logger.LOGGER.debug("REGISTER", "Loaded structures count: " + STRUCTURES.size());
    
    }


    // Getter
    public static List<Structure> getStructures() { return STRUCTURES; }

    // Setter
    public static void add(Structure structure) { STRUCTURES.add(structure); }

}
