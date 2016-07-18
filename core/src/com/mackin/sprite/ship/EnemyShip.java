package com.mackin.sprite.ship;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mackin.agalag.AgalagGame;

public class EnemyShip extends Ship
{
	private AgalagGame game;
	private float shootDelay;
	
	/**
	 * Empty constructor for ships that are not initially put into the game
	 */
	public EnemyShip(AgalagGame game, Texture texture, float xInit, float yInit) 
	{
		super(new TextureRegion(texture), xInit, yInit);
		this.setScale(.025f / AgalagGame.PPM);
		this.setColor(1.0F, .2F, .2F, 1.0F);
		this.game = game;
		shootDelay = 0;
	}


	@Override
	public void shoot() 
	{
		
	}
}
