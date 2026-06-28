package io.kyrixen.tinyblox.platform;

import io.kyrixen.tinyblox.menu.selection.WorldList;

public interface WorldIE {
    
    // Imports selected world
    void importWorld(WorldList worldList);

    // Exports selected world
    void exportWorld(String worldName);

}