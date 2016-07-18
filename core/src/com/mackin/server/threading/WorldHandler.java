package com.mackin.server.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.agalag.AgalagGame;
import com.mackin.body.PendingObject;
import com.mackin.body.projectile.Projectile;
import com.mackin.body.ship.EnemyShipBody;
import com.mackin.body.ship.FriendlyShipBody;
import com.mackin.body.ship.ShipBody;
import com.mackin.managers.SpaceContactListener;
import com.mackin.server.Server;

public class WorldHandler implements Runnable 
{
	private static final long STEP_TIME = 1/60;
	private static final float WORLD_STEP = (float) (1.0 / 60.0);
	private static final String ID = "game_world";
	private Server server;
	private World world;
	private boolean running;
	
	private volatile Map<String, FriendlyShipBody> players;
	private volatile Map<String, EnemyShipBody> enemies;
	private volatile Map<String, Projectile> projectiles;
	private volatile ArrayList<PendingObject> pendingObjects;
	
	public WorldHandler(Server server, World world)
	{
		this.world = world;
		this.server = server;
		players = new HashMap<String, FriendlyShipBody>();
		enemies = new HashMap<String, EnemyShipBody>();
		projectiles = new HashMap<String, Projectile>();
		pendingObjects = new ArrayList<PendingObject>();
		createBoundaries();
	}

	@Override
	public void run() 
	{
		long timer = 0;
		running = true;
		while(running)
		{
			long startTime = System.currentTimeMillis();
			if(timer > STEP_TIME)
			{
				world.step(WORLD_STEP, 8, 3);
				for(Entry<String, FriendlyShipBody> entry: players.entrySet())
				{
					entry.getValue().update(timer);
					server.tellEveryone(ID, "UPDATE_PLAYER " + entry.getKey() + " " + 
										entry.getValue().getX() + " " + entry.getValue().getY());
				}
				for(Entry<String, EnemyShipBody> entry: enemies.entrySet())
				{
					entry.getValue().update(timer);
					server.tellEveryone(ID, "UPDATE_ENEMY " + entry.getKey() + " " + 
										entry.getValue().getX() + " " + entry.getValue().getY());
				}
				for(Entry<String, Projectile> entry: projectiles.entrySet())
				{
					server.tellEveryone(ID, "UPDATE_PROJECTILE " + entry.getValue().getID() + " " + 
										entry.getValue().getX() + " " + entry.getValue().getY());
					entry.getValue().update(timer);
				}
				
				timer = 0;
				// Objects are created after the world takes a step (after the world is unlocked)
				for(int i = 0; i < pendingObjects.size(); i++)
				{
					pendingObjects.get(i).createBody(server, world, players, enemies, projectiles);
				}
				pendingObjects.clear();
			}
			timer+= (System.currentTimeMillis() - startTime);

		}
	}
	
	/**
	 * Creates the boundaries for the game world so that players don't just leave the screen
	 */
	public void createBoundaries()
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(0, 0);
		body = world.createBody(bdef);
		
		PolygonShape southZone = new PolygonShape();
		southZone.setAsBox(AgalagGame.V_WIDTH / (2 * AgalagGame.PPM), 2 / AgalagGame.PPM, 
							new Vector2(0, -(.5F * AgalagGame.V_HEIGHT) / AgalagGame.PPM), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP | SpaceContactListener.FRIENDLY_BULLET;


		fdef.shape = southZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape northZone = new PolygonShape();
		northZone.setAsBox(AgalagGame.V_WIDTH / (2 * AgalagGame.PPM), 2 / AgalagGame.PPM, 
							new Vector2(0, (.5F * AgalagGame.V_HEIGHT) / AgalagGame.PPM), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP | SpaceContactListener.FRIENDLY_BULLET;

		fdef.shape = northZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape eastZone = new PolygonShape();
		eastZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2((.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP | SpaceContactListener.FRIENDLY_BULLET;


		fdef.shape = eastZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape westZone = new PolygonShape();
		westZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2(-(.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP | SpaceContactListener.FRIENDLY_BULLET;

		fdef.shape = westZone;
		body.createFixture(fdef);
	}

	public void createPlayer(String id, float x, float y) 
	{
		System.out.println("[INTERNAL] Created player in world.");
		pendingObjects.add(new PendingObject(FriendlyShipBody.class, id, x, y));
	}

	public ShipBody getPlayers(String id) 
	{
		return players.get(id);
	}

	public void userInput(String id, String isPressed, String keyVal) 
	{
		if(keyVal.equals("4"))
		{
			pendingObjects.add(new PendingObject(Projectile.class, server.generateProjectileID(), players.get(id)));
		}
		else 
			players.get(id).moveBody(isPressed, keyVal);
	}
	
	public void createEnemy(String id, float x, float y, int aistyle) 
	{

		System.out.println("[INTERNAL] Created enemy in world.");
		pendingObjects.add(new PendingObject(EnemyShipBody.class, id, x, y, aistyle));
		//enemies.put(id, new EnemyShipBody(world, x, y, aistyle));
	}

	public Map<String, EnemyShipBody> getAllEnemies() 
	{
		return enemies;
	}

	public EnemyShipBody getEnemy(String enemyID) 
	{
		return enemies.get(enemyID);
	}

	/**
	 * @return
	 */
	public Map<String, Projectile> getProjectiles() 
	{
		return projectiles;
	}
}
