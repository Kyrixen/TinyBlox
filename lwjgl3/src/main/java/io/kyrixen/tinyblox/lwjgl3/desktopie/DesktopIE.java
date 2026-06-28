package io.kyrixen.tinyblox.lwjgl3.desktopie;

import io.kyrixen.tinyblox.menu.selection.WorldList;
import io.kyrixen.tinyblox.menu.selection.WorldListScanner;
import io.kyrixen.tinyblox.platform.WorldIE;

public class DesktopIE implements WorldIE {
    
    @Override
    public void importWorld(WorldList worldList) {
        DesktopImportWorld.openExplorer();
        worldList.updateWorldSlots(WorldListScanner.getWorlds());
    }

    @Override
    public void exportWorld(String worldName) {
        DesktopExportWorld.openExplorer(worldName);
    }

}
