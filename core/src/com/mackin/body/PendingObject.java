package com.mackin.body;

import java.util.Map;

import com.badlogic.gdx.physics.box2d.World;
import com.mackin.body.projectile.Projectile;
import com.mackin.body.ship.EnemyShipBody;
import com.mackin.body.ship.FriendlyShipBody;
import com.mackin.body.ship.ShipBody;
import com.mackin.server.Server;

public class PendingObject
{
	private static final String ID = "body_maker";
	
	private Class<? extends Formable> objectType;
	private ShipBody origin;
	private float x;
	private float y;
	private String id;
	private int aistyle;

	/**
	 * constructor for pending player ships
	 * @param objectType
	 * @param id
	 * @param x
	 * @param y
	 */
	public <T extends Formable> PendingObject(Class<T> objectType, String id, float x, float y) 
	{
		this.objectType = objectType;
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * constructor for pending enemy ships
	 * @param objectType
	 * @param id
	 * @param x
	 * @param y
	 * @param aistyle
	 */
	public <T extends Formable> PendingObject(Class<T> objectType, String id, float x, float y, int aistyle) 
	{
		this.objectType = objectType;
		this.id = id;
		this.x = x;
		this.y = y;
		this.aistyle = aistyle;
	}
	
	/**
	 * constructor for pending projectles
	 * @param objectType
	 * @param id
	 * @param origin
	 */
	public <T extends Formable> PendingObject(Class<T> objectType, String id, ShipBody origin)
	{
		this.objectType = objectType;
		this.id = id;
		this.origin = origin;
	}

	public void createBody(Server server, World world, Map<String, FriendlyShipBody> players, Map<String, EnemyShipBody> enemies,
			Map<String, Projectile> projectiles)
	{
		if(objectType.equals(FriendlyShipBody.class))
		{
			players.put(id, new FriendlyShipBody(world, x, y));
			server.tellEveryone(ID, "PLAYER " + id+ " " + 0+ " " + 0);
		}
		if(objectType.equals(EnemyShipBody.class))
		{
			enemies.put(id, new EnemyShipBody(world, x, y, aistyle));
			server.tellEveryone(ID, "DEPLOY " + id + " " + "texture" + " " + x+ " " + y);		
		}
		if(objectType.equals(Projectile.class))
		{
			Projectile proj = null;
			if(origin instanceof FriendlyShipBody)
				proj = new Projectile(world, (FriendlyShipBody) origin, id);
			else if(origin instanceof EnemyShipBody)
				proj =  new Projectile(world, (EnemyShipBody) origin, id);

			if(proj != null)
			{
				projectiles.put(id, proj);
				server.tellEveryone(ID, "PROJECTILE " + proj.getID() + " " + proj.getX() + " " + proj.getY());
			}
			else
			{
				try 
				{
					throw new Exception("[INTERNAL] Exception creating projectile. Owner was not friendly or enemy ship body!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
	}

}
