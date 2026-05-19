package io.kyrixen.tinyblox.graphics.texture;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.Logger;

public class TextureManager {

    // List of loaded textures
    private final Map<TextureID, Texture> loadedTextures = new HashMap<>();


    // Load texture
    public void load(TextureID identifier, String path) {
    
        Logger.LOGGER.debug("TEXTURES", "Loading: " + path);
    
        try {

            Texture asset = new Texture(path);
            if(loadedTextures.containsKey(identifier)) { Logger.LOGGER.error("TEXTURES", "ID already registered!: " + identifier.toString()); return; }

            loadedTextures.put(identifier, asset);

        } catch(GdxRuntimeException e) {
            Logger.LOGGER.error("TEXTURES", "File not found: " + path);
        }

    }

    public Texture getTexture(TextureID identifier) {
    
        Texture asset = loadedTextures.get(identifier);

        if(asset == null) Logger.LOGGER.error("TEXTURES", "Texture not loaded: " + identifier.toString());

        return asset;
    
    }

    
    // Load functions //

    // Load backgrounds
    public void loadBackgrounds(){
        this.load(new TextureID("tinyblox", TextureType.BACKGROUND, "settings_background"), "textures/background/settings_background.png");
        this.load(new TextureID("tinyblox", TextureType.BACKGROUND, "menu_background"), "textures/background/menu_background.png");
    }



    // Load textures
    public void loadGame() {
    
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "entity"), "textures/entities/entity.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "player"), "textures/entities/player.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "enemy"), "textures/entities/enemy.png");

        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "air"), "textures/terrain/air.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "grass"), "textures/terrain/grass.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "dirt"), "textures/terrain/dirt.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "water"), "textures/terrain/water.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "stone"), "textures/terrain/stone.png");
        
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles"), "textures/terrain/terrain.png");
    
    }

    public void loadHUD() {

        this.load(new TextureID("tinyblox", TextureType.HUD, "hotbar_slot"), "textures/hud/inventory/hotbar_slot.png");

    }

    public void loadUI() {

        this.load(new TextureID("tinyblox", TextureType.UI, "dialog_corner"), "textures/ui/dialog/dialog_corner.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"dialog_side"), "textures/ui/dialog/dialog_side.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"dialog_center"), "textures/ui/dialog/dialog_center.png");

        this.load(new TextureID("tinyblox", TextureType.UI,"brown_button"), "textures/ui/button/brown_button.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"gray_button"), "textures/ui/button/gray_button.png");
        
        this.load(new TextureID("tinyblox", TextureType.UI,"white_toggle_button"), "textures/ui/button/white_toggle_button.png");
        
        this.load(new TextureID("tinyblox", TextureType.UI,"white_slider"), "textures/ui/slider/white_slider.png");
    
    }


    // Cleanup resources
    public void cleanup() {

        for(Texture tex : loadedTextures.values()) { tex.dispose(); }

        // Clear loaded textures list
        loadedTextures.clear(); // removes references to Textures

    }
 
}
