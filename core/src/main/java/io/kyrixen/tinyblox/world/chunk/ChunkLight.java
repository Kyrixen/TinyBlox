package io.kyrixen.tinyblox.world.chunk;

import com.badlogic.gdx.graphics.Color;

public class ChunkLight {

    public byte r;
    public byte g;
    public byte b;


    public ChunkLight(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ChunkLight(ChunkLight chunkLight) {
        this.r = chunkLight.r;
        this.g = chunkLight.g;
        this.b = chunkLight.b;
    }

    public ChunkLight(Color color) {
        this.r = (byte) (Math.min(color.r, 1f) * 255f);
        this.g = (byte) (Math.min(color.g, 1f) * 255f);
        this.b = (byte) (Math.min(color.b, 1f) * 255f);
    }


    public void copyLight(ChunkLight chunkLight) {
        this.r = chunkLight.r;
        this.g = chunkLight.g;
        this.b = chunkLight.b;
    }

    public void addOther(ChunkLight chunkLight) {
        r = (byte) Math.min(255, (r & 0xFF) + (chunkLight.r & 0xFF));
        g = (byte) Math.min(255, (g & 0xFF) + (chunkLight.g & 0xFF));
        b = (byte) Math.min(255, (b & 0xFF) + (chunkLight.b & 0xFF));
    }

    public Color toColor() {
        return new Color((r & 0xFF) / 255f, (g & 0xFF) / 255f, (b & 0xFF) / 255f, 1f);
    }

}