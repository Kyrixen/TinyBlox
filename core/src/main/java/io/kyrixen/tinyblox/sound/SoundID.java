package io.kyrixen.tinyblox.sound;

import java.util.Objects;

public class SoundID {
    
    // Sound type enum helper
    public enum SoundType {
        UI,
        SFX,
        MUSIC,
        HUD,
        MISC
    }

    // ID vars //

    private final String namespace;
    private final SoundType type;
    private final String id;

    // SoundID constructor
    public SoundID(String namespace, SoundType type, String id) {
        this.namespace = namespace;
        this.type = type;
        this.id = id;
    }

    // Getters //

    public String getNamespace() { return this.namespace; }
    public String getID() { return this.id; }
    public SoundType getType() { return this.type; }

    // Overrides //

    @Override
    public boolean equals(Object o) {
        
        if(o == null) return false;
        if(!(o instanceof SoundID)) return false;

        SoundID other = (SoundID) o;

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

}
