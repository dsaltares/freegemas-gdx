package com.siondream.freegemas;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Freegemas implements ApplicationListener {
	
	// States
	private HashMap<String, State> _states = null;
	private State _currentState = null;
	
	// Assets
	private AssetManager _assetManager = null;
	
	private SpriteBatch _batch = null;
	
	// Mouse pointer
	private Texture _mouseTexture = null;
	
	@Override
	public void create() {
		// Create assets manager
		_assetManager = new AssetManager();
		
		// Create states table
		_states = new HashMap<String, State>();
		
		// Create states
		
		// Asign initial state
		
		// Load initial state
		
		// Load general assets
		_assetManager.load("data/handCursor.png", Texture.class);
		_assetManager.finishLoading();
		
		// Get assets
		_mouseTexture = _assetManager.get("data/handCursor.png", Texture.class);
		
		// Sprite batch
		_batch = new SpriteBatch();
		
		// Mouse hidden
		Gdx.input.setCursorCatched(true);
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
		
		_batch.begin();
		
		// Update and render current state
		if (_currentState != null) {
			_currentState.update();
			_currentState.render();
		}
		
		// Render mouse on top
		_batch.draw(_mouseTexture, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		
		_batch.end();
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
			// Unload old state
			_currentState.unload();
			
			// Assign new state
			_currentState = newState;
			
			// Load new state
			_currentState.load();
		
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
}
