package io.kyrixen.tinyblox.utils;

import java.util.Random;

public class RandomUtils {
    
    // Randoms //

    private final static Random RANDOM = new Random();
    private final Random seedRandom;


    // Constructs with seed
    public RandomUtils(long seed) {
        this.seedRandom = new Random(seed);
    }


    // Boolean //

    public boolean seedBoolean(float chance) {
        return seedRandom.nextFloat() < chance;
    }
    
    public static boolean randomBoolean(float chance) {
        return RANDOM.nextFloat() < chance;
    }
    
    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }


    // Int //

    public int seedInt(int start, int end) {
        if(start > end) return 0;
        return seedRandom.nextInt(end - start + 1) + start;
    }

    public static int randomInt(int start, int end) {
        if(start > end) return 0;
        return RANDOM.nextInt(end - start + 1) + start;
    }

    public static int randomInt(int end) {
        return RANDOM.nextInt(end + 1);
    }


    // Float //
    
    public float seedFloat(float start, float end) {
        if (start > end) return 0.0f;
        return seedRandom.nextFloat() * (end - start) + start;
    }

    public static float randomFloat(float start, float end) {
        if (start > end) return 0.0f;
        return RANDOM.nextFloat() * (end - start) + start;
    }

    public static float randomFloat(float end) {
        return RANDOM.nextFloat() * end;
    }


    // Misc //

    public static long mixSeed(long baseSeed, long salt) {
        return baseSeed ^ salt;
    }

}
