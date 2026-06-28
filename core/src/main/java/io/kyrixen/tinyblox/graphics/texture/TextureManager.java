package io.kyrixen.tinyblox.graphics.texture;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.kyrixen.tinyblox.graphics.texture.TextureID.TextureType;
import io.kyrixen.tinyblox.utils.Logger;

public class TextureManager {

    // List of loaded textures
    private final Map<TextureID, Texture> loadedTextures = new HashMap<>();
    private final Map<TextureID, Texture> loadedTextureOutlines = new HashMap<>();

    // Texture for missing texture
    private static final TextureID MISSING_TEXTURE = new TextureID("tinyblox", TextureType.MISC, "missing_texture");

    // Load texture
    public void load(TextureID identifier, String path) {
    
        Logger.LOGGER.debug("TEXTURES", "Loading: " + path);
    
        try {

            Pixmap pixmap = new Pixmap(Gdx.files.internal(path));

            Texture asset = new Texture(pixmap);
            asset.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loadedTextures.put(identifier, asset);

            if(identifier.getType() == TextureType.ENTITY || identifier.getType() == TextureType.TERRAIN) {
                Texture assetOutline = generateDepthOverlay(pixmap);
                loadedTextureOutlines.put(identifier, assetOutline);
            }
    
            pixmap.dispose();

        } catch(GdxRuntimeException e) {
            Logger.LOGGER.error("TEXTURES", "File not found: " + path);
        }

    }

    // Generates overlay for all tiles
    public Texture generateDepthOverlay(Pixmap source) {

        Pixmap overlay = new Pixmap(source.getWidth(), source.getHeight(), Pixmap.Format.RGBA8888);
        
        overlay.setColor(0f, 0f, 0f, 0f);
        overlay.fill();

        for(int x = 0; x < source.getWidth(); x++) {
            for(int y = 0; y < source.getHeight(); y++) {

                int pixel = source.getPixel(x, y);

                if((pixel & 0x000000ff) == 0) continue;

                overlay.setColor(1f, 1f, 1f, 1f);
                overlay.drawPixel(x, y);

            }
        }
        
        Texture result = new Texture(overlay);
        result.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        overlay.dispose();

        return result;

    }


    public Texture getTexture(TextureID identifier) {
    
        Texture asset = loadedTextures.get(identifier);

        if(asset == null) { Logger.LOGGER.error("TEXTURES", "Texture not loaded: " + identifier.toString());  return loadedTextures.get(MISSING_TEXTURE); }

        return asset;
    
    }
    
    public Texture getOutlineTexture(TextureID identifier) {
    
        Texture outline = loadedTextureOutlines.get(identifier);

        if(outline == null) { Logger.LOGGER.error("TEXTURES", "Texture overlay not loaded: " + identifier.toString());  return loadedTextures.get(MISSING_TEXTURE); }

        return outline;
    
    }
    
    
    // Load functions //

    // Load backgrounds
    public void loadBackgrounds() {
        this.load(new TextureID("tinyblox", TextureType.BACKGROUND, "selection_background"), "textures/background/selection_background.png");
        this.load(new TextureID("tinyblox", TextureType.BACKGROUND, "settings_background"), "textures/background/settings_background.png");
        this.load(new TextureID("tinyblox", TextureType.BACKGROUND, "menu_background"), "textures/background/menu_background.png");
    }



    // Load textures
    public void loadGame() {
    
        this.load(new TextureID("tinyblox", TextureType.MISC, "missing_texture"), "textures/misc/missing_texture.png");

        this.load(new TextureID("tinyblox", TextureType.ENTITY, "entity"), "textures/entities/entity.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "player"), "textures/entities/player.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "enemy"), "textures/entities/enemy.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "slime"), "textures/entities/slime.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "bomber"), "textures/entities/bomber.png");
        this.load(new TextureID("tinyblox", TextureType.ENTITY, "voidling"), "textures/entities/voidling.png");

        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "air"), "textures/terrain/air.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "grass"), "textures/terrain/grass.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "dirt"), "textures/terrain/dirt.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "water"), "textures/terrain/water.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "stone"), "textures/terrain/stone.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "iron_ore"), "textures/terrain/iron.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "coal_ore"), "textures/terrain/coal.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "wood"), "textures/terrain/wood.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "leaves"), "textures/terrain/leaves.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "ladder"), "textures/terrain/ladder.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "caged_lamp"), "textures/terrain/caged_lamp.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "slime_tile"), "textures/terrain/slime_tile.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "sand"), "textures/terrain/sand.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "glass"), "textures/terrain/glass.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "clay"), "textures/terrain/clay.png");
        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "brick"), "textures/terrain/brick.png");

        this.load(new TextureID("tinyblox", TextureType.TERRAIN, "terrain_tiles"), "textures/terrain/terrain.png");
    
    }

    public void loadHUD() {

        this.load(new TextureID("tinyblox", TextureType.HUD, "hotbar_slot"), "textures/hud/inventory/hotbar_slot.png");

        this.load(new TextureID("tinyblox", TextureType.HUD, "wooden_sword"), "textures/hud/inventory/items/wood_sword.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "stone_sword"), "textures/hud/inventory/items/stone_sword.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "iron_sword"), "textures/hud/inventory/items/iron_sword.png");

        this.load(new TextureID("tinyblox", TextureType.HUD, "wooden_pickaxe"), "textures/hud/inventory/items/wood_pickaxe.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "stone_pickaxe"), "textures/hud/inventory/items/stone_pickaxe.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "iron_pickaxe"), "textures/hud/inventory/items/iron_pickaxe.png");

        this.load(new TextureID("tinyblox", TextureType.HUD, "wooden_axe"), "textures/hud/inventory/items/wood_axe.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "stone_axe"), "textures/hud/inventory/items/stone_axe.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "iron_axe"), "textures/hud/inventory/items/iron_axe.png");

        this.load(new TextureID("tinyblox", TextureType.HUD, "crafting_menu_container"), "textures/hud/inventory/crafting_menu/container.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "crafting_menu_arrow"), "textures/hud/inventory/crafting_menu/arrow.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_hover"), "textures/hud/inventory/crafting_menu/button_hover.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_idle"), "textures/hud/inventory/crafting_menu/button_idle.png");
        this.load(new TextureID("tinyblox", TextureType.HUD, "crafting_menu_button_selected"), "textures/hud/inventory/crafting_menu/button_selected.png");

    }

    public void loadUI() {

        this.load(new TextureID("tinyblox", TextureType.UI, "world_slot"), "textures/ui/misc/world_slot.png");
        
        this.load(new TextureID("tinyblox", TextureType.UI, "dialog_corner"), "textures/ui/dialog/dialog_corner.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"dialog_side"), "textures/ui/dialog/dialog_side.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"dialog_center"), "textures/ui/dialog/dialog_center.png");

        this.load(new TextureID("tinyblox", TextureType.UI,"brown_button"), "textures/ui/button/brown_button.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"gray_button"), "textures/ui/button/gray_button.png");
        this.load(new TextureID("tinyblox", TextureType.UI,"red_button"), "textures/ui/button/red_button.png");

        this.load(new TextureID("tinyblox", TextureType.UI,"white_toggle_button"), "textures/ui/button/white_toggle_button.png");
        
        this.load(new TextureID("tinyblox", TextureType.UI,"white_slider"), "textures/ui/slider/white_slider.png");
    
    }

    // Cleanup resources
    public void cleanup() {

        for(Texture tex : loadedTextures.values()) { tex.dispose(); }
        for(Texture out : loadedTextureOutlines.values()) { out.dispose(); }

        // Clear loaded textures list
        loadedTextures.clear();
        loadedTextureOutlines.clear();

    }
 
}
