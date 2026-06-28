package io.kyrixen.tinyblox.lwjgl3.desktopie;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Zipper;

public class DesktopImportWorld {
    
    // Opens file explorer
    static void openExplorer() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TinyBlox Worlds (*.tbworld)", "tbworld"));
        
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) importWorld(chooser.getSelectedFile().toPath().toString());

    }


    // Imports selected world
    private static void importWorld(String worldFile) {

        // Scan worlds
        List<String> worldNames = new ArrayList<>();
        List<String> worldPaths = Platform.fileManager.listDir(WorldManager.worldsFolder.path());
        
        for(String worldPath : worldPaths) {
            worldNames.add(Platform.fileManager.getEndpoint(worldPath));
        }

        String worldName = Platform.fileManager.getEndpoint(worldFile).replace(".tbworld", "");

        int copiesCount = 0;
        for(String wName : worldNames) { if(wName.equals(worldName) || wName.startsWith(worldName + "_")) copiesCount++; }

        if(copiesCount > 0) worldName += "_" + copiesCount;
        Zipper.unzipFile(Platform.fileManager.readBytes(worldFile), WorldManager.worldsFolder.path() + "/" + worldName);

    }

}
