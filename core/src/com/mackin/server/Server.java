package com.mackin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mackin.enemypatterns.DefaultPattern;
import com.mackin.formation.AttackFormation;
import com.mackin.managers.SpaceContactListener;
import com.mackin.server.threading.ClientHandler;
import com.mackin.server.threading.GameEvents;
import com.mackin.server.threading.WorldHandler;

public class Server extends Game
{
	public static final String ID = "DOMINATRIX";
	
	private ServerSocket server; 
	private ArrayList<ClientHandler> clientThreads;
	
	private AttackFormation enemyFormation;
	
	// Server threading
	private GameEvents gameEvents;
	private WorldHandler worldHandler;
	
	// The server controls the world and simulates the physics
	// The clients receive the results
	private World world;
	
	public static void main(String[] args)
	{
	}
	@Override
	public void create() 
	{
		new Server(8989);
		
	}
	
	public Server(int port)
	{
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new SpaceContactListener());

		clientThreads = new ArrayList<ClientHandler>();
		enemyFormation = new AttackFormation(new DefaultPattern());
		try 
		{
			server = new ServerSocket(8989);
			System.out.println("[INFO] Server Established");
			boolean gameStarted = false;
			while(true)
			{
				Socket clientSocket = server.accept();
				System.out.println("[SERVER] Got a new connection");
				ClientHandler handler = new ClientHandler(clientSocket, generateID(), this, this.clientThreads);
				Thread t = new Thread(handler);
				clientThreads.add(handler);
				t.start();
				if(!gameStarted)
				{
					worldHandler = new WorldHandler(this, world);
					new Thread(worldHandler).start();
					gameEvents = new GameEvents(this, enemyFormation); 
					new Thread(gameEvents).start();
					gameStarted = true;
				}
			}

		} catch (IOException e1)
		{
			System.out.println("[SEVERE] Server could not be created!");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Player IDs 
	 * We can generate 10,300,000,000,000,000,000,000,000,000  of them (36^18)
	 * I guess there's a possibility that this method could recursively go forever...
	 * @return
	 */
	private String generateID()
	{
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String saltStr = salt.toString();
        System.out.println("[INTERNAL] Generated ID: " + saltStr);
        /*for(int i = 0; i < clientThreads.size(); i++)
        {
        	if(clientThreads.get(i).getID().equals(saltStr))
        	{
        		// This should never happen
        		System.out.println("[LOTTO] We just won the jackpot");
        		return generateID();
        	}
        }*/
        return saltStr;
	}
	
	public String generateAIID()
	{
		String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String saltStr = salt.toString();
        //System.out.println("[INTERNAL] Generated ID: " + saltStr);
        /*for(int i = 0; i < clientThreads.size(); i++)
        {
        	if(clientThreads.get(i).getID().equals(saltStr))
        	{
        		// This should never ever happen
        		System.out.println("[LOTTO] We just won the jackpot");
        		return generateID();
        	}
        }*/
        return saltStr;
	}
	
	public String generateProjectileID()
	{
		String chars = "1234567890";
		StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String saltStr = salt.toString();
        System.out.println("[INTERNAL] Generated ID: " + saltStr);
        for(int i = 0; i < clientThreads.size(); i++)
        {
        	if(clientThreads.get(i).getID().equals(saltStr))
        	{
        		// This should never ever happen
        		System.out.println("[LOTTO] We just won the jackpot");
        		return generateID();
        	}
        }
        return saltStr;
	}

	public void tellEveryone(String id, String message) 
	{
		for(int i = 0; i < clientThreads.size(); i++)
		{
			if(!clientThreads.get(i).getID().equals(id) && clientThreads.get(i).isVerified() && clientThreads.get(i).isReady())
				clientThreads.get(i).tellClient(message);
		}
	}
	
	public ArrayList<ClientHandler> getClients()
	{
		return clientThreads;
	}
	
	public WorldHandler getWorld()
	{
		return worldHandler;
	}
}
