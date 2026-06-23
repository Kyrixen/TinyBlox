package io.kyrixen.tinyblox.world;

// Frequency enum
public enum FrequencyType {

    FLAT(0.003f),
    NORMAL(0.007f),
    MOUNTAINOUS(0.012f),
    EXTREME(0.025f),
    GOOD_LUCK_BRO(0.045f);


    private final float frequency;

    FrequencyType(float frequency) {
        this.frequency = frequency;
    }

    public float getFrequency() { return this.frequency; }

}