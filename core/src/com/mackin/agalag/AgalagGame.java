package com.mackin.agalag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class AgalagGame extends Game 
{
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;

	public SpriteBatch batch;
	
	private PrintWriter toServer = null;

	@Override
	public void create () 
	{
		batch = new SpriteBatch();
		//connectSocket();
		Socket socket = null;
		BufferedReader fromServer = null;

		boolean connected = false;

		try {
			socket = new Socket("localhost", 8989);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(socket.getOutputStream());
			connected = true;
			
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to the server!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not connect to the server!");
			e.printStackTrace();
		}
		
		if(connected)
		{
			setScreen(new Space(this, socket, fromServer, login(toServer, fromServer)));
		}
	}

	@Override
	public void render () 
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		this.screen.dispose();
		batch.dispose();
	}
	
	/** 
	 *	We can tell the server stuff
	 * @param message
	 */
	public void tellServer(String message)
	{
		toServer.println(message);
		toServer.flush();
	}
	
	private String login(PrintWriter toServer, BufferedReader fromServer)
	{
		String id = null;
		String incomingMessage;
		try
		{
			tellServer("LOGIN");
			boolean verifying = true;
			while(verifying && (incomingMessage = fromServer.readLine()) != null)
			{
				Gdx.app.log("[CLIENT] Loging In", incomingMessage);
				if(incomingMessage.contains("HELLO"))
				{
					id = incomingMessage.substring(6);
					tellServer("LOGIN " + id);
				}
				if(incomingMessage.equals("VERIFIED"))
					verifying = false;
					
			}
		}catch(IOException e)
		{
			
		}
		
		return id;
	}
}
