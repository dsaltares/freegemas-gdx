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
	private State _nextState = null;
	private State _oldState = null;
	
	// Assets
	private AssetManager _assetManager = null;
	
	private SpriteBatch _batch = null;
	private OrthographicCamera _camera = null;
	
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
		_states.put("StateMenu", new StateMenu(this));
		
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
		
		// Perform pending memory unloading, safely
		performPendingAssetsUnloading();
		
		// Perform pending state changes, memory safe
		performPendingStateChange();
	}

	@Override
	public void resize(int arg0, int arg1) {
		_camera.setToOrtho(true, arg0, arg1);
	}

	@Override
	public void resume() {
		
	}
	
	public boolean changeState(String stateName) {
		// Fetch new state and, therefore, schedule safe change
		_nextState = _states.get(stateName);
		
		if (_nextState != null) {
			return true;
		}
		
		return false;
	}
	
	public AssetManager getAssetManager() {
		return _assetManager;
	}

	public SpriteBatch getSpriteBatch() {
		return _batch;
	}
	
	public OrthographicCamera getCamera() {
		return _camera;
	}
	
	private void performPendingStateChange() {
		if (_nextState != null) {
			// Assign new state
			_currentState = _nextState;
			
			// Load new state and register as input processor
			_currentState.load();
			Gdx.input.setInputProcessor(_currentState);
			
			// Nullify scheduled state change
			_nextState = null;
			
			// Schedule resource unload
			_oldState = null;
		}
	}
	
	private void performPendingAssetsUnloading() {
		// Unload old state if there was one and it's not the same one as the current one
		if (_oldState != null && _oldState != _currentState) {
			_oldState.unload();
			_oldState = null;
		}
	}
}
