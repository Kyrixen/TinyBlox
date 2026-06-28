package io.kyrixen.tinyblox.teavm.webie;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.Uint8Array;

import io.kyrixen.tinyblox.saving.world.WorldManager;
import io.kyrixen.tinyblox.utils.Zipper;

public class WebExportWorld {

    // Download web file native method
    @JSBody(params = { "filename", "data" }, script =
            "const blob = new Blob([data]);" +
            "const url = URL.createObjectURL(blob);" +
            "const a = document.createElement('a');" +
            "a.href = url;" +
            "a.download = filename;" +
            "a.click();" +
            "URL.revokeObjectURL(url);"
    )
    private static native void downloadNative(String filename, Uint8Array data);


    // Opens file explorer
    static void openExplorer(String worldName) {
        
        String worldDirName = worldName.toLowerCase().replace(" ", "_");        
        byte[] zip = exportWorld(worldDirName);

        Uint8Array array = new Uint8Array(zip.length);
        for(int i = 0; i < zip.length; i++) { array.set(i, (short) (zip[i] & 0xFF)); }
        downloadNative(worldDirName + ".tbworld", array);

    }

    // Exports selected world
    private static byte[] exportWorld(String worldDirName) {
        String worldFolder = WorldManager.worldsFolder.path() + "/" + worldDirName;
        return Zipper.zipFolder(worldFolder);
    }

}
