package com.mackin.agalag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AgalagGame extends Game 
{
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;
	
	public SpriteBatch batch;
	
	@Override
	public void create () 
	{
		batch = new SpriteBatch();
		setScreen(new Space(this));

	}

	@Override
	public void render () 
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		batch.dispose();
	}
}
