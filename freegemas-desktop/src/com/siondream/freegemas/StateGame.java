package com.siondream.freegemas;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class StateGame extends State {

	public enum State {
		Loading,
		InitialGems,
		Wait,
		SelectedGem,
		ChangingGems,
		DisappearingGems,
		FallingGems,
		DisappearingBoard,
		TimeFinished,
		ShowingScoreTable
	};
	
	private static final Vector2 gemsInitial = new Vector2(490, 70);
	
	// Current game state
	private State _state;
	
	// Selected squares
	private Coord _selectedSquareFirst;
	private Coord _selectedSquareSecond;
	private boolean _clicking;
	
	// Hints
	private double _showingHint;
	private double _animHintTotalTime;
	private Coord _coordHint;
	
	// Game board
	private Board _board;
	
	// Animations
	private double _animTime;
	private double _animTotalTime;
	private double _animTotalInitTime;
	
	// Points and gems matches
	private MultipleMatch _groupedSquares;
	private int _points;
	private int _multiplier = 0;
	private String _txtTime;
	
	// Game elements textures
	private TextureRegion _imgBoard;
	private TextureRegion _imgWhite;
	private TextureRegion _imgRed;
	private TextureRegion _imgPurple;
	private TextureRegion _imgOrange;
	private TextureRegion _imgGreen;
	private TextureRegion _imgYellow;
	private TextureRegion _imgBlue;
	private TextureRegion _imgSelector;
	
	// GUI Buttons
	private Button _hintButton;
	private Button _resetButton;
	private Button _exitButton;
	private Button _musicButton;
	
	// Background textures
	private TextureRegion _imgScoreBackground;
	private TextureRegion _imgTimeBackground;
	
	// Fonts
	private BitmapFont _fontTime;
	private BitmapFont _fontScore;
	private BitmapFont _fontText;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	// Loading image
	private TextureRegion _imgLoading;
	
	// Starting time
	private double _remainingTime;
	
	// SFX and music
	private Sound _match1SFX;
	private Sound _match2SFX;
	private Sound _match3SFX;
	private Sound _selectSFX;
	private Sound _fallSFX;
	private Music _song;
	
	// Floating scores
	private ArrayList<FloatingScore> _floatingScores;
	
	// Particle effects
	private ParticleEffect _effect;
	private ArrayList<ParticleEffect> _effects;
	
	// Mouse pos
	private Vector3 _mousePos = null;
	
	public StateGame(Freegemas freegemas) {
		super(freegemas);
		
		// Initial state
		_state = State.Loading;
		
		// Load and sync loading banner
		AssetManager assetManager = _parent.getAssetManager();
		assetManager.load("data/loadingBanner.png", Texture.class);
		assetManager.finishLoading();
		_imgLoading = new TextureRegion(assetManager.get("data/loadingBanner.png", Texture.class));
		_imgLoading.flip(false,  true);
		
		// Create buttons
		_hintButton = new Button(_parent, 130, 460, "Hint");
		_resetButton = new Button(_parent, 130, 510, "Reset");
		_exitButton = new Button(_parent, 130, 560, "Exit");
		_musicButton = new Button(_parent, 130, 610, "Turn off music");
		
		// Creare board
		_board = new Board();
		
		// Time txt
		_txtTime = new String("");
		
		_selectedSquareFirst = new Coord(-1, -1);
		_selectedSquareSecond = new Coord(-1, -1);
		
		// Scores
		_floatingScores = new ArrayList<FloatingScore>();
		
		// Mouse pos
		_mousePos = new Vector3();
		
		// Particle effects
		_effect = new ParticleEffect();
		_effect.load(Gdx.files.internal("data/particleStars"), Gdx.files.internal("data"));
		
		_effects = new ArrayList<ParticleEffect>();
		
		// Init game for the first time
		init();
	}
	
	@Override
	public void load() {
		AssetManager assetManager = _parent.getAssetManager();
		
		// Load fonts
		BitmapFontLoader.BitmapFontParameter fontParameters = new BitmapFontLoader.BitmapFontParameter();
		fontParameters.flip = true;
		
		assetManager.load("data/timeFont.fnt", BitmapFont.class, fontParameters);
		assetManager.load("data/scoreFont.fnt", BitmapFont.class, fontParameters);
		assetManager.load("data/normalFont.fnt", BitmapFont.class, fontParameters);
		
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
		
		resetGame();
	}
	
	@Override
	public void unload() {
		// Set assets references to null
		_imgBoard = null;
		_imgWhite = null;
		_imgRed = null;
		_imgPurple = null;
		_imgOrange = null;
		_imgGreen = null;
		_imgYellow = null;
		_imgBlue = null;
		_imgSelector = null;
		_imgLoading = null;
		_imgScoreBackground = null;
		_imgTimeBackground = null;
		_fontTime = null;
		_fontScore = null;
		_fontText = null;
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
		_fontText = assetManager.get("data/normalFont.fnt", BitmapFont.class);
		
		// Load textures
		_imgScoreBackground = new TextureRegion(assetManager.get("data/scoreBackground.png", Texture.class));
		_imgBoard = new TextureRegion(assetManager.get("data/board.png", Texture.class));
		_imgSelector = new TextureRegion(assetManager.get("data/selector.png", Texture.class));
		_imgTimeBackground = new TextureRegion(assetManager.get("data/timeBackground.png", Texture.class));
		_imgWhite = new TextureRegion(assetManager.get("data/gemWhite.png", Texture.class));
		_imgRed = new TextureRegion(assetManager.get("data/gemRed.png", Texture.class));
		_imgPurple = new TextureRegion(assetManager.get("data/gemPurple.png", Texture.class));
		_imgOrange = new TextureRegion(assetManager.get("data/gemOrange.png", Texture.class));
		_imgGreen = new TextureRegion(assetManager.get("data/gemGreen.png", Texture.class));
		_imgYellow = new TextureRegion(assetManager.get("data/gemYellow.png", Texture.class));
		_imgBlue = new TextureRegion(assetManager.get("data/gemBlue.png", Texture.class));
		
		_imgScoreBackground.flip(false, true);
		_imgBoard.flip(false, true);
		_imgSelector.flip(false, true);
		_imgTimeBackground.flip(false, true);
		_imgWhite.flip(false, true);
		_imgRed.flip(false, true);
		_imgPurple.flip(false, true);
		_imgOrange.flip(false, true);
		_imgGreen.flip(false, true);
		_imgYellow.flip(false, true);
		_imgBlue.flip(false, true);
		
		// Button textures and font
		TextureRegion buttonBackground = new TextureRegion(assetManager.get("data/buttonBackground.png", Texture.class));
		TextureRegion iconHint = new TextureRegion(assetManager.get("data/iconHint.png", Texture.class));
		TextureRegion iconRestart = new TextureRegion(assetManager.get("data/iconRestart.png", Texture.class));
		TextureRegion iconExit = new TextureRegion(assetManager.get("data/iconExit.png", Texture.class));
		TextureRegion iconMusic = new TextureRegion(assetManager.get("data/iconMusic.png", Texture.class));
		
		buttonBackground.flip(false, true);
		iconHint.flip(false, true);
		iconRestart.flip(false, true);
		iconExit.flip(false, true);
		iconMusic.flip(false, true);
		
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
		
		// Play music if it wasn´t playing
		if (!_song.isPlaying() &&_musicButton.getText().equals("Turn off music"))
		{
			_song.setLooping(true);
	        //_song.play();
		}
	}
	
	@Override
	public void update(double deltaT) {
		
		// Update mouse pos
		_mousePos.x = Gdx.input.getX();
		_mousePos.y = Gdx.input.getY();
		_parent.getCamera().unproject(_mousePos);
		
		// LOADING STATE
		if (_state == State.Loading) {
			// If we finish loading, assign resources and change to FirstFlip state
			if (_parent.getAssetManager().update()) {
				assignResources();
				_state = State.InitialGems;
			}
			
			return;
		}
		
		// Particle effects
		int numParticles = _effects.size();
		
		for (int i = 0; i < numParticles; ++i) {
			_effects.get(i).update(Gdx.graphics.getDeltaTime());
		}
		
		// Game time
		_remainingTime -= deltaT;
		// If we are under the time limit, compute the string for the board
		if (_remainingTime > 0) {
			int minutes = (int)(_remainingTime / 60.0);
			int seconds = (int)(_remainingTime - minutes * 60);
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
			if ((_animTime += deltaT) >= _animTotalInitTime) {
				// Switch to next state (waiting for user input)
				_state = State.Wait;
				_board.endAnimation();
				
				// Reset animation step counter
				_animTime = 0;
			}
		}
		
		// WAITING STATE
		if (_state == State.Wait) {
			// Multiplier must be 0
			_multiplier = 0;
		}
		
		// SWAPPING GEMS STATE
		if (_state == State.ChangingGems) {
			// When animation ends
			if ((_animTime += deltaT) >= _animTotalTime) {
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
				_animTime = 0;
			}
		}
	
		// DISAPPEARING GEMS STATE
		if (_state == State.DisappearingGems) {
			
			// When anim ends
			if ((_animTime += deltaT) >= _animTotalTime) {
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
				_animTime = 0;
			}
		}
		
		// GEMS FALLING STATE
		if (_state == State.FallingGems) {
			// When animation ends
			if ((_animTime += deltaT) >= _animTotalTime) {
				// Play the fall sound fx
				_fallSFX.play();
				
				// Switch to the next state (waiting)
				_state = State.Wait;
				
				// Reset animation counter
				_animTime = 0;

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
	        if((_animTime += deltaT) >= _animTotalTime) {
	            // Switch to the initial state
	            _state = State.InitialGems;

	            // Generate a brand new board
	            _board.generate();

	            // Reset animation counter
	            _animTime = 0;
	        }
	    }
		
		// In this state, the time has finished, so we need to create a ScoreBoard
	    else if(_state == State.TimeFinished) {

	        // When animation ends
	        if((_animTime += deltaT) >= _animTotalInitTime){

	            // Create a new score table
	            // _scoreTable = new ScoreTable(_parent, _points);

	            // Switch to the following state
	            _state = State.ShowingScoreTable;
				 
	            // Reset animation counter
	            _animTime = 0;
	        }
	    }

	    // Whenever a hint is being shown, decrease its controlling variable
	    if (_showingHint > 0.0)
	    {
	        _showingHint -= Gdx.graphics.getDeltaTime();
	    }
	}
	
	private void removeEndedParticles() {
		int numParticles = _effects.size();
		
		for (int i = 0; i < numParticles; ++i) {
			if (_effects.get(i).isComplete()) {
				_effects.remove(i);
				--i;
				--numParticles;
			}
		}
	}

	private void removeEndedFloatingScores() {
		int numScores = _floatingScores.size();
		
		for (int i = 0; i < numScores; ++i) {
			if (_floatingScores.get(i).isFinished()) {
				_floatingScores.remove(i);
				--i;
				--numScores;
			}
		}
	}

	@Override
	public void render() {
		SpriteBatch batch = _parent.getSpriteBatch();
		
		// STATE LOADING
		if (_state == State.Loading) {
			batch.draw(_imgLoading, 50, 50);
			
			return;
		}
		
		// Background image
		batch.draw(_imgBoard, 0, 0);
		
		// Draw buttons
		_hintButton.render();
		_resetButton.render();
		_musicButton.render();
		_exitButton.render();
		
		// Draw the score
		batch.draw(_imgScoreBackground, 130, 210);
		_fontScore.draw(batch,
						new String("" + _points),
						318 - _fontScore.getBounds(new String("" + _points)).width,
						219);
		
		// Draw the time
		batch.draw(_imgTimeBackground, 130, 310);
		_fontTime.draw(batch,
				_txtTime,
				310 - _fontTime.getBounds(_txtTime).width,
				325);
		
		// Draw board
		TextureRegion img = null;
		
		if (_state != State.ShowingScoreTable) {
			// Go through all of the squares
	        for (int i = 0; i < 8; ++i) {
	            for (int j = 0; j < 8; ++j) {

	                // Check the type of each square and
	                // save the proper image in the img pointer
	                switch (_board.getSquare(i, j).getType()) {
	                case sqWhite:
	                    img = _imgWhite;
	                    break;

	                case sqRed:
	                    img = _imgRed;
	                    break;

	                case sqPurple:
	                    img = _imgPurple;
	                    break;

	                case sqOrange:
	                    img = _imgOrange;
	                    break;

	                case sqGreen:
	                    img = _imgGreen;
	                    break;

	                case sqYellow:
	                    img = _imgYellow;
	                    break;

	                case sqBlue:
	                    img = _imgBlue;
	                    break;

	                } // switch end
	                
	             // Now, if img is not NULL (there's something to draw)
	                if (img != null) {
	                    // Default positions
	                    float imgX = gemsInitial.x + i * 76;
	                    float imgY = gemsInitial.y + j * 76;

	                    // Default color
	                    Color imgColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	                    
	                    // In the initial state, the gems fall vertically
	                    // decreasing its speed
	                    if (_state == State.InitialGems) {
	                        imgY = Animation.easeOutQuad(_animTime,
							                             gemsInitial.y + _board.getSquares()[i][j].origY * 76,
							                             _board.getSquares()[i][j].destY * 76,
							                             _animTotalInitTime);                            
	                    }

	                    // In the ending states, gems fall vertically, 
	                    // increasing their speed
	                    else if (_state == State.DisappearingBoard || _state == State.TimeFinished) {
	                        imgY = Animation.easeInQuad(_animTime,
	                        						     gemsInitial.y + _board.getSquares()[i][j].origY * 76,
							                            _board.getSquares()[i][j].destY * 76,
							                            _animTotalInitTime); 
	                    }

	                    else if ((_state == State.Wait ||
	                    		  _state == State.SelectedGem ||
	                    		  _state == State.FallingGems)
	                    		  && _board.getSquare(i, j).mustFall) {
	                        
	                    	imgY = Animation.easeOutQuad(_animTime,
							                             gemsInitial.y + _board.getSquares()[i][j].origY * 76,
							                             _board.getSquares()[i][j].destY * 76,
							                             _animTotalTime); 
	                    }                    

	                    // When two gems are switching
	                    else if (_state == State.ChangingGems) {
	                        if(i == _selectedSquareFirst.x &&  j == _selectedSquareFirst.y) {

	                            imgX = Animation.easeOutQuad(_animTime,
	                            							 gemsInitial.x + i * 76,
	                            		                     (_selectedSquareSecond.x - _selectedSquareFirst.x) * 76,
	                            		                     _animTotalTime);

	                            imgY = Animation.easeOutQuad(_animTime,
	                            							 gemsInitial.y + j * 76,
	                            							 (_selectedSquareSecond.y - _selectedSquareFirst.y) * 76,
	                            							 _animTotalTime);

	                        }

	                        else if (i == _selectedSquareSecond.x && j == _selectedSquareSecond.y){

	                            imgX = Animation.easeOutQuad(_animTime,
	                            							 gemsInitial.x + i * 76,
	                            							 (_selectedSquareFirst.x - _selectedSquareSecond.x) * 76,
	                            							 _animTotalTime);

	                            imgY = Animation.easeOutQuad(_animTime,
	                            							 gemsInitial.y + j * 76,
	                            							 (_selectedSquareFirst.y - _selectedSquareSecond.y) * 76,
	                            							 _animTotalTime);
	                        }
	                    }
	                    
	                    else if (_state == State.DisappearingGems) {
	                    	// Winning gems disappearing
	                    	if (_groupedSquares.isMatched(new Coord(i, j))) {
	                    		imgColor = new Color(1.0f, 1.0f, 1.0f, (1.0f - (float)(_animTime/_animTotalTime)));
	                    	}
	                    }
	                    
	                    // Finally draw the image
	                    batch.setColor(imgColor);
	                    batch.draw(img, imgX, imgY);
	                    batch.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));

	                } // End if (img != NULL)
	                
	                img = null;
	            }
	            
	            // If the mouse is over a gem
	            if (overGem((int)_mousePos.x, (int)_mousePos.y)) {
	                // Draw the selector over that gem
	            	Coord coord = getCoord((int)_mousePos.x, (int)_mousePos.y);
	                batch.draw(_imgSelector,
	                		  (int)gemsInitial.x + coord.x * 76,
	                		  (int)gemsInitial.y + coord.y * 76);
	            }
	            
	            
	            
	            // If a gem was previously clicked
	            if(_state == State.SelectedGem){
	                // Draw the tinted selector over it
	            	batch.setColor(1.0f, 0.0f, 1.0f, 1.0f);
	                batch.draw(_imgSelector,
	                		   (int)gemsInitial.x + _selectedSquareFirst.x * 76,
	                		   (int)gemsInitial.y + _selectedSquareFirst.y * 76);
	                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	            }
	        }
	        
	        // If a hint is being shown
	        if (_showingHint > 0.0) {
	            // Get the opacity percentage
	            float p = (float)(_showingHint / _animHintTotalTime);

	            float x = gemsInitial.x + _coordHint.x * 76;
	            float y = gemsInitial.y + _coordHint.y * 76;

	            
	            batch.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f - p));
	            batch.draw(_imgSelector, x, y);
	            batch.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
	        }
		}
		
		// Draw each score little message
		int numScores = _floatingScores.size();
		
		for (int i = 0; i < numScores; ++i) {
			_floatingScores.get(i).draw();
		}
		
		// Draw particle systems
		int numParticles = _effects.size();
		
		for (int i = 0; i < numParticles; ++i) {
			_effects.get(i).draw(batch);
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
		if (arg3 == 0){ // Left mouse button clicked
	        _clicking = true;

	        _mousePos.x = arg0;
	        _mousePos.y = arg1;
	        _parent.getCamera().unproject(_mousePos);
	        
	        // Button 
	        if (_exitButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            _parent.changeState("StateMenu");
	        }
	        else if (_hintButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            showHint();
	        }
	        else if (_musicButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            if (_song.isPlaying()) {
	                _musicButton.setText("Turn on music");
	                _song.stop();
	            }
	            else {
	            	_musicButton.setText("Turn off music");
	            	_song.setLooping(true);
	                //_song.play();
	            }	    
	        }
	        else if (_resetButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            _state = State.DisappearingBoard;
	            gemsOutScreen();
	            resetGame();
	        }
	        else if (overGem((int)_mousePos.x, (int)_mousePos.y)) { // Si se pulsó sobre una gema
	            _selectSFX.play();

	            if (_state == State.Wait) { // Si no hay ninguna gema marcada
	                _state = State.SelectedGem;
	                Coord coord = getCoord((int)_mousePos.x, (int)_mousePos.y);
	                _selectedSquareFirst.x = coord.x;
	                _selectedSquareFirst.y = coord.y;
	            }

	            else if (_state == State.SelectedGem) { // Si ya había una gema marcada
	                if (!checkClickedSquare((int)_mousePos.x, (int)_mousePos.y)) {
	                    _selectedSquareFirst.x = -1;
	                    _selectedSquareFirst.y = -1;
	                    _state = State.Wait;		    
	                }
	            }
	        }
		}

		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		if (arg3 == 0){ // Left mouse button clicked
	        _clicking = false;
	        
	        _mousePos.x = arg0;
	        _mousePos.y = arg1;
	        _parent.getCamera().unproject(_mousePos);
	        
	        if (_state == State.SelectedGem) {

	            Coord res = getCoord((int)_mousePos.x, (int)_mousePos.y);

	            if(!(res == _selectedSquareFirst)) {
	                checkClickedSquare((int)_mousePos.x, (int)_mousePos.y);
	            }
	        }
		}
		
		return false;
	}
	
	private void gemsOutScreen() {
	    for(int x = 0; x < 8; ++x){
	        for(int y = 0; y < 8; ++y){
	            _board.getSquare(x, y).mustFall = true;
	            _board.getSquare(x, y).origY = y;
	            _board.getSquare(x, y).destY = 9 + MathUtils.random(1, 7);
	        }
	    }
	}
	
	private void init() {
		// Initial animation state
	    _animTime = 0;

	    // Steps for short animations
	    _animTotalTime = 0.3;

	    // Steps for long animations
	    _animTotalInitTime = 1.0;

	    // Steps for the hint animation
	    _animHintTotalTime = 1.0;

	    // Reset the hint flag
	    _showingHint = -1;

	    // Initial score multiplier
	    _multiplier = 1;

	    // Reset the game to the initial values
	    resetGame();
	}
	
	private boolean overGem(int mX, int mY) {
		return (mX > gemsInitial.x && mX < gemsInitial.x + 76 * 8 &&
	            mY > gemsInitial.y && mY < gemsInitial.y + 76 * 8);
	}
	
	private Coord getCoord(int mX, int mY) {
		return new Coord((mX - (int)gemsInitial.x) / 76, (mY - (int)gemsInitial.y) / 76);
	}
	
	private void redrawScoreBoard() {
		
	}
	
	private void createFloatingScores() {
	    // For each match in the group of matched squares
	    int numMatches = _groupedSquares.size();
	    
	    for (int i = 0; i < numMatches; ++i) {
	    	// Create new floating score
	    	Match match = _groupedSquares.get(i);
	    	int matchSize = match.size();
	    	_floatingScores.add(new FloatingScore(_parent,
	    										  _fontScore,
	    										  matchSize * 5 * _multiplier,
	    										  gemsInitial.x + match.getMidSquare().x * 76 + 5,
	    										  gemsInitial.y + match.getMidSquare().y * 76 + 5));
	    	
	    	// Create a particle effect for each matching square
	    	for (int j = 0; j < matchSize; ++j) {
	    		ParticleEffect newEffect = new ParticleEffect(_effect);
	    		newEffect.setPosition(gemsInitial.x + match.get(j).x * 76 + 38, gemsInitial.y + match.get(j).y * 76 + 38);
	    		newEffect.start();
	    		_effects.add(newEffect);
	    	}
	    	
	    	_points += matchSize * 5 * _multiplier;
	    }
	}
	
	private boolean checkClickedSquare(int mX, int mY) {
	    _selectedSquareSecond = getCoord(mX, mY);

	    // If gem is neighbour
	    if(Math.abs(_selectedSquareFirst.x - _selectedSquareSecond.x) 
	       + Math.abs(_selectedSquareFirst.y - _selectedSquareSecond.y) == 1){ 

	        Board temporal = new Board(_board);
	        temporal.swap(_selectedSquareFirst.x, _selectedSquareFirst.y,
	                      _selectedSquareSecond.x, _selectedSquareSecond.y);

	        _groupedSquares = temporal.check();

	        // If winning movement
	        if (!_groupedSquares.isEmpty()) {
	            _state = State.ChangingGems;
	            return true;
	        }
	    }

	    return false;
	}
	
	private void showHint() {
		ArrayList<Coord> solutions = _board.solutions();
		_coordHint = solutions.get(0);
		_showingHint = _animHintTotalTime;
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
		_remainingTime = 120; 
	}
}
