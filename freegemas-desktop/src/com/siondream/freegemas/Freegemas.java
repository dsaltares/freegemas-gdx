package com.siondream.freegemas;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class Freegemas implements ApplicationListener {
	private HashMap<String, State> _states = null;
	private State _currentState = null;
	
	@Override
	public void create() {
		// Create states table
		_states = new HashMap<String, State>();
		
		// Create states
		
		// Asign initial state
		
		// Load initial state
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (_currentState != null) {
			_currentState.update();
			_currentState.render();
		}
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
}
