package com.mackin.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;
import com.mackin.ship.body.EnemyShipBody;
import com.mackin.ship.body.FriendlyShipBody;

public class Projectile 
{
	private Body b2body;
	
	public Projectile(World world, FriendlyShipBody origin) 
	{
		BodyDef bdef = new BodyDef();
		bdef.position.set(origin.getPositionX(), origin.getPositionY());
		bdef.type = BodyDef.BodyType.KinematicBody;
		b2body = world.createBody(bdef);	
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(4 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0,15F / AgalagGame.PPM ));
		fdef.shape = shape;
		fdef.restitution = 0F;
		b2body.createFixture(fdef).setUserData(origin);
		b2body.setLinearVelocity(new Vector2(0, 3F));
	}
	
	public Projectile(World world, EnemyShipBody origin)
	{
		BodyDef bdef = new BodyDef();
		bdef.position.set(origin.getPositionX(), origin.getPositionY());
		bdef.type = BodyDef.BodyType.KinematicBody;
		b2body = world.createBody(bdef);	
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(4 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0,15F / AgalagGame.PPM ));
		fdef.shape = shape;
		fdef.restitution = 0F;
		b2body.createFixture(fdef).setUserData(origin);
		b2body.setLinearVelocity(new Vector2(0, -3F));
	}

	public float getX() 
	{
		return b2body.getPosition().x;
	}
	
	public float getY() 
	{
		return b2body.getPosition().y;
	}

}
