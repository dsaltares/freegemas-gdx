package com.siondream.freegemas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Button {
	private Freegemas _game;
	private TextureRegion _background;
	private TextureRegion _backgroundClicked;
	private TextureRegion _icon;
	private String _text;
	private BitmapFont _font;
	private Vector2 _pos;
	private int _width;
	private int _height;
	private boolean _clicked;

	public Button(Freegemas game,
				  int x,
				  int y,
				  String text,
				  TextureRegion background,
				  TextureRegion icon,
				  BitmapFont font) {
		_game = game;
		_background = background;
		_icon = icon;
		_font = font;
		_text = text;
		_pos = new Vector2(x, y);
		_width = 0;
		_height = 0;
		_clicked = false;
	}
	
	public Button(Freegemas game, int x, int y, String text) {
		_game = game;
		_text = text;
		_background = null;
		_icon = null;
		_font = null;
		_clicked = false;
		
		_pos = new Vector2(x, y);
	}
	
	public void render() {
		SpriteBatch batch = _game.getSpriteBatch();
		
		if (!_clicked && _background != null)
		{
			batch.draw(_background, _pos.x, _pos.y);
		}
		else if (_backgroundClicked != null)
		{
			batch.draw(_backgroundClicked, _pos.x, _pos.y);
		}
		
		if (_icon != null)
		{
			batch.draw(_icon, _pos.x + 15, _pos.y - (_background.getRegionHeight() - _icon.getRegionHeight()) / 2);
		}
		
		if (_font != null)
		{
			_font.draw(batch, _text, _pos.x + 60, _pos.y + 25);
		}
	}
	
	public int getX() {
		return (int)_pos.x;
	}
	
	public int getY() {
		return (int)_pos.y;
	}
	
	public void setX(int x) {
		_pos.x = x;
	}
	
	public void setY(int y) {
		_pos.y = y;
	}
	
	public void setPosition(int x, int y) {
		_pos.x = x;
		_pos.y = y;
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		_text = text;
	}
	
	public void setIcon(TextureRegion icon) {
		_icon = icon;
	}
	
	public void setBackground(TextureRegion background) {
		_background = background;
		_width = _background.getRegionWidth();
		_height = -_background.getRegionHeight();
	}
	
	public void setBackgroundClicked(TextureRegion backgroundClicked) {
		_backgroundClicked = backgroundClicked;
	}
	
	public void setFont(BitmapFont font) {
		_font = font;
	}
	
	public boolean isClicked(int mX, int mY) {
		if (mX > _pos.x &&
			mX < _pos.x + _width &&
		    mY > _pos.y &&
		    mY < _pos.y + _height)
		{
			_clicked = true;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void touchUp() {
		_clicked = false;
	}
}