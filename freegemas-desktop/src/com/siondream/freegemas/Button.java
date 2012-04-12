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
	private TextureRegion _icon;
	private String _text;
	private BitmapFont _font;
	private Vector2 _pos;

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
	}
	
	public Button(Freegemas game, int x, int y, String text) {
		_game = game;
		_text = text;
		_background = null;
		_icon = null;
		_font = null;
		
		_pos = new Vector2(x, y);
	}
	
	public void render() {
		SpriteBatch batch = _game.getSpriteBatch();
		
		if (_background != null)
		{
			batch.draw(_background, _pos.x, _pos.y);
		}
		
		if (_icon != null)
		{
			batch.draw(_icon, _pos.x + 10, _pos.y + 8);
		}
		
		if (_font != null)
		{
			_font.draw(batch, _text, _pos.x + 50, _pos.y + 12);
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
	}
	
	public void setFont(BitmapFont font) {
		_font = font;
	}
	
	public boolean isClicked(int mX, int mY) {
		if (mX > _pos.x &&
			mX < _pos.x + _background.getRegionWidth() &&
		    mY > _pos.y &&
		    mY < _pos.y + -_background.getRegionHeight())
		{
			return true;
		}
		else {
			return false;
		}
	}
}