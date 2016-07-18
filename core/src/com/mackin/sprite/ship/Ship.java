package com.mackin.sprite.ship;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

// TODO: get rid of world stuffs
public abstract class Ship extends Sprite
{
	protected Vector2 previousPosition;
	
	public Ship()
	{
		
	}
	
	public Ship(TextureRegion textureRegion, float xInit, float yInit)
	{
		super(textureRegion);
	}
	
	protected abstract void shoot();

	public void update(float parseFloat, float parseFloat2) 
	{
		setPosition(parseFloat - getWidth() / 2, parseFloat2 - getHeight() / 2);	
	}
	
	/*public void remove()
	{
		world.destroyBody(b2body);
	}*/

}
