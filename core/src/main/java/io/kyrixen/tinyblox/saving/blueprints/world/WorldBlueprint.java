package io.kyrixen.tinyblox.saving.blueprints.world;

public class WorldBlueprint {
    
    public int formatVersion;
    
    public String worldName;
    public int worldSeed;
    public String worldFrequency;

    public int lastEntityID;

    @Override
    public String toString() {
        return "WorldBlueprint {version=" + formatVersion + ", name=" + worldName + ", seed=" + worldSeed + ", frequency=" + worldFrequency + ", last_id=" + lastEntityID + "}";
    }
    
}
