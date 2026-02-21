package io.kyrixen.tinyblox.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Peripheal {


    // Key Handling //

    public static boolean keyReleased(int k) {
        return !Gdx.input.isKeyPressed(k);
    }

    public static boolean keyPressed(int k) {
        return Gdx.input.isKeyPressed(k);
    }

    public static boolean anyKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ANY_KEY);
    }

    public static boolean anyWASDPressed() {
        return keyPressed(Input.Keys.W) ||
               keyPressed(Input.Keys.A) ||
               keyPressed(Input.Keys.S) ||
               keyPressed(Input.Keys.D);
    }

    // Mouse Handling //

    public static boolean mouseReleased(int b) {
        return !Gdx.input.isButtonPressed(b);
    }

    public static boolean mousePressed(int b) {
        return Gdx.input.isButtonPressed(b);
    }

    public static boolean anyMousePressed() {
        return Gdx.input.isButtonPressed(Input.Buttons.BACK)
            || Gdx.input.isButtonPressed(Input.Buttons.FORWARD)
            || Gdx.input.isButtonPressed(Input.Buttons.LEFT)
            || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)
            || Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);
    }

}
