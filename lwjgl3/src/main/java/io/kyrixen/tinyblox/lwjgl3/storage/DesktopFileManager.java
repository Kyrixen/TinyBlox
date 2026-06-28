package io.kyrixen.tinyblox.lwjgl3.storage;

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

import io.kyrixen.tinyblox.platform.FileManager;
import io.kyrixen.tinyblox.utils.Logger;

// File manager class
public class DesktopFileManager implements FileManager {
    
    @Override
    // Writes file
    public void writeFile(String filePath, String data) {

        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt write a file " + filePath + ": " + e); }

    }

    @Override
    // Writes bytes to file
    public void writeBytes(String filePath, byte[] data) {
    
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            Files.write(Paths.get(filePath), data);   
        } catch (Exception e) { Logger.LOGGER.error("FILE", "Couldnt write file bytes " + filePath + ": " + e); }
    
    }

    @Override
    // Reads file
    public String readFile(String filePath) {

        String fileData = null;
        
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            fileData = new String(bytes);
        } catch (IOException e) {}

        return fileData;

    }

    @Override
    // Reads bytes from file
    public byte[] readBytes(String filePath) {

        byte[] byteData = null;

        try {
            byteData = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {}

        return byteData;

    }

    @Override
    // Deletes file
    public void deleteFile(String filePath) {

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete a file " + filePath + ": " + e); }

    }

    @Override
    // Delete entire dir
    public void deleteDir(String dirPath) {

        try {

            Files.walk(Paths.get(dirPath)).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete file " + path.getFileName().toString() + ": " + e); }
            });

        } catch (IOException e) { Logger.LOGGER.error("FILE", "Couldnt delete dir " + dirPath + ": " + e); }

    }

    @Override
    // Creates dir
    public void createDir(String dirPath) {

        try { 
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Cannot create dir" + dirPath + ": " + e); }

    }

    @Override
    // List dir contents
    public List<String> listDir(String dirString) {

        List<String> contents = new ArrayList<>();

        try (Stream<Path> stream = Files.list(Paths.get(dirString))) {
            contents.addAll(stream.map(Path::toString).collect(Collectors.toList()));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Failed to scan dir " + dirString + ": " + e); }

        return contents;
    }

    @Override
    // List dir contents recursive
    public List<String> listDirRecursive(String dirString) {

        List<String> contents = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(dirString))) {
            contents.addAll(stream.skip(1).map(Path::toString).collect(Collectors.toList()));
        } catch (IOException e) { Logger.LOGGER.error("FILE", "Failed to scan dir " + dirString + ": " + e); }

        return contents;
    }

    @Override
    // If direcotry
    public boolean isDir(String path) {
        return Files.isDirectory(Paths.get(path));
    }

    @Override
    // Get the endpoint
    public String getEndpoint(String path) {
        return Paths.get(path).getFileName().toString();
    }

}
