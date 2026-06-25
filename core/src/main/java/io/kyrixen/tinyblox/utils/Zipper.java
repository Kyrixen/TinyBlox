package io.kyrixen.tinyblox.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {

    // Zips folder
    public static void zipFolder(String sourceFolder, String zipFile) {

        try {

            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Paths.get(zipFile).toFile()))) {
                Files.walk(Paths.get(sourceFolder)).skip(1).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                    
                    ZipEntry zipper = new ZipEntry(Paths.get(sourceFolder).relativize(path).toString());
                    try {
                        
                        zos.putNextEntry(zipper);
                        Files.copy(path, zos);
                        zos.closeEntry();

                    } catch (IOException e) { Logger.LOGGER.error("ZIPPER", "Error while zipping file " + path.getFileName().toString() + ": " + e); }

                });
            }

        } catch(IOException e) { Logger.LOGGER.error("ZIPPER", "Error while zipping folder " + sourceFolder.toString() + ": " + e); }
    
    }

    // Unzips file
    public static void unzipFile(String zipFile, String targetFolder) {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(Paths.get(zipFile).toFile()))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                Path output = Paths.get(targetFolder).resolve(entry.getName()).normalize();
                if (!output.startsWith(targetFolder)) throw new IOException("Invalid zip entry");

                Files.createDirectories(output.getParent());
                Files.copy(zis, output, StandardCopyOption.REPLACE_EXISTING);

                zis.closeEntry();

            }

        } catch(IOException e) { Logger.LOGGER.error("ZIPPER", "Failed to unzip " + zipFile.toString() + ": " + e); }
    
    }

}
