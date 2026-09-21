package io.kyrixen.tinyblox.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;

import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.Constants;

public class RendererStack {
	
	public final SpriteBatch batch;
	public final ShapeRenderer shape;
	public final BitmapFont font;
	public final Camera camera;

	private final Matrix4 projection = new Matrix4();
	

	public RendererStack() {
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("tinyblox/fonts/tinyblox_font.fnt"));
		camera = new Camera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.RENDER_DISTANCE, 3f);
		
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

	}


	public void resize(int width, int height) {

		camera.resize(width, height);

		projection.setToOrtho2D(0, 0, width, height);
		batch.setProjectionMatrix(projection);
		shape.setProjectionMatrix(projection);
	
	}
	
	
	public void dispose() {
		
		batch.dispose();
		shape.dispose();
		font.dispose();
		
	}
	
}
