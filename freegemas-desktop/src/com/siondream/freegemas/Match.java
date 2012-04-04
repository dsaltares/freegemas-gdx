package com.siondream.freegemas;

import java.util.ArrayList;

public class Match extends ArrayList<Coord> {
	
	private static final long serialVersionUID = 1L;

	public Match() {
		super();
	}
	
	public boolean isMatched(Coord c) {
		return false;
	}
	
	public Coord getMidSquare() {
		return get(size() / 2);
	}
}
