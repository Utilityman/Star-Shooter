package com.mackin.server.threading;

import com.mackin.formation.AttackFormation;
import com.mackin.formation.Wave;
import com.mackin.server.Server;
import com.mackin.ship.shell.ShipShell;

public class GameEvents  implements Runnable
{
	private Server server;
	private AttackFormation enemyFormation;
	private static final String ID = "game_events";
	
	private static final int SEND_DELAY = 200; // Short delay to put space between ships
	
	private int count; 
	
	public GameEvents(Server server, AttackFormation enemyFormation)
	{
		this.server = server;
		this.enemyFormation = enemyFormation;
		count = 0;
	}
	
	@Override
	public void run() 
	{
		// Begin Countdown
		countdown();
		
		while(!enemyFormation.isFinished())
		{
			boolean completed = deploy(enemyFormation.getNextWave());
			if(!completed)
				;//failure
			else
				;// good job!
		}
	}
	
	private boolean deploy(Wave wave)
	{
		System.out.println("[WAVE " + count + "] Deploying new wave");
		//EnemyController controller = new EnemyController(server, wave);
		//new Thread(controller).start();
		while(wave.hasEnemies())
		{
			System.out.println("[WAVE " + count + "] Sending new strand");
			ShipShell[] enemies = wave.sendEnemies();
			for(int i = 0; i < enemies.length; i++)
			{
				try { Thread.sleep(SEND_DELAY); } catch (InterruptedException e) { e.printStackTrace(); }
				String enemyID = server.generateAIID();
				server.getWorld().createEnemy(enemyID, enemies[i].getXInit(), enemies[i].getYInit(), enemies[i].getAIStyle());
				server.tellEveryone(ID, "DEPLOY " + enemyID + " " + enemies[i].getShipTexture() + " " + 
									enemies[i].getXInit() + " " + enemies[i].getYInit());
				//controller.addEnemy(server.getWorld().getEnemy(enemyID));
			}
			try { Thread.sleep((long) wave.getDownTime()); } catch (InterruptedException e) { e.printStackTrace(); }
			
		}
		System.out.println("[WAVE " + count + "] Finished");
		count++;
		return true;
	}
	
	private void countdown()
	{
		try {
			Thread.sleep(2000);
			server.tellEveryone(ID, "DISPLAY 5");
			Thread.sleep(1000);
			server.tellEveryone(ID, "DISPLAY 4");
			Thread.sleep(1000);
			server.tellEveryone(ID, "DISPLAY 3");
			Thread.sleep(1000);
			server.tellEveryone(ID, "DISPLAY 2");
			Thread.sleep(1000);
			server.tellEveryone(ID, "DISPLAY 1");
			Thread.sleep(1500);
			server.tellEveryone(ID, "DISPLAY CLEAR");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
