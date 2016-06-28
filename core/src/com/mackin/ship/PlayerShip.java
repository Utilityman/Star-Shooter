package com.mackin.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mackin.agalag.AgalagGame;

public class PlayerShip extends Ship
{	
	private static final float SHOOT_DELAY = .5F;
	private float shootTimer;
	private AgalagGame game;
	
	public PlayerShip(AgalagGame game, Texture texture, float xInit, float yInit) 
	{
		super(new TextureRegion(texture), xInit, yInit);
		this.setScale(.025f / AgalagGame.PPM);
		this.game = game;
		shootTimer = 0;
	}

	/**
	 * D - 3, move right
	 * W - 0, move up
	 * A - 1, move left
	 * S - 2, move down
	 * SPACE - 4, shoot
	 * @param delta to keep track of the shoot timer
	 */
	public void handleInput(float delta)
	{
		shootTimer += delta;
		// Movement
		if(Gdx.input.isKeyPressed(Input.Keys.D))
			game.tellServer("INPUT 1 3");//b2body.applyLinearImpulse(new Vector2(.25F, 0), b2body.getWorldCenter(), true);
		else if(!Gdx.input.isKeyPressed(Input.Keys.D))
			game.tellServer("INPUT 0 3");//b2body.applyLinearImpulse(new Vector2(-.25F, 0),  b2body.getWorldCenter(), true);
		
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			game.tellServer("INPUT 1 0");
		else if(!Gdx.input.isKeyPressed(Input.Keys.W))
			game.tellServer("INPUT 0 0");
		
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			game.tellServer("INPUT 1 1");
		else if(!Gdx.input.isKeyPressed(Input.Keys.A))
			game.tellServer("INPUT 0 1");
		
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			game.tellServer("INPUT 1 2");
		else if(!Gdx.input.isKeyPressed(Input.Keys.S))
			game.tellServer("INPUT 0 2");
		
		if(shootTimer >= SHOOT_DELAY && Gdx.input.isKeyPressed(Input.Keys.SPACE))
		{
			shootTimer = 0;
			System.out.println("pew");
			game.tellServer("INPUT 1 4");
		}
	}
	
	protected void shoot()
	{
		System.out.println("pew!");
	}
}
