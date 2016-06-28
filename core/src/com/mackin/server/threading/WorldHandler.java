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
import com.mackin.managers.SpaceContactListener;
import com.mackin.projectile.Projectile;
import com.mackin.server.Server;
import com.mackin.ship.body.EnemyShipBody;
import com.mackin.ship.body.FriendlyShipBody;
import com.mackin.ship.body.ShipBody;

public class WorldHandler implements Runnable 
{
	private static final long STEP_TIME = 1/60;
	private static final float WORLD_STEP = (float) (1.0 / 60.0);
	private static final String ID = "game_world";
	private Server server;
	private World world;
	private boolean running;
	
	private Map<String, FriendlyShipBody> players;
	private Map<String, EnemyShipBody> enemies;
	private ArrayList<Projectile> projectiles;
	
	public WorldHandler(Server server, World world)
	{
		this.world = world;
		this.server = server;
		players = new HashMap<String, FriendlyShipBody>();
		enemies = new HashMap<String, EnemyShipBody>();
		projectiles = new ArrayList<Projectile>();
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
					entry.getValue().getPositionX() + " " + entry.getValue().getPositionY());
				}
				for(Entry<String, EnemyShipBody> entry: enemies.entrySet())
				{
					entry.getValue().update(timer);
					server.tellEveryone(ID, "UPDATE_ENEMY " + entry.getKey() + " " + 
					entry.getValue().getPositionX() + " " + entry.getValue().getPositionY());
				}
				for(int i = 0; i < projectiles.size(); i++)
				{
					server.tellEveryone(ID, "UPDATE_PROJECTILE " + i + " " + projectiles.get(i).getX() + " " + projectiles.get(i).getY());
				}
				
				timer = 0;
			}
			timer+= (System.currentTimeMillis() - startTime);

		}
	}
	
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
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP;


		fdef.shape = southZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape northZone = new PolygonShape();
		northZone.setAsBox(AgalagGame.V_WIDTH / (2 * AgalagGame.PPM), 2 / AgalagGame.PPM, 
							new Vector2(0, (.5F * AgalagGame.V_HEIGHT) / AgalagGame.PPM), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP;

		fdef.shape = northZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape eastZone = new PolygonShape();
		eastZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2((.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP;


		fdef.shape = eastZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape westZone = new PolygonShape();
		westZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2(-(.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.filter.categoryBits = SpaceContactListener.WALLS;
		fdef.filter.maskBits = SpaceContactListener.FRIENDLY_SHIP;


		fdef.shape = westZone;
		body.createFixture(fdef);
	}

	public void createPlayer(String id, float x, float y) 
	{
		System.out.println("[INTERNAL] Created player in world.");
		players.put(id, new FriendlyShipBody(world, x, y));
	}

	public ShipBody getPlayers(String id) 
	{
		return players.get(id);
	}

	public void userInput(String id, String keyPressed, String keyVal) 
	{
		if(keyPressed.equals("4"))
		{
			Projectile newBullet = new Projectile(world, players.get(id));
			projectiles.add(newBullet);
			server.tellEveryone(ID, "PROJECTILE " + (projectiles.size()-1) + " " + newBullet.getX() + " " + newBullet.getY());
		}
		else 
			players.get(id).moveBody(keyPressed, keyVal);
	}
	
	public void createEnemy(String id, float x, float y, int aistyle) 
	{
		System.out.println("[INTERNAL] Created enemy in world.");
		enemies.put(id, new EnemyShipBody(world, x, y, aistyle));
	}

	public Map<String, EnemyShipBody> getAllEnemies() 
	{
		return enemies;
	}

	public EnemyShipBody getEnemy(String enemyID) 
	{
		return enemies.get(enemyID);
	}
}
