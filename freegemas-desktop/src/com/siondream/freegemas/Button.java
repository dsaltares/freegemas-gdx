package com.siondream.freegemas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {
	private Freegemas _game;
	private int _x;
	private int _y;
	private Texture _background;
	private Texture _icon;
	private String _text;
	private BitmapFont _font;
	
	public Button(Freegemas game, int x, int y, String text, Texture background, Texture icon, BitmapFont font) {
		_game = game;
		_x = x;
		_y = y;
		_background = background;
		_icon = icon;
		_font = font;
		_text = text;
	}
	
	public Button(Freegemas game, int x, int y, String text) {
		_game = game;
		_x = x;
		_y = y;
		_text = text;
		_background = null;
		_icon = null;
		_font = null;
	}
	
	public void render() {
		SpriteBatch batch = _game.getSpriteBatch();

		if (_background != null)
		{
			batch.draw(_background, _x, _y);
		}
		
		if (_icon != null)
		{
			batch.draw(_icon, _x + 5, _y + 5);
		}
		
		if (_font != null)
		{
			_font.draw(batch, _text, _x + 45, _y + 32);
		}
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public void setX(int x) {
		_x = x;
	}
	
	public void setY(int y) {
		_y = y;
	}
	
	public void setPosition(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		_text = text;
	}
	
	public void setIcon(Texture icon) {
		_icon = icon;
	}
	
	public void setBackground(Texture background) {
		_background = background;
	}
	
	public void setFont(BitmapFont font) {
		_font = font;
	}
}