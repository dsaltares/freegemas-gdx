package com.siondream.freegemas;

import com.badlogic.gdx.InputProcessor;

public class State implements InputProcessor {
	protected Freegemas _parent = null;
	
	public State(Freegemas freegemas) {
		_parent = freegemas;
	}
	
	// GAME LOOP
	
	public void update() {
	
	}
	
	public void render() {
	
	}

	// EVENTS
	
	@Override
	public boolean keyDown(int arg0) {
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		return false;
	}

	@Override
	public boolean touchMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	// MEMORY MANAGEMENT
	
	public void load() {
		
	}
	
	public void unload() {
	
	}
}
