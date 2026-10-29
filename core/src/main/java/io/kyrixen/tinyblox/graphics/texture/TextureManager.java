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
    public void load(TinyIdentifier identifier, String file) {
    
        String path = identifier.getNamespace() + "/textures/" + file;

        Logger.LOGGER.debug("TEXTURES", "Loading: " + path);
    
        try {

            Pixmap pixmap = new Pixmap(Gdx.files.internal(path));

            Texture asset = new Texture(pixmap);
            asset.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loadedTextures.put(identifier, asset);

            if(path.contains("entities") || path.contains("terrain")) {
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
        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "selection_background"), "background/selection_background.png");
        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "settings_background"), "background/settings_background.png");
        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "menu_background"), "background/menu_background.png");
    }



    // Load textures
    public void loadGame() {
    
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "missing_texture"), "misc/missing_texture.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "entity"), "entities/entity.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "player"), "entities/player.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "enemy"), "entities/enemy.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime"), "entities/slime.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "bomber"), "entities/bomber.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "voidling"), "entities/voidling.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "npc"), "entities/npc.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "air"), "terrain/air.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "grass"), "terrain/grass.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dirt"), "terrain/dirt.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "water"), "terrain/water.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone"), "terrain/stone.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_ore"), "terrain/iron.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "coal_ore"), "terrain/coal.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wood"), "terrain/wood.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "leaves"), "terrain/leaves.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "ladder"), "terrain/ladder.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "caged_lamp"), "terrain/caged_lamp.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "slime_tile"), "terrain/slime_tile.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "sand"), "terrain/sand.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "glass"), "terrain/glass.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "clay"), "terrain/clay.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "brick"), "terrain/brick.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "terrain_tiles"), "terrain/terrain.png");

        // Halloween //

        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "pumpkin_tiles"), "terrain/pumpkin_tiles.png");
        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "pumpkin_tile"), "terrain/pumpkin_tile.png");
        this.load(new TinyIdentifier("tinyblox_halloween", IdentifierType.TEXTURE, "angry_pumpkin"), "entities/angry_pumpkin.png");

    }

    public void loadHUD() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "hotbar_slot"), "hud/inventory/hotbar_slot.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_sword"), "hud/inventory/items/wood_sword.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_sword"), "hud/inventory/items/stone_sword.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_sword"), "hud/inventory/items/iron_sword.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_pickaxe"), "hud/inventory/items/wood_pickaxe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_pickaxe"), "hud/inventory/items/stone_pickaxe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_pickaxe"), "hud/inventory/items/iron_pickaxe.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "wooden_axe"), "hud/inventory/items/wood_axe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_axe"), "hud/inventory/items/stone_axe.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "iron_axe"), "hud/inventory/items/iron_axe.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "health_bar"), "hud/health_bar.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stamina_bar"), "hud/stamina_bar.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_container"), "hud/inventory/crafting_menu/container.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_arrow"), "hud/inventory/crafting_menu/arrow.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_hover"), "hud/inventory/crafting_menu/button_hover.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_idle"), "hud/inventory/crafting_menu/button_idle.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "crafting_menu_button_selected"), "hud/inventory/crafting_menu/button_selected.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "breaking_tile_anim"), "hud/breaking_tile.png");

    }

    public void loadUI() {

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "world_slot"), "ui/misc/world_slot.png");
        
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "dialog_corner"), "ui/dialog/dialog_corner.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"dialog_side"), "ui/dialog/dialog_side.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"dialog_center"), "ui/dialog/dialog_center.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "popup_window"), "ui/dialog/popup_window.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"brown_button"), "ui/button/brown_button.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"gray_button"), "ui/button/gray_button.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"red_button"), "ui/button/red_button.png");
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"green_button"), "ui/button/green_button.png");

        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"white_toggle_button"), "ui/button/white_toggle_button.png");
        
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"white_slider"), "ui/slider/white_slider.png");
    
        this.load(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE,"stone_dialog"), "ui/dialog/stone_dialog.png");

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
