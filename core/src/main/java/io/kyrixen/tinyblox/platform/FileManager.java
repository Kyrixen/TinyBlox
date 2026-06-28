package io.kyrixen.tinyblox.platform;

import java.util.List;

public interface FileManager {
  
    // Writes file
    public void writeFile(String filePath, String data);

    // Writes bytes to file
    public void writeBytes(String filePath, byte[] data);

    // Reads file
    public String readFile(String filePath);

    // Reads bytes from file
    public byte[] readBytes(String filePath);

    // Deletes file
    public void deleteFile(String filePath);

    // Delete entire dir
    public void deleteDir(String dirPath);

    // Creates dir
    public void createDir(String dirPath);

    // List dir contents
    public List<String> listDir(String dirString);

    // List all dir contents
    public List<String> listDirRecursive(String dirPath);

    // If direcotry
    public boolean isDir(String path);

    // Get the endpoint
    public String getEndpoint(String path);

}
