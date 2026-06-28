package io.kyrixen.tinyblox.teavm.webie;

import io.kyrixen.tinyblox.menu.selection.WorldList;
import io.kyrixen.tinyblox.menu.selection.WorldListScanner;
import io.kyrixen.tinyblox.platform.WorldIE;

public class WebIE implements WorldIE {
    
    @Override
    public void importWorld(WorldList worldList) {        
        WebImportWorld.openExplorer(worldList);
        worldList.updateWorldSlots(WorldListScanner.getWorlds());
    }

    @Override
    public void exportWorld(String worldName) {
        WebExportWorld.openExplorer(worldName);
    }

}
