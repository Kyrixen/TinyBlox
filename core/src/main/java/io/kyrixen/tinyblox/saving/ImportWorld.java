package io.kyrixen.tinyblox.saving;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Logger;

public class ImportWorld {
    
    // Opens file explorer
    public static void openExplorer() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TinyBlox Worlds (*.tbworld)", "tbworld"));
        
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) importWorld(chooser.getSelectedFile().toPath());

    }


    // Imports selected world
    public static void importWorld(Path worldFile) {

        List<String> worldNames = new ArrayList<>();
        try {

            List<Path> worldPaths = new ArrayList<>();
            try(Stream<Path> stream = Files.list(Paths.get(WorldManager.worldsFolder.path()))) {
                worldPaths.addAll(stream.collect(Collectors.toList()));
            }

            for(Path worldPath : worldPaths) {
                worldNames.add(worldPath.getFileName().toString());
            }

        } catch (IOException e) { Logger.LOGGER.error("SCANNER", "Failed to scan worlds: " + e); }

        String worldName = worldFile.getFileName().toString().replace(".tbworld", "");

        int copiesCount = 0;
        for(String wName : worldNames) { if(wName.equals(worldName) || wName.startsWith(worldName + "_")) copiesCount++; }

        if(copiesCount > 0) worldName += "_" + copiesCount;
        unzipFile(worldFile, Paths.get(WorldManager.worldsFolder + "/" + worldName));
        
    }

    // Unzip helper
    private static void unzipFile(Path zipFile, Path targetFolder) {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                Path output = targetFolder.resolve(entry.getName());

                Files.createDirectories(output.getParent());
                Files.copy(zis, output, StandardCopyOption.REPLACE_EXISTING);

                zis.closeEntry();

            }

        } catch(IOException e) { Logger.LOGGER.error("UNZIP", "Failed to unzip " + zipFile.toString() + ": " + e); }
    
    }

}
