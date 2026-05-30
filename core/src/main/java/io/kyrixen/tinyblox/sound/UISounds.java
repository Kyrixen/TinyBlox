package io.kyrixen.tinyblox.sound;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class UISounds {
    
    public Sound hollow;
    public Sound slider;
    public Sound click;
    public Sound options;
    
    private final ArrayList<Sound> uis = new ArrayList<>();

    public UISounds(){

        hollow = Gdx.audio.newSound(Gdx.files.internal("sounds/ui/hollow.wav"));
        slider = Gdx.audio.newSound(Gdx.files.internal("sounds/ui/slider.wav"));
        click = Gdx.audio.newSound(Gdx.files.internal("sounds/ui/click.wav"));
        options = Gdx.audio.newSound(Gdx.files.internal("sounds/ui/options.wav"));

        uis.add(hollow);
        uis.add(slider);
        uis.add(click);
        uis.add(options);

    }

    public void cleanup(){

        for(Sound ui : uis){ ui.dispose(); }

        uis.clear();

    }

}
