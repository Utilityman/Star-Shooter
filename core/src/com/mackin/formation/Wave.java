package com.mackin.formation;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.utils.Queue;
import com.mackin.ship.shell.ShipShell;

public class Wave 
{
	private float downTime;
	private Queue<ShipShell> enemies;
	private int enemiesPerStrand;
	private int strands;
	private boolean active;
	
	public Wave()
	{
		enemies = new Queue<ShipShell>();
		active = true;
	}
	
	public void addEnemies(ShipShell...enemyShips)
	{
		for(int i = 0; i < enemyShips.length; i++)
		{
			enemies.addLast(enemyShips[i]);
		}
	}

	public void addEnemies(ShipShell enemyShip) 
	{
		enemies.addFirst(enemyShip);
	}
	
	public void setDownTime(float time)
	{
		this.downTime = time;
	}
	
	public void setEnemiesPerStrand(int enemiesPerStrand)
	{
		this.enemiesPerStrand = enemiesPerStrand;
	}
	
	public void setStrands(int strands)
	{
		this.strands = strands;
	}
	
	public ShipShell[] sendEnemies()
	{
		ShipShell[] deployEnemies;
		if(enemies.size >= enemiesPerStrand)
			deployEnemies = new ShipShell[enemiesPerStrand];
		else
			deployEnemies = new ShipShell[enemies.size];
		
		for(int i = 0; i < deployEnemies.length; i++)
		{
			deployEnemies[i] = enemies.removeFirst();
		}
		
		return deployEnemies;
	}
	
	public float getDownTime()
	{
		return downTime;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public boolean hasEnemies()
	{
		if(enemies.size != 0)
			return true;
		return false;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		
		try {
			json.put("strands", strands);
			json.put("enemiesPerStrand", enemiesPerStrand);
			json.put("downtime", downTime);
			for(int i = 0; i < enemies.size; i++)
			{
				System.out.println("enemy: " + i);
				json.append("enemy", enemies.get(i).toJson());
			}
			
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}
