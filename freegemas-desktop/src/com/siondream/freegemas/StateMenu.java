package com.siondream.freegemas;

public class StateMenu extends State {

	public StateMenu(Freegemas parent) {
		super(parent);
	}
	
	@Override
	public boolean keyDown(int arg0) {
		_parent.changeState("StateGame");
		return false;
	}
}
