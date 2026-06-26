package io.kyrixen.tinyblox.world;

public enum Difficulty {

    EASY(0.5f),
    NORMAL(1f),
    HARD(2f);

    private final float diffMult;

    Difficulty(float diffMult) {
        this.diffMult = diffMult;
    }

    public float getDiffMult() { return this.diffMult; }

}