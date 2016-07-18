package com.mackin.body.ship;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.body.Formable;

public abstract class ShipBody implements Formable
{
	protected static final float MAX_SPEED = .175F; 
	protected static final float IMPULSE_SPEED = .050F;

	protected Body b2body;
	protected World world;
	
	protected boolean toDestroy;
		
	public ShipBody(World world, float x, float y) 
	{
		this.world = world;
		this.toDestroy = false;
		defineBody(x, y);
	}

	protected abstract void defineBody(float x, float y);

	public float getX() 
	{
		return b2body.getPosition().x;
	}
	
	public float getY() 
	{
		return b2body.getPosition().y;
	}
	
	public void destroy()
	{
		world.destroyBody(b2body);
		toDestroy = false;
	}

	public void setToDestroy(boolean b) 
	{
		this.toDestroy = b;
	}
	
	public void update(float timer) 
	{
		if(toDestroy)
			destroy();
	}
}
