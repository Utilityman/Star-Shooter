package com.mackin.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;

public class Ship extends Sprite
{
	private Vector2 previousPosition;
	private World world;
	private Body b2body;
	
	public Ship(World world, Texture shipTexture, int xInit, int yInit)
	{
		super(new TextureRegion(shipTexture));
		this.world = world;

		this.setScale(.025f / AgalagGame.PPM);
		defineBody(xInit, yInit);
		previousPosition = new Vector2(getPositionX(), getPositionY());
	}
	
	public void update(float delta)
	{
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
	}
	
	public void handleInput(float delta)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.D) && b2body.getLinearVelocity().x < 50 / AgalagGame.PPM)
			b2body.applyLinearImpulse(.08F, 0, getPositionX(), getPositionY(), true);

		if(Gdx.input.isKeyPressed(Input.Keys.S) && b2body.getLinearVelocity().y < 50 / AgalagGame.PPM)
			b2body.applyLinearImpulse(0, .08F, getPositionX(), getPositionY(), true);
		if(Gdx.input.isKeyPressed(Input.Keys.A) && b2body.getLinearVelocity().x > -50 / AgalagGame.PPM)
			b2body.applyLinearImpulse(-.08F, 0, getPositionX(), getPositionY(), true);
		if(Gdx.input.isKeyPressed(Input.Keys.W) && b2body.getLinearVelocity().y < 50 / AgalagGame.PPM)
			b2body.applyLinearImpulse(0, .08F, getPositionX(), getPositionY(), true);

	}
	
	public void defineBody(int xInit, int yInit)
	{
		BodyDef bdef = new BodyDef();
		bdef.position.set(xInit / AgalagGame.PPM, yInit / AgalagGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(12 / AgalagGame.PPM);
		shape.setPosition(new Vector2(0, -2 / AgalagGame.PPM));
		fdef.shape = shape;
		fdef.restitution = 0F;
		b2body.createFixture(fdef);
	}

	public float getPositionX() 
	{
		return b2body.getPosition().x;
	}
	
	public float getPositionY() 
	{
		return b2body.getPosition().y;
	}
	
	public void setBodyPosition(float x, float y)
	{
		b2body.setTransform(x, y, 0);
	}
	
	public boolean hasMoved()
	{
		if(previousPosition.x != getPositionX() || previousPosition.y != getPositionY())
		{
			previousPosition.x = getPositionX();
			previousPosition.y = getPositionY();
			return true;
		}
		return false;
	}
	
	public void remove()
	{
		world.destroyBody(b2body);
	}
}
