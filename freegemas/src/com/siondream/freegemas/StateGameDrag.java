package com.siondream.freegemas;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.siondream.freegemas.StateGame.State;

public class StateGameDrag extends StateGame {

	public StateGameDrag(Freegemas freegemas) {
		super(freegemas);
		
		
	
	}

ArrayList<Coord> arrcoord = new ArrayList<Coord>();
boolean dragged;
	
	@Override
	public void render() {
	 batch = _parent.getSpriteBatch();
	//	dragged=false;
		// STATE LOADING
		if (_state == State.Loading) {
			String loading = _lang.getString("Loading...");
			TextBounds bounds = _fontLoading.getBounds(loading);
			_fontLoading.draw(batch,
							  loading,
							  (Freegemas.VIRTUAL_WIDTH - bounds.width) / 2,
							  (Freegemas.VIRTUAL_HEIGHT - bounds.height) / 2);
			
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
		batch.draw(_imgScoreBackground, 70, 75);
		_fontText.draw(batch, _lang.getString("Points"), 78, 40);
		_fontScore.draw(batch,
						"" + _points,
						452 - _fontScore.getBounds("" + _points).width,
						93);
		
		// Draw the time
		batch.draw(_imgTimeBackground, 70, 215);
		_fontText.draw(batch, _lang.getString("Time left"), 78, 180);
				 
		_fontTime.draw(batch,
				_txtTime,
				390 - _fontTime.getBounds(_txtTime).width,
				237);
		
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
					default:
						break;

	                } // switch end
	                
	             // Now, if img is not NULL (there's something to draw)
	                if (img != null) {
	                    // Default positions
	                    float imgX = gemsInitial.x + i * 76;
	                    float imgY = gemsInitial.y + j * 76;	                   
	                    
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
/*
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
	                    }*/
	                    
	                    else if (_state == State.DisappearingGems) {
	                    	// Winning gems disappearing
	                    	/*if (_groupedSquares.isMatched(new Coord(i, j))) {
	                    		_imgColor.a = 1.0f - (float)(_animTime/_animTotalTime);
	                    	}*/
	                    }
	                    
	                    // Finally draw the image
	                    batch.setColor(_imgColor);
	                    batch.draw(img, imgX, imgY);
	                    _imgColor.a = 1.0f;
	                    batch.setColor(_imgColor);

	                } // End if (img != NULL)
	                
	                img = null;
	            }
	            
	            // If the mouse is over a gem
	           /* if (overGem((int)_mousePos.x, (int)_mousePos.y)) {
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
	            }*/
	        }
	        
	        // If a hint is being shown
	        /*if (_showingHint > 0.0) {
	            // Get the opacity percentage
	            float p = (float)(_showingHint / _animHintTotalTime);

	            float x = gemsInitial.x + _coordHint.x * 76;
	            float y = gemsInitial.y + _coordHint.y * 76;
 
	            _imgColor.a = 1.0f - p;
	            batch.setColor(_imgColor);
	            batch.draw(_imgSelector, x, y);
	            _imgColor.a = 1.0f;
	            batch.setColor(_imgColor);
	        }
		}*/
		
		// Score table
		if (_scoreTable != null && _state == State.ShowingScoreTable) {
			_scoreTable.draw();
		}
		
		// Draw each score little message
		int numScores = _floatingScores.size;
		
		for (int i = 0; i < numScores; ++i) {
			_floatingScores.get(i).draw();
		}
		
		// Draw particle systems
		int numParticles = _effects.size;
		
		for (int i = 0; i < numParticles; ++i) {
			_effects.get(i).draw(batch);
		}
		
	}
	
		 batch.setColor(1.0f, 0.0f, 1.0f, 1.0f);
		// System.err.println(arrcoord.size());
		 for (int j = 0; j < arrcoord.size(); j++) {
			 batch.draw(_imgSelector,
	        		  (int)StateGame.gemsInitial.x +arrcoord.get(j).x * 76,
	        		  (int)gemsInitial.y + arrcoord.get(j).y * 76);
			}
		 batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		// System.out.println("render");
		// System.out.println();
	}
	
	@Override
	public void update(double deltaT) {
		
		// Update mouse pos
		//System.out.println("update");
		_mousePos.x = Gdx.input.getX();
		_mousePos.y = Gdx.input.getY();
		_parent.getCamera().unproject(_mousePos);
		
		// LOADING STATE
		if (super._state == State.Loading) {
			// If we finish loading, assign resources and change to FirstFlip state
			if (_parent.getAssetManager().update()) {
				assignResources();
				_state = State.InitialGems;
			}
			
			return;
		}
		
		// Particle effects
		int numParticles = _effects.size;
		
		for (int i = 0; i < numParticles; ++i) {
			_effects.get(i).update(Gdx.graphics.getDeltaTime());
		}
		
		// Game time
		_remainingTime -= deltaT;
		// If we are under the time limit, compute the string for the board
		if (_remainingTime > 0) {
			int minutes = (int)(_remainingTime / 60.0);
			int seconds = (int)(_remainingTime - minutes * 60);
			_txtTime = "" + minutes;
			if (seconds < 10) {
				_txtTime += ":0" + seconds;
			}
			else {
				_txtTime += ":" + seconds;
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
			_multiplier = 1;
			for (int j = 0; j < arrcoord.size(); j++) {
				System.err.println(arrcoord.get(j).x+" "+arrcoord.get(j).y);
			}
			System.err.println();
			if (!dragged&&arrcoord.size()>2){
				for (int i = 0; i < arrcoord.size(); i++) {
					_board.del(arrcoord.get(i).x,arrcoord.get(i).y);
				}
				createFloatingScores();
					arrcoord.clear();
					//System.err.println(arrcoord.size());
					if(arrcoord.isEmpty()){
						// Increase multiplier
						++_multiplier;
						
						// Play matching sounds
						playMatchSound();
						
						// Create floating scores for the matching group
						//createFloatingScores();
						
						// Reset animation step
						_animTime = 0;
						_state = State.DisappearingGems;
					}
					
				
			}
			
			
		}
		
		// SWAPPING GEMS STATE
		if (_state == State.ChangingGems) {
			/*if (!dragged&&arrcoord.size()>3){
				for (int i = 0; i < arrcoord.size(); i++) {
					_board.del(arrcoord.get(i).x,arrcoord.get(i).y);
					System.err.println(arrcoord.size());
         				arrcoord.remove(i);
					if(arrcoord.isEmpty()){
						
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
					if (arrcoord.size()==1){
						// Increase multiplier
						++_multiplier;
						
						// Play matching sounds
						playMatchSound();
						
						// Create floating scores for the matching group
						createFloatingScores();
						
						// Reset animation step
						_animTime = 0;
						
					}
				//_state = State.DisappearingGems;
				}
			*/
			
			
			
			//_state = State.DisappearingGems;
			// When animation ends
			if ((_animTime += deltaT) >= _animTotalTime) {
				// Switch to next state, gems start to disappear
				_state = State.DisappearingGems;
				
				/*// Swap gems in the board
				_board.swap((int)_selectedSquareFirst.x,
							(int)_selectedSquareFirst.y,
							(int)_selectedSquareSecond.x,
							(int)_selectedSquareSecond.y);*/
				
				
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
				
				/*for (int i = 0; i < _groupedSquares.size; ++i) {
					for (int j = 0; j < _groupedSquares.get(i).size; ++j) {
						_board.del((int)_groupedSquares.get(i).get(j).x,
								   (int)_groupedSquares.get(i).get(j).y);
					}
				}*/
				
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
	           // _groupedSquares = _board.check();
	
	            // If there are...
	           // if(_groupedSquares.size != 0) {
	            	 if(arrcoord.size()>3) {
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
	            else if(_board.solutions().size == 0) {
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
	            _scoreTable = new ScoreTable(_parent, _points);
	
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
	
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Coord coord;
		int x,y;
		boolean present=false;
		dragged =true;
		System.out.println("dragged");
		if (overGem((int)_mousePos.x, (int)_mousePos.y)) {
             // Draw the selector over that gem
			x=getCoord((int)_mousePos.x, (int)_mousePos.y).x;
			y=getCoord((int)_mousePos.x, (int)_mousePos.y).y;
			
			coord = getCoord((int)_mousePos.x, (int)_mousePos.y);
			//System.err.println(+coord.x+" "+" "+coord.y);
         	/*if(!arrcoord.contains(coord)){
         		
         		if(arrcoord.isEmpty()||_board.getSquare(x,y).getType().equals(_board.getSquare(arrcoord.get(0).x,arrcoord.get(0).y).getType())){
         			
         	arrcoord.add(new Coord(getCoord((int)_mousePos.x, (int)_mousePos.y).x,getCoord((int)_mousePos.x, (int)_mousePos.y).y));
         	
         		}
         		else {
         			
         				arrcoord.clear();
					
         		}
         	}*/
			for (int i = 0; i < arrcoord.size(); i++) {
				if(arrcoord.get(i).x==x&&arrcoord.get(i).y==y){
					present=true;
				}
			} 
			if(!present){
         		if (neighbour(x,y)){
         			if(_board.getSquare(x,y).getType().equals(_board.getSquare(arrcoord.get(0).x,arrcoord.get(0).y).getType())){
         			
         				arrcoord.add(new Coord(getCoord((int)_mousePos.x, (int)_mousePos.y).x,getCoord((int)_mousePos.x, (int)_mousePos.y).y));
         	
         			}
         			/*else if(!_board.getSquare(x,y).getType().equals(_board.getSquare(arrcoord.get(0).x,arrcoord.get(0).y).getType())){
             			
         				arrcoord.clear();
    				
         			}*/
         		
         		
         		}
         		else if(arrcoord.isEmpty()) {
         			arrcoord.add(new Coord(getCoord((int)_mousePos.x, (int)_mousePos.y).x,getCoord((int)_mousePos.x, (int)_mousePos.y).y));
         		}
         		else {
         			
     				arrcoord.clear();
				
     			}
         	}
         	
         	
        }
         	
		return false;
	}
	private boolean neighbour(int x,int y) {
		// TODO Auto-generated method stub
		boolean check=false;
		for (int i = 0; i < arrcoord.size(); i++) {
			if((Math.abs(arrcoord.get(i).x-x)<2/*||(Math.abs(arrcoord.get(i).x-x)==0*/)&&Math.abs(arrcoord.get(i).y-y)<2){
				return true;
			}
		
		
	}
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		if (arg3 == 0){ // Left mouse button clicked
	        _mousePos.x = arg0;
	        _mousePos.y = arg1;
	        _parent.getCamera().unproject(_mousePos);
	        
	       /* if (_state == State.SelectedGem) {

	            Coord res = getCoord((int)_mousePos.x, (int)_mousePos.y);

	            if(!(res == _selectedSquareFirst)) {
	                checkClickedSquare((int)_mousePos.x, (int)_mousePos.y);
	            }
	        }*/
	        
	        _hintButton.touchUp();
	        _musicButton.touchUp();
	        _exitButton.touchUp();
	        _resetButton.touchUp();
	        
		}
		dragged=false;
		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if (arg3 == 0){ // Left mouse button clicked
	        _mousePos.x = arg0;
	        _mousePos.y = arg1;
	        _parent.getCamera().unproject(_mousePos);
	        
	        // Button 
	        if (_exitButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            _parent.changeState("StateMenu");
	        }
	        else if (_hintButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	           // showHint();
	        }
	        else if (_musicButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            if (_song.isPlaying()) {
	                _musicButton.setText(_lang.getString("Turn on music"));
	                _song.stop();
	            }
	            else {
	            	_musicButton.setText(_lang.getString("Turn off music"));
	            	_song.setLooping(true);
	                _song.play();
	            }	    
	        }
	        else if (_resetButton.isClicked((int)_mousePos.x, (int)_mousePos.y)) {
	            _state = State.DisappearingBoard;
	            gemsOutScreen();
	            resetGame();
	        }
	      /*  else if (overGem((int)_mousePos.x, (int)_mousePos.y)) { // Si se puls� sobre una gema
	            _selectSFX.play();

	            if (_state == State.Wait) { // If there is no marked gem
	                _state = State.SelectedGem;
	                Coord coord = getCoord((int)_mousePos.x, (int)_mousePos.y);
	                _selectedSquareFirst.x = coord.x;
	                _selectedSquareFirst.y = coord.y;
	            }

	            else if (_state == State.SelectedGem) { // Si ya hab�a una gema marcada
	                if (!checkClickedSquare((int)_mousePos.x, (int)_mousePos.y)) {
	                    _selectedSquareFirst.x = -1;
	                    _selectedSquareFirst.y = -1;
	                    _state = State.Wait;		    
	                }
	            }
	        }*/
		}

		return false;
	}
	@Override
	protected void createFloatingScores() {
	    // For each match in the group of matched squares
		/*_groupedSquares=arrcoord;
	    int numMatches = _groupedSquares.size;*/
	    
	    /*for (int i = 0; i < numMatches; ++i) {
	    	// Create new floating score
	    	Match match = _groupedSquares.get(i);
	    	int matchSize = match.size;*/
		if(!arrcoord.isEmpty()){
	    	_floatingScores.add(new FloatingScore(_parent,
	    										  _fontScore,
	    										  arrcoord.size() * 5 * _multiplier,
	    										  gemsInitial.x + arrcoord.get(0).x * 76 + 5,
	    										  gemsInitial.y + arrcoord.get(0).y * 76 + 5));
	    	
	    	// Create a particle effect for each matching square
	    	for (int j = 0; j < arrcoord.size(); ++j) {
	    		PooledEffect newEffect = _effectPool.obtain();
	    		newEffect.setPosition(gemsInitial.x + arrcoord.get(j).x * 76 + 38, gemsInitial.y + arrcoord.get(j).y * 76 + 38);
	    		newEffect.start();
	    		_effects.add(newEffect);
	    	}
		}
	    	
	   /* }*/
		_points += arrcoord.size() * 5 * _multiplier;
		
	}
	protected void resetGame() {
		// Reset score
		_points = 0;
		
		// Generate board
		_board.generate();
		
		// Redraw the scoreboard
		redrawScoreBoard();
		
		// Restart the time (two minutes)
		_remainingTime = 300; 
	}
	
	
}


