package com.siondream.freegemas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {
	private Freegemas _game;
	private float _x;
	private float _y;
	private Texture _background;
	private Texture _icon;
	private String _text;
	private BitmapFont _font;
	
	public Button(Freegemas game, float x, float y, Texture background, Texture icon, BitmapFont font, String text) {
		_game = game;
		_x = x;
		_y = y;
		_background = background;
		_icon = icon;
		_font = font;
		_text = text;
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
			_font.draw(batch, _text, _x + 20, _y + 5);
		}
	}
	
	public float getX() {
		return _x;
	}
	
	public float getY() {
		return _y;
	}
	
	public void setX(float x) {
		_x = x;
	}
	
	public void setY(float y) {
		_y = y;
	}
	
	public void setPosition(float x, float y) {
		_x = x;
		_y = y;
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		_text = text;
	}
}