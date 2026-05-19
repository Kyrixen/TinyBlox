package io.kyrixen.tinyblox.graphics.texture;

import java.util.Objects;

public final class TextureID {
    
    // Texture type enum helper
    public enum TextureType {
    
        UI,
        HUD,
        ENTITY,
        TERRAIN,
        BACKGROUND,
        MISC
    
    }

    // ID vars //

    private final String namespace;
    private final TextureType type;
    private final String id;

    // TextureID constructor
    public TextureID(String namespace, TextureType type, String id) {
        this.namespace = namespace;
        this.type = type;
        this.id = id;
    }

    // Getters //

    public String getNamespace() { return this.namespace; }
    public String getID() { return this.id; }
    public TextureType getType() { return this.type; }

    // Overiddes //

    @Override
    public boolean equals(Object o) {
        
        if(o == null) return false;
        if(!(o instanceof TextureID)) return false;

        TextureID other = (TextureID) o;

        return this.namespace.equals(other.getNamespace()) && this.type.equals(other.getType()) && this.id.equals(other.getID());

    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, type, id);
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.type.name() + ":" + this.id;
    }

}
