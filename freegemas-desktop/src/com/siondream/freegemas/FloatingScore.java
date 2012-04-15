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
	private float _time;
	
	public FloatingScore(Freegemas game, BitmapFont font, int score, float x, float y) {
		_game = game;
		_font = font;
		_text = new String("" + score);
		_pos = new Vector2(x, y);
		_time = 0.0f;
	}
	
	public void draw() {
		SpriteBatch batch = _game.getSpriteBatch();
		
		_time += Gdx.graphics.getDeltaTime();
		
		float p = 1.0f - _time/duration;
		Vector2 currentPos = new Vector2(_pos.x - 12, _pos.y - (1.0f - p) * 20);
		Color oldFontColor = _font.getColor();
		
		_font.setColor(new Color(0.0f, 0.0f, 0.0f, p));
		_font.draw(batch, _text, (int)currentPos.x - 2, (int)currentPos.y - 2);
		_font.draw(batch, _text, (int)currentPos.x + 2, (int)currentPos.y + 2);
		_font.setColor(new Color(1.0f, 1.0f, 1.0f, p));
		_font.draw(batch, _text, (int)currentPos.x, (int)currentPos.y);
		_font.setColor(oldFontColor);
	}
	
	public boolean isFinished() {
		return _time >= duration;
	}
}
