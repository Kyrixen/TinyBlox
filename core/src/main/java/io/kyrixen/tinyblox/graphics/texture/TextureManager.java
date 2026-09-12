package io.kyrixen.tinyblox.graphics.texture;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.utils.Logger;

public class TextureManager {

    // List of loaded textures
    private final Map<TinyIdentifier, Texture> loadedTextures = new HashMap<>();
    private final Map<TinyIdentifier, Texture> loadedTextureOutlines = new HashMap<>();

    // Texture for missing texture
    private static final TinyIdentifier MISSING_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "missing_texture");

    // Load texture
    public void load(TinyIdentifier identifier, String path) {
    
        Logger.LOGGER.debug("TEXTURES", "Loading: " + path);
    
        try {

            Pixmap pixmap = new Pixmap(Gdx.files.internal(path));

            Texture asset = new Texture(pixmap);
            asset.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loadedTextures.put(identifier, asset);

            if(identifier.getType() == IdentifierType.TEXTURE || identifier.getType() == IdentifierType.TEXTURE) {
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


    public Texture getTexture(TinyIdentifier identifier) {
    
        Texture asset = loadedTextures.get(identifier);

        if(asset == null) { Logger.LOGGER.error("TEXTURES", "Texture not loaded: " + identifier.toString());  return loadedTextures.get(MISSING_TEXTURE); }

        return asset;
    
    }
    
    public Texture getOutlineTexture(TinyIdentifier identifier) {
    
        Texture outline = loadedTextureOutlines.get(identifier);

        if(outline == null) { Logger.LOGGER.error("TEXTURES", "Texture overlay not loaded: " + identifier.toString());  return loadedTextures.get(MISSING_TEXTURE); }

        return outline;
    
    }
    
    
    // Load functions //

    // Load backgrounds
    public void loadBackgrounds() {
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "selection_background"), "tinyblox/textures/background/selection_background.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "settings_background"), "tinyblox/textures/background/settings_background.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "menu_background"), "tinyblox/textures/background/menu_background.png");
    }



    // Load textures
    public void loadGame() {
    
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "missing_texture"), "tinyblox/textures/misc/missing_texture.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "entity"), "tinyblox/textures/entities/entity.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "player"), "tinyblox/textures/entities/player.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "enemy"), "tinyblox/textures/entities/enemy.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime"), "tinyblox/textures/entities/slime.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "bomber"), "tinyblox/textures/entities/bomber.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "voidling"), "tinyblox/textures/entities/voidling.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "air"), "tinyblox/textures/terrain/air.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "grass"), "tinyblox/textures/terrain/grass.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dirt"), "tinyblox/textures/terrain/dirt.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "water"), "tinyblox/textures/terrain/water.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone"), "tinyblox/textures/terrain/stone.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_ore"), "tinyblox/textures/terrain/iron.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "coal_ore"), "tinyblox/textures/terrain/coal.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wood"), "tinyblox/textures/terrain/wood.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "leaves"), "tinyblox/textures/terrain/leaves.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "ladder"), "tinyblox/textures/terrain/ladder.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "caged_lamp"), "tinyblox/textures/terrain/caged_lamp.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime_tile"), "tinyblox/textures/terrain/slime_tile.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "sand"), "tinyblox/textures/terrain/sand.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "glass"), "tinyblox/textures/terrain/glass.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "clay"), "tinyblox/textures/terrain/clay.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "brick"), "tinyblox/textures/terrain/brick.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), "tinyblox/textures/terrain/terrain.png");
    
    }

    public void loadHUD() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "hotbar_slot"), "tinyblox/textures/hud/inventory/hotbar_slot.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_sword"), "tinyblox/textures/hud/inventory/items/wood_sword.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_sword"), "tinyblox/textures/hud/inventory/items/stone_sword.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_sword"), "tinyblox/textures/hud/inventory/items/iron_sword.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_pickaxe"), "tinyblox/textures/hud/inventory/items/wood_pickaxe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_pickaxe"), "tinyblox/textures/hud/inventory/items/stone_pickaxe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_pickaxe"), "tinyblox/textures/hud/inventory/items/iron_pickaxe.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_axe"), "tinyblox/textures/hud/inventory/items/wood_axe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_axe"), "tinyblox/textures/hud/inventory/items/stone_axe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_axe"), "tinyblox/textures/hud/inventory/items/iron_axe.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_container"), "tinyblox/textures/hud/inventory/crafting_menu/container.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_arrow"), "tinyblox/textures/hud/inventory/crafting_menu/arrow.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_hover"), "tinyblox/textures/hud/inventory/crafting_menu/button_hover.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_idle"), "tinyblox/textures/hud/inventory/crafting_menu/button_idle.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_selected"), "tinyblox/textures/hud/inventory/crafting_menu/button_selected.png");

    }

    public void loadUI() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "world_slot"), "tinyblox/textures/ui/misc/world_slot.png");
        
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dialog_corner"), "tinyblox/textures/ui/dialog/dialog_corner.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"dialog_side"), "tinyblox/textures/ui/dialog/dialog_side.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"dialog_center"), "tinyblox/textures/ui/dialog/dialog_center.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"brown_button"), "tinyblox/textures/ui/button/brown_button.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"gray_button"), "tinyblox/textures/ui/button/gray_button.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"red_button"), "tinyblox/textures/ui/button/red_button.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"white_toggle_button"), "tinyblox/textures/ui/button/white_toggle_button.png");
        
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"white_slider"), "tinyblox/textures/ui/slider/white_slider.png");
    
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
