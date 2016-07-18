package com.mackin.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mackin.body.projectile.Projectile;
import com.mackin.body.ship.ShipBody;

public class SpaceContactListener implements ContactListener 
{
	public static final short NOTHING = 		0x0000;
	public static final short FRIENDLY_SHIP = 	0x0001;
	public static final short ENEMY_SHIP = 		0x0002;
	public static final short FRIENDLY_BULLET = 0x0004;
	public static final short ENEMY_BULLET = 	0x0010;
	public static final short WALLS =			0x0020;

	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch(cDef)
		{
			case FRIENDLY_SHIP | FRIENDLY_SHIP:
				System.out.println("Friendly collision!");
				break;
			case FRIENDLY_SHIP | WALLS:
				System.out.println("Wall collision!");
				break;
			case FRIENDLY_SHIP | ENEMY_SHIP:
				((ShipBody) fixA.getUserData()).setToDestroy(true);
				((ShipBody) fixB.getUserData()).setToDestroy(true);
				System.out.println("BOOM");
				break;
			case ENEMY_SHIP | FRIENDLY_BULLET:
				System.out.println("Target destroyed! Get some points");
				break;
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch(cDef)
		{
			case FRIENDLY_BULLET | WALLS:
				if(fixA.getFilterData().categoryBits == FRIENDLY_BULLET)//
					((Projectile) fixA.getUserData()).setToDestroy(true);
				else
					((Projectile) fixB.getUserData()).setToDestroy(true);
				break;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		// TODO Auto-generated method stub

	}

}
