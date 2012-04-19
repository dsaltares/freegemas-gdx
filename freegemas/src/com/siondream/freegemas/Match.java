package com.siondream.freegemas;

import java.util.ArrayList;

public class Match extends ArrayList<Coord> {
	
	private static final long serialVersionUID = 1L;

	public Match() {
		super();
	}
	
	public boolean isMatched(Coord c) {
		return contains(c);
	}
	
	public Coord getMidSquare() {
		if (size() > 0)
		{
			return get(size() / 2);
		}
		
		return null;
	}
	
	public String toString() {
		String string = new String("Matches: ");
		
		for (int i = 0; i < size(); ++i) {
			string += new String("(" + get(i).x + ", " + get(i).y + ")"); 
		}
		
		return string;
	}
}
