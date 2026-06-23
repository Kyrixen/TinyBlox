package io.kyrixen.tinyblox.saving;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;

import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class ExportWorld {

    // Opens file explorer
    public static void openExplorer(String worldString) {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(worldString.toLowerCase().replace(" ", "_") + ".tbworld"));

        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) exportWorld(chooser.getSelectedFile().toPath(), worldString);

    }


    // Exports selected world
    public static void exportWorld(Path exportPath, String worldName) {

        if(!exportPath.toString().endsWith(".tbworld")) exportPath = Paths.get(exportPath.toString() + ".tbworld");

        String worldDirName = worldName.toLowerCase().replace(" ", "_");
        String worldFolder = WorldManager.worldsFolder + "/" + worldDirName;
        
        try {
            zipFolder(Paths.get(worldFolder), exportPath);  
        } catch (IOException e) { Logger.LOGGER.error("EXPORTER", "Couldnt export world " + worldName + ": " + e); }
    
    }

    // Zip helper
    private static void zipFolder(Path sourceFolder, Path zipFile) throws IOException {

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile.toFile()))) {
            Files.walk(sourceFolder).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                
                ZipEntry zipper = new ZipEntry(sourceFolder.relativize(path).toString());
                try {
                    
                    zos.putNextEntry(zipper);
                    Files.copy(path, zos);
                    zos.closeEntry();

                } catch (IOException e) { Logger.LOGGER.error("ZIP", "Errow while zipping " + sourceFolder.toString() + ": " + e); }
    
            });
        }
    
    }

}
