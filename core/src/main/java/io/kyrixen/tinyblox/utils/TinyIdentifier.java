package io.kyrixen.tinyblox.utils;

import java.util.Objects;

public final class TinyIdentifier {

    // Identifier type enum helper
    public enum IdentifierType {
    
        TEXTURE,
        ITEM,
        TILE,
        SOUND,
        MUSIC,
        STRUCTURE,
        RECIPE,
        MISC
    
    }

    
    // ID vars //

    private final String namespace;
    private final IdentifierType type;
    private final String id;


    // Identifier constructor
    public TinyIdentifier(String namespace, IdentifierType type, String id) {
        this.namespace = namespace;
        this.type = type;
        this.id = id;
    }


    // Getters //

    public String getNamespace() { return this.namespace; }
    public String getID() { return this.id; }
    public IdentifierType getType() { return this.type; }


    // Overiddes //

    @Override
    public boolean equals(Object o) {
        
        if(o == null) return false;
        if(!(o instanceof TinyIdentifier)) return false;

        TinyIdentifier other = (TinyIdentifier) o;

        return this.namespace.equals(other.getNamespace()) && this.type.equals(other.getType()) && this.id.equals(other.getID());

    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, type, id);
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.type.name().toLowerCase() + ":" + this.id;
    }


    // Helpers //

    public static TinyIdentifier fromString(String identifier) {
        String[] parts = identifier.split(":");
        if(parts.length > 3) return null;
        return new TinyIdentifier(parts[0], IdentifierType.valueOf(parts[1].toUpperCase()), parts[2]);
    }

}
