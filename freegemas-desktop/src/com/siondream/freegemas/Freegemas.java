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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

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
	private Rectangle _viewport = null;
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;
	private static final float ASPECT_RATIO = 1.7777f;
	
	// Mouse pointer
	private TextureRegion _imgMouse = null;
	private Vector3 _mousePos = null;
	
	private Logger _logger = null;
	
	@Override
	public void create() {
		// Logger
		_logger = new Logger("Freegemas");
		
		// Mouse pos
		_mousePos = new Vector3();
		
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
		_camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		_camera.setToOrtho(true, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		
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
        //_camera.apply(Gdx.gl10);
        Gdx.gl.glViewport((int) _viewport.x, (int) _viewport.y,
        				  (int) _viewport.width, (int) _viewport.height);
        
        // Start rendering
        _batch.begin();
        
        _batch.setProjectionMatrix(_camera.combined);
		
		// Update and render current state
		if (_currentState != null) {
			_currentState.update(Gdx.graphics.getDeltaTime());
			_currentState.render();
		}
		
		// Render mouse on top
		_mousePos.x = Gdx.input.getX();
		_mousePos.y = Gdx.input.getY();
		_camera.unproject(_mousePos);
		_batch.draw(_imgMouse, _mousePos.x, _mousePos.y);
		
		_batch.end();
		
		// Perform pending memory unloading, safely
		performPendingAssetsUnloading();
		
		// Perform pending state changes, memory safe
		performPendingStateChange();
	}

	@Override
	public void resize(int arg0, int arg1) {
		_logger.info("Resizing to: " + arg0 + "x" + arg1);
		
		// calculate new viewport
        float aspectRatio = (float)arg0/(float)arg1;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)arg1 / (float)VIRTUAL_HEIGHT;
            crop.x = (arg0 - VIRTUAL_WIDTH * scale) / 2.0f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)arg0 / (float)VIRTUAL_WIDTH;
            crop.y = (arg1 - VIRTUAL_HEIGHT * scale) / 2.0f;
        }
        else
        {
            scale = (float)arg0/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH * scale;
        float h = (float)VIRTUAL_HEIGHT * scale;
        _viewport = new Rectangle(crop.x, crop.y, w, h);
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
