package com.group23.TowerDefense;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class TowerDefense implements ApplicationListener, InputProcessor 
{
	public final static int SCREEN_WIDTH  = 1920;
	public final static int SCREEN_HEIGHT = 1080;
	
	public static ShapeRenderer shapeRenderer;
	public static SpriteBatch spriteBatch;
	
	// Camera defines view space of screen
	private OrthographicCamera camera;
	
	// current level being played
	private Level curLevel;	
	
	private Vector3 touchPos;
	
	@Override
	// Essentially just loads the game
	public void create() 
	{		
		// set debug variables
		Tower.DEBUG_DRAWRANGE  = true;
		Tower.DEBUG_DRAWTARGET = true;
		
		// initialize textures
		Enemy.texture = new Texture(Gdx.files.internal("enemy00.png"));
		Tower.texture = new Texture(Gdx.files.internal("tower00.png"));
		
		Level.textures    = new Texture[4];
		Level.textures[0] = new Texture(Gdx.files.internal("tile00.png"));
		Level.textures[1] = new Texture(Gdx.files.internal("tile01.png"));
		Level.textures[2] = new Texture(Gdx.files.internal("tile02.png"));
		Level.textures[3] = new Texture(Gdx.files.internal("tile03.png"));
		
		// initialize static batches
		spriteBatch   = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		// initialize member variables
		curLevel      = new Level();
		touchPos      = new Vector3();
		
		camera = new OrthographicCamera();				
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// recognize this class as the input processor
		Gdx.input.setInputProcessor(this);
	}

	@Override
	// Called when game is killed, unloads everything
	public void dispose() {
		for (Texture t : Level.textures)
			t.dispose();
		Enemy.texture.dispose();
		spriteBatch.dispose();
	}

	@Override
	// Game update loop, everything is drawn to screen here
	public void render() 
	{	
		// update the current level
		curLevel.update(Gdx.graphics.getDeltaTime());
		
		// clear the screen
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// update the camera
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		
		// draw to screen
		shapeRenderer.begin(ShapeType.Line);
		spriteBatch.begin();
		
			curLevel.draw(spriteBatch);
		
		spriteBatch.end();
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	// For input, lets you select a tile on screen
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{		
		touchPos.x = screenX;
		touchPos.y = screenY;
		touchPos.z = 0;
		
		camera.unproject(touchPos);								// Converts where you touched into pixel coordinates
		int x  = (int) (touchPos.x) / 128;			// Converts to tile coordinates
		int y  = (int) (touchPos.y) / 128;			// Converts to tile coordinates
		
		if (y < curLevel.getHeight())
			curLevel.placeTower(x, y);
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
