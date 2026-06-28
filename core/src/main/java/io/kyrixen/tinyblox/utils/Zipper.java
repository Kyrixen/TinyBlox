package io.kyrixen.tinyblox.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.kyrixen.tinyblox.platform.Platform;

public class Zipper {

    // Zips folder
    public static byte[] zipFolder(String sourceFolder) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
        
            ZipOutputStream zos = new ZipOutputStream(out);

            for (String path : Platform.fileManager.listDirRecursive(sourceFolder)) {

                if (Platform.fileManager.isDir(path)) continue;

                String relative = path.substring(sourceFolder.length() + 1);

                zos.putNextEntry(new ZipEntry(relative));
                zos.write(Platform.fileManager.readBytes(path));
                zos.closeEntry();

            }

            zos.close();
            
        } catch(IOException e) { Logger.LOGGER.error("ZIPPER", "Couldnt zip " + sourceFolder + ": " + e); }

        return out.toByteArray();

    }

    // Unzips file
    public static void unzipFile(byte[] zip, String targetFolder) {

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {

            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {

                String output = targetFolder + "/" + entry.getName();

                if (entry.isDirectory()) Platform.fileManager.createDir(output);
                else {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zis.read(buffer)) != -1) { bos.write(buffer, 0, len); }

                    byte[] bytes = bos.toByteArray();
                    Platform.fileManager.writeBytes(output, bytes);
                
                }

                zis.closeEntry();
                entry = zis.getNextEntry();

            }

        } catch(IOException e) { Logger.LOGGER.error("ZIPPER", "Failed to unzip to " + targetFolder + ": " + e); }
    
    }

}
