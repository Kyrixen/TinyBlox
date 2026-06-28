package io.kyrixen.tinyblox.teavm;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import io.kyrixen.tinyblox.Main;
import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.teavm.storage.WebFileManager;
import io.kyrixen.tinyblox.teavm.webie.WebIE;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {

    public static void main(String[] args) {
        
        setupPlatform();
        
        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        config.width = 800;
        config.height = 600;
        
        new WebApplication(new Main(), config);

    }

    private static void setupPlatform() {
        Platform.fileManager = new WebFileManager();
        Platform.worldIE = new WebIE();
    }

}