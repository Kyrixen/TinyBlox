package io.kyrixen.tinyblox.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.teavm.tooling.TeaVMSourceFilePolicy;
import org.teavm.tooling.sources.DirectorySourceFileProvider;
import org.teavm.vm.TeaVMOptimizationLevel;

/** Builds the TeaVM/HTML application. */
public class TeaVMBuilder {
    
    public static void main(String[] args) {

        boolean jetty = false;
        for (String arg : args) {
            if ("run".equals(arg)) { jetty = true; break; }
        }

        File output = new File("build/dist");

        new TeaCompiler(
            new WebBackend()    
            .setHtmlWidth(Constants.WINDOW_WIDTH)
            .setHtmlHeight(Constants.WINDOW_HEIGHT)
            .setHtmlTitle("TinyBlox " + Constants.VERSION)
//          .setWebAssembly(true)
            .setStartJettyAfterBuild(jetty)
            .setJettyPort(8080)
        )
        .addAssets(new AssetFileHandle("../assets"))    
        .setOptimizationLevel(Constants.DEBUG ? TeaVMOptimizationLevel.SIMPLE : TeaVMOptimizationLevel.ADVANCED)
        .setMainClass(TeaVMLauncher.class.getName())
        .setObfuscated(Constants.DEBUG)
        .setDebugInformationGenerated(Constants.DEBUG)
        .setSourceMapsFileGenerated(Constants.DEBUG)
        .setSourceFilePolicy(TeaVMSourceFilePolicy.COPY)
        .addSourceFileProvider(new DirectorySourceFileProvider(new File("../core/src/main/java/")))
        
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.world.WorldBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.world.ChunkBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.world.EntityChunkBlueprint")

        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.entities.EntityBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.entities.MobEntityBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.entities.PlayerBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.entities.EnemyBlueprint")

        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.InventoryBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.RecipeBlueprint")
        .addReflectionClass("io.kyrixen.tinyblox.saving.blueprints.StructureBlueprint")

        .build(output);

        // Patch generated index.html
        try {

            Path html = Paths.get(output.getPath() + "/webapp/index.html");
            String text = Files.readString(html);

            text = text.replace("</body>", "<script src=\"wheel.js\"></script>\n</body>");
            Files.writeString(html, text);
        
        } catch(IOException e) { Logger.LOGGER.error("HTML", "Couldnt apply patch: " + e); }

        // Copy patch
        try {
            Files.copy(Paths.get("src/main/resources/wheel.js"), Paths.get(output.getPath() + "/webapp/wheel.js"), StandardCopyOption.REPLACE_EXISTING);        
        } catch (IOException e) { Logger.LOGGER.error("HTML", "Couldnt copy patch: " + e); }
    
    }

}