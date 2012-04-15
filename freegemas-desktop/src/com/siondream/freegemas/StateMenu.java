package com.siondream.freegemas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class StateMenu extends State {

	// Possible menu states
	public enum State { Loading,
						TransitionIn,
						Active,
						TransitionOut };
	
	// Current menu state
	private State _state;
	
	// Textures
	private TextureRegion _imgBackground;
	private TextureRegion _imgLogo;
	private TextureRegion _imgHighlight;
	
	// Font
	private BitmapFont _fontMenu;
	private BitmapFont _fontText;
	
	// Options
	private int _selectedOption;
	private ArrayList<Pair<String, String>> _options;
	
	// Animation time
	private double _animTime;
	private double _animLogoTime;
	private double _animTotalTime;
	
	// Positions
	private Vector2 _menuStart;
	private Vector2 _menuEnd;
	private int _menuGap;
	private Vector3 _mousePos;
	
	// Lang
	LanguagesManager _lang;
	
	public StateMenu(Freegemas freegemas) {
		super(freegemas);
		
		// Languages manager
		_lang = LanguagesManager.getInstance();
				
		// Initial state
		_state = State.Loading;
		
		// Resources are initially null
		_imgBackground = null;
		_imgLogo = null;
		_imgHighlight = null;
		_fontMenu = null;
		
		// Load font resource
		AssetManager assetManager = _parent.getAssetManager();
		assetManager.load("data/normalFont.fnt", BitmapFont.class);
		assetManager.finishLoading();
		_fontText = assetManager.get("data/normalFont.fnt", BitmapFont.class);
		
		// Menu options
		_selectedOption = 0;
		_options = new ArrayList<Pair<String, String>>();
		_options.add(new Pair(_lang.getString("Timetrial mode"), "StateGame"));
		_options.add(new Pair(_lang.getString("How to play"), "StateHowto"));
		_options.add(new Pair(_lang.getString("Exit"), "StateQuit"));
		
		// Mouse pos
		_mousePos = new Vector3();
		
		// Animation times
		_animTime = 0.0;
		_animTotalTime = 0.5;
		_animLogoTime = 0.5;
	}
	
	@Override
	public void load() {
		AssetManager assetManager = _parent.getAssetManager();
		
		// Load textures
		assetManager.load("data/mainMenuBackground.png", Texture.class);
		assetManager.load("data/mainMenuLogo.png", Texture.class);
		assetManager.load("data/menuHighlight.png", Texture.class);
		
		// Load fonts
		BitmapFontLoader.BitmapFontParameter fontParameters = new BitmapFontLoader.BitmapFontParameter();
		fontParameters.flip = true;
		assetManager.load("data/menuFont.fnt", BitmapFont.class, fontParameters);
	}
	
	@Override
	public void unload() {
		// Set references to null
		_imgBackground = null;
		_imgLogo = null;
		_imgHighlight = null;
		_fontMenu = null;
		
		// Unload resources
		AssetManager assetManager = _parent.getAssetManager();
		assetManager.unload("data/mainMenuBackground.png");
		assetManager.unload("data/mainMenuLogo.png");
		assetManager.unload("data/menuHighlight.png");
		assetManager.unload("data/menuFont.fnt");
	}
	
	@Override
	public void assignResources() {
		// Retrieve resources
		AssetManager assetManager = _parent.getAssetManager();
		_imgBackground = new TextureRegion(assetManager.get("data/mainMenuBackground.png", Texture.class));
		_imgLogo = new TextureRegion(assetManager.get("data/mainMenuLogo.png", Texture.class));
		_imgHighlight = new TextureRegion(assetManager.get("data/menuHighlight.png", Texture.class));
		_fontMenu = assetManager.get("data/menuFont.fnt", BitmapFont.class);
		
		_imgBackground.flip(false, true);
		_imgLogo.flip(false, true);
		_imgHighlight.flip(false, true);
		
		// Set positions now that we now about sizes
		
		float width;
		float maxWidth = 0;
		int numOptions = _options.size();
		
		for (int i = 0; i < numOptions; ++i) {
			TextBounds bounds = _fontMenu.getBounds(_options.get(i).getFirst());
			
			if (bounds.width > maxWidth) {
				maxWidth = bounds.width;
			}
		}
		
		_menuStart = new Vector2((Freegemas.VIRTUAL_WIDTH - maxWidth) / 2, 400);
		_menuGap = 75;
		_menuEnd = new Vector2(_menuStart.x + maxWidth, 350 + _options.size() * _menuGap);
	}
	
	@Override
	public void update(double deltaT) {
		if (_state == State.Loading) {
			if (_parent.getAssetManager().update()) {
				assignResources();
				_state = State.TransitionIn;
			}
			
			return;
		}
		else if (_state  == State.TransitionIn) {
	        if ((_animTime += deltaT) >= _animTotalTime) {
	        	_state = State.Active;
	        	_animTime = _animLogoTime;
	        }
	    }
	    else if (_state == State.Active) {
	    	
	    }
	    else if (_state == State.TransitionOut) {

	    }

	    // Mouse position and selected option
	    _mousePos.x = Gdx.input.getX();
	    _mousePos.y = Gdx.input.getY();
	    _parent.getCamera().unproject(_mousePos);

	    if (_mousePos.y >= _menuStart.y && _mousePos.y < _menuEnd.y) {
	        _selectedOption = (int)(_mousePos.y - _menuStart.y) / _menuGap;
	    }
	}
	
	@Override
	public void render () {
		SpriteBatch batch = _parent.getSpriteBatch();
		
		// STATE LOADING - Just render loading
		if (_state == State.Loading) {
			String loading = _lang.getString("Loading...");
			TextBounds bounds = _fontText.getBounds(loading);
			_fontText.draw(batch,
						   loading,
						   (Freegemas.VIRTUAL_WIDTH - bounds.width) / 2,
						   (Freegemas.VIRTUAL_HEIGHT - bounds.height) / 2);
			
			return;
		}
		
	    batch.draw(_imgBackground, 0, 0);
	    
	    batch.setColor(1.0f, 1.0f, 1.0f, (float)(_animTime / _animLogoTime));
	    batch.draw(_imgLogo, 0, 0);
	    batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
	    int numOptions = _options.size();
		
		for (int i = 0; i < numOptions; ++i) {
			TextBounds bounds = _fontMenu.getBounds(_options.get(i).getFirst());
			
			_fontMenu.setColor(0.0f, 0.0f, 0.0f, 0.5f);
			_fontMenu.draw(batch, _options.get(i).getFirst(), (Freegemas.VIRTUAL_WIDTH - bounds.width) / 2, _menuStart.y + i * _menuGap + 4);
			_fontMenu.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	        _fontMenu.draw(batch, _options.get(i).getFirst(), (Freegemas.VIRTUAL_WIDTH - bounds.width) / 2, _menuStart.y + i * _menuGap);
		}

	    //jewelAnim . draw();

	    batch.draw(_imgHighlight,
	    		   (Freegemas.VIRTUAL_WIDTH - _imgHighlight.getRegionWidth()) / 2,
	    		   _menuStart.y + 5 + _selectedOption * _menuGap);
	}
	
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// Left click		
		if (arg3 == 0) {
			_parent.changeState(_options.get(_selectedOption).getSecond());
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		
		return false;
	}
}
