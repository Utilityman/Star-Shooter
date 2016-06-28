package com.mackin.server.threading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.mackin.server.Server;

public class ClientHandler implements Runnable 
{
	private static final String ID = "game_handler";
	
	private Server server;
	
	private Socket clientSocket;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	
	//private ArrayList<ClientHandler> clientThreads;
	
	private boolean running;
	private boolean verified;
	
	// PlayerDetails
	private String id;
	//private float x;
	//private float y;
	
	public ClientHandler(Socket clientSocket, String id, Server server, ArrayList<ClientHandler> clientThreads) 
	{
		this.id = id;
		this.clientSocket = clientSocket;
		this.server = server;
		verified = false;
		//this.clientThreads = clientThreads;
		//x = 0;
		//y = 0;
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

			while(running && (incomingMessage = fromClient.readLine()) != null)
			{
				// try/catch socket exception 
				if(!incomingMessage.contains("UPDATE") && !incomingMessage.contains("INPUT"))
				System.out.println("[SERVER] Received: " + incomingMessage);
				
				
				reply(incomingMessage);

			}
			
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
			tellClient("INIT " + id +" " + 0+ " " + 0);
			server.getWorld().createPlayer(id, 0, 0);
			server.tellEveryone(ID, "PLAYER " + id+ " " + 0+ " " + 0);
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
		return server.getWorld().getPlayers(id).getPositionX();
	}
	
	public float getY()
	{
		return server.getWorld().getPlayers(id).getPositionY();
	}

	public boolean isVerified() 
	{
		return verified;
	}

}
