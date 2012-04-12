package com.siondream.freegemas;

import java.util.HashMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Freegemas implements ApplicationListener {
	
	// States
	private HashMap<String, State> _states = null;
	private State _currentState = null;
	
	// Assets
	private static AssetManager _assetManager = null;
	
	private static SpriteBatch _batch = null;
	private static OrthographicCamera _camera = null;
	
	// Mouse pointer
	private TextureRegion _imgMouse = null;
	
	@Override
	public void create() {
		// Create assets manager
		_assetManager = new AssetManager();
		
		// Create states table
		_states = new HashMap<String, State>();
		
		// Init animation system
		Animation.init();
		
		// Load general assets
		_assetManager.load("data/handCursor.png", Texture.class);
		_assetManager.finishLoading();
		
		// Get assets
		_imgMouse = new TextureRegion(_assetManager.get("data/handCursor.png", Texture.class));
        _imgMouse.flip(false, true);
		
		// Sprite batch
		_batch = new SpriteBatch();
		
		// Ortographic camera
		_camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera.setToOrtho(true);
		
		// Mouse hidden
		Gdx.input.setCursorCatched(true);
		
		// Create states
		_states.put("StateGame", new StateGame(this));
		
		// Asign initial state
		changeState("StateGame");
	}

	@Override
	public void dispose() {
		_assetManager.dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render() {
		// Clear the screen, update the camera and make the sprite batch
        // use its matrices.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        _camera.update();
        _batch.setProjectionMatrix(_camera.combined);
		
        // Start rendering
        _batch.begin();
		
		// Update and render current state
		if (_currentState != null) {
			_currentState.update(Gdx.graphics.getDeltaTime());
			_currentState.render();
		}
		
		// Render mouse on top
		_batch.draw(_imgMouse, Gdx.input.getX(), Gdx.input.getY());
		
		_batch.end();
	}

	@Override
	public void resize(int arg0, int arg1) {
		_camera.setToOrtho(true, arg0, arg1);
	}

	@Override
	public void resume() {
		
	}
	
	public boolean changeState(String stateName) {
		// Fetch new state
		State newState = _states.get(stateName);
		
		if (newState != null) {
			// Unload old state if there was one
			if (_currentState != null) {
				_currentState.unload();
			}
			
			// Assign new state
			_currentState = newState;
			
			// Load new state and register as input processor
			_currentState.load();
			Gdx.input.setInputProcessor(_currentState);
		
			return true;
		}
		
		return false;
	}
	
	public static AssetManager getAssetManager() {
		return _assetManager;
	}

	public static SpriteBatch getSpriteBatch() {
		return _batch;
	}
	
	public static OrthographicCamera getCamera() {
		return _camera;
	}
}
