package com.siondream.freegemas;

import java.util.HashMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class Freegemas implements ApplicationListener {
	
	// States
	private HashMap<String, State> _states = null;
	private State _currentState = null;
	
	// Assets
	private AssetManager _assetManager = null;
	
	private SpriteBatch _batch = null;
	private OrthographicCamera _camera = null;
	
	// Mouse pointer
	private Texture _mouseTexture = null;
	
	// Time control
	private double _time0;
	private double _time1;
	
	@Override
	public void create() {
		// Create assets manager
		_assetManager = new AssetManager();
		
		// Create states table
		_states = new HashMap<String, State>();
		
		// Init animation system
		Animation.init();
		
		// Create states
		_states.put("StateGame", new StateGame(this));
		
		// Asign initial state
		changeState("StateGame");
		
		// Load general assets
		_assetManager.load("data/handCursor.png", Texture.class);
		_assetManager.finishLoading();
		
		// Get assets
		_mouseTexture = _assetManager.get("data/handCursor.png", Texture.class);
		
		// Sprite batch
		_batch = new SpriteBatch();
		
		// Ortographic camera
		_camera = new OrthographicCamera(1280, 720);
		_camera.position.set(1280 / 2, 720 / 2, 0);
		
		// Mouse hidden
		Gdx.input.setCursorCatched(true);
		
		// Time control
		_time0 = TimeUtils.millis();
		_time1 = _time0;
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
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Start batch in case we need some debug rendering in update
		_camera.update();
		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		
		double deltaT = _time1 - _time0;
		
		// Update and render current state
		if (_currentState != null) {
			_currentState.update(deltaT);
			_currentState.render();
		}
		
		// Render mouse on top
		_batch.draw(_mouseTexture, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		
		_batch.end();
		
		// Update time control
		_time0 = _time1;
		_time1 = TimeUtils.millis();
	}

	@Override
	public void resize(int arg0, int arg1) {
		
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
	
	public AssetManager getAssetManager() {
		return _assetManager;
	}

	public SpriteBatch getSpriteBatch() {
		return _batch;
	}
	
	public OrthographicCamera getCamera() {
		return _camera;
	}
	
//	public TweenManager getTweenManager() {
//		return _tweenManager;
//	}
}
