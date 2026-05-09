package io.kyrixen.tinyblox;

import com.badlogic.gdx.Game;

import io.kyrixen.tinyblox.menu.Menu;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new Menu(this));
    }

    @Override
    public void dispose() {

        if (getScreen() != null) getScreen().dispose();
    
        super.dispose();
    
    }

}