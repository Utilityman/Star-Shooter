package com.mackin.agalag.server;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mackin.agalag.AgalagGame;
import com.mackin.server.Server;

public class ServerLauncher 
{
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 480;
		config.height = 600;
		//config.resizable = false;
	
		new LwjglApplication(new Server(8989), config);
	}
}
