package com.mackin.server.threading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.mackin.body.projectile.Projectile;
import com.mackin.body.ship.EnemyShipBody;
import com.mackin.server.Server;

public class ClientHandler implements Runnable 
{
	@SuppressWarnings("unused")
	private static final String ID = "client_handler";
	
	private Server server;
	
	private Socket clientSocket;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	
	//private ArrayList<ClientHandler> clientThreads;
	
	private boolean running;
	private boolean verified;
	private boolean ready;
	
	private String id;
	
	private TimeoutWatcher timer;
	
	public ClientHandler(Socket clientSocket, String id, Server server, ArrayList<ClientHandler> clientThreads) 
	{
		this.id = id;
		this.clientSocket = clientSocket;
		this.server = server;
		verified = false;
		ready = false;
		timer = new TimeoutWatcher();
	}

	@Override
	public void run() 
	{
		String incomingMessage;
		running = true;
		try
		{
			this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.toClient = new PrintWriter(clientSocket.getOutputStream());
			
			new Thread(timer).start();

			while(running && !timer.isTriggered() && (incomingMessage = fromClient.readLine()) != null)
			{
				timer.reset();
				if(!incomingMessage.contains("UPDATE") && !incomingMessage.contains("INPUT"))
					System.out.println("[SERVER] Received: " + incomingMessage);	
				
				reply(incomingMessage);
			}
			
			if(timer.isTriggered())
				System.out.println("hey");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void reply(String incomingMessage)
	{
		String[] params = incomingMessage.split("\\s+");

		if(params[0].equals("LOGIN"))
		{
			if(params.length == 2 && params[1].equals(id))
			{
				verified = true;
				tellClient("VERIFIED");
			}
			else
				tellClient("HELLO " + id);
		}
		if(params[0].equals("INIT"))
		{
			server.getWorld().createPlayer(id, 0, 0);
		}
		else if(params[0].equals("GET_PLAYERS"))
		{
			for(int i = 0; i < server.getClients().size(); i++)
			{
				if(!server.getClients().get(i).getID().equals(id))
					tellClient("PLAYER " + server.getClients().get(i).getID() + " " + 
					server.getClients().get(i).getX() + " " + server.getClients().get(i).getY());
			}
		}
		else if(params[0].equals("GET_ENEMIES"))
		{
			for(Entry<String, EnemyShipBody> entry: server.getWorld().getAllEnemies().entrySet())
			{
				tellClient("DEPLOY " + entry.getKey() + " texture " + 
				entry.getValue().getX() + " " + entry.getValue().getY());
			}		
		}
		else if(params[0].equals("GET_PROJECTILES"))
		{
			for(Entry<String, Projectile> entry: server.getWorld().getProjectiles().entrySet())
			{
				tellClient("PROJECTILE " + entry.getValue().getID() + " " + 
									entry.getValue().getX() + " " + entry.getValue().getY());
			}			
			ready = true;
		}
		else if(params[0].equals("UPDATE"))
		{
			server.tellEveryone(id, "UPDATE_PLAYER " + id + " " + getX()+ " " +getY());
		}
		else if(params[0].equals("SUBMIT_FORMAATION"))
		{
			System.out.println(incomingMessage);
		}
		else if(params[0].equals("INPUT"))
		{
			server.getWorld().userInput(id, params[1], params[2]);
		}
	}
	
	public void tellClient(String message)
	{
		toClient.println(message);
		toClient.flush();
	}
	
	public String getID()
	{
		return id;
	}
	
	public float getX()
	{
		return server.getWorld().getPlayers(id).getX();
	}
	
	public float getY()
	{
		return server.getWorld().getPlayers(id).getY();
	}

	public boolean isVerified() 
	{
		return verified;
	}
	
	public boolean isReady()
	{
		return ready;
	}

}
