package io.kyrixen.tinyblox.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// File manager class
public class FileManager {
    
    // Writes file
    public static void writeFile(String filePath, String data) {

        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt write a file " + filePath + ": " + e); }

    }

    // Reads file
    public static String readFile(String filePath) {

        String fileData = null;
        
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            fileData = new String(bytes);
        } catch (IOException e) {}

        return fileData;

    }

    // Deletes file
    public static void deleteFile(String filePath) {

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete a file " + filePath + ": " + e); }

    }

    // Delete entire dir
    public static void deleteDir(String dirPath) {

        try {

            Files.walk(Paths.get(dirPath)).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete file " + path.getFileName().toString() + ": " + e); }
            });

        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete dir " + dirPath + ": " + e); }

    }

    // Creates dir
    public static void createDir(String dirPath) {

        try { 
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Cannot create dir" + dirPath + ": " + e); }

    }

    // List dir contents
    public static List<String> listDir(String dirString) {

        List<String> contents = new ArrayList<>();

        try {

            try(Stream<Path> stream = Files.list(Paths.get(dirString))) {
               contents.addAll(stream.map(Path::toString).collect(Collectors.toList()));
            }
        
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Failed to scan dir" + dirString + ": " + e); }

        return contents;

    }

    // If direcotry
    public static boolean isDir(String path) {
        return Files.isDirectory(Paths.get(path));
    }

    // Get the endpoint
    public static String getEndpoint(String path) {
        return Paths.get(path).getFileName().toString();
    }

}
