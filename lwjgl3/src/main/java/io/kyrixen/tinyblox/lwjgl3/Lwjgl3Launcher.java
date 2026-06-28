package io.kyrixen.tinyblox.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.lwjgl3.desktopie.DesktopIE;
import io.kyrixen.tinyblox.lwjgl3.storage.DesktopFileManager;
import io.kyrixen.tinyblox.platform.Platform;


/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {

        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        setupPlatform();
        createApplication();
    
    }

    // Handles platform specific code
    private static void setupPlatform() {
        Platform.fileManager = new DesktopFileManager();
        Platform.worldIE = new DesktopIE();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("TinyBlox " + Constants.VERSION);
        
        configuration.useVsync(Constants.VSYNC);
        configuration.setForegroundFPS(Constants.FPS);
        configuration.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        configuration.setWindowIcon("tinyblox128.png", "tinyblox64.png", "tinyblox32.png", "tinyblox16.png");

        return configuration;
    
    }

}
