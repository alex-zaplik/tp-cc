package edu.pwr.tp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	BitmapFont fpsFont;
	BitmapFont testFont;
	GlyphLayout testLayout;

	public void fontsInit() {
		FreeTypeFontGenerator ftg1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter ftfp1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		ftfp1.size = 12;
		fpsFont = ftg1.generateFont(ftfp1);
		fpsFont.setColor(1, 1, 0, 0.6f);

		FreeTypeFontGenerator ftg2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter ftfp2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		ftfp2.size = 36;
		testFont = ftg2.generateFont(ftfp2);
		testFont.setColor(1, 1, 1, 1);

		testLayout = new GlyphLayout(testFont, "mójTest");
	}

	@Override
	public void create () {
		Gdx.graphics.setTitle("Chinese Checkers");
		batch = new SpriteBatch();
		img = new Texture("test.png");
		fontsInit();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		fpsFont.draw(batch, Float.toString(Gdx.graphics.getFramesPerSecond()), 5, Gdx.graphics.getHeight() - 5);
		testFont.draw(batch, testLayout, (Gdx.graphics.getWidth() - testLayout.width) / 2, (Gdx.graphics.getHeight() + testLayout.height) / 2);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
