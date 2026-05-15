package io.kyrixen.tinyblox.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class Peripheal {

    // Scroll var
    private static int scrollType = 0;

    // Register scroll event
    static {

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean scrolled(float amountX, float amountY) {

                if (amountY > 0)
                    scrollType = 1;
                else if (amountY < 0)
                    scrollType = -1;
                else
                    scrollType = 0;

                return true;
            }
        });
    }
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


    public static int mouseScroll() {

        int tempScroll = scrollType;
        scrollType = 0;

        return tempScroll;
    
    }

    public static boolean anyMousePressed() {
        return Gdx.input.isButtonPressed(Input.Buttons.BACK)
            || Gdx.input.isButtonPressed(Input.Buttons.FORWARD)
            || Gdx.input.isButtonPressed(Input.Buttons.LEFT)
            || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)
            || Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);
    }

    public static int getMouseX() {
        return Gdx.input.getX();
    }

    public static int getMouseY() {
        return Gdx.input.getY();
    }

}
