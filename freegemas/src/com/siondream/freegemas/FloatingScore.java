package com.siondream.freegemas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FloatingScore {
	
	private static final float duration = 0.75f; 
	
	private Freegemas _game;
	private BitmapFont _font;
	private String _text;
	private Vector2 _pos;
	private Vector2 _currentPos;
	private float _time;
	
	private Color _backColor = Color.BLACK.cpy();
	private Color _frontColor = Color.WHITE.cpy();
	
	public FloatingScore(Freegemas game, BitmapFont font, int score, float x, float y) {
		_game = game;
		_font = font;
		_text = new String("" + score);
		_pos = new Vector2(x, y);
		_currentPos = Vector2.Zero.cpy();
		_time = 0.0f;
	}
	
	public void draw() {
		SpriteBatch batch = _game.getSpriteBatch();
		
		_time += Gdx.graphics.getDeltaTime();
		
		float p = 1.0f - _time/duration;
		_currentPos.x = _pos.x - 12;
		_currentPos.y = _pos.y - (1.0f - p) * 20;
		Color oldFontColor = _font.getColor();
		
		_backColor.a = p;
		_frontColor.a = p;
		
		_font.setColor(_backColor);
		_font.draw(batch, _text, (int)_currentPos.x - 2, (int)_currentPos.y - 2);
		_font.draw(batch, _text, (int)_currentPos.x + 2, (int)_currentPos.y + 2);
		_font.setColor(_frontColor);
		_font.draw(batch, _text, (int)_currentPos.x, (int)_currentPos.y);
		_font.setColor(oldFontColor);
	}
	
	public boolean isFinished() {
		return _time >= duration;
	}
}
