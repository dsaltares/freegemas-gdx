package com.siondream.freegemas;

import java.util.ArrayList;

public class MultipleMatch extends ArrayList<Match> {
	
	private static final long serialVersionUID = 1L;

	public MultipleMatch() {
		super();
	}
	
	public boolean isMatched(Coord c) {
		int numMatches = size();
		
		for (int i = 0; i < numMatches; ++i) {
			if (get(i).isMatched(c)) {
				return true;
			}
		}
		
		return false;
	}
}
