package com.siondream.freegemas;

public class Square  {
	public enum Type {sqEmpty,
					  sqWhite,
					  sqRed,
					  sqPurple,
					  sqOrange,
					  sqGreen,
					  sqYellow,
					  sqBlue};
	
	public int origY;
	public int destY;
	public boolean mustFall;
	private Type _type;
	
	public Square(Type type) {
		_type = type;
	}
	
	public Square(Square other) {
		_type = other._type;
		origY = other.origY;
		destY = other.destY;
		mustFall = other.mustFall;
	}
	
	public Type getType() {
		return _type;
	}
	
	public void setType(Type type) {
		_type = type;
	}
	
	public boolean equals(Square other) {
		return other.getType() == _type;
	}
	
	public boolean equals(Type type) {
		return type == _type;
	}
	
	public static Type numToType(int num) {
		switch (num) {
		case 0:
			return Type.sqWhite;
		case 1:
			return Type.sqRed;
		case 2:
			return Type.sqPurple;
		case 3:
			return Type.sqOrange;
		case 4:
			return Type.sqGreen;
		case 5:
			return Type.sqYellow;
		case 6:
			return Type.sqBlue;
		default:
			return Type.sqEmpty;
		}
	}
}
