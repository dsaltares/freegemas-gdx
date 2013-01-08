package com.siondream.freegemas;

import com.badlogic.gdx.utils.Array;

public class Match extends Array<Coord> {
	public Match() {
		super();
	}
	
	public boolean isMatched(Coord c) {
		return contains(c, false);
	}
	
	public Coord getMidSquare() {
		if (size > 0)
		{
			return get(size / 2);
		}
		
		return null;
	}
	
	public String toString() {
		String string = "Matches: ";
		
		for (int i = 0; i < size; ++i) {
			string += "(" + get(i).x + ", " + get(i).y + ")"; 
		}
		
		return string;
	}
}
