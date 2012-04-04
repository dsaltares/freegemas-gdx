package com.siondream.freegemas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateGame extends State {

	public enum State {
		FirstFlip,
		Loading,
		InitialGems,
		Wait,
		SelectedGem,
		ChangingGmes,
		DisappearingGems,
		FallingGems,
		DisappearingBoard,
		TimeFinished,
		ShowingScoreTable
	};
	
	// Current game state
	private State _state;
	
	// Selected squares
	private Coord _selectedSquareFirst;
	private Coord _selectedSquareSecond;
	private boolean _clicking;
	
	// Hints
	private int _showingHint;
	private int _totalAnimHint;
	private Coord _coordHint;
	
	// Game board
	private Board _board;
	
	// Animations
	private int _animStep;
	private int _animTotal;
	private int _animTotalInit;
	
	// Points and gems matches
	private MultipleMatch _groupedSquares;
	private int _points;
	private int _scoreMultiplier;
	
	// Game elements textures
	private Texture _boardTexture;
	private Texture _whiteTexture;
	private Texture _redTexture;
	private Texture _purpleTexture;
	private Texture _orangeTexture;
	private Texture _greenTexture;
	private Texture _yellowTexture;
	private Texture _blueTexture;
	private Texture _selectorTexture;
	private Texture _pointsTexture;
	
	// GUI Buttons
	private Button _hintButton;
	private Button _resetButton;
	private Button _exitButton;
	private Button _musicButton;
	
	// Background textures
	private Texture _scoreBackground;
	private Texture _timeBackground;
	private Texture _scoreHeader;
	
	// Fonts
	private BitmapFont _fontTime;
	private BitmapFont _fontScore;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	// Loading image
	private Texture _loadingTexture;
	
	// Remaining time string
	private String _timeText;
	
	// Starting time
	private double _startTime;
	
	// SFX and music
	private Sound _match1SFX;
	private Sound _match2SFX;
	private Sound _match3SFX;
	private Sound _selectSFX;
	private Sound _fallSFX;
	private Music _song;
	
	public StateGame(Freegemas freegemas) {
		super(freegemas);
		
		_state = State.Loading;
		
		AssetManager assetManager = _parent.getAssetManager();
		assetManager.load("data/loadingBanner.png", Texture.class);
		assetManager.finishLoading();
		_loadingTexture = assetManager.get("data/loadingBanner.png", Texture.class);
		
		// Create buttons
		_hintButton = new Button(_parent, 0, 0, "Hint");
		_resetButton = new Button(_parent, 0, 0, "Reset");
		_exitButton = new Button(_parent, 0, 0, "Exit");
		_musicButton = new Button(_parent, 0, 0, "Music");
	}
	
	@Override
	public void load() {
		AssetManager assetManager = _parent.getAssetManager();
		
		// Load fonts
		assetManager.load("data/timeFont.fnt", BitmapFont.class);
		assetManager.load("data/scoreFont.fnt", BitmapFont.class);
		assetManager.load("data/normalFont.fnt", BitmapFont.class);
		
		// Load textures
		assetManager.load("data/scoreBackground.png", Texture.class);
		assetManager.load("data/buttonBackground.png", Texture.class);
		assetManager.load("data/board.png", Texture.class);
		assetManager.load("data/selector.png", Texture.class);
		assetManager.load("data/timeBackground.png", Texture.class);
		assetManager.load("data/gemWhite.png", Texture.class);
		assetManager.load("data/gemRed.png", Texture.class);
		assetManager.load("data/gemPurple.png", Texture.class);
		assetManager.load("data/gemOrange.png", Texture.class);
		assetManager.load("data/gemGreen.png", Texture.class);
		assetManager.load("data/gemYellow.png", Texture.class);
		assetManager.load("data/gemBlue.png", Texture.class);
		assetManager.load("data/iconHint.png", Texture.class);
		assetManager.load("data/iconRestart.png", Texture.class);
		assetManager.load("data/iconExit.png", Texture.class);
		assetManager.load("data/iconMusic.png", Texture.class);
		
		// Load SFX and music
		assetManager.load("data/match1.ogg", Sound.class);
		assetManager.load("data/match2.ogg", Sound.class);
		assetManager.load("data/match3.ogg", Sound.class);
		assetManager.load("data/select.ogg", Sound.class);
		assetManager.load("data/fall.ogg", Sound.class);
		assetManager.load("data/music1.ogg", Music.class);
	}
	
	@Override
	public void unload() {
		// Set assets references to null
		_boardTexture = null;
		_whiteTexture = null;
		_redTexture = null;
		_purpleTexture = null;
		_orangeTexture = null;
		_greenTexture = null;
		_yellowTexture = null;
		_blueTexture = null;
		_selectorTexture = null;
		_pointsTexture = null;
		_scoreBackground = null;
		_timeBackground = null;
		_loadingTexture = null;
		_scoreHeader = null;
		_fontTime = null;
		_fontScore = null;
		_match1SFX = null;
		_match2SFX = null;
		_match3SFX = null;
		_selectSFX = null;
		_fallSFX = null;
		_song = null;
		
		_hintButton.setIcon(null);
		_resetButton.setIcon(null);
		_exitButton.setIcon(null);
		_musicButton.setIcon(null);
		
		_hintButton.setBackground(null);
		_resetButton.setBackground(null);
		_exitButton.setBackground(null);
		_musicButton.setBackground(null);
		
		_hintButton.setFont(null);
		_resetButton.setFont(null);
		_exitButton.setFont(null);
		_musicButton.setFont(null);
		
		// Unload assets
		AssetManager assetManager = _parent.getAssetManager();
		assetManager.unload("data/timeFont.fnt");
		assetManager.unload("data/scoreFont.fnt");
		assetManager.unload("data/normalFont.fnt");
		assetManager.unload("data/scoreBackground.png");
		assetManager.unload("data/buttonBackground.png");
		assetManager.unload("data/board.png");
		assetManager.unload("data/selector.png");
		assetManager.unload("data/timeBackground.png");
		assetManager.unload("data/loadingBanner.png");
		assetManager.unload("data/gemWhite.png");
		assetManager.unload("data/gemRed.png");
		assetManager.unload("data/gemPurple.png");
		assetManager.unload("data/gemOrange.png");
		assetManager.unload("data/gemGreen.png");
		assetManager.unload("data/gemYellow.png");
		assetManager.unload("data/gemBlue.png");
		assetManager.unload("data/iconHint.png");
		assetManager.unload("data/iconRestart.png");
		assetManager.unload("data/iconExit.png");
		assetManager.unload("data/iconMusic.png");
		assetManager.unload("data/match1.ogg");
		assetManager.unload("data/match2.ogg");
		assetManager.unload("data/match3.ogg");
		assetManager.unload("data/select.ogg");
		assetManager.unload("data/fall.ogg");
		assetManager.unload("data/music1.ogg");
	}
	
	@Override
	public void assignResources() {
		super.assignResources();
		
		AssetManager assetManager = _parent.getAssetManager();
		
		// Load fonts
		_fontTime = assetManager.get("data/timeFont.fnt", BitmapFont.class);
		_fontScore = assetManager.get("data/scoreFont.fnt", BitmapFont.class);
		
		// Load textures
		_scoreBackground = assetManager.get("data/scoreBackground.png", Texture.class);
		_boardTexture = assetManager.get("data/board.png", Texture.class);
		_selectorTexture = assetManager.get("data/selector.png", Texture.class);
		_timeBackground = assetManager.get("data/timeBackground.png", Texture.class);
		_whiteTexture = assetManager.get("data/gemWhite.png", Texture.class);
		_redTexture = assetManager.get("data/gemRed.png", Texture.class);
		_purpleTexture = assetManager.get("data/gemPurple.png", Texture.class);
		_orangeTexture = assetManager.get("data/gemOrange.png", Texture.class);
		_greenTexture = assetManager.get("data/gemGreen.png", Texture.class);
		_yellowTexture = assetManager.get("data/gemYellow.png", Texture.class);
		_blueTexture = assetManager.get("data/gemBlue.png", Texture.class);
		
		// Button textures and font
		Texture buttonBackground = assetManager.get("data/buttonBackground.png", Texture.class);
		Texture iconHint = assetManager.get("data/iconHint.png", Texture.class);
		Texture iconRestart = assetManager.get("data/iconRestart.png", Texture.class);
		Texture iconExit = assetManager.get("data/iconExit.png", Texture.class);
		Texture iconMusic = assetManager.get("data/iconMusic.png", Texture.class);
		BitmapFont buttonFont = assetManager.get("data/normalFont.fnt", BitmapFont.class);
		
		_hintButton.setIcon(iconHint);
		_resetButton.setIcon(iconRestart);
		_exitButton.setIcon(iconExit);
		_musicButton.setIcon(iconMusic);
		
		_hintButton.setBackground(buttonBackground);
		_resetButton.setBackground(buttonBackground);
		_exitButton.setBackground(buttonBackground);
		_musicButton.setBackground(buttonBackground);
		
		_hintButton.setFont(buttonFont);
		_resetButton.setFont(buttonFont);
		_exitButton.setFont(buttonFont);
		_musicButton.setFont(buttonFont);
		
		// Load SFX and music
		_match1SFX = assetManager.get("data/match1.ogg", Sound.class);
		_match2SFX = assetManager.get("data/match2.ogg", Sound.class);
		_match3SFX = assetManager.get("data/match3.ogg", Sound.class);
		_selectSFX = assetManager.get("data/select.ogg", Sound.class);
		_fallSFX = assetManager.get("data/fall.ogg", Sound.class);
		_song = assetManager.get("data/music1.ogg", Music.class);
	}
	
	@Override
	public void update() {
		
		// LOADING STATE
		if (_state == State.Loading) {
			// If we finish loading, assign resources and change to FirstFlip state
			if (_parent.getAssetManager().update()) {
				assignResources();
				_state = State.FirstFlip;
			}
			
			return;
		}
	}
	
	@Override
	public void render() {
		SpriteBatch batch = _parent.getSpriteBatch();
		
		if (_state == State.Loading) {
			batch.draw(_loadingTexture, 0, 0);
		}
	}
	
	@Override
	public boolean keyDown(int arg0) {
		return false;
	}
	
	@Override
	public boolean keyUp(int arg0) {
		return false;
	}
	
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}
	
	private void gemsOutScreen() {
		
	}
	
	private void init() {
		
	}
	
	private boolean overGem(int mX, int mY) {
		return false;
	}
	
	private Coord getCoord(int mX, int mY) {
		return new Coord(0, 0);
	}
	
	private void redrawScoreBoard() {
		
	}
	
	private void createFloatingScores() {
		
	}
	
	private boolean checkClickedSquare(int mX, int mY) {
		return false;
	}
	
	private void showHint() {
		
	}
	
	private void playMatchSound() {
		
	}
	
	private void resetGame() {
		
	}
}
