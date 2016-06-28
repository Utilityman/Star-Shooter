package com.mackin.agalag;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;

public class ServerReader implements Runnable
{
	private boolean running;
	private BufferedReader fromServer;
	private Space game;
	
	public ServerReader(Space game, BufferedReader fromServer)
	{
		this.game = game;
		this.fromServer = fromServer;
	}
	
	@Override
	public void run() 
	{
		String incomingMessage;
		running = true;
		try
		{
			while(running && (incomingMessage = fromServer.readLine()) != null)
			{
				if(!incomingMessage.contains("UPDATE"))
					Gdx.app.log("[CLIENT] Received", incomingMessage);
				//System.out.println("[CLIENT] Received: " + incomingMessage);
				game.handle(incomingMessage);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}

}
