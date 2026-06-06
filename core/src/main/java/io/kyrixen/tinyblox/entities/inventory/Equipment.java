package io.kyrixen.tinyblox.entities.inventory;

import io.kyrixen.tinyblox.graphics.texture.TextureID;
import io.kyrixen.tinyblox.utils.Utils;

public class Equipment extends Item {

    // Type of equipment
    public enum EquipmentType {

        PICKAXE(0.35f, 0.15f, 1f),
        AXE(0.45f, 1f, 0.15f),
        WEAPON(0.60f, 0.15f, 0.15f);


        // Vars //

        private final float damageMult;
        private final float mineWoodMult;
        private final float mineStoneMult;


        // Enum constructor
        EquipmentType(float damageMult, float mineWoodishMult, float mineStonishMult) {
            this.damageMult = damageMult;
            this.mineWoodMult = mineWoodishMult;
            this.mineStoneMult = mineStonishMult;
        }


        // Getters //

        public float getDamageMult() { return damageMult; }

        public float getMineWoodMult() { return mineWoodMult; }
        public float getMineStoneMult() { return mineStoneMult; }

    }

    // Type of tools tier
    public enum ToolTier {

        WOOD(1f),
        STONE(1.5f),
        IRON(2f);

        // Tier multiplier
        private final float tierMult;

        // Enum constructor
        ToolTier(float tierMult) {
            this.tierMult = tierMult;
        }

        // Getter
        public float getTierMult() { return tierMult; }

    }


    // Item type
    private final EquipmentType equipmentType;

    // Item tool tier
    private final ToolTier toolTier;


    // Constructor
    public Equipment(String name, TextureID textureID, boolean obtainable, ToolTier toolTier, EquipmentType equipmentType) {
        super(name, Utils.generateItemID(), textureID, obtainable, (byte) 1);
        this.toolTier = toolTier;
        this.equipmentType = equipmentType;
    }


    // Getters //

    public float getAttackDamage() { return this.toolTier.getTierMult() * this.equipmentType.getDamageMult(); }
    public float getWoodMiningSpeed() { return this.toolTier.getTierMult() * this.equipmentType.getMineWoodMult(); }
    public float getStoneMiningSpeed() { return this.toolTier.getTierMult() * this.equipmentType.getMineStoneMult(); }

}
