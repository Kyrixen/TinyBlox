package io.kyrixen.tinyblox.saving.blueprints.world;

public class EntityChunkBlueprint {

    public int formatVersion;
    public SavedEntity[] entities;


    public static class SavedEntity {
        public int id;
        public String type;
        public String data;
    }

}
