package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;

import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.Constants;

public class RendererStack {
	
	public final SpriteBatch batch;
	public final ShapeRenderer shape;
	public final BitmapFont font;
	public final Camera camera;
	
	public RendererStack() {
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("fonts/tinyblox_font.fnt"));
		camera = new Camera(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, Constants.RENDER_DISTANCE, 3f);
		
	}
	
	public void dispose() {
		
		batch.dispose();
		shape.dispose();
		font.dispose();
		camera.cleanup();
		
	}
	
}
