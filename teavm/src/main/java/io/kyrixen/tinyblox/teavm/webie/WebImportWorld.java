package io.kyrixen.tinyblox.teavm.webie;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Uint8Array;

import io.kyrixen.tinyblox.menu.selection.WorldList;
import io.kyrixen.tinyblox.menu.selection.WorldListScanner;
import io.kyrixen.tinyblox.platform.Platform;
import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Zipper;

public class WebImportWorld {
    
    // Callback for selected file
    private interface FileImportCallback extends JSObject {
        void accept(String filename, Uint8Array data);
    }

    // Opens file explorer
    @JSBody(params = "callback", script =
        "const input = document.createElement('input');" +
        "input.type='file';" +
        "input.accept='.tbworld';" +
        "input.onchange = () => {" +
        "  const file = input.files[0];" +
        "  if (!file) return;" +
        "  file.arrayBuffer().then(buffer => {" +
        "    callback.accept(file.name, new Uint8Array(buffer));" +
        "  });" +
        "};" +
        "input.click();"
    )
    private static native void openFile(FileImportCallback callback);


    // Opens file explorer
    static void openExplorer(WorldList worldList) {

        openFile((filename, data) -> {

            byte[] bytes = new byte[data.getLength()];
            for (int i = 0; i < bytes.length; i++) { bytes[i] = (byte) data.get(i); }

            importWorld(filename, bytes);

            worldList.updateWorldSlots(WorldListScanner.getWorlds());
        
        });

    }

    // Imports selected world
    private static void importWorld(String worldFile, byte[] data) {

        List<String> worldNames = new ArrayList<>();
        List<String> worldPaths = Platform.fileManager.listDir(WorldManager.worldsFolder.path());
        
        for(String worldPath : worldPaths) {
            worldNames.add(Platform.fileManager.getEndpoint(worldPath));
        }
        
        String worldName = Platform.fileManager.getEndpoint(worldFile).replace(".tbworld", "");

        int copiesCount = 0;
        for(String wName : worldNames) { if(wName.equals(worldName) || wName.startsWith(worldName + "_")) copiesCount++; }

        if(copiesCount > 0) worldName += "_" + copiesCount;
        Zipper.unzipFile(data, WorldManager.worldsFolder.path() + "/" + worldName);

    }

}
