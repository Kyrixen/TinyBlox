package io.kyrixen.tinyblox;

import com.badlogic.gdx.Game;

import io.kyrixen.tinyblox.menu.Menu;
import io.kyrixen.tinyblox.utils.Logger;

public class Main extends Game {

    @Override
    public void create() {
        Logger.LOGGER.setDebug(Constants.DEBUG);
        setScreen(new Menu(this));
    }

    @Override
    public void dispose() {

        if (getScreen() != null) getScreen().dispose();
    
        super.dispose();
    
    }

}