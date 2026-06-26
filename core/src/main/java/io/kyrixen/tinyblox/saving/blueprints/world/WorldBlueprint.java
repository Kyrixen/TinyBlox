package io.kyrixen.tinyblox.saving.blueprints.world;

public class WorldBlueprint {
    
    public int formatVersion;
    
    public String worldName;
    public int worldSeed;
    public String worldFrequency;
    public String worldDifficulty;

    public int lastEntityID;

    @Override
    public String toString() {
        return "WorldBlueprint {version=" + formatVersion + ", name=" + worldName + ", seed=" + worldSeed + ", frequency=" + worldFrequency + ", difficulty=" + worldDifficulty + ", last_id=" + lastEntityID + "}";
    }
    
}
