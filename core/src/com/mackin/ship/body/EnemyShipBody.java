package com.mackin.ship.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;
import com.mackin.ai.style.AIStyle;
import com.mackin.managers.SpaceContactListener;

public class EnemyShipBody extends ShipBody
{
	protected int aiStyle;
	
	public EnemyShipBody(World world, float x, float y, int aistyle) 
	{
		super(world, x, y);
		this.aiStyle = aistyle;
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
		fdef.isSensor = true;
		fdef.filter.categoryBits = SpaceContactListener.ENEMY_SHIP;

		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP | SpaceContactListener.FRIENDLY_BULLET;
		b2body.createFixture(fdef).setUserData(this);		
	}

	public void moveBody(float delta) 
	{
		switch(aiStyle)
		{
			case AIStyle.TOP_DOWN:
				if(b2body.getLinearVelocity().y > -MAX_SPEED)
					b2body.applyLinearImpulse(new Vector2(0, -.1F), b2body.getWorldCenter(), true);
				break;
				
			default:	// Just chill there I guess
				break;
			
		}
	}
	
	@Override
	public void update(float delta)
	{
		moveBody(delta);
	}

}
