package io.kyrixen.tinyblox.saving;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.FileManager;
import io.kyrixen.tinyblox.utils.Zipper;

public class ImportWorld {
    
    // Opens file explorer
    public static void openExplorer() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TinyBlox Worlds (*.tbworld)", "tbworld"));
        
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) importWorld(chooser.getSelectedFile().toPath().toString());

    }


    // Imports selected world
    public static void importWorld(String worldFile) {

        // Scan worlds
        List<String> worldNames = new ArrayList<>();
        List<String> worldPaths = FileManager.listDir(WorldManager.worldsFolder.path());
        
        for(String worldPath : worldPaths) {
            worldNames.add(FileManager.getEndpoint(worldPath));
        }

        String worldName = FileManager.getEndpoint(worldFile).replace(".tbworld", "");

        int copiesCount = 0;
        for(String wName : worldNames) { if(wName.equals(worldName) || wName.startsWith(worldName + "_")) copiesCount++; }

        if(copiesCount > 0) worldName += "_" + copiesCount;
        Zipper.unzipFile(worldFile, WorldManager.worldsFolder.path() + "/" + worldName);

    }

}
