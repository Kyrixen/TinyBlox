package io.kyrixen.tinyblox.saving;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Zipper;

public class ExportWorld {

    // Opens file explorer
    public static void openExplorer(String worldString) {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(worldString.toLowerCase().replace(" ", "_") + ".tbworld"));
        chooser.setFileFilter(new FileNameExtensionFilter("TinyBlox Worlds (*.tbworld)", "tbworld"));
        
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) exportWorld(chooser.getSelectedFile().toPath().toString(), worldString);

    }


    // Exports selected world
    public static void exportWorld(String exportPath, String worldName) {

        if(!exportPath.endsWith(".tbworld")) exportPath = exportPath + ".tbworld";

        String worldDirName = worldName.toLowerCase().replace(" ", "_");
        String worldFolder = WorldManager.worldsFolder.path() + "/" + worldDirName;
        
        Zipper.zipFolder(worldFolder, exportPath);

    }

}
