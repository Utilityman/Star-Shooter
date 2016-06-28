package com.mackin.ship.body;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class ShipBody 
{
	protected static final float MAX_SPEED = .175F; 
	protected static final float IMPULSE_SPEED = .050F;

	protected Body b2body;
	protected World world;
	
	//protected Vector2 previousPosition;
	
	public ShipBody(World world, float x, float y) 
	{
		this.world = world;
		defineBody(x, y);
		//previousPosition = new Vector2(getPositionX(), getPositionY());
	}

	protected abstract void defineBody(float x, float y);

	public float getPositionX() 
	{
		return b2body.getPosition().x;
	}
	
	public float getPositionY() 
	{
		return b2body.getPosition().y;
	}
	
	public void destroy()
	{
		world.destroyBody(b2body);
	}
	
	/*public boolean hasMoved()
	{
		System.out.println("ShipBody.hasMoved() has been called"); 
		if(previousPosition.x != getPositionX() || previousPosition.y != getPositionY())
		{
			previousPosition.x = getPositionX();
			previousPosition.y = getPositionY();
			return true;
		}
		return false;
	}*/

	public void update(float timer) 
	{
		
	}
}
