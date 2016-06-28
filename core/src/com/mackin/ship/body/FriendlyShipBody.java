package com.mackin.ship.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;
import com.mackin.managers.SpaceContactListener;

public class FriendlyShipBody extends ShipBody {

	public FriendlyShipBody(World world, float x, float y) 
	{
		super(world, x, y);
	}

	@Override
	protected void defineBody(float x, float y) 
	{
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(12 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0, -2 / AgalagGame.PPM));
		fdef.shape = shape;
		fdef.restitution = 0F;
		fdef.filter.categoryBits = SpaceContactListener.FRIENDLY_SHIP;

		fdef.filter.maskBits = SpaceContactListener.ENEMY_BULLET | SpaceContactListener.ENEMY_SHIP | SpaceContactListener.WALLS;
		b2body.createFixture(fdef).setUserData(this);
	}
	
	public void moveBody(String keyPressed, String keyVal)
	{
		if(keyVal.equals("3"))
		{
			if(keyPressed.equals("1") && b2body.getLinearVelocity().x < MAX_SPEED)
				b2body.applyLinearImpulse(new Vector2(MAX_SPEED, 0), b2body.getWorldCenter(), true);
			else if(keyPressed.equals("0") && b2body.getLinearVelocity().x > 0)
				b2body.setLinearVelocity(new Vector2(0, b2body.getLinearVelocity().y));
		}
		else if(keyVal.equals("1"))
		{
			if(keyPressed.equals("1") && b2body.getLinearVelocity().x > -MAX_SPEED)
				b2body.applyLinearImpulse(new Vector2(-MAX_SPEED, 0), b2body.getWorldCenter(), true);
			else if(b2body.getLinearVelocity().x < 0 && keyPressed.equals("0"))
				b2body.setLinearVelocity(new Vector2(0, b2body.getLinearVelocity().y));
		}
		else if(keyVal.equals("0"))
		{
			if(keyPressed.equals("1") && b2body.getLinearVelocity().y < MAX_SPEED)
				b2body.applyLinearImpulse(new Vector2(0, MAX_SPEED), b2body.getWorldCenter(), true);
			else if(b2body.getLinearVelocity().y > 0 && keyPressed.equals("0"))
				b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 0));
		}
		else if(keyVal.equals("2"))
		{
			if(keyPressed.equals("1") && b2body.getLinearVelocity().y > -MAX_SPEED)
				b2body.applyLinearImpulse(new Vector2(0, -MAX_SPEED), b2body.getWorldCenter(), true);
			else if(b2body.getLinearVelocity().y < 0 && keyPressed.equals("0"))
				b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 0));
		}
	}
	

}
