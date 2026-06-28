package io.kyrixen.tinyblox.teavm.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.kyrixen.tinyblox.platform.FileManager;

import java.util.List;
import java.util.ArrayList;

public class WebFileManager implements FileManager {
    
    @Override
    // Writes file
    public void writeFile(String filePath, String data) {
        Gdx.files.local(filePath).parent().mkdirs();
        Gdx.files.local(filePath).writeString(data, false);
    }

    @Override
    // Writes bytes to file
    public void writeBytes(String filePath, byte[] data) {
        Gdx.files.local(filePath).parent().mkdirs();
        Gdx.files.local(filePath).writeBytes(data, false);    
    }

    @Override
    // Reads file
    public String readFile(String filePath) {

        FileHandle file = Gdx.files.local(filePath);
        if (!file.exists()) return null;

        return file.readString();

    }

    @Override
    // Reads bytes from file
    public byte[] readBytes(String filePath) {

        FileHandle file = Gdx.files.local(filePath);
        if (!file.exists()) return null;

        return file.readBytes();

    }

    @Override
    // Deletes file
    public void deleteFile(String filePath) {
        Gdx.files.local(filePath).delete();
    }

    @Override
    // Delete entire dir
    public void deleteDir(String dirPath) {
        Gdx.files.local(dirPath).deleteDirectory();
    }

    @Override
    // Creates dir
    public void createDir(String dirPath) {
        Gdx.files.local(dirPath).mkdirs();
    }

    @Override
    // List dir contents
    public List<String> listDir(String dirString) {

        List<String> dirList = new ArrayList<>();

        FileHandle[] files = Gdx.files.local(dirString).list();
        for(FileHandle file : files) { dirList.add(file.path()); }

        return dirList;

    }

    @Override
    // List dir contents recursive
    public List<String> listDirRecursive(String dirString) {

        List<String> files = new ArrayList<>();
        scanRecursive(Gdx.files.local(dirString), files);

        return files;

    }

    @Override
    // If direcotry
    public boolean isDir(String path) {
        return Gdx.files.local(path).isDirectory();
    }

    @Override
    // Get the endpoint
    public String getEndpoint(String path) {
        
        String p = Gdx.files.local(path).path();
        int index = p.lastIndexOf('/');
        
        if (index == -1) return p;
        return p.substring(index + 1);

    }

    private void scanRecursive(FileHandle dir, List<String> files) {

        for (FileHandle child : dir.list()) {
            files.add(child.path());
            if (child.isDirectory()) scanRecursive(child, files);
        }

    }

}
