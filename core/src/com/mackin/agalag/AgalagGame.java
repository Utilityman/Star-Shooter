package com.mackin.agalag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.socket.client.IO;
import io.socket.client.Socket;

public class AgalagGame extends Game 
{
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;

	public SpriteBatch batch;
	
	private Socket socket;
	
	@Override
	public void create () 
	{
		batch = new SpriteBatch();
		connectSocket();
		
		setScreen(new Space(this, socket));
	}
	
	public void connectSocket()
	{
		try
		{
			socket = IO.socket("http://localhost:8989");
			socket.connect();
		}catch(Exception e)
		{
			System.out.println(e);
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
		batch.dispose();
	}
}
