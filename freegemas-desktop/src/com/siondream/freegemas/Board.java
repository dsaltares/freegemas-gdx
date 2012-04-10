package com.siondream.freegemas;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;

public class Board {
	
	private Square[][] _squares;
	
	public Board() {
		_squares = new Square[8][8];
	}
	
	public Board(Board other) {
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				_squares[i][j] = new Square(other._squares[i][j]);
			}
		}
	}
	
	public Square getSquare(int x, int y) {
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return null;
		}
		else {
			return _squares[x][y];
		}
	}
	
	public Square[][] getSquares() {
		return _squares;
	}
	
	public void swap(int x1, int y1, int x2, int y2) {
		if (x1 >= 0 && x1 < 8 &&
			y1 >= 0 && y1 < 8 &&
			x2 >= 0 && x2 < 8 &&
			y2 >= 0 && y2 < 8) {
			
			Square temp = _squares[x1][y1];
			_squares[x1][y1] = _squares[x2][y2]; 
			_squares[x2][y1] = temp;
		}
	}
	
	public void del(int x, int y) {
		if (x >= 0 && x < 8 &&
			y >= 0 && y < 8) {
			
			_squares[x][y] = new Square(Square.Type.sqEmpty);
		}
	}
	
	public void generate() {
		boolean repeat = false;
		
		do {
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					_squares[i][j] = new Square(Square.numToType(MathUtils.random(0, 6)));
	                _squares[i][j].mustFall = true;
	                _squares[i][j].origY = MathUtils.random(-7, -1);
	                _squares[i][j].destY = j - _squares[i][j].origY;
				}
			}
			
			/*if (!check().isEmpty()) {
				repeat = true;
			}
			
			else */if (solutions().isEmpty()) {
				repeat = true;
			}
		} while(repeat);
	}
	
	public void calcFallMovements() {
		for (int x = 0; x < 8; ++x) {
			// From bottom to top
			for (int y = 7; y >= 0; --y) {
				// origY stores the initial position in the fall
				_squares[x][y].origY = y;
				
				if (_squares[x][y].equals(Square.Type.sqEmpty)) {
					for (int k = y - 1; k >= 0; --k) {
						_squares[x][k].mustFall = true;
						_squares[x][k].destY++;
					}
				}
			}
		}
	}
	
	public void applyFall() {
		for (int x = 0; x < 8; ++x) {
			// From bottom to top in order not to overwrite squares
			for (int y = 7; y >= 0; --y) {
				if (_squares[x][y].mustFall && !_squares[x][y].equals(Square.Type.sqEmpty)) {
					int y0 = _squares[x][y].destY;
					
					_squares[x][y + y0] = _squares[x][y];
					_squares[x][y].setType(Square.Type.sqEmpty);
				}
			}
		}
	}
	
	public void fillSpaces() {
		for(int x = 0; x < 8; ++x){
	        // Count how many jumps do we have to fall
	        int jumps = 0;

	        for(int y = 0; y < 8; ++y){
	            if(!_squares[x][y].equals(Square.Type.sqEmpty)) {
	            	break;
	            }
	            ++jumps;
	        }

	        for(int y = 0; y < 8; ++y){
	            if(_squares[x][y].equals(Square.Type.sqEmpty)) {
	                _squares[x][y].setType(Square.numToType(MathUtils.random(0, 7)));
	                _squares[x][y].mustFall = true;  
	                _squares[x][y].origY = y - jumps;
	                _squares[x][y].destY = jumps;
	            }       
	        }
	    }   
	}
	
	public MultipleMatch check() {
	    int k;

	    MultipleMatch matches = new MultipleMatch();    

	    // First, we check each row (horizontal)
	    for(int y = 0; y < 8; ++y){

	        for(int x = 0; x < 6; ++x){

	            Match currentRow = new Match();
	            currentRow.add(new Coord(x, y));

	            for(k = x + 1; k < 8; ++k){
	                if(_squares[x][y].equals(_squares[k][y]) && !_squares[x][y].equals(Square.Type.sqEmpty)){
	                    currentRow.add(new Coord(k, y));
	                }
	                else{
	                    break;
	                }
	            }

	            if(currentRow.size() > 2){
	                matches.add(currentRow);
	            }

	            x = k - 1;
	        }   
	    }

	    for(int x = 0; x < 8; ++x){
	        for(int y = 0; y < 6; ++y){

	            Match currentColumn = new Match();
	            currentColumn.add(new Coord(x, y));

	            for(k = y + 1; k < 8; ++k){
	                if(_squares[x][y].equals(_squares[x][k]) && !_squares[x][y].equals(Square.Type.sqEmpty)){
	                    currentColumn.add(new Coord(x, k));
	                }
	                else{
	                    break;
	                }
	            }

	            if(currentColumn.size() > 2){
	                matches.add(currentColumn);
	            }

	            y = k - 1;
	        }
	    }

	    return matches;
	}
	
	public ArrayList<Coord> solutions() {
	    ArrayList<Coord> results = new ArrayList<Coord>();
	    
	    if(!check().isEmpty()){
	        results.add(new Coord(-1, -1));
	        return results;
	    }

	    /* 
	       Check all possible boards
	       (49 * 4) + (32 * 2) although there are many duplicates
	    */
	    Board temp = this;
	    for(int x = 0; x < 8; ++x){
	        for(int y = 0; y < 8; ++y){
	        
	            // Swap with the one above and check
	            if(y > 0){
	                temp.swap(x, y, x, y - 1);
	                if(!temp.check().isEmpty()){
	                    results.add(new Coord(x, y));
	                }
	                temp.swap(x, y, x, y - 1);
	            }

	            // Swap with the one below and check
	            if(y < 7){
	                temp.swap(x, y, x, y+1);
	                if(!temp.check().isEmpty()){
	                    results.add(new Coord(x, y));
	                }
	                temp.swap(x, y, x, y + 1);
	            }

	            // Swap with the one on the left and check
	            if(x > 0){
	                temp.swap(x, y, x - 1, y);
	                if(!temp.check().isEmpty()){
	                    results.add(new Coord(x, y));
	                }
	                temp.swap(x, y, x - 1, y);
	            }

	            // Swap with the one on the right and check
	            if(x < 7){
	                temp.swap(x, y, x + 1, y);
	                if(!temp.check().isEmpty()){
	                    results.add(new Coord(x, y));
	                }
	                temp.swap(x, y, x + 1, y);
	            }
	        }
	    }

	    return results;
	}
	
	public void endAnimation() {
		for(int x = 0; x < 8; ++x){
	        for(int y = 0; y < 8; ++y){
	            _squares[x][y].mustFall = false;
	            _squares[x][y].origY = y;
	            _squares[x][y].destY = 0;
	        }
	    }
	}
}
