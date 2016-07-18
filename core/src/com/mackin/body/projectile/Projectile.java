package com.mackin.body.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;
import com.mackin.body.Formable;
import com.mackin.body.ship.EnemyShipBody;
import com.mackin.body.ship.FriendlyShipBody;
import com.mackin.managers.SpaceContactListener;

public class Projectile implements Formable
{
	private Body b2body;
	private String id;
	private World world;
	private boolean toDestroy;
	
	public Projectile(World world, FriendlyShipBody origin, String id) 
	{
		this.id = id;
		this.world = world;
		this.toDestroy = false;
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(origin.getX(), origin.getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);	
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(4 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0,15F / AgalagGame.PPM ));
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = SpaceContactListener.FRIENDLY_BULLET;
		fdef.filter.maskBits = SpaceContactListener.ENEMY_BULLET | SpaceContactListener.ENEMY_SHIP | SpaceContactListener.WALLS;

		b2body.createFixture(fdef).setUserData(this);
		b2body.setLinearVelocity(new Vector2(0, 1F));
	}
	
	public Projectile(World world, EnemyShipBody origin, String id)
	{
		this.id = id;
		this.toDestroy = false;
		BodyDef bdef = new BodyDef();
		bdef.position.set(origin.getX(), origin.getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);	
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(4 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0,15F / AgalagGame.PPM ));
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = SpaceContactListener.ENEMY_BULLET;
		fdef.filter.maskBits = SpaceContactListener.ENEMY_BULLET | SpaceContactListener.ENEMY_SHIP | SpaceContactListener.WALLS;
		b2body.createFixture(fdef).setUserData(this);
		b2body.setLinearVelocity(new Vector2(0, -1F));
	}

	public float getX() 
	{
		return b2body.getPosition().x;
	}
	
	public float getY() 
	{
		return b2body.getPosition().y;
	}
	
	public void update(float delta)
	{
		if(toDestroy)
		{
			destroy();
		}
	}
	
	public String getID()
	{
		return id;
	}

	/**
	 * 
	 */
	public void destroy() 
	{
		System.out.println("destroying the bullet");
		toDestroy = false;
		world.destroyBody(b2body);
	}

	/**
	 * @param b - whether or not the object needs to be destroyed
	 */
	public void setToDestroy(boolean b) 
	{
		this.toDestroy = b;
	}

}
