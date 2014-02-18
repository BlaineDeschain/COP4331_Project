package com.group23.TowerDefense;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TowerDefense implements ApplicationListener , InputProcessor {
	private OrthographicCamera camera;				// Camera defines viewspace of screen
	private SpriteBatch batch;						// Canvas that you draw sprites on to
	private int select;								// Makes a tile purple when you touch it
	private Level curLevel;							// Level currently being played
	private Array<Enemy> enemies;							// Enemy on screen
	private Texture[] textures;						// Stores the tile textures	
	private double lastSpawnTime;
	
	public final int SCREEN_WIDTH = 1920;
	public final int SCREEN_HEIGHT = 1080;
	
	@Override
	// Essentially just loads the game
	public void create() 
	{		
		camera = new OrthographicCamera();				
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		lastSpawnTime = 0;
		
		Gdx.input.setInputProcessor(this);				// Makes it so this class excepts input
		
		select = -1;									// Not selecting anything on screen
		
		enemies = new Array<Enemy>();
		
		batch = new SpriteBatch();
		
		// Loads level textures into an array
		textures = new Texture[4];
		textures[0] = new Texture(Gdx.files.internal("tile00.png"));
		textures[1] = new Texture(Gdx.files.internal("tile01.png"));
		textures[2] = new Texture(Gdx.files.internal("tile02.png"));
		textures[3] = new Texture(Gdx.files.internal("tile03.png"));
		
		// Loads current level and puts an enemy on screen
		curLevel = Level.debug();
		

		enemies.add(new Enemy(curLevel));
	}

	@Override
	// Called when game is killed, unloads everything
	public void dispose() {
		batch.dispose();
		for (Texture t : textures)
			t.dispose();
	}

	@Override
	// Game update loop, everything is drawn to screen here
	public void render() {	
		
		lastSpawnTime += Gdx.graphics.getDeltaTime();
		if(lastSpawnTime > 2)
		{
			enemies.add(new Enemy(curLevel));
			lastSpawnTime = 0;
		}
		
		// Updates enemies current location
		for(int i = 0; i < enemies.size; i++)
		{
			enemies.get(i).update();
		}												
		
		Gdx.gl.glClearColor(1, 1, 1, 1);								// Clears screen 
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();												// Don't know
		
		batch.setProjectionMatrix(camera.combined);						// Don't know
		batch.begin();													// Start drawing to screen
		
		// Grid is 8x15
		// Draws map ***BASED ONcd Documents/ SCREEN WIDTH***
		for (int y = 0; y < SCREEN_HEIGHT / 128; y++)		
			for (int x = 0; x < SCREEN_WIDTH / 128; x++)
			{
				final int w = SCREEN_WIDTH / 128;
				if( select ==  y * w + x)
					batch.draw(textures[3], x * 128, y * 128);
				else
					batch.draw(textures[curLevel.getTile(x, y)], x * 128, y * 128);
			}
		
		// Draw enemies to screen
		for(int i = 0; i < enemies.size; i++)
		{
			enemies.get(i).draw(batch);
		}					
		batch.end();						// Stop drawing to screen
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
		Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);		// 
		camera.unproject(worldCoordinates);								// Converts where you touched into pixel coordinates
		int x  = (int)(worldCoordinates.x) / 128;			// Converts to tile coordinates
		int y  = (int)(worldCoordinates.y) / 128;			// Converts to tile coordinates
		select = y * SCREEN_WIDTH / 128 + x;    // Converts to tile array index
		 
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
