package com.siondream.freegemas;

public class Square {
	enum Type {sqEmpty, sqWhite, sqRed, sqPurple, sqOrange, sqGreen, sqYellow, sqBlue};
	
	public int origY;
	public int destY;
	public boolean mustFall;
	private Type _type;
	
	public Square(Type type) {
		_type = type;
	}
	
	public Type getType() {
		return _type;
	}
	
	public boolean equals(Square other) {
		return other.getType() == _type;
	}
	
	public boolean equals(Type type) {
		return type == _type;
	}
}
