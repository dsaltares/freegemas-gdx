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
	private double _remainingTime;
	
	// SFX and music
	private Sound _match1SFX;
	private Sound _match2SFX;
	private Sound _match3SFX;
	private Sound _selectSFX;
	private Sound _fallSFX;
	private Music _song;

	private int _multiplier = 0;

	private String _txtTime;
	
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
		
		// Creare board
		_board = new Board();
		
		// Reset game
		resetGame();
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
	public void update(double deltaT) {
		
		// LOADING STATE
		if (_state == State.Loading) {
			// If we finish loading, assign resources and change to FirstFlip state
			if (_parent.getAssetManager().update()) {
				assignResources();
				_state = State.InitialGems;
			}
			
			return;
		}
		
		// Game time
		_remainingTime -= deltaT;
		
		// If we are under the time limit, compute the string for the board
		if (_remainingTime > 0) {
			int minutes = (int)(_remainingTime / 60.0 / 1000.0);
			int seconds = (int)(_remainingTime / 1000.0 - minutes * 60);
			_txtTime = new String("" + minutes);
			if (seconds < 10) {
				_txtTime += new String(":0" + seconds);
			}
			else {
				_txtTime += new String(":" + seconds);
			}
		}
		
		// If we are over the time limit and not in a final score
		else if (_remainingTime <= 0 && _state != State.TimeFinished && _state != State.ShowingScoreTable) {
			// End the game
			_state = State.TimeFinished;
			
			// Take gems out of screen
			gemsOutScreen();
		}
		
		// Remove the hidden floating score
		removeEndedFloatingScores();
		
		// Remove the ended particle systems
		removeEndedParticles();
		
		// INITIAL GAME STATE
		if (_state == State.InitialGems) {
			// If animation ended
			if (++_animStep == _animTotal) {
				// Switch to next state (waiting for user input)
				_state = State.Wait;
				_board.endAnimation();
				
				// Reset animation step counter
				_animStep = 0;
			}
		}
		
		// WAITING STATE
		if (_state == State.Wait) {
			// Multiplier must be 0
			_multiplier = 0;
		}
		
		// SWAPPING GEMS STATE
		if (_state == State.ChangingGmes) {
			// When animation ends
			if (++_animStep == _animTotal) {
				// Switch to next state, gems start to disappear
				_state = State.DisappearingGems;
				
				// Swap gems in the board
				_board.swap((int)_selectedSquareFirst.x,
							(int)_selectedSquareFirst.y,
							(int)_selectedSquareSecond.x,
							(int)_selectedSquareSecond.y);
				
				// Increase multiplier
				++_multiplier;
				
				// Play matching sounds
				playMatchSound();
				
				// Create floating scores for the matching group
				createFloatingScores();
				
				// Reset animation step
				_animStep = 0;
			}
		}
	
		// DISAPPEARING GEMS STATE
		if (_state == State.DisappearingGems) {
			// When anim ends
			if (++_animStep == _animTotal) {
				// Switch to next state, gems falling
				_state = State.FallingGems;
				
				// Redraw scoreboard with new points
				redrawScoreBoard();
				
				// Delete squares that were matched on the board
				for (int i = 0; i < _groupedSquares.size(); ++i) {
					for (int j = 0; j < _groupedSquares.get(i).size(); ++j) {
						_board.del((int)_groupedSquares.get(i).get(j).x,
								   (int)_groupedSquares.get(i).get(j).y);
					}
				}
				
				// Calculate fall movements
				_board.calcFallMovements();
				
				// Apply changes to the board
				_board.applyFall();
				
				// Fill empty spaces
				_board.fillSpaces();
				
				// Reset animation counter
				_animStep = 0;
			}
		}
		
		// GEMS FALLING STATE
		if (_state == State.FallingGems) {
			// When animation ends
			if (++_animStep == _animTotal) {
				// Play the fall sound fx
				_fallSFX.play();
				
				// Switch to the next state (waiting)
				_state = State.Wait;
				
				// Reset animation counter
				_animStep = 0;

	            // Reset animation variables
	            _board.endAnimation();

	            // Check if there are matching groups
	            _groupedSquares = _board.check();

	            // If there are...
	            if(!_groupedSquares.isEmpty()) {
	                // Increase the score multiplier
	                ++_multiplier;

	                // Create the floating scores
	                createFloatingScores();

	                // Play matching sound
	                playMatchSound();

	                // Go back to the gems-fading state
	                _state = State.DisappearingGems;
	            }

	            // If there are neither current solutions nor possible future solutions
	            else if(_board.solutions().isEmpty()) {
	                // Make the board disappear
	                _state = State.DisappearingBoard;
	                gemsOutScreen();
	            }
			}
		}
		
		// DISAPPEARING BOARD STATE because there were no possible movements
	    else if(_state == State.DisappearingBoard) {
	        // When animation ends
	        if(++_animStep == _animTotal) {
	            // Switch to the initial state
	            _state = State.InitialGems;

	            // Generate a brand new board
	            _board.generate();

	            // Reset animation counter
	            _animStep = 0;
	        }
	    }
		
		// In this state, the time has finished, so we need to create a ScoreBoard
	    else if(_state == State.TimeFinished) {

	        // When animation ends
	        if(++_animStep == _animTotal){

	            // Create a new score table
	            // _scoreTable = new ScoreTable(_parent, _points);

	            // Switch to the following state
	            _state = State.ShowingScoreTable;
				 
	            // Reset animation counter
	            _animStep = 0;
	        }
	    }

	    // Whenever a hint is being shown, decrease its controlling variable
	    if(_showingHint != -1) 
	        --_showingHint;
	}
	
	private void removeEndedParticles() {
		
	}

	private void removeEndedFloatingScores() {
		
	}

	@Override
	public void render() {
		SpriteBatch batch = _parent.getSpriteBatch();
		
		// STATE LOADING
		if (_state == State.Loading) {
			batch.draw(_loadingTexture, 50, 50);
			
			return;
		}
		
		// Background image
		batch.draw(_boardTexture, 0, 0);
		
		// Draw buttons
		_hintButton.render();
		_resetButton.render();
		_musicButton.render();
		_exitButton.render();
		
		// Draw the score
		
		// Draw the time
		
		// Draw each score little message
		
		// Draw particle systems
		
		// Draw board
		int posX = 241;
		int posY = 41;
		Texture img = null;
		
		if (_state != State.ShowingScoreTable) {
			// Go through all of the squares
	        for(int i = 0; i < 8; ++i) {
	            for(int j = 0; j < 8; ++j) {

	                // Check the type of each square and
	                // save the proper image in the img pointer
	                switch(_board.getSquare(i, j).getType()){
	                case sqWhite:
	                    img = _whiteTexture;
	                    break;

	                case sqRed:
	                    img = _redTexture;
	                    break;

	                case sqPurple:
	                    img = _purpleTexture;
	                    break;

	                case sqOrange:
	                    img = _orangeTexture;
	                    break;

	                case sqGreen:
	                    img = _greenTexture;
	                    break;

	                case sqYellow:
	                    img = _yellowTexture;
	                    break;

	                case sqBlue:
	                    img = _blueTexture;
	                    break;

	                } // switch end
	            }
	        }       
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
		if (_multiplier  == 1) {
			_match1SFX.play();
		}
		else if (_multiplier == 2) {
			_match2SFX.play();
		}
		else {
			_match3SFX.play();
		}
	}
	
	private void resetGame() {
		// Reset score
		_points = 0;
		
		// Generate board
		_board.generate();
		
		// Redraw the scoreboard
		redrawScoreBoard();
		
		// Restart the time (two minutes)
		_remainingTime = 2 * 60 * 1000; 
		
		// Reset multiplier
		_multiplier = 0;
	}
}
