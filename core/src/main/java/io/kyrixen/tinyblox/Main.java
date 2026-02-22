package io.kyrixen.tinyblox;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new Engine());
    }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        super.dispose();
    }

}