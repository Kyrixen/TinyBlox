package io.kyrixen.tinyblox.graphics.animation;

import java.util.HashMap;
import java.util.Map;

import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;


public class AnimationManager {
   
    // List of loaded textures
    private final Map<TinyIdentifier, Animation> loadedAnimations = new HashMap<>();


    // Load animation
    public void load(TinyIdentifier identifier, Animation animation) {
        if(loadedAnimations.containsKey(identifier)) return;
        loadedAnimations.put(identifier, animation);
    }

    // Get animation from identifier
    public Animation getAnimation(TinyIdentifier identifier) {
        return loadedAnimations.get(identifier);
    }

    public void loadAnimations(TextureManager textureManager) {

        if(!loadedAnimations.isEmpty()) return;

        this.load(new TinyIdentifier("tinyblox", IdentifierType.ANIMATION, "breaking_tile"), new Animation(textureManager.getTexture(new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "breaking_tile_anim")), 5, 1, 5));

    }

}
